package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.CHandler;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.pointer.Struct;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StructType implements MappedType {


    private final TypeDefinition definition;
    private final List<NamedType> fields = new ArrayList<>();
    private final String pointerName = "Pointer";
    private final String javaTypeName;

    public StructType(TypeDefinition definition) {
        this.definition = definition;
        this.javaTypeName = definition.getTypeName().replace("struct ", "");
    }

    public void addField(NamedType type) {
        fields.add(type);
    }

    public void write(CompilationUnit compilationUnit, HashMap<MethodDeclaration, String> patchMap) {
        String structPointerRef = javaTypeName + "." + pointerName;

        compilationUnit.addImport(CHandler.class);
        compilationUnit.addImport(Struct.class);
        compilationUnit.addImport(StructPointer.class);
        ClassOrInterfaceDeclaration structClass = compilationUnit.addClass(javaTypeName, Keyword.PUBLIC, Keyword.FINAL);
        structClass.addOrphanComment(new BlockComment("JNI\n#include <jnigen.h>\n"));
        structClass.addExtendedType(Struct.class);
        structClass.addField(long.class, "__size", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);
        structClass.addField(long.class, "__ffi_type", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

        // Static-Init
        BlockStmt staticInit = structClass.addStaticInitializer();
        staticInit.addStatement("__ffi_type = FFITypes.getFFIType(" + Manager.getInstance().getStructID(this) + ");");
        staticInit.addStatement("CHandler.calculateAlignmentAndSizeForType(__ffi_type);");
        staticInit.addStatement("__size = CHandler.getSizeFromFFIType(__ffi_type);");
        staticInit.addStatement("CHandler.registerStructFFIType(" + javaTypeName + ".class, __ffi_type);");
        staticInit.addStatement("CHandler.registerPointingSupplier(" + javaTypeName + ".class, " + javaTypeName + "::new);");
        staticInit.addStatement("CHandler.registerNewStructPointerSupplier(" + javaTypeName + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("CHandler.registerStructPointer(" + javaTypeName + ".class, " + structPointerRef + "::new);");
        staticInit.addStatement("CHandler.registerPointingSupplier(" + structPointerRef + ".class, " + structPointerRef + "::new);");

        compilationUnit.addImport(Manager.getInstance().getBasePackage() + ".FFITypes");

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
            field.getDefinition().getMappedType().importType(compilationUnit);
            MethodDeclaration getMethod = structClass.addMethod(field.getName(), Keyword.PUBLIC);
            getMethod.setType(field.getDefinition().getMappedType().abstractType());
            BlockStmt getBody = new BlockStmt();
            String appendix = "";
            if (field.getDefinition().getTypeKind() == TypeKind.FLOAT)
                appendix = "Float";
            else if (field.getDefinition().getTypeKind() == TypeKind.DOUBLE)
                appendix = "Double";
            Expression expression = StaticJavaParser.parseExpression("CHandler.getStructField" + appendix + "(getPointer(), __ffi_type, " + i + ")");
            getBody.addStatement(new ReturnStmt(field.getDefinition().getMappedType().fromC(expression)));
            getMethod.setBody(getBody);

            MethodDeclaration setMethod = structClass.addMethod(field.getName(), Keyword.PUBLIC);
            setMethod.addParameter(field.getDefinition().getMappedType().abstractType(), field.getName());
            BlockStmt setBody = new BlockStmt();

            MethodCallExpr callSetStruct = new MethodCallExpr("setStructField");
            callSetStruct.setScope(new NameExpr("CHandler"));
            callSetStruct.addArgument("getPointer()");
            callSetStruct.addArgument("__ffi_type");
            callSetStruct.addArgument(String.valueOf(i));
            callSetStruct.addArgument(field.getDefinition().getMappedType().toC(new NameExpr(field.getName())));
            setBody.addStatement(callSetStruct);
            setMethod.setBody(setBody);
        }


        // Pointer
        ClassOrInterfaceDeclaration pointerClass = new ClassOrInterfaceDeclaration(new NodeList<>(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier()), false, pointerName);

        structClass.addMember(pointerClass);
        pointerClass.addExtendedType("StructPointer<" + javaTypeName + ">");
        ConstructorDeclaration pointerConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerConstructor.addParameter(new Parameter(PrimitiveType.longType(), "pointer"));
        pointerConstructor.addParameter(new Parameter(PrimitiveType.booleanType(), "freeOnGC"));
        BlockStmt body = new BlockStmt();
        body.addStatement("super(pointer, freeOnGC);");
        pointerConstructor.setBody(body);
        ConstructorDeclaration defaultConstructorPointer = pointerClass.addConstructor(Keyword.PUBLIC);
        defaultConstructorPointer.createBody().addStatement("super(__size);");

        pointerClass.addMethod("getSize", Keyword.PUBLIC).setType(long.class).createBody().addStatement("return __size;");
        pointerClass.addMethod("getStructClass", Keyword.PUBLIC).setType("Class<" + javaTypeName + ">")
                .createBody().addStatement("return " + javaTypeName + ".class;");

    }

    public String getFFITypeBody(String ffiResolveFunctionName) {
        StringBuilder generateFFIMethodBody = new StringBuilder();
        generateFFIMethodBody.append("\t{\n\t\tffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));\n");
        generateFFIMethodBody.append("\t\ttype->type = FFI_TYPE_STRUCT;\n");
        generateFFIMethodBody.append("\t\ttype->elements = (ffi_type**)malloc(sizeof(ffi_type*) * ").append(fields.size() + 1).append(");\n");
        for (int i = 0; i < fields.size(); i++) {
            NamedType field = fields.get(i);
            if (field.getDefinition().getTypeKind() == TypeKind.POINTER || field.getDefinition().getTypeKind() == TypeKind.CLOSURE) {
                generateFFIMethodBody.append("\t\ttype->elements[").append(i).append("] = &ffi_type_pointer;\n");
            } else if (field.getDefinition().getTypeKind() == TypeKind.STRUCT) {
                int fieldStructID = Manager.getInstance().getStructID((StructType)field.getDefinition().getMappedType());
                generateFFIMethodBody.append("\t\ttype->elements[").append(i).append("] = ")
                        .append(ffiResolveFunctionName).append("(")
                        .append(fieldStructID)
                        .append(");\n");
            } else if (field.getDefinition().getTypeKind() == TypeKind.ENUM) {
                generateFFIMethodBody.append("\t\ttype->elements[").append(i).append("] = GET_FFI_TYPE(int);\n");
            } else {
                generateFFIMethodBody.append("\t\ttype->elements[").append(i).append("] = GET_FFI_TYPE(")
                        .append(field.getDefinition().getTypeName())
                        .append(");\n");
            }
        }
        generateFFIMethodBody.append("\t\ttype->elements[").append(fields.size()).append("] = NULL;\n");
        generateFFIMethodBody.append("\t\treturn type;\n\t}\n");

        return generateFFIMethodBody.toString();
    }

    @Override
    public String abstractType() {
        return javaTypeName;
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
        return packageName() + "." + javaTypeName;
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage() + ".structs";
    }

    @Override
    public MappedType asPointer() {
        return new PointerType(definition);
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        ObjectCreationExpr createObject = new ObjectCreationExpr();
        createObject.setType(instantiationType());
        createObject.addArgument(cRetrieved);
        createObject.addArgument("true");
        return createObject;
    }

    @Override
    public Expression toC(Expression cSend) {
        MethodCallExpr methodCallExpr = new MethodCallExpr("getPointer");
        methodCallExpr.setScope(cSend);
        return methodCallExpr;
    }
}
