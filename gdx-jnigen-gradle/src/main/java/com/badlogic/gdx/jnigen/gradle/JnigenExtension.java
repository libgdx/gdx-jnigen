package com.badlogic.gdx.jnigen.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

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
import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;
import com.badlogic.gdx.jnigen.gradle.JnigenTask.JnigenBuildTargetTask;

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
	
	private static final Logger log = LoggerFactory.getLogger(JnigenExtension.class);
	
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

	public void add(TargetOs type, boolean is64Bit) {
		add(type, is64Bit, false, null);
	}

	public void add(TargetOs type, boolean is64Bit, boolean isARM) {
		add(type, is64Bit, isARM, null);
	}

	public void add(TargetOs type, boolean is64Bit, Action<BuildTarget> container) {
		add(type, is64Bit, false, container);
	}

	public void add(TargetOs type, boolean is64Bit, boolean isARM, Action<BuildTarget> container) {
		BuildTarget target = BuildTarget.newDefaultTarget(type, is64Bit);

		if (all != null)
			all.execute(target);
		if (container != null)
			container.execute(target);

		targets.add(target);

		Task jnigenTask = project.getTasks().getByName("jnigen");
		Task jnigenBuildTask = project.getTasks().getByName("jnigenBuild");
		Task builtTargetTask = project.getTasks().create("jnigenBuild" + type + (is64Bit ? "64" : ""),
				JnigenBuildTargetTask.class, this, target);
		builtTargetTask.dependsOn(jnigenTask);

		if (!target.excludeFromMasterBuildFile
				&& (!target.requireMacOSToBuild || System.getProperty("os.name").contains("Mac")))
			jnigenBuildTask.dependsOn(builtTargetTask);
	}

	class NativeCodeGeneratorConfig {
		String sourceDir;
		String classpath;
		String jniDir = "jni";
		String[] includes = null;
		String[] excludes = null;

		public NativeCodeGeneratorConfig(Project project, String subProjectDir) {
			JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class);
			SourceSetContainer sourceSets = javaPlugin.getSourceSets();
			SourceSet main = sourceSets.findByName("main");
			classpath = main.getRuntimeClasspath().getAsPath();

			Set<File> javaSrcDirs = main.getJava().getSrcDirs();
			for (File f : javaSrcDirs) {
				sourceDir = f.getPath();
			}

			if (javaSrcDirs.size() > 1)
				log.warn("Multiple java SrcDirs detected. Please manually specify sourceDir. We arbitrarily chose " + sourceDir);
		}

		@Override
		public String toString() {
			return "NativeCodeGeneratorConfig[sourceDir=`" + sourceDir + "`, classpath=`" + classpath + "`, jniDir=`"
					+ jniDir + "`, includes=`" + (includes == null ? "null" : Arrays.toString(includes))
					+ "`, excludes=`" + (includes == null ? "null" : Arrays.toString(excludes)) + "`]";
		}
	}
}
