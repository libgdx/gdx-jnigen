package com.badlogic.gdx.jnigen.gradle;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.badlogic.gdx.jnigen.BuildExecutor;
import com.badlogic.gdx.jnigen.BuildTarget;

/**
 * @author Desu
 */
public class JnigenBuildTargetTask extends DefaultTask {
	JnigenExtension ext;
	BuildTarget target;

	@Inject
	public JnigenBuildTargetTask(JnigenExtension ext, BuildTarget target) {
		this.ext = ext;
		this.target = target;
		
		setGroup("jnigen");
		setDescription("Executes all jnigen build script for build target " + target.getTargetFolder() + ".");
	}

	@TaskAction
	public void run() {
		boolean verbose = getProject().findProperty("VERBOSE") != null;
		if(!BuildExecutor.executeAnt(new File(ext.subProjectDir + ext.jniDir, target.getBuildFilename()).getPath(),
				"-Drelease=" + ext.release, "clean", "postcompile", verbose ? "-v" : "")) {
			throw new RuntimeException("Ant execution for " + target.getBuildFilename() + " failed.");
		}
	}
}