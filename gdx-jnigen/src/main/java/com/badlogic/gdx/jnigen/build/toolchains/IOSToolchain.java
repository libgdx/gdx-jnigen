package com.badlogic.gdx.jnigen.build.toolchains;

import com.badlogic.gdx.jnigen.build.ToolFinder;
import com.badlogic.gdx.jnigen.build.ToolchainExecutor;
import com.badlogic.gdx.utils.Os;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Toolchain that compiles c/c++ using gcc/g++/clang toolchains
 */
public class IOSToolchain extends BaseToolchain {

    private static final Logger logger = LoggerFactory.getLogger(GNUToolchain.class);

    private File cCompilerExecutable;
    private File cppCompilerExecutable;
    private File linkerExecutable;

    @Override
    public void checkForTools () {
        String cCompiler = target.compilerPrefix + target.cCompiler + target.compilerSuffix;
        String cppCompiler = target.compilerPrefix + target.cppCompiler + target.compilerSuffix;

        //Check that these actually exist!
        cCompilerExecutable = ToolFinder.getToolFile(cCompiler, ENV, ".exe", true);
        cppCompilerExecutable = ToolFinder.getToolFile(cppCompiler, ENV, ".exe", true);
        linkerExecutable = cppCompilerExecutable;



        //Don't strip on Mac or in non release mode

        logger.info("Toolchain is valid.\ncCompiler: {}\ncppCompiler: {}\nlinker: {}\nstripper: {}", cCompilerExecutable, cppCompilerExecutable, linkerExecutable, stripperExecutable);
    }


    @Override
    public void compile () {
        compileC();
        compileCPP();

        link();
        strip();
    }


    private void compileC () {
        if (sourceCollectedCFiles.isEmpty()) {
            logger.info("No c files, skipping c compilation");
            return;
        }

        List<String> args = new ArrayList<>();
        args.addAll(stringFlagsToArgs(target.cFlags));
        args.add("-Ijni-headers");
        args.add("-Ijni-headers/" + target.os.getJniPlatform());
        args.add("-I.");
        args.addAll(Arrays.asList(target.headerDirs));

        ArrayList<String> perFileArgs = new ArrayList<String>();
        perFileArgs.addAll(args);

        for (File buildCFile : buildCFiles) {
            perFileArgs.clear();
            perFileArgs.addAll(args);

            perFileArgs.add(buildCFile.getAbsolutePath());
            perFileArgs.add("-o");
            perFileArgs.add(new File(buildDirectory, toObjectFile(buildCFile.getName())).getAbsolutePath());

            logger.info("Compiling C File {}", buildCFile.getAbsolutePath());
            ToolchainExecutor.execute(cCompilerExecutable, config.jniDir.file(), perFileArgs, createToolChainCallback("C Compile"));
        }
    }

    private void compileCPP () {
        if (sourceCollectedCppFiles.isEmpty()) {
            logger.info("No c files, skipping c compilation");
            return;
        }

        List<String> args = new ArrayList<>();
        args.addAll(stringFlagsToArgs(target.cppFlags));
        args.add("-Ijni-headers/");
        args.add("-Ijni-headers/" + target.os.getJniPlatform());
        args.add("-I.");
        args.addAll(Arrays.asList(target.headerDirs));

        ArrayList<String> perFileArgs = new ArrayList<String>();
        perFileArgs.addAll(args);

        for (File buildCppFile : buildCppFiles) {
            perFileArgs.clear();
            perFileArgs.addAll(args);

            perFileArgs.add(buildCppFile.getAbsolutePath());
            perFileArgs.add("-o");
            perFileArgs.add(new File(buildDirectory, toObjectFile(buildCppFile.getName())).getAbsolutePath());

            logger.info("Compiling CPP File {}", buildCppFile.getAbsolutePath());
            ToolchainExecutor.execute(cppCompilerExecutable, config.jniDir.file(), perFileArgs, createToolChainCallback("CPP Compile"));
        }

    }

    private String toObjectFile (String name) {
        String[] split = name.split("\\.");
        return split[0] + ".o";
    }

    private void link () {

        libsDirectory.mkdirs();
        if (!libsDirectory.exists()) {
            logger.error("Error creating libs dir {}", libsDirectory.getAbsolutePath());
            throw new RuntimeException("Error creating libs dir");
        }


        ArrayList<File> objFiles = new ArrayList<>();
        for (File cObjectFile : sourceCollectedCFiles) {
            objFiles.add(new File(buildDirectory, toObjectFile(cObjectFile.getName())));
        }
        for (File cppObjhectFile : sourceCollectedCppFiles) {
            objFiles.add(new File(buildDirectory, toObjectFile(cppObjhectFile.getName())));
        }

        List<String> args = new ArrayList<>();

        args.addAll(stringFlagsToArgs(target.linkerFlags));
        args.add("-o");
        args.add(targetLibFile.getAbsolutePath());
        for (File objFile : objFiles) {
            args.add(objFile.getAbsolutePath());
        }
        if (!target.libraries.isEmpty()) {
            args.addAll(stringFlagsToArgs(target.libraries));
        }

        ToolchainExecutor.execute(linkerExecutable, config.buildDir.file(), args, createToolChainCallback("Link"));
    }

    private void strip () {
        if (!target.release) {
            logger.info("Skipping strip due to release property {}", target.release);
            return;
        }
        if (target.os == Os.MacOsX) {
            logger.info("Skipping strip due to target being {}", target.os);
            return;
        }

        //Otherwise we strip it ye boooi

        ArrayList<String> args = new ArrayList<>();
        args.add("--strip-unneeded");
        args.add(targetLibFile.getAbsolutePath());

        ToolchainExecutor.execute(stripperExecutable, buildDirectory, args, createToolChainCallback("Strip"));
    }
}
