package com.badlogic.gdx.jnigen.pointer;

public final class VoidPointer extends Pointing {

    public VoidPointer(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public VoidPointer(int size) {
        this(size, true, true);
    }

    public VoidPointer(int size, boolean freeOnGC, boolean guard) {
        super(size, freeOnGC, guard);
    }

    public CSizedIntPointer recastToInt(String cType) {
        CSizedIntPointer tmp = new CSizedIntPointer(getPointer(), getsGCFreed(), cType);
        tmp.guardBytes(getSizeGuard());
        return tmp;
    }

    public FloatPointer recastToFloat() {
        FloatPointer tmp = new FloatPointer(getPointer(), getsGCFreed());
        tmp.guardBytes(getSizeGuard());
        return tmp;
    }

    public DoublePointer recastToDouble() {
        DoublePointer tmp = new DoublePointer(getPointer(), getsGCFreed());
        tmp.guardBytes(getSizeGuard());
        return tmp;
    }
}
