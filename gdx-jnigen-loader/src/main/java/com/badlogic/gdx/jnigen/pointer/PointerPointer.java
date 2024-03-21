package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public class PointerPointer<T extends Pointing> extends Pointing {

    private static final int __pointer_size = CHandler.POINTER_SIZE;
    private final PointerDereferenceSupplier<?> supplier;
    private final int depth;

    public PointerPointer(long pointer, boolean freeOnGC, PointerDereferenceSupplier<?> supplier, int depth) {
        super(pointer, freeOnGC);
        this.supplier = supplier;
        this.depth = depth;
    }

    public PointerPointer(PointerDereferenceSupplier<?> supplier, int depth) {
        this(1, supplier, depth);
    }

    public PointerPointer(int size, PointerDereferenceSupplier<?> supplier, int depth) {
        this(size, true, true, supplier, depth);
    }

    public PointerPointer(int size, boolean freeOnGC, boolean guard, PointerDereferenceSupplier<?> supplier, int depth) {
        super(size, freeOnGC, guard);
        this.supplier = supplier;
        this.depth = depth;
        if (depth < 2)
            throw new IllegalArgumentException("Depth is " + depth + " which is below 2. Depth needs to be at least 2, otherwise use the direct pointer types");
    }

    public PointerPointer<T> guardCount(long count) {
        super.guardBytes(__pointer_size * count);
        return this;
    }

    public void assertDepth(int depth) {
        if (this.depth != depth)
            throw new IllegalArgumentException("Expected depth " + depth + " but got " + this.depth);
    }

    public int getDepth() {
        return depth;
    }

    public T getValue() {
        return getValue(0);
    }

    @SuppressWarnings("unchecked")
    public T getValue(int index) {
        int offset = index * __pointer_size;
        assertBounds(offset);
        long pointer = CHandler.getPointerPart(getPointer(), __pointer_size, offset);
        if (depth == 2)
            return (T)supplier.create(pointer, false);
        return (T)new PointerPointer<>(pointer, false, supplier, depth - 1);
    }

    public void setValue(T value) {
        setValue(value, 0);
    }

    public void setValue(T value, int index) {
        int offset = index * __pointer_size;
        assertBounds(offset);
        CHandler.setPointerPart(getPointer(), __pointer_size, offset, value.getPointer());
    }
}
