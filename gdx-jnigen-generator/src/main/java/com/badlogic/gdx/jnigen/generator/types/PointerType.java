package com.badlogic.gdx.jnigen.generator.types;

public class PointerType implements MappedType {

    private TypeDefinition pointerDefinition;
    private MappedType pointingTo;

    private String name;

    public PointerType(String name, TypeDefinition pointerDefinition, MappedType pointingTo) {
        this.pointerDefinition = pointerDefinition;
        this.pointingTo = pointingTo;
        this.name = name;
    }

    @Override
    public String instantiationType() {
        return name;
    }

    @Override
    public String abstractType() {
        return "StructPointer<" + pointingTo.abstractType() + ">";
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }
}
