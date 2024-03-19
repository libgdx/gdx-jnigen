package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalType implements MappedType {

    private final HashMap<String, ClosureType> closures = new HashMap<>();
    private final List<FunctionType> functions = new ArrayList<>();

    private final String globalName;

    public GlobalType(String globalName) {
        this.globalName = globalName;
    }

    public void addClosure(ClosureType closureType) {
        if (closures.containsKey(closureType.getName()))
            throw new IllegalArgumentException("Closure with name: " + closureType.getName() + " already exists.");
        closures.put(closureType.getName(), closureType);
    }

    public boolean hasClosure(ClosureType closureType) {
        return closures.containsValue(closureType);
    }

    public void addFunction(FunctionType functionType) {
        functions.add(functionType);
    }


    public void write(CompilationUnit cu, HashMap<MethodDeclaration, String> patchNativeMethods) {
        cu.addImport(CHandler.class);
        ClassOrInterfaceDeclaration global = cu.addClass(globalName, Keyword.PUBLIC, Keyword.FINAL);
        global.addOrphanComment(new BlockComment("JNI\n#include <jnigen.h>\n#include <" + Manager.getInstance().getParsedCHeader() + ">\n"));
        for (FunctionType functionType : functions) {
            functionType.write(cu, global, patchNativeMethods);
        }

        for (ClosureType closureType : closures.values()) {
            ClassOrInterfaceDeclaration declaration = closureType.generateClass();
            closureType.write(cu, declaration);
            global.addMember(declaration);
        }
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(classFile());
    }

    @Override
    public String instantiationType() {
        throw new IllegalArgumentException();
    }

    @Override
    public String abstractType() {
        return globalName;
    }

    @Override
    public String primitiveType() {
        throw new IllegalArgumentException();
    }

    @Override
    public String classFile() {
        return Manager.getInstance().getBasePackage() + "." + globalName;
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage();
    }

    @Override
    public MappedType asPointer() {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public Expression toC(Expression cSend) {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public int typeID() {
        throw new IllegalArgumentException("Should not reach");
    }
}
