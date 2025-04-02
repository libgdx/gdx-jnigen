package com.badlogic.gdx.jnigen.generator.types;

import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public enum TypeKind {

    VOID(CXType_Void),
    BOOLEAN(CXType_Bool),
    BYTE(CXType_Char_S, CXType_SChar),
    PROMOTED_BYTE(CXType_UChar, CXType_Char_U),
    SHORT(CXType_Short),
    CHAR(CXType_UShort),
    INT(CXType_Int),
    PROMOTED_INT(CXType_UInt),
    LONG(CXType_Long),
    PROMOTED_LONG(CXType_ULong),
    LONG_LONG(CXType_LongLong),
    PROMOTED_LONG_LONG(CXType_ULongLong),
    FLOAT(CXType_Float),
    DOUBLE(CXType_Double, CXType_LongDouble),
    POINTER(CXType_Pointer, CXType_IncompleteArray),
    STACK_ELEMENT(CXType_Record),
    CLOSURE(CXType_FunctionProto, CXType_FunctionNoProto),
    ENUM(CXType_Enum),
    FIXED_SIZE_ARRAY(CXType_ConstantArray);

    private final int[] kinds;

    private static final TypeKind[] CACHE = values();

    TypeKind(int... kinds) {
        this.kinds = kinds;
    }

    public static TypeKind getTypeKind(CXType type) {
        // TODO: 20.03.24 Get rid of at some point
        type = clang_getCanonicalType(type);
        int kind = type.kind();

        for (TypeKind typeKind : CACHE) {
            for (int k : typeKind.getKinds()) {
                if (k == kind) {
                    return typeKind;
                }
            }
        }

        throw new IllegalArgumentException("Could not find Kind for " + kind + " for type " + clang_getTypeSpelling(type).getString());
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

    public boolean isSigned() {
        switch (this) {
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case LONG_LONG:
        case FLOAT:
        case DOUBLE:
            return true;
        case BOOLEAN:
        case PROMOTED_BYTE:
        case CHAR:
        case PROMOTED_INT:
        case PROMOTED_LONG:
        case PROMOTED_LONG_LONG:
            return false;
        default:
            throw new IllegalArgumentException("Type " + this + " is not a primitive type");
        }
    }

    public int getSize(boolean is32Bit, boolean isWin) {
        switch (this) {
        case BOOLEAN:
        case BYTE:
        case PROMOTED_BYTE:
            return 1;
        case SHORT:
        case CHAR:
            return 2;
        case INT:
        case PROMOTED_INT:
        case FLOAT:
            return 4;
        case LONG:
        case PROMOTED_LONG:
            return is32Bit || isWin ? 4 : 8;
        case LONG_LONG:
        case PROMOTED_LONG_LONG:
        case DOUBLE:
            return 8;
        default:
            throw new IllegalArgumentException("Type " + this + " is not a primitive type");
        }
    }
}
