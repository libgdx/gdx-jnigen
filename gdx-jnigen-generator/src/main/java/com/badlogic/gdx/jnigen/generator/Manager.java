package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.GlobalType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.StructType;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Manager {

    private static final Manager instance;

    static {
        instance = new Manager();
    }

    private final Map<String, StructType> structs = new HashMap<>();
    private final ArrayList<String> knownCTypes = new ArrayList<>();

    private final GlobalType globalType = new GlobalType();

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
        if (!knownCTypes.contains(name) && !name.equals("void"))
            knownCTypes.add(name);
    }

    public void addClosure(ClosureType closureType) {
        globalType.addClosure(closureType);
    }

    public String patchMethodNative(MethodDeclaration method, String nativeCode, String classString) {
        String lineToPatch = Arrays.stream(classString.split("\n"))
                .filter(line -> line.contains(method.toString())).findFirst().orElse(null);
        if (lineToPatch == null)
            throw new IllegalArgumentException("Failed to find native method: " + method.toString() + " in " + classString);

        String offset = lineToPatch.replace(method.toString(), "");
        String newLine = lineToPatch + "/*\n";
        newLine += Arrays.stream(nativeCode.split("\n"))
                .map(s -> offset + "\t" + s)
                .collect(Collectors.joining("\n"));

        newLine += "\n" + offset + "*/";
        return classString.replace(lineToPatch, newLine);
    }

    public void emit() {
        try {
            for (StructType structType : structs.values()) {
                HashMap<MethodDeclaration, String> toPatch = new HashMap<>();
                CompilationUnit cu = new CompilationUnit("test");
                structType.write(cu, toPatch);

                String classContent = cu.toString();
                for (Entry<MethodDeclaration, String> entry : toPatch.entrySet()) {
                    MethodDeclaration methodDeclaration = entry.getKey();
                    String s = entry.getValue();
                    classContent = patchMethodNative(methodDeclaration, s, classContent);
                }

                Files.write(Paths.get("gdx-jnigen-generator/src/test/java/test/" + structType.getName() + ".java"),
                        classContent.getBytes(StandardCharsets.UTF_8));

            }
            CompilationUnit cu = new CompilationUnit("test");
            globalType.write(cu);
            Files.write(Paths.get("gdx-jnigen-generator/src/test/java/test/Global.java"), cu.toString().getBytes(StandardCharsets.UTF_8));

            // FFI Type test
            CompilationUnit ffiTypeCU = new CompilationUnit("test");
            ffiTypeCU.addImport(Global.class);
            ClassOrInterfaceDeclaration ffiTypeClass = ffiTypeCU.addClass("FFITypes", Keyword.PUBLIC);
            MethodDeclaration getFFITypeMethod = ffiTypeClass.addMethod("getFFIType", Keyword.NATIVE, Keyword.PRIVATE, Keyword.STATIC);
            getFFITypeMethod.setBody(null).setType(long.class).addParameter(int.class, "id");
            StringBuilder nativeBody = new StringBuilder();
            nativeBody.append("switch(id) {\n");
            BlockStmt staticInit = ffiTypeClass.addStaticInitializer();

            for (int i = 0; i < knownCTypes.size(); i++) {
                String cType = knownCTypes.get(i);
                staticInit.addStatement("Global.registerCTypeFFIType(\"" + cType + "\", getFFIType(" + i + "));");
                nativeBody.append("\tcase ").append(i).append(":\n");
                nativeBody.append("\t\treturn reinterpret_cast<jlong>(GET_FFI_TYPE(").append(cType).append("));\n");
            }
            nativeBody.append("\tdefault:\n\t\treturn -1;\n");
            nativeBody.append("}\n");

            String ffiTypeString = ffiTypeCU.toString();
            ffiTypeString = patchMethodNative(getFFITypeMethod, nativeBody.toString(), ffiTypeString);

            Files.write(Paths.get("gdx-jnigen-generator/src/test/java/test/FFITypes.java"),
                    ffiTypeString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Manager getInstance() {
        return instance;
    }

}
