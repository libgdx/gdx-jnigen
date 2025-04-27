package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.closure.PointingPoolManager;
import com.badlogic.jnigen.generated.structs.AnonymousClosure.someClosure;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureEncoder;
import com.badlogic.gdx.jnigen.runtime.closure.CClosureObject;
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
                buf.setInt(0, someClosure_call(new SIntPointer(buf.getNativePointer(0), false), buf.getDouble(CHandler.IS_64_BIT ? 8 : 4)));
            }

            default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
                buf.setInt(0, someClosure_call(manager.getPointing(SIntPointer.class, buf.getNativePointer(0)), buf.getDouble(CHandler.IS_64_BIT ? 8 : 4)));
            }

            public static CClosureObject<someClosure> someClosure_downcall(long fnPtr) {
                ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
                return new CClosureObject<>((t, p) -> {
                    BufferPtr bufPtr = encoder.lockOrDuplicate();
                    bufPtr.setNativePointer(0, t.getPointer());
                    bufPtr.setDouble(CHandler.IS_64_BIT ? 8 : 4, p);
                    encoder.invoke(bufPtr);
                    int _retPar = bufPtr.getInt(CHandler.IS_64_BIT ? 16 : 12);
                    encoder.finish(bufPtr);
                    return _retPar;
                }, fnPtr, encoder);
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

            default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
                buf.setFloat(0, anotherClosure_call(buf.getInt(0), buf.getDouble(4)));
            }

            public static CClosureObject<anotherClosure> anotherClosure_downcall(long fnPtr) {
                ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
                return new CClosureObject<>((t, p) -> {
                    BufferPtr bufPtr = encoder.lockOrDuplicate();
                    bufPtr.setInt(0, t);
                    bufPtr.setDouble(4, p);
                    encoder.invoke(bufPtr);
                    float _retPar = bufPtr.getFloat(12);
                    encoder.finish(bufPtr);
                    return _retPar;
                }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackBooleanArg_call(buf.getBoolean(0));
        }

        public static CClosureObject<methodWithCallbackBooleanArg> methodWithCallbackBooleanArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setBoolean(0, arg0);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackTestEnumPointerArg_call(manager.getPointing(TestEnum.TestEnumPointer.class, buf.getNativePointer(0)));
        }

        public static CClosureObject<methodWithCallbackTestEnumPointerArg> methodWithCallbackTestEnumPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setDouble(0, methodWithCallbackDoubleReturn_call());
        }

        public static CClosureObject<methodWithCallbackDoubleReturn> methodWithCallbackDoubleReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                double _retPar = bufPtr.getDouble(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithThrowingCallback_call();
        }

        public static CClosureObject<methodWithThrowingCallback> methodWithThrowingCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackIntArg_call(buf.getInt(0));
        }

        public static CClosureObject<methodWithCallbackIntArg> methodWithCallbackIntArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setInt(0, arg0);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setNativePointer(0, methodWithCallbackTestStructPointerReturn_call().getPointer());
        }

        public static CClosureObject<methodWithCallbackTestStructPointerReturn> methodWithCallbackTestStructPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                TestStruct.TestStructPointer _retPar = new TestStruct.TestStructPointer(bufPtr.getNativePointer(0), false);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setShort(0, methodWithCallbackShortReturn_call());
        }

        public static CClosureObject<methodWithCallbackShortReturn> methodWithCallbackShortReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                short _retPar = bufPtr.getShort(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setNativePointer(0, methodWithCallbackTestEnumPointerReturn_call().getPointer());
        }

        public static CClosureObject<methodWithCallbackTestEnumPointerReturn> methodWithCallbackTestEnumPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                TestEnum.TestEnumPointer _retPar = new TestEnum.TestEnumPointer(bufPtr.getNativePointer(0), false);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackTestStructArg_call(manager.getPointing(TestStruct.class, buf.getNativePointer(0)));
        }

        public static CClosureObject<methodWithCallbackTestStructArg> methodWithCallbackTestStructArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setNativePointer(0, methodWithCallbackIntPointerReturn_call().getPointer());
        }

        public static CClosureObject<methodWithCallbackIntPointerReturn> methodWithCallbackIntPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                SIntPointer _retPar = new SIntPointer(bufPtr.getNativePointer(0), false);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackLongArg_call(buf.getLong(0));
        }

        public static CClosureObject<methodWithCallbackLongArg> methodWithCallbackLongArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((test) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setLong(0, test);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackFloatArg_call(buf.getFloat(0));
        }

        public static CClosureObject<methodWithCallbackFloatArg> methodWithCallbackFloatArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setFloat(0, arg0);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackDoubleArg_call(buf.getDouble(0));
        }

        public static CClosureObject<methodWithCallbackDoubleArg> methodWithCallbackDoubleArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setDouble(0, arg0);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setInt(0, methodWithCallbackIntPointerArg_call(manager.getPointing(SIntPointer.class, buf.getNativePointer(0))));
        }

        public static CClosureObject<methodWithCallbackIntPointerArg> methodWithCallbackIntPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
                int _retPar = bufPtr.getInt(CHandler.IS_64_BIT ? 8 : 4);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackTestEnumArg_call(TestEnum.getByIndex((int) buf.getUInt(0)));
        }

        public static CClosureObject<methodWithCallbackTestEnumArg> methodWithCallbackTestEnumArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setUInt(0, arg0.getIndex());
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackCallThrowingCallback_call(CHandler.getClosureObject(buf.getNativePointer(0), methodWithThrowingCallback_Internal::methodWithThrowingCallback_downcall));
        }

        public static CClosureObject<methodWithCallbackCallThrowingCallback> methodWithCallbackCallThrowingCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackTestStructPointerArg_call(manager.getPointing(TestStruct.TestStructPointer.class, buf.getNativePointer(0)));
        }

        public static CClosureObject<methodWithCallbackTestStructPointerArg> methodWithCallbackTestStructPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setNativePointer(0, methodWithCallbackTestUnionPointerReturn_call().getPointer());
        }

        public static CClosureObject<methodWithCallbackTestUnionPointerReturn> methodWithCallbackTestUnionPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                TestUnion.TestUnionPointer _retPar = new TestUnion.TestUnionPointer(bufPtr.getNativePointer(0), false);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setByte(0, methodWithCallbackByteReturn_call());
        }

        public static CClosureObject<methodWithCallbackByteReturn> methodWithCallbackByteReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                byte _retPar = bufPtr.getByte(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setChar(0, methodWithCallbackCharReturn_call());
        }

        public static CClosureObject<methodWithCallbackCharReturn> methodWithCallbackCharReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                char _retPar = bufPtr.getChar(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setUInt(0, methodWithCallbackTestEnumReturn_call().getIndex());
        }

        public static CClosureObject<methodWithCallbackTestEnumReturn> methodWithCallbackTestEnumReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                TestEnum _retPar = TestEnum.getByIndex((int) bufPtr.getUInt(0));
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackAllArgs_call(buf.getLong(0), buf.getInt(8), buf.getShort(12), buf.getByte(14), buf.getChar(15), buf.getBoolean(17), buf.getFloat(18), buf.getDouble(22));
        }

        public static CClosureObject<methodWithCallbackAllArgs> methodWithCallbackAllArgs_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setLong(0, arg0);
                bufPtr.setInt(8, arg1);
                bufPtr.setShort(12, arg2);
                bufPtr.setByte(14, arg3);
                bufPtr.setChar(15, arg4);
                bufPtr.setBoolean(17, arg5);
                bufPtr.setFloat(18, arg6);
                bufPtr.setDouble(22, arg7);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallback_call();
        }

        public static CClosureObject<methodWithCallback> methodWithCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setNativePointer(0, methodWithIntPtrPtrRet_call().getPointer());
        }

        public static CClosureObject<methodWithIntPtrPtrRet> methodWithIntPtrPtrRet_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                PointerPointer<SIntPointer> _retPar = new PointerPointer<>(bufPtr.getNativePointer(0), false, SIntPointer::new);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackTestUnionPointerArg_call(manager.getPointing(TestUnion.TestUnionPointer.class, buf.getNativePointer(0)));
        }

        public static CClosureObject<methodWithCallbackTestUnionPointerArg> methodWithCallbackTestUnionPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackShortArg_call(buf.getShort(0));
        }

        public static CClosureObject<methodWithCallbackShortArg> methodWithCallbackShortArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setShort(0, arg0);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackByteArg_call(buf.getByte(0));
        }

        public static CClosureObject<methodWithCallbackByteArg> methodWithCallbackByteArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setByte(0, arg0);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setBoolean(0, methodWithCallbackBooleanReturn_call());
        }

        public static CClosureObject<methodWithCallbackBooleanReturn> methodWithCallbackBooleanReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                boolean _retPar = bufPtr.getBoolean(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setInt(0, methodWithCallbackIntReturn_call());
        }

        public static CClosureObject<methodWithCallbackIntReturn> methodWithCallbackIntReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                int _retPar = bufPtr.getInt(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setLong(0, methodWithCallbackLongReturn_call());
        }

        public static CClosureObject<methodWithCallbackLongReturn> methodWithCallbackLongReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                long _retPar = bufPtr.getLong(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithCallbackCharArg_call(buf.getChar(0));
        }

        public static CClosureObject<methodWithCallbackCharArg> methodWithCallbackCharArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setChar(0, arg0);
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            methodWithIntPtrPtrArg_call(manager.getPointerPointer(PointerPointer.class, buf.getNativePointer(0), SIntPointer::new));
        }

        public static CClosureObject<methodWithIntPtrPtrArg> methodWithIntPtrPtrArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setNativePointer(0, thread_callback_call(manager.getPointing(VoidPointer.class, buf.getNativePointer(0))).getPointer());
        }

        public static CClosureObject<thread_callback> thread_callback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>((arg0) -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                bufPtr.setNativePointer(0, arg0.getPointer());
                encoder.invoke(bufPtr);
                VoidPointer _retPar = new VoidPointer(bufPtr.getNativePointer(CHandler.IS_64_BIT ? 8 : 4), false);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setNativePointer(0, methodWithCallbackTestStructReturn_call().getPointer());
        }

        public static CClosureObject<methodWithCallbackTestStructReturn> methodWithCallbackTestStructReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                TestStruct _retPar = new TestStruct();
                bufPtr.setNativePointer(0, _retPar.getPointer());
                encoder.invoke(bufPtr);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
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

        default void invokePooled(BufferPtr buf, PointingPoolManager manager) {
            buf.setFloat(0, methodWithCallbackFloatReturn_call());
        }

        public static CClosureObject<methodWithCallbackFloatReturn> methodWithCallbackFloatReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, __ffi_cache);
            return new CClosureObject<>(() -> {
                BufferPtr bufPtr = encoder.lockOrDuplicate();
                encoder.invoke(bufPtr);
                float _retPar = bufPtr.getFloat(0);
                encoder.finish(bufPtr);
                return _retPar;
            }, fnPtr, encoder);
        }
    }
}
