package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;

import static org.bytedeco.llvm.global.clang.*;

public class EnumParser extends CXCursorVisitor {

    private final TypeDefinition enumType;
    public EnumParser(TypeDefinition enumType) {
        this.enumType = enumType;
    }

    @Override
    public int call(CXCursor current, CXCursor parent, CXClientData data) {
        String cursorSpelling = clang_getCursorSpelling(current).getString();
        if (current.kind() == CXCursor_EnumConstantDecl) {
            long constantValue = clang_getEnumConstantDeclValue(current);
            if (constantValue > Integer.MAX_VALUE)
                throw new IllegalArgumentException("Why is the enum " + enumType.getTypeName() + " so biiig? Please open a issue in the gdx-jnigen repo");
            Manager.getInstance().addEnumConstant(enumType.getTypeName(), cursorSpelling, (int)constantValue);
        }
        return CXChildVisit_Recurse;
    }
}
