package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StructType implements MappedType {


    private final TypeDefinition definition;
    private final List<NamedType> fields = new ArrayList<>();
    private final String pointerName = "Pointer";

    public StructType(TypeDefinition definition) {
        this.definition = definition;
    }

    public void addField(NamedType type) {
        fields.add(type);
    }

    public void write(CompilationUnit compilationUnit, HashMap<MethodDeclaration, String> patchMap) {
        String name = definition.getTypeName();
        String structPointerRef = name + "." + pointerName;

        compilationUnit.addImport(CHandler.class);
        compilationUnit.addImport(Struct.class);
        compilationUnit.addImport(StructPointer.class);
        ClassOrInterfaceDeclaration structClass = compilationUnit.addClass(name, Keyword.PUBLIC, Keyword.FINAL);
        structClass.addOrphanComment(new BlockComment("JNI\n#include <jnigen.h>\n"));
        structClass.addExtendedType(Struct.class);
        structClass.addField(long.class, "__size", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);
        structClass.addField(long.class, "__ffi_type", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

        // Static-Init
        BlockStmt staticInit = structClass.addStaticInitializer();
        staticInit.addStatement("__ffi_type = generateFFIType();");
        staticInit.addStatement("CHandler.calculateAlignmentAndSizeForType(__ffi_type);");
        staticInit.addStatement("__size = CHandler.getSizeFromFFIType(__ffi_type);");
        staticInit.addStatement("CHandler.registerStructFFIType(" + name + ".class, __ffi_type);");
        staticInit.addStatement("CHandler.registerPointingSupplier(" + name + ".class, " + name + "::new);");
        staticInit.addStatement("CHandler.registerNewStructPointerSupplier(" + name + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("CHandler.registerStructPointer(" + name + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("CHandler.registerPointingSupplier(" + structPointerRef + ".class, " + structPointerRef + "::new);");

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
            getMethod.setType(field.getDefinition().getMappedType().abstractType());
            BlockStmt getBody = new BlockStmt();
            getBody.addStatement("return (" + getMethod.getTypeAsString() + ") CHandler.getStructField(getPointer(), __ffi_type, " + i + ");");
            getMethod.setBody(getBody);

            MethodDeclaration setMethod = structClass.addMethod(field.getName(), Keyword.PUBLIC);
            setMethod.addParameter(field.getDefinition().getMappedType().abstractType(), field.getName());
            BlockStmt setBody = new BlockStmt();
            setBody.addStatement("CHandler.setStructField(getPointer(), __ffi_type, " + i + ", " + field.getName() + ");");
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

    @Override
    public String abstractType() {
        return definition.getTypeName();
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(classFile());
    }

    @Override
    public String classFile() {
        return packageName() + "." + definition.getTypeName();
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage() + ".structs";
    }

    @Override
    public MappedType asPointer() {
        return new PointerType(definition);
    }
}
