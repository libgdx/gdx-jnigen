package com.badlogic.gdx.jnigen.build;

public final class StringUtils {

    private StringUtils () {
    }

    /** Returns true if {@code string} starts with {@code prefix}, ignoring case. */
    public static boolean startsWithIgnoreCase (String string, String prefix) {
        int prefixLength = prefix.length();
        return string.length() >= prefixLength && string.regionMatches(true, 0, prefix, 0, prefixLength);
    }
}
