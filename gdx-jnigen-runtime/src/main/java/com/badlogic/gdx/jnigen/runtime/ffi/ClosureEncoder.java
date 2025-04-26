package com.badlogic.gdx.jnigen.runtime.ffi;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.mem.AllocationManager;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtrManager;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ClosureEncoder {

    private final VoidPointer pointer;
    private final int bufferPtrSize;
    private final AtomicBoolean locked;
    private final long fnPtr;
    private final long cif;

    public ClosureEncoder(long fnPtr, CTypeInfo[] functionSignature) {
        int bufSize = 0;
        for (CTypeInfo cTypeInfo : functionSignature) {
            bufSize += cTypeInfo.getSize();
        }

        this.pointer = new VoidPointer(bufSize, true);
        this.fnPtr = fnPtr;
        this.cif = CHandler.getFFICifForSignature(functionSignature);
        this.bufferPtrSize = bufSize;
        this.locked = new AtomicBoolean(false);
    }

    public BufferPtr lockOrDuplicate() {
        if (bufferPtrSize == 0)
            return pointer.getBufPtr();

        if (locked.compareAndSet(false, true))
            return pointer.getBufPtr();

        return AllocationManager.wrap(pointer.getBufPtr().duplicate(), bufferPtrSize);
    }

    public void invoke(BufferPtr bufferPtr) {
        CHandler.dispatchCCall(fnPtr, cif, bufferPtr.getPointer());
    }

    public void finish(BufferPtr bufferPtr) {
        if (bufferPtrSize == 0)
            return;

        if (pointer.getBufPtr() == bufferPtr) {
            locked.set(false);
        } else {
            bufferPtr.free();
            BufferPtrManager.insertPool(bufferPtr);
        }
    }
}
