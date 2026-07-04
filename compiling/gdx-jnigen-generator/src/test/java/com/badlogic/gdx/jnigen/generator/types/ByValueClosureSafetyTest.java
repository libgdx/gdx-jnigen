package com.badlogic.gdx.jnigen.generator.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Locks in {@link StackElementType#isByValueClosureSafe()} - the guard that keeps aggregates which
 * libFFI cannot ABI-classify (unions, system-header structs) from crossing a closure upcall by value.
 * The check is transitive, so these cases assert both the direct and the nested hazards, which the
 * emission-time rejection in {@link ClosureType} relies on. Built from bare model objects (no libclang),
 * so it runs as a plain unit test.
 */
public class ByValueClosureSafetyTest {

    /** A struct/union field is inspected via its definition's mapped type, so keep the definition around. */
    private static TypeDefinition structDef(String name, boolean systemHeader) {
        TypeDefinition def = TypeDefinition.get(TypeKind.STRUCT, name);
        StackElementType type = new StackElementType(def, name, null);
        def.setOverrideMappedType(type);
        if (systemHeader)
            type.markSystemHeader();
        return def;
    }

    private static TypeDefinition unionDef(String name) {
        TypeDefinition def = TypeDefinition.get(TypeKind.UNION, name);
        StackElementType type = new StackElementType(def, name, null);
        def.setOverrideMappedType(type);
        return def;
    }

    private static void addField(TypeDefinition ownerDef, TypeDefinition fieldDef, String fieldName) {
        StackElementType owner = (StackElementType) ownerDef.getMappedType();
        owner.addField(new StackElementField(new NamedType(fieldDef, fieldName), null));
    }

    private static boolean safe(TypeDefinition def) {
        return def.getMappedType().isByValueClosureSafe();
    }

    @Test
    void plainStructIsSafe() {
        assertTrue(safe(structDef("Plain", false)));
    }

    @Test
    void unionIsUnsafe() {
        assertFalse(safe(unionDef("Payload")));
    }

    @Test
    void systemHeaderStructIsUnsafe() {
        assertFalse(safe(structDef("timespec", true)));
    }

    @Test
    void structEmbeddingSystemHeaderStructIsUnsafe() {
        TypeDefinition holder = structDef("TimeHolder", false);
        addField(holder, structDef("timespec", true), "ts");
        assertFalse(safe(holder));
    }

    @Test
    void structEmbeddingUnionIsUnsafe() {
        TypeDefinition holder = structDef("Wrapper", false);
        addField(holder, unionDef("Payload"), "payload");
        assertFalse(safe(holder));
    }

    @Test
    void structEmbeddingOnlySafeStructIsSafe() {
        TypeDefinition outer = structDef("Outer", false);
        addField(outer, structDef("Inner", false), "inner");
        assertTrue(safe(outer));
    }

    @Test
    void systemHeaderStructNestedTwoLevelsDeepIsUnsafe() {
        TypeDefinition mid = structDef("Mid", false);
        addField(mid, structDef("timespec", true), "ts");
        TypeDefinition top = structDef("Top", false);
        addField(top, mid, "mid");
        assertFalse(safe(top));
    }
}
