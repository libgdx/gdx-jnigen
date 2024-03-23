package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.pointer.DoublePointer;
import com.badlogic.gdx.jnigen.pointer.EnumPointer;
import com.badlogic.gdx.jnigen.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.pointer.PointerPointer;
import com.badlogic.gdx.jnigen.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.pointer.VoidPointer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

public class PointerType implements MappedType {

    private final TypeDefinition pointingTo;


    public PointerType(TypeDefinition pointingTo) {
        this.pointingTo = pointingTo;
    }

    public boolean isPointerPointer() {
        return pointingTo.getTypeKind() == TypeKind.POINTER;
    }

    public boolean isEnumPointer() {
        return pointingTo.getTypeKind() == TypeKind.ENUM;
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
        else if (isEnumPointer())
            cu.addImport(EnumPointer.class);
        else if (isPointerPointer())
            cu.addImport(PointerPointer.class);
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
        if (isEnumPointer())
            return EnumPointer.class.getSimpleName() + "<" + pointingTo.getMappedType().abstractType() + ">";
        if (isStackElementPointer())
            return pointingTo.getMappedType().abstractType() + "." + pointingTo.getMappedType().abstractType() + "Pointer";
        if (isPointerPointer())
            return PointerPointer.class.getSimpleName() + "<" + pointingTo.getMappedType().abstractType() + ">";

        throw new IllegalArgumentException();
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
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
        if (isEnumPointer())
            createObject.addArgument(pointingTo.getMappedType().abstractType() + "::getByIndex");
        if (isPointerPointer()) {
            // TODO: 23.03.24 Look whether this should get improved
            PointerType root = new PointerType(pointingTo.rootType());
            if (root.isPointerPointer())
                throw new IllegalArgumentException();

            if (root.isEnumPointer() || root.isIntPointer()) {
                MethodCallExpr methodCallExpr = new MethodCallExpr("getPointerPointerSupplier");
                if (root.isIntPointer()) {
                    methodCallExpr.setScope(new NameExpr(root.abstractType()));
                    methodCallExpr.addArgument(new StringLiteralExpr(root.pointingTo.getTypeName()));
                } else {
                    methodCallExpr.setScope(new NameExpr("EnumPointer"));
                    methodCallExpr.addArgument(root.pointingTo.getMappedType().abstractType() + "::getByIndex");
                }
                createObject.addArgument(methodCallExpr);
            } else {
                createObject.addArgument(root.abstractType() + "::new");
            }
            createObject.addArgument(String.valueOf(pointingTo.getDepth()));
        }
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
        if (isPointerPointer())
            return new ExpressionStmt(new MethodCallExpr("assertDepth").setScope(scope).addArgument(new IntegerLiteralExpr(String.valueOf(pointingTo.getDepth()))));
        return MappedType.super.assertJava(scope);
    }
}
