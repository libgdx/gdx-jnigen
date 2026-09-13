package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.PossibleTarget;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;

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
        return pointingTo.getTypeKind().isStackElement();
    }

    @Override
    public void importType(CompilationUnit cu) {
        pointingTo.getMappedType().importPointerType(cu);
    }

    @Override
    public String pointerType() {
        return "PointerPointer<" + abstractType() + ">";
    }

    @Override
    public void importPointerType(CompilationUnit cu) {
        cu.addImport(ClassNameConstants.POINTERPOINTER_CLASS);
        importType(cu);
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
        return pointingTo.getMappedType().pointerType();
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

    public Expression getPointerPointerSupplier()
    {
        if (!isPointerPointer())
            throw new IllegalArgumentException("Can't get supplier for non PointerPointer type");
        PointerType childPointerType = (PointerType) pointingTo.getMappedType();
        if (childPointerType.isPointerPointer()) {
            LambdaExpr expr = new LambdaExpr();
            expr.setEnclosingParameters(true);
            Parameter peerPar = expr.addAndGetParameter(long.class, "peer" + pointingTo.getDepth());
            Parameter ownedPar = expr.addAndGetParameter(boolean.class, "owned" + pointingTo.getDepth());
            expr.setBody(new ExpressionStmt(pointingTo.getMappedType()
                    .fromC(peerPar.getNameAsExpression(), ownedPar.getNameAsExpression())));

            return expr;
        } else {
            return new MethodReferenceExpr()
                    .setScope(new NameExpr(pointingTo.getMappedType().abstractType()))
                    .setIdentifier("new");
        }
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
        if (isPointerPointer())
            createObject.addArgument(getPointerPointerSupplier());
        return createObject;
    }

    @Override
    public Expression fromCPooled(Expression cRetrieved, Expression pool) {
        if (isPointerPointer()) {
            return new MethodCallExpr("getPointerPointer")
                    .setScope(pool)
                    .addArgument(new ClassExpr().setType("PointerPointer"))
                    .addArgument(cRetrieved)
                    .addArgument(getPointerPointerSupplier());
        }
        return new MethodCallExpr("getPointing")
                .setScope(pool)
                .addArgument(new ClassExpr().setType(abstractType()))
                .addArgument(cRetrieved);
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
    public Expression writeToBufferPtr(Expression bufferPtr, Expression offset, Expression valueToWrite) {
        return new MethodCallExpr("setNativePointer", offset, valueToWrite).setScope(bufferPtr);
    }

    @Override
    public Expression readFromBufferPtr(Expression bufferPtr, Expression offset) {
        return new MethodCallExpr("getNativePointer", offset).setScope(bufferPtr);
    }

    @Override
    public int getSize(PossibleTarget target) {
        return target.is32Bit() ? 4 : 8;
    }
}
