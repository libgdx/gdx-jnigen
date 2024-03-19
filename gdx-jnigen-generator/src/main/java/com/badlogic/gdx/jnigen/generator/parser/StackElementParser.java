package com.badlogic.gdx.jnigen.generator.parser;

import com.badlogic.gdx.jnigen.generator.Generator;
import com.badlogic.gdx.jnigen.generator.JavaUtils;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.types.ClosureType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.StackElementType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.badlogic.gdx.jnigen.generator.types.TypeKind;
import org.bytedeco.llvm.clang.CXClientData;
import org.bytedeco.llvm.clang.CXCursor;
import org.bytedeco.llvm.clang.CXCursorVisitor;
import org.bytedeco.llvm.clang.CXType;

import static org.bytedeco.llvm.global.clang.*;

public class StackElementParser {

    private final CXType toParse;
    private final String alternativeName;
    private final TypeDefinition parent;

    public StackElementParser(CXType toParse, String alternativeName, TypeDefinition parent) {
        this.toParse = toParse;
        this.alternativeName = alternativeName;
        this.parent = parent;
    }

    public void register() {
        String name = clang_getTypeSpelling(toParse).getString();
        CXCursor cursor = clang_getTypeDeclaration(toParse);
        TypeDefinition definition = TypeDefinition.createTypeDefinition(toParse);
        String javaName;
        if (!definition.isAnonymous()) {
            javaName = JavaUtils.cNameToJavaTypeName(name);
        } else {
            javaName = alternativeName;
        }

        StackElementType stackElementType = new StackElementType(definition, javaName, parent, cursor.kind() == CXCursor_StructDecl);
        Manager.getInstance().registerCTypeMapping(name, stackElementType);
        Manager.getInstance().addStackElement(stackElementType, parent == null);

        CXCursorVisitor visitor = new CXCursorVisitor() {
            @Override
            public int call(CXCursor current, CXCursor parent, CXClientData cxClientData) {
                String cursorSpelling = clang_getCursorSpelling(current).getString();
                if (current.kind() == CXCursor_FieldDecl) {
                    CXType type = clang_getCursorType(current);
                    TypeDefinition fieldDefinition = TypeDefinition.createTypeDefinition(type);
                    boolean isClosure = fieldDefinition.getTypeKind() == TypeKind.CLOSURE;
                    Generator.registerCXType(type, cursorSpelling, fieldDefinition.isAnonymous() || isClosure ? definition : null);

                    NamedType namedType = new NamedType(TypeDefinition.createTypeDefinition(type), cursorSpelling);
                    stackElementType.addField(namedType);
                    // TODO: 19.03.24 THis is very bad way to determine whether a closure is parentless
                    if (fieldDefinition.isAnonymous() || (isClosure && !Manager.getInstance().getGlobalType().hasClosure(
                            (ClosureType)fieldDefinition.getMappedType())))
                        stackElementType.addChild(namedType.getDefinition());
                }

                return CXChildVisit_Continue;
            }
        };

        clang_visitChildren(cursor, visitor, null);
        visitor.close();
    }
}
