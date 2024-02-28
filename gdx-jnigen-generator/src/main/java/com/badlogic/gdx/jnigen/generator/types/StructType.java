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
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.ArrayList;
import java.util.List;

public class StructType implements MappedType {


    private final TypeDefinition definition;
    private final List<NamedType> fields = new ArrayList<>();
    private final String pointerName;
    private final String javaTypeName;

    public StructType(TypeDefinition definition) {
        this.definition = definition;
        this.javaTypeName = definition.getTypeName().replace("struct ", "");
        pointerName = javaTypeName + "Pointer";
    }

    public void addField(NamedType type) {
        fields.add(type);
    }

    public void write(CompilationUnit compilationUnit) {
        String structPointerRef = javaTypeName + "." + pointerName;

        compilationUnit.addImport(CHandler.class);
        compilationUnit.addImport(Struct.class);
        compilationUnit.addImport(StructPointer.class);
        ClassOrInterfaceDeclaration structClass = compilationUnit.addClass(javaTypeName, Keyword.PUBLIC, Keyword.FINAL);
        structClass.addExtendedType(Struct.class);
        structClass.addField(int.class, "__size", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);
        structClass.addField(long.class, "__ffi_type", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

        // Static-Init
        BlockStmt staticInit = structClass.addStaticInitializer();
        staticInit.addStatement("__ffi_type = FFITypes.getCTypeInfo(" + typeID() + ").getFfiType();");
        staticInit.addStatement("__size = CHandler.getSizeFromFFIType(__ffi_type);");

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

        structClass.addMethod("asPointer", Keyword.PUBLIC).setType(structPointerRef)
                .createBody().addStatement("return new " + structPointerRef + "(getPointer(), getsGCFreed());");

        // Fields
        int index = 0;
        for (NamedType field : fields) {
            field.getDefinition().getMappedType().importType(compilationUnit);
            MethodDeclaration getMethod = structClass.addMethod(field.getName(), Keyword.PUBLIC);
            getMethod.setType(field.getDefinition().getMappedType().abstractType());
            BlockStmt getBody = new BlockStmt();
            if (field.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
                MethodCallExpr getOffsetExpr = new MethodCallExpr("getOffsetForField");
                getOffsetExpr.setScope(new NameExpr("CHandler"));
                getOffsetExpr.addArgument("__ffi_type");
                getOffsetExpr.addArgument(index + "");
                structClass.addFieldWithInitializer(int.class, "__" + field.getName() + "_offset", getOffsetExpr,
                        Keyword.PRIVATE, Keyword.STATIC, Keyword.FINAL);

                MethodCallExpr getPointer = new MethodCallExpr("getPointer");
                BinaryExpr addPointerOffset = new BinaryExpr(getPointer,
                        new NameExpr("__" + field.getName() + "_offset"), Operator.PLUS);
                MethodCallExpr guardPointer = new MethodCallExpr("guardCount");
                guardPointer.setScope(field.getDefinition().getMappedType().fromC(addPointerOffset));
                guardPointer.addArgument(field.getDefinition().getCount() + "");
                structClass.addFieldWithInitializer(field.getDefinition().getMappedType().abstractType(),
                        "__" + field.getName(), guardPointer,
                        Keyword.PRIVATE, Keyword.FINAL);
                getBody.addStatement("return __" + field.getName() + ";");
                getMethod.setBody(getBody);
                index += field.getDefinition().getCount();
                continue;
            }

            String appendix = "";
            if (field.getDefinition().getTypeKind() == TypeKind.FLOAT) appendix = "Float";
            else if (field.getDefinition().getTypeKind() == TypeKind.DOUBLE) appendix = "Double";
            Expression expression = StaticJavaParser.parseExpression(
                    "CHandler.getStructField" + appendix + "(getPointer(), __ffi_type, " + index + ")");
            getBody.addStatement(new ReturnStmt(field.getDefinition().getMappedType().fromC(expression)));
            getMethod.setBody(getBody);

            MethodDeclaration setMethod = structClass.addMethod(field.getName(), Keyword.PUBLIC);
            setMethod.addParameter(field.getDefinition().getMappedType().abstractType(), field.getName());
            BlockStmt setBody = new BlockStmt();

            MethodCallExpr callSetStruct = new MethodCallExpr("setStructField");
            callSetStruct.setScope(new NameExpr("CHandler"));
            callSetStruct.addArgument("getPointer()");
            callSetStruct.addArgument("__ffi_type");
            callSetStruct.addArgument(String.valueOf(index));
            callSetStruct.addArgument(field.getDefinition().getMappedType().toC(new NameExpr(field.getName())));
            setBody.addStatement(callSetStruct);
            setMethod.setBody(setBody);
            index++;
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

        pointerClass.addConstructor(Keyword.PUBLIC).getBody().addStatement("this(1, true, true);");

        ConstructorDeclaration defaultConstructorPointer = pointerClass.addConstructor(Keyword.PUBLIC);
        defaultConstructorPointer.addParameter(int.class, "count");
        defaultConstructorPointer.addParameter(boolean.class, "freeOnGC");
        defaultConstructorPointer.addParameter(boolean.class, "guard");
        defaultConstructorPointer.createBody().addStatement("super(__size, count, freeOnGC, guard);");

        pointerClass.addMethod("guardCount", Keyword.PUBLIC).setType(structPointerRef)
                .addParameter(long.class, "count")
                .createBody()
                .addStatement("super.guardCount(count);")
                .addStatement("return this;");

        pointerClass.addMethod("getSize", Keyword.PUBLIC).setType(int.class).createBody().addStatement("return __size;");
        pointerClass.addMethod("getStructClass", Keyword.PUBLIC).setType("Class<" + javaTypeName + ">")
                .createBody().addStatement("return " + javaTypeName + ".class;");
        pointerClass.addMethod("createStruct", Keyword.PROTECTED).setType(javaTypeName)
                .addParameter(long.class, "ptr")
                .addParameter(boolean.class, "freeOnGC")
                .createBody().addStatement("return new " + javaTypeName + "(ptr, freeOnGC);");
    }

    public String getFFITypeBody(String ffiResolveFunctionName) {
        ArrayList<NamedType> unwrappedFields = new ArrayList<>();
        for (NamedType field : fields) {
            if (field.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
                for (int i = 0; i < field.getDefinition().getCount(); i++) {
                    unwrappedFields.add(new NamedType(field.getDefinition().getNestedDefinition(), field.getName() + "_" + i));
                }
            } else {
                unwrappedFields.add(field);
            }
        }

        StringBuilder generateFFIMethodBody = new StringBuilder();
        generateFFIMethodBody.append("\t{\n\t\tffi_type* type = (ffi_type*)malloc(sizeof(ffi_type));\n");
        generateFFIMethodBody.append("\t\ttype->type = FFI_TYPE_STRUCT;\n");
        generateFFIMethodBody.append("\t\ttype->elements = (ffi_type**)malloc(sizeof(ffi_type*) * ").append(unwrappedFields.size() + 1).append(");\n");

        for (int i = 0; i < unwrappedFields.size(); i++) {
            NamedType field = unwrappedFields.get(i);
            if (field.getDefinition().getTypeKind() == TypeKind.POINTER || field.getDefinition().getTypeKind() == TypeKind.CLOSURE) {
                generateFFIMethodBody.append("\t\ttype->elements[").append(i).append("] = &ffi_type_pointer;\n");
            } else if (field.getDefinition().getTypeKind() == TypeKind.STRUCT) {
                int fieldStructID = field.getDefinition().getMappedType().typeID();
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
        generateFFIMethodBody.append("\t\ttype->elements[").append(unwrappedFields.size()).append("] = NULL;\n");
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

    @Override
    public int typeID() {
        return Manager.getInstance().getStructID(this);
    }
}
