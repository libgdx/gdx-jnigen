package com.badlogic.gdx.jnigen.generator.types;

public class FunctionSignature {

    private final String name;
    private final NamedType[] arguments;
    private final TypeDefinition returnType;

    public FunctionSignature(String name, NamedType[] arguments, TypeDefinition returnType) {
        this.name = name;
        this.arguments = arguments;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public NamedType[] getArguments() {
        return arguments;
    }

    public TypeDefinition getReturnType() {
        return returnType;
    }
}
