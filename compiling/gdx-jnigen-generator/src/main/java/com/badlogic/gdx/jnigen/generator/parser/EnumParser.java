package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.ClangUtils;
import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.EnumConstant;
import com.badlogic.gdx.jnigen.generator.types.EnumType;
import com.badlogic.gdx.jnigen.generator.types.MappedType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class EnumParser {

    private final TypeDefinition definition;
    private final CXType toParse;
    private final String alternativeName;
    private final boolean forceAlternativeName;

    public EnumParser(TypeDefinition definition, CXType toParse, String alternativeName, boolean forceAlternativeName) {
        this.toParse = toParse;
        this.alternativeName = alternativeName;
        this.definition = definition;
        this.forceAlternativeName = forceAlternativeName;
    }

    public MappedType register() {
        String name = clang_getTypeSpelling(toParse).getString();
        // A named enum normally takes its tag name. When forced (a named enum reached through a
        // system-header typedef), prefer the outer alias so the stable public name is emitted.
        String javaName = (!forceAlternativeName && clang_Cursor_isAnonymous(clang_getTypeDeclaration(toParse)) == 0)
                ? JavaUtils.cNameToJavaTypeName(name) : alternativeName;

        CXCursor cursor = clang_getTypeDeclaration(toParse);

        EnumType enumType = new EnumType(definition, javaName);
        CommentParser commentParser = new CommentParser(cursor);
        if (commentParser.isPresent())
            enumType.setComment(commentParser.parse());

        ClangUtils.visitChildren(cursor, (current, parent) -> {
            String cursorSpelling = clang_getCursorSpelling(current).getString();
            if (current.kind() == CXCursor_EnumConstantDecl) {
                long constantValue = clang_getEnumConstantDeclValue(current);
                if (constantValue > Integer.MAX_VALUE || constantValue < Integer.MIN_VALUE)
                    throw new IllegalArgumentException("Why is the enum " + enumType.abstractType() + " so biiig? Please open a issue in the gdx-jnigen repo");
                EnumConstant constant = new EnumConstant((int) constantValue, cursorSpelling, new CommentParser(current).parse());
                enumType.registerConstant(constant);
            }
            return CXChildVisit_Recurse;
        });

        Manager.getInstance().addEnum(enumType);

        return enumType;
    }
}
