package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SWordPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.UWordPointer;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackSizeT;
import com.badlogic.jnigen.generated.structs.WordStruct;
import org.junit.jupiter.api.Test;

import static com.badlogic.jnigen.generated.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Word-sized C integers (size_t, ssize_t, ptrdiff_t, intptr_t, uintptr_t and typedefs of them) are
 * 4 bytes on 32-bit and 8 bytes on 64-bit targets, including Windows x64 where a plain long stays 4
 * bytes. The generator binds them through the WORD / PROMOTED_WORD kinds; these tests pin the
 * resulting accessor widths, struct layout, pointer classes and closure marshalling against C.
 */
public class WordTypeTest extends BaseTest {

    @Test
    public void testSizeTRoundTrip() {
        assertEquals(6, passSizeT(5));
    }

    @Test
    public void testSizeTMaxIsAllOnesOverThePointerWidth() {
        assertEquals(CHandler.IS_64_BIT ? -1L : 0xFFFFFFFFL, maxSizeT());
    }

    @Test
    public void testSSizeTKeepsTheSign() {
        assertEquals(-42, negateSSizeT(42));
        assertEquals(42, negateSSizeT(-42));
    }

    @Test
    public void testPointerSizedIntegersCarryARealAddress() {
        SIntPointer pointer = new SIntPointer();
        long address = pointer.getPointer();
        assertEquals(address, passIntPtrT(address));
        assertEquals(address, passUIntPtrT(address));
        assertEquals(-3, passPtrDiffT(-3));
    }

    @Test
    public void testTypedefOfSizeTIsWordSized() {
        assertEquals(7, passMySizeT(7));
    }

    @Test
    public void testWordStructLayoutMatchesC() {
        WordStruct struct = new WordStruct();
        assertEquals(wordStructSize(), struct.getSize());

        fillWordStruct(struct.asPointer());
        assertEquals(7, struct.tag());
        assertEquals(42, struct.count());
        assertEquals(-5, struct.delta());
        assertEquals(struct.getPointer(), struct.address());
        assertEquals(99, struct.aliased());

        struct.count(123);
        struct.delta(-7);
        assertEquals(123, readWordStructCount(struct.asPointer()));
        assertEquals(-7, readWordStructDelta(struct.asPointer()));
    }

    /** size_t* and const size_t* both bind to UWordPointer, so this compiling is part of the check. */
    @Test
    public void testSizeTArrayThroughUWordPointer() {
        UWordPointer values = new UWordPointer(4);
        fillSizeTArray(values, 4);
        assertEquals(0, values.getLong(0));
        assertEquals(6, values.getLong(2));
        assertEquals(9, values.getLong(3));

        values.setLong(0xFFFFFFFFL, 0);
        assertEquals(0xFFFFFFFFL, values.getLong(0));
        assertEquals(0xFFFFFFFFL + 3 + 6 + 9, sumSizeTArray(values, 4));
    }

    @Test
    public void testSSizeTArrayThroughSWordPointer() {
        SWordPointer values = new SWordPointer(2);
        values.setLong(-1, 0);
        values.setLong(-2, 1);
        assertEquals(-3, sumSSizeTArray(values, 2));
    }

    @Test
    public void testCallbackWithWordArgsAndReturn() {
        ClosureObject<methodWithCallbackSizeT> closureObject = ClosureObject.fromClosure((in, neg) -> in + neg);
        assertEquals(7, call_methodWithCallbackSizeT(closureObject, 10, -3));
        closureObject.free();
    }

    /** conditional_word_t is unsigned long on LP64 unix, unsigned int on ILP32 and unsigned long long on
     *  Win64; the declared UWordPointer parameter type is the compile-time proof it resolved to a word. */
    @Test
    public void testWin64ConditionalTypedefIsWordSized() {
        assertEquals(0xFFFFFFFFL, passConditionalWord(0xFFFFFFFFL));
        // On 64-bit hosts (Win64 in particular) this actually exercises the 8-byte marshalling; on 32-bit
        // hosts the value does not fit into a word, so it is skipped there.
        if (CHandler.IS_64_BIT)
            assertEquals(1L << 32, passConditionalWord(1L << 32));
        UWordPointer values = new UWordPointer(3);
        values.setLong(1, 0);
        values.setLong(2, 1);
        values.setLong(3, 2);
        assertEquals(6, sumConditionalWords(values, 3));
    }
}
