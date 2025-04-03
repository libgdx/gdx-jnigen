package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Union;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;

/**
 * Some other comment
 */
public final class TestUnion extends Union {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(25).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public TestUnion(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public TestUnion() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public TestUnion.TestUnionPointer asPointer() {
        return new TestUnion.TestUnionPointer(getPointer(), false, this);
    }

    public long uintType() {
        return (long) getValue(0);
    }

    public void uintType(long uintType) {
        setValue(uintType, 0);
    }

    public double doubleType() {
        return (double) getValueDouble(1);
    }

    public void doubleType(double doubleType) {
        setValue(doubleType, 1);
    }

    public SIntPointer fixedSizeInt() {
        return __fixedSizeInt;
    }

    private final SIntPointer __fixedSizeInt = new SIntPointer(getPointer(), false, 3);

    public TestStruct structType() {
        return __structType;
    }

    private final TestStruct __structType = new TestStruct(getPointer(), false);

    public static final class TestUnionPointer extends StackElementPointer<TestUnion> {

        public TestUnionPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public TestUnionPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public TestUnionPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public TestUnionPointer() {
            this(1, true);
        }

        public TestUnionPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected TestUnion createStackElement(long ptr, boolean freeOnGC) {
            return new TestUnion(ptr, freeOnGC);
        }
    }
}
