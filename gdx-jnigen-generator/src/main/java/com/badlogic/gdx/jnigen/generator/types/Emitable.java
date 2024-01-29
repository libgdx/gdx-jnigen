package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

public interface Emitable {

    void write(CompilationUnit cu);
}
