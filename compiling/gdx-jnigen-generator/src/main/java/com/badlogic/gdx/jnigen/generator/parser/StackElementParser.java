package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.ClangUtils;
import com.badlogic.gdx.jnigen.generator.Generator;
import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.*;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class StackElementParser {

    private final TypeDefinition typeDefinition;
    private final CXType toParse;
    private final StackElementType stackElementType;
    private CXType anonymousType;

    public StackElementParser(TypeDefinition typeDefinition, CXType toParse) {
        this.typeDefinition = typeDefinition;
        this.toParse = toParse;
        this.stackElementType = constructMappedType();
    }

    private StackElementType constructMappedType() {
        DeclaringMember declaringMember = typeDefinition.getDeclaringMember();
        String javaName = declaringMember == null
                ? JavaUtils.cNameToJavaTypeName(clang_getTypeSpelling(toParse).getString())
                : declaringMember.name;
        MappedType parent = declaringMember == null ? null : declaringMember.owner.getMappedType();
        StackElementType type = new StackElementType(typeDefinition, javaName, parent);
        Manager.getInstance().addStackElement(type, declaringMember == null);
        if (declaringMember != null)
            Manager.getInstance().addNestedType(declaringMember.owner, type);

        return type;
    }

    public StackElementType getStackElementType() {
        return stackElementType;
    }

    public void parseMappedType() {
        CXCursor cursor = clang_getTypeDeclaration(toParse);
        // A forward-declared struct has no definition in the parsed header: it is unsized/opaque, so
        // only a pointer to it can be emitted (it can neither be allocated nor dereferenced).
        if (clang_Type_getSizeOf(toParse) == CXTypeLayoutError_Incomplete)
            stackElementType.markOpaque();
        CommentParser commentParser = new CommentParser(cursor);
        if (commentParser.isPresent()) {
            stackElementType.setComment(commentParser.parse());
        }
        ClangUtils.visitChildren(cursor, this::visitField);
        // Flush a trailing anonymous struct/union that no named field referenced.
        if (anonymousType != null)
            parseAnonymousType();
    }

    private void parseAnonymousType() {
        CXCursor anonymousCursor = clang_getTypeDeclaration(anonymousType);
        anonymousType = null;
        ClangUtils.visitChildren(anonymousCursor, this::visitField);
        if (anonymousType != null)
            parseAnonymousType();
    }

    private int visitField(CXCursor current, CXCursor parent) {
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

            TypeDefinition fieldDefinition = Generator.registerCXType(type, cursorSpelling, new DeclaringMember(typeDefinition, cursorSpelling));
            if (fieldDefinition.getTypeKind() == TypeKind.VOID)
                stackElementType.markIncomplete();

            if (fieldDefinition.getTypeKind() == TypeKind.CLOSURE) {
                Generator.patchClosureTypeWithCursor(fieldDefinition, current);
            }

            NamedType namedType = new NamedType(fieldDefinition, cursorSpelling);
            StackElementField field = new StackElementField(namedType, new CommentParser(current).parse());
            stackElementType.addField(field);
        } else if (current.kind() == CXCursor_StructDecl || current.kind() == CXCursor_UnionDecl) {
            if (anonymousType != null)
                parseAnonymousType();

            if (clang_Cursor_isAnonymous(current) != 0)
                anonymousType = clang_getCursorType(current);
        }

        return CXChildVisit_Continue;
    }
}
