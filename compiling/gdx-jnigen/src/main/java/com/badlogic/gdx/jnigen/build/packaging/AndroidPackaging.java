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

            File abiFile = new File(targetBinaryFolder, abiString + File.separator + "lib" + buildConfig.sharedLibName + ".so");

            if (!abiFile.exists()) {
                logger.warn("No build found for abi {}. If this is a specific abi, this could be due to building with a newer NDK", abi);
                continue;
            }

            try {
                Util.JarFiles(outputJar, Collections.singletonList(abiFile), buildConfig.errorOnPackageMissingNative);
            } catch (IOException e) {
                logger.error("Exception when packing", e);
                throw new RuntimeException(e);
            }

            logger.info("Packed android platform to {}", outputJar);
        }
    }
}
