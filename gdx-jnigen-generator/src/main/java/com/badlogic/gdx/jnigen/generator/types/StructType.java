package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.ArrayList;
import java.util.List;

public class StructType implements Emitable {

    private final String name;
    private final List<StructField> fields = new ArrayList<>();
    private final String pointerName = "Pointer";

    public StructType(String name) {
        this.name = name;
    }

    public void addField(String name, TypeKind kind) {
        fields.add(new StructField(name, kind));
    }

    @Override
    public void write(CompilationUnit compilationUnit) {
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
        staticInit.addStatement("__size = calculateSize();");
        staticInit.addStatement("__ffi_type = generateFFIType();");
        staticInit.addStatement("Global.registerStructFFIType(" + name + ".class, __ffi_type);");
        staticInit.addStatement("Global.registerPointingSupplier(" + name + ".class, " + name + "::new);");
        staticInit.addStatement("Global.registerNewStructPointerSupplier(" + name + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("Global.registerStructPointer(" + name + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("Global.registerPointingSupplier(" + structPointerRef + ".class, " + structPointerRef + "::new);");

        // Standard methods


        // Fields


        // Pointer
        ClassOrInterfaceDeclaration pointerClass = compilationUnit.addClass(pointerName, Keyword.PUBLIC, Keyword.STATIC, Keyword.FINAL);
        pointerClass.addMember(pointerClass);
        pointerClass.addExtendedType("StructPointer<" + name + ">");
        ConstructorDeclaration pointerConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerConstructor.addParameter(new Parameter(PrimitiveType.longType(), "pointer"));
        pointerConstructor.addParameter(new Parameter(PrimitiveType.booleanType(), "freeOnGC"));
        BlockStmt body = new BlockStmt();
        body.addStatement("super(pointer, freeOnGC);");
        pointerConstructor.setBody(body);
        ConstructorDeclaration defaultConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        defaultConstructor.createBody().addStatement("super(__size);");
    }
    static class StructField {
        private String name;
        private TypeKind kind;

        public StructField(String name, TypeKind kind) {
            this.name = name;
            this.kind = kind;
        }
    }
}
