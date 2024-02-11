package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import org.bytedeco.llvm.clang.CXType;
import org.bytedeco.llvm.global.clang;

public class TypeDefinition {

    private final TypeKind typeKind;
    private final String typeName;
    private TypeDefinition nestedDefinition;

    public TypeDefinition(TypeKind typeKind, String typeName) {
        this.typeKind = typeKind;
        if (typeKind == TypeKind.STRUCT)
            this.typeName = typeName.replace("struct ", "");
        else
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
        if (typeName.equals("_Bool"))
            typeName = "bool"; //TODO WHYYYY?????? Is it a typedef that gets resolved?
        TypeDefinition definition = new TypeDefinition(TypeKind.getTypeKind(type), typeName);
        if (!definition.getTypeKind().isSpecial() && !Manager.getInstance().hasCTypeMapping(typeName)) {
            Manager.getInstance().recordCType(typeName);
            Manager.getInstance().registerCTypeMapping(typeName, PrimitiveType.fromTypeDefinition(definition));
        }
        if (definition.getTypeKind() == TypeKind.POINTER) {
            CXType pointee = clang.clang_getPointeeType(type);
            definition.nestedDefinition = createTypeDefinition(pointee);
        }
        return definition;
    }

    public String asCastExpression(String toCallOn) {
        switch (typeKind) {
            case ENUM:
                return getMappedType().instantiationType() + ".getByIndex(" + toCallOn + ".asInt())";
            case CLOSURE:
            case POINTER:
            case STRUCT:
                return "(" + getMappedType().instantiationType() + ")" +  toCallOn + ".asPointing()";
            default:
                String name = getMappedType().abstractType();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                return toCallOn +  ".as" + name + "()";
        }
    }

    public MappedType getMappedType() {
        if (nestedDefinition != null)
            return nestedDefinition.getMappedType().asPointer();
        return Manager.getInstance().resolveCTypeMapping(typeName);
    }
}
