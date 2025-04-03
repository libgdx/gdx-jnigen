package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;

public class PointerPointer<T extends Pointing> extends VoidPointer {

    private static final int __pointer_size = CHandler.POINTER_SIZE;
    private final PointerDereferenceSupplier<T> supplier;

    public PointerPointer(long pointer, boolean freeOnGC, PointerDereferenceSupplier<T> supplier) {
        super(pointer, freeOnGC);
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

    public PointerPointer<T> guardCount(long count) {
        super.guardBytes(__pointer_size * count);
        return this;
    }

    public T getValue() {
        return getValue(0);
    }

    public T getValue(int index) {
        int offset = index * __pointer_size;
        assertBounds(offset);
        long pointer = getBufPtr().getNativePointer(offset);
        return supplier.create(pointer, false);
    }

    public void setValue(T value) {
        setValue(value, 0);
    }

    public void setValue(T value, int index) {
        int offset = index * __pointer_size;
        assertBounds(offset);
        getBufPtr().setNativePointer(offset, value.getPointer());
    }
}
