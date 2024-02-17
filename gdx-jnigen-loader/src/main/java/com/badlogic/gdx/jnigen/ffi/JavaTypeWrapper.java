package com.badlogic.gdx.jnigen.ffi;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CEnum;
import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.pointer.Pointing;

public final class JavaTypeWrapper {

    private long wrappingType;
    private final CTypeInfo cTypeInfo;

    public JavaTypeWrapper(CTypeInfo cTypeInfo) {
        this.cTypeInfo = cTypeInfo;
    }

    public int getSize() {
        if (cTypeInfo.isVoid())
            return 0;
        if (cTypeInfo.isStruct())
            return CHandler.POINTER_SIZE;
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
        return new JavaTypeWrapper(cTypeInfo);
    }

    public long unwrapToLong() {
        return wrappingType;
    }

    public long asLong() {
        return wrappingType;
    }

    public float asFloat() {
        return Float.intBitsToFloat((int)wrappingType);
    }

    public double asDouble() {
        return Double.longBitsToDouble(wrappingType);
    }

    public void assertBounds() {
        cTypeInfo.assertBounds(unwrapToLong());
    }
}
