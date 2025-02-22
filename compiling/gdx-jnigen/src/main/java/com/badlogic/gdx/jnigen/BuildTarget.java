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


import com.badlogic.gdx.jnigen.commons.*;

import java.io.File;

/**
 * Defines the configuration for building a native shared library for a specific platform.
 */
public class BuildTarget {
    /**
     * the target operating system
     **/
    public Os os;
    /**
     * whether this is a 32-bit, 64-bit or 128-bit build, not used for Android
     **/
    public Architecture.Bitness bitness;
    /**
     * whether this is an x86, ARM or RISC-V build, not used for Android
     **/
    public Architecture architecture = Architecture.x86;

    /**
     * Compiler ABI type, GCC/clang compatible or MSVC
     **/
    public CompilerABIType compilerABIType = CompilerABIType.GCC_CLANG;

    /**
     * the C files and directories to be included in the build, accepts Ant path format, must not be null
     **/
    public String[] cIncludes;
    /**
     * the C files and directories to be excluded from the build, accepts Ant path format, must not be null
     **/
    public String[] cExcludes;
    /**
     * the C++ files and directories to be included in the build, accepts Ant path format, must not be null
     **/
    public String[] cppIncludes;
    /**
     * the C++ files and directories to be excluded from the build, accepts Ant path format, must not be null
     **/
    public String[] cppExcludes;
    /**
     * the directories containing headers for the build, must not be null
     **/
    public String[] headerDirs;
    /**
     * the compiler to use when compiling c files. Usually gcc or clang, must not be null
     */
    public String cCompiler = "gcc";
    /**
     * the compiler to use when compiling c++ files. Usually g++ or clang++, must not be null
     */
    public String cppCompiler = "g++";
    /**
     * prefix for the compiler (g++, gcc), useful for cross compilation, must not be null
     **/
    public String compilerPrefix = "";
    /**
     * suffix for the compiler (g++, gcc), useful for cross compilation, must not be null
     **/
    public String compilerSuffix = "";
    /**
     * the flags passed to the C compiler, must not be null
     **/
    public String[] cFlags;
    /**
     * the flags passed to the C++ compiler, must not be null
     **/
    public String[] cppFlags;
    /**
     * the flags passed to the linker, must not be null
     **/
    public String[] linkerFlags;
    /**
     * Pre linker flags for msvc, required separately during linking to provide compiler flags
     **/
    public String[] msvcPreLinkerFlags;
    /**
     * the name of the generated build file for this target, defaults to "build-${target}(64)?.xml", must not be null
     **/
    public String buildFileName;
    /**
     * whether to exclude this build target from the master build file, useful for debugging
     **/
    public boolean excludeFromMasterBuildFile = false;
    /**
     * the libraries to be linked to the output, specify via e.g. -ldinput -ldxguid etc.
     **/
    public String[] libraries;
    /**
     * The name used for folders for this specific target. Defaults to "${target}(64)"
     **/
    public String osFileName;
    /**
     * The name used for the library file. This is a full file name, including file extension. Default is platform specific. E.g.
     * "lib{sharedLibName}64.so"
     **/
    public String libName;

    /**
     * Extra lines which will be added to Android's Android.mk
     */
    public String[] androidAndroidMk = {};
    /**
     * Extra lines which will be added to Android's Application.mk
     */
    public String[] androidApplicationMk = {};
    /**
     * Extra lines which will be added to Android's Android.mk `BUILD_SHARED_LIBRARY` module
     */
    public String[] androidAndroidMkSharedLibModule = {};
    /**
     * Is the target a simulator
     */
    public TargetType targetType = TargetType.DEVICE;

    /**
     * If this is a release build or not
     */
    public boolean release;

    /**
     * Override for building abi on android
     **/
    private AndroidABI targetAndroidABI;

    /**
     * Creates a new build target. See members of this class for a description of the parameters.
     */
    public BuildTarget (Os targetOs, Architecture.Bitness bitness, String[] cIncludes, String[] cExcludes, String[] cppIncludes, String[] cppExcludes, String[] headerDirs, String compilerPrefix, String[] cFlags, String[] cppFlags, String[] linkerFlags, String[] msvcPreLinkerFlags) {
        if (targetOs == null) throw new IllegalArgumentException("targetOs must not be null");
        if (cIncludes == null) cIncludes = new String[0];
        if (cExcludes == null) cExcludes = new String[0];
        if (cppIncludes == null) cppIncludes = new String[0];
        if (cppExcludes == null) cppExcludes = new String[0];
        if (headerDirs == null) headerDirs = new String[0];
        if (compilerPrefix == null) compilerPrefix = "";
        if (cFlags == null) cFlags = new String[0];
        if (cppFlags == null) cppFlags = new String[0];
        if (linkerFlags == null) linkerFlags = new String[0];
        if (msvcPreLinkerFlags == null) msvcPreLinkerFlags = new String[0];

        this.os = targetOs;
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
        this.msvcPreLinkerFlags = msvcPreLinkerFlags;
        this.libraries = new String[0];
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

        String suffix = os.getLibExtension().isEmpty() ? "" : "." + os.getLibExtension();

        // generate shared lib prefix and suffix, determine jni platform headers directory
        return os.getLibPrefix() + sharedLibName + architecture.toSuffix() + bitness.toSuffix() + suffix;
    }

    public String getTargetFolder () {
        // Use specified osFileName if it is user provided
        if (osFileName != null && !osFileName.isEmpty())
            return osFileName;

        return os.toString().toLowerCase() + architecture.toSuffix() + bitness.name().substring(1);
    }

    /**
     * Creates a new default BuildTarget for the given OS, using common default values.
     */
    public static BuildTarget newDefaultTarget (Os osTarget, Architecture.Bitness bitness) {
        return newDefaultTarget(osTarget, bitness, Architecture.x86);
    }

    public static BuildTarget newDefaultTarget (Os osTarget, Architecture.Bitness bitness, Architecture architecture) {
        return newDefaultTarget(osTarget, bitness, architecture, CompilerABIType.GCC_CLANG);
    }

    public static BuildTarget newDefaultTarget (Os osTarget, Architecture.Bitness bitness, Architecture architecture, CompilerABIType abiType) {
        return newDefaultTarget(osTarget, bitness, architecture, abiType, TargetType.DEVICE);
    }

    /**
     * Creates a new default BuildTarget for the given OS, using common default values.
     */
    public static BuildTarget newDefaultTarget (Os osTarget, Architecture.Bitness bitness, Architecture architecture, CompilerABIType abiType, TargetType targetType) {

        if (abiType == CompilerABIType.MSVC) {
            if (osTarget == Os.Windows && architecture == Architecture.x86 && bitness == Architecture.Bitness._32) {
                // Windows x86 32-Bit
                BuildTarget target = new BuildTarget(Os.Windows, Architecture.Bitness._32, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "", new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[]{"/DLL"}, new String[]{"/MT"});
                target.cCompiler = "cl.exe";
                target.cppCompiler = "cl.exe";
                target.compilerABIType = abiType;
                return target;
            }

            if (osTarget == Os.Windows && architecture == Architecture.x86 && bitness == Architecture.Bitness._64) {
                // Windows x86 64-Bit
                BuildTarget target = new BuildTarget(Os.Windows, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "", new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[0], new String[]{"/MT"});
                target.compilerABIType = abiType;
                target.cCompiler = "cl.exe";
                target.cppCompiler = "cl.exe";
                return target;
            }

            if (osTarget == Os.Windows && architecture == Architecture.ARM && bitness == Architecture.Bitness._32) {
                // Windows ARM 32-Bit
                BuildTarget target = new BuildTarget(Os.Windows, Architecture.Bitness._32, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "", new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[0], new String[]{"/MT"});
                target.cCompiler = "cl.exe";
                target.cppCompiler = "cl.exe";
                target.architecture = Architecture.ARM;
                target.compilerABIType = abiType;
                return target;
            }

            if (osTarget == Os.Windows && architecture == Architecture.ARM && bitness == Architecture.Bitness._64) {
                // Windows ARM 64-Bit
                BuildTarget target = new BuildTarget(Os.Windows, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "", new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[]{"/O2", "/W4", "/fp:fast"},
                        new String[0], new String[]{"/MT"});
                target.cCompiler = "cl.exe";
                target.cppCompiler = "cl.exe";
                target.architecture = Architecture.ARM;
                target.compilerABIType = abiType;
                return target;
            }
        } else {
            if (osTarget == Os.Windows && architecture == Architecture.x86 && bitness == Architecture.Bitness._32) {
                // Windows x86 32-Bit
                return new BuildTarget(Os.Windows, Architecture.Bitness._32, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "i686-w64-mingw32-", new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse2", "-fmessage-length=0", "-m32"},
                        new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse2", "-fmessage-length=0", "-m32"},
                        new String[] {"-Wl,--kill-at", "-shared", "-static", "-static-libgcc", "-static-libstdc++", "-m32"}, null);
            }

            if (osTarget == Os.Windows && architecture == Architecture.x86 && bitness == Architecture.Bitness._64) {
                // Windows x86 64-Bit
                return new BuildTarget(Os.Windows, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "x86_64-w64-mingw32-", new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse2", "-fmessage-length=0", "-m64"},
                        new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse2", "-fmessage-length=0", "-m64"},
                        new String[] {"-Wl,--kill-at", "-shared", "-static", "-static-libgcc", "-static-libstdc++", "-m64"}, null);
            }

            if (osTarget == Os.Windows && architecture == Architecture.ARM && bitness == Architecture.Bitness._32) {
                // Windows ARM 32-Bit
                BuildTarget target = new BuildTarget(Os.Windows, Architecture.Bitness._32, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "armv7-w64-mingw32-", new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0"},
                        new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0"},
                        new String[] {"-Wl,--kill-at", "-shared", "-static", "-static-libgcc", "-static-libstdc++"}, null);
                target.architecture = Architecture.ARM;
                return target;
            }

            if (osTarget == Os.Windows && architecture == Architecture.ARM && bitness == Architecture.Bitness._64) {
                // Windows ARM 64-Bit
                BuildTarget target = new BuildTarget(Os.Windows, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                        new String[0], new String[0], "aarch64-w64-mingw32-", new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0"},
                        new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0"},
                        new String[] {"-Wl,--kill-at", "-shared", "-static", "-static-libgcc", "-static-libstdc++"}, null);
                target.architecture = Architecture.ARM;
                return target;
            }
        }

        if (osTarget == Os.Linux && architecture == Architecture.LOONGARCH && bitness == Architecture.Bitness._64) {
            // Linux LoongArch 64-Bit
            BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "loongarch64-unknown-linux-gnu-", new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"},
                    new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"}, new String[] {"-shared"}, null);
            target.architecture = Architecture.LOONGARCH;
            return target;
        }

        if (osTarget == Os.Linux && architecture == Architecture.RISCV && bitness == Architecture.Bitness._32) {
            // Linux RISCV 32-Bit
            BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._32, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "riscv32-linux-gnu-", new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"},
                    new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"}, new String[] {"-shared"}, null);
            target.architecture = Architecture.RISCV;
            return target;
        }

        if (osTarget == Os.Linux && architecture == Architecture.RISCV && bitness == Architecture.Bitness._64) {
            // Linux RISCV 64-Bit
            BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "riscv64-linux-gnu-", new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"},
                    new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"}, new String[] {"-shared"}, null);
            target.architecture = Architecture.RISCV;
            return target;
        }

        if (osTarget == Os.Linux && architecture == Architecture.ARM && bitness == Architecture.Bitness._32) {
            // Linux ARM 32-Bit hardfloat
            BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._32, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "arm-linux-gnueabihf-", new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"},
                    new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"}, new String[] {"-shared"}, null);
            target.architecture = Architecture.ARM;
            return target;
        }

        if (osTarget == Os.Linux && architecture == Architecture.ARM && bitness == Architecture.Bitness._64) {
            // Linux ARM 64-Bit
            BuildTarget target = new BuildTarget(Os.Linux, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "aarch64-linux-gnu-", new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"},
                    new String[]{"-c", "-Wall", "-O2", "-fmessage-length=0", "-fPIC"}, new String[] {"-shared"}, null);
            target.architecture = Architecture.ARM;
            return target;
        }

        if (osTarget == Os.Linux && bitness == Architecture.Bitness._32) {
            // Linux 32-Bit
            return new BuildTarget(Os.Linux, Architecture.Bitness._32, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "", new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse", "-fmessage-length=0", "-fPIC", "-m32"},
                    new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse", "-fmessage-length=0", "-fPIC", "-m32"}, new String[] {"-shared", "-m32"}, null);
        }

        if (osTarget == Os.Linux && bitness == Architecture.Bitness._64) {
            // Linux 64-Bit
            return new BuildTarget(Os.Linux, Architecture.Bitness._64, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "", new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse", "-fmessage-length=0", "-fPIC", "-m64"},
                    new String[]{"-c", "-Wall", "-O2", "-mfpmath=sse", "-msse", "-fmessage-length=0", "-fPIC", "-m64"}, new String[] {"-shared", "-m64"}, null);
        }

        if (osTarget == Os.MacOsX && bitness == Architecture.Bitness._32) {
            throw new RuntimeException("macOS 32-bit not supported");
        }

        if (osTarget == Os.MacOsX && bitness == Architecture.Bitness._64 && architecture == Architecture.ARM) {
            // Mac OS aarch64
            BuildTarget mac = new BuildTarget(Os.MacOsX, Architecture.Bitness._64, new String[]{""}, new String[0],
                    new String[]{""}, new String[0], new String[0], "",
                    new String[] {"-c", "-Wall", "-O2", "-arch", "arm64", "-DFIXED_POINT", "-fmessage-length=0", "-fPIC", "-mmacosx-version-min=10.7", "-stdlib=libc++"},
                    new String[] {"-c", "-Wall", "-O2", "-arch", "arm64", "-DFIXED_POINT", "-fmessage-length=0", "-fPIC", "-mmacosx-version-min=10.7", "-stdlib=libc++"},
                    new String[] {"-shared", "-arch", "arm64", "-mmacosx-version-min=10.7", "-stdlib=libc++"}, null);
            mac.cCompiler = "clang";
            mac.cppCompiler = "clang++";
            mac.architecture = Architecture.ARM;
            return mac;
        }

        if (osTarget == Os.MacOsX && bitness == Architecture.Bitness._64) {
            // Mac OS x86_64
            BuildTarget mac = new BuildTarget(Os.MacOsX, Architecture.Bitness._64, new String[]{""}, new String[0],
                    new String[]{""}, new String[0], new String[0], "",
                    new String[] {"-c", "-Wall", "-O2", "-arch", "x86_64", "-DFIXED_POINT", "-fmessage-length=0", "-fPIC", "-mmacosx-version-min=10.7", "-stdlib=libc++"},
                    new String[] {"-c", "-Wall", "-O2", "-arch", "x86_64", "-DFIXED_POINT", "-fmessage-length=0", "-fPIC", "-mmacosx-version-min=10.7", "-stdlib=libc++"},
                    new String[] {"-shared", "-arch", "x86_64", "-mmacosx-version-min=10.7", "-stdlib=libc++"}, null);
            mac.cCompiler = "clang";
            mac.cppCompiler = "clang++";
            return mac;
        }

        if (osTarget == Os.Android) {
            BuildTarget android = new BuildTarget(Os.Android, Architecture.Bitness._32, new String[]{""}, new String[0],
                    new String[]{""}, new String[0], new String[0], "", new String[] {"-O2", "-Wall", "-D__ANDROID__"}, new String[] {"-O2", "-Wall", "-D__ANDROID__"},
                    new String[] {"-lm", "-Wl,-z,max-page-size=0x4000"}, null);
            return android;
        }

        if (osTarget == Os.IOS) {
            // iOS, x86_64 simulator, armv7, and arm64 compiled to fat static lib
            BuildTarget ios = new BuildTarget(Os.IOS, bitness, new String[]{""}, new String[0], new String[]{""},
                    new String[0], new String[0], "", new String[] {"-c", "-Wall", "-O2", "-stdlib=libc++"}, new String[] {"-c", "-Wall", "-O2", "-stdlib=libc++"},
                    new String[] {"-shared", "-stdlib=libc++"}, null);
            ios.cCompiler = "clang";
            ios.cppCompiler = "clang++";
            ios.targetType = targetType;
            ios.architecture = architecture;
            return ios;
        }

        throw new RuntimeException("Unknown target type");
    }

    public File getTargetBinaryFile (BuildConfig config) {
        File libsDirectory = config.libsDir.child(getTargetFolder()).file();

        String sharedLibFilename = getSharedLibFilename(config.sharedLibName);
        if (targetType == TargetType.SIMULATOR) {
            sharedLibFilename += "-sim";
        }

        return new File(libsDirectory, sharedLibFilename);
    }

    public AndroidABI getTargetAndroidABI () {
        return targetAndroidABI;
    }

    public void setAndroidOverrideABI (AndroidABI overrideAndroidABI) {
        this.targetAndroidABI = overrideAndroidABI;
    }

    @Override
    public String toString () {
        if (os == Os.Android) {
            return "BuildTarget{" +
                    "os=" + os +
                    ", androidABI=" + targetAndroidABI +
                    '}';
        } else {
            return "BuildTarget{" +
                    "os=" + os +
                    ", bitness=" + bitness +
                    ", architecture=" + architecture +
                    ", compilerABIType=" + compilerABIType +
                    '}';
        }
    }

    public boolean canBuildOnHost (Os hostOS) {
        switch (hostOS) {
            case Windows:
                //Android, Windows, WindowsMSCV
                return os == Os.Windows ||
                        os == Os.Android;
            case Linux:
                //Android, Windows, Linux
                if (os == Os.Windows) {
                    //No MSVC support
                    if (compilerABIType == CompilerABIType.MSVC) {
                        return false;
                    }
                    return true;
                }

                //The rest
                return os == Os.Android || os == Os.Linux;
            case MacOsX:
                //Mac / iOS. Yes there are others, but Its not as trivial, so I think we should just handle Apple ecosphere
                return os == Os.MacOsX || os == Os.IOS || os == Os.Android;

            case Android:
            case IOS:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + hostOS);
        }
    }
}
