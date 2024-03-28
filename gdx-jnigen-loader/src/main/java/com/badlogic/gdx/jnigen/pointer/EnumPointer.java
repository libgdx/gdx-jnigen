package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CEnum;

public abstract class EnumPointer<T extends CEnum> extends Pointing {

    private static final int __int_size = CHandler.getCTypeInfo("int").getSize();

    public EnumPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public EnumPointer(int size, boolean freeOnGC, boolean guard) {
        super(__int_size * size, freeOnGC, guard);
    }

    public EnumPointer<T> guardCount(long count) {
        super.guardBytes(count * __int_size);
        return this;
    }

    public T getEnumValue() {
        return getEnumValue(0);
    }

    public T getEnumValue(int index) {
        int offset = index * __int_size;
        assertBounds(offset);
        return getEnum((int)CHandler.getPointerPart(getPointer(), __int_size, offset));
    }

    public void setEnumValue(T value) {
        setEnumValue(value, 0);
    }

    public void setEnumValue(T value, int index) {
        int offset = index * __int_size;
        assertBounds(offset);
        CHandler.setPointerPart(getPointer(), __int_size, offset, value.getIndex());
    }

    protected abstract T getEnum(int index);
}
