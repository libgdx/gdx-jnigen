package com.badlogic.jnigen.generated.enums;

import com.badlogic.gdx.jnigen.pointer.EnumPointer;
import com.badlogic.gdx.jnigen.c.CEnum;

public enum TestEnum implements CEnum {

    FIRST(0), SECOND(1), THIRD(4);

    private final int index;

    TestEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static TestEnum getByIndex(int index) {
        return _values[index];
    }

    private final static TestEnum[] _values = { FIRST, SECOND, null, null, THIRD };

    public static final class TestEnumPointer extends EnumPointer<TestEnum> {

        public TestEnumPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TestEnumPointer() {
            this(1, true, true);
        }

        public TestEnumPointer(int count, boolean freeOnGC, boolean guard) {
            super(count, freeOnGC, guard);
        }

        public TestEnum.TestEnumPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        protected TestEnum getEnum(int index) {
            return getByIndex(index);
        }
    }
}
