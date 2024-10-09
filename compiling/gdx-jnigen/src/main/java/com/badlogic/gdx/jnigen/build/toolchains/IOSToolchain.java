package com.badlogic.gdx.jnigen.build.toolchains;

import com.badlogic.gdx.jnigen.build.ToolFinder;
import com.badlogic.gdx.jnigen.build.ToolchainExecutor;
import com.badlogic.gdx.jnigen.commons.Architecture;
import com.badlogic.gdx.jnigen.commons.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Toolchain that compiles c/c++ using gcc/g++/clang toolchains
 */
public class IOSToolchain extends BaseToolchain {

    private static final Logger logger = LoggerFactory.getLogger(GNUToolchain.class);

    private File cCompilerExecutable;
    private File cppCompilerExecutable;
    private File linkerExecutable;
    private File iphoneSDK;
    private File simSDK;

    @Override
    public void checkForTools () {
        String cCompiler = target.compilerPrefix + target.cCompiler + target.compilerSuffix;
        String cppCompiler = target.compilerPrefix + target.cppCompiler + target.compilerSuffix;

        //Check that these actually exist!
        cCompilerExecutable = ToolFinder.getToolFile(cCompiler, ENV, ".exe", true);
        cppCompilerExecutable = ToolFinder.getToolFile(cppCompiler, ENV, ".exe", true);
        linkerExecutable = cppCompilerExecutable;

        iphoneSDK = ToolFinder.getSDK("/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk/", true);
        simSDK = ToolFinder.getSDK("/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk", true);

        logger.info("Toolchain is valid.\ncCompiler: {}\ncppCompiler: {}\nlinker: {}\niOS SDK: {}\niOS Sim SDK: {}", cCompilerExecutable, cppCompilerExecutable, linkerExecutable, iphoneSDK, simSDK);
    }


    @Override
    public void collectCompilationTasks () {
        collectCCompileTasks();
        collectCPPCompileTasks();
    }

    private String getClangArch () {
        if (target.architecture == Architecture.ARM) {
            return "arm64";
        }
        if (target.architecture == Architecture.x86) {
            return "x86_64";
        }
        throw new RuntimeException("Unsupported arch");
    }


    private void collectCCompileTasks () {
        if (buildCFiles.isEmpty()) {
            logger.info("No c files, skipping c compilation");
            return;
        }

        List<String> args = new ArrayList<>();

        args.add("-isysroot");
        if (target.targetType == TargetType.DEVICE) {
            args.add(iphoneSDK.getAbsolutePath());
        } else {
            args.add(simSDK.getAbsolutePath());
        }

        args.add("-arch");
        args.add(getClangArch());

        String iosVesion = target.targetType == TargetType.DEVICE ? "-miphoneos-version-min" : "-mios-simulator-version-min";
        args.add(iosVesion + "=" + config.robovmBuildConfig.minIOSVersion);

        args.addAll(stringFlagsToArgs(target.cFlags));

        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers"));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/" + target.os.getJniPlatform()));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "."));
        for (String headerDir : target.headerDirs) {
            args.add("-I" + convertToAbsoluteRelativeTo(config.projectDir, headerDir));
        }

        args.add("-g");

        for (File buildCFile : buildCFiles) {
            ArrayList<String> perFileArgs = new ArrayList<String>();
            perFileArgs.addAll(args);

            perFileArgs.add(buildCFile.getAbsolutePath());
            perFileArgs.add("-o");
            perFileArgs.add(new File(buildDirectory, toObjectFile(buildCFile.getName())).getAbsolutePath());


            compilationTasks.add(new Callable<Void>() {
                @Override
                public Void call () throws Exception {
                    logger.info("Compiling C File {}", buildCFile.getAbsolutePath());
                    ToolchainExecutor.execute(cCompilerExecutable, config.jniDir.file(), perFileArgs, createToolChainCallback("C Compile - " + buildCFile.getName()));
                    return null;
                }
            });
        }
    }

    private void collectCPPCompileTasks () {
        if (buildCppFiles.isEmpty()) {
            logger.info("No c files, skipping c compilation");
            return;
        }

        List<String> args = new ArrayList<>();

        args.add("-isysroot");
        if (target.targetType == TargetType.DEVICE) {
            args.add(iphoneSDK.getAbsolutePath());
        } else {
            args.add(simSDK.getAbsolutePath());
        }

        args.add("-arch");
        args.add(getClangArch());

        String iosVesion = target.targetType == TargetType.DEVICE ? "-miphoneos-version-min" : "-mios-simulator-version-min";
        args.add(iosVesion + "=" + config.robovmBuildConfig.minIOSVersion);

        args.addAll(stringFlagsToArgs(target.cppFlags));


        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers"));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/" + target.os.getJniPlatform()));
        args.add("-I" + convertToAbsoluteRelativeTo(config.jniDir, "."));
        for (String headerDir : target.headerDirs) {
            args.add("-I" + convertToAbsoluteRelativeTo(config.projectDir, headerDir));
        }

        args.add("-g");

        for (File buildCppFile : buildCppFiles) {
            ArrayList<String> perFileArgs = new ArrayList<String>();
            perFileArgs.addAll(args);

            perFileArgs.add(buildCppFile.getAbsolutePath());
            perFileArgs.add("-o");
            perFileArgs.add(new File(buildDirectory, toObjectFile(buildCppFile.getName())).getAbsolutePath());

            compilationTasks.add(new Callable<Void>() {
                @Override
                public Void call () throws Exception {
                    logger.info("Compiling CPP File {}", buildCppFile.getAbsolutePath());
                    ToolchainExecutor.execute(cppCompilerExecutable, config.jniDir.file(), perFileArgs, createToolChainCallback("CPP Compile - " + buildCppFile.getName()));
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
        for (File cppObjhectFile : buildCppFiles) {
            objFiles.add(new File(buildDirectory, toObjectFile(cppObjhectFile.getName())));
        }

        List<String> args = new ArrayList<>();

        args.add("-isysroot");
        if (target.targetType == TargetType.DEVICE) {
            args.add(iphoneSDK.getAbsolutePath());
        } else {
            args.add(simSDK.getAbsolutePath());
        }

        args.add("-arch");
        args.add(getClangArch());

        String iosVesion = target.targetType == TargetType.DEVICE ? "-miphoneos-version-min" : "-mios-simulator-version-min";
        args.add(iosVesion + "=" + config.robovmBuildConfig.minIOSVersion);

        args.addAll(stringFlagsToArgs(target.linkerFlags));
        args.add("-install_name");
        args.add("@rpath/" + config.sharedLibName + ".framework/" + config.sharedLibName);

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

    @Override
    public void strip () {

    }


}
