package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import org.bytedeco.llvm.clang.CXType;
import org.bytedeco.llvm.global.clang;

public class TypeDefinition {

    private final TypeKind typeKind;
    private final String typeName;
    private TypeDefinition nestedDefinition;
    private boolean constMarked = false;
    private int count;

    public TypeDefinition(TypeKind typeKind, String typeName) {
        this.typeKind = typeKind;
        if (typeKind.isPrimitive())
            Manager.getInstance().recordCType(typeName);
        if (typeName.startsWith("const ")) {
            this.typeName = typeName.replace("const ", "");
            constMarked = true;
        } else {
            this.typeName = typeName;
        }
    }

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getCount() {
        return count;
    }

    public TypeDefinition getNestedDefinition() {
        return nestedDefinition;
    }

    public static TypeDefinition createTypeDefinition(CXType type) {
        String typeName = clang.clang_getTypeSpelling(type).getString();
        if (typeName.equals("_Bool"))
            typeName = "bool"; //TODO WHYYYY?????? Is it a typedef that gets resolved?
        TypeDefinition definition = new TypeDefinition(TypeKind.getTypeKind(type), typeName);
        if (definition.getTypeKind() == TypeKind.POINTER) {
            CXType pointee = clang.clang_getPointeeType(clang.clang_getCanonicalType(type));
            if (pointee.kind() == 0) {
                definition.nestedDefinition = new TypeDefinition(TypeKind.VOID, "void");
            } else {
                definition.nestedDefinition = createTypeDefinition(pointee);
            }
            // TODO: 19.03.2024 Make better
            if (definition.nestedDefinition.typeKind == TypeKind.CLOSURE)
                return new TypeDefinition(TypeKind.CLOSURE, typeName); // TODO: WROOONG, but atm I do not care
        } else if (definition.getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
            definition.count = (int)clang.clang_getArraySize(type);
            definition.nestedDefinition = createTypeDefinition(clang.clang_getArrayElementType(type));
        }
        return definition;
    }

    public MappedType getMappedType() {
        if (nestedDefinition != null)
            return nestedDefinition.getMappedType().asPointer();
        if (typeKind.isPrimitive() || typeKind == TypeKind.VOID) // TODO: Is this correct with void?
            return PrimitiveType.fromTypeDefinition(this);

        return Manager.getInstance().resolveCTypeMapping(typeName);
    }
}
