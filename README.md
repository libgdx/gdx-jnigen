## gdx-jnigen

[![Build Status](https://github.com/libgdx/gdx-jnigen/workflows/Build%20and%20deploy/badge.svg)](https://github.com/libgdx/gdx-jnigen/actions?query=workflow%3A"Build+and+deploy")

The gdx-jnigen tool can be used with or without libGDX to allow C/C++ code to be written inline
with Java source code. 

This increases the locality of code that conceptually belongs together (the Java native class methods and the actual implementation) and makes refactoring a lot easier
compared to the usual JNI workflow. Arrays and direct buffers are converted for you, further
reducing boilerplate. Building the natives for Windows, Linux, macOS, Android, iOS, and Web (via Emscripten) is handled for
you. jnigen also provides a mechanism for loading native libraries from a JAR at runtime, which
avoids "java.library.path" troubles.

See the libGDX Wiki for usage: https://libgdx.com/wiki/utils/jnigen

> [!NOTE]
> For documentation of jnigen-runtime and jnigen-generator API please refer to `RUNTIME.MD`.

> [!NOTE]
> For migrating from jnigen 2.x see the `MIGRATION.MD`


## gdx-jnigen-gradle quickstart

We recommend you look at some existing projects for examples:
- [gdx](https://github.com/libgdx/libgdx/blob/master/gdx/build.gradle) (Uses jnigen 2.x)
- [gdx-freetype](https://github.com/libgdx/libgdx/blob/master/extensions/gdx-freetype/build.gradle) (Uses jnigen 2.x)
- [gdx-bullet](https://github.com/libgdx/libgdx/blob/master/extensions/gdx-bullet/build.gradle) (Uses jnigen 2.x)
- [gdx-video-desktop](https://github.com/libgdx/gdx-video/blob/master/gdx-video-desktop/build.gradle) (Uses jnigen 2.x)
- [Jamepad](https://github.com/libgdx/Jamepad/blob/master/build.gradle) (Uses jnigen 2.x)
- [gdx-box2d](https://github.com/libgdx/gdx-box2d/blob/master/build.gradle.kts) (Uses jnigen 3.x and kotlin DSL)


## Configuring 
```gradle
// Apply jnigen plugin
plugins {
    id "com.badlogicgames.jnigen.jnigen-gradle"
}

// ...

// Define jnigen extension
jnigen {
    // Your shared library name
    sharedLibName = "example"
    
    //Enable multi threading compilation
    multiThreadedCompile = true

    // Shared configuration for all BuildTargets. Executed first
    // See all BuildTarget options here: https://github.com/libgdx/gdx-jnigen/blob/master/gdx-jnigen/src/main/java/com/badlogic/gdx/jnigen/BuildTarget.java
    // Most paths are relative to the $jniDir directory
    // String options can be replaced by using `x = "value"` or appended to with `x += "extravalue"`
    // String[] options can be replaced by using `x = ["value"]` or appended to with `x += "extravalue"` or `x += ["extravalue", "extravalue2"]`
    all {
        // Add extra flags passed to the C compiler
        cFlags += ["-fvisibility=hidden"]
        // Add extra flags passed to the C++ compiler
        cppFlags += ["-std=c++11", "-fvisibility=hidden"]
        // Add extra flags passed to the linker
        linkerFlags += ["-fvisibility=hidden"]
    }

    // Configure robovm.xml for IOS builds, most simple libraries will not need to do this
    robovm {
        // Define ios framework bundle identifier
        // xcframeworkBundleIdentifier = "com.badlogic.gdx.JniGen
        // Deinfe minimum supported iOS version
        // minIOSVersion = "11.0"

        // Use preexisting robovm.xml, cannot be combined with other options.
        //manualFile file("robovm.xml")
        
        // Add extra patterns to forceLinkClasses
        //forceLinkClasses "test", "test2"
        //forceLinkClasses "pattern3"
        // Add extra library "test.a" with variant "device"
        //extraLib "test.a", "device"
        //extraXCFramework "libs/test.xcframework"
    }

    // Add BuildTargets
    // All BuildTarget options can be further customized in an OS+Arch specific manner within a {} block

    // Add windows 32-bit BuildTarget and customize it
    addWindows(x32, x86) {
        //cFlags += ["-fextraflag=fake"]
        //compilerPrefix = "someprefix-";
        //cIncludes += "windowsspecificdir/*.c"
    }
    
    //Add windows 64 bit, x86, MSVC toolchain 
    addWindows(x64, x86, MSVC) {
        msvcPreLinkerFlags += ["/MD"]
    }
    
    addWindows(x64, x86)    
    addLinux(x32, x86)
    addLinux(x64, x86)
    addLinux(x32, ARM)
    addLinux(x64, ARM)
    addMac(x64, x86)
    addMac(x64, ARM)
    
    //Auto add all possible ABIs 
    addAndroid() {
        // Add extra content to the generated Application.mk file
        //androidApplicationMk += ["APP_STL := c++_static"]
    }
    
    //Add specific android ABI
    addAndroid(AndroidABI.ABI_ARM64_V8A)
    
    //Auto add all possible iOS targets, including sim 
    addIOS() 
    
    //Add specific ios device target
    addIOS(x64, ARM, DEVICE)
    
    //Add ios 64 bit x86 simulator target
    addIOS(x64, x86, SIMULATOR)

    // Add Emscripten/WebAssembly target (requires Emscripten SDK)
    addWeb()

    // Add web target with customization
    addWeb {
        // Extra flags for emcc/em++ compilation
        //cFlags += ["-s", "USE_SDL=2"]
        //linkerFlags += ["-s", "TOTAL_MEMORY=67108864"]
    }

    // Customize each BuildTarget that matches the condition
    each({ it.os != Android && it.architecture != ARM }) {
        //cppFlags += ["-march=nocona"]
    }
    // Customize everything again, can be used for conditional changes
    each({ true }) {
        //if(!it.cppCompiler.contains("clang")) {
        //    it.cFlags += ["-flto"]
        //    it.cppFlags += ["-flto"]
        //    it.linkerFlags += ["-flto"]
        //}

        //if(it.cppCompiler.contains("clang"))
        //    it.linkerFlags += ["-Wl,-dead_strip", "-Wl,-s"]
        //else
        //    it.linkerFlags += ["-Wl,--gc-sections"]
    }
}
```

## Building

After the Gradle plugin is configured, jnigen will automatically generate and register
appropriate tasks for building.

### Generation
`jnigen`
Required to be run before any other jnigen tasks, must be run any time source is changed
or config is changed

e.g. `./gradlew jnigen`

### Compilation

`jnigenBuildXXX`
Execute the native compilation for the targets you want. Replace `XXX` with the target name.

e.g. `./gradlew jnigenBuildAllWindows` Build all windows targets

e.g. `./gradlew jnigenBuildLinuxArm_64` Build the 64-bit ARM Linux target only


### Packaging
`jnigenPackageXXX`
Package the native libraries into a jar. Replace `XXX` with the target name.

e.g. `./gradlew jnigenPackageAll` Package all targets into their jars

e.g. `./gradlew jnigenPackageAllIOS` Package the iOS targets into their jar

e.g. `./gradlew jnigenPackageAllAndroid`    Package the Android targets into their jars

e.g. `./gradlew jnigenPackageAllWeb`    Package the Web targets into their jar


## Emscripten / Web Setup

jnigen supports compiling C/C++ to WebAssembly via the [Emscripten](https://emscripten.org/) toolchain. The compiled `.js` and `.wasm` files can then be used with GWT or TeaVM to run your native code in the browser.

### Prerequisites

1. **Install the Emscripten SDK:**

   ```bash
   git clone https://github.com/emscripten-core/emsdk.git
   cd emsdk
   ./emsdk install latest
   ./emsdk activate latest
   ```

2. **Set up your environment** (must be done in each terminal session, or add to your shell profile):

   ```bash
   source /path/to/emsdk/emsdk_env.sh
   ```

   This adds `emcc` and `em++` to your `PATH` and sets the `EMSDK` environment variable. jnigen will also look for `EMSDK` to locate the compilers automatically.

3. **Verify the installation:**

   ```bash
   emcc --version
   ```

### Gradle Configuration

Add the web target to your `jnigen` block:

```gradle
jnigen {
    sharedLibName = "mylib"

    // ... other targets ...

    addWeb()
}
```

The `addWeb()` call configures a build target that uses `emcc`/`em++` with these defaults:
- **Architecture:** WASM (32-bit)
- **Output:** `mylib.js` + `mylib.wasm` (Emscripten modularized output)
- **Linker flags:** `MODULARIZE=1`, `ALLOW_MEMORY_GROWTH=1`, `ALLOW_TABLE_GROWTH=1`, exported runtime methods for `ccall`, `cwrap`, `addFunction`, `removeFunction`

You can customize the target like any other:

```gradle
addWeb {
    cFlags += ["-s", "USE_SDL=2"]
    cppFlags += ["-std=c++17"]
    linkerFlags += ["-s", "TOTAL_MEMORY=67108864"]
    headerDirs += ["src/main/native/web"]
}
```

### Build Commands

```bash
# Generate JNI wrappers (run first)
./gradlew jnigen

# Build WebAssembly output
./gradlew jnigenBuildAllEmscripten

# Package into natives-web.jar
./gradlew jnigenPackageAllWeb
```

### JNI Header Compatibility

Emscripten builds do not use JNI headers. Instead, jnigen provides a compatibility header (`jnigen-web.h`) that maps JNI types to standard C types:

| JNI Type  | Web Type    |
|-----------|-------------|
| `jint`    | `int32_t`   |
| `jlong`   | `int64_t`   |
| `jfloat`  | `float`     |
| `jdouble` | `double`    |
| `jbyte`   | `int8_t`    |
| `JNIEnv*` | `void*`     |
| `JNIEXPORT` | `EMSCRIPTEN_KEEPALIVE` |

### GWT Integration

Add the GWT emulation module to your project:

```gradle
dependencies {
    implementation "com.badlogicgames.jnigen:jnigen-runtime-gwt:$jnigenVersion"
}
```

In your GWT module descriptor (`.gwt.xml`):

```xml
<inherits name="com.badlogic.gdx.jnigen.gwt.JnigenGwt"/>
```

This provides super-source replacements for `CHandler` and `SharedLibraryLoader` that delegate to the Emscripten WASM module via JSNI.

### TeaVM Integration

Add the TeaVM emulation module to your project:

```gradle
dependencies {
    implementation "com.badlogicgames.jnigen:jnigen-runtime-teavm:$jnigenVersion"
}
```

The module registers a TeaVM plugin via `META-INF/services` that provides `@JSBody`-annotated bridge methods (`CHandlerWeb`, `WasmMemory`) for calling into the Emscripten Module from TeaVM-transpiled code.

### Loading the WASM Module

Each jnigen library gets a unique factory function name based on `sharedLibName`: `createModule_{sharedLibName}` (with non-alphanumeric characters replaced by `_`). This prevents clashes when multiple jnigen libraries are loaded on the same page.

In your HTML host page, load the generated JS module before your GWT/TeaVM application starts:

```html
<script src="mylib.js"></script>
<script>
  // Factory function name is createModule_{sharedLibName}
  createModule_mylib().then(function(Module) {
    window.Module = Module;
    // Now start your GWT/TeaVM application
  });
</script>
```

If you have multiple jnigen libraries, load them all and merge into a single `Module` or use separate references:

```html
<script src="mylib.js"></script>
<script src="otherlib.js"></script>
<script>
  Promise.all([
    createModule_mylib(),
    createModule_otherlib()
  ]).then(function(modules) {
    window.Module_mylib = modules[0];
    window.Module_otherlib = modules[1];
    // Start your application
  });
</script>
```

### Generator Web Bindings

If you are using `jnigen-generator` to auto-generate bindings from C headers, enable web output:

```gradle
jnigen {
    generator {
        // ... other generator config ...
        webEnabled = true
    }
}
```

This generates additional web-specific binding classes alongside the standard JNI bindings, which call Emscripten-exported functions directly by their C names (not JNI-mangled).


### Publishing

Jnigen automatically generates publishing tasks that integrate with the maven publishing plugin.
If you are setup to publish to a maven repository, you can simply run:

`./gradlew publish`

Your artifacts will be published using `groupid:artifactid-platform:version:classifierXXX` as the coordinates.

replace classifierXXX with the target name.

e.g. natives-desktop, natives-ios, natives-android-arm64-v8a 
