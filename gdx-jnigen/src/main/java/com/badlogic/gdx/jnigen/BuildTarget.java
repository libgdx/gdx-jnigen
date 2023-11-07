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

import com.badlogic.gdx.utils.Architecture;
import com.badlogic.gdx.utils.Os;

import java.util.function.BooleanSupplier;

/** Defines the configuration for building a native shared library for a specific platform. Used with {@link AntScriptGenerator}
 * to create Ant build files that invoke the compiler toolchain to create the shared libraries. */
public class BuildTarget {
	/** the target operating system **/
	public Os os;
	/** whether this is a 32-bit, 64-bit or 128-bit build, not used for Android **/
	public Architecture.Bitness bitness;
	/** whether this is an x86, ARM or RISC-V build, not used for Android **/
	public Architecture architecture = Architecture.x86;
	/** the C files and directories to be included in the build, accepts Ant path format, must not be null **/
	public String[] cIncludes;
	/** the C files and directories to be excluded from the build, accepts Ant path format, must not be null **/
	public String[] cExcludes;
	/** the C++ files and directories to be included in the build, accepts Ant path format, must not be null **/
	public String[] cppIncludes;
	/** the C++ files and directories to be excluded from the build, accepts Ant path format, must not be null **/
	public String[] cppExcludes;
	/** the directories containing headers for the build, must not be null **/
	public String[] headerDirs;
	/** the compiler to use when compiling c files. Usually gcc or clang, must not be null */
	public String cCompiler = "gcc";
	/** the compiler to use when compiling c++ files. Usually g++ or clang++, must not be null */
	public String cppCompiler = "g++";
	/** the command to use when archiving files. Usually ar, must not be null */
	public String archiver = "ar";
	/** prefix for the compiler (g++, gcc), useful for cross compilation, must not be null **/
	public String compilerPrefix = "";
	/** suffix for the compiler (g++, gcc), useful for cross compilation, must not be null **/
	public String compilerSuffix = "";
	/** the flags passed to the C compiler, must not be null **/
	public String cFlags;
	/** the flags passed to the C++ compiler, must not be null **/
	public String cppFlags;
	/** the flags passed to the linker, must not be null **/
	public String linkerFlags;
	/** the flags passed to the archiver, must not be null **/
	public String archiverFlags = "rcs";
	/** the name of the generated build file for this target, defaults to "build-${target}(64)?.xml", must not be null **/
	public String buildFileName;
	/** whether to exclude this build target from the master build file, useful for debugging **/
	public boolean excludeFromMasterBuildFile = false;
	/** Ant XML executed in a target before compilation **/
	public String preCompileTask;
	/** Ant Xml executed in a target after compilation **/
	public String postCompileTask;
	/** the libraries to be linked to the output, specify via e.g. -ldinput -ldxguid etc. **/
	public String libraries;
	/** The name used for folders for this specific target. Defaults to "${target}(64)" **/
	public String osFileName;
	/** The name used for the library file. This is a full file name, including file extension. Default is platform specific. E.g.
	 * "lib{sharedLibName}64.so" **/
	public String libName;
	/** Condition to check if build this target */
	public BooleanSupplier canBuild = () -> !System.getProperty("os.name").contains("Mac");
	
	/** List of ABIs we wish to build for Android. Defaults to all available in current NDK.
	 * <a href="https://developer.android.com/ndk/guides/application_mk#app_abi">https://developer.android.com/ndk/guides/application_mk#app_abi</a> **/
	public String[] androidABIs = {"all"};
	/** Extra lines which will be added to Android's Android.mk */
	public String[] androidAndroidMk = {};
	/** Extra lines which will be added to Android's Application.mk */
	public String[] androidApplicationMk = {};
	/** ios framework bundle identifier, if null an automatically generated bundle identifier will be used */
	public String xcframeworkBundleIdentifier = null;
	/** Minimum supported iOS version, will default to iOS 11*/
	public String minIOSVersion = "11.0";

	/** Creates a new build target. See members of this class for a description of the parameters. */
	public BuildTarget(Os targetType, Architecture.Bitness bitness, String[] cIncludes, String[] cExcludes, String[] cppIncludes, String[] cppExcludes, String[] headerDirs, String compilerPrefix, String cFlags, String cppFlags, String linkerFlags) {
		if (targetType == null) throw new IllegalArgumentException("targetType must not be null");
		if (cIncludes == null) cIncludes = new String[0];
		if (cExcludes == null) cExcludes = new String[0];
		if (cppIncludes == null) cppIncludes = new String[0];
		if (cppExcludes == null) cppExcludes = new String[0];
		if (headerDirs == null) headerDirs = new String[0];
		if (compilerPrefix == null) compilerPrefix = "";
		if (cFlags == null) cFlags = "";
		if (cppFlags == null) cppFlags = "";
		if (linkerFlags == null) linkerFlags = "";

		this.os = targetType;
		this.bitness = bitness;
		this.cIncludes = cIncludes;
		this.cExcludes = cExcludes;
		this.cppIncludes = cppIncludes;
		this.cppExcludes = cppExcludes;
		this.headerDirs = headerDirs;
		this.compilerPrefix = compilerPrefix;
		this.cFlags = cFlags;
		this.cppFlags = cppFlags;
		this.linkerFlags = linkerFlags;
		this.libraries = "";
	}

	public String getBuildFilename () {
		// Use specified buildFileName if it is user provided
		if (buildFileName != null && !buildFileName.isEmpty())
			return buildFileName;

		return "build-" + os.toString().toLowerCase() + architecture.toSuffix() + bitness.name().substring(1) + ".xml";
	}

	public String getSharedLibFilename (String sharedLibName) {
		// Use specified libName if it is user provided
		if (libName != null && !libName.isEmpty())
			return libName;

		// generate shared lib prefix and suffix, determine jni platform headers directory
		return os.getLibPrefix() + sharedLibName + architecture.toSuffix() + bitness.toSuffix() + "." + os.getLibExtension();
	}

	public String getTargetFolder () {
		// Use specified osFileName if it is user provided
		if (osFileName != null && !osFileName.isEmpty())
			return osFileName;

		return os.toString().toLowerCase() + architecture.toSuffix() + bitness.name().substring(1);
	}

	/** Creates a new default BuildTarget for the given OS, using common default values.
	 * @deprecated Use {@link #newDefaultTarget(Os, Architecture.Bitness) newDefaultTarget} method.*/
	@Deprecated
	public static BuildTarget newDefaultTarget (Os type, boolean is64Bit) {
		return newDefaultTarget(type, is64Bit, false);
	}

	/** Creates a new default BuildTarget for the given OS, using common default values. */
	public static BuildTarget newDefaultTarget (Os type, Architecture.Bitness bitness) {
		return newDefaultTarget(type, bitness, Architecture.x86);
	}

	/** Creates a new default BuildTarget for the given OS, using common default values.
	 * @deprecated Use {@link #newDefaultTarget(Os, Architecture.Bitness, Architecture) newDefaultTarget} method.*/
	@Deprecated
	public static BuildTarget newDefaultTarget (Os type, boolean is64Bit, boolean isARM) {
		return newDefaultTarget(type, is64Bit ? Architecture.Bitness._64 : Architecture.Bitness._32, isARM ? Architecture.ARM : Architecture.x86);
	}

	/** Creates a new default BuildTarget for the given OS, using common default values. */
	public static BuildTarget newDefaultTarget (Os type, Architecture.Bitness bitness, Architecture architecture) {
		if (type == Os.Windows && bitness == Architecture.Bitness._32) {
			// Windows 32-Bit
			return new BuildTarget(Os.Windows, Architecture.Bitness._32, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "i686-w64-mingw32-", "-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m32",
				"-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m32",
				"-Wl,--kill-at -shared -m32 -static -static-libgcc -static-libstdc++");
		}

		if (type == Os.Windows && bitness == Architecture.Bitness._64) {
			// Windows 64-Bit
			return new BuildTarget(Os.Windows, Architecture.Bitness._64, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "x86_64-w64-mingw32-", "-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m64",
				"-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m64",
				"-Wl,--kill-at -shared -static -static-libgcc -static-libstdc++ -m64");
		}

		if (type == Os.Linux && architecture == Architecture.RISCV && bitness == Architecture.Bitness._32) {
			// Linux RISCV 32-Bit
			BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._32, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
					new String[0], new String[0], "riscv32-linux-gnu-", "-c -Wall -O2 -fmessage-length=0 -fPIC",
					"-c -Wall -O2 -fmessage-length=0 -fPIC", "-shared");
			target.architecture = Architecture.RISCV;
			return target;
		}

		if (type == Os.Linux && architecture == Architecture.RISCV && bitness == Architecture.Bitness._64) {
			// Linux RISCV 64-Bit
			BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._64, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
					new String[0], new String[0], "riscv64-linux-gnu-", "-c -Wall -O2 -fmessage-length=0 -fPIC",
					"-c -Wall -O2 -fmessage-length=0 -fPIC", "-shared");
			target.architecture = Architecture.RISCV;
			return target;
		}

		if (type == Os.Linux && architecture == Architecture.ARM && bitness == Architecture.Bitness._32) {
			// Linux ARM 32-Bit hardfloat
			BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._32, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "arm-linux-gnueabihf-", "-c -Wall -O2 -fmessage-length=0 -fPIC",
				"-c -Wall -O2 -fmessage-length=0 -fPIC", "-shared");
			target.architecture = Architecture.ARM;
			return target;
		}

		if (type == Os.Linux && architecture == Architecture.ARM && bitness == Architecture.Bitness._64) {
			// Linux ARM 64-Bit
			BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._64, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "aarch64-linux-gnu-", "-c -Wall -O2 -fmessage-length=0 -fPIC",
				"-c -Wall -O2 -fmessage-length=0 -fPIC", "-shared");
			target.architecture = Architecture.ARM;
			return target;
		}

		if (type == Os.Linux && bitness == Architecture.Bitness._32) {
			// Linux 32-Bit
			return new BuildTarget(Os.Linux, Architecture.Bitness._32, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "", "-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m32 -fPIC",
				"-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m32 -fPIC", "-shared -m32");
		}

		if (type == Os.Linux && bitness == Architecture.Bitness._64) {
			// Linux 64-Bit
			return new BuildTarget(Os.Linux, Architecture.Bitness._64, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "", "-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m64 -fPIC",
				"-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m64 -fPIC", "-shared -m64 -Wl,-wrap,memcpy");
		}

		if (type == Os.MacOsX && bitness == Architecture.Bitness._32) {
			throw new RuntimeException("macOS 32-bit not supported");
		}

		if (type == Os.MacOsX && bitness == Architecture.Bitness._64 && architecture == Architecture.ARM) {
			// Mac OS aarch64
			BuildTarget mac = new BuildTarget(Os.MacOsX, Architecture.Bitness._64, new String[] {"**/*.c"}, new String[0],
				new String[] {"**/*.cpp"}, new String[0], new String[0], "",
				"-c -Wall -O2 -arch arm64 -DFIXED_POINT -fmessage-length=0 -fPIC -mmacosx-version-min=10.7 -stdlib=libc++",
				"-c -Wall -O2 -arch arm64 -DFIXED_POINT -fmessage-length=0 -fPIC -mmacosx-version-min=10.7 -stdlib=libc++",
				"-shared -arch arm64 -mmacosx-version-min=10.7 -stdlib=libc++");
			mac.cCompiler = "clang";
			mac.cppCompiler = "clang++";
			mac.canBuild = () -> System.getProperty("os.name").contains("Mac");
			mac.architecture = Architecture.ARM;
			return mac;
		}

		if (type == Os.MacOsX && bitness == Architecture.Bitness._64) {
			// Mac OS x86_64
			BuildTarget mac = new BuildTarget(Os.MacOsX, Architecture.Bitness._64, new String[] {"**/*.c"}, new String[0],
				new String[] {"**/*.cpp"}, new String[0], new String[0], "",
				"-c -Wall -O2 -arch x86_64 -DFIXED_POINT -fmessage-length=0 -fPIC -mmacosx-version-min=10.7 -stdlib=libc++",
				"-c -Wall -O2 -arch x86_64 -DFIXED_POINT -fmessage-length=0 -fPIC -mmacosx-version-min=10.7 -stdlib=libc++",
				"-shared -arch x86_64 -mmacosx-version-min=10.7 -stdlib=libc++");
			mac.cCompiler = "clang";
			mac.cppCompiler = "clang++";
			mac.canBuild = () -> System.getProperty("os.name").contains("Mac");
			return mac;
		}

		if (type == Os.Android) {
			BuildTarget android = new BuildTarget(Os.Android, Architecture.Bitness._32, new String[] {"**/*.c"}, new String[0],
				new String[] {"**/*.cpp"}, new String[0], new String[0], "", "-O2 -Wall -D__ANDROID__", "-O2 -Wall -D__ANDROID__",
				"-lm");
			return android;
		}

		if(type == Os.IOS) {
			// iOS, x86_64 simulator, armv7, and arm64 compiled to fat static lib
			BuildTarget ios = new BuildTarget(Os.IOS, Architecture.Bitness._32, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
					new String[0], new String[0], "", "-c -Wall -O2 -stdlib=libc++", "-c -Wall -O2 -stdlib=libc++",
					"-shared -stdlib=libc++");
			ios.cCompiler = "clang";
			ios.cppCompiler = "clang++";
			ios.canBuild = () -> System.getProperty("os.name").contains("Mac");
			return ios;
		}

		throw new RuntimeException("Unknown target type");
	}
}
