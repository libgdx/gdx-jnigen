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

    public void get(T toWrite) {
        toWrite.getBufPtr().copyFrom(getBufPtr(), getSize());
    }

    public void get(int index, T toWrite) {
        int offset = getSize() * index;
        toWrite.getBufPtr().copyFrom(0, getBufPtr(), offset, getSize());
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
        // TODO: 07.04.25 Maybe it's smarter to allow creating Pointing objects via BufferPtr
        stackElement.setParent(this);
        return stackElement;
    }

    public void set(T struct) {
        getBufPtr().copyFrom(struct.getBufPtr(), getSize());
    }

    public void set(T struct, int index) {
        int offset = getSize() * index;
        getBufPtr().copyFrom(offset, struct.getBufPtr(), 0, getSize());
    }

    public abstract int getSize();

    protected abstract T createStackElement(long ptr, boolean freeOnGC);
}
