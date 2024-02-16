package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CEnum;
import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.badlogic.gdx.jnigen.ffi.ParameterTypes.*;

public final class ClosureInfo<T extends Closure> {

    private final long cif;
    private final T toCallOn;

    private final JavaTypeWrapper[] cachedWrappers;
    private final JavaTypeWrapper cachedReturnWrapper;
    private final AtomicBoolean cacheLock = new AtomicBoolean(false);

    public ClosureInfo(long cif, T toCallOn) {
        this.cif = cif;
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
        // TODO: 16.02.24 Check on void return
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
                    parameter.position(parameter.position() + 7);
                } else if (cSize == 2) {
                    wrapper.setValue(parameter.getShort());
                    parameter.position(parameter.position() + 6);
                } else if (cSize == 4) {
                    wrapper.setValue(parameter.getInt());
                    parameter.position(parameter.position() + 4);
                } else if (cSize == 8) {
                    wrapper.setValue(parameter.getLong());
                }
            }
        }

        toCallOn.invoke(wrappers, returnWrapper);
        long returnValue = 0;
        // TODO: 16.02.24 Check for void
        returnWrapper.assertBounds();
        returnValue = returnWrapper.unwrapToLong();

        if (usedCachedWrapper)
            cacheLock.set(false);
        return returnValue;
    }
}
