package com.badlogic.gdx.jnigen.build.packaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class Util {


    public static void JarFiles (File target, List<File> filesToJar) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(target)) {
            JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream);

            for (File file : filesToJar) {
                JarEntry jarEntry = new JarEntry(file.getName());

                jarOutputStream.putNextEntry(jarEntry);
                Files.copy(file.toPath(), jarOutputStream);
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
        }
    }
}
