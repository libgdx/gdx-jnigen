package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.ClassNameConstants;
import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.PossibleTarget;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class PrimitiveType implements MappedType {

    private final TypeDefinition definition;

    public PrimitiveType(TypeDefinition typeDefinition) {
        this.definition = typeDefinition;
    }

    public Class<?> getJavaRepresentation() {
        switch (definition.getTypeKind()) {
            case VOID:
                return void.class;
            // Technically not correct, but most platforms have it signed, so better to follow that I think
            case NATIVE_BYTE:
            case SIGNED_BYTE:
                return byte.class;
            case PROMOTED_BYTE:
            case CHAR:
                return char.class;
            case SHORT:
                return short.class;
            case INT:
                return int.class;
            case PROMOTED_INT:
            case PROMOTED_LONG:
            case LONG_LONG:
            case PROMOTED_LONG_LONG:
            case LONG:
            case WORD:
            case PROMOTED_WORD:
                return long.class;
            case FLOAT:
                return float.class;
            case DOUBLE:
                return double.class;
            case BOOLEAN:
                return boolean.class;
            default:
                throw new IllegalArgumentException(definition.getTypeName() + " is not primitive.");
        }
    }

    @Override
    public String abstractType() {
        return getJavaRepresentation().getName();
    }

    @Override
    public String pointerType() {
        switch (definition.getTypeKind()) {
        case VOID:
            return "VoidPointer";
        case FLOAT:
            return "FloatPointer";
        case DOUBLE:
            return "DoublePointer";
        case NATIVE_BYTE:
            return "BytePointer";
        case SIGNED_BYTE:
            return "SBytePointer";
        case BOOLEAN:
        case PROMOTED_BYTE:
            return "UBytePointer";
        case SHORT:
            return "SShortPointer";
        case CHAR:
            return "UShortPointer";
        case INT:
            return "SIntPointer";
        case PROMOTED_INT:
            return "UIntPointer";
        case LONG:
            return "SLongPointer";
        case PROMOTED_LONG:
            return "ULongPointer";
        case WORD:
            return "SWordPointer";
        case PROMOTED_WORD:
            return "UWordPointer";
        case LONG_LONG:
            return "SInt64Pointer";
        case PROMOTED_LONG_LONG:
            return "UInt64Pointer";
        default:
            throw new IllegalArgumentException(definition.getTypeName() + " has no pointer class");
        }
    }

    @Override
    public void importPointerType(CompilationUnit cu) {
        switch (definition.getTypeKind()) {
        case VOID:
            cu.addImport(ClassNameConstants.VOIDPOINTER_CLASS);
            break;
        case FLOAT:
            cu.addImport(ClassNameConstants.FLOATPOINTER_CLASS);
            break;
        case DOUBLE:
            cu.addImport(ClassNameConstants.DOUBLEPOINTER_CLASS);
            break;
        default:
            cu.addImport(ClassNameConstants.INTEGER_POINTER_PACKAGE + "." + pointerType());
        }
    }

    @Override
    public String primitiveType() {
        return getJavaRepresentation().getName();
    }

    @Override
    public void importType(CompilationUnit cu) {
        // Unimportable
    }

    @Override
    public String classFile() {
        throw new IllegalArgumentException();
    }

    @Override
    public String packageName() {
        throw new IllegalArgumentException();
    }

    @Override
    public Expression fromC(Expression cRetrieved) {
        return cRetrieved;
    }

    @Override
    public Expression toC(Expression cSend) {
        return cSend;
    }

    @Override
    public int typeID() {
        if (definition.getTypeKind() == TypeKind.VOID)
            return Manager.VOID_FFI_ID;
        else
            return Manager.getInstance().getCTypeID(definition.getTypeName());
    }

    @Override
    public Expression writeToBufferPtr(Expression bufferPtr, Expression offset, Expression valueToWrite) {
        String methodName;
        switch (definition.getTypeKind()) {
        case BOOLEAN:
            methodName = "setBoolean";
            break;
        case NATIVE_BYTE:
        case SIGNED_BYTE:
            methodName = "setByte";
            break;
        case PROMOTED_BYTE:
            methodName = "setUByte";
            break;
        case SHORT:
            methodName = "setShort";
            break;
        case CHAR:
            methodName = "setChar";
            break;
        case INT:
            methodName = "setInt";
            break;
        case PROMOTED_INT:
            methodName = "setUInt";
            break;
        case LONG:
            methodName = "setNativeLong";
            break;
        case PROMOTED_LONG:
            methodName = "setNativeULong";
            break;
        case WORD:
            methodName = "setNativeWord";
            break;
        case PROMOTED_WORD:
            methodName = "setNativeUWord";
            break;
        case LONG_LONG:
        case PROMOTED_LONG_LONG:
            methodName = "setLong";
            break;
        case FLOAT:
            methodName = "setFloat";
            break;
        case DOUBLE:
            methodName = "setDouble";
            break;
        default:
                throw new IllegalArgumentException(definition.getTypeName() + " is not primitive.");
        }

        return new MethodCallExpr(methodName, offset, valueToWrite).setScope(bufferPtr);
    }

    @Override
    public Expression readFromBufferPtr(Expression bufferPtr, Expression offset) {
        String methodName;
        switch (definition.getTypeKind()) {
        case BOOLEAN:
            methodName = "getBoolean";
            break;
        case NATIVE_BYTE:
        case SIGNED_BYTE:
            methodName = "getByte";
            break;
        case PROMOTED_BYTE:
            methodName = "getUByte";
            break;
        case SHORT:
            methodName = "getShort";
            break;
        case CHAR:
            methodName = "getChar";
            break;
        case INT:
            methodName = "getInt";
            break;
        case PROMOTED_INT:
            methodName = "getUInt";
            break;
        case LONG:
            methodName = "getNativeLong";
            break;
        case PROMOTED_LONG:
            methodName = "getNativeULong";
            break;
        case WORD:
            methodName = "getNativeWord";
            break;
        case PROMOTED_WORD:
            methodName = "getNativeUWord";
            break;
        case LONG_LONG:
        case PROMOTED_LONG_LONG:
            methodName = "getLong";
            break;
        case FLOAT:
            methodName = "getFloat";
            break;
        case DOUBLE:
            methodName = "getDouble";
            break;
        default:
            throw new IllegalArgumentException(definition.getTypeName() + " is not primitive.");
        }
        return new MethodCallExpr(methodName, offset).setScope(bufferPtr);
    }

    @Override
    public int getSize(PossibleTarget target) {
        return definition.getTypeKind().getSize(target);
    }

    @Override
    public int getAlignment(PossibleTarget target) {
        return definition.getTypeKind().getAlignment(target);
    }
}
