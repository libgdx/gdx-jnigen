package com.badlogic.gdx.jnigen.build;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ToolchainExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ToolchainExecutor.class);

    public interface ToolchainCallback {

        void onInfoMessage (String message);
        void onErrorMessage (String message);

        void onSuccess ();
        void onFail (int statusCode);
    }

    public static void execute (File executable, File workingDirectory, List<String> args, ToolchainCallback callback) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(executable.getAbsolutePath());
            processBuilder.directory(workingDirectory);
            processBuilder.redirectErrorStream(true);

            processBuilder.command().addAll(args);

            StringBuilder builder = new StringBuilder();
            for (String arg : args) {
                builder.append(arg);
                builder.append(" ");
            }
            String argString = builder.toString().trim();

            logger.info("Staring process: \n{} {}", executable.getAbsolutePath(), argString);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                callback.onInfoMessage(line);
            }
            int exitCode = process.waitFor();

            // Handle errors (if any)
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    callback.onErrorMessage(errorLine);
                }
                callback.onFail(exitCode);
            }

            callback.onSuccess();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
