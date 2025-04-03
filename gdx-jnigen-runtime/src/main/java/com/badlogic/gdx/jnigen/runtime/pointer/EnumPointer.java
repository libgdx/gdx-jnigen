package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CEnum;

public abstract class EnumPointer<T extends CEnum> extends VoidPointer {

    public EnumPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public EnumPointer(int size, boolean freeOnGC, boolean guard) {
        super(size, freeOnGC, guard);
    }

    public EnumPointer<T> guardCount(long count) {
        super.guardBytes(count * getSize());
        return this;
    }

    public T getEnumValue() {
        return getEnumValue(0);
    }

    public T getEnumValue(int index) {
        int offset = index * getSize();
        assertBounds(offset);
        return getEnum((int)CHandler.getPointerPart(getPointer(), getSize(), offset));
    }

    public void setEnumValue(T value) {
        setEnumValue(value, 0);
    }

    public void setEnumValue(T value, int index) {
        int offset = index * getSize();
        assertBounds(offset);
        CHandler.setPointerPart(getPointer(), getSize(), offset, value.getIndex());
    }

    protected abstract T getEnum(int index);
    protected abstract int getSize();
}
