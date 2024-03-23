package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.Statement;

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

    Expression fromC(Expression cRetrieved);

    default Expression fromC(Expression cRetrieved, boolean owned) {
        return fromC(cRetrieved);
    }

    Expression toC(Expression cSend);

    int typeID();

    default Statement assertJava(Expression scope) {
        return new EmptyStmt();
    }

    default String assertC(Expression scope) {
        return "";
    }

    default boolean isLibFFIConvertible() {
        return true;
    }
}
