package com.badlogic.gdx.jnigen.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClangUtils {
    private static final File NULL_FILE = new File(System.getProperty("os.name").startsWith("Windows") ? "NUL" : "/dev/null");

    public static String[] getIncludePaths() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("clang", "-E", "-x", "c", "-", "-v");
            processBuilder.redirectInput(NULL_FILE);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            ArrayList<String> includePaths = new ArrayList<>();
            boolean insideIncludePath = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("End of search list."))
                    insideIncludePath = false;
                if (insideIncludePath)
                    includePaths.add("-I" + line.trim());
                if (line.equals("#include <...> search starts here:"))
                    insideIncludePath = true;
            }

            return includePaths.toArray(new String[0]);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
