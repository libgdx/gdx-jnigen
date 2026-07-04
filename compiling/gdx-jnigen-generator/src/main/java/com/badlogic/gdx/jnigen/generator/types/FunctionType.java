package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.HashMap;

public class FunctionType extends NativeFunction {

    private final String comment;

    public FunctionType(FunctionSignature signature, String comment) {
        super(signature);
        this.comment = comment;
    }

    @Override
    public String getNativeName() {
        return signature.getName() + "_internal";
    }

    @Override
    public String getCallee() {
        return signature.getName();
    }

    @Override
    public void write(CompilationUnit cu, ClassOrInterfaceDeclaration wrappingClass, HashMap<MethodDeclaration, String> patchNativeMethod) {
        String name = signature.getName();
        TypeDefinition returnType = signature.getReturnType();
        NamedType[] arguments = signature.getArguments();

        MethodDeclaration callMethodCreate = wrappingClass.addMethod(name, Keyword.PUBLIC, Keyword.STATIC);
        if (comment != null)
            callMethodCreate.setJavadocComment(comment);
        BlockStmt body = new BlockStmt();

        callMethodCreate.setType(returnType.getMappedType().abstractType());
        returnType.getMappedType().importType(cu);

        MethodCallExpr callExprCreate = new MethodCallExpr(getNativeName());

        for (NamedType namedType : arguments) {
            namedType.getDefinition().getMappedType().importType(cu);
            callMethodCreate.addParameter(namedType.getDefinition().getMappedType().abstractType(), namedType.getName());

            callExprCreate.addArgument(namedType.getDefinition().getMappedType().toC(new NameExpr(namedType.getName())));
        }

        if (returnType.getTypeKind() != TypeKind.VOID) {
            if (returnType.getTypeKind().isPrimitive()) {
                body.addStatement(new ReturnStmt(callExprCreate));
            } else {
                if (returnType.getTypeKind() == TypeKind.POINTER || returnType.getTypeKind().isStackElement()) {
                    MethodDeclaration callMethodParameter = callMethodCreate.clone();
                    callMethodParameter.addParameter(returnType.getMappedType().abstractType(), "_retPar");
                    callMethodParameter.setType(void.class);

                    if (returnType.getTypeKind().isStackElement()) {
                        MethodCallExpr callExprParameter = callExprCreate.clone();
                        callExprParameter.addArgument(returnType.getMappedType().toC(new NameExpr("_retPar")));

                        BlockStmt bodyParameter = body.clone();
                        bodyParameter.addStatement(callExprParameter);
                        callMethodParameter.setBody(bodyParameter);

                        // Allocate the returned struct java side, so native code never mallocs memory that another
                        // library would free (separate CRT heaps on Windows /MT or with statically linked libc)
                        body.addStatement(returnType.getMappedType().abstractType() + " _retPar = new " + returnType.getMappedType().instantiationType() + "();");
                        body.addStatement(callExprParameter.clone());
                        body.addStatement(new ReturnStmt(new NameExpr("_retPar")));
                    } else {
                        PointerType pointerType = (PointerType) returnType.getMappedType();
                        BlockStmt bodyPar = callMethodParameter.createBody();
                        bodyPar.addStatement(new MethodCallExpr(new NameExpr("_retPar"), "setPointer", new NodeList<>(callExprCreate)));

                        if (pointerType.isPointerPointer()) {
                            bodyPar.addStatement(new MethodCallExpr(new NameExpr("_retPar"), "setPointerSupplier", new NodeList<>(pointerType.getPointerPointerSupplier())));
                        }
                    }

                    wrappingClass.addMember(callMethodParameter);
                }

                if (!returnType.getTypeKind().isStackElement()) {
                    body.addStatement(new ReturnStmt(returnType.getMappedType().fromC(callExprCreate)));
                }
            }
        } else {
            body.addStatement(callExprCreate);
        }
        callMethodCreate.setBody(body);

        emitNativeStub(wrappingClass, patchNativeMethod);
    }
}
