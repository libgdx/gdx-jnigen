package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;

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

    private Class<?>[] parameterTypes;
    private JavaTypeWrapper[] cachedWrappers;
    private JavaTypeWrapper cachedReturnWrapper;
    private final WrappingPointingSupplier<? extends Pointing>[] pointingSuppliers;
    private byte[] flags;
    private AtomicBoolean cacheLock = new AtomicBoolean(false);

    public ClosureInfo(long cif, Method method, T toCallOn) {
        this.cif = cif;
        this.toCallOn = toCallOn;
        Parameter[] parameters = method.getParameters();
        parameterTypes = new Class[parameters.length];
        pointingSuppliers = new WrappingPointingSupplier[parameters.length];
        flags = new byte[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            parameterTypes[i] = parameter.getType();
            if (Pointing.class.isAssignableFrom(parameter.getType())) {
                @SuppressWarnings("unchecked")
                WrappingPointingSupplier<?> supplier = Global.getPointingSupplier((Class<? extends Pointing>)parameter.getType());
                if (supplier == null)
                    throw new IllegalArgumentException("Class " + parameters[i].getName() + " has no registered supplier.");
                pointingSuppliers[i] = supplier;
            }
            Annotation[] annotations = parameter.getAnnotations();
            flags[i] = ParameterTypes.buildFlags(parameter.getType(), annotations);
        }
        cachedWrappers = createWrapper();
        if (method.getReturnType() != void.class)
            cachedReturnWrapper = new JavaTypeWrapper(method.getReturnType());
    }

    private JavaTypeWrapper[] createWrapper() {
        if (parameterTypes.length == 0)
            return null;
        JavaTypeWrapper[] wrappers = new JavaTypeWrapper[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            wrappers[i] = new JavaTypeWrapper(type);
        }
        return wrappers;
    }

    private JavaTypeWrapper createReturnWrapper() {
        if (cachedReturnWrapper == null)
            return null;
        return new JavaTypeWrapper(cachedReturnWrapper.getWrappingClass());
    }

    public long invoke(ByteBuffer parameter)
            throws InvocationTargetException, IllegalAccessException {

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
            for (int i = 0; i < wrappers.length; i++) {
                JavaTypeWrapper wrapper = wrappers[i];
                Class<?> param = wrapper.getWrappingClass();
                if (param == boolean.class) {
                    wrapper.setValue(parameter.get());
                    parameter.position(parameter.position() + 7);
                } else if (param == char.class) {
                    wrapper.setValue(parameter.getChar());
                    parameter.position(parameter.position() + 6);
                } else if (param == byte.class) {
                    wrapper.setValue(parameter.get());
                    parameter.position(parameter.position() + 7);
                } else if (param == short.class) {
                    wrapper.setValue(parameter.getShort());
                    parameter.position(parameter.position() + 6);
                } else if (param == int.class) {
                    wrapper.setValue(parameter.getInt());
                    parameter.position(parameter.position() + 4);
                } else if (param == long.class) {
                    wrapper.setValue(parameter.getLong());
                } else if (param == float.class) {
                    wrapper.setValue(parameter.getInt());
                    parameter.position(parameter.position() + 4);
                } else if (param == double.class) {
                    wrapper.setValue(parameter.getLong());
                } else if (Struct.class.isAssignableFrom(param)) {
                    wrapper.setValue(
                            pointingSuppliers[i].create(parameter.getLong(), (flags[i] & PASS_AS_POINTER) == 0));
                } else if (Pointing.class.isAssignableFrom(param)) {
                    wrapper.setValue(pointingSuppliers[i].create(parameter.getLong(), false));
                }
            }
        }

        toCallOn.invoke(wrappers, returnWrapper);
        long returnValue = returnWrapper == null ? 0 : returnWrapper.unwrapToLong();
        if (usedCachedWrapper)
            cacheLock.set(false);
        return returnValue;
    }
}
