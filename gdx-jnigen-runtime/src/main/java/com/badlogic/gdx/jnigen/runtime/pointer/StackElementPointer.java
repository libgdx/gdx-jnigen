package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;

public abstract class StackElementPointer<T extends StackElement> extends VoidPointer {

    protected StackElementPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected StackElementPointer(long pointer, boolean freeOnGC, int capacity) {
        super(pointer, freeOnGC, capacity);
    }

    public StackElementPointer(int size, int count) {
        this(size, count, true);
    }

    public StackElementPointer(int size, int count, boolean freeOnGC) {
        super(size * count, freeOnGC);
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
        T stackElement = createStackElement(getPointer() + offset, false);
        stackElement.setParent(this);
        return stackElement;
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
