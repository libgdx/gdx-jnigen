package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.RobovmBuildConfig;
import com.badlogic.gdx.jnigen.commons.*;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Desu
 */
public class JnigenExtension {
    private static final Logger log = LoggerFactory.getLogger(JnigenExtension.class);

    public static final Architecture.Bitness x32 = Architecture.Bitness._32;
    public static final Architecture.Bitness x64 = Architecture.Bitness._64;
    public static final Architecture.Bitness x128 = Architecture.Bitness._128;
    public static final Architecture x86 = Architecture.x86;
    public static final Architecture ARM = Architecture.ARM;
    public static final Architecture RISCV = Architecture.RISCV;
    public static final Architecture LOONGARCH = Architecture.LOONGARCH;

    public static final CompilerABIType MSVC = CompilerABIType.MSVC;
    public static final CompilerABIType GCC_CLANG = CompilerABIType.GCC_CLANG;
    public static final Os Windows = Os.Windows;
    public static final Os Linux = Os.Linux;
    public static final Os MacOsX = Os.MacOsX;
    public static final Os Android = Os.Android;
    public static final Os IOS = Os.IOS;

    public static final AndroidABI ABI_ARMEABI_V7A = AndroidABI.ABI_ARMEABI_V7A;
    public static final AndroidABI ABI_x86f = AndroidABI.ABI_x86;
    public static final AndroidABI ABI_ARM64_V8A = AndroidABI.ABI_ARM64_V8A;
    public static final AndroidABI ABI_x86_64 = AndroidABI.ABI_x86_64;

    public static final TargetType DEVICE = TargetType.DEVICE;
    public static final TargetType SIMULATOR = TargetType.SIMULATOR;


    final Project project;

    /**
     * Gradle Tasks are executed in the main project working directory. Supply
     * actual subproject path where necessary.
     */
    String subProjectDir;

    String sharedLibName = null;
    String temporaryDir = "build/jnigen/target";
    String libsDir = "build/jnigen/libs";
    String jniDir = "build/jnigen/jni";

    /**
     * If we should build with release flag set.<br/>
     * This strips debug symbols.
     */
    boolean release = true;

    boolean multiThreadedCompile = true;

    NativeCodeGeneratorConfig nativeCodeGeneratorConfig;
    public List<BuildTarget> targets = new ArrayList<>();
    Action<BuildTarget> all = null;

    Action<RobovmBuildConfig> robovm;
	JnigenBindingGeneratorExtension generator = new JnigenBindingGeneratorExtension();

    @Inject
    public JnigenExtension (Project project) {
        this.project = project;
        this.subProjectDir = project.getProjectDir().getAbsolutePath() + File.separator;
        this.nativeCodeGeneratorConfig = new NativeCodeGeneratorConfig(project);
    }

    public void generator(Action<JnigenBindingGeneratorExtension> container) {
		container.execute(generator);
	}

    public void nativeCodeGenerator (Action<NativeCodeGeneratorConfig> container) {
        container.execute(nativeCodeGeneratorConfig);
    }

    public void all (Action<BuildTarget> container) {
        this.all = container;
    }

    public void robovm (Action<RobovmBuildConfig> robovmConfigContainer) {
        this.robovm = robovmConfigContainer;
    }


    public void addLinux (Architecture.Bitness bitness, Architecture architecture) {
        addLinux(bitness, architecture, null);
    }

    public void addLinux (Architecture.Bitness bitness, Architecture architecture, Action<BuildTarget> container) {
        add(Linux, bitness, architecture, CompilerABIType.GCC_CLANG, TargetType.DEVICE, null, container);
    }


    public void addIOS () {
        addIOS(x64, x86, SIMULATOR);
        addIOS(x64, ARM, DEVICE);
        addIOS(x64, ARM, SIMULATOR);
    }

    public void addIOS (Action<BuildTarget> container) {
        addIOS(x64, x86, SIMULATOR, container);
        addIOS(x64, ARM, DEVICE, container);
        addIOS(x64, ARM, SIMULATOR, container);
    }



    public void addIOS (Architecture.Bitness bitness, Architecture architecture, TargetType targetType) {
        addIOS(bitness, architecture, targetType, null);
    }

    public void addIOS (Architecture.Bitness bitness, Architecture architecture, TargetType targetType, Action<BuildTarget> container) {
        add(Os.IOS, bitness, architecture, CompilerABIType.GCC_CLANG, targetType, null, container);
    }

    public void addMac (Architecture.Bitness bitness, Architecture architecture) {
        addMac(bitness, architecture, null);
    }

    public void addMac (Architecture.Bitness bitness, Architecture architecture, Action<BuildTarget> container) {
        add(Os.MacOsX, bitness, architecture, GCC_CLANG, TargetType.DEVICE, null, container);
    }

    public void addWindows (Architecture.Bitness bitness, Architecture architecture) {
        addWindows(bitness, architecture, GCC_CLANG, null);
    }

    public void addWindows (Architecture.Bitness bitness, Architecture architecture, Action<BuildTarget> container) {
        addWindows(bitness, architecture, GCC_CLANG, null);
    }

    public void addWindows (Architecture.Bitness bitness, Architecture architecture, CompilerABIType compilerABIType) {
        addWindows(bitness, architecture, compilerABIType, null);
    }

    public void addWindows (Architecture.Bitness bitness, Architecture architecture, CompilerABIType compilerABIType, Action<BuildTarget> container) {
        add(Os.Windows, bitness, architecture, compilerABIType, TargetType.DEVICE, null, container);
    }

    public void addAndroid () {
        addAndroid(null, null);
    }

    public void addAndroid (Action<BuildTarget> container) {
        addAndroid(null, container);
    }

    public void addAndroid (AndroidABI abi) {
        addAndroid(abi, null);
    }

    public void addAndroid (AndroidABI abi, Action<BuildTarget> container) {
        if (abi == null) {
            for (AndroidABI value : AndroidABI.values()) {
                //Add them all!
                addAndroid(value, container);
            }
        } else {
            //bitness and arch doesn't mean anything for android
            add(Os.Android, Architecture.Bitness._32, Architecture.x86, CompilerABIType.GCC_CLANG, DEVICE, abi, container);
        }
    }

    public void add (Os targetOs, Architecture.Bitness bitness, Architecture architecture, CompilerABIType abiType, TargetType targetType, AndroidABI androidABI, Action<BuildTarget> container) {
        String name = targetOs + architecture.toSuffix().toUpperCase() + bitness.toSuffix();

        if (get(targetOs, bitness, architecture, androidABI, targetType, container) != null)
            throw new RuntimeException("Attempt to add duplicate build target " + name);
        if ((targetOs == Android) && bitness != Architecture.Bitness._32 && architecture != Architecture.x86)
            throw new RuntimeException("Android and iOS must not have is64Bit or isARM or isRISCV.");

        BuildTarget target = BuildTarget.newDefaultTarget(targetOs, bitness, architecture, abiType, targetType);
        target.release = release;

        if (all != null)
            all.execute(target);
        if (container != null)
            container.execute(target);

        if (target.excludeFromMasterBuildFile) {
            return;
        }
        targets.add(target);
        target.setAndroidOverrideABI(androidABI);

        checkForTasksToAdd(target);

    }

    private Set<Os> osLevelTargetsSeen = new HashSet<>();
    private Set<Platform> platformLevelTargetsSeen = new HashSet<>();

    private void checkForTasksToAdd (BuildTarget target) {

        Os os = target.os;
        Platform platform = os.getPlatform();
        Architecture architecture = target.architecture;
        Architecture.Bitness bitness = target.bitness;

        JnigenTask jnigenTask = (JnigenTask) project.getTasks().getByName("jnigen");

        if (!osLevelTargetsSeen.contains(os)) {
            osLevelTargetsSeen.add(os);
            JnigenBuildTask jnigenBuildTask = project.getTasks().create("jnigenBuildAll" + os.name(), JnigenBuildTask.class, this);
            jnigenBuildTask.setOsToBuild(os);
            jnigenBuildTask.dependsOn(jnigenTask);
        }

        if (target.os == Android) {
            JnigenBuildTask jnigenBuildTask = project.getTasks().create("jnigenBuild" + os.name() + "_" + target.getTargetAndroidABI().getAbiString(), JnigenBuildTask.class, this);
            jnigenBuildTask.setBuildTarget(target);
            jnigenBuildTask.dependsOn(jnigenTask);

            //Add the package task, android does separate artifacts

           JnigenPackageTask jnigenPackageTask = project.getTasks().create("jnigenPackage" + platform.name() + "_" + target.getTargetAndroidABI().getAbiString(), JnigenPackageTask.class, this);
           jnigenPackageTask.configure(target.getTargetAndroidABI(), platform);

        } else if (target.os == Os.IOS) {
            //Nope! No platform specific builds for ios, because theyend up in framework.
            //Don't want to have to do a separate task after for combining the framework
        } else {
            JnigenBuildTask jnigenBuildTask = project.getTasks().create("jnigenBuild" + os.name() + "_" + architecture.getDisplayName() + bitness.name(), JnigenBuildTask.class, this);
            jnigenBuildTask.setBuildTarget(target);
            jnigenBuildTask.dependsOn(jnigenTask);

            if (HostDetection.os == os && HostDetection.architecture == architecture && HostDetection.bitness == bitness) {
                DefaultTask hostTask = project.getTasks().create("jnigenBuildHost", DefaultTask.class);
                hostTask.dependsOn(jnigenBuildTask);
                hostTask.setGroup("jnigen");
                hostTask.setDescription("Builds only the host architecture");
            }
        }

        if (!platformLevelTargetsSeen.contains(platform)) {
            platformLevelTargetsSeen.add(platform);

            JnigenPackageTask jnigenPackageTask = project.getTasks().create("jnigenPackageAll" + platform.name(), JnigenPackageTask.class, this);
            jnigenPackageTask.configure(null, platform);
        }

    }

    public BuildTarget get (Os type, Architecture.Bitness bitness, Architecture architecture, AndroidABI androidABI, TargetType targetType, Action<BuildTarget> container) {
        for (BuildTarget target : targets) {
            if (
                    target.os == type &&
                            target.bitness == bitness &&
                            target.architecture == architecture &&
                            target.getTargetAndroidABI() == androidABI &&
                            target.targetType == targetType) {
                if (container != null)
                    container.execute(target);
                return target;
            }
        }
        return null;
    }

    public void each (Predicate<BuildTarget> condition, Action<BuildTarget> container) {
        for (BuildTarget target : targets) {
            if (condition.test(target))
                container.execute(target);
        }
    }

    class NativeCodeGeneratorConfig {
        SourceSet sourceSet;
        private String[] sourceDirs;
        String[] includes = null;
        String[] excludes = null;

        public NativeCodeGeneratorConfig (Project project) {
            JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class);
            SourceSetContainer sourceSets = javaPlugin.getSourceSets();
            sourceSet = sourceSets.findByName("main");
        }

        @Override
        public String toString () {
            return "NativeCodeGeneratorConfig[sourceDir=`" + Arrays.toString(sourceDirs) + "`, sourceSet=`" + sourceSet + "`, jniDir=`"
                    + jniDir + "`, includes=`" + Arrays.toString(includes)
                    + "`, excludes=`" + Arrays.toString(excludes) + "`]";
        }

        /**
         * This method is deprecated in favor of {@link NativeCodeGeneratorConfig#setSourceDirs(String[])}
         */
        @Deprecated
        public void setSourceDir (String sourceDir) {
            this.sourceDirs = new String[]{sourceDir};
        }

        public void setSourceDirs (String[] sourceDirs) {
            this.sourceDirs = sourceDirs;
        }

        public String[] getSourceDirs () {
            //If already set, use provided value
            if (sourceDirs != null) {
                return sourceDirs;
            }

            sourceDirs = sourceSet.getJava().getSrcDirs().stream().map(File::getPath).toArray(String[]::new);
            return sourceDirs;
        }
    }


}
