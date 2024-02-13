package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public abstract class StructPointer<T extends Struct> extends Pointing {

    protected StructPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public StructPointer(int size, int count) {
        this(size, count, true, true);
    }

    public StructPointer(int size, int count, boolean freeOnGC, boolean guard) {
        super(size * count, freeOnGC, guard);
    }

    public StructPointer<T> guardCount(long count) {
        super.guardBytes(count * getSize());
        return this;
    }

    public T get() {
        return get(0);
    }

    public T get(int index) {
        int offset = getSize() * index;
        assertBounds(offset);
        long newPtr = CHandler.clone(getPointer() + offset, getSize());
        return createStruct(newPtr, true);
    }

    public T asStruct() {
        return asStruct(0);
    }

    public T asStruct(int index) {
        int offset = getSize() * index;
        assertBounds(offset);
        return createStruct(getPointer() + offset, getsGCFreed());
    }

    public void set(T struct) {
        set(struct, 0);
    }

    public void set(T struct, int index) {
        int offset = getSize() * index;
        assertBounds(offset);
        CHandler.memcpy(getPointer() + offset, struct.getPointer(), struct.getSize());
    }

    public abstract Class<T> getStructClass();

    public abstract int getSize();

    protected abstract T createStruct(long ptr, boolean freeOnGC);
}
