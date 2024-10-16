package com.badlogic.gdx.jnigen.build.packaging;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.commons.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Packager {

    private static Logger logger = LoggerFactory.getLogger(Packager.class);

    /**
     * Packager takes in all possible build targets, filters based on what is configured, and bundles into the platform
     * specific archives
     *
     * @param platform
     * @param config
     * @param totalTargets
     */
    public void packagePlatform (Platform platform, BuildConfig config, List<BuildTarget> totalTargets) {

        List<BuildTarget> targetList = gatherCompatibleBuildTargets(platform, totalTargets);

        if (targetList.isEmpty()) {
            logger.warn("No build targets are compatible with this platform {}", platform);
        }

        logger.info("Starting package for platform {}", platform);

        PlatformPackager packaging = getPackaging(platform);
        packaging.startPackage(config, targetList);

    }

    private static List<BuildTarget> gatherCompatibleBuildTargets (Platform platform, List<BuildTarget> totalTargets) {
        List<BuildTarget> targetList = new ArrayList<>();
        for (BuildTarget buildTarget : totalTargets) {
            if (buildTarget.os.getPlatform() == platform) {
                targetList.add(buildTarget);
            }
        }
        return targetList;
    }

    private PlatformPackager getPackaging (Platform platform) {
        switch (platform) {
            case Desktop:
                return new DesktopPackaging();
            case Android:
                return new AndroidPackaging();
            case IOS:
                return new IOSPackaging();
            default:
                throw new RuntimeException("Packaging for platform " + platform + " is not supported");
        }
    }


}
