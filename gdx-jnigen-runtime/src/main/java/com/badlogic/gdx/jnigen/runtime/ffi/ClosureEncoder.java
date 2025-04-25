package com.badlogic.gdx.jnigen.runtime.ffi;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ClosureEncoder extends VoidPointer {

    private final int bufferPtrSize;
    private final AtomicBoolean locked;
    private final long fnPtr;
    private final long cif;

    private static int calculateBufferPtrSize(CTypeInfo[] functionSignature) {
        int bufSize = 0;
        for (CTypeInfo cTypeInfo : functionSignature) {
            bufSize += cTypeInfo.getSize();
        }

        return bufSize;
    }

    public ClosureEncoder(long fnPtr, CTypeInfo[] functionSignature) {
        super(calculateBufferPtrSize(functionSignature), true);
        this.fnPtr = fnPtr;
        this.cif = CHandler.getFFICifForSignature(functionSignature);
        this.bufferPtrSize = calculateBufferPtrSize(functionSignature);
        this.locked = new AtomicBoolean(false);
    }

    private ClosureEncoder(ClosureEncoder closureEncoder) {
        super(closureEncoder.bufferPtrSize, true);
        this.fnPtr = closureEncoder.fnPtr;
        this.cif = closureEncoder.cif;
        this.bufferPtrSize = closureEncoder.bufferPtrSize;
        this.locked = new AtomicBoolean(true);
    }

    public ClosureEncoder lockOrDuplicate() {
        if (locked.compareAndSet(false, true))
            return this;

        return new ClosureEncoder(this);
    }

    public BufferPtr getBufPtr() {
        return super.getBufPtr();
    }

    public void invoke() {
        CHandler.dispatchCCall(fnPtr, cif, getPointer());
        locked.set(false);
    }
}
