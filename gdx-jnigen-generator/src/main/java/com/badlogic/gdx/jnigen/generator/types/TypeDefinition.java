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

    public static TypeDefinition getTypeDefinition(CXType type) {
        return new TypeDefinition(TypeKind.getTypeKind(type), clang.clang_getTypeSpelling(type).getString());
    }

    public String asCastExpression(String toCallOn) {
        switch (typeKind) {
            case POINTER:
                throw new IllegalArgumentException();
            case STRUCT:
                return "(" + typeName + ")" +  toCallOn + ".asPointing()";
            default:
                String name = resolveJavaClass();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                return toCallOn +  ".as" + name + "()";
        }
    }

    public String resolveJavaClass() {
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
            case PROMOTED_SHORT:
            case INT:
                return int.class.getName();
            case POINTER:
            case LONG:
            case PROMOTED_INT:
            case PROMOTED_LONG:
                return long.class.getName();
            case FLOAT:
                return float.class.getName();
            case DOUBLE:
                return double.class.getName();
            case STRUCT:
                return Manager.getInstance().getStruct(typeName).getName();
            default:
                throw new IllegalArgumentException();
        }
    }
}
