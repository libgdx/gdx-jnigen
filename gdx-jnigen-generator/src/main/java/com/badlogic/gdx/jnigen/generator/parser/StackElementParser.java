package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.Generator;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.StackElementType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class StackElementParser {

    private final CXType toParse;

    public StackElementParser(CXType toParse) {
        this.toParse = toParse;
    }

    public StackElementType parse() {
        CXCursor cursor = clang_getTypeDeclaration(toParse);
        StackElementType stackElementType = new StackElementType(TypeDefinition.createTypeDefinition(toParse), cursor.kind() == CXCursor_StructDecl);
        CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(CXCursor current, CXCursor parent, CXClientData cxClientData) {
                String cursorSpelling = clang_getCursorSpelling(current).getString();
                if (current.kind() == CXCursor_FieldDecl) {
                    CXType type = clang_getCursorType(current);
                    Generator.registerCXType(type);
                    NamedType namedType = new NamedType(TypeDefinition.createTypeDefinition(type), cursorSpelling);
                    stackElementType.addField(namedType);
                }

                return CXChildVisit_Continue;
            }
        };

        clang_visitChildren(cursor, visitor, null);
        visitor.close();
        Manager.getInstance().addStackElement(stackElementType);
        return stackElementType;
    }


}
