package com.badlogic.gdx.jnigen.generator.types;

public interface MappedType {

    default String instantiationType() {
        return abstractType();
    }

    String abstractType();

    String primitiveType();

}
