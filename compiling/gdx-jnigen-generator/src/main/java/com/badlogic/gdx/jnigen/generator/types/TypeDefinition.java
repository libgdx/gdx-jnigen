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
    private boolean anonymous;
    private MappedType mappedType;


    public TypeDefinition(TypeKind typeKind, String typeName) {
        this.typeKind = typeKind;
        if (typeKind.isPrimitive())
            Manager.getInstance().recordCType(typeName);
        this.typeName = typeName;
        if (typeName.startsWith("const "))
            constMarked = true;
    }

    public TypeKind getTypeKind() {
        return typeKind;
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
