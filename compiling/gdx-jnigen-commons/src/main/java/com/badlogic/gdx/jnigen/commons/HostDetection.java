package com.badlogic.gdx.jnigen.commons;

public class HostDetection {

    static public Os os;
    static public Architecture.Bitness bitness = Architecture.Bitness._32;
    static public Architecture architecture = Architecture.x86;

    static {
        if (System.getProperty("os.name").contains("Windows"))
            os = Os.Windows;
        else if (System.getProperty("os.name").contains("Linux"))
            os = Os.Linux;
        else if (System.getProperty("os.name").contains("Mac"))
            os = Os.MacOsX;

        if (System.getProperty("os.arch").startsWith("arm") || System.getProperty("os.arch").startsWith("aarch64"))
            architecture = Architecture.ARM;
        else if (System.getProperty("os.arch").startsWith("riscv"))
            architecture = Architecture.RISCV;
        else if (System.getProperty("os.arch").startsWith("loongarch"))
            architecture = Architecture.LOONGARCH;

        if (System.getProperty("os.arch").contains("64") || System.getProperty("os.arch").startsWith("armv8"))
            bitness = Architecture.Bitness._64;
        else if (System.getProperty("os.arch").contains("128"))
            bitness = Architecture.Bitness._128;

        boolean isMOEiOS = System.getProperty("moe.platform.name") != null;
        String vm = System.getProperty("java.runtime.name");
        if (vm != null && vm.contains("Android Runtime")) {
            os = Os.Android;
            bitness = Architecture.Bitness._32;
            architecture = Architecture.x86;
        }
        if (isMOEiOS || (os != Os.Android && os != Os.Windows && os != Os.Linux && os != Os.MacOsX)) {
            os = Os.IOS;
            bitness = Architecture.Bitness._32;
            architecture = Architecture.x86;
        }
    }
}
