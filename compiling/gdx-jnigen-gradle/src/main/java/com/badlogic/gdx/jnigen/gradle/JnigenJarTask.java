package com.badlogic.gdx.jnigen.gradle;

import java.io.File;

import javax.inject.Inject;

import com.badlogic.gdx.jnigen.commons.Os;
import org.gradle.api.tasks.bundling.Jar;

import com.badlogic.gdx.jnigen.BuildTarget;

/**
 * @author Desu
 */
public class JnigenJarTask extends Jar {
	@Inject
	public JnigenJarTask(Os os) {
		switch(os) {
			case Android:
				getArchiveClassifier().set("natives-android");
				break;
			case IOS:
				getArchiveClassifier().set("natives-ios");
				break;
			default:
				getArchiveClassifier().set("natives-desktop");
				break;
		}

		setGroup("jnigen");
		setDescription("Assembles a jar archive containing the native libraries.");
	}
	
	public final void add(BuildTarget target, JnigenExtension ext) {
		add(target, ext, null);
	}
	
	public void add(BuildTarget target, JnigenExtension ext, String abi) {
		String targetFolder = target.getTargetFolder();
		
		if (abi != null && !abi.isEmpty()) {
			targetFolder = abi;
			getArchiveClassifier().set("natives-" + abi);
		}
		
		String path = ext.subProjectDir + ext.libsDir + File.separatorChar + targetFolder + File.separatorChar;
		from(path);
	}
}
