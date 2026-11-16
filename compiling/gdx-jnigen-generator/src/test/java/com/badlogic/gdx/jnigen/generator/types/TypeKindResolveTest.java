package com.badlogic.gdx.jnigen.generator.types;

import com.badlogic.gdx.jnigen.generator.Manager;
import com.badlogic.gdx.jnigen.generator.ParseTarget;
import com.badlogic.gdx.jnigen.generator.PossibleTarget;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.jnigen.generator.PossibleTarget.*;
import static com.badlogic.gdx.jnigen.generator.types.TypeKind.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The generator parses the header once per target and interns, for every primitive C type, a definition with
 * the kind clang reported plus sizeof and alignof on that target. {@link TypeKind#resolve} turns those
 * definitions into the single kind whose per-target layout matches all of them, and rejects anything jnigen's
 * layout model cannot represent, even when every pass agrees on the kind. Built from bare model objects, no
 * libclang.
 */
public class TypeKindResolveTest {

    /** What one pass for {@code target} interned for the type: a fresh manager per call, like a fresh pass. */
    private static TypeDefinition on(PossibleTarget target, TypeKind kind, long size, long alignment) {
        ParseTarget pass = Arrays.stream(ParseTarget.values()).filter(candidate -> candidate.getPossibleTarget() == target).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No parse pass stands for " + target));
        Manager.init(pass, "test.h", "test");
        return Manager.getInstance().defineType(kind, "t", size, alignment);
    }

    /** A definition whose layout is exactly what jnigen's model predicts for the kind on that target. */
    private static TypeDefinition modelled(PossibleTarget target, TypeKind kind) {
        return on(target, kind, kind.getSize(target), kind.getAlignment(target));
    }

    /**
     * Definitions for the four data models that only differ in integer widths: unix64, unix32, win64, win32.
     * Every integer kind is naturally aligned on those four, so alignment equals size.
     */
    private static List<TypeDefinition> seen(TypeKind unix64, long s64, TypeKind unix32, long s32, TypeKind win64, long w64, TypeKind win32, long w32) {
        return new ArrayList<>(Arrays.asList(
                on(UNIX_64, unix64, s64, s64), on(UNIX_32_NOT_X86, unix32, s32, s32),
                on(WIN_64, win64, w64, w64), on(WIN_32, win32, w32, w32)));
    }

    @Test
    void agreeingPrimitivesWithTheModelledLayoutResolveToThemselves() {
        for (TypeKind kind : TypeKind.values()) {
            if (!kind.isPrimitive())
                continue;
            List<TypeDefinition> all = new ArrayList<>();
            for (PossibleTarget target : PossibleTarget.values())
                all.add(modelled(target, kind));
            assertEquals(kind, resolve("t", all), kind.name());
        }
    }

    @Test
    void agreeingNonPrimitivesAreNotLayoutChecked() {
        for (TypeKind kind : new TypeKind[] {VOID, STRUCT, UNION, POINTER, ENUM, CLOSURE, FIXED_SIZE_ARRAY}) {
            List<TypeDefinition> all = Arrays.asList(on(UNIX_64, kind, -1, -1), on(WIN_32, kind, -1, -1));
            assertEquals(kind, resolve("t", all), kind.name());
        }
    }

    @Test
    void agreeingKindWhoseSizeBreaksTheModelIsRejected() {
        // long double is DOUBLE for clang everywhere, but 16 bytes on x86_64 and 12 on i386; jnigen models 8.
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> resolve("long double", seen(DOUBLE, 16, DOUBLE, 12, DOUBLE, 16, DOUBLE, 12)));
        assertTrue(e.getMessage().contains("long double"), e.getMessage());
        assertTrue(e.getMessage().contains("DOUBLE"), e.getMessage());
        assertTrue(e.getMessage().contains("16 bytes"), e.getMessage());
    }

    @Test
    void agreeingKindWhoseAlignmentBreaksTheModelIsRejected() {
        // i386 SysV packs long long at 4; the 32-bit unix class models 8 (ARM32), so such a pass must not map there.
        List<TypeDefinition> all = Arrays.asList(modelled(UNIX_64, LONG_LONG), on(UNIX_32_NOT_X86, LONG_LONG, 8, 4),
                modelled(WIN_64, LONG_LONG), modelled(WIN_32, LONG_LONG));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> resolve("long long", all));
        assertTrue(e.getMessage().contains("align 4"), e.getMessage());
    }

    @Test
    void i386UnixPacksEightByteTypesAtFour() {
        List<TypeDefinition> all = Arrays.asList(modelled(UNIX_64, DOUBLE), modelled(UNIX_32_NOT_X86, DOUBLE),
                on(UNIX_X86_32, DOUBLE, 8, 4), modelled(WIN_64, DOUBLE), modelled(WIN_32, DOUBLE));
        assertEquals(DOUBLE, resolve("double", all));
    }

    @Test
    void sizeTResolvesToPromotedWord() {
        // glibc: unsigned long / unsigned int; mingw: unsigned long long / unsigned int
        assertEquals(PROMOTED_WORD, resolve("size_t",
                seen(PROMOTED_LONG, 8, PROMOTED_INT, 4, PROMOTED_LONG_LONG, 8, PROMOTED_INT, 4)));
    }

    @Test
    void ssizeTResolvesToWord() {
        assertEquals(WORD, resolve("ssize_t", seen(LONG, 8, INT, 4, LONG_LONG, 8, INT, 4)));
    }

    @Test
    void darwinStyleLongTypedefStillResolvesToWordThanksToTheWindowsPass() {
        // Darwin spells intptr_t as long on both 32- and 64-bit; only mingw reveals it is 8 bytes on Win64.
        assertEquals(WORD, resolve("intptr_t", seen(LONG, 8, LONG, 4, LONG_LONG, 8, INT, 4)));
    }

    @Test
    void int64TResolvesToLongLong() {
        assertEquals(LONG_LONG, resolve("int64_t", seen(LONG, 8, LONG_LONG, 8, LONG_LONG, 8, LONG_LONG, 8)));
        assertEquals(PROMOTED_LONG_LONG, resolve("uint64_t",
                seen(PROMOTED_LONG, 8, PROMOTED_LONG_LONG, 8, PROMOTED_LONG_LONG, 8, PROMOTED_LONG_LONG, 8)));
    }

    @Test
    void plainLongStaysLong() {
        assertEquals(LONG, resolve("long", seen(LONG, 8, LONG, 4, LONG, 4, LONG, 4)));
    }

    @Test
    void conditionalTypedefThatIsIntOnWindowsAndLongOnUnixIsLong() {
        // Sizes 8/4/4/4 are the long pattern even though the Windows passes spell it int.
        assertEquals(LONG, resolve("t", seen(LONG, 8, INT, 4, INT, 4, INT, 4)));
    }

    @Test
    void resolvedIntegerMustAlsoMatchTheModelledAlignment() {
        // The size pattern is size_t's, but an 8-byte alignment on 32-bit Windows matches no word-sized kind.
        List<TypeDefinition> all = Arrays.asList(on(UNIX_64, PROMOTED_LONG, 8, 8), on(UNIX_32_NOT_X86, PROMOTED_INT, 4, 4),
                on(WIN_64, PROMOTED_LONG_LONG, 8, 8), on(WIN_32, PROMOTED_INT, 4, 8));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> resolve("size_t", all));
        assertTrue(e.getMessage().contains("WIN_32"), e.getMessage());
    }

    @Test
    void mixedSignednessIsRejected() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> resolve("wchar_t", seen(INT, 4, INT, 4, PROMOTED_INT, 4, PROMOTED_INT, 4)));
        assertTrue(e.getMessage().contains("wchar_t"), e.getMessage());
        assertTrue(e.getMessage().contains("WIN_64"), e.getMessage());
    }

    @Test
    void unmatchedSizePatternIsRejectedWithEveryTargetListed() {
        // 8 bytes on 32-bit Windows matches no kind.
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> resolve("odd_t", seen(LONG, 8, INT, 4, LONG_LONG, 8, LONG_LONG, 8)));
        assertTrue(e.getMessage().contains("odd_t"), e.getMessage());
        for (String target : new String[] {"UNIX_64", "UNIX_32_NOT_X86", "WIN_64", "WIN_32"})
            assertTrue(e.getMessage().contains(target), e.getMessage());
    }

    @Test
    void nonIntegerDisagreementIsRejected() {
        assertThrows(IllegalStateException.class, () -> resolve("t", seen(FLOAT, 4, INT, 4, INT, 4, INT, 4)));
    }

    @Test
    void plainCharIsNativeByteStraightFromClang() {
        assertEquals(NATIVE_BYTE, TypeKind.forClangKind(org.bytedeco.llvm.global.clang.CXType_Char_S));
        assertEquals(NATIVE_BYTE, TypeKind.forClangKind(org.bytedeco.llvm.global.clang.CXType_Char_U));
        assertEquals(SIGNED_BYTE, TypeKind.forClangKind(org.bytedeco.llvm.global.clang.CXType_SChar));
        assertEquals(PROMOTED_BYTE, TypeKind.forClangKind(org.bytedeco.llvm.global.clang.CXType_UChar));
    }
}
