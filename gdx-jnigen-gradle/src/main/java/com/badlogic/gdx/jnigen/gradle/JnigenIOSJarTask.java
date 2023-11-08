package com.badlogic.gdx.jnigen.gradle;

import java.io.File;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.utils.Os;

public class JnigenIOSJarTask extends JnigenJarTask {
	public JnigenIOSJarTask() {
		super(Os.IOS);
	}

	public void add(BuildTarget target, JnigenExtension ext, String abi) {
		String targetFolder = target.getTargetFolder();

		String path = ext.subProjectDir + ext.libsDir + File.separatorChar + targetFolder;
		from(path, (copySpec) -> {
			copySpec.include("**/*.xcframework/");
			copySpec.include("**/*.framework/");
			copySpec.include("**/*.a");
			copySpec.into("META-INF/robovm/ios/libs");
		});

		File robovmXml = new File(getProject().getBuildDir(), "robovm.xml");
		from(robovmXml, (copySpec) -> {
			copySpec.into("META-INF/robovm/ios");
			copySpec.rename(".*", "robovm.xml");
		});
	}
}
