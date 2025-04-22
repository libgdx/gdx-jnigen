package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

public final class ClosureObject<T extends Closure> extends Pointing {

    private final T closure;
    private final long closurePtr;

    public static <T extends Closure> ClosureObject<T> fromClosure(T object) {
        return CHandler.createClosureForObject(object);
    }

    public ClosureObject(T closure, long fnPtr, long closurePtr) {
        super(fnPtr, false);
        this.closure = closure;
        this.closurePtr = closurePtr;
    }

    @Override
    public void free() {
        if (freed)
            throw new IllegalArgumentException("Closure already freed");
        if (closurePtr == 0)
            throw new IllegalArgumentException("Closure is not java closure: " + getPointer());
        CHandler.deregisterFunctionPointer(getPointer());
        CHandler.freeClosure(closurePtr);
        freed = true;
    }

    public T getClosure() {
        return closure;
    }
}
