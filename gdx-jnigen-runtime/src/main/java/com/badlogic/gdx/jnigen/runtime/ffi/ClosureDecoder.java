package com.badlogic.gdx.jnigen.runtime.ffi;

import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ClosureDecoder<T extends Closure> {

    private final T toCallOn;

    private final JavaTypeWrapper[] cachedWrappers;
    private final JavaTypeWrapper cachedReturnWrapper;
    private final AtomicBoolean cacheLock = new AtomicBoolean(false);

    public ClosureDecoder(T toCallOn) {
        this.toCallOn = toCallOn;
        CTypeInfo[] functionSignature = toCallOn.functionSignature();
        int parameterLength = functionSignature.length - 1;

        cachedWrappers = new JavaTypeWrapper[parameterLength];
        for (int i = 1; i < functionSignature.length; i++) {
            cachedWrappers[i - 1] = new JavaTypeWrapper(functionSignature[i]);
        }
        cachedReturnWrapper = new JavaTypeWrapper(functionSignature[0]);
    }

    private JavaTypeWrapper[] createWrapper() {
        if (cachedWrappers.length == 0)
            return cachedWrappers;
        JavaTypeWrapper[] wrappers = new JavaTypeWrapper[cachedWrappers.length];
        for (int i = 0; i < cachedWrappers.length; i++) {
            wrappers[i] = cachedWrappers[i].newJavaTypeWrapper();
        }
        return wrappers;
    }

    private JavaTypeWrapper createReturnWrapper() {
        if (cachedReturnWrapper.getSize() == 0)
            return cachedReturnWrapper;
        return cachedReturnWrapper.newJavaTypeWrapper();
    }

    public long invoke(ByteBuffer parameter) {
        JavaTypeWrapper[] wrappers = cachedWrappers;
        JavaTypeWrapper returnWrapper = cachedReturnWrapper;
        boolean usedCachedWrapper = true;
        if (!cacheLock.compareAndSet(false, true)) {
            wrappers = createWrapper();
            returnWrapper = createReturnWrapper();
            usedCachedWrapper = false;
        }

        if (parameter != null) {
            parameter.order(ByteOrder.nativeOrder());
            for (JavaTypeWrapper wrapper : wrappers) {
                int cSize = wrapper.getSize();
                if (cSize == 1) {
                    wrapper.setValue(parameter.get());
                } else if (cSize == 2) {
                    wrapper.setValue(parameter.getShort());
                } else if (cSize == 4) {
                    wrapper.setValue(parameter.getInt());
                } else if (cSize == 8) {
                    wrapper.setValue(parameter.getLong());
                }
            }
        }

        toCallOn.invoke(wrappers, returnWrapper);
        long returnValue = 0;
        if (returnWrapper.getSize() > 0) {
            returnWrapper.assertBounds();
            returnValue = returnWrapper.unwrapToLong();
        }

        if (usedCachedWrapper)
            cacheLock.set(false);
        return returnValue;
    }
}
