package com.badlogic.gdx.jnigen.pointer;

public final class VoidPointer extends Pointing {

    public VoidPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public VoidPointer(long size) {
        super(size);
    }

    public CSizedIntPointer recastToInt(String cType) {
        return new CSizedIntPointer(getPointer(), getsGCFreed(), cType);
    }

    public FloatPointer recastToFloat() {
        return new FloatPointer(getPointer(), getsGCFreed());
    }

    public DoublePointer recastToDouble() {
        return new DoublePointer(getPointer(), getsGCFreed());
    }
}
