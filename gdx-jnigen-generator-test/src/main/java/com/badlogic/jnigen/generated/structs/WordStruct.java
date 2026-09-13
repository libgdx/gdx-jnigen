package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;

public final class WordStruct extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(38).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public WordStruct(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public WordStruct(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public WordStruct() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public WordStruct.WordStructPointer asPointer() {
        return new WordStruct.WordStructPointer(getPointer(), false, 1, this);
    }

    public void asPointer(WordStruct.WordStructPointer ptr) {
        ptr.setPointer(this);
    }

    public char tag() {
        return getBufPtr().getUByte(0);
    }

    public void tag(char tag) {
        getBufPtr().setUByte(0, tag);
    }

    public long count() {
        return getBufPtr().getNativeUWord(CHandler.IS_64_BIT ? 8 : 4);
    }

    public void count(long count) {
        getBufPtr().setNativeUWord(CHandler.IS_64_BIT ? 8 : 4, count);
    }

    public long delta() {
        return getBufPtr().getNativeWord(CHandler.IS_64_BIT ? 16 : 8);
    }

    public void delta(long delta) {
        getBufPtr().setNativeWord(CHandler.IS_64_BIT ? 16 : 8, delta);
    }

    public long address() {
        return getBufPtr().getNativeWord(CHandler.IS_64_BIT ? 24 : 12);
    }

    public void address(long address) {
        getBufPtr().setNativeWord(CHandler.IS_64_BIT ? 24 : 12, address);
    }

    public long aliased() {
        return getBufPtr().getNativeUWord(CHandler.IS_64_BIT ? 32 : 16);
    }

    public void aliased(long aliased) {
        getBufPtr().setNativeUWord(CHandler.IS_64_BIT ? 32 : 16, aliased);
    }

    public static final class WordStructPointer extends StackElementPointer<WordStruct> {

        public WordStructPointer(VoidPointer pointer) {
            super(pointer);
        }

        public WordStructPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public WordStructPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public WordStructPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public WordStructPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public WordStructPointer() {
            this(1, true);
        }

        public WordStructPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected WordStruct createStackElement(long ptr, boolean freeOnGC) {
            return new WordStruct(ptr, freeOnGC);
        }
    }
}
