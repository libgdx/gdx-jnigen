package com.badlogic.gdx.jnigen.build.toolchains;

import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.build.ToolFinder;
import com.badlogic.gdx.jnigen.build.ToolchainExecutor;
import com.badlogic.gdx.jnigen.commons.Os;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AndroidToolchain extends BaseToolchain {


    private static final Logger logger = LoggerFactory.getLogger(AndroidToolchain.class);

    private File ndkExecutable;

    private String argsArrayToString(String[] args) {
        return Arrays.stream(args)
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(" "));
    }

    @Override
    public void checkForTools () {
        String ndkHome = System.getenv("NDK_HOME");
        if (ndkHome == null) {
            logger.warn("NDK_HOME not set, may not be able to find ndk-build unless you have set it on Path");
        } else {
            ENV.addToPath(ndkHome);
        }

        ndkExecutable = ToolFinder.getToolFile("ndk-build", ENV, ".cmd", true);
        logger.info("Toolchain is valid.\nCompiler: {}", ndkExecutable);
    }

    @Override
    public void compileTasks () {
        if (target.os != Os.Android) throw new IllegalArgumentException("target os must be Android");

        // create all the directories for outputing object files, shared libs and natives jar as well as build scripts.
        if (!config.libsDir.exists()) {
            if (!config.libsDir.mkdirs())
                throw new RuntimeException("Couldn't create directory for shared library files in '" + config.libsDir + "'");
        }
        if (!config.jniDir.exists()) {
            if (!config.jniDir.mkdirs())
                throw new RuntimeException("Couldn't create native code directory '" + config.jniDir + "'");
        }


        // create Application.mk file
        String template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/Application.mk.template",
                FileDescriptor.FileType.Classpath).readString();

        template = template.replace("%androidABIs%", target.getTargetAndroidABI().getAbiString());

        for (String extra : target.androidApplicationMk) {
            template += "\n" + extra;
        }

        config.buildDir.child("android32").child("Application.mk").writeString(template, false);

        // create Android.mk file
        template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/Android.mk.template", FileDescriptor.FileType.Classpath)
                .readString();

        ArrayList<File> allFiles = new ArrayList<>();
        allFiles.addAll(buildCFiles);
        allFiles.addAll(buildCppFiles);


        StringBuilder srcFiles = new StringBuilder();
        for (int i = 0; i < allFiles.size(); i++) {
            if (i > 0) {
                srcFiles.append("\t");
            }
            srcFiles.append(allFiles.get(i).getAbsolutePath());
            if (i < allFiles.size() - 1) {
                srcFiles.append("\\\n");
            } else {
                srcFiles.append("\n");
            }
        }

        StringBuilder headerDirs = new StringBuilder();
        for (String headerDir : target.headerDirs) {
            headerDirs.append(convertToAbsoluteRelativeTo(config.projectDir, headerDir));
            headerDirs.append(" ");
        }

        headerDirs.append(" " + convertToAbsoluteRelativeTo(config.jniDir, "."));

        template = template.replace("%sharedLibName%", config.sharedLibName);
        template = template.replace("%headerDirs%", headerDirs);
        template = template.replace("%cFlags%", argsArrayToString(target.cFlags));
        template = template.replace("%cppFlags%", argsArrayToString(target.cppFlags));
        template = template.replace("%linkerFlags%", argsArrayToString(target.linkerFlags));
        template = template.replace("%libraries%", argsArrayToString(target.libraries));
        template = template.replace("%srcFiles%", srcFiles);
        template = template.replace("%extraSharedLibModule%", String.join("\n", target.androidAndroidMkSharedLibModule));
        for (String extra : target.androidAndroidMk)
            template += "\n" + extra;

        config.buildDir.child("android32").child("Android.mk").writeString(template, false);

        ArrayList<String> args = new ArrayList<>();
        if (config.multiThreadedCompile) {
            args.add("-j" + Runtime.getRuntime().availableProcessors());
        }
        args.add("NDK_PROJECT_PATH=" + buildDirectory.getAbsolutePath());
        args.add("NDK_APPLICATION_MK=" + new File(buildDirectory, "Application.mk").getAbsolutePath());
        args.add("APP_BUILD_SCRIPT=" + new File(buildDirectory, "Android.mk").getAbsolutePath());
        args.add("NDK_OUT=" + buildDirectory.getAbsolutePath());
        args.add("NDK_LIBS_OUT=" + libsDirectory.getAbsolutePath());
        ToolchainExecutor.execute(ndkExecutable, config.jniDir.file(), args, createToolChainCallback("Android compile"));

    }

    @Override
    public void collectCompilationTasks () {
       //Dont have independent compilation tasks, ndk handles it
    }

    @Override
    public void link () {
        //NDK build handles
    }

    @Override
    public void strip () {
        //NDK build handles
    }

}
