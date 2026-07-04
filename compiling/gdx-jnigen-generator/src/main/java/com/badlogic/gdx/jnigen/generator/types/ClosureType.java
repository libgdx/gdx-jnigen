package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.PossibleTarget;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.UnknownType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClosureType implements MappedType, WritableClass {

    private final FunctionSignature signature;
    private final MappedType parent;
    private final DirectStubFunctionType directStub;
    private String comment;

    public ClosureType(FunctionSignature signature, MappedType parent, DirectStubFunctionType directStub) {
        this.signature = signature;
        this.parent = parent;
        this.directStub = directStub;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public ClassOrInterfaceDeclaration generateClass() {
        return new ClassOrInterfaceDeclaration()
                .setInterface(true)
                .addExtendedType("Closure")
                .addExtendedType(internalClassName())
                .setModifier(Keyword.PUBLIC, true)
                .setName(signature.getName());
    }

    @Override
    public ClassOrInterfaceDeclaration generateClassInternal() {
        return new ClassOrInterfaceDeclaration()
                .setInterface(true)
                .addExtendedType("Closure")
                .setModifier(Keyword.PUBLIC, true)
                .setName(getInternalName());
    }

    public void writeHelper(CompilationUnit cuPrivate, ClassOrInterfaceDeclaration closureHelperClass) {
        cuPrivate.addImport(ClassNameConstants.CCLOSUREOBJECT_CLASS);

        TypeDefinition returnType = signature.getReturnType();
        NamedType[] arguments = signature.getArguments();
        boolean isStackReturn = returnType.getTypeKind().isStackElement();

        MethodDeclaration downcallMethod = closureHelperClass.addMethod(getName() + "_downcall",
                Keyword.PUBLIC, Keyword.STATIC);
        downcallMethod.addParameter(long.class, "fnPtr");
        downcallMethod.setType("CClosureObject<" + parent.abstractType() + "." + getName() + ">");
        BlockStmt methodBody = downcallMethod.createBody();

        LambdaExpr lambda = new LambdaExpr();
        lambda.setEnclosingParameters(true);
        for (NamedType arg : arguments) {
            arg.getDefinition().getMappedType().importType(cuPrivate);
            lambda.addAndGetParameter(new UnknownType(), arg.getName());
        }

        BlockStmt lambdaBody = new BlockStmt();

        List<Expression> callArgs = new ArrayList<>();
        callArgs.add(new NameExpr("fnPtr"));
        for (NamedType arg : arguments) {
            callArgs.add(arg.getDefinition().getMappedType().toC(new NameExpr(arg.getName())));
        }

        if (isStackReturn) {
            MappedType retMapped = returnType.getMappedType();
            retMapped.importType(cuPrivate);
            VariableDeclarator decl = new VariableDeclarator()
                    .setType(retMapped.abstractType())
                    .setName("_retPar")
                    .setInitializer(new ObjectCreationExpr().setType(retMapped.abstractType()));
            lambdaBody.addStatement(new ExpressionStmt(new VariableDeclarationExpr(decl)));
            callArgs.add(new MethodCallExpr(new NameExpr("_retPar"), "getPointer"));
            MethodCallExpr directCall = directStub.buildCall(callArgs);
            lambdaBody.addStatement(new ExpressionStmt(directCall));
            lambdaBody.addStatement(new ReturnStmt(new NameExpr("_retPar")));
        } else if (returnType.getTypeKind() == TypeKind.VOID) {
            MethodCallExpr directCall = directStub.buildCall(callArgs);
            lambdaBody.addStatement(new ExpressionStmt(directCall));
        } else {
            MethodCallExpr directCall = directStub.buildCall(callArgs);
            lambdaBody.addStatement(new ReturnStmt(returnType.getMappedType().fromC(directCall)));
        }

        lambda.setBody(lambdaBody);

        ObjectCreationExpr closureObjectCreation = new ObjectCreationExpr()
                .setType("CClosureObject<>")
                .addArgument(lambda)
                .addArgument("fnPtr");

        methodBody.addStatement(new ReturnStmt(closureObjectCreation));
    }

    @Override
    public void write(CompilationUnit cuPublic, ClassOrInterfaceDeclaration toWriteToPublic, CompilationUnit cuPrivate, ClassOrInterfaceDeclaration toWriteToPrivate) {
        String name = signature.getName();
        TypeDefinition returnType = signature.getReturnType();
        NamedType[] arguments = signature.getArguments();
        if (!returnType.getMappedType().isLibFFIConvertible() || !Arrays.stream(arguments).map(namedType -> namedType.getDefinition().getMappedType()).allMatch(MappedType::isLibFFIConvertible)) {
            throw new IllegalArgumentException("Unions are not allowed to be passed via stack from/to a closure, failing closure: " + name);
        }

        importType(cuPublic);
        cuPublic.addImport(ClassNameConstants.CLOSURE_CLASS);
        cuPublic.addImport(parent.internalClass());

        cuPrivate.addImport(ClassNameConstants.CTYPEINFO_CLASS);
        cuPrivate.addImport(ClassNameConstants.CLOSURE_CLASS);
        cuPrivate.addImport(ClassNameConstants.BUFFER_PTR);
        cuPrivate.addImport(ClassNameConstants.POINTINGPOOLMANAGER_CLASS);
        parent.importType(cuPrivate);

        NodeList<Expression> arrayInitializerExpr = new NodeList<>();
        ArrayCreationExpr arrayCreationExpr = new ArrayCreationExpr();
        arrayCreationExpr.setElementType("CTypeInfo");
        arrayCreationExpr.setInitializer(new ArrayInitializerExpr(arrayInitializerExpr));

        toWriteToPrivate.addFieldWithInitializer("CTypeInfo[]", "__ffi_cache", arrayCreationExpr);

        MethodDeclaration callMethod = toWriteToPublic.addMethod(name + "_call");
        if (comment != null)
            callMethod.setJavadocComment(comment);

        callMethod.setBody(null);
        for (NamedType namedType : arguments) {
            namedType.getDefinition().getMappedType().importType(cuPublic);
            namedType.getDefinition().getMappedType().importType(cuPrivate);
            callMethod.addAndGetParameter(namedType.getDefinition().getMappedType().abstractType(), namedType.getName());
            MethodCallExpr getTypeID = new MethodCallExpr("getCTypeInfo");
            getTypeID.setScope(new NameExpr("FFITypes"));
            getTypeID.addArgument(new IntegerLiteralExpr(String.valueOf(namedType.getDefinition().getMappedType().typeID())));
            arrayInitializerExpr.add(getTypeID);
        }
        returnType.getMappedType().importType(cuPublic);
        returnType.getMappedType().importType(cuPrivate);
        callMethod.setType(returnType.getMappedType().abstractType());

        toWriteToPrivate.addMember(new MethodDeclaration(callMethod.getModifiers(), callMethod.getNameAsString(), callMethod.getType(), callMethod.getParameters()).setBody(null));

        MethodCallExpr getTypeID = new MethodCallExpr("getCTypeInfo");
        getTypeID.setScope(new NameExpr("FFITypes"));
        getTypeID.addArgument(new IntegerLiteralExpr(String.valueOf(returnType.getMappedType().typeID())));
        arrayInitializerExpr.add(0, getTypeID);

        toWriteToPrivate.addMethod("functionSignature", Keyword.DEFAULT)
                .setType("CTypeInfo[]")
                .createBody().addStatement("return __ffi_cache;");

        MethodDeclaration invokeMethod = toWriteToPrivate.addMethod("invoke", Keyword.DEFAULT);
        invokeMethod.addParameter("BufferPtr", "buf");
        BlockStmt invokeBody = new BlockStmt();
        MethodCallExpr callExpr = new MethodCallExpr(callMethod.getNameAsString());
        for (int i = 0; i < arguments.length; i++) {
            NamedType type = arguments[i];
            TypeDefinition definition = type.getDefinition();
            Expression fromBuf = definition.getMappedType().readFromBufferPtr(new NameExpr("buf"), JavaUtils.getOffsetAsExpression(i, this::getParameterOffset));
            callExpr.addArgument(definition.getMappedType().fromC(fromBuf));
        }

        if (returnType.getTypeKind() == TypeKind.VOID) {
            invokeBody.addStatement(callExpr);
        } else {
            Expression toBuf = returnType.getMappedType().writeToBufferPtr(new NameExpr("buf"), new IntegerLiteralExpr("0"), returnType.getMappedType().toC(callExpr));
            invokeBody.addStatement(toBuf);
        }

        invokeMethod.setBody(invokeBody);

        MethodDeclaration invokePooledMethod = toWriteToPrivate.addMethod("invokePooled", Keyword.DEFAULT);
        invokePooledMethod.addParameter("BufferPtr", "buf");
        invokePooledMethod.addParameter("PointingPoolManager", "manager");

        BlockStmt invokePooledBody = new BlockStmt();

        MethodCallExpr pooledCallExpr = new MethodCallExpr(callMethod.getNameAsString());
        for (int i = 0; i < arguments.length; i++) {
            NamedType type = arguments[i];
            TypeDefinition definition = type.getDefinition();
            Expression fromBuf = definition.getMappedType().readFromBufferPtr(new NameExpr("buf"), JavaUtils.getOffsetAsExpression(i, this::getParameterOffset));
            pooledCallExpr.addArgument(definition.getMappedType().fromCPooled(fromBuf, new NameExpr("manager")));
        }

        if (returnType.getTypeKind() == TypeKind.VOID) {
            invokePooledBody.addStatement(pooledCallExpr);
        } else {
            Expression toBuf = returnType.getMappedType().writeToBufferPtr(new NameExpr("buf"), new IntegerLiteralExpr("0"), returnType.getMappedType().toC(pooledCallExpr));
            invokePooledBody.addStatement(toBuf);
        }

        invokePooledMethod.setBody(invokePooledBody);

        writeHelper(cuPrivate, toWriteToPrivate);
    }

    private int getParameterOffset(int index, PossibleTarget target) {
        if (index < 0 || index > signature.getArguments().length)
            throw new IllegalArgumentException("Index out of bounds: " + index);

        int offset = 0;
        for (int i = 0; i < index; i++) {
            offset += signature.getArguments()[i].getDefinition().getMappedType().getSizeFromC(target);
        }

        return offset;
    }

    public String getName() {
        return signature.getName();
    }

    public String getInternalName() {
        return getName() + "_Internal";
    }

    public FunctionSignature getSignature() {
        return signature;
    }

    @Override
    public String abstractType() {
        return "ClosureObject<" + parent.abstractType() + "." + signature.getName() + ">";
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(ClassNameConstants.CLOSUREOBJECT_CLASS);
        cu.addImport(ClassNameConstants.CHANDLER_CLASS);
        parent.importType(cu);
        cu.addImport(parent.internalClass());
    }

    @Override
    public String classFile() {
        return parent.classFile();
    }

    @Override
    public String packageName() {
        return parent.packageName();
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        MethodCallExpr methodCallExpr = new MethodCallExpr("getClosureObject");
        methodCallExpr.setScope(new NameExpr("CHandler"));
        methodCallExpr.addArgument(cRetrieved);
        methodCallExpr.addArgument(internalClassName() + "::" + getName() + "_downcall");
        return methodCallExpr;
    }

    @Override
    public Expression toC(Expression cSend) {
        MethodCallExpr methodCallExpr = new MethodCallExpr("getPointer");
        methodCallExpr.setScope(cSend);
        return methodCallExpr;
    }

    @Override
    public int typeID() {
        return Manager.POINTER_FFI_ID;
    }

    @Override
    public String internalClassName() {
        return parent.internalClassName() + "." + getInternalName();
    }

    @Override
    public String internalClass() {
        return parent.internalClass() + "." + internalClassName();
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
    public int getSize(PossibleTarget target) {
        return target.is32Bit() ? 4 : 8;
    }
}
