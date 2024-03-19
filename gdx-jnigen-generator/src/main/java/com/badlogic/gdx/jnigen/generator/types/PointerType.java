package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.pointer.DoublePointer;
import com.badlogic.gdx.jnigen.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.pointer.VoidPointer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

public class PointerType implements MappedType {

    private final TypeDefinition pointingTo;


    public PointerType(TypeDefinition pointingTo) {
        this.pointingTo = pointingTo;
    }

    public boolean isStackElementPointer() {
        return pointingTo.getTypeKind() == TypeKind.STACK_ELEMENT;
    }

    public boolean isDoublePointer() {
        return pointingTo.getTypeKind() == TypeKind.DOUBLE;
    }

    public boolean isFloatPointer() {
        return pointingTo.getTypeKind() == TypeKind.FLOAT;
    }

    public boolean isIntPointer() {
        return pointingTo.getTypeKind().isPrimitive() && !isFloatPointer() && !isDoublePointer();
    }

    public boolean isVoidPointer() {
        return pointingTo.getTypeKind() == TypeKind.VOID;
    }

    @Override
    public void importType(CompilationUnit cu) {
        if (isStackElementPointer())
            cu.addImport(StackElementPointer.class);
        else if (isFloatPointer())
            cu.addImport(FloatPointer.class);
        else if (isDoublePointer())
            cu.addImport(DoublePointer.class);
        else if (isIntPointer())
            cu.addImport(CSizedIntPointer.class);
        else if (isVoidPointer())
            cu.addImport(VoidPointer.class);
        else
            throw new IllegalArgumentException("Type " + pointingTo.getTypeKind() + " can't be pointerized");
        pointingTo.getMappedType().importType(cu);
    }

    @Override
    public String classFile() {
        throw new IllegalArgumentException();
    }

    @Override
    public String packageName() {
        throw new IllegalArgumentException();
    }

    @Override
    public String abstractType() {
        if (isIntPointer())
            return CSizedIntPointer.class.getSimpleName();
        if (isFloatPointer())
            return FloatPointer.class.getSimpleName();
        if (isDoublePointer())
            return DoublePointer.class.getSimpleName();
        if (isVoidPointer())
            return VoidPointer.class.getSimpleName();
        if (isStackElementPointer())
            return pointingTo.getMappedType().abstractType() + "." + pointingTo.getMappedType().abstractType() + "Pointer";

        throw new IllegalArgumentException();
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }

    @Override
    public MappedType asPointer() {
        throw new IllegalArgumentException("Not yet implemented");
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        return fromC(cRetrieved, false);
    }

    @Override
    public Expression fromC(Expression cRetrieved, boolean owned) {
        ObjectCreationExpr createObject = new ObjectCreationExpr();
        createObject.setType(instantiationType());
        createObject.addArgument(cRetrieved);
        createObject.addArgument(String.valueOf(owned));
        if (isIntPointer())
            createObject.addArgument(new StringLiteralExpr(pointingTo.getTypeName()));
        return createObject;
    }

    @Override
    public Expression toC(Expression cSend) {
        MethodCallExpr methodCallExpr = new MethodCallExpr("getPointer");
        methodCallExpr.setScope(cSend);
        return methodCallExpr;
    }

    @Override
    public int typeID() {
        return Manager.POINTER_FFI_ID;
    }

    @Override
    public Statement assertJava(Expression scope) {
        if (isIntPointer())
            return new ExpressionStmt(new MethodCallExpr("assertHasCTypeBacking").setScope(scope).addArgument(new StringLiteralExpr(pointingTo.getTypeName())));
        return MappedType.super.assertJava(scope);
    }
}
