package com.badlogic.gdx.jnigen.generator.types;

public class NamedType {

    private final TypeDefinition definition;
    private String name;

    public NamedType(TypeDefinition definition, String name) {
        this.definition = definition;
        this.name = name;
    }

    public TypeDefinition getDefinition() {
        return definition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
