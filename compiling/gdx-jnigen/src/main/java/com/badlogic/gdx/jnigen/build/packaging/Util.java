package com.badlogic.gdx.jnigen.build.packaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class Util {


    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static void JarFiles (File target, List<File> filesToJar, boolean failOnMissingFiles) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(target)) {
            JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream);

            for (File file : filesToJar) {

                if (!file.exists()) {
                    if (failOnMissingFiles) {
                        throw new FileNotFoundException("File not found when packaging: " + file);
                    } else {
                        log.warn("File not found when packaging: {}", file);
                        continue;
                    }
                }


                JarEntry jarEntry = new JarEntry(file.getName());

                jarOutputStream.putNextEntry(jarEntry);
                Files.copy(file.toPath(), jarOutputStream);
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
        }
    }
}
