package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class TimeHolder extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(28).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public TimeHolder(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public TimeHolder(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public TimeHolder() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public TimeHolder.TimeHolderPointer asPointer() {
        return new TimeHolder.TimeHolderPointer(getPointer(), false, 1, this);
    }

    public void asPointer(TimeHolder.TimeHolderPointer ptr) {
        ptr.setPointer(this);
    }

    public static final class TimeHolderPointer extends StackElementPointer<TimeHolder> {

        public TimeHolderPointer(VoidPointer pointer) {
            super(pointer);
        }

        public TimeHolderPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TimeHolderPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public TimeHolderPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public TimeHolderPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public TimeHolderPointer() {
            this(1, true);
        }

        public TimeHolderPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected TimeHolder createStackElement(long ptr, boolean freeOnGC) {
            return new TimeHolder(ptr, freeOnGC);
        }
    }
}
