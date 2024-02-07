package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;

public interface MappedType {

    default void importType(CompilationUnit cu) {
        throw new IllegalArgumentException();
    }

    String classFile();

    String packageName();

    default String instantiationType() {
        return abstractType();
    }

    String abstractType();

    String primitiveType();

}
