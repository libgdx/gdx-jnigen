package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public interface WritableClass {

    ClassOrInterfaceDeclaration generateClass();
    ClassOrInterfaceDeclaration generateClassInternal();

    void write(CompilationUnit cuPublic, ClassOrInterfaceDeclaration toWriteToPublic, CompilationUnit cuPrivate, ClassOrInterfaceDeclaration toWriteToPrivate);
}
