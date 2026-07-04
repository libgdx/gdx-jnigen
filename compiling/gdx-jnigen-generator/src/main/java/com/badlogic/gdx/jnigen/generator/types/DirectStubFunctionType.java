package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.HashMap;
import java.util.List;

public class DirectStubFunctionType extends NativeFunction {

    private final String name;
    private final MappedType host;

    public DirectStubFunctionType(FunctionSignature signature, MappedType parent, MappedType host) {
        super(signature);
        this.host = host;

        if (parent instanceof GlobalType)
            name = signature.getName() + "_direct";
        else
            name = parent.abstractType() + "_" + signature.getName() + "_direct";

    }

    @Override
    public void write(CompilationUnit cu, ClassOrInterfaceDeclaration wrappingClass,
                      HashMap<MethodDeclaration, String> patchNativeMethods) {

        MethodDeclaration declaration = emitNativeStub(wrappingClass, patchNativeMethods);

        Parameter param = new Parameter(PrimitiveType.longType(), "fnPtr");
        declaration.getParameters().addFirst(param);
    }

    public MethodCallExpr buildCall(List<Expression> args) {
        MethodCallExpr call = new MethodCallExpr(name);
        call.setScope(new NameExpr(host.abstractType()));
        for (Expression arg : args) {
            call.addArgument(arg);
        }
        return call;
    }

    @Override
    public String getNativeName() {
        return name;
    }

    @Override
    public String getCallee() {
        TypeDefinition returnType = signature.getReturnType();
        NamedType[] arguments = signature.getArguments();

        StringBuilder fnSigArgs = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (i > 0)
                fnSigArgs.append(", ");
            fnSigArgs.append(arguments[i].getDefinition().getTypeName());
        }
        String castReturnType = returnType.getTypeKind() == TypeKind.VOID ? "void" : returnType.getTypeName();
        return  "((" + castReturnType + "(*)(" + fnSigArgs + "))fnPtr)";
    }
}
