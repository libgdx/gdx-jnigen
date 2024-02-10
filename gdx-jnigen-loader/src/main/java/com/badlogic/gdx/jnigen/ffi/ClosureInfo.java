package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CEnum;
import com.badlogic.gdx.jnigen.c.Signed;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.Utils;
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

    private final Class<?>[] parameterTypes;
    private final JavaTypeWrapper[] cachedWrappers;
    private final JavaTypeWrapper cachedReturnWrapper;
    private final WrappingPointingSupplier<? extends Pointing>[] pointingSuppliers;
    private final int[] realSize;
    private final byte[] flags;
    private final AtomicBoolean cacheLock = new AtomicBoolean(false);

    public ClosureInfo(long cif, Method method, T toCallOn) {
        this.cif = cif;
        this.toCallOn = toCallOn;
        Parameter[] parameters = method.getParameters();
        parameterTypes = new Class[parameters.length];
        pointingSuppliers = new WrappingPointingSupplier[parameters.length];
        flags = new byte[parameters.length];
        realSize = new int[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            parameterTypes[i] = parameter.getType();

            if (Pointing.class.isAssignableFrom(parameter.getType())) {
                @SuppressWarnings("unchecked")
                WrappingPointingSupplier<?> supplier = CHandler.getPointingSupplier((Class<? extends Pointing>)parameter.getType());
                if (supplier == null)
                    throw new IllegalArgumentException("Class " + parameters[i].getName() + " has no registered supplier.");
                pointingSuppliers[i] = supplier;
                realSize[i] = CHandler.POINTER_SIZE;
            } else if (CEnum.class.isAssignableFrom(parameter.getType())) {
                realSize[i] = CHandler.getCTypeSize("int"); // TODO Converting to CType annotation on parameter?
            } else {
                // If we are primitive
                realSize[i] = Utils.getSizeForAnnotatedElement(parameter);
            }
            Annotation[] annotations = parameter.getAnnotations();
            flags[i] = ParameterTypes.buildFlags(parameter.getType(), annotations);
        }
        cachedWrappers = createWrapper();
        if (method.getReturnType() != void.class) {
            int size = CHandler.POINTER_SIZE;
            if (method.getReturnType().isPrimitive())
                size = Utils.getSizeForAnnotatedElement(method);
            cachedReturnWrapper = new JavaTypeWrapper(method.getReturnType(), size, method.isAnnotationPresent(
                    Signed.class));
        } else {
            cachedReturnWrapper = null;
        }
    }

    private JavaTypeWrapper[] createWrapper() {
        if (parameterTypes.length == 0)
            return null;
        JavaTypeWrapper[] wrappers = new JavaTypeWrapper[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            wrappers[i] = new JavaTypeWrapper(type, realSize[i], (flags[i] & SIGNED) != 0);
        }
        return wrappers;
    }

    private JavaTypeWrapper createReturnWrapper() {
        if (cachedReturnWrapper == null)
            return null;
        return cachedReturnWrapper.newJavaTypeWrapper();
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
                int cSize = realSize[i];
                WrappingPointingSupplier<?> pointingSupplier = pointingSuppliers[i];
                if (pointingSupplier != null) {
                    wrapper.setValue(
                            pointingSuppliers[i].create(parameter.getLong(), (flags[i] & PASS_AS_POINTER) == 0));
                } else if (cSize == 1) {
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
        if (returnWrapper != null) {
            returnWrapper.assertBounds(); // TODO: Make opt in?
            returnValue = returnWrapper.unwrapToLong();
        }
        if (usedCachedWrapper)
            cacheLock.set(false);
        return returnValue;
    }
}
