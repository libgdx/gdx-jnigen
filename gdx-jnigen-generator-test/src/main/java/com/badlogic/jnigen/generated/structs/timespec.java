package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class timespec extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(33).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public timespec(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public timespec(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public timespec() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public timespec.timespecPointer asPointer() {
        return new timespec.timespecPointer(getPointer(), false, 1, this);
    }

    public void asPointer(timespec.timespecPointer ptr) {
        ptr.setPointer(this);
    }

    public static final class timespecPointer extends StackElementPointer<timespec> {

        public timespecPointer(VoidPointer pointer) {
            super(pointer);
        }

        public timespecPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public timespecPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public timespecPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public timespecPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public timespecPointer() {
            this(1, true);
        }

        public timespecPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected timespec createStackElement(long ptr, boolean freeOnGC) {
            return new timespec(ptr, freeOnGC);
        }
    }
}
