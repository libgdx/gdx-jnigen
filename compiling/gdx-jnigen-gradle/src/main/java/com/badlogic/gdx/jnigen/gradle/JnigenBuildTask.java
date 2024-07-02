package com.badlogic.gdx.jnigen.gradle;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * @author Desu
 */
public class JnigenBuildTask  extends DefaultTask {
	JnigenExtension ext;

	@Inject
	public JnigenBuildTask(JnigenExtension ext) {
		this.ext = ext;
		
		setGroup("jnigen");
		setDescription("Executes all available jnigen build scripts for the current platform.");
	}

	@TaskAction
	public void run() {
		// Empty task
	}
}