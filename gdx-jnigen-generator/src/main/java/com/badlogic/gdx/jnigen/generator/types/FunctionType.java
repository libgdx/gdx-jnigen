package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
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

    public void write(CompilationUnit cu, ClassOrInterfaceDeclaration wrappingClass, HashMap<MethodDeclaration, String> patchNativeMethod) {
        MethodDeclaration callMethod = wrappingClass.addMethod(name, Keyword.PUBLIC, Keyword.STATIC);

        MethodDeclaration nativeMethod = wrappingClass.addMethod(name + "_internal").setStatic(true).setPrivate(true).setNative(true).setBody(null);
        callMethod.setType(returnType.getMappedType().abstractType());
        returnType.getMappedType().importType(cu);
        if (returnType.getTypeKind().isPrimitive())
            nativeMethod.setType(returnType.getMappedType().abstractType());
        else if (returnType.getTypeKind() == TypeKind.POINTER)
            nativeMethod.setType(long.class);
        else if (returnType.getTypeKind() == TypeKind.ENUM)
            nativeMethod.setType(int.class);
        MethodCallExpr callExpr = new MethodCallExpr(nativeMethod.getNameAsString());

        StringBuilder nativeBody = new StringBuilder();
        nativeBody.append(name).append("(");
        for (NamedType namedType : arguments) {
            namedType.getDefinition().getMappedType().importType(cu);
            callMethod.addParameter(namedType.getDefinition().getMappedType().abstractType(), namedType.getName());
            if (namedType.getDefinition().getTypeKind() == TypeKind.STRUCT) {
                nativeBody.append("*(").append(namedType.getDefinition().getTypeName()).append("*)").append(namedType.getName()).append(", ");
            } else {
                nativeBody.append("(").append(namedType.getDefinition().getTypeName()).append(")").append(namedType.getName()).append(", ");
            }
            nativeMethod.addParameter(namedType.getDefinition().getMappedType().primitiveType(), namedType.getName());
            if (namedType.getDefinition().getTypeKind().isPrimitive()) {
                callExpr.addArgument(namedType.getName());
            } else {
                if (namedType.getDefinition().getTypeKind() == TypeKind.CLOSURE)
                    callExpr.addArgument(namedType.getName() + ".getFnPtr()");
                else if (namedType.getDefinition().getTypeKind() == TypeKind.ENUM)
                    callExpr.addArgument(namedType.getName() + ".getIndex()");
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
                nativeBody.insert(0, "return (j" + returnType.getMappedType().primitiveType() + ")");
                body.addStatement(new ReturnStmt(callExpr));
            } else {
                if (returnType.getTypeKind() == TypeKind.STRUCT) {
                    nativeMethod.addParameter(long.class, "_ret");
                    nativeBody.insert(0, "*(" + returnType.getTypeName() + " *)_ret  = ");
                    body.addStatement(returnType.getTypeName() + " _ret = new " + returnType.getTypeName() + "();");
                    callExpr.addArgument("_ret.getPointer()");
                    body.addStatement(callExpr);
                    body.addStatement("return _ret;");
                } else if (returnType.getTypeKind() == TypeKind.ENUM) {
                    nativeBody.insert(0, "return (jint)");
                    MethodCallExpr callByIndex = new MethodCallExpr("getByIndex", callExpr);
                    callByIndex.setScope(new NameExpr(returnType.getMappedType().instantiationType()));
                    body.addStatement(new ReturnStmt(callByIndex));
                } else {
                    nativeBody.insert(0, "return (jlong)");
                    ObjectCreationExpr createObject = new ObjectCreationExpr();
                    createObject.setType(returnType.getMappedType().instantiationType());
                    createObject.addArgument(callExpr);
                    createObject.addArgument("false");
                    body.addStatement(new ReturnStmt(createObject));
                }
            }
        } else {
            body.addStatement(callExpr);
        }
        callMethod.setBody(body);

        patchNativeMethod.put(nativeMethod, nativeBody.toString());
    }
}
