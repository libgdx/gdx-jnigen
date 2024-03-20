package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.EnumType;
import com.badlogic.gdx.jnigen.generator.types.MappedType;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class EnumParser {

    private final CXType toParse;
    private final String alternativeName;

    public EnumParser(CXType toParse, String alternativeName) {
        this.toParse = toParse;
        this.alternativeName = alternativeName;
    }

    public MappedType register() {
        String name = clang_getTypeSpelling(toParse).getString();
        String javaName = clang_Cursor_isAnonymous(clang_getTypeDeclaration(toParse)) == 0 ? JavaUtils.cNameToJavaTypeName(name) : alternativeName;

        CXCursor cursor = clang_getTypeDeclaration(toParse);

        EnumType enumType = new EnumType(javaName);

        CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(CXCursor current, CXCursor parent, CXClientData cxClientData) {
                String cursorSpelling = clang_getCursorSpelling(current).getString();
                if (current.kind() == CXCursor_EnumConstantDecl) {
                    long constantValue = clang_getEnumConstantDeclValue(current);
                    if (constantValue > Integer.MAX_VALUE)
                        throw new IllegalArgumentException("Why is the enum " + enumType.abstractType() + " so biiig? Please open a issue in the gdx-jnigen repo");
                    enumType.registerConstant(cursorSpelling, (int)constantValue);
                }
                return CXChildVisit_Recurse;
            }
        };

        clang_visitChildren(cursor, visitor, null);
        visitor.close();

        Manager.getInstance().addEnum(enumType);

        return enumType;
    }
}
