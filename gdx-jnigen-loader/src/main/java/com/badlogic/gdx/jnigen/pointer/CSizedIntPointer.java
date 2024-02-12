package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CTypeInfo;

public final class CSizedIntPointer extends Pointing {

    private final CTypeInfo cTypeInfo;

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
    
    private int calculateOffset(int index) {
        int offset = index * cTypeInfo.getSize();
        assertBounds(offset);
        return offset;
    }

    public boolean getBoolean(int index) {
        cTypeInfo.assertCanHoldByte(); // Assuming boolean is represented as a byte
        return CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index)) != 0;
    }

    public void setBoolean(boolean value, int index) {
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value ? 1 : 0);
    }

    public byte getByte(int index) {
        cTypeInfo.assertCanHoldByte();
        return (byte)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setByte(byte value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public short getShort(int index) {
        cTypeInfo.assertCanHoldShort();
        return (short)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setShort(short value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public char getChar(int index) {
        cTypeInfo.assertCanHoldChar();
        return (char)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setChar(char value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public int getInt(int index) {
        cTypeInfo.assertCanHoldInt();
        return (int)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setInt(int value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public long getLong(int index) {
        return CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setLong(long value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public CSizedIntPointer recast(String newCType) {
        CSizedIntPointer tmp = new CSizedIntPointer(getPointer(), getsGCFreed(), newCType);
        tmp.guard(getSizeGuard());
        return tmp;
    }
}
