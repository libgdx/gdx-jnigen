package com.badlogic.gdx.jnigen.build;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class RuntimeEnv {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeEnv.class);

    private List<String> paths = new ArrayList<>();

    public RuntimeEnv () {
        addToPathFromSystemEnv("Path");
        addToPathFromSystemEnv("PATH");

        addToPathFromSystemEnv("NDK_HOME");
    }

    public List<String> getPaths () {
        return paths;
    }

    private void addToPathFromSystemEnv (String envKey) {
        String env = System.getenv(envKey);
        addToPath(env);
    }

    public void addToPath (String encodedPath) {
        String[] split = splitToPaths(encodedPath);
        if (split != null) {
            paths.addAll(Arrays.asList(split));
        }
    }

    public String[] splitToPaths (String encodedPath) {
        if (encodedPath == null) {
            logger.trace("Path is null {}", encodedPath);
            return null;
        }

        String[] split = encodedPath.split(File.pathSeparator);
        return split;
    }
}
