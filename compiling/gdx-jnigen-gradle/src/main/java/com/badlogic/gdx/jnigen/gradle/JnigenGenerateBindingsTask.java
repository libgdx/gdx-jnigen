package com.badlogic.gdx.jnigen.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class JnigenGenerateBindingsTask extends DefaultTask {

    private final JnigenBindingGeneratorExtension ext;
    private Configuration configuration;

    @Inject
    public JnigenGenerateBindingsTask(JnigenExtension ext) {
        this.ext = ext.generator;

        setGroup("jnigen");

        setDescription("Generates java jnigen binding code.");

        Configuration llvmConfiguration = getProject().getConfigurations().create("generatorLLVM", conf -> {
            conf.setVisible(false);
            conf.setCanBeResolved(true);
            conf.setCanBeConsumed(false);
            conf.setDescription("LLVM dependencies for generator");
        });

        getProject().getDependencies().add("generatorLLVM", "org.bytedeco:llvm-platform:19.1.3-1.5.11");

        this.configuration = llvmConfiguration;
    }

    @Classpath
    public Configuration getConfiguration() {
        return configuration;
    }

    @TaskAction
    public void run() {
        Objects.requireNonNull(ext.getOutputPath(), "jnigen.generator.outputPath not defined");
        Objects.requireNonNull(ext.getBasePackage(), "jnigen.generator.basePackage not defined");
        Objects.requireNonNull(ext.getFileToParse(), "jnigen.generator.fileToParse not defined");

        // Gradle is annoying and ships it's own javaparser, but doesn't relocate it
        FileCollection filteredClasspath = getProject().files(
                getProject().getBuildscript()
                        .getConfigurations().getByName("classpath")
                        .resolve().stream()
                        .filter(file -> !file.getAbsolutePath().contains("gradle-"))
                        .collect(Collectors.toList())
        );

        String[] options = ext.getOptions();
        if (options == null)
            options = new String[0];

        ArrayList<String> args = new ArrayList<>();
        args.add(ext.getOutputPath().getAbsolutePath());
        args.add(ext.getBasePackage());
        args.add(ext.getFileToParse());
        args.addAll(Arrays.asList(options));

        getProject().javaexec(spec -> {
            spec.environment("LIBCLANG_DISABLE_CRASH_RECOVERY", "1");
            spec.setClasspath(filteredClasspath.plus(configuration));
            spec.getMainClass().set("com.badlogic.gdx.jnigen.generator.Generator");
            spec.args(args);
        });
    }
}
