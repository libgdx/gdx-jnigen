package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.jnigen.generated.structs.AnonymousClosure.someClosure;
import com.badlogic.gdx.jnigen.runtime.ffi.ClosureEncoder;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.jnigen.generated.structs.AnonymousClosure.anotherClosure;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackBooleanArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumPointerArg;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackDoubleReturn;
import com.badlogic.jnigen.generated.TestData.methodWithThrowingCallback;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackShortReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructArg;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackLongArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackFloatArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackDoubleArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackCallThrowingCallback;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.jnigen.generated.TestData_Internal.methodWithThrowingCallback_Internal;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructPointerArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestUnionPointerReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackByteReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackCharReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestEnumReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackAllArgs;
import com.badlogic.jnigen.generated.TestData.methodWithCallback;
import com.badlogic.jnigen.generated.TestData.methodWithIntPtrPtrRet;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestUnionPointerArg;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackShortArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackByteArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackBooleanReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackLongReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackCharArg;
import com.badlogic.jnigen.generated.TestData.methodWithIntPtrPtrArg;
import com.badlogic.gdx.jnigen.runtime.pointer.PointerPointer;
import com.badlogic.jnigen.generated.TestData.thread_callback;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTestStructReturn;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackFloatReturn;

public final class TestData_Internal {

    public final static class forwardDeclStruct_Internal {
    }

    public final static class GlobalArg_Internal {

        public final static class allArgs_Internal {
        }
    }

    public final static class AnonymousStructField_Internal {

        public final static class inner_Internal {
        }
    }

    public final static class TestStruct_Internal {
    }

    public final static class AnonymousClosure_Internal {

        public interface someClosure_Internal extends Closure {

            CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(-1), FFITypes.getCTypeInfo(3) };

            int someClosure_call(SIntPointer t, double p);

            default CTypeInfo[] functionSignature() {
                return __ffi_cache;
            }

            default void invoke(BufferPtr buf) {
                buf.setInt(0, someClosure_call(new SIntPointer(buf.getNativePointer(0), false), buf.getDouble(CHandler.IS_32_BIT ? 4 : 8)));
            }

            public static someClosure someClosure_downcall(long fnPtr) {
                ClosureEncoder encoder = new ClosureEncoder(fnPtr, someClosure_Internal.__ffi_cache);
                return (t, p) -> {
                    ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                    useEncoder.getBufPtr().setNativePointer(0, t.getPointer());
                    useEncoder.getBufPtr().setDouble(CHandler.IS_32_BIT ? 4 : 8, p);
                    useEncoder.invoke();
                    return useEncoder.getBufPtr().getInt(CHandler.IS_32_BIT ? 12 : 16);
                };
            }
        }

        public interface anotherClosure_Internal extends Closure {

            CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(3) };

            float anotherClosure_call(int t, double p);

            default CTypeInfo[] functionSignature() {
                return __ffi_cache;
            }

            default void invoke(BufferPtr buf) {
                buf.setFloat(0, anotherClosure_call(buf.getInt(0), buf.getDouble(4)));
            }

            public static anotherClosure anotherClosure_downcall(long fnPtr) {
                ClosureEncoder encoder = new ClosureEncoder(fnPtr, anotherClosure_Internal.__ffi_cache);
                return (t, p) -> {
                    ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                    useEncoder.getBufPtr().setInt(0, t);
                    useEncoder.getBufPtr().setDouble(4, p);
                    useEncoder.invoke();
                    return useEncoder.getBufPtr().getFloat(12);
                };
            }
        }
    }

    public final static class AnonymousStructNoFieldEnd_Internal {
    }

    public final static class AnonymousStructNoField_Internal {
    }

    public final static class AnonymousStructNoFieldNested_Internal {
    }

    public final static class AnonymousStructFieldArray_Internal {

        public final static class inner_Internal {
        }
    }

    public final static class AnonymousStructNoFieldConsecutive_Internal {
    }

    public final static class TestUnion_Internal {
    }

    public final static class SpecialStruct_Internal {
    }

    public interface methodWithCallbackBooleanArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(0) };

        void methodWithCallbackBooleanArg_call(boolean arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackBooleanArg_call(buf.getBoolean(0));
        }

        public static methodWithCallbackBooleanArg methodWithCallbackBooleanArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackBooleanArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setBoolean(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackTestEnumPointerArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestEnumPointerArg_call(TestEnum.TestEnumPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackTestEnumPointerArg_call(new TestEnum.TestEnumPointer(buf.getNativePointer(0), false));
        }

        public static methodWithCallbackTestEnumPointerArg methodWithCallbackTestEnumPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackDoubleReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(3) };

        double methodWithCallbackDoubleReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setDouble(0, methodWithCallbackDoubleReturn_call());
        }

        public static methodWithCallbackDoubleReturn methodWithCallbackDoubleReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackDoubleReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getDouble(0);
            };
        }
    }

    public interface methodWithThrowingCallback_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2) };

        void methodWithThrowingCallback_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithThrowingCallback_call();
        }

        public static methodWithThrowingCallback methodWithThrowingCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithThrowingCallback_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackIntArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(5) };

        void methodWithCallbackIntArg_call(int arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackIntArg_call(buf.getInt(0));
        }

        public static methodWithCallbackIntArg methodWithCallbackIntArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setInt(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackTestStructPointerReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestStruct.TestStructPointer methodWithCallbackTestStructPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setNativePointer(0, methodWithCallbackTestStructPointerReturn_call().getPointer());
        }

        public static methodWithCallbackTestStructPointerReturn methodWithCallbackTestStructPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return new TestStruct.TestStructPointer(useEncoder.getBufPtr().getNativePointer(0), false);
            };
        }
    }

    public interface methodWithCallbackShortReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(7) };

        short methodWithCallbackShortReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setShort(0, methodWithCallbackShortReturn_call());
        }

        public static methodWithCallbackShortReturn methodWithCallbackShortReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackShortReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getShort(0);
            };
        }
    }

    public interface methodWithCallbackTestEnumPointerReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestEnum.TestEnumPointer methodWithCallbackTestEnumPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setNativePointer(0, methodWithCallbackTestEnumPointerReturn_call().getPointer());
        }

        public static methodWithCallbackTestEnumPointerReturn methodWithCallbackTestEnumPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return new TestEnum.TestEnumPointer(useEncoder.getBufPtr().getNativePointer(0), false);
            };
        }
    }

    public interface methodWithCallbackTestStructArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(24) };

        void methodWithCallbackTestStructArg_call(TestStruct arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackTestStructArg_call(new TestStruct(buf.getNativePointer(0), true));
        }

        public static methodWithCallbackTestStructArg methodWithCallbackTestStructArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackIntPointerReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        SIntPointer methodWithCallbackIntPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setNativePointer(0, methodWithCallbackIntPointerReturn_call().getPointer());
        }

        public static methodWithCallbackIntPointerReturn methodWithCallbackIntPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return new SIntPointer(useEncoder.getBufPtr().getNativePointer(0), false);
            };
        }
    }

    public interface methodWithCallbackLongArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(11) };

        void methodWithCallbackLongArg_call(long test);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackLongArg_call(buf.getLong(0));
        }

        public static methodWithCallbackLongArg methodWithCallbackLongArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackLongArg_Internal.__ffi_cache);
            return (test) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setLong(0, test);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackFloatArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(4) };

        void methodWithCallbackFloatArg_call(float arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackFloatArg_call(buf.getFloat(0));
        }

        public static methodWithCallbackFloatArg methodWithCallbackFloatArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackFloatArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setFloat(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackDoubleArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(3) };

        void methodWithCallbackDoubleArg_call(double arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackDoubleArg_call(buf.getDouble(0));
        }

        public static methodWithCallbackDoubleArg methodWithCallbackDoubleArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackDoubleArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setDouble(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackIntPointerArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(-1) };

        int methodWithCallbackIntPointerArg_call(SIntPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setInt(0, methodWithCallbackIntPointerArg_call(new SIntPointer(buf.getNativePointer(0), false)));
        }

        public static methodWithCallbackIntPointerArg methodWithCallbackIntPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
                return useEncoder.getBufPtr().getInt(CHandler.IS_32_BIT ? 4 : 8);
            };
        }
    }

    public interface methodWithCallbackTestEnumArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(14) };

        void methodWithCallbackTestEnumArg_call(TestEnum arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackTestEnumArg_call(TestEnum.getByIndex((int) buf.getUInt(0)));
        }

        public static methodWithCallbackTestEnumArg methodWithCallbackTestEnumArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setUInt(0, arg0.getIndex());
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackCallThrowingCallback_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackCallThrowingCallback_call(ClosureObject<methodWithThrowingCallback> arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackCallThrowingCallback_call(CHandler.getClosureObject(buf.getNativePointer(0), methodWithThrowingCallback_Internal::methodWithThrowingCallback_downcall));
        }

        public static methodWithCallbackCallThrowingCallback methodWithCallbackCallThrowingCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCallThrowingCallback_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackTestStructPointerArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestStructPointerArg_call(TestStruct.TestStructPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackTestStructPointerArg_call(new TestStruct.TestStructPointer(buf.getNativePointer(0), false));
        }

        public static methodWithCallbackTestStructPointerArg methodWithCallbackTestStructPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackTestUnionPointerReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestUnion.TestUnionPointer methodWithCallbackTestUnionPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setNativePointer(0, methodWithCallbackTestUnionPointerReturn_call().getPointer());
        }

        public static methodWithCallbackTestUnionPointerReturn methodWithCallbackTestUnionPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestUnionPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return new TestUnion.TestUnionPointer(useEncoder.getBufPtr().getNativePointer(0), false);
            };
        }
    }

    public interface methodWithCallbackByteReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(1) };

        byte methodWithCallbackByteReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setByte(0, methodWithCallbackByteReturn_call());
        }

        public static methodWithCallbackByteReturn methodWithCallbackByteReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackByteReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getByte(0);
            };
        }
    }

    public interface methodWithCallbackCharReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(9) };

        char methodWithCallbackCharReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setChar(0, methodWithCallbackCharReturn_call());
        }

        public static methodWithCallbackCharReturn methodWithCallbackCharReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCharReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getChar(0);
            };
        }
    }

    public interface methodWithCallbackTestEnumReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(14) };

        TestEnum methodWithCallbackTestEnumReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setUInt(0, methodWithCallbackTestEnumReturn_call().getIndex());
        }

        public static methodWithCallbackTestEnumReturn methodWithCallbackTestEnumReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return TestEnum.getByIndex((int) useEncoder.getBufPtr().getUInt(0));
            };
        }
    }

    public interface methodWithCallbackAllArgs_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(11), FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(7), FFITypes.getCTypeInfo(1), FFITypes.getCTypeInfo(9), FFITypes.getCTypeInfo(0), FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(3) };

        void methodWithCallbackAllArgs_call(long arg0, int arg1, short arg2, byte arg3, char arg4, boolean arg5, float arg6, double arg7);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackAllArgs_call(buf.getLong(0), buf.getInt(8), buf.getShort(12), buf.getByte(14), buf.getChar(15), buf.getBoolean(17), buf.getFloat(18), buf.getDouble(22));
        }

        public static methodWithCallbackAllArgs methodWithCallbackAllArgs_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackAllArgs_Internal.__ffi_cache);
            return (arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setLong(0, arg0);
                useEncoder.getBufPtr().setInt(8, arg1);
                useEncoder.getBufPtr().setShort(12, arg2);
                useEncoder.getBufPtr().setByte(14, arg3);
                useEncoder.getBufPtr().setChar(15, arg4);
                useEncoder.getBufPtr().setBoolean(17, arg5);
                useEncoder.getBufPtr().setFloat(18, arg6);
                useEncoder.getBufPtr().setDouble(22, arg7);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallback_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2) };

        void methodWithCallback_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallback_call();
        }

        public static methodWithCallback methodWithCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallback_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithIntPtrPtrRet_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        PointerPointer<SIntPointer> methodWithIntPtrPtrRet_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setNativePointer(0, methodWithIntPtrPtrRet_call().getPointer());
        }

        public static methodWithIntPtrPtrRet methodWithIntPtrPtrRet_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithIntPtrPtrRet_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return new PointerPointer<>(useEncoder.getBufPtr().getNativePointer(0), false, SIntPointer::new);
            };
        }
    }

    public interface methodWithCallbackTestUnionPointerArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestUnionPointerArg_call(TestUnion.TestUnionPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackTestUnionPointerArg_call(new TestUnion.TestUnionPointer(buf.getNativePointer(0), false));
        }

        public static methodWithCallbackTestUnionPointerArg methodWithCallbackTestUnionPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestUnionPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackShortArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(7) };

        void methodWithCallbackShortArg_call(short arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackShortArg_call(buf.getShort(0));
        }

        public static methodWithCallbackShortArg methodWithCallbackShortArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackShortArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setShort(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackByteArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(1) };

        void methodWithCallbackByteArg_call(byte arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackByteArg_call(buf.getByte(0));
        }

        public static methodWithCallbackByteArg methodWithCallbackByteArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackByteArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setByte(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackBooleanReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(0) };

        boolean methodWithCallbackBooleanReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setBoolean(0, methodWithCallbackBooleanReturn_call());
        }

        public static methodWithCallbackBooleanReturn methodWithCallbackBooleanReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackBooleanReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getBoolean(0);
            };
        }
    }

    public interface methodWithCallbackIntReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(5) };

        int methodWithCallbackIntReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setInt(0, methodWithCallbackIntReturn_call());
        }

        public static methodWithCallbackIntReturn methodWithCallbackIntReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getInt(0);
            };
        }
    }

    public interface methodWithCallbackLongReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(11) };

        long methodWithCallbackLongReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setLong(0, methodWithCallbackLongReturn_call());
        }

        public static methodWithCallbackLongReturn methodWithCallbackLongReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackLongReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getLong(0);
            };
        }
    }

    public interface methodWithCallbackCharArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(9) };

        void methodWithCallbackCharArg_call(char arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithCallbackCharArg_call(buf.getChar(0));
        }

        public static methodWithCallbackCharArg methodWithCallbackCharArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCharArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setChar(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithIntPtrPtrArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithIntPtrPtrArg_call(PointerPointer<SIntPointer> arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            methodWithIntPtrPtrArg_call(new PointerPointer<>(buf.getNativePointer(0), false, SIntPointer::new));
        }

        public static methodWithIntPtrPtrArg methodWithIntPtrPtrArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithIntPtrPtrArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
            };
        }
    }

    public interface thread_callback_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1), FFITypes.getCTypeInfo(-1) };

        VoidPointer thread_callback_call(VoidPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setNativePointer(0, thread_callback_call(new VoidPointer(buf.getNativePointer(0), false)).getPointer());
        }

        public static thread_callback thread_callback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, thread_callback_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.getBufPtr().setNativePointer(0, arg0.getPointer());
                useEncoder.invoke();
                return new VoidPointer(useEncoder.getBufPtr().getNativePointer(CHandler.IS_32_BIT ? 4 : 8), false);
            };
        }
    }

    public interface methodWithCallbackTestStructReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(24) };

        TestStruct methodWithCallbackTestStructReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setNativePointer(0, methodWithCallbackTestStructReturn_call().getPointer());
        }

        public static methodWithCallbackTestStructReturn methodWithCallbackTestStructReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                TestStruct _retPar = new TestStruct();
                useEncoder.getBufPtr().setNativePointer(0, _retPar.getPointer());
                useEncoder.invoke();
                return _retPar;
            };
        }
    }

    public interface methodWithCallbackFloatReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4) };

        float methodWithCallbackFloatReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(BufferPtr buf) {
            buf.setFloat(0, methodWithCallbackFloatReturn_call());
        }

        public static methodWithCallbackFloatReturn methodWithCallbackFloatReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackFloatReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.invoke();
                return useEncoder.getBufPtr().getFloat(0);
            };
        }
    }
}
