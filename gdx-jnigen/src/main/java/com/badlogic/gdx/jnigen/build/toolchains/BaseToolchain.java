package com.badlogic.gdx.jnigen.build.toolchains;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.build.RuntimeEnv;
import com.badlogic.gdx.jnigen.build.ToolchainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class BaseToolchain {

    private static final Logger logger = LoggerFactory.getLogger(BaseToolchain.class);

    protected RuntimeEnv ENV = new RuntimeEnv();
    protected BuildConfig config;
    protected BuildTarget target;
    protected List<File> sourceCollectedCFiles;
    protected List<File> sourceCollectedCppFiles;

    protected List<File> buildCFiles;
    protected List<File> buildCppFiles;

    protected File buildDirectory;
    protected File libsDirectory;
    protected File targetLibFile;

    public void configure (BuildTarget target, BuildConfig config) {
        this.target = target;
        this.config = config;
        this.buildDirectory = config.buildDir.child(target.getTargetFolder()).file();
        this.libsDirectory = config.libsDir.child(target.getTargetFolder()).file();

        this.targetLibFile = new File(libsDirectory, target.getSharedLibFilename(config.sharedLibName));

        checkForTools();
    }

    public abstract void checkForTools ();

    public void build () {
        collectCFiles();
        collectCPPFiles();
        cleanBuildDirectory();
        createBuildDirectory();

        compile();
    }

    public abstract void compile ();

    private void collectCFiles () {
        File rootDir = config.jniDir.file();
        if (!rootDir.exists()) {
            throw new RuntimeException("Jni directory does not exist at path" + config.jniDir.file().getAbsolutePath());
        }

        String[] cIncludes = target.cIncludes;
        String[] cExcludes = target.cExcludes;

        try {
            sourceCollectedCFiles = collectFiles(rootDir.toPath(), cIncludes, cExcludes);
            logger.info("Collected C files {}", sourceCollectedCFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void collectCPPFiles () {
        File rootDir = config.jniDir.file();
        if (!rootDir.exists()) {
            throw new RuntimeException("Jni directory does not exist at path " + config.jniDir.file().getAbsolutePath());
        }

        String[] cppIncludes = target.cppIncludes;
        String[] cppExcludes = target.cppExcludes;

        try {
            sourceCollectedCppFiles = collectFiles(rootDir.toPath(), cppIncludes, cppExcludes);
            logger.info("Collected CPP files {}", sourceCollectedCppFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void cleanBuildDirectory () {

        logger.info("Cleaning build directory {} - abs {}", buildDirectory, buildDirectory.getAbsolutePath());
        logger.info("Cleaning libs directory {} - abs {}", libsDirectory, libsDirectory.getAbsolutePath());

        logger.error("Not actually wiping directories yet");
    }

    private void createBuildDirectory () {
        logger.info("Creating build directory and copying source includes");

        buildDirectory.mkdirs();
        if (!buildDirectory.exists()) {
            logger.error("Failure making file {}", buildDirectory.getAbsolutePath());
            throw new RuntimeException("Failure to create build dir");
        }

        try {
            //copy the source files all over
            buildCFiles = new ArrayList<>();
            buildCppFiles = new ArrayList<>();
            for (File collectedCFile : sourceCollectedCFiles) {
                File targetFile = new File(buildDirectory, collectedCFile.getName());
                buildCFiles.add(targetFile);
                Files.copy(collectedCFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            for (File collectedCppFile : sourceCollectedCppFiles) {
                File targetFile = new File(buildDirectory, collectedCppFile.getName());
                buildCppFiles.add(targetFile);
                Files.copy(collectedCppFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            logger.error("Failure to copy files into build directory", e);
            throw new RuntimeException("Failure to copy files into build directory");
        }

    }

    public static List<File> collectFiles (Path rootDir, String[] includes, String[] excludes) throws IOException {
        List<File> matchedFiles = new ArrayList<>();

        Files.walkFileTree(rootDir, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory (Path dir, BasicFileAttributes attrs) {
                for (String exclude : excludes) {
                    if (dir.endsWith(exclude)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile (Path file, BasicFileAttributes attrs) {
                String filePath = file.toString();
                boolean include = false;
                for (String includePattern : includes) {
                    if (filePath.matches(includePattern.replace("**", ".+"))) {
                        include = true;
                        break;
                    }
                }
                if (include) {
                    matchedFiles.add(file.toFile());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed (Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory (Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });

        return matchedFiles;
    }

    protected Collection<String> stringFlagsToArgs (String flagsEmbeddedinString) {
        String[] split = flagsEmbeddedinString.split(" ");
        ArrayList<String> args = new ArrayList<>(Arrays.asList(split));
        return args;
    }

    protected ToolchainExecutor.ToolchainCallback createToolChainCallback (String tag) {
        return new ToolchainExecutor.ToolchainCallback() {
            @Override
            public void onInfoMessage (String message) {
                System.out.println(message);
            }

            @Override
            public void onErrorMessage (String message) {
                System.err.println(message);
            }

            @Override
            public void onSuccess () {
                logger.info("{} complete", tag);
            }

            @Override
            public void onFail (int statusCode) {
                logger.error("Failure to {} target {} with status {}", tag, target, statusCode);
                throw new RuntimeException("Failed to " + tag);
            }
        };
    }
}
