package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.Emitable;
import com.badlogic.gdx.jnigen.generator.types.StructType;
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

    public void startStruct(String name) {
        if (structs.containsKey(name))
            throw new IllegalArgumentException("Struct with name: " + name + " already exists.");
        structs.put(name, new StructType(name));
    }

    public void putStructField(String structName, String fieldName, TypeKind type) {
        if (!structs.containsKey(structName))
            throw new IllegalArgumentException("Struct with name: " + structName + " does not exists.");
        StructType struct = structs.get(structName);
        struct.addField(fieldName, type);
    }


    public void emit() {
        structs.values().forEach(structType -> {
            CompilationUnit cu = new CompilationUnit("test");
            structType.write(cu);
            try {
                Files.write(Paths.get("gdx-jnigen-generator/src/test/java/test"), cu.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Manager getInstance() {
        return instance;
    }

}
