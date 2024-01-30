package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.printer.PrettyPrinter;

public class ClosureType {

    private final String name;
    private final NamedType[] arguments;
    private final TypeDefinition returnType;

    public ClosureType(String name, TypeDefinition returnType, NamedType[] arguments) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public void write(ClassOrInterfaceDeclaration wrappingClass) {

        wrappingClass.tryAddImportToParentCompilationUnit(Closure.class);
        wrappingClass.tryAddImportToParentCompilationUnit(JavaTypeWrapper.class);
        ClassOrInterfaceDeclaration closureClass = new ClassOrInterfaceDeclaration()
                .setInterface(true)
                .addExtendedType(Closure.class)
                .setModifier(Keyword.PUBLIC, true)
                .setName(name);

        MethodDeclaration callMethod = closureClass.addMethod(name + "_call");
        callMethod.setBody(null);
        for (NamedType namedType : arguments) {
            callMethod.addParameter(namedType.getDefinition().resolveJavaClass(), namedType.getName());
        }
        callMethod.setType(returnType.resolveJavaClass());

        MethodDeclaration invokeMethod = closureClass.addMethod("invoke", Keyword.DEFAULT);
        invokeMethod.addParameter(JavaTypeWrapper[].class, "parameters");
        invokeMethod.addParameter(JavaTypeWrapper.class, "returnType");
        BlockStmt invokeBody = new BlockStmt();
        MethodCallExpr callExpr = new MethodCallExpr(callMethod.getNameAsString());
        for (int i = 0; i < arguments.length; i++) {
            NamedType type = arguments[i];
            TypeDefinition definition = type.getDefinition();
            callExpr.addArgument(definition.asCastExpression("parameters[" + i + "]"));
        }

        if (returnType.getTypeKind() == TypeKind.VOID) {
            invokeBody.addStatement(callExpr);
        } else {
            MethodCallExpr returnTypeSetMethodCall = new MethodCallExpr("returnType.setValue", callExpr);
            invokeBody.addStatement(returnTypeSetMethodCall);
        }

        invokeMethod.setBody(invokeBody);


        wrappingClass.addMember(closureClass);
    }
}
