package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.runtime.ffi.ClosureEncoder;

public final class AnonymousClosure extends com.badlogic.gdx.jnigen.runtime.pointer.Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(11).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public AnonymousClosure(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public AnonymousClosure() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public AnonymousClosure.AnonymousClosurePointer asPointer() {
        return new AnonymousClosure.AnonymousClosurePointer(getPointer(), getsGCFreed());
    }

    public ClosureObject<someClosure> someClosure() {
        return CHandler.getClosureObject(getValue(0), someClosure::someClosure_downcall);
    }

    public void someClosure(ClosureObject<someClosure> someClosure) {
        setValue(someClosure.getPointer(), 0);
    }

    public ClosureObject<anotherClosure> anotherClosure() {
        return CHandler.getClosureObject(getValue(1), anotherClosure::anotherClosure_downcall);
    }

    public void anotherClosure(ClosureObject<anotherClosure> anotherClosure) {
        setValue(anotherClosure.getPointer(), 1);
    }

    public static final class AnonymousClosurePointer extends StackElementPointer<AnonymousClosure> {

        public AnonymousClosurePointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousClosurePointer() {
            this(1, true, true);
        }

        public AnonymousClosurePointer(int count, boolean freeOnGC, boolean guard) {
            super(__size, count, freeOnGC, guard);
        }

        public AnonymousClosure.AnonymousClosurePointer guardCount(long count) {
            super.guardCount(count);
            return this;
        }

        public int getSize() {
            return __size;
        }

        protected AnonymousClosure createStackElement(long ptr, boolean freeOnGC) {
            return new AnonymousClosure(ptr, freeOnGC);
        }
    }

    public interface someClosure extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(-1), FFITypes.getCTypeInfo(3) };

        int someClosure_call(CSizedIntPointer arg0, double arg1);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(someClosure_call(new CSizedIntPointer(parameters[0].asLong(), false, "int"), (double) parameters[1].asDouble()));
        }

        public static someClosure someClosure_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, someClosure.__ffi_cache);
            return (arg0, arg1) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                    useEncoder.setValue(1, arg1);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(someClosure.__ffi_cache[someClosure.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (int) returnConvert.asLong();
            };
        }
    }

    public interface anotherClosure extends com.badlogic.gdx.jnigen.runtime.closure.Closure {

        com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] __ffi_cache = new com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] { FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(5), FFITypes.getCTypeInfo(3) };

        float anotherClosure_call(int arg0, double arg1);

        default com.badlogic.gdx.jnigen.runtime.c.CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper[] parameters, com.badlogic.gdx.jnigen.runtime.ffi.JavaTypeWrapper returnType) {
            returnType.setValue(anotherClosure_call((int) parameters[0].asLong(), (double) parameters[1].asDouble()));
        }

        public static anotherClosure anotherClosure_downcall(long fnPtr) {
            ClosureEncoder encoder = new ClosureEncoder(fnPtr, anotherClosure.__ffi_cache);
            return (arg0, arg1) -> {
                ClosureEncoder useEncoder = encoder.lockOrDuplicate();
                {
                    useEncoder.setValue(0, arg0);
                    useEncoder.setValue(1, arg1);
                }
                JavaTypeWrapper returnConvert = new JavaTypeWrapper(anotherClosure.__ffi_cache[anotherClosure.__ffi_cache.length - 1]);
                returnConvert.setValue(useEncoder.invoke());
                return (float) returnConvert.asFloat();
            };
        }
    }
}
