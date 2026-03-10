package com.badlogic.gdx.jnigen.build.packaging;

import com.badlogic.gdx.jnigen.BuildTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * Packages Emscripten .js and .wasm files into a natives-web.jar
 */
public class WebPackaging extends PlatformPackager {

    private static final Logger logger = LoggerFactory.getLogger(WebPackaging.class);

    @Override
    protected void packagePlatformImpl () {
        List<File> targetBinaries = new ArrayList<>();
        for (BuildTarget buildTarget : targetList) {
            File jsFile = buildTarget.getTargetBinaryFile(buildConfig);
            targetBinaries.add(jsFile);

            // Also include the .wasm file that Emscripten generates alongside the .js file
            String jsPath = jsFile.getAbsolutePath();
            if (jsPath.endsWith(".js")) {
                File wasmFile = new File(jsPath.substring(0, jsPath.length() - 3) + ".wasm");
                if (wasmFile.exists()) {
                    targetBinaries.add(wasmFile);
                }
            }
        }

        File outputJar = new File(buildConfig.libsDir.file().getAbsoluteFile(), buildConfig.targetJarBaseName + "-natives-web.jar");

        try (FileOutputStream fos = new FileOutputStream(outputJar);
             JarOutputStream jarOutputStream = new JarOutputStream(fos)) {

            for (File file : targetBinaries) {
                if (!file.exists()) {
                    if (buildConfig.errorOnPackageMissingNative) {
                        throw new IOException("File not found when packaging: " + file);
                    } else {
                        logger.warn("File not found when packaging: {}", file);
                        continue;
                    }
                }
                jarOutputStream.putNextEntry(new JarEntry(file.getName()));
                Files.copy(file.toPath(), jarOutputStream);
                jarOutputStream.closeEntry();
            }

            // Add empty marker file so TeaVM plugin can discover this jar
            jarOutputStream.putNextEntry(new JarEntry("META-INF/gdx-teavm.properties"));
            jarOutputStream.closeEntry();

        } catch (IOException e) {
            logger.error("Exception when packing.", e);
            throw new RuntimeException(e);
        }

        logger.info("Packed web platform to {}", outputJar);
    }
}
