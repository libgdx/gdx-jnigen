package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.commons.Os;
import com.badlogic.gdx.jnigen.gradle.gha.GHABuilder;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

public class JnigenGithubActionBuilderTask extends DefaultTask {

    private static final Logger logger = LoggerFactory.getLogger(JnigenGithubActionBuilderTask.class);

    private final JnigenExtension ext;

    private BuildTarget buildTarget;
    private String overrideABI;
    private Os osToBuild;

    @Inject
    public JnigenGithubActionBuilderTask (JnigenExtension ext) {
        this.ext = ext;

        setGroup("jnigen");
        setDescription("Generate github action to build");
    }

    @TaskAction
    public void run () {
        File file = getProject().file(".github/workflows/jnigen.yaml");
        file.getParentFile().mkdirs();


        try {
            GHABuilder ghaBuilder = new GHABuilder(file, ext);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
