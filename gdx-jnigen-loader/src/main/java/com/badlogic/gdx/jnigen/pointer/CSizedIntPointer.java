package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CTypeInfo;

public class CSizedIntPointer extends Pointing {

    private CTypeInfo cTypeInfo;

    public CSizedIntPointer(long pointer, boolean freeOnGC, String cTypeInfo) {
        super(pointer, freeOnGC);
        this.cTypeInfo = CHandler.getCTypeInfo(cTypeInfo);
    }

    public CSizedIntPointer(String cType, long size) {
        this(CHandler.getCTypeInfo(cType), size);
    }

    private CSizedIntPointer(CTypeInfo info, long size) {
        super(info.getSize() * size);
        this.cTypeInfo = info;
    }

    public int getInt(int index) {
        if (cTypeInfo.getSize() > 4 || !cTypeInfo.isSigned() && cTypeInfo.getSize() == 4)
            throw new IllegalArgumentException("Java int exceeds size of backed type");
        return (int)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), index * cTypeInfo.getSize());
    }

    public void setInt(int value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), index * cTypeInfo.getSize(), value);
    }


    public CSizedIntPointer recast(String newCType) {
        return new CSizedIntPointer(getPointer(), getsGCFreed(), newCType);
    }
}
