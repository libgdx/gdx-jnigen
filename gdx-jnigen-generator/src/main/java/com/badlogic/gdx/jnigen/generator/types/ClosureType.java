package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.pointer.CType;
import com.badlogic.gdx.jnigen.pointer.Signed;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

public class ClosureType implements MappedType {

    private final String name;
    private final NamedType[] arguments;
    private final TypeDefinition returnType;

    public ClosureType(String name, TypeDefinition returnType, NamedType[] arguments) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public void write(CompilationUnit cu, ClassOrInterfaceDeclaration wrappingClass) {

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
            namedType.getDefinition().getMappedType().importType(cu);
            Parameter parameter = callMethod.addAndGetParameter(namedType.getDefinition().getMappedType().instantiationType(), namedType.getName());
            if (namedType.getDefinition().getTypeKind().isPrimitive()) {
                parameter.addAndGetAnnotation(CType.class).addPair("value", "\"" + namedType.getDefinition().getTypeName() + "\"");
                if (namedType.getDefinition().getTypeKind().isSigned())
                    parameter.addAnnotation(Signed.class);
            }
        }
        returnType.getMappedType().importType(cu);
        callMethod.setType(returnType.getMappedType().abstractType());
        if (returnType.getTypeKind().isPrimitive()) {
            callMethod.addAndGetAnnotation(CType.class).addPair("value", "\"" + returnType.getTypeName() + "\"");
            if (returnType.getTypeKind().isSigned())
                callMethod.addAnnotation(Signed.class);
        }

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

    public String getName() {
        return name;
    }

    @Override
    public String abstractType() {
        return "ClosureObject<" + name + ">";
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(ClosureObject.class);
        cu.addImport(classFile() + "." + name);
    }

    @Override
    public String classFile() {
        return Manager.getInstance().getGlobalType().classFile();
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage();
    }
}
