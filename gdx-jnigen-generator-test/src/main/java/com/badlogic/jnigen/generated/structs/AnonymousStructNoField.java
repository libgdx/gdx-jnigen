package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;

/**
 * Anonymous struct jaja
 */
public final class AnonymousStructNoField extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(18).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructNoField(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructNoField() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousStructNoField.AnonymousStructNoFieldPointer asPointer() {
        return new AnonymousStructNoField.AnonymousStructNoFieldPointer(getPointer(), false, this);
    }

    public int intValue() {
        return (int) getBufPtr().getInt(0);
    }

    public void intValue(int intValue) {
        getBufPtr().setInt(0, intValue);
    }

    public float floatValue() {
        return (float) getBufPtr().getFloat(4);
    }

    public void floatValue(float floatValue) {
        getBufPtr().setFloat(4, floatValue);
    }

    public int externalValue() {
        return (int) getBufPtr().getInt(8);
    }

    public void externalValue(int externalValue) {
        getBufPtr().setInt(8, externalValue);
    }

    public static final class AnonymousStructNoFieldPointer extends StackElementPointer<AnonymousStructNoField> {

        public AnonymousStructNoFieldPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructNoFieldPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public AnonymousStructNoFieldPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public AnonymousStructNoFieldPointer() {
            this(1, true);
        }

        public AnonymousStructNoFieldPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructNoField createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructNoField(ptr, freeOnGC);
        }
    }
}
