package com.badlogic.gdx.jnigen.generator;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXIndex;
import org.bytedeco.llvm.clang.CXTranslationUnit;

import static org.bytedeco.llvm.global.clang.*;

public class Generator {

    public static void main(String[] args) {
        // What does 0,1 mean? Who knows!
        CXIndex index = clang_createIndex(0,1);
        BytePointer file = new BytePointer("/home/berstanio/IdeaProjects/gdx-jnigen/gdx-jnigen-generator/src/test/resources/definitions.h");
        String[] parameter = new String[]{};
        PointerPointer<BytePointer> argPointer = new PointerPointer<>(parameter);
        CXTranslationUnit translationUnit = clang_parseTranslationUnit(index, file, argPointer, parameter.length, null, 0,
                CXTranslationUnit_SkipFunctionBodies | CXTranslationUnit_DetailedPreprocessingRecord | CXTranslationUnit_IncludeAttributedTypes);

        CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(@ByVal CXCursor current, @ByVal CXCursor parent, CXClientData cxClientData) {
                switch (current.kind()) {
                    case CXCursor_StructDecl:
                        System.out.println(current.data().getString(0));
                }
                System.out.println("Howdyy " + current.kind());
                return CXChildVisit_Recurse;
            }
        };
        clang_visitChildren(clang_getTranslationUnitCursor(translationUnit), visitor, null);
        argPointer.close();
        file.close();
        translationUnit.close();
        visitor.close();
    }
}
