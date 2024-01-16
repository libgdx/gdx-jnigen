package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.WrappingPointingSupplier;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.badlogic.gdx.jnigen.ffi.ParameterTypes.*;

public class ClosureInfo<T extends Closure> {

    private final long cif;
    private final T toCallOn;

    private Class<?>[] parameterTypes;
    private Object[] objects;
    private final WrappingPointingSupplier<? extends Pointing>[] pointingSuppliers;
    private byte flags = 0;

    public ClosureInfo(long cif, Parameter[] parameters, T toCallOn) {
        this.cif = cif;
        this.toCallOn = toCallOn;
        parameterTypes = new Class[parameters.length];
        pointingSuppliers = new WrappingPointingSupplier[parameters.length];
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
            flags = ParameterTypes.buildFlags(parameter.getType(), annotations);
        }
        objects = new Object[parameters.length];
    }

    public Object invoke(ByteBuffer parameter)
            throws InvocationTargetException, IllegalAccessException {
        if (parameterTypes.length == 0)
            return toCallOn.invoke(null);

        parameter.order(ByteOrder.nativeOrder());
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> param = parameterTypes[i];
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
                objects[i] = pointingSuppliers[i].create(parameter.getLong(), (flags & PASS_AS_POINTER) == 0);
            } else if (Pointing.class.isAssignableFrom(param)) {
                objects[i] = pointingSuppliers[i].create(parameter.getLong(), false);
            }
        }
        return toCallOn.invoke(objects);
    }
}
