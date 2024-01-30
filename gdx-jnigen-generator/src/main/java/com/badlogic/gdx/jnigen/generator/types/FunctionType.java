package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.Arrays;

public class FunctionType {

    private final String name;
    private final NamedType[] arguments;
    private final TypeDefinition returnType;

    public FunctionType(String name, NamedType[] arguments, TypeDefinition returnType) {
        this.name = name;
        this.arguments = arguments;
        this.returnType = returnType;
    }

    public void write(ClassOrInterfaceDeclaration wrappingClass) {
        boolean doWeNeedToUnwrap = !returnType.getTypeKind().isPrimitive() || Arrays.stream(arguments).anyMatch(type -> !type.getDefinition().getTypeKind().isPrimitive());

        MethodDeclaration callMethod = wrappingClass.addMethod(name);

        for (NamedType namedType : arguments) {
            callMethod.addParameter(namedType.getDefinition().resolveJavaClass(), namedType.getName());
        }
        callMethod.setType(returnType.resolveJavaClass());
        if (!doWeNeedToUnwrap) {
            callMethod.setBody(null);
            callMethod.setNative(true);
        } else {
            MethodDeclaration nativeMethod = wrappingClass.addMethod(name + "_internal").setPrivate(true).setNative(true);
            MethodCallExpr callExpr = new MethodCallExpr(nativeMethod.getNameAsString());
            for (NamedType namedType : arguments) {
                if (namedType.getDefinition().getTypeKind().isPrimitive()) {
                    callMethod.addParameter(namedType.getDefinition().resolveJavaClass(), namedType.getName());
                    callExpr.addArgument(namedType.getName());
                } else {
                    callMethod.addParameter(long.class, namedType.getName());
                    callExpr.addArgument(namedType.getName() + ".getPointer()");
                }
            }
            if (returnType.getTypeKind() != TypeKind.VOID) {

            }
        }
    }
}
