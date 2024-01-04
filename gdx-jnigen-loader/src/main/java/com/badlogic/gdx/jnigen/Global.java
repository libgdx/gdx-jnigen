package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.utils.SharedLibraryLoader;

public class Global {

    static {
        new SharedLibraryLoader("build/libs/gdx-jnigen-loader-2.5.1-SNAPSHOT-natives-desktop.jar").load("jnigen-native");
    }

    /*JNI
    #include <cstdlib>
    */

    public static native long calloc(long size);/*
        return (jlong)calloc(1, size);
    */

    public static native void free(long pointer);/*
        free((void*)pointer);
    */
}
