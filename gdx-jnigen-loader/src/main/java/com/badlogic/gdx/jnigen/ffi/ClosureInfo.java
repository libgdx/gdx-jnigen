package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.closure.Closure;

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

    public ClosureInfo(long cif, Method toCall, T toCallOn) {
        this.cif = cif;
        this.toCall = toCall;
        this.toCallOn = toCallOn;
        toCall.setAccessible(true);
        parameters = toCall.getParameterTypes();
        objects = new Object[parameters.length];
    }

    public Object invoke(ByteBuffer parameter) throws InvocationTargetException, IllegalAccessException {
        if (parameters.length == 0)
            return toCall.invoke(toCallOn);
        parameter.order(ByteOrder.nativeOrder());
        for (int i = 0; i < parameters.length; i++) {
            Class<?> param = parameters[i];
            long value = parameter.getLong();
            if (param == boolean.class) {
                objects[i] = (value >> 7) == 1;
            } else if (param == char.class) {
                objects[i] = (char) (value >> 7);
            } else if (param == byte.class) {
                objects[i] = (byte) (value >> 7);
            } else if (param == short.class) {
                objects[i] = (short) (value >> 6);
            }else if (param == int.class) {
                objects[i] = (int) (value >> 4);
            }else if (param == long.class) {
                objects[i] = value;
            } else if (param == float.class) {
                objects[i] = Float.intBitsToFloat((int)(value >> 4));
            } else if (param == double.class) {
                objects[i] = Double.longBitsToDouble(value);
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
