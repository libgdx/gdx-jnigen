package com.badlogic.gdx.jnigen.generator.types;

import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public enum TypeKind {

    VOID(-2, false, CXType_Void),
    BOOLEAN(1, false, CXType_Bool),
    BYTE(1, true, CXType_Char_S, CXType_SChar),
    PROMOTED_BYTE(1, false, CXType_UChar, CXType_Char_U),
    SHORT(2, true, CXType_Short),
    CHAR(2, false, CXType_UShort),
    INT(4, true, CXType_Int),
    PROMOTED_INT(4, false, CXType_UInt),
    LONG(8, true, CXType_Long, CXType_LongLong),
    PROMOTED_LONG(8, false, CXType_ULong, CXType_ULongLong),
    FLOAT(4, true, CXType_Float),
    DOUBLE(8, true, CXType_Double, CXType_LongDouble),
    POINTER(8, false, CXType_Pointer, CXType_IncompleteArray),
    STACK_ELEMENT(-1, false, CXType_Record),
    CLOSURE(-1, false, CXType_FunctionProto, CXType_FunctionNoProto),
    ENUM(4, true, CXType_Enum),
    FIXED_SIZE_ARRAY(-1, false, CXType_ConstantArray);

    private final int size;
    private final boolean signed;
    private final int[] kinds;

    private static final TypeKind[] CACHE = values();

    TypeKind(int size, boolean signed, int... kinds) {
        this.size = size;
        this.signed = signed;
        this.kinds = kinds;
    }

    public static TypeKind getTypeKind(CXType type) {
        // TODO: 20.03.24 Get rid of at some point
        type = clang_getCanonicalType(type);
        int kind = type.kind();

        long size = clang_Type_getSizeOf(type);
        for (TypeKind typeKind : CACHE) {
            for (int k : typeKind.getKinds()) {
                if (k == kind) {
                    if (size != -2 && size != typeKind.getSize() && typeKind.getSize() != -1)
                        throw new IllegalArgumentException("Kind: " + kind + " got identified as " + typeKind.name() + ", but has a size of " + size + " != " + typeKind.size);
                    return typeKind;
                }
            }
        }
        throw new IllegalArgumentException("Could not find Kind for " + kind + " for type " + clang_getTypeSpelling(type).getString());
    }

    public int getSize() {
        return size;
    }

    public boolean isSigned() {
        return signed;
    }

    public int[] getKinds() {
        return kinds;
    }

    public boolean isSpecial() {
        return this == POINTER || this == STACK_ELEMENT || this == CLOSURE || this == ENUM || this == FIXED_SIZE_ARRAY;
    }
    public boolean isPrimitive() {
        return !isSpecial() && this != VOID;
    }
}
