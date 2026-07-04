package com.badlogic.jnigen.generated.enums;

import com.badlogic.gdx.jnigen.runtime.pointer.EnumPointer;
import com.badlogic.gdx.jnigen.runtime.c.CEnum;
import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public enum public_color implements CEnum {

    COLOR_RED(1), COLOR_GREEN(2), COLOR_BLUE(3);

    private static final int __size = 4;

    private final int index;

    public_color(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return __size;
    }

    public static public_color getByIndex(int index) {
        switch(index) {
            case 1:
                return COLOR_RED;
            case 2:
                return COLOR_GREEN;
            case 3:
                return COLOR_BLUE;
            default:
                throw new IllegalArgumentException("Index " + index + " does not exist.");
        }
    }

    public static final class public_colorPointer extends EnumPointer<public_color> {

        public public_colorPointer(VoidPointer pointer) {
            super(pointer);
        }

        public public_colorPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public public_colorPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public public_colorPointer() {
            this(1, true);
        }

        public public_colorPointer(int count, boolean freeOnGC) {
            super(count * __size, freeOnGC);
        }

        public public_color getEnumValue(int index) {
            return getByIndex((int) getBufPtr().getUInt(index * __size));
        }

        public void setEnumValue(public_color value, int index) {
            getBufPtr().setUInt(index * __size, value.getIndex());
        }
    }
}
