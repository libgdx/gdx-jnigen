package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StackElementType implements MappedType, WritableClass {

    private final TypeDefinition definition;
    private final MappedType parent;
    private final List<TypeDefinition> children = new ArrayList<>();

    private final List<StackElementField> fields = new ArrayList<>();
    private final String pointerName;
    private final String javaTypeName;
    private String comment;

    public StackElementType(TypeDefinition definition, String javaTypeName, MappedType parent) {
        this.definition = definition;
        this.javaTypeName = javaTypeName;
        this.parent = parent;
        pointerName = javaTypeName + "Pointer";
    }

    public void addField(StackElementField type) {
        fields.add(type);
    }

    public void addChild(TypeDefinition child) {
        children.add(child);
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    private void writeStackFields(StackElementField field, ClassOrInterfaceDeclaration toWriteToPublic) {
        NamedType fieldType = field.getType();

        MethodDeclaration reinterpretMethod = toWriteToPublic.addMethod(fieldType.getName(), Keyword.PUBLIC);
        if (field.getComment() != null)
            reinterpretMethod.setJavadocComment(field.getComment());
        reinterpretMethod.setType(fieldType.getDefinition().getMappedType().abstractType());

        Expression pointer = new MethodCallExpr("getPointer");
        Expression fieldOffset = getFieldOffsetAsExpression(fields.indexOf(field));
        if (isStruct() && !fieldOffset.toString().equals("0"))
            pointer = new BinaryExpr(pointer, new EnclosedExpr(fieldOffset), Operator.PLUS);

        Expression fromCExpression = fieldType.getDefinition().getMappedType()
                .fromC(pointer, new BooleanLiteralExpr(false));

        if (fieldType.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
            fromCExpression.asObjectCreationExpr().addArgument(fieldType.getDefinition().getCount() + "");
        }

        reinterpretMethod.createBody().addStatement(new ReturnStmt(fromCExpression));

        MethodDeclaration reinterpretSetMethod = toWriteToPublic.addMethod(fieldType.getName(), Keyword.PUBLIC)
                .addParameter(fieldType.getDefinition().getMappedType().abstractType(), "toSetPtr");

        if (field.getComment() != null)
            reinterpretSetMethod.setJavadocComment(field.getComment());

        MethodCallExpr setPtr = new MethodCallExpr("setPointer")
                .setScope(new NameExpr("toSetPtr"))
                .addArgument(pointer)
                .addArgument(getFieldSizeAsExpression(fields.indexOf(field)))
                .addArgument(new ThisExpr());

        reinterpretSetMethod.createBody().addStatement(setPtr);

        MethodDeclaration cloneMethod = toWriteToPublic.addMethod(JavaUtils.getGetter(fieldType.getName()), Keyword.PUBLIC);

        if (field.getComment() != null)
            cloneMethod.setJavadocComment(field.getComment());
        cloneMethod.setType(fieldType.getDefinition().getMappedType().abstractType());

        MethodCallExpr cloneExpr = new MethodCallExpr("duplicate")
                .setScope(new MethodCallExpr("getBufPtr"))
                .addArgument(fieldOffset)
                .addArgument(getFieldSizeAsExpression(fields.indexOf(field)));

        Expression fromCClonesExpression = fieldType.getDefinition().getMappedType().fromC(cloneExpr);

        if (fieldType.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
            fromCClonesExpression.asObjectCreationExpr().addArgument(fieldType.getDefinition().getCount() + "");
        }

        cloneMethod.createBody().addStatement(new ReturnStmt(fromCClonesExpression));

        MethodDeclaration copyMethod = toWriteToPublic.addMethod(JavaUtils.getGetter(fieldType.getName()), Keyword.PUBLIC)
                .addParameter(fieldType.getDefinition().getMappedType().abstractType(), "toCopyTo");

        if (field.getComment() != null)
            copyMethod.setJavadocComment(field.getComment());

        MethodCallExpr copyExpr = new MethodCallExpr("copyFrom")
                .setScope(new MethodCallExpr("getBufPtr").setScope(new NameExpr("toCopyTo")))
                .addArgument("0")
                .addArgument(new MethodCallExpr("getBufPtr"))
                .addArgument(getFieldOffsetAsExpression(fields.indexOf(field)))
                .addArgument(getFieldSizeAsExpression(fields.indexOf(field)));
        copyMethod.createBody().addStatement(copyExpr);


        MethodDeclaration setMethod = toWriteToPublic.addMethod(JavaUtils.getSetter(fieldType.getName()), Keyword.PUBLIC)
                .addParameter(fieldType.getDefinition().getMappedType().abstractType(), "toCopyFrom");

        if (field.getComment() != null)
            setMethod.setJavadocComment(field.getComment());

        MethodCallExpr setExpr = new MethodCallExpr("copyFrom")
                .setScope(new MethodCallExpr("getBufPtr"))
                .addArgument(getFieldOffsetAsExpression(fields.indexOf(field)))
                .addArgument(new MethodCallExpr("getBufPtr").setScope(new NameExpr("toCopyFrom")))
                .addArgument("0")
                .addArgument(getFieldSizeAsExpression(fields.indexOf(field)));
        setMethod.createBody().addStatement(setExpr);
    }

    @Override
    public void write(CompilationUnit cuPublic, ClassOrInterfaceDeclaration toWriteToPublic, CompilationUnit cuPrivate, ClassOrInterfaceDeclaration toWriteToPrivate) {
        String structPointerRef = javaTypeName + "." + pointerName;

        cuPublic.addImport(ClassNameConstants.CHANDLER_CLASS);
        cuPublic.addImport(isStruct() ? ClassNameConstants.STRUCT_CLASS : ClassNameConstants.UNION_CLASS);
        cuPublic.addImport(ClassNameConstants.STACKELEMENTPOINTER_CLASS);
        cuPublic.addImport(ClassNameConstants.POINTING_CLASS);

        if (comment != null) {
            toWriteToPublic.setJavadocComment(comment);
        }

        toWriteToPublic.addExtendedType(isStruct() ? "Struct" : "Union");
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

        ConstructorDeclaration pointerParentTakingConstructor = toWriteToPublic.addConstructor(Keyword.PUBLIC);
        pointerParentTakingConstructor.addParameter(long.class, "pointer");
        pointerParentTakingConstructor.addParameter(boolean.class, "freeOnGC");
        pointerParentTakingConstructor.addParameter("Pointing", "parent");
        pointerParentTakingConstructor.getBody().addStatement("super(pointer, freeOnGC);").addStatement("setParent(parent);");

        ConstructorDeclaration defaultConstructor = toWriteToPublic.addConstructor(Keyword.PUBLIC);
        defaultConstructor.getBody().addStatement("super(__size);");

        // Standard methods
        toWriteToPublic.addMethod("getSize", Keyword.PUBLIC).setType(long.class).createBody()
                .addStatement("return __size;");

        toWriteToPublic.addMethod("getFFIType", Keyword.PUBLIC).setType(long.class).createBody()
                .addStatement("return __ffi_type;");

        toWriteToPublic.addMethod("asPointer", Keyword.PUBLIC).setType(structPointerRef).createBody()
                .addStatement("return new " + structPointerRef + "(getPointer(), false, 1, this);");

        // Fields
        for (int i = 0; i < fields.size(); i++) {
            StackElementField field = fields.get(i);
            NamedType fieldType = field.getType();
            fieldType.getDefinition().getMappedType().importType(cuPublic);

            if (fieldType.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY || fieldType.getDefinition().getTypeKind().isStackElement()) {
                writeStackFields(field, toWriteToPublic);
                continue;
            }

            MethodDeclaration getMethod = toWriteToPublic.addMethod(fieldType.getName(), Keyword.PUBLIC);
            if (field.getComment() != null)
                getMethod.setJavadocComment(field.getComment());
            getMethod.setType(fieldType.getDefinition().getMappedType().abstractType());
            BlockStmt getBody = new BlockStmt();

            getBody.addStatement(new ReturnStmt(fieldType.getDefinition().getMappedType().fromC(fieldType.getDefinition().getMappedType().readFromBufferPtr(new MethodCallExpr("getBufPtr"), getFieldOffsetAsExpression(i)))));
            getMethod.setBody(getBody);

            MethodDeclaration setMethod = toWriteToPublic.addMethod(fieldType.getName(), Keyword.PUBLIC);
            setMethod.addParameter(fieldType.getDefinition().getMappedType().abstractType(), fieldType.getName());
            if (field.getComment() != null) setMethod.setJavadocComment(field.getComment());
            BlockStmt setBody = new BlockStmt();

            setBody.addStatement(fieldType.getDefinition().getMappedType().writeToBufferPtr(new MethodCallExpr("getBufPtr"), getFieldOffsetAsExpression(i), fieldType.getDefinition().getMappedType().toC(new NameExpr(fieldType.getName()))));
            setMethod.setBody(setBody);
        }

        // Pointer
        ClassOrInterfaceDeclaration pointerClass = new ClassOrInterfaceDeclaration(
                new NodeList<>(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier()), false,
                pointerName);

        toWriteToPublic.addMember(pointerClass);
        pointerClass.addExtendedType("StackElementPointer<" + javaTypeName + ">");
        ConstructorDeclaration pointerConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerConstructor.addParameter(new Parameter(PrimitiveType.longType(), "pointer"));
        pointerConstructor.addParameter(new Parameter(PrimitiveType.booleanType(), "freeOnGC"));
        BlockStmt body = new BlockStmt();
        body.addStatement("super(pointer, freeOnGC);");
        pointerConstructor.setBody(body);

        ConstructorDeclaration pointerConstructorCapacity = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerConstructorCapacity.addParameter(
                new Parameter(com.github.javaparser.ast.type.PrimitiveType.longType(), "pointer"));
        pointerConstructorCapacity.addParameter(new Parameter(PrimitiveType.booleanType(), "freeOnGC"));
        pointerConstructorCapacity.addParameter(new Parameter(PrimitiveType.intType(), "capacity"));
        BlockStmt bodyCapacity = new BlockStmt();
        bodyCapacity.addStatement("super(pointer, freeOnGC, capacity * __size);");
        pointerConstructorCapacity.setBody(bodyCapacity);

        ConstructorDeclaration pointerAndParentTakingConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerAndParentTakingConstructor.addParameter(long.class, "pointer");
        pointerAndParentTakingConstructor.addParameter(boolean.class, "freeOnGC");
        pointerAndParentTakingConstructor.addParameter("Pointing", "parent");
        pointerAndParentTakingConstructor.getBody().addStatement("super(pointer, freeOnGC);")
                .addStatement("setParent(parent);");

        ConstructorDeclaration pointerAndParentAndCapacityTakingConstructor = pointerClass.addConstructor(Keyword.PUBLIC);
        pointerAndParentAndCapacityTakingConstructor.addParameter(long.class, "pointer");
        pointerAndParentAndCapacityTakingConstructor.addParameter(boolean.class, "freeOnGC");
        pointerAndParentAndCapacityTakingConstructor.addParameter(int.class, "capacity");
        pointerAndParentAndCapacityTakingConstructor.addParameter("Pointing", "parent");
        pointerAndParentAndCapacityTakingConstructor.getBody().addStatement("super(pointer, freeOnGC, capacity * __size);")
                .addStatement("setParent(parent);");

        pointerClass.addConstructor(Keyword.PUBLIC).getBody().addStatement("this(1, true);");

        ConstructorDeclaration defaultConstructorPointer = pointerClass.addConstructor(Keyword.PUBLIC);
        defaultConstructorPointer.addParameter(int.class, "count");
        defaultConstructorPointer.addParameter(boolean.class, "freeOnGC");
        defaultConstructorPointer.createBody().addStatement("super(__size, count, freeOnGC);");

        pointerClass.addMethod("getSize", Keyword.PUBLIC).setType(int.class).createBody()
                .addStatement("return __size;");
        pointerClass.addMethod("createStackElement", Keyword.PROTECTED).setType(javaTypeName)
                .addParameter(long.class, "ptr").addParameter(boolean.class, "freeOnGC").createBody()
                .addStatement("return new " + javaTypeName + "(ptr, freeOnGC);");

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

    private ArrayList<NamedType> getUnwrappedFields() {
        ArrayList<NamedType> unwrappedFields = new ArrayList<>();
        for (StackElementField field : fields) {
            NamedType fieldType = field.getType();
            if (fieldType.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
                for (int i = 0; i < fieldType.getDefinition().getCount(); i++) {
                    unwrappedFields.add(new NamedType(fieldType.getDefinition().getNestedDefinition(), fieldType.getName() + "_" + i));
                }
            } else {
                unwrappedFields.add(fieldType);
            }
        }

        return unwrappedFields;
    }

    public List<NamedType> getFields() {
        return fields.stream().map(StackElementField::getType).collect(Collectors.toList());
    }

    public String getFFITypeBody(String ffiResolveFunctionName) {
        ArrayList<NamedType> unwrappedFields = getUnwrappedFields();

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

    public int getUnwrappedIndex(int index) {
        int unwrappedStartIndex = 0;
        for (int i = 0; i < index; i++) {
            StackElementField field = fields.get(i);
            NamedType fieldType = field.getType();
            unwrappedStartIndex += fieldType.getDefinition().getCount();
        }

        return unwrappedStartIndex;
    }

    public int getFieldOffset(int index, boolean is32Bit, boolean isWin) {
        if (!isStruct())
            return 0;

        ArrayList<NamedType> unwrappedFields = getUnwrappedFields();

        if (index < 0 || index >= getFields().size())
            throw new IndexOutOfBoundsException("Field index " + index + " is out of bounds for struct with " + getFields().size() + " fields");

        int unwrappedStartIndex = getUnwrappedIndex(index);
        int offset = 0;

        for (int i = 0; i <= unwrappedStartIndex; i++) {
            NamedType fieldType = unwrappedFields.get(i);
            int fieldAlignment = fieldType.getDefinition().getMappedType().getAlignment(is32Bit, isWin);

            if (offset % fieldAlignment != 0) {
                offset += fieldAlignment - (offset % fieldAlignment);
            }

            if (i != unwrappedStartIndex) {
                int fieldSize = fieldType.getDefinition().getMappedType().getSize(is32Bit, isWin);
                offset += fieldSize;
            }
        }

        return offset;
    }

    public int getFieldSize(int index, boolean is32Bit, boolean isWin) {
        StackElementField field = fields.get(index);
        NamedType fieldType = field.getType();
        int baseSize = fieldType.getDefinition().getMappedType().getSize(is32Bit, isWin);
        if (fieldType.getDefinition().getTypeKind() == TypeKind.FIXED_SIZE_ARRAY) {
            baseSize = fieldType.getDefinition().getNestedDefinition().getMappedType().getSize(is32Bit, isWin);
        }
        return baseSize * fieldType.getDefinition().getCount();
    }

    public Expression getFieldSizeAsExpression(int index) {
        return JavaUtils.getSizeAsExpression((is32Bit, isWin) -> getFieldSize(index, is32Bit, isWin));
    }

    public Expression getFieldOffsetAsExpression(int index) {
        return JavaUtils.getOffsetAsExpression(index, this::getFieldOffset);
    }

    public boolean isStruct() {
        return definition.getTypeKind() == TypeKind.STRUCT;
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
    public Expression fromCPooled(Expression cRetrieved, Expression pool) {
        return new MethodCallExpr("getPointing")
                .setScope(pool)
                .addArgument(new ClassExpr().setType(abstractType()))
                .addArgument(cRetrieved);
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

    @Override
    public Expression writeToBufferPtr(Expression bufferPtr, Expression offset, Expression valueToWrite) {
        return new MethodCallExpr("setNativePointer", offset, valueToWrite).setScope(bufferPtr);
    }

    @Override
    public Expression readFromBufferPtr(Expression bufferPtr, Expression offset) {
        return new MethodCallExpr("getNativePointer", offset).setScope(bufferPtr);
    }

    @Override
    public int getSize(boolean is32Bit, boolean isWin) {
        int structSize = 0;
        int structAlignment = getAlignment(is32Bit, isWin);

        if (!isStruct()) {
            int maxSize = 0;
            for (NamedType fieldType : getUnwrappedFields()) {
                int fieldSize = fieldType.getDefinition().getMappedType().getSize(is32Bit, isWin);

                if (fieldSize > maxSize) {
                    maxSize = fieldSize;
                }
            }
            return maxSize;
        }

        for (NamedType fieldType : getUnwrappedFields()) {
            int fieldAlignment = fieldType.getDefinition().getMappedType().getAlignment(is32Bit, isWin);
            int fieldSize = fieldType.getDefinition().getMappedType().getSize(is32Bit, isWin);

            if (structSize % fieldAlignment != 0) {
                structSize += fieldAlignment - (structSize % fieldAlignment);
            }

            structSize += fieldSize;
        }

        if (structAlignment != 0 && structSize % structAlignment != 0) {
            structSize += structAlignment - (structSize % structAlignment);
        }

        return structSize;
    }

    @Override
    public int getAlignment(boolean is32Bit, boolean isWin) {
        int maxAlignment = 1;

        for (NamedType fieldType : getUnwrappedFields()) {
            int fieldAlignment = fieldType.getDefinition().getMappedType().getAlignment(is32Bit, isWin);

            if (fieldAlignment > maxAlignment) {
                maxAlignment = fieldAlignment;
            }
        }

        return maxAlignment;
    }

    @Override
    public int getSizeFromC(boolean is32Bit, boolean isWin) {
        return is32Bit ? 4 : 8;
    }
}
