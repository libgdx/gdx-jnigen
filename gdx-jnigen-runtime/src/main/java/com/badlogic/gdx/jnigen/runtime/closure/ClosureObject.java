package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

public abstract class ClosureObject<T extends Closure> extends Pointing {

    private final T closure;

    public static <T extends Closure> ClosureObject<T> fromClosure(T object) {
        return CHandler.createClosureForObject(object);
    }

    public ClosureObject(T closure, long fnPtr) {
        super(fnPtr, false);
        this.closure = closure;
    }

    @Override
    public abstract void free();
    public abstract void setPoolManager(PointingPoolManager manager);

    public T getClosure() {
        return closure;
    }
}
