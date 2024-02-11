package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.pointer.DoublePointer;
import com.badlogic.gdx.jnigen.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.github.javaparser.ast.CompilationUnit;

public class PointerType implements MappedType {

    private TypeDefinition pointingTo;


    public PointerType(TypeDefinition pointingTo) {
        this.pointingTo = pointingTo;
    }

    private boolean isStructPointer() {
        return pointingTo.getTypeKind() == TypeKind.STRUCT;
    }

    private boolean isDoublePointer() {
        return pointingTo.getTypeKind() == TypeKind.DOUBLE;
    }

    private boolean isFloatPointer() {
        return pointingTo.getTypeKind() == TypeKind.FLOAT;
    }

    private boolean isIntPointer() {
        return pointingTo.getTypeKind().isPrimitive() && !isFloatPointer() && !isDoublePointer();
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
        else if (isStructPointer())
            return "StructPointer<" + pointingTo.getMappedType().abstractType() + ">";
        else
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
}
