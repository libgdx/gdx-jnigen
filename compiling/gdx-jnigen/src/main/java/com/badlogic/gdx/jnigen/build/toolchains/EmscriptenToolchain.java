package com.badlogic.gdx.jnigen.build.toolchains;

import com.badlogic.gdx.jnigen.build.ToolFinder;
import com.badlogic.gdx.jnigen.build.ToolchainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Toolchain that compiles C/C++ to WebAssembly using Emscripten (emcc/em++).
 */
public class EmscriptenToolchain extends BaseToolchain {

    private static final Logger logger = LoggerFactory.getLogger(EmscriptenToolchain.class);

    private File cCompilerExecutable;
    private File cppCompilerExecutable;

    @Override
    public void checkForTools () {
        String cCompiler = target.cCompiler;
        String cppCompiler = target.cppCompiler;

        // Check EMSDK env var for additional PATH entries
        String emsdk = System.getenv("EMSDK");
        if (emsdk != null) {
            String emsdkUpstream = emsdk + File.separator + "upstream" + File.separator + "emscripten";
            String currentPath = System.getenv("PATH");
            if (currentPath != null && !currentPath.contains(emsdkUpstream)) {
                ENV.addToPath(emsdkUpstream);
            }
        }

        cCompilerExecutable = ToolFinder.getToolFile(cCompiler, ENV, true);
        cppCompilerExecutable = ToolFinder.getToolFile(cppCompiler, ENV, true);

        logger.info("Emscripten toolchain is valid.\nemcc: {}\nem++: {}", cCompilerExecutable, cppCompilerExecutable);
    }

    @Override
    public void collectCompilationTasks () {
        collectCCompileTasks();
        collectCPPCompileTasks();
    }

    private void collectCCompileTasks () {
        if (buildCFiles.isEmpty()) {
            logger.info("No c files, skipping c compilation");
            return;
        }

        List<String> args = new ArrayList<>();
        args.addAll(Arrays.asList(target.cFlags));

        // Include JNI headers (web/jni_md.h provides Emscripten-compatible definitions)
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/"));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/" + target.os.getJniPlatform()));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "."));
        for (String headerDir : target.headerDirs) {
            args.add("-I" + convertToAbsoluteRelativeTo(config.projectDir, headerDir));
        }

        for (File buildCFile : buildCFiles) {
            ArrayList<String> perFileArgs = new ArrayList<>(args);
            perFileArgs.add(buildCFile.getAbsolutePath());
            perFileArgs.add("-o");
            perFileArgs.add(new File(buildDirectory, toObjectFile(buildCFile.getName())).getAbsolutePath());

            compilationTasks.add(new Callable<Void>() {
                @Override
                public Void call () throws Exception {
                    logger.info("Compiling C File (Emscripten) {}", buildCFile.getAbsolutePath());
                    ToolchainExecutor.execute(cCompilerExecutable, config.jniDir.file(), perFileArgs, createToolChainCallback("Emscripten C Compile - " + buildCFile.getName()));
                    return null;
                }
            });
        }
    }

    private void collectCPPCompileTasks () {
        if (buildCppFiles.isEmpty()) {
            logger.info("No cpp files, skipping cpp compilation");
            return;
        }

        List<String> args = new ArrayList<>();
        args.addAll(Arrays.asList(target.cppFlags));

        // Include JNI headers (web/jni_md.h provides Emscripten-compatible definitions)
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/"));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/" + target.os.getJniPlatform()));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "."));
        for (String headerDir : target.headerDirs) {
            args.add("-I" + convertToAbsoluteRelativeTo(config.projectDir, headerDir));
        }

        for (File buildCppFile : buildCppFiles) {
            ArrayList<String> perFileArgs = new ArrayList<>(args);
            perFileArgs.add(buildCppFile.getAbsolutePath());
            perFileArgs.add("-o");
            perFileArgs.add(new File(buildDirectory, toObjectFile(buildCppFile.getName())).getAbsolutePath());

            compilationTasks.add(new Callable<Void>() {
                @Override
                public Void call () throws Exception {
                    logger.info("Compiling CPP File (Emscripten) {}", buildCppFile.getAbsolutePath());
                    ToolchainExecutor.execute(cppCompilerExecutable, config.jniDir.file(), perFileArgs, createToolChainCallback("Emscripten CPP Compile - " + buildCppFile.getName()));
                    return null;
                }
            });
        }
    }

    private String toObjectFile (String name) {
        String[] split = name.split("\\.");
        return split[0] + ".o";
    }

    @Override
    public void link () {
        libsDirectory.mkdirs();
        if (!libsDirectory.exists()) {
            logger.error("Error creating libs dir {}", libsDirectory.getAbsolutePath());
            throw new RuntimeException("Error creating libs dir");
        }

        ArrayList<File> objFiles = new ArrayList<>();
        for (File cObjectFile : buildCFiles) {
            objFiles.add(new File(buildDirectory, toObjectFile(cObjectFile.getName())));
        }
        for (File cppObjectFile : buildCppFiles) {
            objFiles.add(new File(buildDirectory, toObjectFile(cppObjectFile.getName())));
        }

        List<String> args = new ArrayList<>();

        // Add linker flags (contains Emscripten-specific -s flags)
        args.addAll(Arrays.asList(target.linkerFlags));

        // Generate a unique EXPORT_NAME based on the shared library name to avoid
        // clashes when multiple jnigen libraries are loaded on the same page
        String exportName = "createModule_" + sanitizeForJs(config.sharedLibName);
        args.add("-sEXPORT_NAME=" + exportName);
        logger.info("Using Emscripten EXPORT_NAME={}", exportName);

        args.add("-o");
        args.add(targetLibFile.getAbsolutePath());

        for (File objFile : objFiles) {
            args.add(objFile.getAbsolutePath());
        }

        args.addAll(Arrays.asList(target.libraries));

        // Use emcc for linking (handles both C and C++ object files)
        ToolchainExecutor.execute(cppCompilerExecutable, config.buildDir.file(), args, createToolChainCallback("Emscripten Link"));
    }

    /**
     * Sanitizes a library name into a valid JavaScript identifier suffix.
     * Replaces non-alphanumeric characters with underscores.
     */
    private static String sanitizeForJs (String name) {
        return name.replaceAll("[^a-zA-Z0-9]", "_");
    }

    @Override
    public void strip () {
        // Emscripten handles optimization/stripping via -O flags during compilation and linking.
        // No separate strip step needed.
        logger.info("Skipping strip for Emscripten (handled by optimization level)");
    }
}
