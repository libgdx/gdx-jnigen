package com.badlogic.gdx.jnigen.runtime.ffi;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CEnum;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ClosureEncoder {

    private final AtomicBoolean locked;
    private final long fnPtr;
    private final long cif;
    private final JavaTypeWrapper[] cachedWrappers;
    private final ByteBuffer cachedBuffer;

    public ClosureEncoder(long fnPtr, CTypeInfo[] functionSignature) {
        this.fnPtr = fnPtr;
        this.cif = CHandler.getFFICifForSignature(functionSignature);

        int parameterLength = functionSignature.length - 1;
        
        JavaTypeWrapper[] wrappers = new JavaTypeWrapper[parameterLength];
        for (int i = 1; i < functionSignature.length; i++) {
            wrappers[i - 1] = new JavaTypeWrapper(functionSignature[i]);
        }
        
        cachedWrappers = wrappers;
        cachedBuffer = ByteBuffer.allocateDirect(parameterLength * 8);
        cachedBuffer.order(ByteOrder.nativeOrder());

        locked = new AtomicBoolean(false);
    }

    private ClosureEncoder(ClosureEncoder closureEncoder) {
        this.fnPtr = closureEncoder.fnPtr;
        this.cif = closureEncoder.cif;
        this.cachedWrappers = new JavaTypeWrapper[closureEncoder.cachedWrappers.length];
        System.arraycopy(closureEncoder.cachedWrappers, 0, this.cachedWrappers, 0,
                closureEncoder.cachedWrappers.length);
        cachedBuffer = ByteBuffer.allocateDirect(closureEncoder.cachedBuffer.limit());
        cachedBuffer.order(ByteOrder.nativeOrder());

        locked = new AtomicBoolean(true);
    }

    public ClosureEncoder lockOrDuplicate() {
        if (locked.compareAndSet(false, true))
            return this;

        return new ClosureEncoder(this);
    }

    public void setValue(int index, boolean b) {
        cachedWrappers[index].setValue(b);
    }

    public void setValue(int index, byte value) {
        cachedWrappers[index].setValue(value);
    }

    public void setValue(int index, char value) {
        cachedWrappers[index].setValue(value);
    }

    public void setValue(int index, short value) {
        cachedWrappers[index].setValue(value);
    }

    public void setValue(int index, int value) {
        cachedWrappers[index].setValue(value);
    }

    public void setValue(int index, long value) {
        cachedWrappers[index].setValue(value);
    }

    public void setValue(int index, double value) {
        cachedWrappers[index].setValue(value);
    }

    public void setValue(int index, float value) {
        cachedWrappers[index].setValue(value);
    }

    public void setValue(int index, CEnum cEnum) {
        cachedWrappers[index].setValue(cEnum);
    }

    public void setValue(int index, Pointing wrappingPointing) {
        cachedWrappers[index].setValue(wrappingPointing);
    }

    public long invoke() {
        cachedBuffer.position(0);
        for (JavaTypeWrapper wrapper : cachedWrappers) {
            wrapper.assertBounds();
            cachedBuffer.putLong(wrapper.unwrapToLong());
        }

        long ret = CHandler.dispatchCCall(fnPtr, cif, cachedBuffer);

        locked.set(false);

        return ret;
    }
}
