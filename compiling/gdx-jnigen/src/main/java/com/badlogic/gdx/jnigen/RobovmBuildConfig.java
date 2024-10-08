package com.badlogic.gdx.jnigen;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RobovmBuildConfig {

    /**
     * ios framework bundle identifier, if null an automatically generated bundle identifier will be used
     */
    public String xcframeworkBundleIdentifier = null;
    /**
     * Minimum supported iOS version, will default to iOS 12
     */
    public String minIOSVersion = "12.0";

    /**
     * Use an existing robovm.xml file instead of generating one.
     */
    private File manualFile = null;
    private List<String> forceLinkClasses = new ArrayList<>();
    private List<RoboVMXmlLib> extraLibs = new ArrayList<>();
    private List<String> extraXCFrameworks = new ArrayList<>();

    public File getManualFile () {
        return manualFile;
    }

    public List<String> getForceLinkClasses () {
        return forceLinkClasses;
    }

    public List<RoboVMXmlLib> getExtraLibs () {
        return extraLibs;
    }

    public List<String> getExtraXCFrameworks () {
        return extraXCFrameworks;
    }

    public void manualFile (File manualFile) {
        if (!forceLinkClasses.isEmpty() || !extraLibs.isEmpty())
            throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
        this.manualFile = manualFile;
    }

    public void forceLinkClasses (String[] forceLinkClasses) {
        if (manualFile != null)
            throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
        this.forceLinkClasses.addAll(Arrays.asList(forceLinkClasses));
    }

    public void extraLib (String path) {
        if (manualFile != null)
            throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
        extraLibs.add(new RoboVMXmlLib(path, null));
    }

    public void extraLib (String path, String variant) {
        if (manualFile != null)
            throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
        extraLibs.add(new RoboVMXmlLib(path, variant));
    }

    public void extraXCFramework (String path) {
        if (manualFile != null)
            throw new RuntimeException("robovm cannot use both manualFile and gradle overrides");
        extraXCFrameworks.add(path);
    }

    public class RoboVMXmlLib {
        public String path;
        public String variant;

        public RoboVMXmlLib (String path, String variant) {
            this.path = path;
            this.variant = variant;
        }
    }
}