package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.Arrays;
import java.util.HashMap;

public class FunctionType {

    private final String name;
    private final NamedType[] arguments;
    private final TypeDefinition returnType;

    public FunctionType(String name, NamedType[] arguments, TypeDefinition returnType) {
        this.name = name;
        this.arguments = arguments;
        this.returnType = returnType;
    }

    public void write(ClassOrInterfaceDeclaration wrappingClass, HashMap<MethodDeclaration, String> patchNativeMethod) {
        MethodDeclaration callMethod = wrappingClass.addMethod(name);



        MethodDeclaration nativeMethod = wrappingClass.addMethod(name + "_internal").setPrivate(true).setNative(true).setBody(null);
        callMethod.setType(returnType.resolveJavaClass());
        if (returnType.getTypeKind().isPrimitive())
            nativeMethod.setType(returnType.resolveJavaClass());
        else
            nativeMethod.setType(long.class);
        MethodCallExpr callExpr = new MethodCallExpr(nativeMethod.getNameAsString());

        StringBuilder nativeBody = new StringBuilder();
        nativeBody.append(name).append("(");
        for (NamedType namedType : arguments) {
            callMethod.addParameter(namedType.getDefinition().resolveJavaClass(), namedType.getName());
            nativeBody.append(namedType.getName()).append(", ");
            if (namedType.getDefinition().getTypeKind().isPrimitive()) {
                nativeMethod.addParameter(namedType.getDefinition().resolveJavaClass(), namedType.getName());
                callExpr.addArgument(namedType.getName());
            } else {
                nativeMethod.addParameter(long.class, namedType.getName());
                callExpr.addArgument(namedType.getName() + ".getPointer()");
            }
        }
        if (arguments.length != 0)
            nativeBody.delete(nativeBody.length() - 2, nativeBody.length());
        nativeBody.append(");");

        BlockStmt body = new BlockStmt();
        if (returnType.getTypeKind() != TypeKind.VOID) {
            nativeBody.insert(0, "return ");
            body.addStatement(new ReturnStmt(callExpr));
        } else {
            body.addStatement(callExpr);
        }
        callMethod.setBody(body);

        patchNativeMethod.put(nativeMethod, nativeBody.toString());
    }
}
