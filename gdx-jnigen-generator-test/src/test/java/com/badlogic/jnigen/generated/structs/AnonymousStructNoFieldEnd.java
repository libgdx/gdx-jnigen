package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class AnonymousStructNoFieldEnd extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(16).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructNoFieldEnd(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructNoFieldEnd() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousStructNoFieldEnd.AnonymousStructNoFieldEndPointer asPointer() {
        return new AnonymousStructNoFieldEnd.AnonymousStructNoFieldEndPointer(getPointer(), getsGCFreed());
    }

    public int externalValue() {
        return (int) getValue(0);
    }

    public void externalValue(int externalValue) {
        setValue(externalValue, 0);
    }

    public int intValue() {
        return (int) getValue(1);
    }

    public void intValue(int intValue) {
        setValue(intValue, 1);
    }

    public float floatValue() {
        return (float) getValueFloat(2);
    }

    public void floatValue(float floatValue) {
        setValue(floatValue, 2);
    }

    public static final class AnonymousStructNoFieldEndPointer extends StackElementPointer<AnonymousStructNoFieldEnd> {

        public AnonymousStructNoFieldEndPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructNoFieldEndPointer() {
            this(1, true, true);
        }

        public AnonymousStructNoFieldEndPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public AnonymousStructNoFieldEnd.AnonymousStructNoFieldEndPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructNoFieldEnd createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructNoFieldEnd(ptr, freeOnGC);
        }
    }
}
