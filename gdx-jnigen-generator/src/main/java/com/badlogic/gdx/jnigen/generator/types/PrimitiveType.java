package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;

public class PrimitiveType implements MappedType {

    private Class<?> javaRepresentation;

    private TypeDefinition definition;

    public PrimitiveType(TypeDefinition typeDefinition, Class<?> javaRepresentation) {
        this.definition = typeDefinition;
        this.javaRepresentation = javaRepresentation;
    }

    public static PrimitiveType fromTypeDefinition(TypeDefinition definition) {
        if (definition.getTypeKind().isSpecial())
            throw new IllegalArgumentException();
        switch (definition.getTypeKind()) {
        case VOID:
            return new PrimitiveType(definition, void.class);
        case BYTE:
            return new PrimitiveType(definition, byte.class);
        case PROMOTED_BYTE:
        case CHAR:
            return new PrimitiveType(definition, char.class);
        case SHORT:
            return new PrimitiveType(definition, short.class);
        case INT:
            return new PrimitiveType(definition, int.class);
        case PROMOTED_INT:
        case PROMOTED_LONG:
        case LONG:
            return new PrimitiveType(definition, long.class);
        case FLOAT:
            return new PrimitiveType(definition, float.class);
        case DOUBLE:
            return new PrimitiveType(definition, double.class);
        case BOOLEAN:
            return new PrimitiveType(definition, boolean.class);
        default:
            throw new IllegalArgumentException(definition.getTypeName() + " is not primitive.");
        }
    }

    @Override
    public String abstractType() {
        return javaRepresentation.getName();
    }

    @Override
    public String primitiveType() {
        return javaRepresentation.getName();
    }

    @Override
    public void importType(CompilationUnit cu) {
        // Unimportable
    }

    @Override
    public String classFile() {
        throw new IllegalArgumentException();
    }

    @Override
    public String packageName() {
        throw new IllegalArgumentException();
    }
}
