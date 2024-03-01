package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public abstract class StackElementPointer<T extends StackElement> extends Pointing {

    protected StackElementPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public StackElementPointer(int size, int count) {
        this(size, count, true, true);
    }

    public StackElementPointer(int size, int count, boolean freeOnGC, boolean guard) {
        super(size * count, freeOnGC, guard);
    }

    public StackElementPointer<T> guardCount(long count) {
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
        return createStackElement(newPtr, true);
    }

    public T asStackElement() {
        return asStackElement(0);
    }

    public T asStackElement(int index) {
        int offset = getSize() * index;
        assertBounds(offset);
        return createStackElement(getPointer() + offset, getsGCFreed());
    }

    public void set(T struct) {
        set(struct, 0);
    }

    public void set(T struct, int index) {
        int offset = getSize() * index;
        assertBounds(offset);
        CHandler.memcpy(getPointer() + offset, struct.getPointer(), struct.getSize());
    }

    public abstract int getSize();

    protected abstract T createStackElement(long ptr, boolean freeOnGC);
}
