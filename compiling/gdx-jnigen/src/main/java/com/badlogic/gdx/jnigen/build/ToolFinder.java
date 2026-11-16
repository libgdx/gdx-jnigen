package com.badlogic.gdx.jnigen.build;

import com.badlogic.gdx.jnigen.commons.HostDetection;
import com.badlogic.gdx.jnigen.commons.Os;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ToolFinder {

    private static final Logger logger = LoggerFactory.getLogger(ToolFinder.class);

    public static File getSDK (String pathName, boolean failOnNotFound) {
        File absoluteFile = new File(pathName);
        if (absoluteFile.exists()) {
            return absoluteFile;
        } else {
            if (failOnNotFound) {
                logger.error("Failure to find SDK {}. Make sure that the path is correct", pathName);
                throw new RuntimeException("Tool was not found");
            }
        }
        return null;
    }

    public static File getToolFile (String toolPathOrName, RuntimeEnv env, boolean failOnNotFound) {
        return getToolFile(toolPathOrName, env, null, failOnNotFound);
    }

    public static File getToolFile (String toolPathOrName, RuntimeEnv env, String optionalFallbackSuffix, boolean failOnNotFound) {
        boolean found = false;

        File absoluteFile = new File(toolPathOrName);
        if (absoluteFile.exists() && absoluteFile.isFile()) {
            return absoluteFile;
        }

        List<String> paths = env.getPaths();
        for (String path : paths) {
            File file = new File(path, toolPathOrName);
            if (file.exists() && file.isFile()) {
                return file;
            }
        }

        if (failOnNotFound && !found && optionalFallbackSuffix != null) {

            //Lets do a last ditch for windows friendly support
            if (HostDetection.os == Os.Windows && !toolPathOrName.endsWith(optionalFallbackSuffix)) {
                return getToolFile(toolPathOrName + optionalFallbackSuffix, env, optionalFallbackSuffix, failOnNotFound);
            }

            logger.error("Failure to find tool {}. Make sure that its available on your path", toolPathOrName);
            throw new RuntimeException("Tool was not found");
        }

        if (failOnNotFound) {
            logger.error("Failure to find tool {}. Make sure that its available on your path", toolPathOrName);
            throw new RuntimeException("Tool was not found");
        }

        return null;
    }

}
