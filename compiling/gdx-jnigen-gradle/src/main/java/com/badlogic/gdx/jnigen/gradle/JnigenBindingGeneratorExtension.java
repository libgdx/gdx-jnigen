package com.badlogic.gdx.jnigen.gradle;

import java.io.File;

public class JnigenBindingGeneratorExtension {

    private File outputPath;
    private String basePackage;
    private String fileToParse;
    private String[] options;
    private boolean webEnabled = false;

    public File getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(File outputPath) {
        this.outputPath = outputPath;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getFileToParse() {
        return fileToParse;
    }

    public void setFileToParse(String fileToParse) {
        this.fileToParse = fileToParse;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public boolean isWebEnabled() {
        return webEnabled;
    }

    public void setWebEnabled(boolean webEnabled) {
        this.webEnabled = webEnabled;
    }
}
