package com.badlogic.gdx.utils;

/** The target operating system of a build target. */
public enum Os {
	Windows, Linux, MacOsX, Android, IOS;

	public String getJniPlatform () {
		if (this == Os.Windows) return "win32";
		if (this == Os.Linux) return "linux";
		if (this == Os.MacOsX) return "mac";
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
		return "";
	}
}
