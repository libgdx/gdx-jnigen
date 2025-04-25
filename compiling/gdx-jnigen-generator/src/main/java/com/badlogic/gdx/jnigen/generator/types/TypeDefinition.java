package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;

public class TypeDefinition {

    private final String typeName;
    private TypeKind typeKind;
    private TypeDefinition nestedDefinition;
    private boolean constMarked = false;
    private int count = 1;
    private boolean anonymous;
    private MappedType mappedType;


    private TypeDefinition(TypeKind typeKind, String typeName) {
        this.typeKind = typeKind;
        this.typeName = typeName;
        if (typeName.startsWith("const "))
            constMarked = true;
    }

    public static TypeDefinition get(TypeKind typeKind, String typeName) {
        if (typeKind.isPrimitive()) {
            if (!Manager.getInstance().hasCType(typeName)) {
                TypeDefinition definition = new TypeDefinition(typeKind, typeName);
                Manager.getInstance().recordCType(typeName, definition);
                return definition;
            }

            TypeDefinition definition = Manager.getInstance().getCType(typeName);
            if (definition.getTypeKind() != typeKind)
                throw new IllegalArgumentException("Type " + typeName + " has kind " + definition.getTypeKind() + ", but requested was " + typeKind);
            return definition;
        }

        return new TypeDefinition(typeKind, typeName);
    }

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public void setTypeKind(TypeKind typeKind) {
        this.typeKind = typeKind;
    }

    public void setOverrideMappedType(MappedType mappedType) {
        this.mappedType = mappedType;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TypeDefinition getNestedDefinition() {
        return nestedDefinition;
    }

    public void setNestedDefinition(TypeDefinition nestedDefinition) {
        this.nestedDefinition = nestedDefinition;
    }

    public int getDepth() {
        int depth = 0;
        TypeDefinition inner = this;
        while (inner != null) {
            depth++;
            inner = inner.nestedDefinition;
        }
        return depth;
    }

    public TypeDefinition rootType() {
        TypeDefinition root = this;
        while (root.nestedDefinition != null)
            root = root.nestedDefinition;
        return root;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public MappedType getMappedType() {
        if (mappedType != null)
            return mappedType;
        throw new IllegalArgumentException("Type with name " + typeName + " has no mapped type");
    }
}
