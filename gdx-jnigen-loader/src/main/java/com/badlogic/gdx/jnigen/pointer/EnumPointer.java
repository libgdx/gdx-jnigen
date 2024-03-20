package com.badlogic.gdx.jnigen.pointer;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CEnum;

public class EnumPointer<T extends CEnum> extends Pointing {

    private static final int __int_size = CHandler.getCTypeInfo("int").getSize();
    private final IntToCEnumFunction<T> function;

    public EnumPointer(long pointer, boolean freeOnGC, IntToCEnumFunction<T> function) {
        super(pointer, freeOnGC);
        this.function = function;
    }

    public EnumPointer(IntToCEnumFunction<T> function) {
        this(1, function);
    }

    public EnumPointer(int size, IntToCEnumFunction<T> function) {
        this(size, true, true, function);
    }

    public EnumPointer(int size, boolean freeOnGC, boolean guard, IntToCEnumFunction<T> function) {
        super(__int_size * size, freeOnGC, guard);
        this.function = function;
    }

    public static <E extends CEnum> PointerDereferenceSupplier<EnumPointer<E>> getPointerPointerSupplier(IntToCEnumFunction<E> function) {
        return (pointer, freeOnGC) -> new EnumPointer<>(pointer, freeOnGC, function);
    }

    public EnumPointer<T> guardCount(long count) {
        super.guardBytes(count * __int_size);
        return this;
    }

    public T getEnumValue() {
        return getEnumValue(0);
    }

    public T getEnumValue(int index) {
        int offset = index * __int_size;
        assertBounds(offset);
        return function.apply((int)CHandler.getPointerPart(getPointer(), __int_size, offset));
    }

    public void setEnumValue(T value) {
        setEnumValue(value, 0);
    }

    public void setEnumValue(T value, int index) {
        int offset = index * __int_size;
        assertBounds(offset);
        CHandler.setPointerPart(getPointer(), __int_size, offset, value.getIndex());
    }
}
