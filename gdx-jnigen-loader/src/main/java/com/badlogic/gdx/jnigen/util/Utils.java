package com.badlogic.gdx.jnigen.util;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.CType;

import java.lang.reflect.AnnotatedElement;

public class Utils {

    public static int getSizeForAnnotatedElement(AnnotatedElement element) {
        CType cType = element.getAnnotation(CType.class);
        if (cType == null)
            throw new IllegalArgumentException("CType annotation is missing on " + element);

        return Global.getCTypeSize(cType.value());
    }

    public static boolean checkBoundsForNumber(long value, long size, boolean signed) {
        if (size == 8)
            return true;

        if (!signed) {
            return value < (1L << (size << 3));
        } else {
            long max = (1L << ((size << 3) - 1)) - 1;
            long min = -1L << ((size << 3) - 1);
            return value >= min && value <= max;
        }
    }
}
