package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.PossibleTarget;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;

public interface MappedType {

    void importType(CompilationUnit cu);

    String classFile();

    String packageName();

    default String instantiationType() {
        return abstractType();
    }

    String abstractType();

    default String pointerType() {
        throw new IllegalArgumentException("Type " + abstractType() + " has no pointer class");
    }

    default void importPointerType(CompilationUnit cu) {
        importType(cu);
    }

    String primitiveType();

    Expression fromC(Expression cRetrieved);

    default Expression fromC(Expression cRetrieved, Expression owned) {
        return fromC(cRetrieved);
    }

    default Expression fromCPooled(Expression cRetrieved, Expression pool) {
        return fromC(cRetrieved);
    }

    Expression toC(Expression cSend);

    int typeID();

    default boolean isByValueClosureSafe() {
        return true;
    }

    default String internalClassName() {
        return abstractType() + "_Internal";
    }

    default String internalClass() {
        return classFile() + "_Internal";
    }

    default void importInternalType(CompilationUnit cu) {
        cu.addImport(internalClass());
    }

    Expression writeToBufferPtr(Expression bufferPtr, Expression offset, Expression valueToWrite);
    Expression readFromBufferPtr(Expression bufferPtr, Expression offset);
    int getSize(PossibleTarget target);
    default int getAlignment(PossibleTarget target) {
        return getSize(target);
    }
    default int getSizeFromC(PossibleTarget target) {
        return getSize(target);
    }
}
