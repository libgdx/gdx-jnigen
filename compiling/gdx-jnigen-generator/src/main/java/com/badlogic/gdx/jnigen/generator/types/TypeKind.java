package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.PossibleTarget;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXType;

import java.util.List;

import static org.bytedeco.llvm.global.clang.*;

public enum TypeKind {

    VOID(CXType_Void),
    BOOLEAN(CXType_Bool),
    NATIVE_BYTE(CXType_Char_S, CXType_Char_U),
    SIGNED_BYTE(CXType_SChar),
    PROMOTED_BYTE(CXType_UChar),
    SHORT(CXType_Short),
    CHAR(CXType_UShort),
    INT(CXType_Int),
    PROMOTED_INT(CXType_UInt),
    LONG(CXType_Long),
    PROMOTED_LONG(CXType_ULong),
    LONG_LONG(CXType_LongLong),
    PROMOTED_LONG_LONG(CXType_ULongLong),
    WORD(),
    PROMOTED_WORD(),
    FLOAT(CXType_Float),
    DOUBLE(CXType_Double, CXType_LongDouble),
    POINTER(CXType_Pointer, CXType_IncompleteArray),
    STRUCT(),
    UNION(),
    CLOSURE(CXType_FunctionProto, CXType_FunctionNoProto),
    ENUM(CXType_Enum),
    FIXED_SIZE_ARRAY(CXType_ConstantArray);

    private final int[] kinds;

    private static final TypeKind[] CACHE = values();

    TypeKind(int... kinds) {
        this.kinds = kinds;
    }

    public static TypeKind forClangKind(int clangKind) {
        for (TypeKind typeKind : CACHE)
            for (int k : typeKind.getKinds())
                if (k == clangKind)
                    return typeKind;
        return null;
    }

    public static TypeKind getTypeKind(CXType type) {
        // TODO: 20.03.24 Get rid of at some point
        CXType canonicalType = clang_getCanonicalType(type);
        int kind = canonicalType.kind();

        if (kind == CXType_Record) {
            CXCursor cursor = clang_getTypeDeclaration(type);
            return cursor.kind() == CXCursor_StructDecl ? TypeKind.STRUCT : TypeKind.UNION;
        }

        TypeKind typeKind = forClangKind(kind);
        if (typeKind == null)
            throw new IllegalArgumentException("Could not find Kind for " + kind + " for type " + clang_getTypeSpelling(type).getString());
        return typeKind;
    }

    public int[] getKinds() {
        return kinds;
    }

    public boolean isSpecial() {
        return this == POINTER || this == STRUCT || this == UNION || this == CLOSURE || this == ENUM || this == FIXED_SIZE_ARRAY;
    }
    public boolean isPrimitive() {
        return !isSpecial() && this != VOID;
    }

    public boolean isStackElement() {
        return this == STRUCT || this == UNION;
    }

    public boolean isSigned() {
        switch (this) {
        case SIGNED_BYTE:
        case SHORT:
        case INT:
        case LONG:
        case LONG_LONG:
        case WORD:
        case FLOAT:
        case DOUBLE:
            return true;
        case BOOLEAN:
        case PROMOTED_BYTE:
        case CHAR:
        case PROMOTED_INT:
        case PROMOTED_LONG:
        case PROMOTED_LONG_LONG:
        case PROMOTED_WORD:
            return false;
        default:
            throw new IllegalArgumentException("Type " + this + " is not a primitive type");
        }
    }

    private static final TypeKind[] SIGNED_INTEGER_CANDIDATES = {INT, LONG, LONG_LONG, WORD};
    private static final TypeKind[] UNSIGNED_INTEGER_CANDIDATES = {PROMOTED_INT, PROMOTED_LONG, PROMOTED_LONG_LONG, PROMOTED_WORD};


    public static TypeKind resolve(String typeName, List<TypeDefinition> definitions) {
        if (definitions.isEmpty())
            throw new IllegalArgumentException("Type " + typeName + " was never observed by any parse pass");
        TypeKind first = definitions.get(0).getTypeKind();
        boolean allSame = true;
        for (TypeDefinition definition : definitions)
            allSame &= definition.getTypeKind() == first;
        if (allSame) {
            if (first.isPrimitive())
                checkLayout(typeName, first, definitions);
            return first;
        }

        // Only integer kinds may legitimately differ between targets, and only within one signedness.
        if (!isInteger(first))
            throw new IllegalStateException("Type " + typeName + " has no platform-independent kind, the targets disagree: " + definitions);
        boolean signed = first.isSigned();
        for (TypeDefinition definition : definitions) {
            if (!isInteger(definition.getTypeKind()) || definition.getTypeKind().isSigned() != signed)
                throw new IllegalStateException("Type " + typeName + " has no platform-independent kind, the targets disagree: " + definitions);
        }

        TypeKind[] candidates = signed ? SIGNED_INTEGER_CANDIDATES : UNSIGNED_INTEGER_CANDIDATES;
        for (TypeKind candidate : candidates) {
            boolean matches = true;
            for (TypeDefinition definition : definitions)
                matches &= definition.matchesLayoutOf(candidate);
            if (matches)
                return candidate;
        }
        throw new IllegalStateException("Type " + typeName + " has no platform-independent kind, no size pattern matches: " + definitions);
    }

    private static void checkLayout(String typeName, TypeKind kind, List<TypeDefinition> definitions) {
        for (TypeDefinition definition : definitions) {
            if (!definition.matchesLayoutOf(kind))
                throw new IllegalStateException("Type " + typeName + " is " + kind + " on every target, but its observed layout does not match jnigen's model for "
                        + kind + " (" + kind.getSize(definition.getTarget()) + " bytes, align " + kind.getAlignment(definition.getTarget()) + " on " + definition.getTarget()
                        + "): " + definitions);
        }
    }

    private static boolean isInteger(TypeKind kind) {
        // Excluding NATIVE_BYTE here is best-effort, cause it has not sign
        return kind.isPrimitive() && kind != BOOLEAN && kind != FLOAT && kind != DOUBLE && kind != NATIVE_BYTE;
    }

    public int getSize(PossibleTarget target) {
        switch (this) {
        case BOOLEAN:
        case NATIVE_BYTE:
        case SIGNED_BYTE:
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
            return target.is32Bit() || target.isWin() ? 4 : 8;
        case WORD:
        case PROMOTED_WORD:
            return target.is32Bit() ? 4 : 8;
        case LONG_LONG:
        case PROMOTED_LONG_LONG:
        case DOUBLE:
            return 8;
        default:
            throw new IllegalArgumentException("Type " + this + " is not a primitive type");
        }
    }

    public int getAlignment(PossibleTarget target) {
        switch (this) {
        case BOOLEAN:
        case NATIVE_BYTE:
        case SIGNED_BYTE:
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
            return target.is32Bit() || target.isWin() ? 4 : 8;
        case WORD:
        case PROMOTED_WORD:
            return target.is32Bit() ? 4 : 8;
        case LONG_LONG:
        case PROMOTED_LONG_LONG:
        case DOUBLE:
            return target.isUnixX86_32() ? 4 : 8;
        default:
            throw new IllegalArgumentException("Type " + this + " is not a primitive type");
        }
    }
}
