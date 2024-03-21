package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class AnonymousStructNoFieldNested extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(16).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructNoFieldNested(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructNoFieldNested() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousStructNoFieldNested.AnonymousStructNoFieldNestedPointer asPointer() {
        return new AnonymousStructNoFieldNested.AnonymousStructNoFieldNestedPointer(getPointer(), getsGCFreed());
    }

    public int intValue1() {
        return (int) getValue(0);
    }

    public void intValue1(int intValue1) {
        setValue(intValue1, 0);
    }

    public float floatValue2() {
        return (float) getValueFloat(1);
    }

    public void floatValue2(float floatValue2) {
        setValue(floatValue2, 1);
    }

    public int externalValue() {
        return (int) getValue(2);
    }

    public void externalValue(int externalValue) {
        setValue(externalValue, 2);
    }

    public static final class AnonymousStructNoFieldNestedPointer extends StackElementPointer<AnonymousStructNoFieldNested> {

        public AnonymousStructNoFieldNestedPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructNoFieldNestedPointer() {
            this(1, true, true);
        }

        public AnonymousStructNoFieldNestedPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public AnonymousStructNoFieldNested.AnonymousStructNoFieldNestedPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructNoFieldNested createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructNoFieldNested(ptr, freeOnGC);
        }
    }
}
