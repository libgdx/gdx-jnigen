package com.badlogic.gdx.jnigen.gradle;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.tasks.bundling.Jar;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;

/**
 * @author Desu
 */
public class JnigenJarTask extends Jar {
	@Inject
	public JnigenJarTask() {
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
		} else {
			//We are copying LWJGL3's classifier names, including omitting "-x64"
			String os = target.os == TargetOs.MacOsX ? "macos" : target.os.name().toLowerCase();
			if(target.isARM) {
				os += "-" + (target.is64Bit ? "arm64" : "arm32");
			} else if(!target.is64Bit) {
				os += "-x86";
			}

			getArchiveClassifier().set("natives-" + os);
		}
		
		String path = ext.subProjectDir + ext.libsDir + File.separatorChar + targetFolder + File.separatorChar + target.getSharedLibFilename(ext.sharedLibName);
		from(path);
	}
}
