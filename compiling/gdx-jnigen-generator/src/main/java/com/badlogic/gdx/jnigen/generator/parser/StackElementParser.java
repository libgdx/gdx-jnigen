package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.Generator;
import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.MappedType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.StackElementType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class StackElementParser {

    private final TypeDefinition typeDefinition;
    private final CXType toParse;
    private final String alternativeName;
    private final StackElementType stackElementType;
    private MappedType parent;

    public StackElementParser(TypeDefinition typeDefinition, CXType toParse, String alternativeName, MappedType parent) {
        this.typeDefinition = typeDefinition;
        this.toParse = toParse;
        this.alternativeName = alternativeName;
        this.parent = parent;
        this.stackElementType = constructMappedType();
    }

    private StackElementType constructMappedType() {
        String name = clang_getTypeSpelling(toParse).getString();
        CXCursor cursor = clang_getTypeDeclaration(toParse);
        String javaName;
        if (!typeDefinition.isAnonymous()) {
            parent = null;
            javaName = JavaUtils.cNameToJavaTypeName(name);
        } else {
            javaName = alternativeName;
        }

        StackElementType type = new StackElementType(typeDefinition, javaName, parent, cursor.kind() == CXCursor_StructDecl);
        Manager.getInstance().addStackElement(type, parent == null);

        return type;
    }

    public StackElementType getStackElementType() {
        return stackElementType;
    }

    public void parseMappedType() {
        CXCursor cursor = clang_getTypeDeclaration(toParse);
        CXCursorVisitor visitor = new CXCursorVisitor() {
            private CXType anonymousType;

            private void parseAnonymousType() {
                CXCursor anonymousCursor = clang_getTypeDeclaration(anonymousType);
                anonymousType = null;
                clang_visitChildren(anonymousCursor, this, null);
                if (anonymousType != null)
                    parseAnonymousType();
            }

            @Override
            public void close() {
                if (anonymousType != null)
                    parseAnonymousType();
                super.close();
            }

            @Override
            public int call(CXCursor current, CXCursor parent, CXClientData cxClientData) {
                String cursorSpelling = clang_getCursorSpelling(current).getString();
                if (current.kind() == CXCursor_FieldDecl) {
                    CXType type = clang_getCursorType(current);

                    if (anonymousType != null) {
                        CXType resolvedType = type;
                        if (resolvedType.kind() == CXType_ConstantArray)
                            resolvedType = clang_getArrayElementType(resolvedType);

                        resolvedType = clang_getCursorType(clang_getTypeDeclaration(resolvedType));

                        if (clang_equalTypes(resolvedType, anonymousType) == 0)
                            parseAnonymousType();

                        anonymousType = null;
                    }

                    TypeDefinition fieldDefinition = Generator.registerCXType(type, cursorSpelling, stackElementType);

                    NamedType namedType = new NamedType(fieldDefinition, cursorSpelling);
                    stackElementType.addField(namedType);

                    while (fieldDefinition.getNestedDefinition() != null)
                        fieldDefinition = fieldDefinition.getNestedDefinition();

                    if (fieldDefinition.isAnonymous())
                        stackElementType.addChild(fieldDefinition);
                } else if (current.kind() == CXCursor_StructDecl || current.kind() == CXCursor_UnionDecl) {
                    if (anonymousType != null)
                        parseAnonymousType();

                    if (clang_Cursor_isAnonymous(current) != 0)
                        anonymousType = clang_getCursorType(current);
                }

                return CXChildVisit_Continue;
            }
        };

        clang_visitChildren(cursor, visitor, null);
        visitor.close();
    }
}
