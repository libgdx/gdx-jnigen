package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.pointer.StructPointer;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ParameterTypes {

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

        if (toMap == boolean.class)
            return -1;
        if (toMap == char.class)
            return -2;
        if (toMap == byte.class)
            return -1;
        if (toMap == short.class)
            return -2;
        if (toMap == int.class)
            return -3;
        if (toMap == long.class)
            return -4;
        if (toMap == float.class)
            return -5;
        if (toMap == double.class)
            return -6;

        if (Struct.class.isAssignableFrom(toMap)) {
            byte flags = buildFlags(toMap, annotations);
            if ((flags & PASS_AS_POINTER) != 0)
                return -7;
            long type = Global.getStructFFIType((Class<? extends Struct>)toMap);
            if (type == 0)
                throw new IllegalArgumentException("Class " + toMap.getName() + " does not got registered yet.");
            return type;
        }

        if (Pointing.class.isAssignableFrom(toMap))
            return -7;

        throw new IllegalArgumentException("Class " + toMap.getName() + " can not be mapped to native.");

    }
}
