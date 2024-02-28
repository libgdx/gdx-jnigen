package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class StructParser extends CXCursorVisitor {

    private final TypeDefinition structType;
    public StructParser(TypeDefinition structType) {
        this.structType = structType;
    }

    @Override
    public int call(CXCursor current, CXCursor parent, CXClientData data) {
        String cursorSpelling = clang_getCursorSpelling(current).getString();
        if (current.kind() == CXCursor_FieldDecl) {
            CXType type = clang_getCursorType(current);
            NamedType namedType = new NamedType(TypeDefinition.createTypeDefinition(type), cursorSpelling);
            Manager.getInstance().putStructField(structType.getTypeName(), namedType);
        }
        return CXChildVisit_Recurse;
    }
}
