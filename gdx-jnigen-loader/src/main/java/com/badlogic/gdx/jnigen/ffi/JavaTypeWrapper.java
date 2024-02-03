package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.Utils;

public final class JavaTypeWrapper {

    private final Class<?> wrappingClass;
    private long wrappingType;
    private Pointing wrappingPointing;

    private int size;
    private boolean signed;

    public JavaTypeWrapper(Class<?> wrappingClass, int size, boolean signed) {
        this.wrappingClass = wrappingClass;
        this.size = size;
        this.signed = signed;
        if (wrappingClass == char.class && signed)
            throw new IllegalArgumentException("A char can't be signed");
    }

    public void setValue(boolean b) {
        wrappingType = b ? 1 : 0;
    }

    public void setValue(byte value) {
        if (signed)
            this.wrappingType = value;
        else
            this.wrappingType = value & 0xFF;
    }

    public void setValue(char value) {
        this.wrappingType = value;
    }

    public void setValue(short value) {
        if (signed)
            this.wrappingType = value;
        else
            this.wrappingType = value & 0xFFFF;
    }

    public void setValue(int value) {
        if (signed)
            this.wrappingType = value;
        else
            this.wrappingType = value & 0xFFFFFFFFL;
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

    public JavaTypeWrapper newJavaTypeWrapper() {
        return new JavaTypeWrapper(wrappingClass, size, signed);
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

    public void assertBounds() {
        if (!Utils.checkBoundsForNumber(unwrapToLong(), size, signed))
            throw new IllegalArgumentException("Number " + unwrapToLong() + " is out of bounds for " + size + " and sign: " + signed);
    }

    public int getSize() {
        return size;
    }

    public boolean isSigned() {
        return signed;
    }
}
