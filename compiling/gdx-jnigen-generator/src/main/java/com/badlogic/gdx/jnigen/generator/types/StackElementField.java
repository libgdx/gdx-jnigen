package com.badlogic.gdx.jnigen.generator.types;

public class StackElementField {

    private final NamedType type;
    private final String comment;

    public StackElementField(NamedType type, String comment) {
        this.type = type;
        this.comment = comment;
    }

    public NamedType getType() {
        return type;
    }

    public String getComment() {
        return comment;
    }
}
