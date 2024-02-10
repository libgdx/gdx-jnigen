package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CEnum;
import com.badlogic.gdx.jnigen.c.Signed;
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


    public static final byte PASS_AS_POINTER = 1 << 0;
    public static final byte SIGNED = 1 << 2;


    public static byte buildFlags(Class<?> toMap, Annotation[] annotations) {
        byte flags = 0;
        if (Pointing.class.isAssignableFrom(toMap) && !Struct.class.isAssignableFrom(toMap))
            flags |= PASS_AS_POINTER;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Signed.class)
                flags |= SIGNED;
        }
        return flags;
    }

    public static long mapToFFIType(Class<?> toMap, AnnotatedElement element) {
        byte flags = buildFlags(toMap, element.getAnnotations());

        if (toMap == void.class)
            return ffi_type_void;

        if (toMap.isPrimitive())
            return Utils.getFFITypeForElement(element);

        if (CEnum.class.isAssignableFrom(toMap))
            return CHandler.getCTypeFFIType("int"); // TODO Converting to CType annotation on parameter?

        if (Struct.class.isAssignableFrom(toMap)) {
            long type = CHandler.getStructFFIType((Class<? extends Struct>)toMap);
            if (type == 0)
                throw new IllegalArgumentException("Class " + toMap.getName() + " does not got registered yet.");
            return type;
        }

        if (Pointing.class.isAssignableFrom(toMap))
            return ffi_type_pointer;

        throw new IllegalArgumentException("Class " + toMap.getName() + " can not be mapped to native.");

    }
}
