package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.pointer.StructPointer;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ParameterTypes {

    private static final long ffi_type_void;
    private static final long ffi_type_uint8;
    private static final long ffi_type_uint16;
    private static final long ffi_type_uint32;
    private static final long ffi_type_uint64;
    private static final long ffi_type_float;
    private static final long ffi_type_double;
    private static final long ffi_type_pointer;

    static {
        ffi_type_void = fetchFFIType(0);
        ffi_type_uint8 = fetchFFIType(1);
        ffi_type_uint16 = fetchFFIType(2);
        ffi_type_uint32 = fetchFFIType(3);
        ffi_type_uint64 = fetchFFIType(4);
        ffi_type_float = fetchFFIType(5);
        ffi_type_double = fetchFFIType(6);
        ffi_type_pointer = fetchFFIType(7);
    }

    /*JNI
        #include <ffi.h>
        #include "definitions.h"
    */

    private static native long fetchFFIType(int type);/*
        switch (type) {
            case 0:
                return reinterpret_cast<jlong>(&ffi_type_void);
                break;
            case 1:
                return reinterpret_cast<jlong>(&ffi_type_uint8);
                break;
            case 2:
                return reinterpret_cast<jlong>(&ffi_type_uint16);
                break;
            case 3:
                return reinterpret_cast<jlong>(&ffi_type_uint32);
                break;
            case 4:
                return reinterpret_cast<jlong>(&ffi_type_uint64);
                break;
            case 5:
                return reinterpret_cast<jlong>(&ffi_type_float);
                break;
            case 6:
                return reinterpret_cast<jlong>(&ffi_type_double);
                break;
            case 7:
                return reinterpret_cast<jlong>(&ffi_type_pointer);
                break;
            default:
                return -1;
        }
    */


    public static final int PASS_AS_POINTER = 1 << 0;


    public static byte buildFlags(Class<?> toMap, Annotation[] annotations) {
        byte flags = 0;
        if (StructPointer.class.isAssignableFrom(toMap))
            flags |= PASS_AS_POINTER;
        for (Annotation annotation : annotations) {

        }
        return flags;
    }

    public static long mapObjectToID(Class<?> toMap, Annotation[] annotations) {
        if (toMap == void.class)
            return ffi_type_void;
        if (toMap == boolean.class)
            return ffi_type_uint8;
        if (toMap == char.class)
            return ffi_type_uint16;
        if (toMap == byte.class)
            return ffi_type_uint8;
        if (toMap == short.class)
            return ffi_type_uint16;
        if (toMap == int.class)
            return ffi_type_uint32;
        if (toMap == long.class)
            return ffi_type_uint64;
        if (toMap == float.class)
            return ffi_type_float;
        if (toMap == double.class)
            return ffi_type_double;

        if (Struct.class.isAssignableFrom(toMap)) {
            byte flags = buildFlags(toMap, annotations);
            long type = Global.getStructFFIType((Class<? extends Struct>)toMap);
            if (type == 0)
                throw new IllegalArgumentException("Class " + toMap.getName() + " does not got registered yet.");
            return type;
        }

        if (Pointing.class.isAssignableFrom(toMap))
            return ffi_type_pointer;

        throw new IllegalArgumentException("Class " + toMap.getName() + " can not be mapped to native.");

    }
}
