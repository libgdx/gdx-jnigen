package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;

public interface MappedType {

    default void importType(CompilationUnit cu) {
        throw new IllegalArgumentException();
    }

    String residingCU();

    default String instantiationType() {
        return abstractType();
    }

    String abstractType();

    String primitiveType();

}
