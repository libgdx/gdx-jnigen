package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.c.CTypeInfo;
import com.badlogic.gdx.jnigen.closure.Closure;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.ffi.JavaTypeWrapper;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.Arrays;

public class ClosureType implements MappedType, WritableClass {

    private final FunctionSignature signature;
    private final MappedType parent;

    public ClosureType(FunctionSignature signature, MappedType parent) {
        this.signature = signature;
        this.parent = parent;
    }

    @Override
    public ClassOrInterfaceDeclaration generateClass() {
        return new ClassOrInterfaceDeclaration()
                .setInterface(true)
                .addExtendedType(Closure.class)
                .setModifier(Keyword.PUBLIC, true)
                .setName(signature.getName());
    }

    @Override
    public void write(CompilationUnit cu, ClassOrInterfaceDeclaration closureClass) {
        String name = signature.getName();
        TypeDefinition returnType = signature.getReturnType();
        NamedType[] arguments = signature.getArguments();
        if (!returnType.getMappedType().isLibFFIConvertible() || !Arrays.stream(arguments).map(namedType -> namedType.getDefinition().getMappedType()).allMatch(MappedType::isLibFFIConvertible)) {
            throw new IllegalArgumentException("Unions are not allowed to be passed via stack from/to a closure, failing closure: " + name);
        }

        cu.addImport(Closure.class);
        cu.addImport(JavaTypeWrapper.class);
        cu.addImport(CTypeInfo.class);

        NodeList<Expression> arrayInitializerExpr = new NodeList<>();
        ArrayCreationExpr arrayCreationExpr = new ArrayCreationExpr();
        arrayCreationExpr.setElementType(CTypeInfo[].class);
        arrayCreationExpr.setInitializer(new ArrayInitializerExpr(arrayInitializerExpr));

        closureClass.addFieldWithInitializer(CTypeInfo[].class, "__ffi_cache", arrayCreationExpr);

        MethodDeclaration callMethod = closureClass.addMethod(name + "_call");
        callMethod.setBody(null);
        for (NamedType namedType : arguments) {
            namedType.getDefinition().getMappedType().importType(cu);
            callMethod.addAndGetParameter(namedType.getDefinition().getMappedType().instantiationType(), namedType.getName());
            MethodCallExpr getTypeID = new MethodCallExpr("getCTypeInfo");
            getTypeID.setScope(new NameExpr("FFITypes"));
            getTypeID.addArgument(new IntegerLiteralExpr(String.valueOf(namedType.getDefinition().getMappedType().typeID())));
            arrayInitializerExpr.add(getTypeID);
        }
        returnType.getMappedType().importType(cu);
        callMethod.setType(returnType.getMappedType().abstractType());

        MethodCallExpr getTypeID = new MethodCallExpr("getCTypeInfo");
        getTypeID.setScope(new NameExpr("FFITypes"));
        getTypeID.addArgument(new IntegerLiteralExpr(String.valueOf(returnType.getMappedType().typeID())));
        arrayInitializerExpr.add(0, getTypeID);

        closureClass.addMethod("functionSignature", Keyword.DEFAULT)
                .setType(CTypeInfo[].class)
                .createBody().addStatement("return __ffi_cache;");

        MethodDeclaration invokeMethod = closureClass.addMethod("invoke", Keyword.DEFAULT);
        invokeMethod.addParameter(JavaTypeWrapper[].class, "parameters");
        invokeMethod.addParameter(JavaTypeWrapper.class, "returnType");
        BlockStmt invokeBody = new BlockStmt();
        MethodCallExpr callExpr = new MethodCallExpr(callMethod.getNameAsString());
        for (int i = 0; i < arguments.length; i++) {
            NamedType type = arguments[i];
            TypeDefinition definition = type.getDefinition();
            ArrayAccessExpr arrayAccessExpr = new ArrayAccessExpr(new NameExpr("parameters"), new IntegerLiteralExpr(String.valueOf(i)));
            String methodName = "asLong";
            if (type.getDefinition().getTypeKind() == TypeKind.FLOAT)
                methodName = "asFloat";
            else if (type.getDefinition().getTypeKind() == TypeKind.DOUBLE)
                methodName = "asDouble";
            MethodCallExpr methodCallExpr = new MethodCallExpr(methodName);
            methodCallExpr.setScope(arrayAccessExpr);
            callExpr.addArgument(definition.getMappedType().fromC(methodCallExpr));
        }

        if (returnType.getTypeKind() == TypeKind.VOID) {
            invokeBody.addStatement(callExpr);
        } else {
            MethodCallExpr returnTypeSetMethodCall = new MethodCallExpr("returnType.setValue", callExpr);
            invokeBody.addStatement(returnTypeSetMethodCall);
        }

        invokeMethod.setBody(invokeBody);
    }

    public String getName() {
        return signature.getName();
    }

    @Override
    public String abstractType() {
        return "ClosureObject<" + signature.getName() + ">";
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(ClosureObject.class);
        cu.addImport(classFile() + "." + signature.getName());
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
    public MappedType asPointer() {
        throw new IllegalArgumentException("To implement");
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        MethodCallExpr methodCallExpr = new MethodCallExpr("getClosureObject");
        methodCallExpr.setScope(new NameExpr("CHandler"));
        methodCallExpr.addArgument(cRetrieved);
        return methodCallExpr;
    }

    @Override
    public Expression toC(Expression cSend) {
        MethodCallExpr methodCallExpr = new MethodCallExpr("getFnPtr");
        methodCallExpr.setScope(cSend);
        return methodCallExpr;
    }

    @Override
    public int typeID() {
        return Manager.POINTER_FFI_ID;
    }
}
