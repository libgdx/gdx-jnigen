package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.EnumConstant;
import com.badlogic.gdx.jnigen.generator.types.EnumType;
import com.badlogic.gdx.jnigen.generator.types.MappedType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class EnumParser {

    private final TypeDefinition definition;
    private final CXType toParse;
    private final String alternativeName;

    public EnumParser(TypeDefinition definition, CXType toParse, String alternativeName) {
        this.toParse = toParse;
        this.alternativeName = alternativeName;
        this.definition = definition;
    }

    public MappedType register() {
        String name = clang_getTypeSpelling(toParse).getString();
        String javaName = clang_Cursor_isAnonymous(clang_getTypeDeclaration(toParse)) == 0 ? JavaUtils.cNameToJavaTypeName(name) : alternativeName;

        CXCursor cursor = clang_getTypeDeclaration(toParse);

        EnumType enumType = new EnumType(definition, javaName);
        CommentParser commentParser = new CommentParser(cursor);
        if (commentParser.isPresent())
            enumType.setComment(commentParser.parse());

        CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(CXCursor current, CXCursor parent, CXClientData cxClientData) {
                String cursorSpelling = clang_getCursorSpelling(current).getString();
                if (current.kind() == CXCursor_EnumConstantDecl) {
                    long constantValue = clang_getEnumConstantDeclValue(current);
                    if (constantValue > Integer.MAX_VALUE || constantValue < Integer.MIN_VALUE)
                        throw new IllegalArgumentException("Why is the enum " + enumType.abstractType() + " so biiig? Please open a issue in the gdx-jnigen repo");
                    EnumConstant constant = new EnumConstant((int) constantValue, cursorSpelling, new CommentParser(current).parse());
                    enumType.registerConstant(constant);
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
