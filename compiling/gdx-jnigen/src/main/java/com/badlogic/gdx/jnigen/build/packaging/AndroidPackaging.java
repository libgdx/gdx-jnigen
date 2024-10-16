package com.badlogic.gdx.jnigen.build.packaging;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.commons.AndroidABI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AndroidPackaging extends PlatformPackager {


    private static final Logger logger = LoggerFactory.getLogger(AndroidPackaging.class);

    @Override
    protected void packagePlatformImpl () {
        //Each one goes in its own package
        for (BuildTarget abi : targetList) {
            AndroidABI targetAndroidABI = abi.getTargetAndroidABI();

            String abiString = targetAndroidABI.getAbiString();
            File outputJar = new File(buildConfig.libsDir.file().getAbsoluteFile(), buildConfig.targetJarBaseName + "-natives-" + abiString + ".jar");

            File targetBinaryFolder = abi.getTargetBinaryFile(buildConfig).getParentFile();

            File abiFile = new File(targetBinaryFolder, abiString + File.separator);

            if (!abiFile.exists()) {
                logger.warn("No build found for abi {}. If this is a specific abi, this could be due to building with a newer NDK", abi);
            }

            if (!abiFile.isDirectory()) {
                logger.warn("{} is not an directory?", abiFile);
            }

            File[] files = abiFile.listFiles((dir, name) -> name.endsWith(".so"));
            if (files == null || files.length == 0) {
                logger.warn("No files found for abi {}. If this is a specific abi, this could be due to building with a newer NDK", abi);
                files = new File[0];
            }

            try {
                Util.JarFiles(outputJar, Arrays.asList(files), buildConfig.errorOnPackageMissingNative);
            } catch (IOException e) {
                logger.error("Exception when packing", e);
                throw new RuntimeException(e);
            }

            logger.info("Packed android platform to {}", outputJar);
        }
    }
}
