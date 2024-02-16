package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CEnum;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class ParameterTypes {

    private static final long ffi_type_void;
    private static final long ffi_type_pointer;

    static {
        ffi_type_void = fetchFFIType(0);
        ffi_type_pointer = fetchFFIType(1);
    }

    /*JNI
        #include <ffi.h>
    */

    private static native long fetchFFIType(int type);/*
        switch (type) {
            case 0:
                return reinterpret_cast<jlong>(&ffi_type_void);
                break;
            case 1:
                return reinterpret_cast<jlong>(&ffi_type_pointer);
                break;
            default:
                return -1;
        }
    */
}
