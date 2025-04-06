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
        return (long) getBufPtr().getLong(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void longVal(long longVal) {
        getBufPtr().setLong(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), longVal);
    }

    public int intVal() {
        return (int) getBufPtr().getInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void intVal(int intVal) {
        getBufPtr().setInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), intVal);
    }

    public short shortVal() {
        return (short) getBufPtr().getShort(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void shortVal(short shortVal) {
        getBufPtr().setShort(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), shortVal);
    }

    public byte byteVal() {
        return (byte) getBufPtr().getByte(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void byteVal(byte byteVal) {
        getBufPtr().setByte(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), byteVal);
    }

    public char charVal() {
        return (char) getBufPtr().getChar(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void charVal(char charVal) {
        getBufPtr().setChar(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), charVal);
    }

    public boolean boolVal() {
        return (boolean) getBufPtr().getBoolean(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void boolVal(boolean boolVal) {
        getBufPtr().setBoolean(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), boolVal);
    }

    public float floatVal() {
        return (float) getBufPtr().getFloat(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void floatVal(float floatVal) {
        getBufPtr().setFloat(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), floatVal);
    }

    public double doubleVal() {
        return (double) getBufPtr().getDouble(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
    }

    public void doubleVal(double doubleVal) {
        getBufPtr().setDouble(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), doubleVal);
    }

    public SIntPointer intPtr() {
        return new SIntPointer(getBufPtr().getNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0)), false);
    }

    public void intPtr(SIntPointer intPtr) {
        getBufPtr().setNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), intPtr.getPointer());
    }

    public PointerPointer<SIntPointer> intPtrPtr() {
        return new PointerPointer<>(getBufPtr().getNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0)), false, SIntPointer::new);
    }

    public void intPtrPtr(PointerPointer<SIntPointer> intPtrPtr) {
        getBufPtr().setNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), intPtrPtr.getPointer());
    }

    public TestStruct structVal() {
        return __structVal;
    }

    private final TestStruct __structVal = new TestStruct(getPointer(), false);

    public TestStruct.TestStructPointer structPtr() {
        return new TestStruct.TestStructPointer(getBufPtr().getNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0)), false);
    }

    public void structPtr(TestStruct.TestStructPointer structPtr) {
        getBufPtr().setNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), structPtr.getPointer());
    }

    public TestEnum enumVal() {
        return TestEnum.getByIndex((int) getBufPtr().getUInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0)));
    }

    public void enumVal(TestEnum enumVal) {
        getBufPtr().setUInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), enumVal.getIndex());
    }

    public TestEnum.TestEnumPointer enumPtr() {
        return new TestEnum.TestEnumPointer(getBufPtr().getNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0)), false);
    }

    public void enumPtr(TestEnum.TestEnumPointer enumPtr) {
        getBufPtr().setNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), enumPtr.getPointer());
    }

    public TestUnion.TestUnionPointer unionPtr() {
        return new TestUnion.TestUnionPointer(getBufPtr().getNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0)), false);
    }

    public void unionPtr(TestUnion.TestUnionPointer unionPtr) {
        getBufPtr().setNativePointer(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), unionPtr.getPointer());
    }

    public allArgs allArgs() {
        return __allArgs;
    }

    private final allArgs __allArgs = new allArgs(getPointer(), false);

    public static final class GlobalArgPointer extends StackElementPointer<GlobalArg> {

        public GlobalArgPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public GlobalArgPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public GlobalArgPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public GlobalArgPointer() {
            this(1, true);
        }

        public GlobalArgPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
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
            return (long) getBufPtr().getLong(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0));
        }

        public void arg1(long arg1) {
            getBufPtr().setLong(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 0 : 0) : (CHandler.IS_COMPILED_WIN ? 0 : 0), arg1);
        }

        public int arg2() {
            return (int) getBufPtr().getInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 8 : 8) : (CHandler.IS_COMPILED_WIN ? 8 : 8));
        }

        public void arg2(int arg2) {
            getBufPtr().setInt(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 8 : 8) : (CHandler.IS_COMPILED_WIN ? 8 : 8), arg2);
        }

        public short arg3() {
            return (short) getBufPtr().getShort(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 12 : 12) : (CHandler.IS_COMPILED_WIN ? 12 : 12));
        }

        public void arg3(short arg3) {
            getBufPtr().setShort(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 12 : 12) : (CHandler.IS_COMPILED_WIN ? 12 : 12), arg3);
        }

        public byte arg4() {
            return (byte) getBufPtr().getByte(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 14 : 14) : (CHandler.IS_COMPILED_WIN ? 14 : 14));
        }

        public void arg4(byte arg4) {
            getBufPtr().setByte(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 14 : 14) : (CHandler.IS_COMPILED_WIN ? 14 : 14), arg4);
        }

        public char arg5() {
            return (char) getBufPtr().getChar(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 16 : 16) : (CHandler.IS_COMPILED_WIN ? 16 : 16));
        }

        public void arg5(char arg5) {
            getBufPtr().setChar(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 16 : 16) : (CHandler.IS_COMPILED_WIN ? 16 : 16), arg5);
        }

        public boolean arg6() {
            return (boolean) getBufPtr().getBoolean(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 18 : 18) : (CHandler.IS_COMPILED_WIN ? 18 : 18));
        }

        public void arg6(boolean arg6) {
            getBufPtr().setBoolean(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 18 : 18) : (CHandler.IS_COMPILED_WIN ? 18 : 18), arg6);
        }

        public float arg7() {
            return (float) getBufPtr().getFloat(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 20 : 20) : (CHandler.IS_COMPILED_WIN ? 20 : 20));
        }

        public void arg7(float arg7) {
            getBufPtr().setFloat(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 20 : 20) : (CHandler.IS_COMPILED_WIN ? 20 : 20), arg7);
        }

        public double arg8() {
            return (double) getBufPtr().getDouble(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 24 : 24) : (CHandler.IS_COMPILED_WIN ? 24 : 24));
        }

        public void arg8(double arg8) {
            getBufPtr().setDouble(CHandler.IS_32_BIT ? (CHandler.IS_COMPILED_WIN ? 24 : 24) : (CHandler.IS_COMPILED_WIN ? 24 : 24), arg8);
        }

        public static final class allArgsPointer extends StackElementPointer<allArgs> {

            public allArgsPointer(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public allArgsPointer(long pointer, boolean freeOnGC, int capacity) {
                super(pointer, freeOnGC, capacity * __size);
            }

            public allArgsPointer(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public allArgsPointer() {
                this(1, true);
            }

            public allArgsPointer(int count, boolean freeOnGC) {
                super(__size, count, freeOnGC);
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
