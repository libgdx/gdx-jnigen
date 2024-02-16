package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;

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

    MappedType asPointer();

    Expression fromC(Expression cRetrieved);

    Expression toC(Expression cSend);

    int typeID();

}
