package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
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
        return new AnonymousStructNoFieldConsecutive.AnonymousStructNoFieldConsecutivePointer(getPointer(), false, this);
    }

    public int externalValue() {
        return (int) getValue(0);
    }

    public void externalValue(int externalValue) {
        setValue(externalValue, 0);
    }

    public int intValue1() {
        return (int) getValue(1);
    }

    public void intValue1(int intValue1) {
        setValue(intValue1, 1);
    }

    public float floatValue1() {
        return (float) getValueFloat(2);
    }

    public void floatValue1(float floatValue1) {
        setValue(floatValue1, 2);
    }

    public int intValue2() {
        return (int) getValue(3);
    }

    public void intValue2(int intValue2) {
        setValue(intValue2, 3);
    }

    public float floatValue2() {
        return (float) getValueFloat(4);
    }

    public void floatValue2(float floatValue2) {
        setValue(floatValue2, 4);
    }

    public static final class AnonymousStructNoFieldConsecutivePointer extends StackElementPointer<AnonymousStructNoFieldConsecutive> {

        public AnonymousStructNoFieldConsecutivePointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousStructNoFieldConsecutivePointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public AnonymousStructNoFieldConsecutivePointer() {
            this(1, true);
        }

        public AnonymousStructNoFieldConsecutivePointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public AnonymousStructNoFieldConsecutive.AnonymousStructNoFieldConsecutivePointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousStructNoFieldConsecutive createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousStructNoFieldConsecutive(ptr, freeOnGC);
        }
    }
}
