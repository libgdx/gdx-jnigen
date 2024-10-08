/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.jnigen;

import java.util.ArrayList;

import com.badlogic.gdx.jnigen.FileDescriptor.FileType;
import com.badlogic.gdx.jnigen.commons.Os;

/** <p>Generates Ant scripts for multiple native build targets based on the given {@link BuildConfig}.
 * 
 * For each build target, an Ant build script is created that will compile C/C++ files to a shared library for a specific
 * platform. A master build script is generated that will execute the build scripts for each platform and bundles their shared
 * libraries into a Jar file containing all shared libraries for all desktop platform targets, and armeabi/ and armeabi-v7a/
 * folders containing the shard libraries for Android. The scripts can be executed from the command line or via the
 * {@link BuildExecutor}. The resulting shared libraries can be loaded with the SharedLibraryLoader which will load
 * the correct shared library from the natives jar/arm folders based on the platform the application is running on</p>
 * 
 * A common use case looks like this:
 * 
 * <pre>
 * BuildTarget win32 = BuildTarget.newDefaultTarget(Os.Windows, Architecture.Bitness._32);
 * BuildTarget win64 = BuildTarget.newDefaultTarget(Os.Windows, Architecture.Bitness._64);
 * BuildTarget linux32 = BuildTarget.newDefaultTarget(Os.Linux, Architecture.Bitness._32);
 * BuildTarget linux64 = BuildTarget.newDefaultTarget(Os.Linux, Architecture.Bitness._64);
 * BuildTarget mac = BuildTarget.newDefaultTarget(Os.MacOsX, Architecture.Bitness._32);
 * BuildTarget android = BuildTarget.newDefaultTarget(Os.Android, Architecture.Bitness._32);
 * BuildConfig config = new BuildConfig("mysharedlibrary");
 * 
 * new AntScriptGenerator().generate(config, win32, win64, linux32, linux64, mac, android);
 * BuildExecutor.executeAnt("jni/build.xml", "clean", "all", "-v");
 * 
 * // assuming the natives jar is on the classpath of the application 
 * new SharedLibraryLoader().load("mysharedlibrary)
 * </pre>
 * 
 * <p>This will create the build scripts and execute the build of the shared libraries for each platform, provided that the compilers
 * are available on the system. Mac OS X might have to be treated separately as there are no cross-compilers for it.</p>
 * 
 * <p>The generator will also copy the necessary JNI headers to the jni/jni-headers folder for Windows, Linux and Mac OS X.</p>
 * 
 * @author mzechner */
public class AntScriptGenerator {
	/** Creates a master build script and one build script for each target to generated native shared libraries.
	 * @param config the {@link BuildConfig}
	 * @param targets list of {@link BuildTarget} instances */
	public void generate (BuildConfig config, BuildTarget... targets) {
		// create all the directories for outputing object files, shared libs and natives jar as well as build scripts.
		if (!config.libsDir.exists()) {
			if (!config.libsDir.mkdirs())
				throw new RuntimeException("Couldn't create directory for shared library files in '" + config.libsDir + "'");
		}
		if (!config.jniDir.exists()) {
			if (!config.jniDir.mkdirs())
				throw new RuntimeException("Couldn't create native code directory '" + config.jniDir + "'");
		}

		// copy jni headers
		copyJniHeaders(config.jniDir.path());
		if (!config.jniDir.child("jnigen.h").exists()) {
			new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/jnigen.h", FileType.Classpath).copyTo(
					config.jniDir.child("jnigen.h"));
		}

		// copy memcpy_wrap.c, needed if your build platform uses the latest glibc, e.g. Ubuntu 12.10
		if (!config.jniDir.child("memcpy_wrap.c").exists()) {
			new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/memcpy_wrap.c", FileType.Classpath).copyTo(config.jniDir
				.child("memcpy_wrap.c"));
		}

		ArrayList<String> buildFiles = new ArrayList<String>();
		ArrayList<String> libsDirs = new ArrayList<String>();
		ArrayList<String> sharedLibFiles = new ArrayList<String>();

		// generate an Ant build script for each target
		for (BuildTarget target : targets) {
			String buildFile = generateBuildTargetTemplate(config, target);
			FileDescriptor libsDir = new FileDescriptor(getLibsDirectory(config, target));

			if (!libsDir.exists()) {
				if (!libsDir.mkdirs()) throw new RuntimeException("Couldn't create libs directory '" + libsDir + "'");
			}

			String buildFileName = target.getBuildFilename();
			config.jniDir.child(buildFileName).writeString(buildFile, false);
			System.out.println("Wrote target '" + target.os + target.architecture.toSuffix() + target.bitness.toSuffix() + "' build script '"
				+ config.jniDir.child(buildFileName) + "'");
			if (target.os == Os.IOS) {
				byte[] plist = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/Info.plist.template", FileType.Classpath)
						.readBytes();
				config.jniDir.child("Info.plist").writeBytes(plist, false);
			}

			if (!target.excludeFromMasterBuildFile) {
				if (target.os != Os.MacOsX && target.os != Os.IOS) {
					buildFiles.add(buildFileName);
				}

				String sharedLibFilename = target.getSharedLibFilename(config.sharedLibName);

				if (target.os != Os.Android && target.os != Os.IOS) {
					sharedLibFiles.add(sharedLibFilename);
					libsDirs.add("../" + libsDir.path().replace('\\', '/'));
				}
			}
		}

		// generate the master build script
		String template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/build.xml.template", FileType.Classpath)
			.readString();
		StringBuilder clean = new StringBuilder();
		StringBuilder compile = new StringBuilder();
		StringBuilder pack = new StringBuilder();

		for (int i = 0; i < buildFiles.size(); i++) {
			clean.append("\t\t<ant antfile=\"" + buildFiles.get(i) + "\" target=\"clean\"/>\n");
			compile.append("\t\t<ant antfile=\"" + buildFiles.get(i) + "\"/>\n");
		}
		for (int i = 0; i < libsDirs.size(); i++) {
			pack.append("\t\t\t<fileset dir=\"" + libsDirs.get(i) + "\" includes=\"" + sharedLibFiles.get(i) + "\"/>\n");
		}

		if (config.sharedLibs != null) {
			for (String sharedLib : config.sharedLibs) {
				pack.append("\t\t\t<fileset dir=\"" + sharedLib + "\"/>\n");
			}
		}

		template = template.replace("%projectName%", config.sharedLibName + "-natives");
		template = template.replace("<clean/>", clean.toString());
		template = template.replace("<compile/>", compile.toString());
		template = template.replace("%packFile%", "../" + config.libsDir.path().replace('\\', '/') + "/" + config.sharedLibName
			+ "-natives.jar");
		template = template.replace("<pack/>", pack);

		config.jniDir.child("build.xml").writeString(template, false);
		System.out.println("Wrote master build script '" + config.jniDir.child("build.xml") + "'");
	}

	private void copyJniHeaders (String jniDir) {
		final String pack = "com/badlogic/gdx/jnigen/resources/headers";
		String files[] = {"classfile_constants.h", "jawt.h", "jdwpTransport.h", "jni.h", "linux/jawt_md.h", "linux/jni_md.h",
			"mac/jni_md.h", "win32/jawt_md.h", "win32/jni_md.h"};

		for (String file : files) {
			new FileDescriptor(pack, FileType.Classpath).child(file).copyTo(
				new FileDescriptor(jniDir).child("jni-headers").child(file));
		}
	}

	public static String getLibsDirectory (BuildConfig config, BuildTarget target) {
		return config.libsDir.child(target.getTargetFolder()).path().replace('\\', '/');
	}

	private String generateBuildTargetTemplate (BuildConfig config, BuildTarget target) {
		// special case for android
		if (target.os == Os.Android) {
			new AndroidNdkScriptGenerator().generate(config, target);
			String template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/build-android.xml.template",
				FileType.Classpath).readString();
			template = template.replace("%precompile%", target.preCompileTask == null ? "" : target.preCompileTask);
			template = template.replace("%postcompile%", target.postCompileTask == null ? "" : target.postCompileTask);
			return template;
		}

		// read template file from resources
		String template = null;
		if (target.os == Os.IOS) {
			template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/build-ios.xml.template", FileType.Classpath)
				.readString();
		} else {
			template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/build-target.xml.template", FileType.Classpath)
				.readString();
		}

		// generate shared lib filename and jni platform headers directory name
		String libName = target.getSharedLibFilename(config.sharedLibName);

		// generate include and exclude fileset Ant description for C/C++
		// append memcpy_wrap.c to list of files to be build
		StringBuilder cIncludes = new StringBuilder();
		cIncludes.append("\t\t<include name=\"memcpy_wrap.c\"/>\n");
		for (String cInclude : target.cIncludes) {
			cIncludes.append("\t\t<include name=\"" + cInclude + "\"/>\n");
		}
		StringBuilder cppIncludes = new StringBuilder();
		for (String cppInclude : target.cppIncludes) {
			cppIncludes.append("\t\t<include name=\"" + cppInclude + "\"/>\n");
		}
		StringBuilder cExcludes = new StringBuilder();
		for (String cExclude : target.cExcludes) {
			cExcludes.append("\t\t<exclude name=\"" + cExclude + "\"/>\n");
		}
		StringBuilder cppExcludes = new StringBuilder();
		for (String cppExclude : target.cppExcludes) {
			cppExcludes.append("\t\t<exclude name=\"" + cppExclude + "\"/>\n");
		}

		// generate C/C++ header directories
		StringBuilder headerDirs = new StringBuilder();
		for (String headerDir : target.headerDirs) {
			headerDirs.append("\t\t\t<arg value=\"-I" + headerDir + "\"/>\n");
		}

		// replace template vars with proper values
		template = template.replace("%projectName%", config.sharedLibName + "-" + target.os + "-" + target.architecture.toSuffix() + target.bitness.name().substring(1));
		template = template.replace("%buildDir%", config.buildDir.child(target.getTargetFolder()).path().replace('\\', '/'));
		template = template.replace("%libsDir%", "../" + getLibsDirectory(config, target));
		template = template.replace("%libName%", libName);
		template = template.replace("%xcframeworkName%", config.sharedLibName);
		template = template.replace("%xcframeworkBundleIdentifier%", target.xcframeworkBundleIdentifier == null ? ("gdx.jnigen." + config.sharedLibName) : target.xcframeworkBundleIdentifier);
		template = template.replace("%minIOSVersion%", target.minIOSVersion);
		template = template.replace("%jniPlatform%", target.os.getJniPlatform());
		template = template.replace("%cCompiler%", target.cCompiler);
		template = template.replace("%cppCompiler%", target.cppCompiler);
		template = template.replace("%archiver%", target.archiver);
		template = template.replace("%compilerPrefix%", target.compilerPrefix);
		template = template.replace("%compilerSuffix%", target.compilerSuffix);
		template = template.replace("%cFlags%", target.cFlags);
		template = template.replace("%cppFlags%", target.cppFlags);
		template = template.replace("%linkerFlags%", target.linkerFlags);
		template = template.replace("%archiverFlags%", target.archiverFlags);
		template = template.replace("%libraries%", target.libraries);
		template = template.replace("%cIncludes%", cIncludes.toString().trim());
		template = template.replace("%cExcludes%", cExcludes.toString().trim());
		template = template.replace("%cppIncludes%", cppIncludes.toString().trim());
		template = template.replace("%cppExcludes%", cppExcludes.toString().trim());
		template = template.replace("%headerDirs%", headerDirs.toString().trim());
		template = template.replace("%precompile%", target.preCompileTask == null ? "" : target.preCompileTask);
		template = template.replace("%postcompile%", target.postCompileTask == null ? "" : target.postCompileTask);

		return template;
	}
}
