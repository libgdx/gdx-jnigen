package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;

/**
 * This class models the C concept of "pointer-to-pointer" (e.g. int**).
 * The class does not work well more than 2 layers deep.
 *
 * When constructing this class, you need to provide a {@link PointerDereferenceSupplier} that is used, to construct the underlying type on derefernce
 */
public class PointerPointer<T extends Pointing> extends VoidPointer {

    private static final int __pointer_size = CHandler.POINTER_SIZE;
    private PointerDereferenceSupplier<T> supplier;

    public PointerPointer(VoidPointer pointer, PointerDereferenceSupplier<T> supplier) {
        super(pointer);
        this.supplier = supplier;
    }

    public PointerPointer(long pointer, boolean freeOnGC, PointerDereferenceSupplier<T> supplier) {
        super(pointer, freeOnGC);
        this.supplier = supplier;
    }

    public PointerPointer(long pointer, boolean freeOnGC, PointerDereferenceSupplier<T> supplier, int capacity) {
        super(pointer, freeOnGC, capacity * __pointer_size);
        this.supplier = supplier;
    }

    public PointerPointer(PointerDereferenceSupplier<T> supplier) {
        this(1, supplier);
    }

    public PointerPointer(int size, PointerDereferenceSupplier<T> supplier) {
        this(size, true, supplier);
    }

    public PointerPointer(int size, boolean freeOnGC, PointerDereferenceSupplier<T> supplier) {
        super(size * __pointer_size, freeOnGC);
        this.supplier = supplier;
    }

    /**
     * Set the underlying {@link PointerDereferenceSupplier}. Use with caution
     */
    public void setPointerSupplier(PointerDereferenceSupplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the value the pointer is pointing to at index 0.
     */
    public T getValue() {
        return getValue(0);
    }

    /**
     * Gets the value the pointer is pointing to at index `index`.
     */
    public T getValue(int index) {
        int offset = index * __pointer_size;
        long pointer = getBufPtr().getNativePointer(offset);
        return supplier.create(pointer, false);
    }

    /**
     * Gets the value this pointer is pointing to at index `0` and set the pointer to the passed value.
     */
    public void getValue(T value) {
        getValue(0, value);
    }

    /**
     * Gets the value this pointer is pointing to at index `index` and set the pointer to the passed value.
     */
    public void getValue(int index, T value) {
        int offset = index * __pointer_size;
        long pointer = getBufPtr().getNativePointer(offset);
        value.setPointer(pointer);
    }

    /**
     * Sets the value the pointer is pointing to at index 0.
     */
    public void setValue(T value) {
        setValue(value, 0);
    }

    /**
     * Sets the value the pointer is pointing to at index `index`.
     */
    public void setValue(T value, int index) {
        int offset = index * __pointer_size;
        getBufPtr().setNativePointer(offset, value.getPointer());
    }
}
