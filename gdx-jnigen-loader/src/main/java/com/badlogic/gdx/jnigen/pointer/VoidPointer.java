package com.badlogic.gdx.jnigen.pointer;

public final class VoidPointer extends Pointing {

    public VoidPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public VoidPointer(long size) {
        super(size);
    }

    public CSizedIntPointer recastToInt(String cType) {
        CSizedIntPointer tmp = new CSizedIntPointer(getPointer(), getsGCFreed(), cType);
        tmp.guard(getSizeGuard());
        return tmp;
    }

    public FloatPointer recastToFloat() {
        FloatPointer tmp = new FloatPointer(getPointer(), getsGCFreed());
        tmp.guard(getSizeGuard());
        return tmp;
    }

    public DoublePointer recastToDouble() {
        DoublePointer tmp = new DoublePointer(getPointer(), getsGCFreed());
        tmp.guard(getSizeGuard());
        return tmp;
    }
}
