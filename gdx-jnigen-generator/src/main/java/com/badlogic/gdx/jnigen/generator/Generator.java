package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.parser.EnumParser;
import com.badlogic.gdx.jnigen.generator.parser.StackElementParser;
import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.FunctionSignature;
import com.badlogic.gdx.jnigen.generator.types.FunctionType;
import com.badlogic.gdx.jnigen.generator.types.MappedType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.PointerType;
import com.badlogic.gdx.jnigen.generator.types.PrimitiveType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.badlogic.gdx.jnigen.generator.types.TypeKind;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXIndex;
import org.bytedeco.llvm.clang.CXSourceLocation;
import org.bytedeco.llvm.clang.CXSourceRange;
import org.bytedeco.llvm.clang.CXToken;
import org.bytedeco.llvm.clang.CXTranslationUnit;
import org.bytedeco.llvm.clang.CXType;
import org.bytedeco.llvm.global.clang;

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

    public static TypeDefinition registerCXType(CXType type, String alternativeName, MappedType parent) {
        TypeKind typeKind = TypeKind.getTypeKind(type);
        String name = clang_getTypeSpelling(type).getString();
        if (name.equals("_Bool"))
            name = "bool"; //TODO WHYYYY?????? Is it a typedef that gets resolved?

        // We need to do this early, because numbers may be typedefed depending on the platform.
        // To properly resolve this, we need to generate the CTypeInfo for the "highest" declaration.
        if (!typeKind.isSpecial()) {
            TypeDefinition typeDefinition = new TypeDefinition(typeKind, name);
            MappedType mappedType = PrimitiveType.fromTypeDefinition(typeDefinition);
            typeDefinition.setOverrideMappedType(mappedType);
            return typeDefinition;
        }

        if (clang_getTypeDeclaration(type).kind() == CXCursor_TypedefDecl) {
            CXType typeDef = clang_getTypedefDeclUnderlyingType(clang_getTypeDeclaration(type));
            Manager.getInstance().registerTypeDef(clang_getTypedefName(type).getString(), clang_getTypeSpelling(typeDef).getString());

            // A typedef unsets a parent, because an anonymous declaration can't be typedefed I think
            TypeDefinition lower = registerCXType(typeDef, clang_getTypedefName(type).getString(), null);
            TypeDefinition definition = new TypeDefinition(lower.getTypeKind(), clang_getTypedefName(type).getString());
            definition.setOverrideMappedType(lower.getMappedType());
            return definition;
        }

        if (typeKind == TypeKind.CLOSURE) {
            if (alternativeName == null)
                throw new IllegalArgumentException();

            if (Manager.getInstance().hasCTypeMapping(alternativeName))
                return Manager.getInstance().resolveCTypeMapping(alternativeName);

            MappedType parentMappedType = parent == null ? Manager.getInstance().getGlobalType() : parent;
            // TODO: 20.03.24 I have yet to find a way to reliably parse closure type arg names
            FunctionSignature functionSignature = parseFunctionSignature(alternativeName, type, null);

            // TODO: 19.03.24 Solve better, something like "lockMapping" idk
            if (Manager.getInstance().hasCTypeMapping(alternativeName)) // function -> closure -> struct -> same closure
                return Manager.getInstance().resolveCTypeMapping(alternativeName);

            ClosureType closureType = new ClosureType(functionSignature, parentMappedType);
            TypeDefinition typeDefinition = new TypeDefinition(TypeKind.CLOSURE, name);
            typeDefinition.setOverrideMappedType(closureType);
            typeDefinition.setAnonymous(parent != null);
            if (!typeDefinition.isAnonymous()) {
                Manager.getInstance().addClosure(closureType);
                Manager.getInstance().registerCTypeMapping(alternativeName, typeDefinition);
            }

            return typeDefinition;
        }

        if (Manager.getInstance().hasCTypeMapping(name))
            return Manager.getInstance().resolveCTypeMapping(name);


        if (type.kind() == CXType_Pointer) {
            CXType pointee = clang_getPointeeType(type);
            TypeDefinition typeDefinition = new TypeDefinition(TypeKind.POINTER, name);

            if (pointee.kind() == 0)
                typeDefinition.setOverrideMappedType(new PointerType(new TypeDefinition(TypeKind.VOID, "void")));

            TypeDefinition nested = registerCXType(pointee, alternativeName, parent);
            if (TypeKind.getTypeKind(pointee) == TypeKind.CLOSURE) {
                typeDefinition = new TypeDefinition(TypeKind.CLOSURE, name);
                typeDefinition.setOverrideMappedType(nested.getMappedType());
                typeDefinition.setAnonymous(nested.isAnonymous());
            } else {
                typeDefinition.setOverrideMappedType(nested.getMappedType().asPointer());
                typeDefinition.setNestedDefinition(nested);
            }
            return typeDefinition;
        }

        if (type.kind() == CXType_IncompleteArray) {
            TypeDefinition typeDefinition = new TypeDefinition(TypeKind.POINTER, name.replace("[]", "*"));
            TypeDefinition nested = registerCXType(clang_getArrayElementType(type), alternativeName, parent);
            typeDefinition.setOverrideMappedType(nested.getMappedType().asPointer());
            typeDefinition.setNestedDefinition(nested);
            return typeDefinition;
        }

        if (type.kind() == CXType_ConstantArray) {
            TypeDefinition typeDefinition = new TypeDefinition(TypeKind.FIXED_SIZE_ARRAY, name);
            typeDefinition.setCount((int)clang.clang_getArraySize(type));
            TypeDefinition nested = registerCXType(clang_getArrayElementType(type), alternativeName, parent);
            typeDefinition.setOverrideMappedType(nested.getMappedType().asPointer());
            typeDefinition.setNestedDefinition(nested);
            return typeDefinition;
        }

        if (typeKind == TypeKind.STACK_ELEMENT) {
            TypeDefinition typeDefinition = new TypeDefinition(TypeKind.STACK_ELEMENT, name);
            typeDefinition.setAnonymous(clang_Cursor_isAnonymous(clang.clang_getTypeDeclaration(type)) != 0);
            Manager.getInstance().registerCTypeMapping(name, typeDefinition);
            StackElementParser parser = new StackElementParser(typeDefinition, type, alternativeName, parent);

            typeDefinition.setOverrideMappedType(parser.getStackElementType());
            parser.parseMappedType();
            return typeDefinition;
        } else if (typeKind == TypeKind.ENUM) {
            TypeDefinition typeDefinition = new TypeDefinition(TypeKind.ENUM, name);
            Manager.getInstance().registerCTypeMapping(name, typeDefinition);

            typeDefinition.setOverrideMappedType(new EnumParser(typeDefinition, type, alternativeName).register());
            return typeDefinition;
        }

        throw new IllegalArgumentException("Should not reach");
    }

    // TODO: Pass proper parameter names
    public static FunctionSignature parseFunctionSignature(String functionName, CXType functionType, CXCursor cursor) {
        if (clang_isFunctionTypeVariadic(functionType) != 0)
            throw new IllegalArgumentException("Function " + functionName + " is variadic, which is currently not supported");

        CXType returnType = clang_getResultType(functionType);
        TypeDefinition returnDefinition = registerCXType(returnType, "ret", null);

        int numArgs = clang_getNumArgTypes(functionType);
        NamedType[] argTypes = new NamedType[numArgs];
        for (int i = 0; i < numArgs; i++) {
            CXType argType = clang_getArgType(functionType, i);
            if (clang_getTypeSpelling(argType).getString().equals("va_list"))
                throw new IllegalArgumentException("Function " + functionName + " has va_list parameter, which is currently not supported");

            String name = "arg" + i;
            if (cursor != null) {
                CXCursor paramCursor = clang_Cursor_getArgument(cursor, i);
                String potentialName = clang_getCursorSpelling(paramCursor).getString();
                if (!potentialName.isEmpty())
                    name = potentialName;
            }
            TypeDefinition argTypeDefinition = registerCXType(argType, name, null);

            argTypes[i] = new NamedType(argTypeDefinition, name);
        }
        return new FunctionSignature(functionName, argTypes, returnDefinition);
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
                    try {
                        Manager.startNewManager();
                        FunctionSignature functionSignature = parseFunctionSignature(name, funcType, current);
                        Manager.getInstance().addFunction(new FunctionType(functionSignature));
                    }catch (Throwable e) {
                        Manager.rollBack();
                        System.err.println("Failed to parse function: " + name);
                        e.printStackTrace();
                    }
                } else if (current.kind() == CXCursor_MacroDefinition) {
                    if (clang_Cursor_isMacroBuiltin(current) == 0 && clang_Cursor_isMacroFunctionLike(current) == 0) {
                        CXSourceRange range = clang_getCursorExtent(current);
                        CXToken tokens = new CXToken(null);
                        IntPointer nTokens = new IntPointer(1);
                        clang_tokenize(translationUnit, range, tokens, nTokens);
                        String tokenizedName = clang_getTokenSpelling(translationUnit, tokens.position(0)).getString();
                        StringBuilder value = new StringBuilder();

                        for (int i = 1; i < nTokens.get(); i++) {
                            value.append(clang_getTokenSpelling(translationUnit, tokens.position(i)).getString());
                        }
                        Manager.getInstance().registerMacro(tokenizedName, value.toString());
                    }
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
