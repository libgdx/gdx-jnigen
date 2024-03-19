package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;

public abstract class StackElement extends Pointing {

    protected StackElement(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    protected StackElement(int size) {
        super(size, true, true);
    }

    protected void setValue(long value, int index) {
        CHandler.setStackElementField(getPointer(), getFFIType(), index, value, hasElementOffsets());
    }

    protected void setValue(float value, int index) {
        setValue(Float.floatToIntBits(value), index);
    }

    protected void setValue(double value, int index) {
        setValue(Double.doubleToLongBits(value), index);
    }

    protected void setValue(boolean value, int index) {
        setValue(value ? 1 : 0, index);
    }


    protected long getValue(int index) {
        return CHandler.getStackElementField(getPointer(), getFFIType(), index, hasElementOffsets());
    }

    protected float getValueFloat(int index) {
        return Float.intBitsToFloat((int)getValue(index));
    }
    protected double getValueDouble(int index) {
        return Double.longBitsToDouble(getValue(index));
    }

    public abstract StackElementPointer<? extends StackElement> asPointer();

    // TODO: All these methods could be removed with the generator. Performance tests need to show, whether virtual dispatch has any significant penalty
    //  However, I doubt it.
    public abstract long getSize();

    public abstract long getFFIType();

    public abstract boolean hasElementOffsets();
}
