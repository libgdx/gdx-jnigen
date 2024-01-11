package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.Struct;

import java.util.HashMap;

public class ParameterTypes {

    public static long mapObjectToID(Class<?> toMap) {

        if (toMap == boolean.class)
            return -1;
        if (toMap == char.class)
            return -1;
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

        throw new IllegalArgumentException("Only primitive types are currently supported");

    }
}
