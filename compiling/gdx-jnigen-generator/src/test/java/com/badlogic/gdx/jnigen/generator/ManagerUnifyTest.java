package com.badlogic.gdx.jnigen.generator;

import com.badlogic.gdx.jnigen.generator.types.EnumConstant;
import com.badlogic.gdx.jnigen.generator.types.EnumType;
import com.badlogic.gdx.jnigen.generator.types.FunctionSignature;
import com.badlogic.gdx.jnigen.generator.types.FunctionType;
import com.badlogic.gdx.jnigen.generator.types.MacroType;
import com.badlogic.gdx.jnigen.generator.types.NamedType;
import com.badlogic.gdx.jnigen.generator.types.PrimitiveType;
import com.badlogic.gdx.jnigen.generator.types.StackElementField;
import com.badlogic.gdx.jnigen.generator.types.StackElementType;
import com.badlogic.gdx.jnigen.generator.types.TypeDefinition;
import com.badlogic.gdx.jnigen.generator.types.TypeKind;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Only primitive kinds are unified across the parse passes; structs, enums, functions and macros are emitted
 * from a single pass. {@link Manager#unifyWith} therefore has to prove that every other pass saw the same
 * declarations, otherwise the choice of that pass would silently decide what a {@code #ifdef _WIN32} branch
 * generates. Built from bare model objects, no libclang.
 */
public class ManagerUnifyTest {

    /** Runs {@code build} in a fresh pass for every target and returns the passes in the generator's order. */
    private static List<Manager> passes(Consumer<ParseTarget> build) {
        List<Manager> passes = new ArrayList<>();
        for (ParseTarget target : ParseTarget.values()) {
            Manager.init(target, "test.h", "test");
            build.accept(target);
            passes.add(Manager.getInstance());
        }
        return passes;
    }

    /** Like the generator: the last pass is the current instance and the one unified into. */
    private static void unify(List<Manager> passes) {
        Manager.getInstance().unifyWith(passes);
    }

    private static TypeDefinition primitive(TypeKind kind, String name, int size) {
        TypeDefinition definition = Manager.getInstance().defineType(kind, name, size, size);
        definition.setOverrideMappedType(new PrimitiveType(definition));
        return definition;
    }

    private static StackElementType struct(String name) {
        TypeDefinition definition = Manager.getInstance().defineType(TypeKind.STRUCT, name, -1, -1);
        StackElementType type = new StackElementType(definition, name, null);
        definition.setOverrideMappedType(type);
        Manager.getInstance().addStackElement(type, true);
        return type;
    }

    private static void field(StackElementType owner, TypeDefinition type, String name) {
        owner.addField(new StackElementField(new NamedType(type, name), null));
    }

    private static void function(String name, TypeDefinition returnType, NamedType... arguments) {
        Manager.getInstance().addFunction(new FunctionType(new FunctionSignature(name, arguments, returnType), null));
    }

    /** The whole point of the passes: the same C type has another kind and size per target. */
    private static TypeDefinition sizeT(ParseTarget target) {
        switch (target.getPossibleTarget()) {
        case WIN_64:
            return primitive(TypeKind.PROMOTED_LONG_LONG, "size_t", 8);
        case UNIX_64:
            return primitive(TypeKind.PROMOTED_LONG, "size_t", 8);
        default:
            return primitive(TypeKind.PROMOTED_INT, "size_t", 4);
        }
    }

    private static void baseModel(ParseTarget target) {
        TypeDefinition intDef = primitive(TypeKind.INT, "int", 4);
        TypeDefinition sizeT = sizeT(target);
        StackElementType point = struct("Point");
        field(point, intDef, "x");
        field(point, sizeT, "y");
        EnumType color = new EnumType(Manager.getInstance().defineType(TypeKind.ENUM, "Color", -1, -1), "Color");
        color.registerConstant(new EnumConstant(0, "RED", null));
        color.registerConstant(new EnumConstant(1, "GREEN", null));
        Manager.getInstance().addEnum(color);
        function("area", sizeT, new NamedType(intDef, "w"), new NamedType(intDef, "h"));
        Manager.getInstance().registerMacro(new MacroType("VERSION", "3", null));
    }

    @Test
    void passesThatOnlyDifferInPrimitiveLayoutUnify() {
        List<Manager> passes = passes(ManagerUnifyTest::baseModel);
        assertDoesNotThrow(() -> unify(passes));
        assertEquals(TypeKind.PROMOTED_WORD, Manager.getInstance().getCType("size_t").getTypeKind());
    }

    @Test
    void aStructFieldOnlyOneTargetSawIsRejected() {
        List<Manager> passes = passes(target -> {
            baseModel(target);
            if (target == ParseTarget.WINDOWS_X86_64)
                field(struct("Extra"), primitive(TypeKind.INT, "int", 4), "onlyOnWindows");
        });
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> unify(passes));
        assertTrue(e.getMessage().contains("WINDOWS_X86_64"), e.getMessage());
        assertTrue(e.getMessage().contains("Extra"), e.getMessage());
    }

    @Test
    void aFieldTypeThatDiffersPerTargetIsRejected() {
        List<Manager> passes = passes(target -> {
            baseModel(target);
            StackElementType holder = struct("Holder");
            field(holder, target == ParseTarget.MACOS_AARCH64 ? primitive(TypeKind.INT, "int", 4) : Manager.getInstance().getCType("size_t"), "value");
        });
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> unify(passes));
        assertTrue(e.getMessage().contains("MACOS_AARCH64"), e.getMessage());
        assertTrue(e.getMessage().contains("Holder"), e.getMessage());
    }

    @Test
    void anEnumConstantThatDiffersPerTargetIsRejected() {
        List<Manager> passes = passes(target -> {
            baseModel(target);
            EnumType mode = new EnumType(Manager.getInstance().defineType(TypeKind.ENUM, "Mode", -1, -1), "Mode");
            mode.registerConstant(new EnumConstant(target == ParseTarget.LINUX_ARM ? 2 : 1, "FAST", null));
            Manager.getInstance().addEnum(mode);
        });
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> unify(passes));
        assertTrue(e.getMessage().contains("LINUX_ARM"), e.getMessage());
        assertTrue(e.getMessage().contains("FAST"), e.getMessage());
    }

    @Test
    void aFunctionOnlySomeTargetsDeclareIsRejected() {
        List<Manager> passes = passes(target -> {
            baseModel(target);
            if (target != ParseTarget.WINDOWS_X86 && target != ParseTarget.WINDOWS_X86_64)
                function("fork", Manager.getInstance().getCType("int"));
        });
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> unify(passes));
        assertTrue(e.getMessage().contains("WINDOWS_X86_64"), e.getMessage());
        assertTrue(e.getMessage().contains("fork"), e.getMessage());
    }

    @Test
    void aMacroValueThatDiffersPerTargetIsRejected() {
        List<Manager> passes = passes(target -> {
            baseModel(target);
            Manager.getInstance().registerMacro(new MacroType("PATH_SEP", target == ParseTarget.WINDOWS_X86_64 ? "'\\\\'" : "'/'", null));
        });
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> unify(passes));
        assertTrue(e.getMessage().contains("WINDOWS_X86_64"), e.getMessage());
        assertTrue(e.getMessage().contains("PATH_SEP"), e.getMessage());
    }
}
