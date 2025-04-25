package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.Manager;
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

    private String findJavaPointerForIntPointer() {
        if (!isIntPointer())
            throw new IllegalArgumentException("Trying to find int pointer for non-integer pointer type");
        switch (pointingTo.getTypeKind()) {
        case NATIVE_BYTE:
            return "BytePointer";
        case SIGNED_BYTE:
            return "SBytePointer";
        case BOOLEAN:
        case PROMOTED_BYTE:
            return "UBytePointer";
        case SHORT:
            return "SShortPointer";
        case CHAR:
            return "UShortPointer";
        case INT:
            return "SIntPointer";
        case PROMOTED_INT:
            return "UIntPointer";
        case LONG:
            return "SLongPointer";
        case PROMOTED_LONG:
            return "ULongPointer";
        case LONG_LONG:
            return "SInt64Pointer";
        case PROMOTED_LONG_LONG:
            return "UInt64Pointer";
        default:
            throw new IllegalArgumentException("Type " + this + " is not a primitive type");
        }
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
            cu.addImport("com.badlogic.gdx.jnigen.runtime.pointer.integer." + abstractType());
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
            return findJavaPointerForIntPointer();
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

    private Expression getPointerPointerSupplier()
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
    public int getSize(boolean is32Bit, boolean isWin) {
        return is32Bit ? 4 : 8;
    }
}
