package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.Emitable;
import com.badlogic.gdx.jnigen.generator.types.Global;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.StructType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.badlogic.gdx.jnigen.generator.types.TypeKind;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Manager {

    private static final Manager instance;

    static {
        instance = new Manager();
    }

    private final Map<String, StructType> structs = new HashMap<>();
    private final ArrayList<String> knownCTypes = new ArrayList<>();

    private final Global global = new Global();

    public void startStruct(String name) {
        if (structs.containsKey(name))
            throw new IllegalArgumentException("Struct with name: " + name + " already exists.");
        structs.put(name, new StructType(name));
    }

    public void putStructField(String structName, NamedType type) {
        if (!structs.containsKey(structName))
            throw new IllegalArgumentException("Struct with name: " + structName + " does not exists.");
        StructType struct = structs.get(structName);
        struct.addField(type);
    }

    public StructType getStruct(String structName) {
        if (!structs.containsKey(structName))
            throw new IllegalArgumentException("Struct with name: " + structName + " does not exists.");
        return structs.get(structName);
    }

    public void recordCType(String name) {
        if (!knownCTypes.contains(name))
            knownCTypes.add(name);
    }

    public void addClosure(ClosureType closureType) {
        global.addClosure(closureType);
    }

    public void emit() {
        structs.values().forEach(structType -> {
            CompilationUnit cu = new CompilationUnit("test");
            structType.write(cu);
            try {
                Files.write(Paths.get("gdx-jnigen-generator/src/test/java/test/" + structType.getName() + ".java"), cu.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        CompilationUnit cu = new CompilationUnit("test");
        global.write(cu);
        try {
            Files.write(Paths.get("gdx-jnigen-generator/src/test/java/test/Global.java"), cu.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Manager getInstance() {
        return instance;
    }

}
