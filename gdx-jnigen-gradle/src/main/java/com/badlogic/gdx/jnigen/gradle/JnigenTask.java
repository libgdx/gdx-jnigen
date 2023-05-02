package com.badlogic.gdx.jnigen.gradle;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.jnigen.AntScriptGenerator;
import com.badlogic.gdx.jnigen.BuildConfig;
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

		setGroup("jnigen");
		setDescription("Generates jnigen native code files and build scripts.");
		dependsOn(ext.nativeCodeGeneratorConfig.sourceSet.getRuntimeClasspath());
	}

	@TaskAction
	public void run() {
		if (ext.sharedLibName == null) {
			log.error("sharedLibName must be defined");
			throw new RuntimeException("sharedLibName must be defined");
		}

		log.debug("subProjectDir " + ext.subProjectDir);
		log.debug("sharedLibName " + ext.sharedLibName);
		log.debug("nativeCodeGeneratorConfig " + ext.nativeCodeGeneratorConfig);

		try {
			String absoluteSourceDir = ext.nativeCodeGeneratorConfig.getSourceDir();
			if(!absoluteSourceDir.startsWith(ext.subProjectDir))
				absoluteSourceDir = ext.subProjectDir + ext.nativeCodeGeneratorConfig.getSourceDir();
			
			new NativeCodeGenerator().generate(absoluteSourceDir,
					ext.nativeCodeGeneratorConfig.sourceSet.getRuntimeClasspath().getAsPath(), ext.subProjectDir + ext.nativeCodeGeneratorConfig.jniDir,
					ext.nativeCodeGeneratorConfig.includes, ext.nativeCodeGeneratorConfig.excludes);
		} catch (Exception e) {
			throw new RuntimeException("NativeCodeGenerator threw exception", e);
		}

		BuildConfig buildConfig = new BuildConfig(ext.sharedLibName, ext.temporaryDir, ext.libsDir,
				ext.subProjectDir + ext.jniDir);
		new AntScriptGenerator().generate(buildConfig, ext.targets.toArray(new BuildTarget[0]));
	}
}
