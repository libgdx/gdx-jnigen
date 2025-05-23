package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class AnonymousStructNoFieldConsecutive extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(19).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousStructNoFieldConsecutive(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousStructNoFieldConsecutive(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public AnonymousStructNoFieldConsecutive() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousStructNoFieldConsecutive.AnonymousStructNoFieldConsecutivePointer asPointer() {
        return new AnonymousStructNoFieldConsecutive.AnonymousStructNoFieldConsecutivePointer(getPointer(), false, 1, this);
    }

    public void asPointer(AnonymousStructNoFieldConsecutive.AnonymousStructNoFieldConsecutivePointer ptr) {
        ptr.setPointer(this);
    }

    public int externalValue() {
        return getBufPtr().getInt(0);
    }

    public void externalValue(int externalValue) {
        getBufPtr().setInt(0, externalValue);
    }

    public int intValue1() {
        return getBufPtr().getInt(4);
    }

    public void intValue1(int intValue1) {
        getBufPtr().setInt(4, intValue1);
    }

    public float floatValue1() {
        return getBufPtr().getFloat(8);
    }

    public void floatValue1(float floatValue1) {
        getBufPtr().setFloat(8, floatValue1);
    }

    public int intValue2() {
        return getBufPtr().getInt(12);
    }

    public void intValue2(int intValue2) {
        getBufPtr().setInt(12, intValue2);
    }

    public float floatValue2() {
        return getBufPtr().getFloat(16);
    }

    public void floatValue2(float floatValue2) {
        getBufPtr().setFloat(16, floatValue2);
    }

    public static final class AnonymousStructNoFieldConsecutivePointer extends StackElementPointer<AnonymousStructNoFieldConsecutive> {

        public AnonymousStructNoFieldConsecutivePointer(VoidPointer pointer) {
            super(pointer);
        }

        public AnonymousStructNoFieldConsecutivePointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructNoFieldConsecutivePointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public AnonymousStructNoFieldConsecutivePointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public AnonymousStructNoFieldConsecutivePointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public AnonymousStructNoFieldConsecutivePointer() {
            this(1, true);
        }

        public AnonymousStructNoFieldConsecutivePointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructNoFieldConsecutive createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructNoFieldConsecutive(ptr, freeOnGC);
        }
    }
}
