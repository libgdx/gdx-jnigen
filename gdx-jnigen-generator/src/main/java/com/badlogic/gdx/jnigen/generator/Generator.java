package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.parser.EnumParser;
import com.badlogic.gdx.jnigen.generator.parser.StackElementParser;
import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.FunctionSignature;
import com.badlogic.gdx.jnigen.generator.types.FunctionType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.badlogic.gdx.jnigen.generator.types.TypeKind;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXIndex;
import org.bytedeco.llvm.clang.CXSourceLocation;
import org.bytedeco.llvm.clang.CXTranslationUnit;
import org.bytedeco.llvm.clang.CXType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.bytedeco.llvm.global.clang.*;

public class Generator {

    private static File createTempParsableFile(String fileToParse) {
        try {
            Path path = Files.createTempFile("jnigen-generator", ".c");
            Files.write(path, ("#include <" + fileToParse + ">\n").getBytes());
            return path.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerCXType(CXType type) {
        registerCXType(type, null);
    }

    public static void registerCXType(CXType type, String typedefName) {
        if (clang_getTypeDeclaration(type).kind() == CXCursor_TypedefDecl) {
            CXType typeDef = clang_getTypedefDeclUnderlyingType(clang_getTypeDeclaration(type));
            Manager.getInstance().registerTypeDef(clang_getTypedefName(type).getString(), clang_getTypeSpelling(typeDef).getString());
            registerCXType(typeDef, clang_getTypedefName(type).getString());
            return;
        }

        TypeKind typeKind = TypeKind.getTypeKind(type);

        String name = clang_getTypeSpelling(type).getString();
        if (typeKind == TypeKind.CLOSURE) {
            if (typedefName == null) {
                typedefName = JavaUtils.functionSignatureToName(name);
            }
            if (Manager.getInstance().hasCTypeMapping(typedefName))
                return;
            ClosureType closureType = new ClosureType(parseFunctionSignature(typedefName, type));
            Manager.getInstance().addClosure(closureType);
            return;
        }

        if (Manager.getInstance().hasCTypeMapping(name)) {
            return;
        }

        if (type.kind() == CXType_Pointer) {
            CXType pointee = clang_getPointeeType(type);
            if (pointee.kind() == 0)
                return;
            registerCXType(pointee, typedefName);
            return;
        }

        if (type.kind() == CXType_IncompleteArray || type.kind() == CXType_ConstantArray) {
            registerCXType(clang_getArrayElementType(type), typedefName);
            return;
        }

        if (typeKind == TypeKind.STACK_ELEMENT) {
            new StackElementParser(type).register();
        } else if (typeKind == TypeKind.ENUM) {
            new EnumParser(type).register();
        }
    }

    public static FunctionSignature parseFunctionSignature(String name, CXType functionType) {
        CXType returnType = clang_getResultType(functionType);
        registerCXType(returnType);
        TypeDefinition returnDefinition = TypeDefinition.createTypeDefinition(returnType);
        int numArgs = clang_getNumArgTypes(functionType);
        NamedType[] argTypes = new NamedType[numArgs];
        for (int i = 0; i < numArgs; i++) {
            CXType argType = clang_getArgType(functionType, i);
            registerCXType(argType);
            // TODO: To retrieve the parameter name if available, we should utilise another visitor
            //  However, I decided that I don't care for the moment
            argTypes[i] = new NamedType(TypeDefinition.createTypeDefinition(argType), "arg" + i);
        }
        return new FunctionSignature(name, argTypes, returnDefinition);
    }

    public static void parse(String fileToParse, String[] options) {
        // What does 0,1 mean? Who knows!
        CXIndex index = clang_createIndex(0,1);
        BytePointer file = new BytePointer(createTempParsableFile(fileToParse).getAbsolutePath());

        String[] includePaths = ClangUtils.getIncludePaths();
        String[] parameter = new String[options.length + includePaths.length];
        System.arraycopy(includePaths, 0, parameter, 0, includePaths.length);
        System.arraycopy(options, 0, parameter, includePaths.length, options.length);

        PointerPointer<BytePointer> argPointer = new PointerPointer<>(parameter);
        CXTranslationUnit translationUnit = clang_parseTranslationUnit(index, file, argPointer, parameter.length, null, 0,
                CXTranslationUnit_SkipFunctionBodies | CXTranslationUnit_DetailedPreprocessingRecord | CXTranslationUnit_IncludeAttributedTypes);

        CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(@ByVal CXCursor current, @ByVal CXCursor parent, CXClientData cxClientData) {
                CXSourceLocation location = clang_getCursorLocation(current);
                if (clang_Location_isInSystemHeader(location) != 0)
                    return CXChildVisit_Continue;

                String name = clang_getCursorSpelling(current).getString(); // Why the hell does `getString` dispose the CXString?
                if (current.kind() == CXCursor_FunctionDecl) {
                    CXType funcType = clang_getCursorType(current);
                    Manager.getInstance().addFunction(new FunctionType(parseFunctionSignature(name, funcType)));
                }

                return CXChildVisit_Recurse;
            }
        };

        clang_visitChildren(clang_getTranslationUnitCursor(translationUnit), visitor, null);
        argPointer.close();
        file.close();
        clang_disposeTranslationUnit(translationUnit);
        clang_disposeIndex(index);
    }

    public static void generateJavaCode(String path) {
        Manager.getInstance().emit(path);
    }

    public static void execute(String path, String basePackage, String fileToParse, String[] options) {
        if (!path.endsWith("/"))
            path += "/";
        Manager.init(fileToParse, basePackage);
        parse(fileToParse, options);
        generateJavaCode(path);
    }

    public static void main(String[] args) {
        String[] options = new String[args.length - 3];
        System.arraycopy(args, 3, options, 0, options.length);
        execute(args[0], args[1], args[2], options);
    }
}
