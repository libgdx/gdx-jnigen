package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class FILE extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(23).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public FILE(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public FILE(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public FILE() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public FILE.FILEPointer asPointer() {
        return new FILE.FILEPointer(getPointer(), false, 1, this);
    }

    public void asPointer(FILE.FILEPointer ptr) {
        ptr.setPointer(this);
    }

    public static final class FILEPointer extends StackElementPointer<FILE> {

        public FILEPointer(VoidPointer pointer) {
            super(pointer);
        }

        public FILEPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public FILEPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public FILEPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public FILEPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public FILEPointer() {
            this(1, true);
        }

        public FILEPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected FILE createStackElement(long ptr, boolean freeOnGC) {
            return new FILE(ptr, freeOnGC);
        }
    }
}
