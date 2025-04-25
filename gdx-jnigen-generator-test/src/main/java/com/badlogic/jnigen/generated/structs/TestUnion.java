package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Union;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;

/**
 * Some other comment
 */
public final class TestUnion extends Union {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(25).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public TestUnion(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public TestUnion() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public TestUnion.TestUnionPointer asPointer() {
        return new TestUnion.TestUnionPointer(getPointer(), false, this);
    }

    public long uintType() {
        return getBufPtr().getLong(0);
    }

    public void uintType(long uintType) {
        getBufPtr().setLong(0, uintType);
    }

    public double doubleType() {
        return getBufPtr().getDouble(0);
    }

    public void doubleType(double doubleType) {
        getBufPtr().setDouble(0, doubleType);
    }

    public SIntPointer fixedSizeInt() {
        return new SIntPointer(getPointer(), false, 3);
    }

    public void fixedSizeInt(SIntPointer toSetPtr) {
        toSetPtr.setPointer(getPointer());
    }

    public SIntPointer getFixedSizeInt() {
        return new SIntPointer(getBufPtr().duplicate(0, 12), false, 3);
    }

    public void getFixedSizeInt(SIntPointer toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, 12);
    }

    public void setFixedSizeInt(SIntPointer toCopyFrom) {
        getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, 12);
    }

    public TestStruct structType() {
        return new TestStruct(getPointer(), false);
    }

    public void structType(TestStruct toSetPtr) {
        toSetPtr.setPointer(getPointer());
    }

    public TestStruct getStructType() {
        return new TestStruct(getBufPtr().duplicate(0, 16), true);
    }

    public void getStructType(TestStruct toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, 16);
    }

    public void setStructType(TestStruct toCopyFrom) {
        getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, 16);
    }

    public static final class TestUnionPointer extends StackElementPointer<TestUnion> {

        public TestUnionPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TestUnionPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public TestUnionPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public TestUnionPointer() {
            this(1, true);
        }

        public TestUnionPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected TestUnion createStackElement(long ptr, boolean freeOnGC) {
            return new TestUnion(ptr, freeOnGC);
        }
    }
}
