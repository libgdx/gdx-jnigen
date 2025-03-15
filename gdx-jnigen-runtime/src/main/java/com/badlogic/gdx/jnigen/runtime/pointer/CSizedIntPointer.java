package com.badlogic.gdx.jnigen.runtime.pointer;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.c.CTypeInfo;

public final class CSizedIntPointer extends Pointing {

    private final CTypeInfo cTypeInfo;

    public CSizedIntPointer(long pointer, boolean freeOnGC, String cTypeInfo) {
        super(pointer, freeOnGC);
        this.cTypeInfo = CHandler.getCTypeInfo(cTypeInfo);
    }

    public CSizedIntPointer(String cType) {
        this(cType, 1);
    }

    public CSizedIntPointer(String cType, int size) {
        this(cType, size, true, true);
    }

    public CSizedIntPointer(String cType, int size, boolean freeOnGC, boolean guard) {
        this(CHandler.getCTypeInfo(cType), size, freeOnGC, guard);
    }

    private CSizedIntPointer(CTypeInfo info, int size, boolean freeOnGC, boolean guard) {
        super(info.getSize() * size, freeOnGC, guard);
        this.cTypeInfo = info;
    }

    public static CSizedIntPointer fromString(String string, boolean freeOnGC) {
        CSizedIntPointer pointer = new CSizedIntPointer("char", string.length() + 1, freeOnGC, true);
        pointer.setString(string);
        return pointer;
    }

    public static PointerDereferenceSupplier<CSizedIntPointer> pointerPointer(final String cTypeInfo) {
        return new PointerDereferenceSupplier<CSizedIntPointer>() {

            @Override
            public CSizedIntPointer create(long pointer, boolean freeOnGC) {
                return new CSizedIntPointer(pointer, freeOnGC, cTypeInfo);
            }
        };
    }

    public CSizedIntPointer guardCount(long count) {
        super.guardBytes(count * cTypeInfo.getSize());
        return this;
    }

    private int calculateOffset(int index) {
        int offset = index * cTypeInfo.getSize();
        assertBounds(offset);
        return offset;
    }

    public boolean getBoolean() {
        return getBoolean(0);
    }

    public boolean getBoolean(int index) {
        cTypeInfo.assertCanHoldByte(); // Assuming boolean is represented as a byte
        return CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index)) != 0;
    }

    public void setBoolean(boolean value) {
        setBoolean(value, 0);
    }

    public void setBoolean(boolean value, int index) {
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value ? 1 : 0);
    }

    public byte getByte() {
        return getByte(0);
    }

    public byte getByte(int index) {
        cTypeInfo.assertCanHoldByte();
        return (byte)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setByte(byte value) {
        setByte(value, 0);
    }

    public void setByte(byte value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public short getShort() {
        return getShort(0);
    }

    public short getShort(int index) {
        cTypeInfo.assertCanHoldShort();
        return (short)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setShort(short value) {
        setShort(value, 0);
    }

    public void setShort(short value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public char getChar() {
        return getChar(0);
    }

    public char getChar(int index) {
        cTypeInfo.assertCanHoldChar();
        return (char)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setChar(char value) {
        setChar(value, 0);
    }

    public void setChar(char value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public int getInt() {
        return getInt(0);
    }

    public int getInt(int index) {
        cTypeInfo.assertCanHoldInt();
        return (int)CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setInt(int value) {
        setInt(value, 0);
    }

    public void setInt(int value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public long getLong() {
        return getLong(0);
    }

    public long getLong(int index) {
        return CHandler.getPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index));
    }

    public void setLong(long value) {
        setLong(value, 0);
    }

    public void setLong(long value, int index) {
        cTypeInfo.assertBounds(value);
        CHandler.setPointerPart(getPointer(), cTypeInfo.getSize(), calculateOffset(index), value);
    }

    public void assertHasCTypeBacking(String name) {
        cTypeInfo.assertConformsTo(name);
    }

    public CSizedIntPointer recast(String newCType) {
        CSizedIntPointer tmp = new CSizedIntPointer(getPointer(), false, newCType);
        tmp.guardBytes(getSizeGuard());
        tmp.setParent(this);
        return tmp;
    }

    public void setString(String string) {
        // TODO: 21.06.24 is that sane?
        assertBounds(string.length());
        CHandler.setPointerAsString(getPointer(), string);
    }

    public String getString() {
        if (isNull())
            return null;
        return CHandler.getPointerAsString(getPointer());
    }
}
