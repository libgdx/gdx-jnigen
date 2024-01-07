package com.badlogic.gdx.jnigen;

import com.badlogic.gdx.utils.SharedLibraryLoader;

public class Global {

    static {
        new SharedLibraryLoader("build/libs/gdx-jnigen-loader-2.5.1-SNAPSHOT-natives-desktop.jar").load("jnigen-native");
    }

    /*JNI
    #include <stdlib.h>
    #include <string.h>
    */

    public static native long malloc(long size);/*
        return (jlong)malloc(size);
    */

    public static native long calloc(long size);/*
        return (jlong)calloc(1, size);
    */

    public static native void free(long pointer);/*
        free((void*)pointer);
    */

    public static native void memcpy(long dst, long src, long size);/*
        memcpy((void*)dst, (void*)src, size);
    */
}
