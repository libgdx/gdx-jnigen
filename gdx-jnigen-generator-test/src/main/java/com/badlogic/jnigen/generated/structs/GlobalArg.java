package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Union;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.PointerPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.jnigen.generated.structs.GlobalArg.allArgs;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;

public final class GlobalArg extends Union {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(22).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public GlobalArg(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public GlobalArg() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public GlobalArg.GlobalArgPointer asPointer() {
        return new GlobalArg.GlobalArgPointer(getPointer(), false, this);
    }

    public long longVal() {
        return (long) getValue(0);
    }

    public void longVal(long longVal) {
        setValue(longVal, 0);
    }

    public int intVal() {
        return (int) getValue(1);
    }

    public void intVal(int intVal) {
        setValue(intVal, 1);
    }

    public short shortVal() {
        return (short) getValue(2);
    }

    public void shortVal(short shortVal) {
        setValue(shortVal, 2);
    }

    public char byteVal() {
        return (char) getValue(3);
    }

    public void byteVal(char byteVal) {
        setValue(byteVal, 3);
    }

    public char charVal() {
        return (char) getValue(4);
    }

    public void charVal(char charVal) {
        setValue(charVal, 4);
    }

    public boolean boolVal() {
        return getValue(5) != 0;
    }

    public void boolVal(boolean boolVal) {
        setValue(boolVal, 5);
    }

    public float floatVal() {
        return (float) getValueFloat(6);
    }

    public void floatVal(float floatVal) {
        setValue(floatVal, 6);
    }

    public double doubleVal() {
        return (double) getValueDouble(7);
    }

    public void doubleVal(double doubleVal) {
        setValue(doubleVal, 7);
    }

    public SIntPointer intPtr() {
        return new SIntPointer(getValue(8), false);
    }

    public void intPtr(SIntPointer intPtr) {
        setValue(intPtr.getPointer(), 8);
    }

    public PointerPointer<SIntPointer> intPtrPtr() {
        return new PointerPointer<>(getValue(9), false, SIntPointer::new);
    }

    public void intPtrPtr(PointerPointer<SIntPointer> intPtrPtr) {
        setValue(intPtrPtr.getPointer(), 9);
    }

    public TestStruct structVal() {
        return __structVal;
    }

    private final TestStruct __structVal = new TestStruct(getPointer(), false);

    public TestStruct.TestStructPointer structPtr() {
        return new TestStruct.TestStructPointer(getValue(11), false);
    }

    public void structPtr(TestStruct.TestStructPointer structPtr) {
        setValue(structPtr.getPointer(), 11);
    }

    public TestEnum enumVal() {
        return TestEnum.getByIndex((int) getValue(12));
    }

    public void enumVal(TestEnum enumVal) {
        setValue(enumVal.getIndex(), 12);
    }

    public TestEnum.TestEnumPointer enumPtr() {
        return new TestEnum.TestEnumPointer(getValue(13), false);
    }

    public void enumPtr(TestEnum.TestEnumPointer enumPtr) {
        setValue(enumPtr.getPointer(), 13);
    }

    public TestUnion.TestUnionPointer unionPtr() {
        return new TestUnion.TestUnionPointer(getValue(14), false);
    }

    public void unionPtr(TestUnion.TestUnionPointer unionPtr) {
        setValue(unionPtr.getPointer(), 14);
    }

    public allArgs allArgs() {
        return __allArgs;
    }

    private final allArgs __allArgs = new allArgs(getPointer(), false);

    public static final class GlobalArgPointer extends StackElementPointer<GlobalArg> {

        public GlobalArgPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public GlobalArgPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public GlobalArgPointer() {
            this(1, true, true);
        }

        public GlobalArgPointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public GlobalArg.GlobalArgPointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected GlobalArg createStackElement(long ptr, boolean freeOnGC) {
            return new GlobalArg(ptr, freeOnGC);
        }
    }

    public final static class allArgs extends Struct {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(26).getFfiType();
            __size = CHandler.getSizeFromFFIType(__ffi_type);
        }

        public allArgs(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public allArgs() {
            super(__size);
        }

        public long getSize() {
            return __size;
        }

        public long getFFIType() {
            return __ffi_type;
        }

        public allArgs.allArgsPointer asPointer() {
            return new allArgs.allArgsPointer(getPointer(), false, this);
        }

        public long arg1() {
            return (long) getValue(0);
        }

        public void arg1(long arg1) {
            setValue(arg1, 0);
        }

        public int arg2() {
            return (int) getValue(1);
        }

        public void arg2(int arg2) {
            setValue(arg2, 1);
        }

        public short arg3() {
            return (short) getValue(2);
        }

        public void arg3(short arg3) {
            setValue(arg3, 2);
        }

        public char arg4() {
            return (char) getValue(3);
        }

        public void arg4(char arg4) {
            setValue(arg4, 3);
        }

        public char arg5() {
            return (char) getValue(4);
        }

        public void arg5(char arg5) {
            setValue(arg5, 4);
        }

        public boolean arg6() {
            return getValue(5) != 0;
        }

        public void arg6(boolean arg6) {
            setValue(arg6, 5);
        }

        public float arg7() {
            return (float) getValueFloat(6);
        }

        public void arg7(float arg7) {
            setValue(arg7, 6);
        }

        public double arg8() {
            return (double) getValueDouble(7);
        }

        public void arg8(double arg8) {
            setValue(arg8, 7);
        }

        public static final class allArgsPointer extends StackElementPointer<allArgs> {

            public allArgsPointer(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public allArgsPointer(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public allArgsPointer() {
                this(1, true, true);
            }

            public allArgsPointer(int count, boolean freeOnGC, boolean guard) {
                super(__size, count, freeOnGC, guard);
            }

            public allArgs.allArgsPointer guardCount(long count) {
                super.guardCount(count);
                return this;
            }

            public int getSize() {
                return __size;
            }

            protected allArgs createStackElement(long ptr, boolean freeOnGC) {
                return new allArgs(ptr, freeOnGC);
            }
        }
    }
}
