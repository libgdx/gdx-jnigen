package com.badlogic.gdx.jnigen.build;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.build.toolchains.IOSToolchain;
import com.badlogic.gdx.jnigen.commons.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IOSFrameworkBuilder {

    private final Logger logger = LoggerFactory.getLogger(IOSFrameworkBuilder.class);

    private static class DeviceSimBuildWrapper {

        private final TargetType targetType;

        private final List<IOSToolchain> deviceTargetToolchains;

        private FileDescriptor frameworkDirectory;
        private FileDescriptor frameworkBinaryFile;

        public DeviceSimBuildWrapper (BuildConfig config, FileDescriptor iosFrameworkFolder, TargetType targetType, List<IOSToolchain> deviceTargetToolchains) {
            this.targetType = targetType;
            this.deviceTargetToolchains = deviceTargetToolchains;

            this.frameworkDirectory = createFrameworkDirectory(iosFrameworkFolder, targetType, config.sharedLibName);
            this.frameworkBinaryFile = this.frameworkDirectory.child(config.sharedLibName);
        }

        private FileDescriptor createFrameworkDirectory (FileDescriptor parent, TargetType targetType, String sharedLibName) {
            FileDescriptor frameworkFolder = parent.child(targetType.getTargetTypeBuildDirName()).child(sharedLibName + ".framework");
            frameworkFolder.mkdirs();
            return frameworkFolder;
        }
    }

    public static void createXCFramework (BuildConfig config, List<BuildTarget> buildTargets) {
        List<IOSToolchain> deviceTargetToolchains = new ArrayList<>();
        List<IOSToolchain> simTargetToolchains = new ArrayList<>();

        for (BuildTarget buildTarget : buildTargets) {
            IOSToolchain iosToolchain = new IOSToolchain();
            iosToolchain.configure(buildTarget, config);
            if (buildTarget.targetType == TargetType.DEVICE) {
                deviceTargetToolchains.add(iosToolchain);
            } else {
                simTargetToolchains.add(iosToolchain);
            }
        }

        boolean hasSim = !simTargetToolchains.isEmpty();
        boolean hasDevice = !deviceTargetToolchains.isEmpty();

        FileDescriptor iosFrameworkFolder = config.buildDir.child("iosframework");
        iosFrameworkFolder.mkdirs();

        List<DeviceSimBuildWrapper> deviceSimBuildWrappers = new ArrayList<>();
        if (hasSim) {
            deviceSimBuildWrappers.add(new DeviceSimBuildWrapper(config, iosFrameworkFolder, TargetType.SIMULATOR, simTargetToolchains));
        }
        if (hasDevice) {
            deviceSimBuildWrappers.add(new DeviceSimBuildWrapper(config, iosFrameworkFolder, TargetType.DEVICE, deviceTargetToolchains));
        }

        RuntimeEnv env = new RuntimeEnv();
        File lipo = ToolFinder.getToolFile("lipo", env, true);

        File buildDir = config.buildDir.file();

        for (DeviceSimBuildWrapper deviceSimBuildWrapper : deviceSimBuildWrappers) {
            runLipoCommand(lipo, buildDir, deviceSimBuildWrapper);
        }


        byte[] plist = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/Info.plist.template", FileDescriptor.FileType.Classpath).readBytes();
        for (DeviceSimBuildWrapper deviceSimBuildWrapper : deviceSimBuildWrappers) {
            writePlist(plist, deviceSimBuildWrapper);
        }

        String xcBundleIdentifier = config.robovmBuildConfig.xcframeworkBundleIdentifier == null ? ("gdx.jnigen." + config.sharedLibName) : config.robovmBuildConfig.xcframeworkBundleIdentifier;

        for (DeviceSimBuildWrapper deviceSimBuildWrapper : deviceSimBuildWrappers) {
            runPlistBuddyCommand(env, deviceSimBuildWrapper, xcBundleIdentifier, config);
        }

        for (DeviceSimBuildWrapper deviceSimBuildWrapper : deviceSimBuildWrappers) {
            runPlutilCommand(env, deviceSimBuildWrapper);
        }

        File dsymutil = ToolFinder.getToolFile("dsymutil", env, true);

        for (DeviceSimBuildWrapper deviceSimBuildWrapper : deviceSimBuildWrappers) {
            runDsymutilCommand(dsymutil, deviceSimBuildWrapper, config);
        }

        File strip = ToolFinder.getToolFile("strip", env, true);
        for (DeviceSimBuildWrapper deviceSimBuildWrapper : deviceSimBuildWrappers) {
            runStripCommand(strip, deviceSimBuildWrapper, config);
        }

        File outputFramework = config.libsDir.child(config.sharedLibName + ".xcframework").file();
        if (outputFramework.exists()) {
            new FileDescriptor(outputFramework).deleteDirectory();
        }

        createXCFramework(env, buildDir, deviceSimBuildWrappers, config, outputFramework);
    }


    private static void runLipoCommand (File lipo, File workingDir, DeviceSimBuildWrapper wrapper) {
        List<String> args = new ArrayList<>();
        args.add("-create");
        args.add("-output");
        args.add(wrapper.frameworkBinaryFile.file().getAbsolutePath());
        for (IOSToolchain toolchain : wrapper.deviceTargetToolchains) {
            args.add(toolchain.getTargetLibFile().getAbsolutePath());
        }
        ToolchainExecutor.execute(lipo, workingDir, args, createCallback("Lipo"));
    }

    private static void writePlist (byte[] plist, DeviceSimBuildWrapper wrapper) {
        FileDescriptor devicePlist = wrapper.frameworkDirectory.child("Info.plist");
        devicePlist.writeBytes(plist, false);
    }

    private static void runPlistBuddyCommand (RuntimeEnv env, DeviceSimBuildWrapper wrapper, String bundleIdentifier, BuildConfig config) {
        File plistBuddy = ToolFinder.getToolFile("/usr/libexec/PlistBuddy", env, true);
        List<String> plistArgs = new ArrayList<>();
        plistArgs.add("-c");
        plistArgs.add("Set :CFBundleName " + config.sharedLibName);
        plistArgs.add("-c");
        plistArgs.add("Set :CFBundleExecutable " + config.sharedLibName);
        plistArgs.add("-c");
        plistArgs.add("Set :DTPlatformName " + wrapper.targetType.getPlatformName());
        plistArgs.add("-c");
        plistArgs.add("Set :CFBundleIdentifier " + bundleIdentifier);
        plistArgs.add("-c");
        plistArgs.add("Set :MinimumOSVersion " + config.robovmBuildConfig.minIOSVersion);
        plistArgs.add(wrapper.frameworkDirectory.child("Info.plist").file().getAbsolutePath());
        ToolchainExecutor.execute(plistBuddy, wrapper.frameworkDirectory.file().getParentFile(), plistArgs, createCallback("PlistBuddy " + wrapper.targetType.getPlatformName()));
    }

    private static void runPlutilCommand (RuntimeEnv env, DeviceSimBuildWrapper wrapper) {
        File plutil = ToolFinder.getToolFile("plutil", env, true);
        List<String> plutilArgs = new ArrayList<>();
        plutilArgs.add("-convert");
        plutilArgs.add("binary1");
        plutilArgs.add(wrapper.frameworkDirectory.child("Info.plist").file().getAbsolutePath());
        ToolchainExecutor.execute(plutil, wrapper.frameworkDirectory.file().getParentFile(), plutilArgs, createCallback("Plutil " + wrapper.targetType.getPlatformName()));
    }

    private static void runDsymutilCommand (File dsymutil, DeviceSimBuildWrapper wrapper, BuildConfig config) {
        List<String> dsymutilArgs = new ArrayList<>();
        dsymutilArgs.add(wrapper.frameworkDirectory.child(config.sharedLibName).file().getAbsolutePath());
        dsymutilArgs.add("-o");
        FileDescriptor dsymFolder = wrapper.frameworkDirectory.parent().child(config.sharedLibName + ".framework.dSYM");
        dsymutilArgs.add(dsymFolder.file().getAbsolutePath());
        ToolchainExecutor.execute(dsymutil, wrapper.frameworkDirectory.file().getParentFile(), dsymutilArgs, createCallback("Dsym " + wrapper.targetType.getPlatformName()));
    }

    private static void runStripCommand (File strip, DeviceSimBuildWrapper wrapper, BuildConfig config) {
        List<String> stripArgs = new ArrayList<>();
        stripArgs.add("-x");
        stripArgs.add(wrapper.frameworkDirectory.child(config.sharedLibName).file().getAbsolutePath());
        ToolchainExecutor.execute(strip, wrapper.frameworkDirectory.file().getParentFile(), stripArgs, createCallback("Strip " + wrapper.targetType.getPlatformName()));
    }

    private static void createXCFramework (RuntimeEnv env, File workingDir, List<DeviceSimBuildWrapper> wrappers, BuildConfig config, File output) {
        File xcodeBuild = ToolFinder.getToolFile("xcodebuild", env, true);
        List<String> xcframeworkArgs = new ArrayList<>();
        xcframeworkArgs.add("-create-xcframework");

        for (DeviceSimBuildWrapper wrapper : wrappers) {
            xcframeworkArgs.add("-framework");
            xcframeworkArgs.add(wrapper.frameworkDirectory.file().getAbsolutePath());
            xcframeworkArgs.add("-debug-symbols");
            xcframeworkArgs.add(wrapper.frameworkDirectory.parent().child(config.sharedLibName + ".framework.dSYM").file().getAbsolutePath());
        }

        xcframeworkArgs.add("-output");
        xcframeworkArgs.add(output.getAbsolutePath());

        ToolchainExecutor.execute(xcodeBuild, workingDir, xcframeworkArgs, createCallback("XCFramework generation"));
    }

    private static ToolchainExecutor.ToolchainCallback createCallback (String identifier) {

        final Logger logger = LoggerFactory.getLogger(IOSFrameworkBuilder.class);

        return new ToolchainExecutor.ToolchainCallback() {
            @Override
            public void onInfoMessage (String message) {
                logger.info(message);
            }

            @Override
            public void onErrorMessage (String message) {
                logger.error(message);
            }

            @Override
            public void onSuccess () {
                logger.info("{} complete", identifier);
            }

            @Override
            public void onFail (int statusCode) {
                logger.error("Failure to {}, status code {}", identifier, statusCode);
                throw new RuntimeException("Failure to " + identifier);
            }
        };
    }

}
