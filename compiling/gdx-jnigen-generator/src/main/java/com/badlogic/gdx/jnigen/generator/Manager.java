package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.*;
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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Manager {

    public static final int VOID_FFI_ID = -2;
    public static final int POINTER_FFI_ID = -1;

    private static Manager instance;

    private final Manager rollBackManager;

    private final String parsedCHeader;
    private final String basePackage;
    private final ParseTarget target;

    public static void init(ParseTarget target, String parsedCHeader, String basePackage) {
        instance = new Manager(target, parsedCHeader, basePackage);
    }

    private final Map<String, StackElementType> stackElements = new HashMap<>();
    private final ArrayList<StackElementType> orderedStackElements = new ArrayList<>();
    private final Map<TypeDefinition, List<WritableClass>> nestedTypes = new HashMap<>();
    private final Map<String, EnumType> enums = new HashMap<>();
    private final HashMap<String, TypeDefinition> knownCTypes = new HashMap<>();

    private final HashMap<String, TypeDefinition> cTypeToJavaStringMapper = new HashMap<>();

    private final Map<String, String> typedefs = new HashMap<>();

    private final Map<String, MacroType> macros = new HashMap<>();

    private final GlobalType globalType;

    public Manager(ParseTarget target, String parsedCHeader, String basePackage) {
        this.rollBackManager = null;
        this.target = target;
        this.parsedCHeader = parsedCHeader;
        this.basePackage = basePackage;
        String[] segments = parsedCHeader.split("/");
        globalType = new GlobalType(JavaUtils.javarizeName(segments[segments.length - 1].split("\\.h")[0]));
    }

    public Manager(Manager rollBackManager) {
        this.rollBackManager = rollBackManager;
        this.target = rollBackManager.target;
        this.parsedCHeader = rollBackManager.parsedCHeader;
        this.basePackage = rollBackManager.basePackage;
        this.stackElements.putAll(rollBackManager.stackElements);
        this.orderedStackElements.addAll(rollBackManager.orderedStackElements);
        rollBackManager.nestedTypes.forEach((owner, nested) -> this.nestedTypes.put(owner, new ArrayList<>(nested)));
        this.enums.putAll(rollBackManager.enums);
        this.knownCTypes.putAll(rollBackManager.knownCTypes);
        this.cTypeToJavaStringMapper.putAll(rollBackManager.cTypeToJavaStringMapper);
        this.typedefs.putAll(rollBackManager.typedefs);
        this.macros.putAll(rollBackManager.macros);
        this.globalType = rollBackManager.globalType.duplicate();
    }

    public static void startNewManager() {
        instance = new Manager(instance);
    }

    public static void rollBack() {
        if (instance.rollBackManager == null)
            throw new IllegalStateException("Can't rollback, because no rollback point exists");
        instance = instance.rollBackManager;
    }

    public void unifyWith(List<Manager> passes) {
        passes.forEach(this::checkSameDeclarations);

        knownCTypes.forEach((name, own) -> {
            List<TypeDefinition> seenPerPass = new ArrayList<>();
            for (Manager pass : passes)
                seenPerPass.add(pass.knownCTypes.get(name));
            own.setTypeKind(TypeKind.resolve(name, seenPerPass));
        });
    }

    public void checkSameDeclarations(Manager manager) {
        DeclarationCheck.checkSame(this, manager);
    }

    public Set<String> getCTypeNames() {
        return knownCTypes.keySet();
    }

    public List<StackElementType> getStackElements() {
        return orderedStackElements;
    }

    public Map<String, EnumType> getEnums() {
        return enums;
    }

    public Map<String, MacroType> getMacros() {
        return macros;
    }

    public void addStackElement(StackElementType stackElementType, boolean registerGlobally) {
        String name = stackElementType.abstractType();
        if (registerGlobally) {
            if (stackElements.containsKey(name))
                throw new IllegalArgumentException("Struct with name: " + name + " already exists.");
            stackElements.put(name, stackElementType);
        }
        orderedStackElements.add(stackElementType);
        orderedStackElements.sort(Comparator.comparing(StackElementType::abstractType));
    }

    public void addNestedType(TypeDefinition owner, WritableClass nested) {
        nestedTypes.computeIfAbsent(owner, key -> new ArrayList<>()).add(nested);
    }

    public List<WritableClass> getNestedTypes(TypeDefinition owner) {
        return nestedTypes.getOrDefault(owner, Collections.emptyList());
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

    public TypeDefinition defineType(TypeKind typeKind, String typeName, long observedSize, long observedAlignment) {
        if (!typeKind.isPrimitive())
            return new TypeDefinition(typeKind, typeName, target.getPossibleTarget(), observedSize, observedAlignment);

        TypeDefinition definition = knownCTypes.get(typeName);
        if (definition == null) {
            definition = new TypeDefinition(typeKind, typeName, target.getPossibleTarget(), observedSize, observedAlignment);
            knownCTypes.put(typeName, definition);
            return definition;
        }
        if (definition.getTypeKind() != typeKind)
            throw new IllegalArgumentException("Type " + typeName + " has kind " + definition.getTypeKind() + ", but requested was " + typeKind);
        if (definition.getObservedSize() != observedSize)
            throw new IllegalArgumentException("Type " + typeName + " has size " + definition.getObservedSize() + ", but requested was " + observedSize);
        if (definition.getObservedAlignment() != observedAlignment)
            throw new IllegalArgumentException("Type " + typeName + " has alignment " + definition.getObservedAlignment() + ", but requested was " + observedAlignment);
        return definition;
    }

    public boolean hasCType(String name) {
        return knownCTypes.containsKey(name);
    }

    public TypeDefinition getCType(String name) {
        return knownCTypes.get(name);
    }

    public int getCTypeID(String name) {
        List<String> cTypes = knownCTypes.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        if (!cTypes.contains(name))
            throw new IllegalArgumentException("CType " + name + " is not registered.");
        return cTypes.indexOf(name);
    }

    public void registerMacro(MacroType macroType) {
        if (macros.containsKey(macroType.getName())) {
            if (!macros.get(macroType.getName()).getValue().equals(macroType.getValue()))
                throw new IllegalArgumentException("Macro with name " + macroType.getName() + " already exists, but has different value. Old: " + macros.get(macroType.getName()).getValue() + " != New: " + macroType.getValue());
        }
        macros.put(macroType.getName(), macroType);
    }

    public void registerCTypeMapping(String name, TypeDefinition javaRepresentation) {
        if (cTypeToJavaStringMapper.containsKey(name))
            throw new IllegalArgumentException("Already registered type " + name);
        cTypeToJavaStringMapper.put(name, javaRepresentation);
    }

    private TypeDefinition getCTypeMapping(String name) {
        if (cTypeToJavaStringMapper.containsKey(name))
            return cTypeToJavaStringMapper.get(name);
        if (typedefs.containsKey(name))
            return getCTypeMapping(typedefs.get(name));
        return null;
    }

    public TypeDefinition resolveCTypeMapping(String name) {
        if (!hasCTypeMapping(name))
            throw new IllegalArgumentException("No registered type " + name);
        return getCTypeMapping(name);
    }

    public boolean hasCTypeMapping(String name) {
        return getCTypeMapping(name) != null;
    }

    public boolean hasFunctionWithName(String name) {
        return globalType.getFunctions().stream().anyMatch(functionType -> functionType.getSignature().getName().equals(name));
    }

    public void addClosure(ClosureType closureType) {
        globalType.addClosure(closureType);
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

    public ParseTarget getTarget() {
        return target;
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

        String lineToPatch = Arrays.stream(classString.split("\r\n|\n"))
                .filter(line -> line.contains(methodString)).findFirst().orElse(null);
        if (lineToPatch == null)
            throw new IllegalArgumentException("Failed to find native method: " + method + " in " + classString);

        String offset = lineToPatch.replace(lineToPatch.trim(), "");
        String newLine = lineToPatch + "/*\n";
        newLine += Arrays.stream(nativeCode.split("\r\n|\n"))
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

    private void createStaticAsserts(List<String> assertBuilder, PossibleTarget target) {
        assertBuilder.add("#if " + target.condition());
        knownCTypes.forEach((name, typeKind) -> {
            assertBuilder.add("static_assert(sizeof(" + name + ") == " + typeKind.getMappedType().getSize(target) + ", \"Type " + name + " has unexpected size.\");");
            assertBuilder.add("static_assert(alignof(" + name + ") == " + typeKind.getMappedType().getAlignment(target) + ", \"Type " + name + " has unexpected alignment.\");");
        });
        for (StackElementType stackElementType : orderedStackElements) {
            if (stackElementType.isIncomplete())
                continue;
            String name = stackElementType.getDefinition().cTypeName();
            String label = stackElementType.classFile();
            assertBuilder.add("static_assert(sizeof(" + name + ") == " + stackElementType.getSize(target) + ", \"Type " + label + " has unexpected size.\");");
            assertBuilder.add("static_assert(alignof(" + name + ") == " + stackElementType.getAlignment(target) + ", \"Type " + label + " has unexpected alignment.\");");
            for (int i = 0; i < stackElementType.getFields().size(); i++) {
                assertBuilder.add("static_assert(offsetof(" + name + ", " + stackElementType.getFields().get(i).getName() + ") == " + stackElementType.getFieldOffset(i, target) + ", \"Type " + label + " has unexpected offset.\");");
            }
        }
        assertBuilder.add("#endif // " + target.condition());
    }

    public void emit(String basePath) {
        try {
            CompilationUnit globalCU = new CompilationUnit(globalType.packageName());
            CompilationUnit globalCUInternal = new CompilationUnit(globalType.packageName());
            ClassOrInterfaceDeclaration global = globalCU.addClass(globalType.abstractType(), Keyword.PUBLIC, Keyword.FINAL);
            ClassOrInterfaceDeclaration globalInternal = globalCUInternal.addClass(globalType.internalClassName(), Keyword.PUBLIC, Keyword.FINAL);


            for (StackElementType stackElementType : stackElements.values()) {
                CompilationUnit cu = new CompilationUnit(stackElementType.packageName());
                ClassOrInterfaceDeclaration declaration = stackElementType.generateClass();
                cu.addType(declaration);

                ClassOrInterfaceDeclaration declarationInternal = stackElementType.generateClassInternal();
                globalInternal.addMember(declarationInternal);
                stackElementType.write(cu, declaration, globalCUInternal, declarationInternal);

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

            globalType.write(globalCU, global, globalCUInternal, globalInternal, patchGlobalMethods);
            String globalFile = globalCU.toString();
            for (Entry<MethodDeclaration, String> entry : patchGlobalMethods.entrySet()) {
                MethodDeclaration methodDeclaration = entry.getKey();
                String s = entry.getValue();
                globalFile = patchMethodNative(methodDeclaration, s, globalFile);
            }
            Files.write(Paths.get(basePath + globalType.classFile().replace(".", "/") + ".java"), globalFile.getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get(basePath + globalType.classFile().replace(".", "/") + "_Internal.java"), globalCUInternal.toString().getBytes(StandardCharsets.UTF_8));

            // Macros
            CompilationUnit constantsCU = new CompilationUnit(basePackage);
            ClassOrInterfaceDeclaration constantsClass = constantsCU.addClass("Constants", Keyword.PUBLIC, Keyword.FINAL);

            macros.entrySet().stream()
                    .sorted(Entry.comparingByValue(Comparator.comparing(MacroType::getName)))
                    .forEach(macroType -> macroType.getValue().write(constantsCU, constantsClass));

            Files.write(Paths.get(basePath + basePackage.replace(".", "/") + "/Constants.java"), constantsCU.toString().getBytes(StandardCharsets.UTF_8));


            // FFI Type test
            CompilationUnit ffiTypeCU = new CompilationUnit(basePackage);
            ffiTypeCU.addImport(ClassNameConstants.CHANDLER_CLASS);
            ffiTypeCU.addImport(ClassNameConstants.CTYPEINFO_CLASS);
            ClassOrInterfaceDeclaration ffiTypeClass = ffiTypeCU.addClass("FFITypes", Keyword.PUBLIC);
            addJNIComment(ffiTypeClass, "#include <jnigen.h>", "#include <" + parsedCHeader + ">");

            List<String> assertBuilder = new ArrayList<>();

            assertBuilder.add("#if " + PossibleTarget.unsupportedPlatformCondition());
            assertBuilder.add("\t#error Unsupported OS/Platform");
            assertBuilder.add("#endif");
            assertBuilder.add("\n");

            for (PossibleTarget target : PossibleTarget.values()) {
                createStaticAsserts(assertBuilder, target);
                assertBuilder.add("\n");
            }

            knownCTypes.forEach((name, typeKind) -> {
                if (typeKind.getTypeKind() == TypeKind.NATIVE_BYTE)
                    return;
                if (typeKind.getTypeKind().isSigned())
                    assertBuilder.add("static_assert(IS_SIGNED_TYPE(" + name + "), \"Type " + name + " is expected signed.\");");
                else
                    assertBuilder.add("static_assert(IS_UNSIGNED_TYPE(" + name + "), \"Type " + name + " is expected unsigned.\");");
            });

            addJNIComment(ffiTypeClass, assertBuilder.toArray(new String[0]));

            ffiTypeClass.addMethod("init", Keyword.PUBLIC, Keyword.STATIC);

            ffiTypeCU.addImport(HashMap.class);
            ffiTypeClass.addFieldWithInitializer("HashMap<Integer, CTypeInfo>", "ffiIdMap", StaticJavaParser.parseExpression("new HashMap<>()"), Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

            ffiTypeClass.addMethod("getCTypeInfo", Keyword.PUBLIC, Keyword.STATIC)
                    .setType("CTypeInfo")
                    .addParameter(int.class, "id")
                    .createBody().addStatement("return ffiIdMap.get(id);");

            BlockComment getFFITypeNativeMethod = new BlockComment();
            ffiTypeClass.addOrphanComment(getFFITypeNativeMethod);

            String nativeGetFFIMethodName = "getNativeType";

            MethodDeclaration getFFITypeMethod = ffiTypeClass.addMethod("getNativeType", Keyword.PRIVATE, Keyword.NATIVE, Keyword.STATIC);
            getFFITypeMethod.setBody(null).setType(long.class).addParameter(int.class, "id");

            MethodDeclaration freeNativeTypeMethod = ffiTypeClass.addMethod("freeNativeType", Keyword.PRIVATE, Keyword.NATIVE, Keyword.STATIC);
            freeNativeTypeMethod.setBody(null).setType(void.class).addParameter(long.class, "nativeType");

            BlockStmt registerCTypeInfoBody = ffiTypeClass.addMethod("registerCTypeInfo", Keyword.PRIVATE, Keyword.STATIC)
                    .addParameter(int.class, "id")
                    .createBody();
            registerCTypeInfoBody.addStatement("long nativeType = getNativeType(id);");
            registerCTypeInfoBody.addStatement("ffiIdMap.put(id, CHandler.constructCTypeFromNativeType(nativeType));");
            registerCTypeInfoBody.addStatement("freeNativeType(nativeType);");

            StringBuilder ffiTypeNativeBody = new StringBuilder("JNI\n");
            ffiTypeNativeBody.append("static native_type* ").append(nativeGetFFIMethodName).append("(int id) {\n");
            ffiTypeNativeBody.append("native_type* nativeType = (native_type*)calloc(1, sizeof(native_type));\n");
            ffiTypeNativeBody.append("switch(id) {\n");
            BlockStmt staticInit = ffiTypeClass.addStaticInitializer();

            // ptr and void
            ffiTypeNativeBody.append("\tcase ").append(VOID_FFI_ID).append(":\n")
                    .append("\t\t").append("nativeType->type = VOID_TYPE;").append("\n")
                    .append("\t\treturn nativeType;\n");
            staticInit.addStatement("registerCTypeInfo(" + VOID_FFI_ID + ");");
            ffiTypeNativeBody.append("\tcase ").append(POINTER_FFI_ID).append(":\n")
                    .append("\t\t").append("nativeType->type = POINTER_TYPE;").append("\n")
                    .append("\t\treturn nativeType;\n");
            staticInit.addStatement("registerCTypeInfo(" + POINTER_FFI_ID + ");");

            List<String> cTypes = knownCTypes.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
            for (int i = 0; i < cTypes.size(); i++) {
                String cType = cTypes.get(i);
                staticInit.addStatement("registerCTypeInfo(" + i + ");");
                ffiTypeNativeBody.append("\tcase ").append(i).append(":\n");
                ffiTypeNativeBody.append("\t\tGET_NATIVE_TYPE(").append(cType).append(", nativeType);\n");
                ffiTypeNativeBody.append("\t\treturn nativeType;\n");
            }
            for (int i = 0; i < orderedStackElements.size(); i++) {
                int id = i + knownCTypes.size();
                StackElementType stackElementType = orderedStackElements.get(i);
                if (stackElementType.isOpaque())
                    continue;
                staticInit.addStatement("registerCTypeInfo(" + id + ");");
                ffiTypeNativeBody.append("\tcase ").append(id).append(":\n");
                ffiTypeNativeBody.append(stackElementType.getFFITypeBody(nativeGetFFIMethodName));
            }
            ffiTypeNativeBody.append("\tdefault:\n\t\tfree(nativeType);\n\t\treturn NULL;\n");
            ffiTypeNativeBody.append("\t}\n}\n");
            getFFITypeNativeMethod.setContent(ffiTypeNativeBody.toString());

            String jniMethodBody = "return reinterpret_cast<jlong>(" + nativeGetFFIMethodName + "(id));\n";

            String ffiTypeString = ffiTypeCU.toString();
            ffiTypeString = patchMethodNative(getFFITypeMethod, jniMethodBody, ffiTypeString);
            ffiTypeString = patchMethodNative(freeNativeTypeMethod, "free_native_type((native_type*)nativeType);\n", ffiTypeString);

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
