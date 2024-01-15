package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.Struct;
import com.badlogic.gdx.jnigen.pointer.Pointing;

public class ParameterTypes {

    public static long mapObjectToID(Class<?> toMap) {

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
