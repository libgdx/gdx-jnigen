package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.jnigen.generated.structs.AnonymousClosure.someClosure;
import com.badlogic.jnigen.generated.structs.AnonymousClosure.anotherClosure;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;

public final class AnonymousClosure extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(10).getFfiType();
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
        return CHandler.getClosureObject(getValue(0));
    }

    public void someClosure(ClosureObject<someClosure> someClosure) {
        setValue(someClosure.getFnPtr(), 0);
    }

    public ClosureObject<anotherClosure> anotherClosure() {
        return CHandler.getClosureObject(getValue(1));
    }

    public void anotherClosure(ClosureObject<anotherClosure> anotherClosure) {
        setValue(anotherClosure.getFnPtr(), 1);
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

    public interface someClosure extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(-1), FFITypes.getCTypeInfo(2) };

        int someClosure_call(CSizedIntPointer arg0, double arg1);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(someClosure_call(new CSizedIntPointer(parameters[0].asLong(), false, "int"), (double) parameters[1].asDouble()));
        }
    }

    public interface anotherClosure extends Closure {

        CTypeInfo[] __ffi_cache = new CTypeInfo[] { FFITypes.getCTypeInfo(3), FFITypes.getCTypeInfo(4), FFITypes.getCTypeInfo(2) };

        float anotherClosure_call(int arg0, double arg1);

        default CTypeInfo[] functionSignature() {
            return __ffi_cache;
        }

        default void invoke(JavaTypeWrapper[] parameters, JavaTypeWrapper returnType) {
            returnType.setValue(anotherClosure_call((int) parameters[0].asLong(), (double) parameters[1].asDouble()));
        }
    }
}
