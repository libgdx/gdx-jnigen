package com.badlogic.gdx.jnigen.runtime.closure;

public class CClosureObject<T extends Closure> extends ClosureObject<T> {

    public CClosureObject(T closure, long fnPtr) {
        super(closure, fnPtr);
    }

    @Override
    public void free() {
        throw new IllegalArgumentException("Can't free C closure");
    }

    @Override
    public void setPoolManager(PointingPoolManager manager) {
        throw new IllegalArgumentException("Can't set pool manager (yet)");
    }
}
