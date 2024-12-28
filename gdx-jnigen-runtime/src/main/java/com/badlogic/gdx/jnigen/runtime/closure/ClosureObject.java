package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

public final class ClosureObject<T extends Closure> extends Pointing {

    private final long closurePtr;

    public static <T extends Closure> ClosureObject<T> fromClosure(T object) {
        return CHandler.createClosureForObject(object);
    }

    public ClosureObject(long fnPtr, long closurePtr, boolean freeOnGC) {
        super(fnPtr, freeOnGC);
        this.closurePtr = closurePtr;
    }

    @Override
    public void free() {
        if (isFreed())
            throw new IllegalArgumentException("Closure already freed");
        if (closurePtr == 0)
            throw new IllegalArgumentException("Closure is not java closure: " + getPointer());
        CHandler.freeClosure(this);
        freed = true;
    }

    public long getClosurePtr() {
        return closurePtr;
    }
}
