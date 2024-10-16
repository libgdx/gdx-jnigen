package com.badlogic.gdx.jnigen.build.packaging;

import com.badlogic.gdx.jnigen.BuildTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DesktopPackaging extends PlatformPackager {

    private static final Logger logger = LoggerFactory.getLogger(DesktopPackaging.class);

    @Override
    protected void packagePlatformImpl () {
        List<File> targetBinaries = new ArrayList<>();
        for (BuildTarget buildTarget : targetList) {
            targetBinaries.add(buildTarget.getTargetBinaryFile(buildConfig));
        }


        File outputJar = new File(buildConfig.libsDir.file().getAbsoluteFile(), buildConfig.targetJarBaseName + "-natives-desktop.jar");

        try {
            Util.JarFiles(outputJar, targetBinaries, buildConfig.errorOnPackageMissingNative);
        } catch (IOException e) {
            logger.error("Exception when packing.", e);
            throw new RuntimeException(e);
        }

        logger.info("Packed desktop platform to {}", outputJar);

    }

}
