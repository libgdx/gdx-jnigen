package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.ArrayList;
import java.util.List;

public class StackElementType implements MappedType, WritableClass {


    private final TypeDefinition definition;
    private final MappedType parent;
    private final List<TypeDefinition> children = new ArrayList<>();

    // TODO: Conceptionally, this belongs into TypeDefinition
    private boolean isStruct;
    private final List<NamedType> fields = new ArrayList<>();
    private final String pointerName;
    private final String javaTypeName;

    public StackElementType(TypeDefinition definition, String javaTypeName, MappedType parent, boolean isStruct) {
        this.definition = definition;
        this.isStruct = isStruct;
        this.javaTypeName = javaTypeName;
        this.parent = parent;
        pointerName = javaTypeName + "Pointer";
    }

    public void addField(NamedType type) {
        fields.add(type);
    }

    public void addChild(TypeDefinition child) {
        children.add(child);
    }

    @Override
    public ClassOrInterfaceDeclaration generateClass() {
        NodeList<Modifier> modifiers = new NodeList<>(Modifier.publicModifier(), Modifier.finalModifier());
        if (parent != null)
            modifiers.add(Modifier.staticModifier());
        return new ClassOrInterfaceDeclaration(modifiers, false, javaTypeName);
    }

    @Override
    public ClassOrInterfaceDeclaration generateClassInternal() {
        NodeList<Modifier> modifiers = new NodeList<>(Modifier.publicModifier(), Modifier.finalModifier(), Modifier.staticModifier());

        return new ClassOrInterfaceDeclaration(modifiers, false, internalClassName());
    }

    @Override
    public void write(CompilationUnit cuPublic, ClassOrInterfaceDeclaration toWriteToPublic, CompilationUnit cuPrivate, ClassOrInterfaceDeclaration toWriteToPrivate) {
        String structPointerRef = javaTypeName + "." + pointerName;

        cuPublic.addImport(ClassNameConstants.CHANDLER_CLASS);
        cuPublic.addImport(isStruct ? ClassNameConstants.STRUCT_CLASS : ClassNameConstants.UNION_CLASS);
        cuPublic.addImport(ClassNameConstants.STACKELEMENTPOINTER_CLASS);

        toWriteToPublic.addExtendedType(isStruct ? "Struct" : "Union");
        toWriteToPublic.addField(int.class, "__size", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);
        toWriteToPublic.addField(long.class, "__ffi_type", Keyword.PRIVATE, Keyword.FINAL, Keyword.STATIC);

        // Static-Init
        BlockStmt staticInit = toWriteToPublic.addStaticInitializer();
        staticInit.addStatement("__ffi_type = FFITypes.getCTypeInfo(" + typeID() + ").getFfiType();");
        staticInit.addStatement("__size = CHandler.getSizeFromFFIType(__ffi_type);");

        cuPublic.addImport(Manager.getInstance().getBasePackage() + ".FFITypes");

        // Constructors
        ConstructorDeclaration pointerTakingConstructor = toWriteToPublic.addConstructor(Keyword.PUBLIC);
        pointerTakingConstructor.addParameter(long.class, "pointer");
        pointerTakingConstructor.addParameter(boolean.class, "freeOnGC");
        pointerTakingConstructor.getBody().addStatement("super(pointer, freeOnGC);");

        ConstructorDeclaration defaultConstructor = toWriteToPublic.addConstructor(Keyword.PUBLIC);
        defaultConstructor.getBody().addStatement("super(__size);");


        // Standard methods
        toWriteToPublic.addMethod("getSize", Keyword.PUBLIC).setType(long.class)
                .createBody().addStatement("return __size;");

        toWriteToPublic.addMethod("getFFIType", Keyword.PUBLIC).setType(long.class)
                .createBody().addStatement("return __ffi_type;");

        toWriteToPublic.addMethod("asPointer", Keyword.PUBLIC).setType(structPointerRef)
                .createBody().addStatement("return new " + structPointerRef + "(getPointer(), getsGCFreed());");

        // Fields
        int index = 0;
        for (NamedType field : fields) {
            field.getDefinition().getMappedType().importType(cuPublic);
            MethodDeclaration getMethod = toWriteToPublic.addMethod(field.getName(), Keyword.PUBLIC);
            getMethod.setType(field.getDefinition().getMappedType().abstractType());
            BlockStmt getBody = new BlockStmt();
            if (field.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY || field.getDefinition().getTypeKind() == TypeKind.STACK_ELEMENT) {
                if (isStruct) {
                    MethodCallExpr getOffsetExpr = new MethodCallExpr("getOffsetForField");
                    getOffsetExpr.setScope(new NameExpr("CHandler"));
                    getOffsetExpr.addArgument("__ffi_type");
                    getOffsetExpr.addArgument(index + "");
                    toWriteToPublic.addFieldWithInitializer(int.class, "__" + field.getName() + "_offset", getOffsetExpr,
                            Keyword.PRIVATE, Keyword.STATIC, Keyword.FINAL);
                }

                Expression pointer;
                if (isStruct) {
                    MethodCallExpr getPointer = new MethodCallExpr("getPointer");
                    pointer = new BinaryExpr(getPointer,
                            new NameExpr("__" + field.getName() + "_offset"), Operator.PLUS);
                } else {
                    pointer = new MethodCallExpr("getPointer");
                }

                Expression fromCExpression = field.getDefinition().getMappedType().fromC(pointer, new BooleanLiteralExpr(false));

                if (field.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
                    MethodCallExpr guardPointer = new MethodCallExpr("guardCount");
                    guardPointer.setScope(fromCExpression);
                    guardPointer.addArgument(field.getDefinition().getCount() + "");
                    fromCExpression = guardPointer;
                }
                toWriteToPublic.addFieldWithInitializer(field.getDefinition().getMappedType().abstractType(),
                        "__" + field.getName(), fromCExpression,
                        Keyword.PRIVATE, Keyword.FINAL);
                getBody.addStatement("return __" + field.getName() + ";");
                getMethod.setBody(getBody);
                if (field.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY)
                    index += field.getDefinition().getCount();
                else
                    index++;
                continue;
            }

            String appendix = "";
            if (field.getDefinition().getTypeKind() == TypeKind.FLOAT) appendix = "Float";
            else if (field.getDefinition().getTypeKind() == TypeKind.DOUBLE) appendix = "Double";
            Expression expression = StaticJavaParser.parseExpression(
                    "getValue" + appendix + "(" + index + ")");
            getBody.addStatement(new ReturnStmt(field.getDefinition().getMappedType().fromC(expression)));
            getMethod.setBody(getBody);

            MethodDeclaration setMethod = toWriteToPublic.addMethod(field.getName(), Keyword.PUBLIC);
            setMethod.addParameter(field.getDefinition().getMappedType().abstractType(), field.getName());
            BlockStmt setBody = new BlockStmt();

            MethodCallExpr callSetStruct = new MethodCallExpr("setValue");
            callSetStruct.addArgument(field.getDefinition().getMappedType().toC(new NameExpr(field.getName())));
            callSetStruct.addArgument(String.valueOf(index));
            setBody.addStatement(callSetStruct);
            setMethod.setBody(setBody);
            index++;
        }


        // Pointer
        ClassOrInterfaceDeclaration pointerClass = new ClassOrInterfaceDeclaration(new NodeList<>(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier()), false, pointerName);

        toWriteToPublic.addMember(pointerClass);
        pointerClass.addExtendedType("StackElementPointer<" + javaTypeName + ">");
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
        pointerClass.addMethod("createStackElement", Keyword.PROTECTED).setType(javaTypeName)
                .addParameter(long.class, "ptr")
                .addParameter(boolean.class, "freeOnGC")
                .createBody().addStatement("return new " + javaTypeName + "(ptr, freeOnGC);");


        // Children
        children.forEach(child -> {
            WritableClass childStackElement = (WritableClass)child.getMappedType();
            ClassOrInterfaceDeclaration declaration = childStackElement.generateClass();
            ClassOrInterfaceDeclaration internalDeclaration = childStackElement.generateClassInternal();
            childStackElement.write(cuPublic, declaration, cuPrivate, internalDeclaration);
            toWriteToPublic.addMember(declaration);
            toWriteToPrivate.addMember(internalDeclaration);
        });
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
        if (isStruct())  {
            generateFFIMethodBody.append("\t\tnativeType->type = STRUCT_TYPE;\n");
        } else {
            generateFFIMethodBody.append("\t\tnativeType->type = UNION_TYPE;\n");
        }
        generateFFIMethodBody.append("\t\tnativeType->field_count = ").append(unwrappedFields.size()).append(";\n");
        generateFFIMethodBody.append("\t\tnativeType->fields = (native_type**)malloc(sizeof(native_type*) * ").append(unwrappedFields.size()).append(");\n");

        for (int i = 0; i < unwrappedFields.size(); i++) {
            NamedType field = unwrappedFields.get(i);
            int fieldStructID = field.getDefinition().getMappedType().typeID();
            generateFFIMethodBody.append("\t\tnativeType->fields[").append(i).append("] = ")
                    .append(ffiResolveFunctionName).append("(")
                    .append(fieldStructID)
                    .append(");\n");
        }
        generateFFIMethodBody.append("\t\treturn nativeType;\n");

        return generateFFIMethodBody.toString();
    }

    public boolean isStruct() {
        return isStruct;
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
        if (parent != null)
            return parent.classFile() + "." + javaTypeName;
        return packageName() + "." + javaTypeName;
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage() + ".structs";
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        return fromC(cRetrieved, new BooleanLiteralExpr(true));
    }

    @Override
    public Expression fromC(Expression cRetrieved, Expression owned) {
        ObjectCreationExpr createObject = new ObjectCreationExpr();
        createObject.setType(instantiationType());
        createObject.addArgument(cRetrieved);
        createObject.addArgument(String.valueOf(owned));
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
        // TODO: 19.03.2024 If parent set, no ID should be needed and no ID specific FFI Type should get generated
        return Manager.getInstance().getStackElementID(this);
    }

    @Override
    public boolean isLibFFIConvertible() {
        return isStruct();
    }


    @Override
    public String internalClass() {
        if (parent != null)
            return parent.internalClass() + "." + internalClassName();
        return Manager.getInstance().getGlobalType().internalClass() + "." + internalClassName();
    }
}
