package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import org.bytedeco.llvm.clang.CXType;
import org.bytedeco.llvm.global.clang;

public class TypeDefinition {

    private final TypeKind typeKind;
    private final String typeName;

    public TypeDefinition(TypeKind typeKind, String typeName) {
        this.typeKind = typeKind;
        this.typeName = typeName;
    }

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public String getTypeName() {
        return typeName;
    }

    public static TypeDefinition createTypeDefinition(CXType type) {
        String typeName = clang.clang_getTypeSpelling(type).getString();
        Manager.getInstance().recordCType(typeName);
        return new TypeDefinition(TypeKind.getTypeKind(type), typeName);
    }

    public String asCastExpression(String toCallOn) {
        switch (typeKind) {
            case CLOSURE:
            case POINTER:
            case STRUCT:
                return "(" + Manager.getInstance().resolveCTypeMapping(typeName) + ")" +  toCallOn + ".asPointing()";
            default:
                String name = getPrimitiveJavaClass();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                return toCallOn +  ".as" + name + "()";
        }
    }

    public String getComplexJavaClass() {
        if (!typeKind.isSpecial())
            return getPrimitiveJavaClass();
        return Manager.getInstance().resolveCTypeMapping(typeName);
    }

    public String getPrimitiveJavaClass() {
        switch (typeKind) {
            case VOID:
                return void.class.getName();
            case BOOLEAN:
                return boolean.class.getName();
            case BYTE:
                return byte.class.getName();
            case SHORT:
            case PROMOTED_BYTE:
                return short.class.getName();
            case CHAR:
                return char.class.getName();
            case INT:
                return int.class.getName();
            case POINTER:
            case CLOSURE:
            case STRUCT:
            case LONG:
            case PROMOTED_INT:
            case PROMOTED_LONG:
                return long.class.getName();
            case FLOAT:
                return float.class.getName();
            case DOUBLE:
                return double.class.getName();
            default:
                throw new IllegalArgumentException();
        }
    }
}
