package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
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
            cu.addImport(ClassNameConstants.FLOATPOINTER_CLASS);
        else if (isDoublePointer())
            cu.addImport(ClassNameConstants.DOUBLEPOINTER_CLASS);
        else if (isIntPointer())
            cu.addImport(ClassNameConstants.CSIZEDINTPOINTER_CLASS);
        else if (isVoidPointer())
            cu.addImport(ClassNameConstants.VOIDPOINTER_CLASS);
        else if (isEnumPointer())
            pointingTo.getMappedType().importType(cu);
        else if (isPointerPointer())
            cu.addImport(ClassNameConstants.POINTERPOINTER_CLASS);
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
            return "CSizedIntPointer";
        if (isFloatPointer())
            return "FloatPointer";
        if (isDoublePointer())
            return "DoublePointer";
        if (isVoidPointer())
            return "VoidPointer";
        if (isEnumPointer())
            return pointingTo.getMappedType().abstractType() + "." + pointingTo.getMappedType().abstractType() + "Pointer";
        if (isStackElementPointer())
            return pointingTo.getMappedType().abstractType() + "." + pointingTo.getMappedType().abstractType() + "Pointer";
        if (isPointerPointer())
            return "PointerPointer<" + pointingTo.getMappedType().abstractType() + ">";

        throw new IllegalArgumentException();
    }

    @Override
    public String instantiationType() {
        if (isPointerPointer())
            return "PointerPointer<>";
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
                createObject.addArgument(pointingTo.getMappedType().abstractType() + "::new");
            }

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
                return new ExpressionStmt(new MethodCallExpr("assertCTypeBacking").setScope(scope)
                        .addArgument(new StringLiteralExpr(pointingTo.rootType().getTypeName())));
            }
        }
        return MappedType.super.assertJava(scope);
    }
}
