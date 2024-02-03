package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.pointer.CType;
import com.badlogic.gdx.jnigen.pointer.Signed;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalType {

    private final HashMap<String, ClosureType> closures = new HashMap<>();
    private final List<FunctionType> functions = new ArrayList<>();

    public void addClosure(ClosureType closureType) {
        if (closures.containsKey(closureType.getName()))
            throw new IllegalArgumentException("Closure with name: " + closureType.getName() + " already exists.");
        closures.put(closureType.getName(), closureType);
    }

    public ClosureType getClosure(String name) {
        if (!closures.containsKey(name))
            throw new IllegalArgumentException("Closure with name: " + name + " does not exists.");

        return closures.get(name);
    }

    public void addFunction(FunctionType functionType) {
        functions.add(functionType);
    }


    public void write(CompilationUnit cu, HashMap<MethodDeclaration, String> patchNativeMethods) {
        cu.addImport(CType.class);
        cu.addImport(Signed.class);
        cu.addImport(ClosureObject.class);
        ClassOrInterfaceDeclaration global = cu.addClass("Global");
        global.addOrphanComment(new BlockComment("JNI\n#include <jnigen.h>\n"));
        for (FunctionType functionType : functions) {
            functionType.write(global, patchNativeMethods);
        }

        for (ClosureType closureType : closures.values()) {
            closureType.write(global);
        }
    }
}
