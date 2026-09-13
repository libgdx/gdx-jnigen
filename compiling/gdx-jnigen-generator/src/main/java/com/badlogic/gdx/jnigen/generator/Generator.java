package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.parser.CommentParser;
import com.badlogic.gdx.jnigen.generator.parser.EnumParser;
import com.badlogic.gdx.jnigen.generator.parser.StackElementParser;
import com.badlogic.gdx.jnigen.generator.types.*;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXFile;
import org.bytedeco.llvm.clang.CXDiagnostic;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static TypeDefinition registerCXType(CXType type, String alternativeName, DeclaringMember declaringMember) {
        if (type.kind() == CXType_Attributed)
            type = clang_Type_getModifiedType(type);

        TypeKind typeKind = TypeKind.getTypeKind(type);
        String name = clang_getTypeSpelling(type).getString();
        if (name.equals("_Bool"))
            name = "bool"; //TODO WHYYYY?????? Is it a typedef that gets resolved?

        // We need to do this early, because numbers may be typedefed depending on the platform.
        // To properly resolve this, we need to generate the CTypeInfo for the "highest" declaration.
        if (!typeKind.isSpecial()) {
            TypeDefinition typeDefinition = Manager.getInstance().defineType(typeKind, name, clang_Type_getSizeOf(type), clang_Type_getAlignOf(type));
            MappedType mappedType = new PrimitiveType(typeDefinition);
            typeDefinition.setOverrideMappedType(mappedType);
            return typeDefinition;
        }

        if (clang_getTypeDeclaration(type).kind() == CXCursor_TypedefDecl) {
            CXType typeDef = clang_getTypedefDeclUnderlyingType(clang_getTypeDeclaration(type));
            Manager.getInstance().registerTypeDef(clang_getTypedefName(type).getString(), clang_getTypeSpelling(typeDef).getString());

            // Stop resolving aliases once they reach a system-header declaration: the underlying name
            // (struct __sFILE / __sighandler_t / enum __color) is a platform-specific implementation
            // detail, whereas the typedef (FILE / sig_t / color) is the stable, portable public name.
            // Bind the alias itself, dispatched by the underlying kind - structs/unions become opaque,
            // while enums still bind their constants and closures still bind their signature; only the
            // name changes.
            CXCursor underlyingDecl = clang_getTypeDeclaration(typeDef);
            if (clang_Location_isInSystemHeader(clang_getCursorLocation(underlyingDecl)) != 0) {
                TypeKind underlyingKind = TypeKind.getTypeKind(typeDef);
                if (underlyingKind.isStackElement()) {
                    if (Manager.getInstance().hasCTypeMapping(name))
                        return Manager.getInstance().resolveCTypeMapping(name);
                    return registerStackElementType(type, underlyingKind, name, null, true);
                }
                if (underlyingKind == TypeKind.ENUM) {
                    if (Manager.getInstance().hasCTypeMapping(name))
                        return Manager.getInstance().resolveCTypeMapping(name);
                    return registerEnumType(typeDef, name, name, true);
                }
                if (isFunctionPointer(typeDef)) {
                    if (Manager.getInstance().hasCTypeMapping(name))
                        return Manager.getInstance().resolveCTypeMapping(name);
                    return registerClosureType(clang_getPointeeType(clang_getCanonicalType(type)), name, name, null, underlyingDecl);
                }
                // else: plain pointer / primitive underlying -> fall through to the normal recursion
            }

            // A typedef unsets a parent, because an anonymous declaration can't be typedefed I think
            String typedefName = clang_getTypedefName(type).getString();
            TypeDefinition lower = registerCXType(typeDef, typedefName, null);
            if (lower.getTypeKind().isStackElement())
                lower.setTypedefName(typedefName);
            if (lower.getTypeKind() == TypeKind.CLOSURE) {
                // As the type system does not retain argument names, we need to reparse it here
                patchClosureTypeWithCursor(lower, clang_getTypeDeclaration(type));
            }
            TypeDefinition definition = Manager.getInstance().defineType(lower.getTypeKind(), clang_getTypedefName(type).getString(), clang_Type_getSizeOf(type), clang_Type_getAlignOf(type));
            definition.setOverrideMappedType(lower.getMappedType());
            return definition;
        }

        if (typeKind == TypeKind.CLOSURE) {
            if (alternativeName == null)
                throw new IllegalArgumentException();
            return registerClosureType(type, name, alternativeName, declaringMember, null);
        }

        if (Manager.getInstance().hasCTypeMapping(name))
            return Manager.getInstance().resolveCTypeMapping(name);


        if (type.kind() == CXType_Pointer) {
            CXType pointee = clang_getPointeeType(type);
            TypeDefinition typeDefinition = Manager.getInstance().defineType(TypeKind.POINTER, name, clang_Type_getSizeOf(type), clang_Type_getAlignOf(type));

            if (pointee.kind() == 0)
                typeDefinition.setOverrideMappedType(new PointerType(Manager.getInstance().defineType(TypeKind.VOID, "void", clang_Type_getSizeOf(pointee), clang_Type_getAlignOf(pointee))));

            TypeDefinition nested = registerCXType(pointee, alternativeName, declaringMember);
            if (TypeKind.getTypeKind(pointee) == TypeKind.CLOSURE) {
                typeDefinition = Manager.getInstance().defineType(TypeKind.CLOSURE, name, clang_Type_getSizeOf(type), clang_Type_getAlignOf(type));
                typeDefinition.setOverrideMappedType(nested.getMappedType());
                typeDefinition.setAnonymous(nested.isAnonymous());
            } else {
                typeDefinition.setOverrideMappedType(new PointerType(nested));
                typeDefinition.setNestedDefinition(nested);
            }
            return typeDefinition;
        }

        if (type.kind() == CXType_IncompleteArray) {
            TypeDefinition typeDefinition = Manager.getInstance().defineType(TypeKind.POINTER, name.replace("[]", "*"), clang_Type_getSizeOf(type), clang_Type_getAlignOf(type));
            TypeDefinition nested = registerCXType(clang_getArrayElementType(type), alternativeName, declaringMember);
            typeDefinition.setOverrideMappedType(new PointerType(nested));
            typeDefinition.setNestedDefinition(nested);
            return typeDefinition;
        }

        if (type.kind() == CXType_ConstantArray) {
            TypeDefinition typeDefinition = Manager.getInstance().defineType(TypeKind.FIXED_SIZE_ARRAY, name, clang_Type_getSizeOf(type), clang_Type_getAlignOf(type));
            typeDefinition.setCount((int)clang.clang_getArraySize(type));
            TypeDefinition nested = registerCXType(clang_getArrayElementType(type), alternativeName, declaringMember);
            typeDefinition.setOverrideMappedType(new PointerType(nested));
            typeDefinition.setNestedDefinition(nested);
            return typeDefinition;
        }

        if (typeKind.isStackElement()) {
            boolean isSystemHeaderType = clang_Location_isInSystemHeader(clang_getCursorLocation(clang_getTypeDeclaration(type))) != 0;
            return registerStackElementType(type, typeKind, name, declaringMember, isSystemHeaderType);
        } else if (typeKind == TypeKind.ENUM) {
            return registerEnumType(type, name, alternativeName, false);
        }

        throw new IllegalArgumentException("Should not reach");
    }

    private static TypeDefinition registerStackElementType(CXType type, TypeKind typeKind, String name, DeclaringMember declaringMember, boolean isSystemHeader) {
        TypeDefinition typeDefinition = Manager.getInstance().defineType(typeKind, name, clang_Type_getSizeOf(type), clang_Type_getAlignOf(type));
        typeDefinition.setAnonymous(clang_Cursor_isAnonymous(clang.clang_getTypeDeclaration(type)) != 0);
        if (typeDefinition.isAnonymous())
            typeDefinition.setDeclaringMember(declaringMember);
        Manager.getInstance().registerCTypeMapping(name, typeDefinition);
        StackElementParser parser = new StackElementParser(typeDefinition, type);
        StackElementType stackElementType = parser.getStackElementType();
        typeDefinition.setOverrideMappedType(stackElementType);

        if (isSystemHeader) {
            // System-header structs are not bound field-by-field. If the type is incomplete even here
            // (a system opaque handle like DIR), it has no size, so only a pointer is possible.
            // Otherwise it has a real size and is emitted as a sized opaque blob (allocatable, usable
            // behind a typed pointer, but without field accessors).
            if (clang_Type_getSizeOf(type) == CXTypeLayoutError_Incomplete)
                stackElementType.markOpaque();
            else
                stackElementType.markSystemHeader();
        } else {
            parser.parseMappedType();
        }
        return typeDefinition;
    }

    private static TypeDefinition registerEnumType(CXType enumType, String name, String alternativeName, boolean forceAlternativeName) {
        TypeDefinition typeDefinition = Manager.getInstance().defineType(TypeKind.ENUM, name, clang_Type_getSizeOf(enumType), clang_Type_getAlignOf(enumType));
        Manager.getInstance().registerCTypeMapping(name, typeDefinition);
        typeDefinition.setNestedDefinition(registerCXType(clang_getEnumDeclIntegerType(clang_getTypeDeclaration(enumType)), null, null));
        typeDefinition.setOverrideMappedType(new EnumParser(typeDefinition, enumType, alternativeName, forceAlternativeName).register());
        return typeDefinition;
    }

    private static TypeDefinition registerClosureType(CXType functionProto, String name, String alternativeName, DeclaringMember declaringMember, CXCursor argNameCursor) {
        if (Manager.getInstance().hasCTypeMapping(alternativeName))
            return Manager.getInstance().resolveCTypeMapping(alternativeName);

        MappedType parentMappedType = declaringMember == null ? Manager.getInstance().getGlobalType() : declaringMember.owner.getMappedType();
        // TODO: 20.03.24 I have yet to find a way to reliably parse closure type arg names
        FunctionSignature functionSignature = parseFunctionSignature(alternativeName, functionProto, null);

        // TODO: 19.03.24 Solve better, something like "lockMapping" idk
        if (Manager.getInstance().hasCTypeMapping(alternativeName)) // function -> closure -> struct -> same closure
            return Manager.getInstance().resolveCTypeMapping(alternativeName);

        DirectStubFunctionType directStub = new DirectStubFunctionType(functionSignature, parentMappedType, Manager.getInstance().getGlobalType());
        ClosureType closureType = new ClosureType(functionSignature, parentMappedType, directStub);
        if (declaringMember != null)
            Manager.getInstance().addNestedType(declaringMember.owner, closureType);
        Manager.getInstance().getGlobalType().addFunction(directStub);
        TypeDefinition typeDefinition = Manager.getInstance().defineType(TypeKind.CLOSURE, name, clang_Type_getSizeOf(functionProto), clang_Type_getAlignOf(functionProto));
        typeDefinition.setOverrideMappedType(closureType);
        typeDefinition.setAnonymous(declaringMember != null);
        if (!typeDefinition.isAnonymous()) {
            Manager.getInstance().addClosure(closureType);
            Manager.getInstance().registerCTypeMapping(alternativeName, typeDefinition);
        }
        // When binding through a typedef, the type system drops argument names; recover them from the
        // provided declaration cursor (the inner function-pointer typedef).
        if (argNameCursor != null && !typeDefinition.isAnonymous())
            patchClosureTypeWithCursor(typeDefinition, argNameCursor);

        return typeDefinition;
    }

    private static boolean isFunctionPointer(CXType type) {
        CXType canonical = clang_getCanonicalType(type);
        if (canonical.kind() != CXType_Pointer)
            return false;
        int pointeeKind = clang_getPointeeType(canonical).kind();
        return pointeeKind == CXType_FunctionProto || pointeeKind == CXType_FunctionNoProto;
    }

    public static void patchClosureTypeWithCursor(TypeDefinition definition, CXCursor cursor) {
        if (definition.getTypeKind() != TypeKind.CLOSURE)
            throw new IllegalArgumentException("Can only reparse closures");

        if (!(definition.getMappedType() instanceof ClosureType))
            throw new IllegalArgumentException("Can only reparse closures");
        ClosureType closureType = (ClosureType)definition.getMappedType();
        CommentParser parser = new CommentParser(cursor);
        if (parser.isPresent())
            closureType.setComment(parser.parse());
        patchSignatureArgNamesWithVisitor(closureType.getSignature(), cursor);
    }

    // Clangs typesystem doesn't retain arg names, so we need to reparse them for closures
    public static void patchSignatureArgNamesWithVisitor(FunctionSignature functionSignature, CXCursor cursor) {
        AtomicInteger counter = new AtomicInteger(0);
        ClangUtils.visitChildren(cursor, (current, parent) -> {
            if (current.kind() == CXCursor_ParmDecl) {
                int id = counter.getAndIncrement();

                String name = clang_getCursorSpelling(current).getString();
                if (name.isEmpty())
                    name = "arg" + id;

                functionSignature.getArguments()[id].setName(name);
            }
            return CXChildVisit_Recurse;
        });
    }

    public static void dumpAST(CXCursor cursor, int depth) {
        String indent = IntStream.range(0, depth).mapToObj(i -> " ").collect(Collectors.joining());
        System.out.printf("%s%s: %s (kind: %s)%n",
                indent,
                clang_getCursorSpelling(cursor).getString(),
                clang_getTypeSpelling(clang_getCursorType(cursor)).getString(),
                clang_getCursorKind(cursor));

        ClangUtils.visitChildren(cursor, (child, parent) -> {
            dumpAST(child, depth + 1);
            return CXChildVisit_Continue;
        });
    }

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
                if (!potentialName.isEmpty()) {
                    name = JavaUtils.deduplicateArgumentName(potentialName);
                }
            }
            TypeDefinition argTypeDefinition = registerCXType(argType, name, null);

            argTypes[i] = new NamedType(argTypeDefinition, name);
        }
        return new FunctionSignature(functionName, argTypes, returnDefinition);
    }

    public static void parse(String fileToParse, String[] options, String passName) {
        // What does 0,1 mean? Who knows!
        CXIndex index = clang_createIndex(0,1);
        BytePointer file = new BytePointer(createTempParsableFile(fileToParse).getAbsolutePath());

        PointerPointer<BytePointer> argPointer = new PointerPointer<>(options);
        CXTranslationUnit translationUnit = clang_parseTranslationUnit(index, file, argPointer, options.length, null, 0,
                CXTranslationUnit_SkipFunctionBodies | CXTranslationUnit_DetailedPreprocessingRecord | CXTranslationUnit_IncludeAttributedTypes);

        try {
            List<String> errors = new ArrayList<>();
            int numDiagnostics = clang_getNumDiagnostics(translationUnit);
            for (int i = 0; i < numDiagnostics; i++) {
                CXDiagnostic diagnostic = clang_getDiagnostic(translationUnit, i);
                try {
                    if (clang_getDiagnosticSeverity(diagnostic) >= CXDiagnostic_Error)
                        errors.add(clang_formatDiagnostic(diagnostic, clang_defaultDiagnosticDisplayOptions()).getString());
                } finally {
                    clang_disposeDiagnostic(diagnostic);
                }
            }
            if (!errors.isEmpty()) {
                for (String error : errors)
                    System.err.println("Parsing " + fileToParse + " for " + passName + ": " + error);
                argPointer.close();
                file.close();
                clang_disposeTranslationUnit(translationUnit);
                clang_disposeIndex(index);
                throw new IllegalStateException("Parsing " + fileToParse + " for " + passName + " failed with " + errors.size()
                        + " error(s):\n" + String.join("\n", errors));
            }

            ClangUtils.visitChildren(clang_getTranslationUnitCursor(translationUnit), (current, parent) -> {
                CXSourceLocation location = clang_getCursorLocation(current);
                if (clang_Location_isInSystemHeader(location) != 0)
                    return CXChildVisit_Continue;

                String name = clang_getCursorSpelling(current).getString(); // Why the hell does `getString` dispose the CXString?
                if (current.kind() == CXCursor_FunctionDecl) {
                    CXType funcType = clang_getCursorType(current);
                    try {
                        Manager.startNewManager();
                        FunctionSignature functionSignature = parseFunctionSignature(name, funcType, current);
                        Manager.getInstance().addFunction(new FunctionType(functionSignature, new CommentParser(current).parse()));
                    }catch (Throwable e) {
                        Manager.rollBack();
                        System.err.println("Failed to parse function: " + name);
                        e.printStackTrace();
                    }
                } else if (current.kind() == CXCursor_MacroDefinition) {
                    // Predefined macros live in the <built-in> and <command line> pseudo files, which are not system headers
                    CXFile macroFile = new CXFile();
                    clang_getExpansionLocation(location, macroFile, (IntPointer) null, null, null);
                    if (!macroFile.isNull() && clang_Cursor_isMacroBuiltin(current) == 0 && clang_Cursor_isMacroFunctionLike(current) == 0) {
                        CXSourceRange range = clang_getCursorExtent(current);
                        CXToken tokens = new CXToken(null);
                        IntPointer nTokens = new IntPointer(1);
                        clang_tokenize(translationUnit, range, tokens, nTokens);
                        String tokenizedName = clang_getTokenSpelling(translationUnit, tokens.position(0)).getString();
                        StringBuilder value = new StringBuilder();

                        for (int i = 1; i < nTokens.get(); i++) {
                            value.append(clang_getTokenSpelling(translationUnit, tokens.position(i)).getString());
                        }

                        // Libclang doesn't support define comment parsing
                        Manager.getInstance().registerMacro(new MacroType(tokenizedName, value.toString(), null));
                    }
                }

                return CXChildVisit_Recurse;
            });
        } finally {
            argPointer.close();
            file.close();
            clang_disposeTranslationUnit(translationUnit);
            clang_disposeIndex(index);
        }
    }

    public static void generateJavaCode(String path) {
        Manager.getInstance().emit(path);
    }

    public static void execute(String path, String basePackage, String fileToParse, Path sysroot, List<ParseTarget> targets, String[] options) {
        if (!path.endsWith("/"))
            path += "/";
        if (!Files.isDirectory(sysroot.resolve("include")) || !Files.isDirectory(sysroot.resolve("libc").resolve("include")))
            throw new IllegalArgumentException("Sysroot " + sysroot + " must contain include/ and libc/include/ (Zig's lib directory)");

        List<Manager> passes = new ArrayList<>();
        for (ParseTarget target : targets) {
            String[] targetArguments = target.clangArguments(sysroot);
            String[] arguments = Arrays.copyOf(targetArguments, targetArguments.length + options.length);
            System.arraycopy(options, 0, arguments, targetArguments.length, options.length);
            Manager.init(target, fileToParse, basePackage);
            parse(fileToParse, arguments, target.name());
            passes.add(Manager.getInstance());
        }

        Manager.getInstance().unifyWith(passes);
        generateJavaCode(path);
    }

    public static void main(String[] args) {
        if (args.length < 5)
            throw new IllegalArgumentException("Usage: <outputPath> <basePackage> <fileToParse> <sysrootDir> <parseTarget,...> [clang options...]");
        String[] options = new String[args.length - 5];
        System.arraycopy(args, 5, options, 0, options.length);
        execute(args[0], args[1], args[2], Paths.get(args[3]), ParseTarget.parse(args[4]), options);
    }
}
