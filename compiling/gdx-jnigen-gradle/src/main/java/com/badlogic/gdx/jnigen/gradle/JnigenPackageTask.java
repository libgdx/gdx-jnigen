package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.RobovmBuildConfig;
import com.badlogic.gdx.jnigen.build.packaging.Packager;
import com.badlogic.gdx.jnigen.commons.AndroidABI;
import com.badlogic.gdx.jnigen.commons.Platform;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Desu
 */
public class JnigenPackageTask extends DefaultTask {

    private static final Logger log = LoggerFactory.getLogger(JnigenPackageTask.class);

    private JnigenExtension ext;

    private Platform[] platformsToPackage;
    @Nullable
    private AndroidABI targetAndroidABI;

    private BuildConfig buildConfig;
    private Packager packager;

    @Inject
    public JnigenPackageTask (JnigenExtension ext) {
        this.ext = ext;

        setGroup("jnigen");
        setDescription("Packages all native libraries into a single Platform artifact");
    }

    public void configure (AndroidABI androidABIPackageOverride, Platform... platformsToPackage) {
        this.platformsToPackage = platformsToPackage;
        this.targetAndroidABI = androidABIPackageOverride;


        RobovmBuildConfig robovmBuildConfig = new RobovmBuildConfig();
        if (ext.robovm != null) {
            ext.robovm.execute(robovmBuildConfig);
        }

        buildConfig = new BuildConfig(ext.sharedLibName, ext.subProjectDir + ext.temporaryDir, ext.subProjectDir + ext.libsDir, ext.subProjectDir + ext.jniDir, robovmBuildConfig, new FileDescriptor(ext.subProjectDir));
        buildConfig.targetJarBaseName = ext.sharedLibName;

        packager = new Packager();

        //add outputs manually here. This is only really required for publishing support
        List<String> outputs = getBuildOutputs();

        for (String output : outputs) {
            getOutputs().file(ext.subProjectDir + ext.libsDir + "/" + output);
        }

        getOutputs().upToDateWhen(new Spec<Task>() {
            @Override
            public boolean isSatisfiedBy (Task task) {
                return false;
            }
        });
    }

    private List<String> getBuildOutputs () {
        ArrayList<String> outputs = new ArrayList<>();

        for (Platform platform : platformsToPackage) {
            if (platform == Platform.Android) {
                if (targetAndroidABI != null) {
                    outputs.add(buildConfig.targetJarBaseName + "-natives-" + targetAndroidABI.getAbiString() + ".jar");
                } else {
                    log.trace("No target AndroidABI set");
                }
            } else {
                switch (platform) {
                    case Desktop:
                        outputs.add(buildConfig.targetJarBaseName + "-natives-" + "desktop" + ".jar");

                        break;
                    case IOS:
                        outputs.add(buildConfig.targetJarBaseName + "-natives-" + "ios" + ".jar");
                        break;
                }
            }
        }

        return outputs;
    }


    @TaskAction
    public void run () {

        for (Platform platform : platformsToPackage) {
            if (platform == Platform.Android && targetAndroidABI != null) {
                List<BuildTarget> filteredForABI = ext.targets.stream()
                        .filter(target -> target.os.getPlatform() == Platform.Android && target.getTargetAndroidABI() == targetAndroidABI)
                        .collect(Collectors.toList());
                packager.packagePlatform(platform, buildConfig, filteredForABI);
            } else {
                packager.packagePlatform(platform, buildConfig, ext.targets);
            }
        }
    }

    @Input
    public String getSharedLibName () {
        return ext.sharedLibName;
    }

}
