package com.badlogic.gdx.jnigen.gradle;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.jnigen.AntScriptGenerator;
import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildExecutor;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.NativeCodeGenerator;

/**
 * @author Desu
 */
public class JnigenTask extends DefaultTask {
	private static final Logger log = LoggerFactory.getLogger(JnigenTask.class);
	
	JnigenExtension ext;

	@Inject
	public JnigenTask(JnigenExtension ext) {
		this.ext = ext;
	}

	@TaskAction
	public void run() {
		if (ext.sharedLibName == null)
			throw new RuntimeException("sharedLibName must be defined");

		log.debug("subProjectDir " + ext.subProjectDir);
		log.debug("sharedLibName " + ext.sharedLibName);
		log.debug("nativeCodeGeneratorConfig " + ext.nativeCodeGeneratorConfig);

		try {
			String absoluteSourceDir = ext.nativeCodeGeneratorConfig.sourceDir;
			if(!absoluteSourceDir.startsWith(ext.subProjectDir))
				absoluteSourceDir = ext.subProjectDir + ext.nativeCodeGeneratorConfig.sourceDir;
			
			new NativeCodeGenerator().generate(absoluteSourceDir,
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
					"-Drelease=" + ext.release, "clean", "postcompile");
		}
	}
}
