package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.Struct;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClosureInfo<T extends Closure> {

    private final long cif;
    private final Method toCall;
    private final T toCallOn;

    private Class<?>[] parameters;
    private Object[] objects;
    private final WrappingPointingSupplier<? extends Pointing>[] pointingSuppliers;

    public ClosureInfo(long cif, Method toCall, T toCallOn) {
        this.cif = cif;
        this.toCall = toCall;
        this.toCallOn = toCallOn;
        toCall.setAccessible(true);
        parameters = toCall.getParameterTypes();
        pointingSuppliers = new WrappingPointingSupplier[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (Struct.class.isAssignableFrom(parameters[i])) {
                @SuppressWarnings("unchecked")
                WrappingPointingSupplier<? extends Pointing> supplier = Global.getPointingSupplier((Class<? extends Pointing>)parameters[i]);
                if (supplier == null)
                    throw new IllegalArgumentException("Class " + parameters[i].getName() + " has no registered supplier.");
                pointingSuppliers[i] = supplier;
            }
        }
        objects = new Object[parameters.length];
    }

    public Object invoke(ByteBuffer parameter)
            throws InvocationTargetException, IllegalAccessException {
        if (parameters.length == 0)
            return toCall.invoke(toCallOn);
        parameter.order(ByteOrder.nativeOrder());
        for (int i = 0; i < parameters.length; i++) {
            Class<?> param = parameters[i];
            if (param == boolean.class) {
                objects[i] = parameter.get() == 1;
                parameter.position(parameter.position() + 7);
            } else if (param == char.class) {
                objects[i] = parameter.getChar();
                parameter.position(parameter.position() + 6);
            } else if (param == byte.class) {
                objects[i] = parameter.get();
                parameter.position(parameter.position() + 7);
            } else if (param == short.class) {
                objects[i] = parameter.getShort();
                parameter.position(parameter.position() + 6);
            }else if (param == int.class) {
                objects[i] = parameter.getInt();
                parameter.position(parameter.position() + 4);
            }else if (param == long.class) {
                objects[i] = parameter.getLong();
            } else if (param == float.class) {
                objects[i] = Float.intBitsToFloat(parameter.getInt());
                parameter.position(parameter.position() + 4);
            } else if (param == double.class) {
                objects[i] = Double.longBitsToDouble(parameter.getLong());
            } else if (Struct.class.isAssignableFrom(param)) {
                objects[i] = pointingSuppliers[i].create(parameter.getLong(), true);
            }
        }
        return toCall.invoke(toCallOn, objects);
    }

    public long getCif() {
        return cif;
    }

    public Method getToCall() {
        return toCall;
    }

    public T getToCallOn() {
        return toCallOn;
    }
}
