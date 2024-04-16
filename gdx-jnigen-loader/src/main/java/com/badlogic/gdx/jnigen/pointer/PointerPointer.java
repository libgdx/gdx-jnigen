package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CTypeInfo;

public class PointerPointer<T extends Pointing> extends Pointing {

    private static final int __pointer_size = CHandler.POINTER_SIZE;
    private final PointerDereferenceSupplier<T> supplier;
    private CTypeInfo cTypeInfo;

    public PointerPointer(long pointer, boolean freeOnGC, PointerDereferenceSupplier<T> supplier) {
        super(pointer, freeOnGC);
        this.supplier = supplier;
    }

    public PointerPointer(PointerDereferenceSupplier<T> supplier) {
        this(1, supplier);
    }

    public PointerPointer(int size, PointerDereferenceSupplier<T> supplier) {
        this(size, true, true, supplier);
    }

    public PointerPointer(int size, boolean freeOnGC, boolean guard, PointerDereferenceSupplier<T> supplier) {
        super(size, freeOnGC, guard);
        this.supplier = supplier;
    }

    public PointerPointer<T> guardCount(long count) {
        super.guardBytes(__pointer_size * count);
        return this;
    }

    public PointerPointer<T> setBackingCType(String name) {
        this.cTypeInfo = CHandler.getCTypeInfo(name);
        return this;
    }

    public void assertCTypeBacking(String name) {
        if (cTypeInfo == null)
            throw new IllegalArgumentException("PointerPointer has no recorded CTypeInfo, set it with setBackingCType");
        if (!cTypeInfo.getName().equals(name))
            throw new IllegalArgumentException("Expected type " + name + " does not match actual type " + cTypeInfo.getName());
    }

    public T getValue() {
        return getValue(0);
    }

    private void assertChild(T pointerObj) {
        if (pointerObj instanceof PointerPointer)
            return;
        if (cTypeInfo != null && !(pointerObj instanceof CSizedIntPointer))
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
