package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.TypeKind;
import com.github.javaparser.ast.CompilationUnit;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXIndex;
import org.bytedeco.llvm.clang.CXSourceLocation;
import org.bytedeco.llvm.clang.CXString;
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
                    if (parent.kind() != CXCursor_TypedefDecl)
                        Manager.getInstance().startStruct(name);
                    break;
                case CXCursor_FieldDecl:
                    if (parent.kind() == CXCursor_StructDecl) {
                        CXType type = clang_getCursorType(current);
                        TypeKind kind = TypeKind.getTypeKind(type);
                        Manager.getInstance().putStructField(clang_getCursorSpelling(parent).getString(), name, kind);
                    }
                    break;
                default:
                    System.out.println(name + " " +  current.kind());
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

    }

    public static void main(String[] args) {
        parse();
        generateJavaCode();
    }
}
