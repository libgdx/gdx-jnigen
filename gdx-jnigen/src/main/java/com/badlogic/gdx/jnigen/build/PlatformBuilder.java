package com.badlogic.gdx.jnigen.build;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.CompilerABIType;
import com.badlogic.gdx.jnigen.build.toolchains.*;
import com.badlogic.gdx.utils.Architecture;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;
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
    public void build (Os targetOSToBuildFor, BuildConfig config, BuildTarget... targets) {
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
                osSeparatedBuildTargets.get(compatibleTarget.os).add(compatibleTarget);
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

                }
            });
        } catch (Exception e) {
            logger.error("Failure to build", e);
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
            if (!buildTargetIsCompatibleWithOS(compatibleTarget)) {
                throw new RuntimeException("Build target is not compatible with the host OS. " + compatibleTarget.toString());
            }
        }
    }

    private boolean buildTargetIsCompatibleWithOS (BuildTarget compatibleTarget) {
        switch (SharedLibraryLoader.os) {
            case Windows:
                //Android, Windows, WindowsMSCV
                return compatibleTarget.os == Os.Windows ||
                        compatibleTarget.os == Os.Android;
            case Linux:
                //Android, Windows, Linux
                if (compatibleTarget.os == Os.Windows) {
                    //No MSVC support
                    if (compatibleTarget.compilerABIType == CompilerABIType.MSVC) {
                        return false;
                    }
                    return true;
                }

                //The rest
                return compatibleTarget.os == Os.Android || compatibleTarget.os == Os.Linux;
            case MacOsX:
                //Mac / iOS. Yes there are others, but Its not as trivial, so I think we should just handle Apple ecosphere
                return compatibleTarget.os == Os.MacOsX || compatibleTarget.os == Os.IOS;

            case Android:
            case IOS:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + SharedLibraryLoader.os);
        }

    }

    private List<BuildTarget> collectTargetsToBuild (Os targetOSToBuildFor, BuildTarget[] targets) {
        ArrayList<BuildTarget> targetsThatMatchOS = new ArrayList<>();
        for (BuildTarget target : targets) {
            if (target.os == targetOSToBuildFor) {
                targetsThatMatchOS.add(target);
            }
        }
        return targetsThatMatchOS;
    }

    public static void main (String[] args) {
        BuildConfig buildConfig = new BuildConfig("test", "tmp/gdx-jnigen", "libtemp/libs", "tmpjni/jni");

        BuildTarget win64 = BuildTarget.newDefaultTarget(Os.Windows, Architecture.Bitness._64, SharedLibraryLoader.architecture, CompilerABIType.MSVC);
        BuildTarget win32 = BuildTarget.newDefaultTarget(Os.Windows, Architecture.Bitness._32, SharedLibraryLoader.architecture, CompilerABIType.MSVC);
        BuildTarget win32Arm = BuildTarget.newDefaultTarget(Os.Windows, Architecture.Bitness._32, Architecture.ARM, CompilerABIType.MSVC);
        BuildTarget win64Arm = BuildTarget.newDefaultTarget(Os.Windows, Architecture.Bitness._64, Architecture.ARM, CompilerABIType.MSVC);

        BuildTarget android = BuildTarget.newDefaultTarget(Os.Android, null, null);

        BuildTarget[] target =  new BuildTarget[] {
//                win64,
//                win32,
//
//                win64Arm,
//                win32Arm

                android
        };
        PlatformBuilder platformBuilder = new PlatformBuilder();
        platformBuilder.build(Os.Android, buildConfig, target);
    }


}
