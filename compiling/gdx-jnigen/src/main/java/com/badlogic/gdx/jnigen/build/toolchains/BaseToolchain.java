package com.badlogic.gdx.jnigen.build.toolchains;

import com.badlogic.gdx.jnigen.AntPathMatcher;
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
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseToolchain {

    private static final Logger logger = LoggerFactory.getLogger(BaseToolchain.class);

    protected RuntimeEnv ENV = new RuntimeEnv();
    protected BuildConfig config;
    protected BuildTarget target;

    private List<File> sourceJniGeneratedCFiles;
    private List<File> sourceJniGeneratedCPPFiles;

    private List<File> sourceIncludedCFiles;
    private List<File> sourceIncludedCPPFiles;

    protected List<File> buildCFiles;
    protected List<File> buildCppFiles;

    protected File buildDirectory;
    protected File libsDirectory;
    protected File targetLibFile;

    protected List<Callable<Void>> compilationTasks;

    BaseToolchain () {
        compilationTasks = new ArrayList<>();
    }

    public void configure (BuildTarget target, BuildConfig config) {
        this.target = target;
        this.config = config;
        this.buildDirectory = config.buildDir.child(target.getTargetFolder()).file();
        this.libsDirectory = config.libsDir.child(target.getTargetFolder()).file();

        this.targetLibFile = target.getTargetBinaryFile(config);

    }

    public abstract void checkForTools ();

    public void build () {
        checkForTools();

        collectCFiles();
        collectCPPFiles();
        cleanBuildDirectory();
        createBuildDirectory();

        collectCompilationTasks();
        compileTasks();

        link();
        strip();
    }

    protected void compileTasks () {
        if (config.multiThreadedCompile) {
            logger.info("Compiling in multithreaded mode");

            int threads = Runtime.getRuntime().availableProcessors();
            ExecutorService executorService = Executors.newFixedThreadPool(threads);

            ArrayList<Future<Void>> futures = new ArrayList<>();

            for (Callable<Void> compilationTask : compilationTasks) {
                Future<Void> submit = executorService.submit(compilationTask);
                futures.add(submit);
            }

            //Wait for all futures to complete
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    logger.error("Failure to compile task", e);
                    throw new RuntimeException("Failure to compile task", e);
                }
            }
            executorService.shutdown();


        } else {
            logger.info("Compiling in single threaded mode");

            for (Callable<Void> task : compilationTasks) {
                try {
                    task.call();
                } catch (Exception e) {
                    logger.error("Failure to compile task", e);
                    throw new RuntimeException("Failure to compile task", e);
                }
            }
        }
    }

    public abstract void collectCompilationTasks ();

    public abstract void link ();
    public abstract void strip ();

    private void collectCFiles () {
        File jniDir = config.jniDir.file();
        if (!jniDir.exists()) {
            throw new RuntimeException("Jni directory does not exist at path " + config.jniDir.file().getAbsolutePath());
        }

        String[] jniCIncludes = new String[]{"**/*.c"};
        String[] jniCExcludes = new String[]{};

        String[] cIncludes = target.cIncludes;
        String[] cExcludes = target.cExcludes;

        try {
            sourceJniGeneratedCFiles = collectFiles(jniDir.toPath(), jniCIncludes, jniCExcludes);
            sourceIncludedCFiles = collectFiles(config.projectDir.file().toPath(), cIncludes, cExcludes);
            logger.info("Collected C files {}", Stream.concat(sourceJniGeneratedCFiles.stream(), sourceIncludedCFiles.stream()).collect(
                    Collectors.toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void collectCPPFiles () {
        File jniDir = config.jniDir.file();
        if (!jniDir.exists()) {
            throw new RuntimeException("Jni directory does not exist at path " + config.jniDir.file().getAbsolutePath());
        }

        String[] jniCPPIncludes = new String[]{"**/*.cpp"};
        String[] jniCPPExcludes = new String[]{};

        String[] cppIncludes = target.cppIncludes;
        String[] cppExcludes = target.cppExcludes;

        try {
            sourceJniGeneratedCPPFiles = collectFiles(jniDir.toPath(), jniCPPIncludes, jniCPPExcludes);
            sourceIncludedCPPFiles = collectFiles(config.projectDir.file().toPath(), cppIncludes, cppExcludes);
            logger.info("Collected CPP files {}", Stream.concat(sourceJniGeneratedCPPFiles.stream(), sourceIncludedCPPFiles.stream()).collect(
                    Collectors.toList()));
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
            for (File collectedCFile : sourceJniGeneratedCFiles) {
                File targetFile = new File(buildDirectory, collectedCFile.getName());
                buildCFiles.add(targetFile);
                Files.copy(collectedCFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            for (File collectedCppFile : sourceJniGeneratedCPPFiles) {
                File targetFile = new File(buildDirectory, collectedCppFile.getName());
                buildCppFiles.add(targetFile);
                Files.copy(collectedCppFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            //for includes, we don't copy
            buildCFiles.addAll(sourceIncludedCFiles);
            buildCppFiles.addAll(sourceIncludedCPPFiles);

        } catch (IOException e) {
            logger.error("Failure to copy files into build directory", e);
            throw new RuntimeException("Failure to copy files into build directory");
        }

    }

    public static List<File> collectFiles (Path rootDir, String[] includes, String[] excludes) throws IOException {
        List<File> matchedFiles = new ArrayList<>();


        Files.walkFileTree(rootDir, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory (Path dir, BasicFileAttributes attrs) {
                if (dir == rootDir) {
                    return FileVisitResult.CONTINUE;
                }


                //Todo skip looking at directories we have no business looking at
                Path relativePath = rootDir.relativize(dir);
                String relativePathString = relativePath.toString();
                if (relativePathString.equalsIgnoreCase(".git")) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
              if (relativePathString.equalsIgnoreCase(".gradle")) {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile (Path file, BasicFileAttributes attrs) {
                Path relativePath = rootDir.relativize(file);

                String relativePathString = relativePath.toString().replace("\\", "/");


                boolean isInclude = AntPathMatcher.match(relativePathString, includes);

                if (isInclude) {
                    boolean isExclude = false;
                    if (excludes.length > 0) {
                       isExclude = AntPathMatcher.match(relativePathString, excludes);
                    }

                    if (!isExclude) {
                        logger.trace("Including file {}", file);
                        matchedFiles.add(file.toFile());
                    } else {
                        logger.trace("Ignoring file as included, but marked by exclude {}", file);
                    }
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



    protected String convertToAbsoluteRelativeTo (FileDescriptor rootRelative, String path) {
        //Path might be a relative path or an absolute, if its absolute just wrap, otherwise convert it to absolute
        //based around our project directory

        File file = new File(path);
        if (!file.isAbsolute()) {
            file = new File(rootRelative.file(), path);
        } else {
            file = file.getAbsoluteFile();
        }
        return file.getAbsolutePath();
    }

    protected Collection<String> stringFlagsToArgs (String flagsEmbeddedinString) {
        String[] split = flagsEmbeddedinString.trim().split("\\s+");
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

    public File getTargetLibFile () {
        return targetLibFile;
    }
}
