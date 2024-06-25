package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.generator.Generator;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.util.Objects;

public class JnigenGenerateBindingsTask extends DefaultTask {

    private final JnigenBindingGeneratorExtension ext;
    @Inject
    public JnigenGenerateBindingsTask(JnigenExtension ext) {
        this.ext = ext.generator;

        setGroup("jnigen");

        setDescription("Generates java jnigen binding code.");
    }

    @TaskAction
    public void run() {
        Objects.requireNonNull(ext.getOutputPath(), "jnigen.generator.outputPath not defined");
        Objects.requireNonNull(ext.getBasePackage(), "jnigen.generator.basePackage not defined");
        Objects.requireNonNull(ext.getFileToParse(), "jnigen.generator.fileToParse not defined");
        String[] options = ext.getOptions();
        if (options == null)
            options = new String[0];
        Generator.execute(ext.getOutputPath().getAbsolutePath(), ext.getBasePackage(), ext.getFileToParse(), options);
    }
}
