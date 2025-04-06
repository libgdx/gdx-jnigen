package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;

public final class AnonymousStructNoFieldEnd extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(20).getFfiType();
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
        return new AnonymousStructNoFieldEnd.AnonymousStructNoFieldEndPointer(getPointer(), false, this);
    }

    public int externalValue() {
        return (int) getBufPtr().getInt(0);
    }

    public void externalValue(int externalValue) {
        getBufPtr().setInt(0, externalValue);
    }

    /**
     * Anon struct field
     */
    public int intValue() {
        return (int) getBufPtr().getInt(4);
    }

    /**
     * Anon struct field
     */
    public void intValue(int intValue) {
        getBufPtr().setInt(4, intValue);
    }

    public float floatValue() {
        return (float) getBufPtr().getFloat(8);
    }

    public void floatValue(float floatValue) {
        getBufPtr().setFloat(8, floatValue);
    }

    public static final class AnonymousStructNoFieldEndPointer extends StackElementPointer<AnonymousStructNoFieldEnd> {

        public AnonymousStructNoFieldEndPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructNoFieldEndPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public AnonymousStructNoFieldEndPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public AnonymousStructNoFieldEndPointer() {
            this(1, true);
        }

        public AnonymousStructNoFieldEndPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructNoFieldEnd createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructNoFieldEnd(ptr, freeOnGC);
        }
    }
}
