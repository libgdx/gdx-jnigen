## gdx-jnigen

[![Build Status](https://github.com/libgdx/gdx-jnigen/workflows/Build%20and%20deploy/badge.svg)](https://github.com/libgdx/gdx-jnigen/actions?query=workflow%3A"Build+and+deploy")

The gdx-jnigen tool can be used with or without libGDX to allow C/C++ code to be written inline
with Java source code. 

This increases the locality of code that conceptually belongs together (the Java native class methods and the actual implementation) and makes refactoring a lot easier
compared to the usual JNI workflow. Arrays and direct buffers are converted for you, further
reducing boilerplate. Building the natives for Windows, Linux, macOS, and Android and iOS is handled for
you. jnigen also provides a mechanism for loading native libraries from a JAR at runtime, which
avoids "java.library.path" troubles.

See the libGDX Wiki for usage: https://libgdx.com/wiki/utils/jnigen

## gdx-jnigen-gradle quickstart

We recommend you look at some existing projects for examples:
- [gdx](https://github.com/libgdx/libgdx/blob/master/gdx/build.gradle)
- [gdx-freetype](https://github.com/libgdx/libgdx/blob/master/extensions/gdx-freetype/build.gradle)
- [gdx-bullet](https://github.com/libgdx/libgdx/blob/master/extensions/gdx-bullet/build.gradle)
- [gdx-video-desktop](https://github.com/libgdx/gdx-video/blob/master/gdx-video-desktop/build.gradle)
- [Jamepad](https://github.com/libgdx/Jamepad/blob/master/build.gradle)

```
// Add buildscript dependency
buildscript {
    dependencies {
        classpath "com.badlogicgames.gdx:gdx-jnigen-gradle:2.X.X"
    }
}

// Apply jnigen plugin
apply plugin: "com.badlogicgames.gdx.gdx-jnigen"

...

// Define jnigen extension
jnigen {
    // Your shared library name
    sharedLibName = "example"
    //temporaryDir = "target"
    //libsDir = "libs"
    //jniDir = "jni"

    // Shared configuration for all BuildTargets. Executed first
    // See all BuildTarget options here: https://github.com/libgdx/gdx-jnigen/blob/master/gdx-jnigen/src/main/java/com/badlogic/gdx/jnigen/BuildTarget.java
    // Most paths are relative to the $jniDir directory
    // String options can be replaced by using `x = "value"` or appended to with `x += "extravalue"`
    // String[] options can be replaced by using `x = ["value"]` or appended to with `x += "extravalue"` or `x += ["extravalue", "extravalue2"]`
    all {
        // Add extra flags passed to the C compiler
        cFlags += " -fvisibility=hidden "
        // Add extra flags passed to the C++ compiler
        cppFlags += " -std=c++11 -fvisibility=hidden "
        // Add extra flags passed to the linker
        linkerFlags += " -fvisibility=hidden "
    }

    // Configure robovm.xml for IOS builds, most simple libraries will not need to do this
    robovm {
        // Use preexisting robovm.xml, cannot be combined with other options.
        //manualFile file("robovm.xml")
        
        // Add extra patterns to forceLinkClasses
        //forceLinkClasses "test", "test2"
        //forceLinkClasses "pattern3"
        // Add extra library "test.a" with variant "device"
        //extraLib "test.a", "device"
    }

    // Add BuildTargets
    // All BuildTarget options can be further customized in an OS+Arch specific manner within a {} block

    // Add windows 32-bit BuildTarget and customize it
    add(Windows, x32) {
        //cFlags += " -fextraflag=fake "
        //compilerPrefix = "someprefix-";
        //cIncludes += "windowsspecificdir/*.c"
    }
    add(Windows, x64)
    add(Linux, x32)
    add(Linux, x64)
    add(Linux, x32, ARM)
    add(Linux, x64, ARM)
    add(MacOsX, x64)
    add(MacOsX, x64, ARM)
    add(Android) {
        // Add extra content to the generated Application.mk file
        //androidApplicationMk += "APP_STL := c++_static"
        // Specify which ABIs to build
        //androidABIs = ["armeabi", "armeabi-v7a", "x86", "x86_64", "arm64-v8a"]
    }
    add(IOS)

    // Customize each BuildTarget that matches the condition
    each({ it.os != Android && !it.isARM }) {
        //cppFlags += " -march=nocona "
    }
    // Customize everything again, can be used for conditional changes
    each({ true }) {
        //if(!it.cppCompiler.contains("clang")) {
        //    it.cFlags += " -flto "
        //    it.cppFlags += " -flto "
        //    it.linkerFlags += " -flto "
        //}

        //if(it.cppCompiler.contains("clang"))
        //    it.linkerFlags += " -Wl,-dead_strip -Wl,-s "
        //else
        //    it.linkerFlags += " -Wl,--gc-sections "
    }
}
```
