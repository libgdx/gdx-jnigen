package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public interface WritableClass {

    ClassOrInterfaceDeclaration generateClass();

    void write(CompilationUnit cu, ClassOrInterfaceDeclaration toWriteTo);
}
