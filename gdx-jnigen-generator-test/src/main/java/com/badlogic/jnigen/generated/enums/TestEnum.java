package com.badlogic.jnigen.generated.enums;

import com.badlogic.gdx.jnigen.runtime.pointer.EnumPointer;
import com.badlogic.gdx.jnigen.runtime.c.CEnum;
import com.badlogic.gdx.jnigen.runtime.CHandler;

/**
 * This is a Test Enum
 */
public enum TestEnum implements CEnum {

    /**
     * This is a comment on FIRST
     */
    FIRST(0),
    /**
     * This is a comment on Second and third
     */
    SECOND(1),
    /**
     * This is a comment on Second and third
     */
    THIRD(4);

    private static final int __size = 4;

    private final int index;

    TestEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return __size;
    }

    public static TestEnum getByIndex(int index) {
        switch(index) {
            case 0:
                return FIRST;
            case 1:
                return SECOND;
            case 4:
                return THIRD;
            default:
                throw new IllegalArgumentException("Index " + index + " does not exist.");
        }
    }

    public static final class TestEnumPointer extends EnumPointer<TestEnum> {

        public TestEnumPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TestEnumPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public TestEnumPointer() {
            this(1, true);
        }

        public TestEnumPointer(int count, boolean freeOnGC) {
            super(count, freeOnGC);
        }

        public TestEnum getEnumValue(int index) {
            return getByIndex((int) getBufPtr().getUInt(index * __size));
        }

        public void setEnumValue(TestEnum value, int index) {
            getBufPtr().setUInt(index * __size, value.getIndex());
        }

        public int getSize() {
            return __size;
        }
    }
}
