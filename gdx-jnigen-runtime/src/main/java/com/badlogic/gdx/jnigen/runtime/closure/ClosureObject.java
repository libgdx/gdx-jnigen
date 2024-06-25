package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

public final class ClosureObject<T extends Closure> extends Pointing {

    private final long fnPtr;


    public static <T extends Closure> ClosureObject<T> fromClosure(T object) {
        return CHandler.createClosureForObject(object);
    }

    public ClosureObject(long fnPtr, long closurePtr, boolean freeOnGC) {
        super(closurePtr, freeOnGC);
        this.fnPtr = fnPtr;
    }

    @Override
    public void free() {
        if (isFreed())
            throw new IllegalArgumentException("Closure already freed");
        CHandler.freeClosure(this);
        freed = true;
    }

    public long getFnPtr() {
        return fnPtr;
    }
}
