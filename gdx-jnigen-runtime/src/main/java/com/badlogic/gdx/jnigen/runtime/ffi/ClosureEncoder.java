package com.badlogic.gdx.jnigen.runtime.ffi;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ClosureEncoder {

    private final VoidPointer bufferPtr;
    private final int bufferPtrSize;
    private final AtomicBoolean locked;
    private final long fnPtr;
    private final long cif;

    public ClosureEncoder(long fnPtr, CTypeInfo[] functionSignature) {
        this.fnPtr = fnPtr;
        this.cif = CHandler.getFFICifForSignature(functionSignature);

        int parameterSize = 0;
        for (int i = 1; i < functionSignature.length; i++) {
            parameterSize += functionSignature[i].getSize();
        }

        this.bufferPtrSize = Math.max(parameterSize, functionSignature[0].getSize());
        this.bufferPtr = new VoidPointer(bufferPtrSize, true);
        this.locked = new AtomicBoolean(false);
    }

    private ClosureEncoder(ClosureEncoder closureEncoder) {
        this.fnPtr = closureEncoder.fnPtr;
        this.cif = closureEncoder.cif;
        this.bufferPtrSize = closureEncoder.bufferPtrSize;
        this.bufferPtr = new VoidPointer(bufferPtrSize, true);
        this.locked = new AtomicBoolean(true);
    }

    public ClosureEncoder lockOrDuplicate() {
        if (locked.compareAndSet(false, true))
            return this;

        return new ClosureEncoder(this);
    }

    public BufferPtr getBufPtr() {
        return bufferPtr.getBufPtr();
    }

    public void invoke() {
        CHandler.dispatchCCall(fnPtr, cif, bufferPtr.getPointer());
        locked.set(false);
    }
}
