package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.EnumType;
import com.badlogic.gdx.jnigen.generator.types.FunctionType;
import com.badlogic.gdx.jnigen.generator.types.GlobalType;
import com.badlogic.gdx.jnigen.generator.types.StackElementType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration.ConfigOption;

import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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
    private final ArrayList<String> knownCTypes = new ArrayList<>();

    private final HashMap<String, TypeDefinition> cTypeToJavaStringMapper = new HashMap<>();

    private final Map<String, String> typedefs = new HashMap<>();

    private final Map<String, String> macros = new HashMap<>();

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
        this.knownCTypes.addAll(rollBackManager.knownCTypes);
        this.cTypeToJavaStringMapper.putAll(rollBackManager.cTypeToJavaStringMapper);
        this.typedefs.putAll(rollBackManager.typedefs);
        this.macros.putAll(rollBackManager.macros);
        this.globalType = rollBackManager.globalType;
    }

    public static void startNewManager() {
        instance = new Manager(instance);
    }

    public static void rollBack() {
        if (instance.rollBackManager == null)
            throw new IllegalStateException("Can't rollback, because no rollback point exists");
        instance = instance.rollBackManager;
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

    public void registerMacro(String name, String value) {
        if (macros.containsKey(name)) {
            if (!macros.get(name).equals(value))
                throw new IllegalArgumentException("Macro with name " + name + " already exists, but has different value. Old: " + macros.get(name) + " != New: " + value);
        }
        macros.put(name, value);
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
            throw new IllegalArgumentException("Failed to find native method: " + method.toString() + " in " + classString);

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

    public void emit(String basePath) {
        try {
            for (StackElementType stackElementType : stackElements.values()) {
                CompilationUnit cu = new CompilationUnit(stackElementType.packageName());
                ClassOrInterfaceDeclaration declaration = stackElementType.generateClass();
                cu.addType(declaration);
                stackElementType.write(cu, declaration);

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

            // Macros
            CompilationUnit constantsCU = new CompilationUnit(basePackage);
            ClassOrInterfaceDeclaration constantsClass = constantsCU.addClass("Constants", Keyword.PUBLIC, Keyword.FINAL);
            macros.keySet().stream().sorted().forEach(name -> {
                String value = macros.get(name);

                // TODO: 21.06.24 We need to find more reliable ways, maybe a fancy regex?
                if (value.startsWith("(") && value.endsWith(")"))
                    value = value.substring(1, value.length() - 1);

                for (int i = 0; i < 3; i++) {
                    if (value.isEmpty())
                        return;
                    char indexLowerCase = value.toLowerCase().charAt(value.length() - 1);
                    if (indexLowerCase == 'l' || indexLowerCase == 'u')
                        value = value.substring(0, value.length() - 1);
                    else
                        break;
                }

                if (SourceVersion.isKeyword(name))
                    name = name + "_r";
                try {
                    long l = Long.decode(value);
                    Class<?> lowestBound;
                    if (l <= Byte.MAX_VALUE && l >= Byte.MIN_VALUE) {
                        lowestBound = byte.class;
                    } else if (l <= Short.MAX_VALUE && l >= Short.MIN_VALUE) {
                        lowestBound = short.class;
                    } else if (l <= Character.MAX_VALUE && l >= Character.MIN_VALUE) {
                        lowestBound = char.class;
                    } else if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE) {
                        lowestBound = int.class;
                    } else {
                        value += "L";
                        lowestBound = long.class;
                    }
                    constantsClass.addFieldWithInitializer(lowestBound, name, new IntegerLiteralExpr(value), Keyword.PUBLIC, Keyword.STATIC, Keyword.FINAL);
                }catch (NumberFormatException ignored){
                    try {
                        if (value.endsWith("L") || value.endsWith("l"))
                            value = value.substring(0, value.length() - 1);
                        Double.parseDouble(value);
                        boolean isFloat = value.endsWith("f") || value.endsWith("F");
                        Class<?> lowestBound = isFloat ? float.class : double.class;
                        constantsClass.addFieldWithInitializer(lowestBound, name, new DoubleLiteralExpr(value), Keyword.PUBLIC, Keyword.STATIC, Keyword.FINAL);
                    } catch (NumberFormatException ignored1){}
                }
            });
            Files.write(Paths.get(basePath + basePackage.replace(".", "/") + "/Constants.java"), constantsCU.toString().getBytes(StandardCharsets.UTF_8));


            // FFI Type test
            CompilationUnit ffiTypeCU = new CompilationUnit(basePackage);
            ffiTypeCU.addImport(ClassNameConstants.CHANDLER_CLASS);
            ffiTypeCU.addImport(ClassNameConstants.CTYPEINFO_CLASS);
            ClassOrInterfaceDeclaration ffiTypeClass = ffiTypeCU.addClass("FFITypes", Keyword.PUBLIC);
            addJNIComment(ffiTypeClass, "#include <jnigen.h>", "#include <" + parsedCHeader + ">");
            ffiTypeClass.addMethod("init", Keyword.PUBLIC, Keyword.STATIC);

            ffiTypeCU.addImport(HashMap.class);
            ffiTypeClass.addFieldWithInitializer("HashMap<Integer, CTypeInfo>", "ffiIdMap", StaticJavaParser.parseExpression("new HashMap<>()"), Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

            ffiTypeClass.addMethod("getCTypeInfo", Keyword.PUBLIC, Keyword.STATIC)
                    .setType(ClassNameConstants.CTYPEINFO_CLASS)
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
            staticInit.addStatement("ffiIdMap.put(" + VOID_FFI_ID + ", CHandler.constructCTypeFromNativeType(\"void\", getNativeType(" + VOID_FFI_ID + ")));");
            ffiTypeNativeBody.append("\tcase ").append(POINTER_FFI_ID).append(":\n")
                    .append("\t\t").append("nativeType->type = POINTER_TYPE;").append("\n")
                    .append("\t\treturn nativeType;\n");
            staticInit.addStatement("ffiIdMap.put(" + POINTER_FFI_ID + ", CHandler.constructCTypeFromNativeType(\"void*\", getNativeType(" + POINTER_FFI_ID + ")));");

            for (int i = 0; i < knownCTypes.size(); i++) {
                String cType = knownCTypes.get(i);
                staticInit.addStatement("ffiIdMap.put(" + i + ", CHandler.constructCTypeFromNativeType(\"" + cType + "\", getNativeType(" + i + ")));");
                staticInit.addStatement("CHandler.registerCType(ffiIdMap.get(" + i + "));");
                ffiTypeNativeBody.append("\tcase ").append(i).append(":\n");
                ffiTypeNativeBody.append("\t\tGET_NATIVE_TYPE(").append(cType).append(", nativeType);\n");
                ffiTypeNativeBody.append("\t\treturn nativeType;\n");
            }
            for (int i = 0; i < orderedStackElements.size(); i++) {
                int id = i + knownCTypes.size();
                StackElementType stackElementType = orderedStackElements.get(i);
                staticInit.addStatement("ffiIdMap.put(" + id + ", CHandler.constructStackElementCTypeFromNativeType(null, getNativeType(" + id + ")));");
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
