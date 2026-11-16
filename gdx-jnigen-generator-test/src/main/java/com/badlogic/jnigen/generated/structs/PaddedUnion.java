package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Union;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;

public final class PaddedUnion extends Union {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(46).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public PaddedUnion(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public PaddedUnion(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public PaddedUnion() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public PaddedUnion.PaddedUnionPointer asPointer() {
        return new PaddedUnion.PaddedUnionPointer(getPointer(), false, 1, this);
    }

    public void asPointer(PaddedUnion.PaddedUnionPointer ptr) {
        ptr.setPointer(this);
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
        toSetPtr.setPointer(getPointer(), 12, this);
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

    public static final class PaddedUnionPointer extends StackElementPointer<PaddedUnion> {

        public PaddedUnionPointer(VoidPointer pointer) {
            super(pointer);
        }

        public PaddedUnionPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public PaddedUnionPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public PaddedUnionPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public PaddedUnionPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public PaddedUnionPointer() {
            this(1, true);
        }

        public PaddedUnionPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected PaddedUnion createStackElement(long ptr, boolean freeOnGC) {
            return new PaddedUnion(ptr, freeOnGC);
        }
    }
}
