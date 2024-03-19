package com.badlogic.gdx.jnigen.generator;

public class JavaUtils {

    public static String cNameToJavaTypeName(String name) {
        return name.replace("struct ", "").replace("union ", "").replace("enum ", "");
    }

    public static String capitalize(String toCapitalize) {
        return toCapitalize.substring(0, 1).toUpperCase() + toCapitalize.substring(1);
    }

    public static String javarizeName(String cName) {
        cName = capitalize(cName);
        int index;
        while ((index = cName.indexOf('_')) != -1) {
            cName = cName.substring(0, index) + capitalize(cName.substring(index + 1));
        }
        return cName;
    }
}
