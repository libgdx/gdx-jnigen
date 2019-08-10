package com.badlogic.gdx.jnigen.gradle;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.badlogic.gdx.jnigen.AntScriptGenerator;
import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildExecutor;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.NativeCodeGenerator;

/**
 * @author Desu
 */
public class JnigenTask extends DefaultTask {

	JnigenExtension ext;

	@Inject
	public JnigenTask(JnigenExtension ext) {
		this.ext = ext;
	}

	@TaskAction
	public void run() {
		if (ext.sharedLibName == null)
			throw new RuntimeException("sharedLibName must be defined");

		if (ext.debug) {
			System.out.println("subProjectDir " + ext.subProjectDir);
			System.out.println("sharedLibName " + ext.sharedLibName);
			System.out.println("nativeCodeGeneratorConfig " + ext.nativeCodeGeneratorConfig);
		}

		try {
			new NativeCodeGenerator().generate(ext.subProjectDir + ext.nativeCodeGeneratorConfig.sourceDir,
					ext.nativeCodeGeneratorConfig.classpath, ext.subProjectDir + ext.nativeCodeGeneratorConfig.jniDir,
					ext.nativeCodeGeneratorConfig.includes, ext.nativeCodeGeneratorConfig.excludes);
		} catch (Exception e) {
			throw new RuntimeException("NativeCodeGenerator threw exception", e);
		}

		BuildConfig buildConfig = new BuildConfig(ext.sharedLibName, ext.temporaryDir, ext.libsDir,
				ext.subProjectDir + ext.jniDir);
		new AntScriptGenerator().generate(buildConfig, ext.targets.toArray(new BuildTarget[0]));
	}

	static class JnigenBuildTask extends DefaultTask {
		JnigenExtension ext;

		@Inject
		public JnigenBuildTask(JnigenExtension ext) {
			this.ext = ext;
		}

		@TaskAction
		public void run() {
			BuildExecutor.executeAnt(new File(ext.subProjectDir + ext.jniDir, "build.xml").getPath(), "pack-natives");
		}
	}

	static class JnigenBuildTargetTask extends DefaultTask {
		JnigenExtension ext;
		BuildTarget target;

		@Inject
		public JnigenBuildTargetTask(JnigenExtension ext, BuildTarget target) {
			this.ext = ext;
			this.target = target;
		}

		@TaskAction
		public void run() {
			// TODO: Deduplicate the buildFileName defaults
			String buildFileName = "build-" + target.os.toString().toLowerCase() + (target.is64Bit ? "64" : "32")
					+ ".xml";
			if (target.buildFileName != null)
				buildFileName = target.buildFileName;
			BuildExecutor.executeAnt(new File(ext.subProjectDir + ext.jniDir, buildFileName).getPath(),
					"-Drelease=true", "clean", "postcompile");
		}
	}
}
