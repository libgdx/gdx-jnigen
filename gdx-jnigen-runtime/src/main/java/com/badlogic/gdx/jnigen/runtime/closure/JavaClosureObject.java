package com.badlogic.gdx.jnigen.runtime.closure;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.ffi.ClosureDecoder;

public class JavaClosureObject<T extends Closure> extends ClosureObject<T> {

    private final long closurePtr;
    private final ClosureDecoder<T> decoder;
    private boolean freed;

    public JavaClosureObject(T closure, long fnPtr, long closurePtr, ClosureDecoder<T> decoder) {
        super(closure, fnPtr);
        this.closurePtr = closurePtr;
        this.decoder = decoder;
    }

    @Override
    public void free() {
        if (freed)
            throw new IllegalArgumentException("Closure already freed");
        CHandler.deregisterFunctionPointer(getPointer());
        CHandler.freeClosure(closurePtr);
        freed = true;
    }

    @Override
    public void setPoolManager(PointingPoolManager manager) {
        decoder.setPoolManager(manager);
    }

    public long getClosurePtr() {
        return closurePtr;
    }
}
