package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.c.CEnum;

public abstract class EnumPointer<T extends CEnum> extends VoidPointer {

    public EnumPointer(VoidPointer pointer) {
        super(pointer);
    }

    protected EnumPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected EnumPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity);
    }

    public EnumPointer(int size, boolean freeOnGC) {
        super(size, freeOnGC);
    }

    public T getEnumValue() {
        return getEnumValue(0);
    }

    public void setEnumValue(T value) {
        setEnumValue(value, 0);
    }

    public abstract T getEnumValue(int index);
    public abstract void setEnumValue(T value, int index);
}
