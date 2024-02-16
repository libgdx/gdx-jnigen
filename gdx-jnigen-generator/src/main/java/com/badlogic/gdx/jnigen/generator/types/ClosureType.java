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
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

public class ClosureType implements MappedType {

    private final String name;
    private final NamedType[] arguments;
    private final TypeDefinition returnType;

    public ClosureType(String name, TypeDefinition returnType, NamedType[] arguments) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public void write(CompilationUnit cu, ClassOrInterfaceDeclaration wrappingClass) {
        cu.addImport(Closure.class);
        cu.addImport(JavaTypeWrapper.class);
        cu.addImport(CTypeInfo.class);
        ClassOrInterfaceDeclaration closureClass = new ClassOrInterfaceDeclaration()
                .setInterface(true)
                .addExtendedType(Closure.class)
                .setModifier(Keyword.PUBLIC, true)
                .setName(name);

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


        wrappingClass.addMember(closureClass);
    }

    public String getName() {
        return name;
    }

    @Override
    public String abstractType() {
        return "ClosureObject<" + name + ">";
    }

    @Override
    public String primitiveType() {
        return long.class.getName();
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(ClosureObject.class);
        cu.addImport(classFile() + "." + name);
    }

    @Override
    public String classFile() {
        return Manager.getInstance().getGlobalType().classFile();
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage();
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
