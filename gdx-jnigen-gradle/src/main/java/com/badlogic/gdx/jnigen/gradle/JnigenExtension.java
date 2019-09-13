package com.badlogic.gdx.jnigen.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;

/**
 * @author Desu
 */
public class JnigenExtension {
	public static final boolean x32 = false;
	public static final boolean x64 = true;
	public static final boolean ARM = true;
	public static final TargetOs Windows = TargetOs.Windows;
	public static final TargetOs Linux = TargetOs.Linux;
	public static final TargetOs MacOsX = TargetOs.MacOsX;
	public static final TargetOs Android = TargetOs.Android;
	public static final TargetOs IOS = TargetOs.IOS;
	
	private Project project;

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
	ArrayList<BuildTarget> targets = new ArrayList<BuildTarget>();
	Action<BuildTarget> all = null;
	
	JnigenJarTask jarDesktopNatives = null;
	Task jarAndroidNatives = null;
	JnigenJarTask[] jarAndroidNativesABIs = null;
	JnigenJarTask jarIOSNatives = null;

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
	
	public void add(TargetOs type) {
		add(type, false, false, null);
	}

	public void add(TargetOs type, boolean is64Bit) {
		add(type, is64Bit, false, null);
	}

	public void add(TargetOs type, boolean is64Bit, boolean isARM) {
		add(type, is64Bit, isARM, null);
	}

	public void add(TargetOs type, Action<BuildTarget> container) {
		add(type, false, false, container);
	}

	public void add(TargetOs type, boolean is64Bit, Action<BuildTarget> container) {
		add(type, is64Bit, false, container);
	}

	public void add(TargetOs type, boolean is64Bit, boolean isARM, Action<BuildTarget> container) {
		BuildTarget target = BuildTarget.newDefaultTarget(type, is64Bit, isARM);

		if (all != null)
			all.execute(target);
		if (container != null)
			container.execute(target);

		targets.add(target);

		Task jnigenTask = project.getTasks().getByName("jnigen");
		Task jnigenBuildTask = project.getTasks().getByName("jnigenBuild");
		Task builtTargetTask = project.getTasks().create("jnigenBuild" + type + (isARM ? "ARM" : "") + (is64Bit ? "64" : ""),
				JnigenBuildTargetTask.class, this, target);
		builtTargetTask.dependsOn(jnigenTask);

		if (!target.excludeFromMasterBuildFile
				&& (!target.requireMacOSToBuild || System.getProperty("os.name").contains("Mac")))
			jnigenBuildTask.dependsOn(builtTargetTask);
		
		
		if(type == Android) {
			if(jarAndroidNatives == null)
			{
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
			
			jarAndroidNativesABIs = new JnigenJarTask[abis.length];
			for(int i = 0; i < abis.length; i++) {
				jarAndroidNativesABIs[i] = project.getTasks().create("jnigenJarNativesAndroid"+abis[i], JnigenJarTask.class, type);
				jarAndroidNativesABIs[i].add(target, this, abis[i]);
				
				jarAndroidNatives.dependsOn(jarAndroidNativesABIs[i]);
			}
		} else if(type == IOS) {
			
		}
		else {
			if(jarDesktopNatives == null)
				jarDesktopNatives = project.getTasks().create("jnigenJarNativesDesktop", JnigenJarTask.class, type);
			
			jarDesktopNatives.add(target, this);
		}
	}

	class NativeCodeGeneratorConfig {
		String sourceDir;
		String classpath;
		String jniDir = "jni";
		String[] includes = null;
		String[] excludes = null;
		
		/**
		 * If we detected multiple source dirs and should ask the user to manually define sourceDir.
		 */
		boolean multipleSourceSetDirs = false;

		public NativeCodeGeneratorConfig(Project project, String subProjectDir) {
			JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class);
			SourceSetContainer sourceSets = javaPlugin.getSourceSets();
			SourceSet main = sourceSets.findByName("main");
			classpath = main.getRuntimeClasspath().getAsPath();

			Set<File> javaSrcDirs = main.getJava().getSrcDirs();
			if (javaSrcDirs.size() == 1) {
				for (File srcDir : javaSrcDirs) {
					sourceDir = srcDir.getPath();
				}
			} else {
				multipleSourceSetDirs = true;
			}
		}

		@Override
		public String toString() {
			return "NativeCodeGeneratorConfig[sourceDir=`" + sourceDir + "`, classpath=`" + classpath + "`, jniDir=`"
					+ jniDir + "`, includes=`" + (includes == null ? "null" : Arrays.toString(includes))
					+ "`, excludes=`" + (includes == null ? "null" : Arrays.toString(excludes)) + "`]";
		}
	}
}
