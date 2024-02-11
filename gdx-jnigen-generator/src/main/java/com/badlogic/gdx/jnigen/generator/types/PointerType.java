package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.pointer.DoublePointer;
import com.badlogic.gdx.jnigen.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.badlogic.gdx.jnigen.pointer.VoidPointer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;

public class PointerType implements MappedType {

    private TypeDefinition pointingTo;


    public PointerType(TypeDefinition pointingTo) {
        this.pointingTo = pointingTo;
    }

    public boolean isStructPointer() {
        return pointingTo.getTypeKind() == TypeKind.STRUCT;
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
        if (isStructPointer())
            cu.addImport(StructPointer.class);
        else if (isFloatPointer())
            cu.addImport(FloatPointer.class);
        else if (isDoublePointer())
            cu.addImport(FloatPointer.class);
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
    public String instantiationType() {
        if (isStructPointer())
            return pointingTo.getMappedType().abstractType() + ".Pointer";
        else
            return abstractType();
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
        if (isStructPointer())
            return "StructPointer<" + pointingTo.getMappedType().abstractType() + ">";

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
        ObjectCreationExpr createObject = new ObjectCreationExpr();
        createObject.setType(instantiationType());
        createObject.addArgument(cRetrieved);
        createObject.addArgument("false");
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
}
