package com.badlogic.jnigen.generated.enums;

import com.badlogic.gdx.jnigen.pointer.CEnum;

public enum TestEnum implements CEnum {

    FIRST(0), SECOND(1), THIRD(2);

    private final int index;

    TestEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
