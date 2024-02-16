package com.badlogic.gdx.jnigen.util;

import com.badlogic.gdx.jnigen.c.CTypeInfo;

public class Utils {

    public static boolean checkBoundsForNumber(long value, long size, boolean signed) {
        if (size == 8)
            return true;
        if (value < 0 && !signed)
            return false;

        if (!signed) {
            return value < (1L << (size << 3));
        } else {
            long max = (1L << ((size << 3) - 1)) - 1;
            long min = -1L << ((size << 3) - 1);
            return value >= min && value <= max;
        }
    }
}
