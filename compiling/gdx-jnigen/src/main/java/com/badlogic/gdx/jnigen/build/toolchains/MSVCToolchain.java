package com.badlogic.gdx.jnigen.build.toolchains;

import com.badlogic.gdx.jnigen.build.ToolFinder;
import com.badlogic.gdx.jnigen.build.ToolchainExecutor;
import com.badlogic.gdx.jnigen.commons.Architecture;
import com.badlogic.gdx.jnigen.commons.HostDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Toolchain that compiles c/c++ using msvc toolchain
 */
public class MSVCToolchain extends BaseToolchain {

    private static final Logger logger = LoggerFactory.getLogger(MSVCToolchain.class);

    private File cCompilerExecutable;
    private File cppCompilerExecutable;
    private File linkerExecutable;
    private File cmdExecutable;

    private String msvcIncludePath;
    private String msvcLibPath;
    private String msvcPathPath;
    private String[] msvcIncludeDirectories;
    private String[] msvcLibDirectories;

    @Override
    public void checkForTools () {
        //Lets configure our environment with vcvarsall
        ToolFinder.getToolFile("vcvarsall.bat", ENV, true);
        cmdExecutable = ToolFinder.getToolFile("cmd.exe", ENV, true);

        executeAndExtractVcVarsAll();


        String cCompiler = target.compilerPrefix + target.cCompiler + target.compilerSuffix;
        String cppCompiler = target.compilerPrefix + target.cppCompiler + target.compilerSuffix;
        String linker = target.compilerPrefix + target.cppCompiler + target.compilerSuffix;

        //Check that these actually exist!
        cCompilerExecutable = ToolFinder.getToolFile(cCompiler, ENV, true);
        cppCompilerExecutable = ToolFinder.getToolFile(cppCompiler, ENV, true);
        linkerExecutable = ToolFinder.getToolFile(linker, ENV, true);

        logger.info("Toolchain is valid.\ncCompiler: {}\ncppCompiler: {}\nlinker: {}", cCompilerExecutable, cppCompilerExecutable, linkerExecutable);
    }

    private String getVcVarsAllTarget () {
        boolean isHostARM = HostDetection.architecture == Architecture.ARM;
        boolean isHostX86 = HostDetection.architecture == Architecture.x86;

        boolean isTargetARM = target.architecture == Architecture.ARM;
        boolean isTargetX86 = target.architecture == Architecture.x86;

        boolean isHost32Bit = HostDetection.bitness == Architecture.Bitness._32;
        boolean isHost64Bit = HostDetection.bitness == Architecture.Bitness._64;

        boolean isTarget32Bit = target.bitness == Architecture.Bitness._32;
        boolean isTarget64Bit = target.bitness == Architecture.Bitness._64;

        if (isHostX86 && isHost64Bit) {
            if (isTargetX86 && isTarget32Bit) {
                return "x86";
            } else if (isTargetX86 && isTarget64Bit) {
                return "x64";
            } else if (isTargetARM && isTarget32Bit) {
                return "amd64_arm";
            } else if (isTargetARM && isTarget64Bit) {
                return "amd64_arm64";
            }
        } else if (isHostX86 && isHost32Bit) {
            if (isTargetX86 && isTarget32Bit) {
                return "x86";
            } else if (isTargetX86 && isTarget64Bit) {
                return "x64";
            } else if (isTargetARM && isTarget32Bit) {
                return "x86_arm";
            } else if (isTargetARM && isTarget64Bit) {
                return "x86_arm64";
            }
        } else if (isHostARM && isHost32Bit) {
            if (isTargetX86 && isTarget32Bit) {
                return "x86";
            } else if (isTargetX86 && isTarget64Bit) {
                return "x64";
            } else if (isTargetARM && isTarget32Bit) {
                return "arm";
            } else if (isTargetARM && isTarget64Bit) {
                return "arm64";
            }
        } else if (isHostARM && isHost64Bit) {
            if (isTargetX86 && isTarget32Bit) {
                return "x86";
            } else if (isTargetX86 && isTarget64Bit) {
                return "x64";
            } else if (isTargetARM && isTarget32Bit) {
                return "arm";
            } else if (isTargetARM && isTarget64Bit) {
                return "arm64";
            }
        }
        throw new UnsupportedOperationException("Unsupported host or target architecture combination");

    }


    private void executeAndExtractVcVarsAll () {
        ArrayList<String> args = new ArrayList<>();

        final String vcVarsAllTarget = getVcVarsAllTarget();

        args.add("/c");
        args.add("call");
        args.add("vcvarsall.bat");
        args.add(vcVarsAllTarget);
        args.add("&&");
        args.add("set");

        List<String> pathLines = new ArrayList<>();

        ToolchainExecutor.execute(cmdExecutable, config.jniDir.file(), args, new ToolchainExecutor.ToolchainCallback() {
            @Override
            public void onInfoMessage (String message) {
                if (message.contains("=")) {
                    if (message.startsWith("INCLUDE=")) {
                        msvcIncludePath = message.split("=")[1];
                    }
                    if (message.startsWith("LIB=")) {
                        msvcLibPath = message.split("=")[1];
                        System.out.println();
                    }
                    if (message.startsWith("Path=")) {
                        msvcPathPath = message.split("=")[1];
                    }
                    pathLines.add(message);
                }
            }

            @Override
            public void onErrorMessage (String message) {
                System.err.println(message);
            }

            @Override
            public void onSuccess () {
                logger.info("Completed vcvarsall");
            }

            @Override
            public void onFail (int statusCode) {
                logger.error("Failure to execute vcvarsall with target {} - status {}", vcVarsAllTarget, statusCode);
                throw new RuntimeException("Failed to execute vcvarsall");
            }
        });

        if (msvcIncludePath == null) {
            throw new RuntimeException("MSVC InludePath is null");
        }
        if (msvcLibPath == null) {
            throw new RuntimeException("MSVC Lib path is null");
        }
        if (msvcPathPath == null) {
            throw new RuntimeException("MSVC Path is null");
        }

        ENV.addToPath(msvcPathPath);

        msvcIncludeDirectories = ENV.splitToPaths(msvcIncludePath);
        if (msvcIncludeDirectories == null) {
            logger.error("MSVC include path cannot be parsed {}", msvcIncludePath);
            throw new RuntimeException("Failure to parse MSVC include path");
        }

        msvcLibDirectories = ENV.splitToPaths(msvcLibPath);
        if (msvcLibDirectories == null) {
            logger.error("MSVC lib directories path cannot be parsed {}", msvcLibPath);
            throw new RuntimeException("Failure to parse MSVC lib path");
        }

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
        args.addAll(stringFlagsToArgs(target.cFlags));
        for (String msvcIncludeDirectory : msvcIncludeDirectories) {
            args.add("/I" + msvcIncludeDirectory);
        }
        args.add("/I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers"));
        args.add("/I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/" + target.os.getJniPlatform()));
        args.add("/I" + convertToAbsoluteRelativeTo(config.jniDir, "."));
        for (String headerDir : target.headerDirs) {
            args.add("/I" + convertToAbsoluteRelativeTo(config.projectDir, headerDir));
        }

        for (File buildCFile : buildCFiles) {
            ArrayList<String> perFileArgs = new ArrayList<String>();
            perFileArgs.addAll(args);

            perFileArgs.add(buildCFile.getAbsolutePath());
            perFileArgs.add("/Fo:" + new File(buildDirectory, toObjectFile(buildCFile.getName())).getAbsolutePath());
            perFileArgs.add("/c");
            perFileArgs.add("/EHsc");

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
            logger.info("No cpp files, skipping cpp compilation");
            return;
        }

        List<String> args = new ArrayList<>();
        args.addAll(stringFlagsToArgs(target.cppFlags));
        for (String msvcIncludeDirectory : msvcIncludeDirectories) {
            args.add("/I" + msvcIncludeDirectory);
        }
        args.add("/I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers"));
        args.add("/I" + convertToAbsoluteRelativeTo(config.jniDir, "jni-headers/" + target.os.getJniPlatform()));
        args.add("/I" + convertToAbsoluteRelativeTo(config.jniDir, "."));
        for (String headerDir : target.headerDirs) {
            args.add("/I" + convertToAbsoluteRelativeTo(config.projectDir, headerDir));
        }

        for (File buildCppFile : buildCppFiles) {
            ArrayList<String> perFileArgs = new ArrayList<String>();
            perFileArgs.addAll(args);

            perFileArgs.add(buildCppFile.getAbsolutePath());
            perFileArgs.add("/Fo:" + new File(buildDirectory, toObjectFile(buildCppFile.getName())).getAbsolutePath());
            perFileArgs.add("/c");
            perFileArgs.add("/EHsc");

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
        return split[0] + ".obj";
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

        if (target.release) {
            args.add("/LD");
        } else {
            args.add("/LDd");
            args.add("/Zi");
        }

        if (!target.msvcPreLinkerFlags.isEmpty()) {
            args.addAll(stringFlagsToArgs(target.msvcPreLinkerFlags));
        }

        args.add("/Fe" + targetLibFile.getName());
        for (File objFile : objFiles) {
            args.add(objFile.getAbsolutePath());
        }

        args.add("/link");

        if (target.release) {
            args.add("/RELEASE");
            args.add("/NOCOFFGRPINFO");
        } else {
            args.add("/DEBUG");
        }

        for (String msvcLibDirectory : msvcLibDirectories) {
            args.add("/LIBPATH:" + msvcLibDirectory);
        }
        args.addAll(stringFlagsToArgs(target.linkerFlags));
        if (!target.libraries.isEmpty()) {
            args.addAll(stringFlagsToArgs(target.libraries));
        }

        logger.info("Linking Target {}", target);

        ToolchainExecutor.execute(linkerExecutable, libsDirectory, args, createToolChainCallback("Link"));
    }

    @Override
    public void strip () {
        //Handled by linker
    }

}
