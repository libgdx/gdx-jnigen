package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CTypeInfo;

public class PointerPointer<T extends Pointing> extends Pointing {

    private static final int __pointer_size = CHandler.POINTER_SIZE;
    private final PointerDereferenceSupplier<T> supplier;
    private CTypeInfo cTypeInfo;
    private final int depth;

    public PointerPointer(long pointer, boolean freeOnGC, PointerDereferenceSupplier<T> supplier, int depth) {
        super(pointer, freeOnGC);
        this.supplier = supplier;
        this.depth = depth;
    }

    public PointerPointer(PointerDereferenceSupplier<T> supplier, int depth) {
        this(1, supplier, depth);
    }

    public PointerPointer(int size, PointerDereferenceSupplier<T> supplier, int depth) {
        this(size, true, true, supplier, depth);
    }

    public PointerPointer(int size, boolean freeOnGC, boolean guard, PointerDereferenceSupplier<T> supplier, int depth) {
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

    public PointerPointer<T> setBackingCType(String name) {
        this.cTypeInfo = CHandler.getCTypeInfo(name);
        return this;
    }

    public void assertCTypeBackingAndDepth(String name, int depth) {
        assertDepth(depth);
        if (cTypeInfo == null)
            throw new IllegalArgumentException("PointerPointer has no recorded CTypeInfo, set it with setBackingCType");
        if (!cTypeInfo.getName().equals(name))
            throw new IllegalArgumentException("Expected type " + name + " does not match actual type " + cTypeInfo.getName());
    }

    public int getDepth() {
        return depth;
    }

    public T getValue() {
        return getValue(0);
    }

    private void assertChild(T pointerObj) {
        if (depth > 2 && !(pointerObj instanceof PointerPointer))
            throw new IllegalArgumentException("Dereference of depth " + depth + " > 2 should result in an PointerPointer");
        if (pointerObj instanceof PointerPointer && ((PointerPointer<?>)pointerObj).getDepth() != depth - 1)
            throw new IllegalArgumentException("Depth hasn't decreased through dereferencing of PointerPointer");
        if (depth == 2 && cTypeInfo != null && !(pointerObj instanceof CSizedIntPointer))
            throw new IllegalArgumentException("PointerPointer has set a backing CTypeInfo, but doesn't dereference to an CSizedIntPointer");
        if (pointerObj instanceof CSizedIntPointer) {
            if (cTypeInfo == null)
                throw new IllegalArgumentException("PointerPointer dereferences to CTypeInfo, but has not CType info, set it with setBackingCType");
            CSizedIntPointer intPointer = (CSizedIntPointer) pointerObj;
            intPointer.assertHasCTypeBacking(cTypeInfo.getName());
        }
    }

    public T getValue(int index) {
        int offset = index * __pointer_size;
        assertBounds(offset);
        long pointer = CHandler.getPointerPart(getPointer(), __pointer_size, offset);
        T pointerObj = supplier.create(pointer, false);
        assertChild(pointerObj);
        return pointerObj;
    }

    public void setValue(T value) {
        setValue(value, 0);
    }

    public void setValue(T value, int index) {
        int offset = index * __pointer_size;
        assertBounds(offset);
        assertChild(value);
        CHandler.setPointerPart(getPointer(), __pointer_size, offset, value.getPointer());
    }
}
