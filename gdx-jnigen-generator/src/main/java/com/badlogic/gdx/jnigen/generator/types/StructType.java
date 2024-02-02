package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StructType {

    private final String name;
    private final List<NamedType> fields = new ArrayList<>();
    private final String pointerName = "Pointer";

    public StructType(String name) {
        this.name = name;
    }

    public void addField(NamedType type) {
        fields.add(type);
    }

    public String getName() {
        return name;
    }

    public void write(CompilationUnit compilationUnit, HashMap<MethodDeclaration, String> patchMap) {
        String structPointerRef = name + "." + pointerName;

        compilationUnit.addImport(Global.class);
        compilationUnit.addImport(Struct.class);
        compilationUnit.addImport(StructPointer.class);
        ClassOrInterfaceDeclaration structClass = compilationUnit.addClass(name, Keyword.PUBLIC, Keyword.FINAL);
        structClass.addExtendedType(Struct.class);
        structClass.addField(long.class, "__size", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);
        structClass.addField(long.class, "__ffi_type", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

        // Static-Init
        BlockStmt staticInit = structClass.addStaticInitializer();
        staticInit.addStatement("__ffi_type = generateFFIType();");
        staticInit.addStatement("Global.calculateAlignmentAndSizeForType(__ffi_type);");
        staticInit.addStatement("__size = Global.getSizeFromFFIType(__ffi_type);");
        staticInit.addStatement("Global.registerStructFFIType(" + name + ".class, __ffi_type);");
        staticInit.addStatement("Global.registerPointingSupplier(" + name + ".class, " + name + "::new);");
        staticInit.addStatement("Global.registerNewStructPointerSupplier(" + name + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("Global.registerStructPointer(" + name + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("Global.registerPointingSupplier(" + structPointerRef + ".class, " + structPointerRef + "::new);");

        MethodDeclaration generateFFIMethod = structClass.addMethod("generateFFIType", Keyword.PUBLIC, Keyword.STATIC, Keyword.NATIVE).setType(long.class).setBody(null);
        StringBuilder generateFFIMethodBody = new StringBuilder();
        generateFFIMethodBody.append("ffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));\n");
        generateFFIMethodBody.append("type->type = FFI_TYPE_STRUCT;\n");
        generateFFIMethodBody.append("type->elements = (ffi_type**)malloc(sizeof(ffi_type*) * ").append(fields.size() + 1).append(");\n");
        for (int i = 0; i < fields.size(); i++) {
            NamedType field = fields.get(i);
            generateFFIMethodBody.append("type->elements[").append(i).append("] = GET_FFI_TYPE(")
                    .append(field.getDefinition().getTypeName())
                    .append(");\n");
        }
        generateFFIMethodBody.append("type->elements[").append(fields.size()).append("] = NULL;\n");
        generateFFIMethodBody.append("return reinterpret_cast<jlong>(type);\n");

        patchMap.put(generateFFIMethod, generateFFIMethodBody.toString());


        // Constructors
        ConstructorDeclaration pointerTakingConstructor = structClass.addConstructor(Keyword.PUBLIC);
        pointerTakingConstructor.addParameter(long.class, "pointer");
        pointerTakingConstructor.addParameter(boolean.class, "freeOnGC");
        pointerTakingConstructor.getBody().addStatement("super(pointer, freeOnGC);");

        ConstructorDeclaration defaultConstructor = structClass.addConstructor(Keyword.PUBLIC);
        defaultConstructor.getBody().addStatement("super(__size);");


        // Standard methods
        structClass.addMethod("getSize", Keyword.PUBLIC).setType(long.class)
                .createBody().addStatement("return __size;");

        structClass.addMethod("getFFIType", Keyword.PUBLIC).setType(long.class)
                .createBody().addStatement("return __ffi_type;");

        // Fields
        for (int i = 0; i < fields.size(); i++) {
            NamedType field = fields.get(i);
            MethodDeclaration getMethod = structClass.addMethod(field.getName(), Keyword.PUBLIC);
            getMethod.setType(field.getDefinition().resolveJavaClass());
            BlockStmt getBody = new BlockStmt();
            getBody.addStatement("return (" + getMethod.getTypeAsString() + ") Global.getStructField(getPointer(), __ffi_type, " + i + ");");
            getMethod.setBody(getBody);

            MethodDeclaration setMethod = structClass.addMethod(field.getName(), Keyword.PUBLIC);
            setMethod.addParameter(field.getDefinition().resolveJavaClass(), field.getName());
            BlockStmt setBody = new BlockStmt();
            setBody.addStatement("Global.setStructField(getPointer(), __ffi_type, " + i + ", " + field.getName() + ");");
            setMethod.setBody(setBody);
        }


        // Pointer
        ClassOrInterfaceDeclaration pointerClass = new ClassOrInterfaceDeclaration(new NodeList<>(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier()), false, pointerName);

        structClass.addMember(pointerClass);
        pointerClass.addExtendedType("StructPointer<" + name + ">");
        ConstructorDeclaration pointerConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerConstructor.addParameter(new Parameter(PrimitiveType.longType(), "pointer"));
        pointerConstructor.addParameter(new Parameter(PrimitiveType.booleanType(), "freeOnGC"));
        BlockStmt body = new BlockStmt();
        body.addStatement("super(pointer, freeOnGC);");
        pointerConstructor.setBody(body);
        ConstructorDeclaration defaultConstructorPointer = pointerClass.addConstructor(Keyword.PUBLIC);
        defaultConstructorPointer.createBody().addStatement("super(__size);");

        pointerClass.addMethod("getSize", Keyword.PUBLIC).setType(long.class).createBody().addStatement("return __size;");
        pointerClass.addMethod("getStructClass", Keyword.PUBLIC).setType("Class<" + name + ">")
                .createBody().addStatement("return " + name + ".class;");

    }
}
