package com.badlogic.gdx.jnigen.build;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class RuntimeEnv {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeEnv.class);

    private Set<String> paths = new HashSet<>();

    public RuntimeEnv () {
        addToPathFromSystemEnv("Path");
        addToPathFromSystemEnv("PATH");
    }

    public Set<String> getPaths () {
        return paths;
    }

    private void addToPathFromSystemEnv (String envKey) {
        String env = System.getenv(envKey);
        if (env == null) {
            logger.warn("Path is null {}", envKey);
            return;
        }

        String[] split = env.split(File.pathSeparator);
        paths.addAll(Arrays.asList(split));
    }


}
