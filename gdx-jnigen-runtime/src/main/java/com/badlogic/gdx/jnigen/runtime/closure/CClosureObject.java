package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.ffi.ClosureEncoder;

public class CClosureObject<T extends Closure> extends ClosureObject<T> {

    private final ClosureEncoder encoder;

    public CClosureObject(T closure, long fnPtr, ClosureEncoder encoder) {
        super(closure, fnPtr);
        this.encoder = encoder;
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
