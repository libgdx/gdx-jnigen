package com.badlogic.jnigen.generated;

import com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
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

            default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
                returnType.setValue(someClosure_call(new SIntPointer(parameters[0].asLong(), false), (double) parameters[1].asDouble()));
            }

            public static someClosure someClosure_downcall(long fnPtr) {
                ClosureEncoder encoder = new ClosureEncoder(fnPtr, someClosure_Internal.__ffi_cache);
                return (t, p) -> {
                    ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                    useEncoder.setValue(0, t);
                    useEncoder.setValue(1, p);
                    JavaTypeWrapper returnConvert = new JavaTypeWrapper(someClosure_Internal.__ffi_cache[0]);
                    returnConvert.setValue(useEncoder.invoke());
                    return (int) returnConvert.asLong();
                };
            }
        }

        public interface anotherClosure_Internal extends Closure {

            CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(3) };

            float anotherClosure_call(int t, double p);

            default CTypeInfo[] functionSignature() {
                return __ffi_cache;
            }

            default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
                returnType.setValue(anotherClosure_call((int) parameters[0].asLong(), (double) parameters[1].asDouble()));
            }

            public static anotherClosure anotherClosure_downcall(long fnPtr) {
                ClosureEncoder encoder = new ClosureEncoder(fnPtr, anotherClosure_Internal.__ffi_cache);
                return (t, p) -> {
                    ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                    useEncoder.setValue(0, t);
                    useEncoder.setValue(1, p);
                    JavaTypeWrapper returnConvert = new JavaTypeWrapper(anotherClosure_Internal.__ffi_cache[0]);
                    returnConvert.setValue(useEncoder.invoke());
                    return (float) returnConvert.asFloat();
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackBooleanArg_call(parameters[0].asLong() != 0);
        }

        public static methodWithCallbackBooleanArg methodWithCallbackBooleanArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackBooleanArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestEnumPointerArg_call(new TestEnum.TestEnumPointer(parameters[0].asLong(), false));
        }

        public static methodWithCallbackTestEnumPointerArg methodWithCallbackTestEnumPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackDoubleReturn_call());
        }

        public static methodWithCallbackDoubleReturn methodWithCallbackDoubleReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackDoubleReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackDoubleReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (double) returnConvert.asDouble();
            };
        }
    }

    public interface methodWithThrowingCallback_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2) };

        void methodWithThrowingCallback_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackIntArg_call((int) parameters[0].asLong());
        }

        public static methodWithCallbackIntArg methodWithCallbackIntArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructPointerReturn_call());
        }

        public static methodWithCallbackTestStructPointerReturn methodWithCallbackTestStructPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestStructPointerReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestStruct.TestStructPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackShortReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(7) };

        short methodWithCallbackShortReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackShortReturn_call());
        }

        public static methodWithCallbackShortReturn methodWithCallbackShortReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackShortReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackShortReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (short) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackTestEnumPointerReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-1) };

        TestEnum.TestEnumPointer methodWithCallbackTestEnumPointerReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestEnumPointerReturn_call());
        }

        public static methodWithCallbackTestEnumPointerReturn methodWithCallbackTestEnumPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestEnumPointerReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestEnum.TestEnumPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackTestStructArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(24) };

        void methodWithCallbackTestStructArg_call(TestStruct arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructArg_call(new TestStruct(parameters[0].asLong(), true));
        }

        public static methodWithCallbackTestStructArg methodWithCallbackTestStructArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntPointerReturn_call());
        }

        public static methodWithCallbackIntPointerReturn methodWithCallbackIntPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackIntPointerReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return new SIntPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackLongArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(11) };

        void methodWithCallbackLongArg_call(long test);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackLongArg_call((long) parameters[0].asLong());
        }

        public static methodWithCallbackLongArg methodWithCallbackLongArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackLongArg_Internal.__ffi_cache);
            return (test) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, test);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackFloatArg_call((float) parameters[0].asFloat());
        }

        public static methodWithCallbackFloatArg methodWithCallbackFloatArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackFloatArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackDoubleArg_call((double) parameters[0].asDouble());
        }

        public static methodWithCallbackDoubleArg methodWithCallbackDoubleArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackDoubleArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntPointerArg_call(new SIntPointer(parameters[0].asLong(), false)));
        }

        public static methodWithCallbackIntPointerArg methodWithCallbackIntPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackIntPointerArg_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (int) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackTestEnumArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(5) };

        void methodWithCallbackTestEnumArg_call(TestEnum arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestEnumArg_call(TestEnum.getByIndex((int) parameters[0].asLong()));
        }

        public static methodWithCallbackTestEnumArg methodWithCallbackTestEnumArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackCallThrowingCallback_call(CHandler.getClosureObject(parameters[0].asLong(), methodWithThrowingCallback_Internal::methodWithThrowingCallback_downcall));
        }

        public static methodWithCallbackCallThrowingCallback methodWithCallbackCallThrowingCallback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCallThrowingCallback_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestStructPointerArg_call(new TestStruct.TestStructPointer(parameters[0].asLong(), false));
        }

        public static methodWithCallbackTestStructPointerArg methodWithCallbackTestStructPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestUnionPointerReturn_call());
        }

        public static methodWithCallbackTestUnionPointerReturn methodWithCallbackTestUnionPointerReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestUnionPointerReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestUnionPointerReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestUnion.TestUnionPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackByteReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(1) };

        char methodWithCallbackByteReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackByteReturn_call());
        }

        public static methodWithCallbackByteReturn methodWithCallbackByteReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackByteReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackByteReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (char) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackCharReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(9) };

        char methodWithCallbackCharReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackCharReturn_call());
        }

        public static methodWithCallbackCharReturn methodWithCallbackCharReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCharReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackCharReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (char) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackTestEnumReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(5) };

        TestEnum methodWithCallbackTestEnumReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestEnumReturn_call());
        }

        public static methodWithCallbackTestEnumReturn methodWithCallbackTestEnumReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestEnumReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestEnumReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return TestEnum.getByIndex((int) returnConvert.asLong());
            };
        }
    }

    public interface methodWithCallbackAllArgs_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(11), FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(7), FFITypes.getCTypeInfo(1), FFITypes.getCTypeInfo(9), FFITypes.getCTypeInfo(0), FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(3) };

        void methodWithCallbackAllArgs_call(long arg0, int arg1, short arg2, char arg3, char arg4, boolean arg5, float arg6, double arg7);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackAllArgs_call((long) parameters[0].asLong(), (int) parameters[1].asLong(), (short) parameters[2].asLong(), (char) parameters[3].asLong(), (char) parameters[4].asLong(), parameters[5].asLong() != 0, (float) parameters[6].asFloat(), (double) parameters[7].asDouble());
        }

        public static methodWithCallbackAllArgs methodWithCallbackAllArgs_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackAllArgs_Internal.__ffi_cache);
            return (arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
                useEncoder.setValue(1, arg1);
                useEncoder.setValue(2, arg2);
                useEncoder.setValue(3, arg3);
                useEncoder.setValue(4, arg4);
                useEncoder.setValue(5, arg5);
                useEncoder.setValue(6, arg6);
                useEncoder.setValue(7, arg7);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithIntPtrPtrRet_call());
        }

        public static methodWithIntPtrPtrRet methodWithIntPtrPtrRet_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithIntPtrPtrRet_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithIntPtrPtrRet_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return new PointerPointer<>(returnConvert.asLong(), false, SIntPointer::new);
            };
        }
    }

    public interface methodWithCallbackTestUnionPointerArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(-1) };

        void methodWithCallbackTestUnionPointerArg_call(TestUnion.TestUnionPointer arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackTestUnionPointerArg_call(new TestUnion.TestUnionPointer(parameters[0].asLong(), false));
        }

        public static methodWithCallbackTestUnionPointerArg methodWithCallbackTestUnionPointerArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestUnionPointerArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackShortArg_call((short) parameters[0].asLong());
        }

        public static methodWithCallbackShortArg methodWithCallbackShortArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackShortArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
                useEncoder.invoke();
            };
        }
    }

    public interface methodWithCallbackByteArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(1) };

        void methodWithCallbackByteArg_call(char arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackByteArg_call((char) parameters[0].asLong());
        }

        public static methodWithCallbackByteArg methodWithCallbackByteArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackByteArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackBooleanReturn_call());
        }

        public static methodWithCallbackBooleanReturn methodWithCallbackBooleanReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackBooleanReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackBooleanReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return returnConvert.asLong() != 0;
            };
        }
    }

    public interface methodWithCallbackIntReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(5) };

        int methodWithCallbackIntReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackIntReturn_call());
        }

        public static methodWithCallbackIntReturn methodWithCallbackIntReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackIntReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackIntReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (int) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackLongReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(11) };

        long methodWithCallbackLongReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackLongReturn_call());
        }

        public static methodWithCallbackLongReturn methodWithCallbackLongReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackLongReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackLongReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (long) returnConvert.asLong();
            };
        }
    }

    public interface methodWithCallbackCharArg_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(-2), FFITypes.getCTypeInfo(9) };

        void methodWithCallbackCharArg_call(char arg0);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithCallbackCharArg_call((char) parameters[0].asLong());
        }

        public static methodWithCallbackCharArg methodWithCallbackCharArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackCharArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            methodWithIntPtrPtrArg_call(new PointerPointer<>(parameters[0].asLong(), false, SIntPointer::new));
        }

        public static methodWithIntPtrPtrArg methodWithIntPtrPtrArg_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithIntPtrPtrArg_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
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

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(thread_callback_call(new VoidPointer(parameters[0].asLong(), false)));
        }

        public static thread_callback thread_callback_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, thread_callback_Internal.__ffi_cache);
            return (arg0) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                useEncoder.setValue(0, arg0);
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(thread_callback_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return new VoidPointer(returnConvert.asLong(), false);
            };
        }
    }

    public interface methodWithCallbackTestStructReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(24) };

        TestStruct methodWithCallbackTestStructReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackTestStructReturn_call());
        }

        public static methodWithCallbackTestStructReturn methodWithCallbackTestStructReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackTestStructReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackTestStructReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return new TestStruct(returnConvert.asLong(), true);
            };
        }
    }

    public interface methodWithCallbackFloatReturn_Internal extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4) };

        float methodWithCallbackFloatReturn_call();

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(methodWithCallbackFloatReturn_call());
        }

        public static methodWithCallbackFloatReturn methodWithCallbackFloatReturn_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, methodWithCallbackFloatReturn_Internal.__ffi_cache);
            return () -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(methodWithCallbackFloatReturn_Internal.__ffi_cache[0]);
                returnConvert.setValue(useEncoder.invoke());
                return (float) returnConvert.asFloat();
            };
        }
    }
}
