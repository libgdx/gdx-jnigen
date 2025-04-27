package com.badlogic.jnigen.generated.enums;

import com.badlogic.gdx.jnigen.runtime.pointer.EnumPointer;
import com.badlogic.gdx.jnigen.runtime.c.CEnum;
import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

public enum SpecialEnum implements CEnum {

    LOWER(0), HIGH(160);

    private static final int __size = (CHandler.IS_64_BIT && CHandler.IS_COMPILED_UNIX) ? 8 : 4;

    private final int index;

    SpecialEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return __size;
    }

    public static SpecialEnum getByIndex(int index) {
        switch(index) {
            case 0:
                return LOWER;
            case 160:
                return HIGH;
            default:
                throw new IllegalArgumentException("Index " + index + " does not exist.");
        }
    }

    public static final class SpecialEnumPointer extends EnumPointer<SpecialEnum> {

        public SpecialEnumPointer(VoidPointer pointer) {
            super(pointer);
        }

        public SpecialEnumPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public SpecialEnumPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public SpecialEnumPointer() {
            this(1, true);
        }

        public SpecialEnumPointer(int count, boolean freeOnGC) {
            super(count * __size, freeOnGC);
        }

        public SpecialEnum getEnumValue(int index) {
            return getByIndex((int) getBufPtr().getNativeLong(index * __size));
        }

        public void setEnumValue(SpecialEnum value, int index) {
            getBufPtr().setNativeLong(index * __size, value.getIndex());
        }

        public int getSize() {
            return __size;
        }
    }
}
