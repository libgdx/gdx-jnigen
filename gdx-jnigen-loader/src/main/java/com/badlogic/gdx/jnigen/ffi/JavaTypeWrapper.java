package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.pointer.Pointing;

public final class JavaTypeWrapper {

    private final Class<?> wrappingClass;
    private long wrappingType;
    private Pointing wrappingPointing;

    public JavaTypeWrapper(Class<?> wrappingClass) {
        this.wrappingClass = wrappingClass;
    }

    public void setValue(long value) {
        this.wrappingType = value;
    }

    public void setValue(double value) {
        this.wrappingType = Double.doubleToLongBits(value);
    }

    public void setValue(float value) {
        this.wrappingType = Float.floatToIntBits(value);
    }

    public void setValue(Pointing wrappingPointing) {
        this.wrappingPointing = wrappingPointing;
    }

    public Class<?> getWrappingClass() {
        return wrappingClass;
    }

    public long unwrapToLong() {
        if (wrappingClass.isPrimitive())
            return wrappingType;
        return wrappingPointing.getPointer();
    }

    public boolean asBoolean() {
        if (wrappingClass != boolean.class)
            throw new IllegalArgumentException();
        return wrappingType != 0;
    }

    public byte asByte() {
        if (wrappingClass != byte.class)
            throw new IllegalArgumentException();
        return (byte)wrappingType;
    }

    public char asChar() {
        if (wrappingClass != char.class)
            throw new IllegalArgumentException();
        return (char)wrappingType;
    }

    public short asShort() {
        if (wrappingClass != short.class)
            throw new IllegalArgumentException();
        return (short)wrappingType;
    }

    public int asInt() {
        if (wrappingClass != int.class)
            throw new IllegalArgumentException();
        return (int)wrappingType;
    }

    public long asLong() {
        if (wrappingClass != long.class)
            throw new IllegalArgumentException();
        return wrappingType;
    }

    public float asFloat() {
        if (wrappingClass != float.class)
            throw new IllegalArgumentException();
        return Float.intBitsToFloat((int)wrappingType);
    }

    public double asDouble() {
        if (wrappingClass != double.class)
            throw new IllegalArgumentException();
        return Double.longBitsToDouble(wrappingType);
    }

    public Pointing asPointing() {
        if (!Pointing.class.isAssignableFrom(wrappingClass))
            throw new IllegalArgumentException();
        return wrappingPointing;
    }
}
