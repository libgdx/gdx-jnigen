package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;

import static org.bytedeco.llvm.global.clang.*;

public class UnionParser extends CXCursorVisitor {

    private final TypeDefinition unionDefinition;

    public UnionParser(TypeDefinition unionDefinition) {
        this.unionDefinition = unionDefinition;
    }

    @Override
    public int call(CXCursor current, CXCursor parent, CXClientData data) {
        String cursorSpelling = clang_getCursorSpelling(current).getString();
        if (current.kind() == CXCursor_FieldDecl) {
            System.out.println(cursorSpelling);
        }
        return CXChildVisit_Recurse;
    }
}
