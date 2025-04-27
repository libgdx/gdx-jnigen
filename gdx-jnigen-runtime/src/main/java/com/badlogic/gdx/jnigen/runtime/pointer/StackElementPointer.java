package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;

/**
 * This class represents the base for all Pointer pointing to a {@link StackElement}.<br>
 */
public abstract class StackElementPointer<T extends StackElement> extends VoidPointer {

    public StackElementPointer(VoidPointer pointer) {
        super(pointer);
    }

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

    /**
     * This method copies the data from the pointer at index 0 to the argument.<br>
     *
     * @param toWrite Into which StackElement to copy the data to
     */
    public void get(T toWrite) {
        toWrite.getBufPtr().copyFrom(getBufPtr(), getSize());
    }


    /**
     * This method copies the data from the pointer at index `index` to the argument. <br>
     *
     * @param index From which index to copy the data
     * @param toWrite Into which StackElement to copy the data to
     */
    public void get(int index, T toWrite) {
        int offset = getSize() * index;
        toWrite.getBufPtr().copyFrom(0, getBufPtr(), offset, getSize());
    }

    /**
     * Creates a new StackElement and copies the data at the index 0 to this
     * @return The new StackElement
     */
    public T get() {
        return get(0);
    }

    /**
     * Creates a new StackElement and copies the data at the `index` to this
     * @param index From which index to copy the data
     * @return The new StackElement
     */
    public T get(int index) {
        int offset = getSize() * index;
        assertBounds(offset);
        long newPtr = CHandler.clone(getPointer() + offset, getSize());
        return createStackElement(newPtr, true);
    }

    /**
     * Creates a new StackElement, but pointing to the address of this pointer. This means, that any changes to the StackElement, will be reflected to the pointer.
     * @return The new StackElement
     */
    public T asStackElement() {
        return asStackElement(0);
    }

    /**
     * Creates a new StackElement, but pointing to the address + `index` of this pointer. This means, that any changes to the StackElement, will be reflected to the pointer.
     * @param index The index to reinterpret
     * @return The new StackElement
     */
    public T asStackElement(int index) {
        int offset = getSize() * index;
        assertBounds(offset);
        T stackElement = createStackElement(getPointer() + offset, false);
        stackElement.setParent(this);
        return stackElement;
    }

    /**
     * Set the address of the argument StackElement to the address of this pointer. This means, that any changes to the StackElement, will be reflected to the pointer.<br>
     * Note, that `toSetPtr` is not allowed to be registered to GC. All previous data of `toSetPtr` will be lost.
     * @param toSetPtr The StackElement to set the pointer from
     */
    public void asStackElement(T toSetPtr) {
        asStackElement(0, toSetPtr);
    }

    /**
     * Set the address of the argument StackElement to the address + `index` of this pointer. This means, that any changes to the StackElement, will be reflected to the pointer.<br>
     * Note, that `toSetPtr` is not allowed to be registered to GC. All previous data of `toSetPtr` will be lost.
     * @param index The index to get the pointer from
     * @param toSetPtr The StackElement to set the pointer from
     */
    public void asStackElement(int index, T toSetPtr) {
        int offset = getSize() * index;
        assertBounds(offset);
        toSetPtr.setPointer(getPointer() + offset, getSize(), this);
    }


    /**
     * Copies the data from `toSet` into this Pointer.
     * @param toSet The StackElement to copy from.
     */
    public void set(T toSet) {
        getBufPtr().copyFrom(toSet.getBufPtr(), getSize());
    }

    /**
     * Copies the data from `toSet` into this Pointer at the specified index.
     * @param index The index to copy to
     * @param toSet The StackElement to copy from.
     */
    public void set(T toSet, int index) {
        int offset = getSize() * index;
        getBufPtr().copyFrom(offset, toSet.getBufPtr(), 0, getSize());
    }

    protected abstract int getSize();

    protected abstract T createStackElement(long ptr, boolean freeOnGC);
}
