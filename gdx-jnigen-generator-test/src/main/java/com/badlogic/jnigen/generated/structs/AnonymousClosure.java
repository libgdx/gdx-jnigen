package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;
import com.badlogic.jnigen.generated.TestData_Internal.AnonymousClosure_Internal.someClosure_Internal;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.jnigen.generated.TestData_Internal.AnonymousClosure_Internal.anotherClosure_Internal;

public final class AnonymousClosure extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(13).getFfiType();
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
        return new AnonymousClosure.AnonymousClosurePointer(getPointer(), false, this);
    }

    /**
     * Comment on internal callback
     */
    public ClosureObject<someClosure> someClosure() {
        return CHandler.getClosureObject(getValue(0), someClosure_Internal::someClosure_downcall);
    }

    /**
     * Comment on internal callback
     */
    public void someClosure(ClosureObject<someClosure> someClosure) {
        setValue(someClosure.getPointer(), 0);
    }

    public ClosureObject<anotherClosure> anotherClosure() {
        return CHandler.getClosureObject(getValue(1), anotherClosure_Internal::anotherClosure_downcall);
    }

    public void anotherClosure(ClosureObject<anotherClosure> anotherClosure) {
        setValue(anotherClosure.getPointer(), 1);
    }

    public static final class AnonymousClosurePointer extends StackElementPointer<AnonymousClosure> {

        public AnonymousClosurePointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public AnonymousClosurePointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
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

    public interface someClosure extends Closure, someClosure_Internal {

        /**
         * Comment on internal callback
         */
        int someClosure_call(SIntPointer t, double p);
    }

    public interface anotherClosure extends Closure, anotherClosure_Internal {

        float anotherClosure_call(int t, double p);
    }
}
