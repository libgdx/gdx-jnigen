package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.c.CEnum;
import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.pointer.Pointing;
import com.badlogic.gdx.jnigen.util.Utils;

public final class JavaTypeWrapper {

    private final Class<?> wrappingClass;
    private long wrappingType;
    private final CTypeInfo cTypeInfo;

    public JavaTypeWrapper(Class<?> wrappingClass, CTypeInfo cTypeInfo) {
        this.wrappingClass = wrappingClass;
        this.cTypeInfo = cTypeInfo;
        if (wrappingClass == char.class && cTypeInfo.isSigned())
            throw new IllegalArgumentException("A char can't be signed");
    }

    public int getSize() {
        return cTypeInfo.getSize();
    }

    public void setValue(boolean b) {
        wrappingType = b ? 1 : 0;
    }

    public void setValue(byte value) {
        if (cTypeInfo.isSigned())
            this.wrappingType = value;
        else
            this.wrappingType = value & 0xFF;
    }

    public void setValue(char value) {
        this.wrappingType = value;
    }

    public void setValue(short value) {
        if (cTypeInfo.isSigned())
            this.wrappingType = value;
        else
            this.wrappingType = value & 0xFFFF;
    }

    public void setValue(int value) {
        if (cTypeInfo.isSigned())
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

    public void setValue(CEnum cEnum) {
        this.wrappingType = cEnum.getIndex();
    }

    public void setValue(Pointing wrappingPointing) {
        wrappingType = wrappingPointing.getPointer();
    }


    public JavaTypeWrapper newJavaTypeWrapper() {
        return new JavaTypeWrapper(wrappingClass, cTypeInfo);
    }

    public long unwrapToLong() {
        return wrappingType;
    }

    public long asLong() {
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

    public void assertBounds() {
        if (wrappingClass != float.class && wrappingClass != double.class)
            cTypeInfo.assertBounds(unwrapToLong());
    }
}
