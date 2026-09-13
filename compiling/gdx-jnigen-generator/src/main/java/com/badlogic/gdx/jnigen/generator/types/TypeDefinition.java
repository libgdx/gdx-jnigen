package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.PossibleTarget;

public class TypeDefinition {

    private final String typeName;
    private TypeKind typeKind;
    private TypeDefinition nestedDefinition;
    private boolean constMarked = false;
    private int count = 1;
    private boolean anonymous;
    private DeclaringMember declaringMember;
    private String typedefName;
    private MappedType mappedType;
    private final PossibleTarget target;
    private final long observedSize;
    private final long observedAlignment;


    public TypeDefinition(TypeKind typeKind, String typeName, PossibleTarget target, long observedSize, long observedAlignment) {
        this.typeKind = typeKind;
        this.typeName = typeName;
        this.target = target;
        this.observedSize = observedSize;
        this.observedAlignment = observedAlignment;
        if (typeName.startsWith("const "))
            constMarked = true;
    }

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public void setTypeKind(TypeKind typeKind) {
        this.typeKind = typeKind;
    }

    public PossibleTarget getTarget() {
        return target;
    }

    public long getObservedSize() {
        return observedSize;
    }

    public long getObservedAlignment() {
        return observedAlignment;
    }

    boolean matchesLayoutOf(TypeKind kind) {
        return kind.getSize(target) == observedSize && kind.getAlignment(target) == observedAlignment;
    }

    @Override
    public String toString() {
        return target + "=" + typeKind + "(" + observedSize + " bytes, align " + observedAlignment + ")";
    }

    public void setOverrideMappedType(MappedType mappedType) {
        this.mappedType = mappedType;
    }

    public String getTypeName() {
        return typeName;
    }

    public String cTypeName() {
        if (typedefName != null)
            return typedefName;
        if (declaringMember != null)
            return "__jnigen_strip<decltype((*(" + declaringMember.owner.cTypeName() + "*)0)." + declaringMember.name + ")>::type";
        return typeName;
    }

    public DeclaringMember getDeclaringMember() {
        return declaringMember;
    }

    public void setDeclaringMember(DeclaringMember declaringMember) {
        this.declaringMember = declaringMember;
    }

    public void setTypedefName(String typedefName) {
        this.typedefName = typedefName;
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
