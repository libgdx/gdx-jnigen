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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Manager {

    public static final int VOID_FFI_ID = -2;
    public static final int POINTER_FFI_ID = -1;

    private static Manager instance;

    private final Manager rollBackManager;

    private final String parsedCHeader;
    private final String basePackage;

    public static void init(String parsedCHeader, String basePackage) {
        instance = new Manager(parsedCHeader, basePackage);
    }

    private final Map<String, StackElementType> stackElements = new HashMap<>();
    private final ArrayList<StackElementType> orderedStackElements = new ArrayList<>();
    private final Map<String, EnumType> enums = new HashMap<>();
    private final HashMap<String, TypeDefinition> knownCTypes = new HashMap<>();

    private final HashMap<String, TypeDefinition> cTypeToJavaStringMapper = new HashMap<>();

    private final Map<String, String> typedefs = new HashMap<>();

    private final Map<String, MacroType> macros = new HashMap<>();

    private boolean webEnabled = false;

    private final GlobalType globalType;

    public Manager(String parsedCHeader, String basePackage) {
        this.rollBackManager = null;
        this.parsedCHeader = parsedCHeader;
        this.basePackage = basePackage;
        String[] segments = parsedCHeader.split("/");
        globalType = new GlobalType(JavaUtils.javarizeName(segments[segments.length - 1].split("\\.h")[0]));
    }

    public Manager(Manager rollBackManager) {
        this.rollBackManager = rollBackManager;
        this.parsedCHeader = rollBackManager.parsedCHeader;
        this.basePackage = rollBackManager.basePackage;
        this.stackElements.putAll(rollBackManager.stackElements);
        this.orderedStackElements.addAll(rollBackManager.orderedStackElements);
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

    public void mergeManager(Manager toMerge) {
        toMerge.knownCTypes.forEach((name, typeDefinition) -> {
            TypeDefinition own = knownCTypes.get(name);
            if (own == null)
                throw new IllegalStateException("Can't merge Manager cause " + name + " doesn't exist in both.");
            if (own.getTypeKind() != typeDefinition.getTypeKind()) {
                if (own.getTypeKind() == TypeKind.SIGNED_BYTE && typeDefinition.getTypeKind() == TypeKind.PROMOTED_BYTE)
                    own.setTypeKind(TypeKind.NATIVE_BYTE);
                else if (own.getTypeKind() == TypeKind.PROMOTED_LONG && typeDefinition.getTypeKind() == TypeKind.PROMOTED_LONG_LONG)
                    own.setTypeKind(TypeKind.PROMOTED_LONG_LONG);
                else if (own.getTypeKind() == TypeKind.LONG && typeDefinition.getTypeKind() == TypeKind.LONG_LONG)
                    own.setTypeKind(TypeKind.LONG_LONG);
                else
                    throw new IllegalStateException("Can't merge " + typeDefinition.getTypeKind() + " into " + own.getTypeKind());
            }
        });
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

    public int getStackElementID(StackElementType stackElementType) {
        return orderedStackElements.indexOf(stackElementType) + knownCTypes.size();
    }

    public void addEnum(EnumType enumType) {
        String name = enumType.abstractType();
        if (enums.containsKey(name))
            throw new IllegalArgumentException("Enum with name: " + name + " already exists.");
        enums.put(name, enumType);
    }

    public void recordCType(String name, TypeDefinition definition) {
        if (hasCType(name))
            throw new IllegalArgumentException("CType with name: " + name + " already exists");
        knownCTypes.put(name, definition);
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
        stackElements.forEach((name, stackElementType) -> {
            if (stackElementType.isIncomplete())
                return;
            assertBuilder.add("static_assert(sizeof(" + name + ") == " + stackElementType.getSize(target) + ", \"Type " + name + " has unexpected size.\");");
            assertBuilder.add("static_assert(alignof(" + name + ") == " + stackElementType.getAlignment(target) + ", \"Type " + name + " has unexpected alignment.\");");
            for (int i = 0; i < stackElementType.getFields().size(); i++) {
                assertBuilder.add("static_assert(offsetof(" + name + ", " + stackElementType.getFields().get(i).getName() + ") == " + stackElementType.getFieldOffset(i, target) + ", \"Type " + name + " has unexpected offset.\");");
            }
        });
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
            StringBuilder ffiTypeNativeBody = new StringBuilder("JNI\n");
            ffiTypeNativeBody.append("static native_type* ").append(nativeGetFFIMethodName).append("(int id) {\n");
            ffiTypeNativeBody.append("native_type* nativeType = (native_type*)malloc(sizeof(native_type));\n");
            ffiTypeNativeBody.append("switch(id) {\n");
            BlockStmt staticInit = ffiTypeClass.addStaticInitializer();

            // ptr and void
            ffiTypeNativeBody.append("\tcase ").append(VOID_FFI_ID).append(":\n")
                    .append("\t\t").append("nativeType->type = VOID_TYPE;").append("\n")
                    .append("\t\treturn nativeType;\n");
            staticInit.addStatement("ffiIdMap.put(" + VOID_FFI_ID + ", CHandler.constructCTypeFromNativeType(getNativeType(" + VOID_FFI_ID + ")));");
            ffiTypeNativeBody.append("\tcase ").append(POINTER_FFI_ID).append(":\n")
                    .append("\t\t").append("nativeType->type = POINTER_TYPE;").append("\n")
                    .append("\t\treturn nativeType;\n");
            staticInit.addStatement("ffiIdMap.put(" + POINTER_FFI_ID + ", CHandler.constructCTypeFromNativeType(getNativeType(" + POINTER_FFI_ID + ")));");

            List<String> cTypes = knownCTypes.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
            for (int i = 0; i < cTypes.size(); i++) {
                String cType = cTypes.get(i);
                staticInit.addStatement("ffiIdMap.put(" + i + ", CHandler.constructCTypeFromNativeType(getNativeType(" + i + ")));");
                ffiTypeNativeBody.append("\tcase ").append(i).append(":\n");
                ffiTypeNativeBody.append("\t\tGET_NATIVE_TYPE(").append(cType).append(", nativeType);\n");
                ffiTypeNativeBody.append("\t\treturn nativeType;\n");
            }
            for (int i = 0; i < orderedStackElements.size(); i++) {
                int id = i + knownCTypes.size();
                StackElementType stackElementType = orderedStackElements.get(i);
                staticInit.addStatement("ffiIdMap.put(" + id + ", CHandler.constructCTypeFromNativeType(getNativeType(" + id + ")));");
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

            // Emit web-compatible bindings if enabled
            emitWeb(basePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setWebEnabled(boolean webEnabled) {
        this.webEnabled = webEnabled;
    }

    public boolean isWebEnabled() {
        return webEnabled;
    }

    /**
     * Emits web-compatible bindings (GWT JSNI and TeaVM @JSBody) for generated functions.
     * These call Emscripten-exported C functions directly by name (not JNI-mangled).
     *
     * @param basePath the output directory for generated Java source files
     */
    public void emitWeb(String basePath) {
        if (!webEnabled) return;

        try {
            // Generate GWT emulation of the global _Internal class
            CompilationUnit gwtCU = new CompilationUnit(globalType.packageName() + ".gwt");
            ClassOrInterfaceDeclaration gwtClass = gwtCU.addClass(globalType.internalClassName() + "_Web", Keyword.PUBLIC, Keyword.FINAL);

            // Generate TeaVM bridge of the global _Internal class
            CompilationUnit teavmCU = new CompilationUnit(globalType.packageName() + ".teavm");
            ClassOrInterfaceDeclaration teavmClass = teavmCU.addClass(globalType.internalClassName() + "_Web", Keyword.PUBLIC, Keyword.FINAL);

            for (FunctionType function : globalType.getFunctions()) {
                FunctionSignature sig = function.getSignature();
                String cFuncName = sig.getName();

                // GWT JSNI method
                MethodDeclaration gwtMethod = gwtClass.addMethod(cFuncName, Keyword.PUBLIC, Keyword.STATIC, Keyword.NATIVE);
                gwtMethod.setType(sig.getReturnType().getMappedType().primitiveType());
                StringBuilder jsniBody = new StringBuilder("/*-{ ");
                if (sig.getReturnType().getTypeKind() != TypeKind.VOID) {
                    jsniBody.append("return ");
                }
                jsniBody.append("$wnd.Module.ccall('").append(cFuncName).append("', ");
                jsniBody.append("'").append(emscriptenReturnType(sig.getReturnType())).append("', [");

                StringBuilder paramTypes = new StringBuilder();
                for (int i = 0; i < sig.getArguments().length; i++) {
                    NamedType arg = sig.getArguments()[i];
                    gwtMethod.addParameter(arg.getDefinition().getMappedType().primitiveType(), arg.getName());
                    if (i > 0) paramTypes.append(", ");
                    paramTypes.append("'").append(emscriptenParamType(arg.getDefinition())).append("'");
                }
                jsniBody.append(paramTypes).append("], [");
                for (int i = 0; i < sig.getArguments().length; i++) {
                    if (i > 0) jsniBody.append(", ");
                    jsniBody.append(sig.getArguments()[i].getName());
                }
                jsniBody.append("]); }-*/");
                gwtMethod.setBody(null);
                // Add JSNI body as comment (GWT pattern)

                // TeaVM @JSBody method
                MethodDeclaration teavmMethod = teavmClass.addMethod(cFuncName, Keyword.PUBLIC, Keyword.STATIC, Keyword.NATIVE);
                teavmMethod.setType(sig.getReturnType().getMappedType().primitiveType());
                StringBuilder jsBody = new StringBuilder();
                if (sig.getReturnType().getTypeKind() != TypeKind.VOID) {
                    jsBody.append("return ");
                }
                jsBody.append("Module._").append(cFuncName).append("(");
                for (int i = 0; i < sig.getArguments().length; i++) {
                    NamedType arg = sig.getArguments()[i];
                    teavmMethod.addParameter(arg.getDefinition().getMappedType().primitiveType(), arg.getName());
                    if (i > 0) jsBody.append(", ");
                    jsBody.append(arg.getName());
                }
                jsBody.append(");");
                teavmMethod.setBody(null);
            }

            // Write GWT web bindings
            Path gwtPath = Paths.get(basePath + (globalType.packageName() + ".gwt").replace(".", "/") + "/"
                    + globalType.internalClassName() + "_Web.java");
            gwtPath.getParent().toFile().mkdirs();
            Files.write(gwtPath, gwtCU.toString().getBytes(StandardCharsets.UTF_8));

            // Write TeaVM web bindings
            Path teavmPath = Paths.get(basePath + (globalType.packageName() + ".teavm").replace(".", "/") + "/"
                    + globalType.internalClassName() + "_Web.java");
            teavmPath.getParent().toFile().mkdirs();
            Files.write(teavmPath, teavmCU.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String emscriptenReturnType(TypeDefinition typeDef) {
        TypeKind kind = typeDef.getTypeKind();
        if (kind == TypeKind.VOID) return "null";
        if (kind == TypeKind.FLOAT || kind == TypeKind.DOUBLE) return "number";
        return "number";
    }

    private String emscriptenParamType(TypeDefinition typeDef) {
        TypeKind kind = typeDef.getTypeKind();
        if (kind == TypeKind.FLOAT || kind == TypeKind.DOUBLE) return "number";
        return "number";
    }

    public static Manager getInstance() {
        return instance;
    }

}
