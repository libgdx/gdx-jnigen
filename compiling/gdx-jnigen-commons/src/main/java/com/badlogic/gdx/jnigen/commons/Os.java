package com.badlogic.gdx.jnigen.commons;


/**
 * The target operating system of a build target.
 */
public enum Os {

    Windows(Platform.Desktop),
    Linux(Platform.Desktop),
    MacOsX(Platform.Desktop),
    Android(Platform.Android),
    IOS(Platform.IOS);

    private final Platform platform;

    Os (Platform platform) {
        this.platform = platform;
    }

    public Platform getPlatform () {
        return platform;
    }

    public String getJniPlatform () {
        if (this == Os.Windows) return "win32";
        if (this == Os.Linux) return "linux";
        if (this == Os.MacOsX) return "mac";
        if (this == Os.IOS) return "mac";
        if (this == Os.Android) return "android";
        return "";
    }

    public String getLibPrefix () {
        if (this == Os.Linux || this == Os.Android || this == Os.MacOsX) {
            return "lib";
        }
        return "";
    }

    public String getLibExtension () {
        if (this == Os.Windows) return "dll";
        if (this == Os.Linux) return "so";
        if (this == Os.MacOsX) return "dylib";
        if (this == Os.Android) return "so";
        return "";
    }
}
