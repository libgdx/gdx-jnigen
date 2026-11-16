package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.ParseTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Primitive definitions are interned per parse pass in the {@link Manager}; the sizeof and alignof clang reported
 * for the pass's target travel with them so the passes can be unified later. Built from bare model objects, no
 * libclang.
 */
public class TypeDefinitionTest {

    @BeforeEach
    void freshManager() {
        Manager.init(ParseTarget.LINUX_X86_64, "test.h", "test");
    }

    @Test
    void observedLayoutIsRecordedWhenAPrimitiveIsFirstInterned() {
        TypeDefinition sizeT = Manager.getInstance().defineType(TypeKind.PROMOTED_LONG, "size_t", 8, 8);
        assertEquals(8, sizeT.getObservedSize());
        assertEquals(8, sizeT.getObservedAlignment());
        assertSame(sizeT, Manager.getInstance().defineType(TypeKind.PROMOTED_LONG, "size_t", 8, 8));
    }

    @Test
    void reinterningWithAnotherLayoutIsRejectedLikeAnotherKind() {
        Manager.getInstance().defineType(TypeKind.PROMOTED_LONG, "size_t", 8, 8);
        IllegalArgumentException kind = assertThrows(IllegalArgumentException.class,
                () -> Manager.getInstance().defineType(TypeKind.PROMOTED_INT, "size_t", 8, 8));
        assertTrue(kind.getMessage().contains("PROMOTED_LONG"), kind.getMessage());
        IllegalArgumentException size = assertThrows(IllegalArgumentException.class,
                () -> Manager.getInstance().defineType(TypeKind.PROMOTED_LONG, "size_t", 4, 4));
        assertTrue(size.getMessage().contains("size 8"), size.getMessage());
        assertTrue(size.getMessage().contains("requested was 4"), size.getMessage());
        IllegalArgumentException alignment = assertThrows(IllegalArgumentException.class,
                () -> Manager.getInstance().defineType(TypeKind.PROMOTED_LONG, "size_t", 8, 4));
        assertTrue(alignment.getMessage().contains("alignment 8"), alignment.getMessage());
        assertTrue(alignment.getMessage().contains("requested was 4"), alignment.getMessage());
    }

    @Test
    void unknownLayoutStaysNegative() {
        // clang reports a negative CXTypeLayoutError for types without a layout (void, incomplete types).
        TypeDefinition voidDef = Manager.getInstance().defineType(TypeKind.VOID, "void", -2, -2);
        assertEquals(-2, voidDef.getObservedSize());
        assertEquals(-2, voidDef.getObservedAlignment());
        assertEquals(-1, Manager.getInstance().defineType(TypeKind.STRUCT, "struct s", -1, -1).getObservedSize());
    }
}
