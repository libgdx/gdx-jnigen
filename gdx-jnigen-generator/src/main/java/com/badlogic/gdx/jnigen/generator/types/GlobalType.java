package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.pointer.CType;
import com.badlogic.gdx.jnigen.pointer.Signed;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalType {

    private final List<ClosureType> closures = new ArrayList<>();
    private final List<FunctionType> functions = new ArrayList<>();

    public void addClosure(ClosureType closureType) {
        closures.add(closureType);
    }

    public void addFunction(FunctionType functionType) {
        functions.add(functionType);
    }


    public void write(CompilationUnit cu, HashMap<MethodDeclaration, String> patchNativeMethods) {
        cu.addImport(CType.class);
        cu.addImport(Signed.class);
        ClassOrInterfaceDeclaration global = cu.addClass("Global");

        for (FunctionType functionType : functions) {
            functionType.write(global, patchNativeMethods);
        }

        for (ClosureType closureType : closures) {
            closureType.write(global);
        }
    }
}
