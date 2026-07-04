package com.badlogic.gdx.jnigen.generator.types;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.HashMap;

public abstract class NativeFunction {

    protected final FunctionSignature signature;

    protected NativeFunction (FunctionSignature signature) {
        this.signature = signature;
    }

    public FunctionSignature getSignature () {
        return signature;
    }

    public abstract void write (CompilationUnit cu, ClassOrInterfaceDeclaration wrappingClass, HashMap<MethodDeclaration, String> patchNativeMethods);

    public abstract String getNativeName();

    public abstract String getCallee();

    protected final MethodDeclaration emitNativeStub (ClassOrInterfaceDeclaration wrappingClass, HashMap<MethodDeclaration, String> patchNativeMethods) {
        TypeDefinition returnType = signature.getReturnType();
        boolean isVoid = returnType.getTypeKind() == TypeKind.VOID;
        boolean isStackReturn = returnType.getTypeKind().isStackElement();

        MethodDeclaration nativeMethod = wrappingClass.addMethod(getNativeName(), Keyword.PUBLIC, Keyword.STATIC, Keyword.NATIVE);
        nativeMethod.setBody(null);
        if (isVoid || isStackReturn)
            nativeMethod.setType(void.class);
        else
            nativeMethod.setType(returnType.getMappedType().primitiveType());
        for (NamedType arg : signature.getArguments())
            nativeMethod.addParameter(arg.getDefinition().getMappedType().primitiveType(), arg.getName());
        if (isStackReturn)
            nativeMethod.addParameter(long.class, "_retPar");

        String nativeBody = buildNativeBody();
        patchNativeMethods.put(nativeMethod, nativeBody);
        return nativeMethod;
    }


    private String buildNativeBody () {
        TypeDefinition returnType = signature.getReturnType();
        NamedType[] arguments = signature.getArguments();
        boolean isVoid = returnType.getTypeKind() == TypeKind.VOID;
        boolean isStackReturn = returnType.getTypeKind().isStackElement();

        StringBuilder callArgs = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            NamedType arg = arguments[i];
            String argType = arg.getDefinition().getTypeName();
            if (i > 0)
                callArgs.append(", ");
            if (arg.getDefinition().getTypeKind().isStackElement())
                callArgs.append("*(").append(argType).append("*)").append(arg.getName());
            else
                callArgs.append("(").append(argType).append(")").append(arg.getName());
        }

        String call = getCallee() + "(" + callArgs + ")";

        StringBuilder body = new StringBuilder();
        if (isVoid) {
            body.append(call).append(";");
        } else if (isStackReturn) {
            body.append("*(").append(returnType.getTypeName()).append("*)_retPar = ").append(call).append(";");
        } else {
            body.append("return (j").append(returnType.getMappedType().primitiveType()).append(")").append(call).append(";");
        }

        for (int i = 0; i < arguments.length; i++) {
            NamedType arg = arguments[i];
            TypeKind kind = arg.getDefinition().getTypeKind();
            if (kind.isPrimitive() && kind != TypeKind.FLOAT && kind != TypeKind.DOUBLE) {
                String ret = (isVoid || isStackReturn) ? "return" : "return 0";
                body.insert(0, "CHECK_AND_THROW_C_TYPE(env, " + arg.getDefinition().getTypeName() + ", "
                        + arg.getName() + ", " + i + ", " + ret + ");\n");
            }
        }

        body.insert(0, "HANDLE_JAVA_EXCEPTION_START()\n");
        body.append("\nHANDLE_JAVA_EXCEPTION_END()");
        if (!isVoid && !isStackReturn)
            body.append("\nreturn 0;");

        return body.toString();
    }
}
