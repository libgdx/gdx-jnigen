package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.FunctionType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXIndex;
import org.bytedeco.llvm.clang.CXSourceLocation;
import org.bytedeco.llvm.clang.CXTranslationUnit;
import org.bytedeco.llvm.clang.CXType;

import java.nio.ByteBuffer;

import static org.bytedeco.llvm.global.clang.*;

public class Generator {

    public static void parse() {
        // What does 0,1 mean? Who knows!
        CXIndex index = clang_createIndex(0,1);
        BytePointer file = new BytePointer("gdx-jnigen-generator/src/test/resources/definitions.h");
        // Determine sysroot dynamically
        String[] parameter = new String[]{};
        PointerPointer<BytePointer> argPointer = new PointerPointer<>(parameter);
        CXTranslationUnit translationUnit = clang_parseTranslationUnit(index, file, argPointer, parameter.length, null, 0,
                CXTranslationUnit_SkipFunctionBodies | CXTranslationUnit_DetailedPreprocessingRecord | CXTranslationUnit_IncludeAttributedTypes);

        CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(@ByVal CXCursor current, @ByVal CXCursor parent, CXClientData cxClientData) {
                CXSourceLocation location = clang_getCursorLocation(current);
                if (clang_Location_isFromMainFile(location) == 0)
                    return CXChildVisit_Continue;

                ByteBuffer buffer = cxClientData.asByteBuffer();
                String name = clang_getCursorSpelling(current).getString(); // Why the hell does `getString` dispose the CXString?
                switch (current.kind()) {
                case CXCursor_StructDecl:
                    // TODO: We don't care about TypeDef for the moment
                    if (parent.kind() != CXCursor_TypedefDecl) {
                        Manager.getInstance().startStruct(name);
                        buffer.put(0, (byte)1);
                    } else {
                        buffer.put(0, (byte)0);
                    }
                    break;
                case CXCursor_FieldDecl:
                    if (parent.kind() == CXCursor_StructDecl && buffer.get() == 1) {
                        CXType type = clang_getCursorType(current);
                        NamedType namedType = new NamedType(TypeDefinition.createTypeDefinition(type), name);
                        Manager.getInstance().putStructField(clang_getCursorSpelling(parent).getString(), namedType);
                    }
                    break;
                case CXCursor_TypedefDecl:
                    CXType typeDef = clang_getTypedefDeclUnderlyingType(current);
                    if (typeDef.kind() == CXType_Pointer) {
                        CXType funcType = clang_getPointeeType(typeDef);
                        if (funcType.kind() == CXType_FunctionProto) {
                            CXType returnType = clang_getResultType(funcType);
                            TypeDefinition returnDefinition = TypeDefinition.createTypeDefinition(returnType);
                            int numArgs = clang_getNumArgTypes(funcType);
                            NamedType[] argTypes = new NamedType[numArgs];
                            for (int i = 0; i < numArgs; i++) {
                                CXType argType = clang_getArgType(funcType, i);
                                // TODO: To retrieve the parameter name if available, we should utilise another visitor
                                //  However, I decided that I don't care for the moment
                                argTypes[i] = new NamedType(TypeDefinition.createTypeDefinition(argType), "arg" + i);
                            }
                            ClosureType closure = new ClosureType(name, returnDefinition, argTypes);
                            Manager.getInstance().addClosure(closure);
                        }
                    }
                    break;
                case CXCursor_FunctionDecl:
                    CXType funcType = clang_getCursorType(current);
                    CXType returnType = clang_getResultType(funcType);
                    TypeDefinition returnDefinition = TypeDefinition.createTypeDefinition(returnType);
                    int numArgs = clang_getNumArgTypes(funcType);
                    NamedType[] argTypes = new NamedType[numArgs];
                    for (int i = 0; i < numArgs; i++) {
                        CXType argType = clang_getArgType(funcType, i);
                        // TODO: To retrieve the parameter name if available, we should utilise another visitor
                        //  However, I decided that I don't care for the moment
                        argTypes[i] = new NamedType(TypeDefinition.createTypeDefinition(argType), "arg" + i);
                    }
                    Manager.getInstance().addFunction(new FunctionType(name, argTypes, returnDefinition));
                    break;
                default:
                    //System.out.println(name + " " +  current.kind());
                }

                return CXChildVisit_Recurse;
            }
        };
        CXClientData cxClientData = new CXClientData(Pointer.malloc(1));
        clang_visitChildren(clang_getTranslationUnitCursor(translationUnit), visitor, cxClientData);
        argPointer.close();
        file.close();
        clang_disposeTranslationUnit(translationUnit);
        clang_disposeIndex(index);
    }

    public static void generateJavaCode() {
        Manager.getInstance().emit();
    }

    public static void main(String[] args) {
        parse();
        generateJavaCode();
    }
}
