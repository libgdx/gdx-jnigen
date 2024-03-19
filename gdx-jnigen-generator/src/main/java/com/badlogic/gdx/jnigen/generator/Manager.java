package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.EnumType;
import com.badlogic.gdx.jnigen.generator.types.FunctionType;
import com.badlogic.gdx.jnigen.generator.types.GlobalType;
import com.badlogic.gdx.jnigen.generator.types.MappedType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.StackElementType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.github.javaparser.StaticJavaParser;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Manager {

    public static final int VOID_FFI_ID = -2;
    public static final int POINTER_FFI_ID = -1;

    private static Manager instance;

    private final String parsedCHeader;
    private final String basePackage;

    public static void init(String parsedCHeader, String basePackage) {
        instance = new Manager(parsedCHeader, basePackage);
    }

    private final Map<String, StackElementType> stackElements = new HashMap<>();
    private final ArrayList<StackElementType> orderedStackElements = new ArrayList<>();
    private final Map<String, EnumType> enums = new HashMap<>();
    private final ArrayList<String> knownCTypes = new ArrayList<>();

    private final HashMap<String, MappedType> cTypeToJavaStringMapper = new HashMap<>();

    private final Map<String, String> typedefs = new HashMap<>();

    private final GlobalType globalType;

    public Manager(String parsedCHeader, String basePackage) {
        this.parsedCHeader = parsedCHeader;
        this.basePackage = basePackage;
        String[] segments = parsedCHeader.split("/");
        globalType = new GlobalType(JavaUtils.javarizeName(segments[segments.length - 1].split("\\.h")[0]));
    }

    public void addStackElement(StackElementType stackElementType) {
        String name = stackElementType.abstractType();
        if (stackElements.containsKey(name))
            return; // TODO: 19.02.24 FIGURE OUT WHY THIS CAN HAPPEN?????
        stackElements.put(name, stackElementType);
        orderedStackElements.add(stackElementType);
        orderedStackElements.sort(Comparator.comparing(StackElementType::abstractType));
    }

    public int getStackElementID(StackElementType stackElementType) {
        return orderedStackElements.indexOf(stackElementType) + knownCTypes.size();
    }

    public void addEnum(EnumType enumType) {
        String name = enumType.abstractType();
        if (enums.containsKey(name))
            throw new IllegalArgumentException("Enum with name: " + name + " already exists.");
        enums.put(name, enumType);
    }

    public void recordCType(String name) {
        if (!knownCTypes.contains(name))
            knownCTypes.add(name);
        knownCTypes.sort(Comparator.naturalOrder());
    }

    public int getCTypeID(String name) {
        if (!knownCTypes.contains(name))
            throw new IllegalArgumentException("CType " + name + " is not registered.");
        return knownCTypes.indexOf(name);
    }

    public void registerCTypeMapping(String name, MappedType javaRepresentation) {
        if (cTypeToJavaStringMapper.containsKey(name))
            throw new IllegalArgumentException("Already registered type " + name);
        cTypeToJavaStringMapper.put(name, javaRepresentation);
    }

    private MappedType getCTypeMapping(String name) {
        if (cTypeToJavaStringMapper.containsKey(name))
            return cTypeToJavaStringMapper.get(name);
        if (typedefs.containsKey(name))
            return getCTypeMapping(typedefs.get(name));
        return null;
    }

    public MappedType resolveCTypeMapping(String name) {
        if (!hasCTypeMapping(name))
            throw new IllegalArgumentException("No registered type " + name);
        return getCTypeMapping(name);
    }

    public boolean hasCTypeMapping(String name) {
        return getCTypeMapping(name) != null;
    }

    public void addClosure(ClosureType closureType) {
        globalType.addClosure(closureType);
        Manager.getInstance().registerCTypeMapping(closureType.getName(), closureType);
        Manager.getInstance().registerCTypeMapping(closureType.getName() + " *", closureType); // TODO: SOlve closure parsing better
    }

    public void registerTypeDef(String typedef, String name) {
        if (typedefs.containsKey(typedef)) {
            if (!typedefs.get(typedef).equals(name))
                throw new IllegalArgumentException("Typedef " + typedef + " already exists");
        }
        typedefs.put(typedef, name);
    }

    public void addFunction(FunctionType functionType) {
        globalType.addFunction(functionType);
    }

    public GlobalType getGlobalType() {
        return globalType;
    }

    public String getParsedCHeader() {
        return parsedCHeader;
    }

    public String getBasePackage() {
        return basePackage;
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

    public void emit(String basePath) {
        try {
            for (StackElementType stackElementType : stackElements.values()) {
                CompilationUnit cu = new CompilationUnit(stackElementType.packageName());
                stackElementType.write(cu);

                String classContent = cu.toString();

                String fullPath = basePath + stackElementType.classFile().replace(".", "/") + ".java";
                Path structPath = Paths.get(fullPath);
                structPath.getParent().toFile().mkdirs();
                Files.write(structPath, classContent.getBytes(StandardCharsets.UTF_8));
            }

            for (EnumType enumType : enums.values()) {
                CompilationUnit cu = new CompilationUnit(enumType.packageName());
                enumType.write(cu);
                String fullPath = basePath + enumType.classFile().replace(".", "/") + ".java";
                Path structPath = Paths.get(fullPath);
                structPath.getParent().toFile().mkdirs();
                Files.write(structPath, cu.toString().getBytes(StandardCharsets.UTF_8));
            }

            HashMap<MethodDeclaration, String> patchGlobalMethods = new HashMap<>();
            CompilationUnit globalCU = new CompilationUnit(globalType.packageName());

            globalType.write(globalCU, patchGlobalMethods);
            String globalFile = globalCU.toString();
            for (Entry<MethodDeclaration, String> entry : patchGlobalMethods.entrySet()) {
                MethodDeclaration methodDeclaration = entry.getKey();
                String s = entry.getValue();
                globalFile = patchMethodNative(methodDeclaration, s, globalFile);
            }
            Files.write(Paths.get(basePath + globalType.classFile().replace(".", "/") + ".java"), globalFile.getBytes(StandardCharsets.UTF_8));

            // FFI Type test
            CompilationUnit ffiTypeCU = new CompilationUnit(basePackage);
            ffiTypeCU.addImport(CHandler.class);
            ClassOrInterfaceDeclaration ffiTypeClass = ffiTypeCU.addClass("FFITypes", Keyword.PUBLIC);
            addJNIComment(ffiTypeClass, "#include <jnigen.h>", "#include <" + parsedCHeader + ">");
            ffiTypeClass.addMethod("init", Keyword.PUBLIC, Keyword.STATIC);

            ffiTypeCU.addImport(HashMap.class);
            ffiTypeClass.addFieldWithInitializer("HashMap<Integer, CTypeInfo>", "ffiIdMap", StaticJavaParser.parseExpression("new HashMap<>()"), Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

            ffiTypeClass.addMethod("getCTypeInfo", Keyword.PUBLIC, Keyword.STATIC)
                    .setType(CTypeInfo.class)
                    .addParameter(int.class, "id")
                    .createBody().addStatement("return ffiIdMap.get(id);");

            BlockComment getFFITypeNativeMethod = new BlockComment();
            ffiTypeClass.addOrphanComment(getFFITypeNativeMethod);

            String nativeGetFFIMethodName = "getFFIType";

            MethodDeclaration getFFITypeMethod = ffiTypeClass.addMethod("getFFITypeNative", Keyword.PRIVATE, Keyword.NATIVE, Keyword.STATIC);
            getFFITypeMethod.setBody(null).setType(long.class).addParameter(int.class, "id");
            StringBuilder ffiTypeNativeBody = new StringBuilder("JNI\n");
            ffiTypeNativeBody.append("static ffi_type* ").append(nativeGetFFIMethodName).append("(int id) {\n");
            ffiTypeNativeBody.append("switch(id) {\n");
            BlockStmt staticInit = ffiTypeClass.addStaticInitializer();

            // ptr and void
            ffiTypeNativeBody.append("\tcase ").append(VOID_FFI_ID).append(":\n").append("\t\treturn &ffi_type_void;\n");
            staticInit.addStatement("ffiIdMap.put(" + VOID_FFI_ID + ", CHandler.constructCTypeFromFFIType(\"void\", getFFITypeNative(" + VOID_FFI_ID + ")));");
            ffiTypeNativeBody.append("\tcase ").append(POINTER_FFI_ID).append(":\n").append("\t\treturn &ffi_type_pointer;\n");
            staticInit.addStatement("ffiIdMap.put(" + POINTER_FFI_ID + ", CHandler.constructCTypeFromFFIType(\"void*\", getFFITypeNative(" + POINTER_FFI_ID + ")));");

            for (int i = 0; i < knownCTypes.size(); i++) {
                String cType = knownCTypes.get(i);
                staticInit.addStatement("ffiIdMap.put(" + i + ", CHandler.constructCTypeFromFFIType(\"" + cType + "\", getFFITypeNative(" + i + ")));");
                staticInit.addStatement("CHandler.registerCType(ffiIdMap.get(" + i + "));");
                ffiTypeNativeBody.append("\tcase ").append(i).append(":\n");
                ffiTypeNativeBody.append("\t\treturn GET_FFI_TYPE(").append(cType).append(");\n");
            }
            for (int i = 0; i < orderedStackElements.size(); i++) {
                int id = i + knownCTypes.size();
                StackElementType stackElementType = orderedStackElements.get(i);
                staticInit.addStatement("ffiIdMap.put(" + id + ", CHandler.constructStackElementCTypeFromFFIType(null, getFFITypeNative(" + id + "), " + stackElementType.isStruct() + "));");
                ffiTypeNativeBody.append("\tcase ").append(id).append(":\n");
                ffiTypeNativeBody.append(stackElementType.getFFITypeBody(nativeGetFFIMethodName));
            }
            ffiTypeNativeBody.append("\tdefault:\n\t\treturn NULL;\n");
            ffiTypeNativeBody.append("\t}\n}\n");
            getFFITypeNativeMethod.setContent(ffiTypeNativeBody.toString());

            String jniMethodBody = "return reinterpret_cast<jlong>(" + nativeGetFFIMethodName + "(id));\n";

            String ffiTypeString = ffiTypeCU.toString();
            ffiTypeString = patchMethodNative(getFFITypeMethod, jniMethodBody, ffiTypeString);

            Files.write(Paths.get(basePath + basePackage.replace(".", "/") + "/FFITypes.java"),
                    ffiTypeString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Manager getInstance() {
        return instance;
    }

}
