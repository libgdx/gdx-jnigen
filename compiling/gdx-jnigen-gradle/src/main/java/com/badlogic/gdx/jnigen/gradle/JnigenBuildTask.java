package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.RobovmBuildConfig;
import com.badlogic.gdx.jnigen.build.PlatformBuilder;
import com.badlogic.gdx.jnigen.commons.Os;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;

/**
 * @author Desu
 */
public class JnigenBuildTask extends DefaultTask {

    private static final Logger logger = LoggerFactory.getLogger(JnigenBuildTask.class);

    private final JnigenExtension ext;

    private BuildTarget buildTarget;
    private Os osToBuild;

    @Inject
    public JnigenBuildTask (JnigenExtension ext) {
        this.ext = ext;

        setGroup("jnigen");
        setDescription("Builds native libraries");
    }

    public void setBuildTarget (BuildTarget buildTarget) {
        this.buildTarget = buildTarget;
    }


    public void setOsToBuild (Os osToBuild) {
        this.osToBuild = osToBuild;
    }

    @TaskAction
    public void run () {
        RobovmBuildConfig robovmBuildConfig = new RobovmBuildConfig();
        if (ext.robovm != null) {
            ext.robovm.execute(robovmBuildConfig);
        }

        BuildConfig buildConfig = new BuildConfig(ext.sharedLibName, ext.subProjectDir + ext.temporaryDir, ext.subProjectDir + ext.libsDir, ext.subProjectDir + ext.jniDir, robovmBuildConfig, new FileDescriptor(ext.subProjectDir));

        buildConfig.multiThreadedCompile = ext.multiThreadedCompile;

        //Build the build target!
        //If we have no build target, its OS level build target build

        //If we have build target, its a specific build

        PlatformBuilder platformBuilder = new PlatformBuilder();

        if (buildTarget != null) {
            platformBuilder.build(buildTarget.os, buildConfig, Collections.singletonList(buildTarget));
        } else {
            platformBuilder.build(osToBuild, buildConfig, ext.targets);
        }
    }
}
