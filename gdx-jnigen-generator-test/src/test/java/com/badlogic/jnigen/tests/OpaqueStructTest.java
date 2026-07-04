package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackTimespecPointerArg;
import com.badlogic.jnigen.generated.structs.TimeHolder;
import com.badlogic.jnigen.generated.structs.forwardDeclStruct;
import com.badlogic.jnigen.generated.structs.timespec;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpaqueStructTest extends BaseTest {

    /**
     * forwardDeclStruct is only forward-declared in the header, so it is unsized/opaque: the generator
     * emits only a typed pointer (extending VoidPointer) and no allocatable struct class. It can only
     * be handled as a C-owned handle. {@code new forwardDeclStruct()} does not compile - there is no
     * public constructor.
     */
    @Test
    public void testForwardDeclaredOpaqueHandle() {
        forwardDeclStruct.forwardDeclStructPointer handle = TestData.createForwardDeclStruct(42);
        assertTrue(handle instanceof VoidPointer);
        assertEquals(42, TestData.readForwardDeclStructValue(handle));
        TestData.freeForwardDeclStruct(handle);
    }

    /**
     * struct timespec lives in a system header: it has a real size but its fields are not bound, so it
     * is emitted as a sized opaque type reachable through a typed pointer (no longer a bare VoidPointer,
     * as FILE* used to be). The pointer faithfully carries the address across the call boundary.
     */
    @Test
    public void testSystemHeaderStructPointer() {
        timespec.timespecPointer ts = new timespec.timespecPointer();
        TestData.fillTimespec(ts);
        assertEquals(42, TestData.tsSeconds(ts));
    }

    /**
     * TimeHolder embeds struct timespec by value. Even though the timespec member is inaccessible, the
     * container must be allocated at its true C size (the pre-fix bug under-allocated it because the
     * system field was counted as ~1 byte). Verifies the Java allocation size matches the native
     * sizeof, then that C can write both the embedded timespec and the trailing marker into the
     * Java-allocated buffer without corruption.
     */
    @Test
    public void testSystemHeaderStructByValueContainer() {
        TimeHolder holder = new TimeHolder();
        assertEquals(TestData.timeHolderSize(), holder.getSize());

        TestData.fillTimeHolder(holder.asPointer(), 0x1234);
        assertEquals(0x1234, TestData.readTimeHolderMarker(holder.asPointer()));
    }

    /**
     * A closure may carry a system-header struct across the boundary BY POINTER. Only the pointer value
     * crosses libFFI, so no fabricated (and ABI-incorrect) struct classification is ever used - the
     * generator allows this, whereas the by-value form is refused at generation time. The C side fills a
     * timespec and hands its pointer to the callback, which reads it back through the bound tsSeconds
     * accessor. That this test compiles at all proves the closure was generated (the guard did not fire).
     */
    @Test
    public void testSystemHeaderStructThroughClosureByPointer() {
        AtomicLong seconds = new AtomicLong();
        ClosureObject<methodWithCallbackTimespecPointerArg> cb =
                ClosureObject.fromClosure(ts -> seconds.set(TestData.tsSeconds(ts)));
        TestData.call_methodWithCallbackTimespecPointerArg(cb);
        assertEquals(42, seconds.get());
        cb.free();
    }
}
