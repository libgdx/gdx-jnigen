package com.badlogic.gdx.jnigen.build;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.commons.CompilerABIType;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.build.toolchains.*;
import com.badlogic.gdx.jnigen.commons.HostDetection;
import com.badlogic.gdx.jnigen.commons.Os;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Will build all targets given to it, each individual toolchain and approach will be decided here
 */
public class PlatformBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PlatformBuilder.class);

    /**
     * Supply all build targets from a configuration, we can sort and collect here. Saves us doing it in
     * Gradle tasks, and lets us support this easily with the direct api calls too
     * <p>
     * e.g. targetOSToBuildFor = android
     * We filter out all the BuildTargets so we only have ones with Android os. We then pick the Android
     * Toolchain and build everything
     * <p>
     * If we want, we can just supply a single build at a time, this gives us backwards compatibility with old gradle tasks
     * <p>
     * Desktop case, this would be called for Windows/Mac/Linux separately, but it would handle all archs in one
     *
     * @param targetOSToBuildFor
     * @param config
     * @param targets
     */
    public void build (Os targetOSToBuildFor, BuildConfig config, List<BuildTarget> targets) {
        try {

            List<BuildTarget> compatibleTargets = collectTargetsToBuild(targetOSToBuildFor, targets);

            //Check these are compatible with the host
            checkTargetsCompatibleWithHost(compatibleTargets);

            if (compatibleTargets.isEmpty()) {
                logger.info("No compatible targets found, nothing to build");
                return;
            }

            //Lets sort these out and build

            HashMap<Os, List<BuildTarget>> osSeparatedBuildTargets = new HashMap<>();
            for (BuildTarget compatibleTarget : compatibleTargets) {
                if (!osSeparatedBuildTargets.containsKey(compatibleTarget.os)) {
                    osSeparatedBuildTargets.put(compatibleTarget.os, new ArrayList<>());
                }
                if (compatibleTarget.excludeFromMasterBuildFile) {
                    logger.warn("Skipping build target because its marked as excluded from master build file. {}", compatibleTarget);
                } else {
                    osSeparatedBuildTargets.get(compatibleTarget.os).add(compatibleTarget);
                }
            }


            osSeparatedBuildTargets.forEach(new BiConsumer<Os, List<BuildTarget>>() {
                @Override
                public void accept (Os os, List<BuildTarget> buildTargets) {

                    if (buildTargets.size() > 1) {
                        //Make sure all abis are the same
                        CompilerABIType compilerABIType = buildTargets.get(0).compilerABIType;

                        for (BuildTarget buildTarget : buildTargets) {
                            if (buildTarget.compilerABIType != compilerABIType) {
                                logger.error("Build target {} does not have the same compilerABI type as previous ABI {}.", buildTarget, compilerABIType);
                                throw new RuntimeException("Invalid configuration");
                            }
                        }
                    }

                    logger.info("Starting build for OS {}", os);

                    for (BuildTarget buildTarget : buildTargets) {
                        logger.info("Starting build for Target {}", buildTarget);

                        BaseToolchain toolchain = findToolchainForTarget(buildTarget);
                        toolchain.configure(buildTarget, config);
                        toolchain.build();
                    }


                    if (os == Os.IOS) {
                        //special ios case to package on the platform into framework
                        IOSFrameworkBuilder.createXCFramework(config, buildTargets);
                    }

                }
            });

        } catch (Exception e) {
            logger.error("Failure to build", e);
            throw e;
        }
    }

    private BaseToolchain findToolchainForTarget (BuildTarget buildTarget) {
        switch (buildTarget.os) {
            case Windows:
                if (buildTarget.compilerABIType == CompilerABIType.MSVC) {
                    return new MSVCToolchain();
                } else {
                    return new GNUToolchain();
                }
            case IOS:
                return new IOSToolchain();
            case Linux:
            case MacOsX:
                return new GNUToolchain();
            case Android:
                return new AndroidToolchain();
            default:
                throw new IllegalStateException("Unexpected value: " + buildTarget.os);
        }
    }


    private void checkTargetsCompatibleWithHost (List<BuildTarget> compatibleTargets) {
        for (BuildTarget compatibleTarget : compatibleTargets) {
            if (!compatibleTarget.canBuildOnHost(HostDetection.os)) {
                throw new RuntimeException("Build target is not compatible with the host OS. " + compatibleTarget.toString());
            }
        }
    }


    private List<BuildTarget> collectTargetsToBuild (Os targetOSToBuildFor, List<BuildTarget> targets) {
        ArrayList<BuildTarget> targetsThatMatchOS = new ArrayList<>();
        for (BuildTarget target : targets) {
            if (target.os == targetOSToBuildFor) {
                targetsThatMatchOS.add(target);
            }
        }
        return targetsThatMatchOS;
    }

    public static void copyHeaders (FileDescriptor jniDir) {
        //copy headers to jni dir
        final String pack = "com/badlogic/gdx/jnigen/resources/headers";
        String files[] = {
                "classfile_constants.h",
                "jawt.h",
                "jdwpTransport.h",
                "jni.h",

                "linux/jawt_md.h",
                "linux/jni_md.h",
                "mac/jni_md.h",

                "win32/jawt_md.h",
                "win32/jni_md.h"
        };

        for (String file : files) {
            FileDescriptor source = new FileDescriptor(pack, FileDescriptor.FileType.Classpath).child(file);
            FileDescriptor destination = jniDir.child("jni-headers").child(file);
            destination.parent().mkdirs();
            source.copyTo(destination);
        }

        FileDescriptor jnigenHeader = new FileDescriptor(pack, FileDescriptor.FileType.Classpath).child("jnigen.h");
        jnigenHeader.copyTo(jniDir.child("jnigen.h"));
    }
}
