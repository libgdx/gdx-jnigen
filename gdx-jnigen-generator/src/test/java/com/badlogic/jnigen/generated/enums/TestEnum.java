package com.badlogic.jnigen.generated.enums;

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
}
