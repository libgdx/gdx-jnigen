package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;

public final class AnonymousStructNoFieldNested extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(21).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructNoFieldNested(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructNoFieldNested(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
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
        return new AnonymousStructNoFieldNested.AnonymousStructNoFieldNestedPointer(getPointer(), false, 1, this);
    }

    public int intValue1() {
        return getBufPtr().getInt(0);
    }

    public void intValue1(int intValue1) {
        getBufPtr().setInt(0, intValue1);
    }

    public float floatValue2() {
        return getBufPtr().getFloat(4);
    }

    public void floatValue2(float floatValue2) {
        getBufPtr().setFloat(4, floatValue2);
    }

    public int externalValue() {
        return getBufPtr().getInt(8);
    }

    public void externalValue(int externalValue) {
        getBufPtr().setInt(8, externalValue);
    }

    public static final class AnonymousStructNoFieldNestedPointer extends StackElementPointer<AnonymousStructNoFieldNested> {

        public AnonymousStructNoFieldNestedPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructNoFieldNestedPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public AnonymousStructNoFieldNestedPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public AnonymousStructNoFieldNestedPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public AnonymousStructNoFieldNestedPointer() {
            this(1, true);
        }

        public AnonymousStructNoFieldNestedPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructNoFieldNested createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructNoFieldNested(ptr, freeOnGC);
        }
    }
}
