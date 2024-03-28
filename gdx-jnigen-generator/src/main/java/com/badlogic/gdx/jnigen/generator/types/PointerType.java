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
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
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
            pointingTo.getMappedType().importType(cu);
        else if (isFloatPointer())
            cu.addImport(FloatPointer.class);
        else if (isDoublePointer())
            cu.addImport(DoublePointer.class);
        else if (isIntPointer())
            cu.addImport(CSizedIntPointer.class);
        else if (isVoidPointer())
            cu.addImport(VoidPointer.class);
        else if (isEnumPointer())
            pointingTo.getMappedType().importType(cu);
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
            return pointingTo.getMappedType().abstractType() + "." + pointingTo.getMappedType().abstractType() + "Pointer";
        if (isStackElementPointer())
            return pointingTo.getMappedType().abstractType() + "." + pointingTo.getMappedType().abstractType() + "Pointer";
        if (isPointerPointer())
            return PointerPointer.class.getSimpleName() + "<" + pointingTo.getMappedType().abstractType() + ">";

        throw new IllegalArgumentException();
    }

    @Override
    public String instantiationType() {
        if (isPointerPointer())
            return PointerPointer.class.getSimpleName() + "<>";
        return MappedType.super.instantiationType();
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        return fromC(cRetrieved, new BooleanLiteralExpr(false));
    }

    @Override
    public Expression fromC(Expression cRetrieved, Expression owned) {
        ObjectCreationExpr createObject = new ObjectCreationExpr();
        createObject.setType(instantiationType());
        createObject.addArgument(cRetrieved);
        createObject.addArgument(String.valueOf(owned));
        if (isIntPointer())
            createObject.addArgument(new StringLiteralExpr(pointingTo.getTypeName()));
        if (isPointerPointer()) {
            PointerType childPointerType = (PointerType) pointingTo.getMappedType();
            if (childPointerType.isIntPointer() || childPointerType.isPointerPointer()) {
                LambdaExpr expr = new LambdaExpr();
                expr.setEnclosingParameters(true);
                Parameter peerPar = expr.addAndGetParameter(long.class, "peer" + pointingTo.getDepth());
                Parameter ownedPar = expr.addAndGetParameter(boolean.class, "owned" + pointingTo.getDepth());
                expr.setBody(new ExpressionStmt(pointingTo.getMappedType()
                        .fromC(peerPar.getNameAsExpression(), ownedPar.getNameAsExpression())));

                createObject.addArgument(expr);
            } else {
                createObject.addArgument(pointingTo.getMappedType().instantiationType() + "::new");
            }
            createObject.addArgument(String.valueOf(pointingTo.getDepth()));

            PointerType root = new PointerType(pointingTo.rootType());
            if (root.isIntPointer()) {
                MethodCallExpr setCType = new MethodCallExpr(createObject, "setBackingCType");
                setCType.addArgument(new StringLiteralExpr(pointingTo.rootType().getTypeName()));
                return setCType;
            }
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
        if (isPointerPointer()) {
            PointerType root = new PointerType(pointingTo.rootType());

            if (root.isIntPointer()) {
                return new ExpressionStmt(new MethodCallExpr("assertCTypeBackingAndDepth").setScope(scope)
                        .addArgument(new StringLiteralExpr(pointingTo.rootType().getTypeName()))
                        .addArgument(new IntegerLiteralExpr(String.valueOf(pointingTo.getDepth()))));
            }
            return new ExpressionStmt(new MethodCallExpr("assertDepth").setScope(scope)
                    .addArgument(new IntegerLiteralExpr(String.valueOf(pointingTo.getDepth()))));
        }
        return MappedType.super.assertJava(scope);
    }
}
