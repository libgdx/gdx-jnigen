package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.lang.reflect.Constructor;
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
        MethodDeclaration callMethod = wrappingClass.addMethod(name, Keyword.PUBLIC);

        MethodDeclaration nativeMethod = wrappingClass.addMethod(name + "_internal").setPrivate(true).setNative(true).setBody(null);
        callMethod.setType(returnType.getComplexJavaClass());
        if (returnType.getTypeKind().isPrimitive())
            nativeMethod.setType(returnType.getPrimitiveJavaClass());
        else
            nativeMethod.setType(long.class);
        MethodCallExpr callExpr = new MethodCallExpr(nativeMethod.getNameAsString());

        StringBuilder nativeBody = new StringBuilder();
        nativeBody.append(name).append("(");
        for (NamedType namedType : arguments) {
            callMethod.addParameter(namedType.getDefinition().getComplexJavaClass(), namedType.getName());
            if (namedType.getDefinition().getTypeKind() == TypeKind.STRUCT) {
                nativeBody.append("*(").append(namedType.getDefinition().getTypeName()).append("*)").append(namedType.getName()).append(", ");
            } else {
                nativeBody.append("(").append(namedType.getDefinition().getTypeName()).append(")").append(namedType.getName()).append(", ");
            }
            if (namedType.getDefinition().getTypeKind().isPrimitive()) {
                nativeMethod.addParameter(namedType.getDefinition().getPrimitiveJavaClass(), namedType.getName());
                callExpr.addArgument(namedType.getName());
            } else {
                nativeMethod.addParameter(long.class, namedType.getName());
                if (namedType.getDefinition().getTypeKind() == TypeKind.CLOSURE)
                    callExpr.addArgument(namedType.getName() + ".getFnPtr()");
                else
                    callExpr.addArgument(namedType.getName() + ".getPointer()");
            }
        }
        if (arguments.length != 0)
            nativeBody.delete(nativeBody.length() - 2, nativeBody.length());
        nativeBody.append(");");

        BlockStmt body = new BlockStmt();
        if (returnType.getTypeKind() != TypeKind.VOID) {
            if (returnType.getTypeKind().isPrimitive()) {
                nativeBody.insert(0, "return (j" + returnType.getPrimitiveJavaClass() + ")");
                body.addStatement(new ReturnStmt(callExpr));
            } else {
                nativeBody.insert(0, "return (jlong)");
                boolean freeOnGC = returnType.getTypeKind() == TypeKind.STRUCT;
                ObjectCreationExpr createObject = new ObjectCreationExpr();
                createObject.setType(Manager.getInstance().resolveCTypeMapping(returnType.getTypeName()));
                createObject.addArgument(callExpr);
                createObject.addArgument(Boolean.toString(freeOnGC));
                body.addStatement(new ReturnStmt(createObject));
            }
        } else {
            body.addStatement(callExpr);
        }
        callMethod.setBody(body);

        patchNativeMethod.put(nativeMethod, nativeBody.toString());
    }
}
