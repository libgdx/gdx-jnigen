package com.badlogic.gdx.jnigen.generator.types;

public final class DeclaringMember {

    public final TypeDefinition owner;
    public final String name;

    public DeclaringMember(TypeDefinition owner, String name) {
        this.owner = owner;
        this.name = name;
    }
}
