package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.jnigen.generated.structs.AnonymousStructFieldArray.inner;

public final class AnonymousStructFieldArray extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(17).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructFieldArray(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructFieldArray() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousStructFieldArray.AnonymousStructFieldArrayPointer asPointer() {
        return new AnonymousStructFieldArray.AnonymousStructFieldArrayPointer(getPointer(), false, this);
    }

    public inner.innerPointer inner() {
        return new inner.innerPointer(getPointer(), false, 2);
    }

    public inner.innerPointer getInner() {
        return new inner.innerPointer(getBufPtr().duplicate(0, 16), false, 2);
    }

    public void getInner(inner.innerPointer toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, 16);
    }

    public void setInner(inner.innerPointer toCopyFrom) {
        getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, 16);
    }

    public int externalValue() {
        return getBufPtr().getInt(16);
    }

    public void externalValue(int externalValue) {
        getBufPtr().setInt(16, externalValue);
    }

    public static final class AnonymousStructFieldArrayPointer extends StackElementPointer<AnonymousStructFieldArray> {

        public AnonymousStructFieldArrayPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructFieldArrayPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public AnonymousStructFieldArrayPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public AnonymousStructFieldArrayPointer() {
            this(1, true);
        }

        public AnonymousStructFieldArrayPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructFieldArray createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructFieldArray(ptr, freeOnGC);
        }
    }

    public final static class inner extends Struct {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(29).getFfiType();
            __size = CHandler.getSizeFromFFIType(__ffi_type);
        }

        public inner(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public inner() {
            super(__size);
        }

        public long getSize() {
            return __size;
        }

        public long getFFIType() {
            return __ffi_type;
        }

        public inner.innerPointer asPointer() {
            return new inner.innerPointer(getPointer(), false, this);
        }

        public int intValue() {
            return getBufPtr().getInt(0);
        }

        public void intValue(int intValue) {
            getBufPtr().setInt(0, intValue);
        }

        public float floatValue() {
            return getBufPtr().getFloat(4);
        }

        public void floatValue(float floatValue) {
            getBufPtr().setFloat(4, floatValue);
        }

        public static final class innerPointer extends StackElementPointer<inner> {

            public innerPointer(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public innerPointer(long pointer, boolean freeOnGC, int capacity) {
                super(pointer, freeOnGC, capacity * __size);
            }

            public innerPointer(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public innerPointer() {
                this(1, true);
            }

            public innerPointer(int count, boolean freeOnGC) {
                super(__size, count, freeOnGC);
            }

            public int getSize() {
                return __size;
            }

            protected inner createStackElement(long ptr, boolean freeOnGC) {
                return new inner(ptr, freeOnGC);
            }
        }
    }
}
