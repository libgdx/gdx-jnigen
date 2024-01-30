package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.List;

public class Global {

    private final List<ClosureType> closures = new ArrayList<>();

    public void addClosure(ClosureType closureType) {
        closures.add(closureType);
    }

    public void write(CompilationUnit cu) {
        ClassOrInterfaceDeclaration global = cu.addClass("Global");

        for (ClosureType closureType : closures) {
            closureType.write(global);
        }
    }
}
