package com.badlogic.gdx.jnigen.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.utils.Architecture;
import com.badlogic.gdx.utils.Os;

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
	public static final Os Windows = Os.Windows;
	public static final Os Linux = Os.Linux;
	public static final Os MacOsX = Os.MacOsX;
	public static final Os Android = Os.Android;
	public static final Os IOS = Os.IOS;

	final Project project;

	/**
	 * Gradle Tasks are executed in the main project working directory. Supply
	 * actual subproject path where necessary.
	 */
	String subProjectDir;

	String sharedLibName = null;
	String temporaryDir = "target";
	String libsDir = "libs";
	String jniDir = "jni";
	
	/**
	 * If we should build with release flag set.<br/>
	 * This strips debug symbols.
	 */
	boolean release = true;

	NativeCodeGeneratorConfig nativeCodeGeneratorConfig;
	List<BuildTarget> targets = new ArrayList<>();
	Action<BuildTarget> all = null;

	Task jarAndroidNatives = null;
	JnigenJarTask jarDesktopNatives = null;

	RoboVMXml robovm = new RoboVMXml();

	@Inject
	public JnigenExtension(Project project) {
		this.project = project;
		this.subProjectDir = project.getProjectDir().getAbsolutePath() + File.separator;
		this.nativeCodeGeneratorConfig = new NativeCodeGeneratorConfig(project, subProjectDir);
	}

	public void nativeCodeGenerator(Action<NativeCodeGeneratorConfig> container) {
		container.execute(nativeCodeGeneratorConfig);
	}

	public void all(Action<BuildTarget> container) {
		this.all = container;
	}

	public void robovm(Action<RoboVMXml> action) {
		action.execute(robovm);
	}

	public void add(Os type) {
		add(type, Architecture.Bitness._32);
	}

	public void add(Os type, Architecture.Bitness bitness) {
		add(type, bitness, Architecture.x86);
	}

	public void add(Os type, Architecture.Bitness bitness, Architecture architecture) {
		add(type, bitness, architecture, null);
	}

	@Deprecated
	public void add(Os type, boolean is64Bit) {
		add(type, is64Bit, false, null);
	}

	@Deprecated
	public void add(Os type, boolean is64Bit, boolean isARM) {
		add(type, is64Bit, isARM, null);
	}

	public void add(Os type, Action<BuildTarget> container) {
		add(type, Architecture.Bitness._32, Architecture.x86, container);
	}

	@Deprecated
	public void add(Os type, boolean is64Bit, Action<BuildTarget> container) {
		add(type, is64Bit, false, container);
	}

	@Deprecated
	public void add(Os type, boolean is64Bit, boolean isARM, Action<BuildTarget> container) {
		add(type, is64Bit ? Architecture.Bitness._64 : Architecture.Bitness._32, isARM ? Architecture.ARM : Architecture.x86, container);
	}

	public void add(Os type, Architecture.Bitness bitness, Architecture architecture, Action<BuildTarget> container) {
		String name = type + architecture.toSuffix().toUpperCase() + bitness.toSuffix();

		if(get(type, bitness, architecture) != null)
			throw new RuntimeException("Attempt to add duplicate build target " + name);
		if((type == Android || type == IOS) && bitness != Architecture.Bitness._32 && architecture != Architecture.x86)
			throw new RuntimeException("Android and iOS must not have is64Bit or isARM or isRISCV.");

		BuildTarget target = BuildTarget.newDefaultTarget(type, bitness, architecture);

		if (all != null)
			all.execute(target);
		if (container != null)
			container.execute(target);

		targets.add(target);

		Task jnigenTask = project.getTasks().getByName("jnigen");
		Task jnigenBuildTask = project.getTasks().getByName("jnigenBuild");
		Task builtTargetTask = project.getTasks().create("jnigenBuild" + name,
				JnigenBuildTargetTask.class, this, target);
		builtTargetTask.dependsOn(jnigenTask);

		if (!target.excludeFromMasterBuildFile && target.canBuild.getAsBoolean())
			jnigenBuildTask.dependsOn(builtTargetTask);
		
		if(type == Android) {
			if(jarAndroidNatives == null) {
				jarAndroidNatives = project.getTasks().create("jnigenJarNativesAndroid");
				jarAndroidNatives.setGroup("jnigen");
				jarAndroidNatives.setDescription("Assembles all jar archives containing the native libraries for Android.");
			}
			
			String[] abis = target.androidABIs;
			
			// If we have an "all" abi, add tasks for all known abis.
			if(Arrays.asList(abis).contains("all")) {
				List<String> tmp = new ArrayList<>(Arrays.asList(abis));
				tmp.remove("all");
				tmp.add("armeabi");
				tmp.add("armeabi-v7a");
				tmp.add("x86");
				tmp.add("x86_64");
				tmp.add("arm64-v8a");
				abis = tmp.toArray(new String[tmp.size()]);
			}
			
			JnigenJarTask[] jarAndroidNativesABIs = new JnigenJarTask[abis.length];
			for(int i = 0; i < abis.length; i++) {
				jarAndroidNativesABIs[i] = project.getTasks().create("jnigenJarNativesAndroid"+abis[i], JnigenJarTask.class, type);
				jarAndroidNativesABIs[i].add(target, this, abis[i]);
				
				jarAndroidNatives.dependsOn(jarAndroidNativesABIs[i]);
			}
		} else if (type == IOS) {
			JnigenGenerateRoboVMXml generateRoboVMXml = project.getTasks().create("jnigenGenerateRoboVMXml",
					JnigenGenerateRoboVMXml.class, this);

			JnigenIOSJarTask jarIOSNatives = project.getTasks().create("jnigenJarNativesIOS", JnigenIOSJarTask.class);
			jarIOSNatives.add(target, this);

			jarIOSNatives.dependsOn(generateRoboVMXml);
		}
		else {
			if(jarDesktopNatives == null)
				jarDesktopNatives = project.getTasks().create("jnigenJarNativesDesktop", JnigenJarTask.class, type);
			jarDesktopNatives.add(target, this);
		}
	}

	public BuildTarget get(Os type) {
		return get(type, Architecture.Bitness._32, Architecture.x86, null);
	}

	public BuildTarget get(Os type, Architecture.Bitness bitness) {
		return get(type, bitness, Architecture.x86);
	}

	@Deprecated
	public BuildTarget get(Os type, boolean is64Bit) {
		return get(type, is64Bit, false, null);
	}

	@Deprecated
	public BuildTarget get(Os type, boolean is64Bit, boolean isARM) {
		return get(type, is64Bit, isARM, null);
	}

	public BuildTarget get(Os type, Architecture.Bitness bitness, Architecture architecture) {
		return get(type, bitness, architecture, null);
	}

	public BuildTarget get(Os type, Action<BuildTarget> container) {
		return get(type, Architecture.Bitness._32, Architecture.x86, container);
	}

	@Deprecated
	public BuildTarget get(Os type, boolean is64Bit, Action<BuildTarget> container) {
		return get(type, is64Bit, false, container);
	}

	@Deprecated
	public BuildTarget get(Os type, boolean is64Bit, boolean isARM, Action<BuildTarget> container) {
		return get(type, is64Bit ? Architecture.Bitness._64 : Architecture.Bitness._32, isARM ? Architecture.ARM : Architecture.x86, container);
	}

	public BuildTarget get(Os type, Architecture.Bitness bitness, Architecture architecture, Action<BuildTarget> container) {
		for(BuildTarget target : targets) {
			if(target.os == type && target.bitness == bitness && target.architecture == architecture) {
				if(container != null)
					container.execute(target);
				return target;
			}
		}
		return null;
	}
	
	public void each(Predicate<BuildTarget> condition, Action<BuildTarget> container) {
		for(BuildTarget target : targets) {
			if(condition.test(target))
				container.execute(target);
		}
	}

	class NativeCodeGeneratorConfig {
		SourceSet sourceSet;
		private String sourceDir;
		String jniDir = "jni";
		String[] includes = null;
		String[] excludes = null;

		public NativeCodeGeneratorConfig(Project project, String subProjectDir) {
			JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class);
			SourceSetContainer sourceSets = javaPlugin.getSourceSets();
			sourceSet = sourceSets.findByName("main");
		}

		@Override
		public String toString() {
			return "NativeCodeGeneratorConfig[sourceDir=`" + sourceDir + "`, sourceSet=`" + sourceSet + "`, jniDir=`"
					+ jniDir + "`, includes=`" + Arrays.toString(includes)
					+ "`, excludes=`" + Arrays.toString(excludes) + "`]";
		}

		public void setSourceDir(String sourceDir) {
			this.sourceDir = sourceDir;
		}

		public String getSourceDir()
		{
			//If already set, use provided value
			if(sourceDir != null) {
				return sourceDir;
			}

			Set<File> javaSrcDirs = sourceSet.getJava().getSrcDirs();
			if (javaSrcDirs.size() == 1) {
				for (File srcDir : javaSrcDirs) {
					sourceDir = srcDir.getPath();
				}
			} else {
				log.error("Multiple java SrcDirs detected. Please manually specify nativeCodeGenerator { sourceDir = \"\"}");
				throw new RuntimeException( "Multiple java SrcDirs detected. Please manually specify nativeCodeGenerator { sourceDir = \"\"}");
			}
			return sourceDir;
		}
	}
	
	class RoboVMXml {
		/**
		 * Use an existing robovm.xml file instead of generating one.
		 */
		private File manualFile = null;
		private List<String> forceLinkClasses = new ArrayList<>();
		private List<RoboVMXmlLib> extraLibs = new ArrayList<>();
		private List<String> extraXCFrameworks = new ArrayList<>();

		public File getManualFile() {
			return manualFile;
		}

		public List<String> getForceLinkClasses() {
			return forceLinkClasses;
		}

		public List<RoboVMXmlLib> getExtraLibs() {
			return extraLibs;
		}

		public List<String> getExtraXCFrameworks() {
			return extraXCFrameworks;
		}

		public void manualFile(File manualFile) {
			if (!forceLinkClasses.isEmpty() || !extraLibs.isEmpty())
				throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
			this.manualFile = manualFile;
		}

		public void forceLinkClasses(String[] forceLinkClasses) {
			if (manualFile != null)
				throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
			this.forceLinkClasses.addAll(Arrays.asList(forceLinkClasses));
		}

		public void extraLib(String path) {
			if (manualFile != null)
				throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
			extraLibs.add(new RoboVMXmlLib(path, null));
		}

		public void extraLib(String path, String variant) {
			if (manualFile != null)
				throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
			extraLibs.add(new RoboVMXmlLib(path, variant));
		}

		public void extraXCFramework(String path) {
			if (manualFile != null)
				throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
			extraXCFrameworks.add(path);
		}

		class RoboVMXmlLib {
			String path;
			String variant;

			public RoboVMXmlLib(String path, String variant) {
				this.path = path;
				this.variant = variant;
			}
		}
	}
}
