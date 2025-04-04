package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.WildcardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalType implements MappedType {

    private final HashMap<String, ClosureType> closures = new HashMap<>();
    private final List<FunctionType> functions = new ArrayList<>();

    private final String globalName;

    public GlobalType(String globalName) {
        this.globalName = globalName;
    }

    public GlobalType duplicate() {
        GlobalType clone = new GlobalType(globalName);
        clone.functions.addAll(functions);
        clone.closures.putAll(closures);
        return clone;
    }

    public void addClosure(ClosureType closureType) {
        if (closures.containsKey(closureType.getName()))
            throw new IllegalArgumentException("Closure with name: " + closureType.getName() + " already exists.");
        closures.put(closureType.getName(), closureType);
    }

    public void addFunction(FunctionType functionType) {
        functions.add(functionType);
    }

    public List<FunctionType> getFunctions() {
        return functions;
    }

    public void write(CompilationUnit cuPublic, ClassOrInterfaceDeclaration global, CompilationUnit cuInternal, ClassOrInterfaceDeclaration globalInternal, HashMap<MethodDeclaration, String> patchNativeMethods) {
        cuPublic.addImport(ClassNameConstants.CXXEXCEPTION_CLASS);
        cuPublic.addImport(IllegalArgumentException.class);
        global.addStaticInitializer()
                .addStatement("CHandler.init();")
                .addStatement("FFITypes.init();")
                .addStatement("init(IllegalArgumentException.class, CXXException.class);");
        global.addMethod("initialize", Keyword.PUBLIC, Keyword.STATIC).createBody();
        global.addOrphanComment(new BlockComment("JNI\n#include <jnigen.h>\n"
                + "#include <" + Manager.getInstance().getParsedCHeader() + ">\n\n"
                + "static jclass illegalArgumentExceptionClass = NULL;\n"
                + "static jclass cxxExceptionClass = NULL;\n"));

        MethodDeclaration initMethod = global.addMethod("init", Keyword.PRIVATE, Keyword.STATIC, Keyword.NATIVE);
        initMethod.setType(void.class)
                .addParameter(new ClassOrInterfaceType().setName("Class").setTypeArguments(new WildcardType()), "illegalArgumentException")
                .addParameter(new ClassOrInterfaceType().setName("Class").setTypeArguments(new WildcardType()), "cxxException")
                .setBody(null);
        patchNativeMethods.put(initMethod, "illegalArgumentExceptionClass = (jclass)env->NewGlobalRef(illegalArgumentException);\n"
                + "cxxExceptionClass = (jclass)env->NewGlobalRef(cxxException);");

        for (FunctionType functionType : functions) {
            functionType.write(cuPublic, global, patchNativeMethods);
        }

        for (ClosureType closureType : closures.values()) {
            ClassOrInterfaceDeclaration declaration = closureType.generateClass();
            ClassOrInterfaceDeclaration declarationInternal = closureType.generateClassInternal();
            closureType.write(cuPublic, declaration, cuInternal, declarationInternal);
            global.addMember(declaration);
            globalInternal.addMember(declarationInternal);
        }
    }

    @Override
    public void importType(CompilationUnit cu) {
        cu.addImport(classFile());
    }

    @Override
    public String instantiationType() {
        throw new IllegalArgumentException();
    }

    @Override
    public String abstractType() {
        return globalName;
    }

    @Override
    public String primitiveType() {
        throw new IllegalArgumentException();
    }

    @Override
    public String classFile() {
        return Manager.getInstance().getBasePackage() + "." + globalName;
    }

    @Override
    public String packageName() {
        return Manager.getInstance().getBasePackage();
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public Expression toC(Expression cSend) {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public int typeID() {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public Expression readFromBufferPtr(Expression bufferPtr, Expression offset) {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public Expression writeToBufferPtr(Expression bufferPtr, Expression offset, Expression valueToWrite) {
        throw new IllegalArgumentException("Should not reach");
    }

    @Override
    public int getSize(boolean is32Bit, boolean isWin) {
        throw new IllegalArgumentException("Should not reach");
    }
}
