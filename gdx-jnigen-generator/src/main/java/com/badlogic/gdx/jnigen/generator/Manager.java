package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.ffi.FFITypes;
import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.FunctionType;
import com.badlogic.gdx.jnigen.generator.types.GlobalType;
import com.badlogic.gdx.jnigen.generator.types.MappedType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.PointerType;
import com.badlogic.gdx.jnigen.generator.types.StructType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.badlogic.gdx.jnigen.generator.types.TypeKind;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration.ConfigOption;

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

    private static Manager instance;


    private final String parsedCHeader;

    public static void init(String parsedCHeader) {
        instance = new Manager(parsedCHeader);
    }

    private final Map<String, StructType> structs = new HashMap<>();
    private final ArrayList<String> knownCTypes = new ArrayList<>();

    private final HashMap<String, MappedType> cTypeToJavaStringMapper = new HashMap<>();

    private final GlobalType globalType;

    public Manager(String parsedCHeader) {
        this.parsedCHeader = parsedCHeader;
        globalType = new GlobalType(JavaUtils.javarizeName(parsedCHeader.split("\\.h")[0]));
    }


    public void startStruct(String name) {
        if (structs.containsKey(name))
            throw new IllegalArgumentException("Struct with name: " + name + " already exists.");
        StructType structType = new StructType(name);
        structs.put(name, structType);
        registerCTypeMapping(name, structType);
        registerCTypeMapping(name + " *", new PointerType(name + ".Pointer", new TypeDefinition(TypeKind.POINTER, name + " *"), structType));
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

    public ClosureType getClosure(String closureType) {
        return globalType.getClosure(closureType);
    }

    public void recordCType(String name) {
        if (!knownCTypes.contains(name) && !name.equals("void"))
            knownCTypes.add(name);
    }

    public void registerCTypeMapping(String name, MappedType javaRepresentation) {
        if (cTypeToJavaStringMapper.containsKey(name))
            throw new IllegalArgumentException("Already registered type " + name);
        cTypeToJavaStringMapper.put(name, javaRepresentation);
    }

    public MappedType resolveCTypeMapping(String name) {
        if (!cTypeToJavaStringMapper.containsKey(name))
            throw new IllegalArgumentException("No registered type " + name);
        return cTypeToJavaStringMapper.get(name);
    }

    public boolean hasCTypeMapping(String name) {
        return cTypeToJavaStringMapper.containsKey(name);
    }

    public void addClosure(ClosureType closureType) {
        globalType.addClosure(closureType);
        registerCTypeMapping(closureType.getName(), closureType);
    }

    public void addFunction(FunctionType functionType) {
        globalType.addFunction(functionType);
    }

    public String getParsedCHeader() {
        return parsedCHeader;
    }

    public String patchMethodNative(MethodDeclaration method, String nativeCode, String classString) {
        String methodString = method.toString(new DefaultPrinterConfiguration().removeOption(new DefaultConfigurationOption(
                ConfigOption.PRINT_COMMENTS)));

        String lineToPatch = Arrays.stream(classString.split("\n"))
                .filter(line -> line.contains(methodString)).findFirst().orElse(null);
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

    private void addJNIComment(ClassOrInterfaceDeclaration toAddTo, String... content) {
        StringBuilder result = new StringBuilder("JNI\n");
        for (String line : content) {
            result.append("\t\t").append(line).append("\n");
        }
        toAddTo.addOrphanComment(new BlockComment(result.toString()));
    }

    public void emit(String basePath, String basePackage) {
        try {
            String packagePath = basePath + "/" + basePackage.replace(".", "/");
            Files.createDirectories(Paths.get(packagePath + "/structs/"));
            for (StructType structType : structs.values()) {
                HashMap<MethodDeclaration, String> toPatch = new HashMap<>();
                CompilationUnit cu = new CompilationUnit(basePackage + ".structs");
                structType.write(cu, toPatch);

                String classContent = cu.toString();
                for (Entry<MethodDeclaration, String> entry : toPatch.entrySet()) {
                    MethodDeclaration methodDeclaration = entry.getKey();
                    String s = entry.getValue();
                    classContent = patchMethodNative(methodDeclaration, s, classContent);
                }

                Files.write(Paths.get(packagePath + "/structs/" + structType.getName() + ".java"),
                        classContent.getBytes(StandardCharsets.UTF_8));

            }
            HashMap<MethodDeclaration, String> patchGlobalMethods = new HashMap<>();
            CompilationUnit cu = new CompilationUnit(basePackage);
            //TODO: Massive hack, every mapped type should expose, how it gets imported. Than, when we emit the functions, everything used gets imported
            structs.values().forEach(structType -> cu.addImport(basePackage + ".structs." + structType.getName()));
            cu.addImport(StructPointer.class);

            globalType.write(cu, patchGlobalMethods);
            String globalFile = cu.toString();
            for (Entry<MethodDeclaration, String> entry : patchGlobalMethods.entrySet()) {
                MethodDeclaration methodDeclaration = entry.getKey();
                String s = entry.getValue();
                globalFile = patchMethodNative(methodDeclaration, s, globalFile);
            }
            Files.write(Paths.get(packagePath + "/" + globalType.getGlobalName() + ".java"), globalFile.getBytes(StandardCharsets.UTF_8));

            // FFI Type test
            CompilationUnit ffiTypeCU = new CompilationUnit(basePackage);
            ffiTypeCU.addImport(CHandler.class);
            ClassOrInterfaceDeclaration ffiTypeClass = ffiTypeCU.addClass("FFITypes", Keyword.PUBLIC);
            addJNIComment(ffiTypeClass, "#include <jnigen.h>");
            ffiTypeClass.addMethod("init", Keyword.PUBLIC, Keyword.STATIC);

            MethodDeclaration getFFITypeMethod = ffiTypeClass.addMethod("getFFIType", Keyword.NATIVE, Keyword.PRIVATE, Keyword.STATIC);
            getFFITypeMethod.setBody(null).setType(long.class).addParameter(int.class, "id");
            StringBuilder nativeBody = new StringBuilder();
            nativeBody.append("switch(id) {\n");
            BlockStmt staticInit = ffiTypeClass.addStaticInitializer();

            for (int i = 0; i < knownCTypes.size(); i++) {
                String cType = knownCTypes.get(i);
                staticInit.addStatement("CHandler.registerCTypeFFIType(\"" + cType + "\", getFFIType(" + i + "));");
                nativeBody.append("\tcase ").append(i).append(":\n");
                nativeBody.append("\t\treturn reinterpret_cast<jlong>(GET_FFI_TYPE(").append(cType).append("));\n");
            }
            nativeBody.append("\tdefault:\n\t\treturn -1;\n");
            nativeBody.append("}\n");

            String ffiTypeString = ffiTypeCU.toString();
            ffiTypeString = patchMethodNative(getFFITypeMethod, nativeBody.toString(), ffiTypeString);

            Files.write(Paths.get(packagePath + "/FFITypes.java"),
                    ffiTypeString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Manager getInstance() {
        return instance;
    }

}
