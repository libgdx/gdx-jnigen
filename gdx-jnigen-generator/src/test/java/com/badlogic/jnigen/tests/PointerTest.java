package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerReturn;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.jnigen.generated.TestData.call_methodWithCallbackIntPointerArg;
import static com.badlogic.jnigen.generated.TestData.call_methodWithCallbackIntPointerReturn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointerTest extends BaseTest {

    @Test
    public void testCallbackIntPointerReturn() {
        CSizedIntPointer ptr = new CSizedIntPointer("int");
        ptr.setInt(14);
        assertEquals(14, ptr.getInt());
        ClosureObject<methodWithCallbackIntPointerReturn> closureObject = ClosureObject.fromClosure(() -> ptr);
        CSizedIntPointer ret = call_methodWithCallbackIntPointerReturn(closureObject, 20);
        assertEquals(ret.getPointer(), ptr.getPointer());
        assertEquals(20, ptr.getInt());
        closureObject.free();
    }

    @Test
    public void testCallbackIntPointerArg() {
        AtomicReference<CSizedIntPointer> ptrRef = new AtomicReference<>();
        ClosureObject<methodWithCallbackIntPointerArg> closureObject = ClosureObject.fromClosure((arg) -> {
            ptrRef.set(arg);
            assertEquals(15, arg.getInt());
            arg.setInt(22);
            return arg.getInt();
        });
        assertEquals(22, call_methodWithCallbackIntPointerArg(closureObject));
        assertEquals(22, ptrRef.get().getInt());
        ptrRef.get().free();
        closureObject.free();
    }

    @Test
    public void testPassPointer() {
        CSizedIntPointer pointer = new CSizedIntPointer("int", 1);
        for (int i = -10; i < 10; i++) {
            pointer.setInt(i, 0);
            assertEquals(i, TestData.passIntPointer(pointer));
        }
    }

    @Test
    public void testPassIncorrectPointer() {
        CSizedIntPointer pointer = new CSizedIntPointer("uint32_t", 1);
        assertThrows(IllegalArgumentException.class, () -> TestData.passIntPointer(pointer));
    }

    @Test
    public void testReturnPointer() {
        for (int i = -10; i < 10; i++) {
            CSizedIntPointer pointer = TestData.returnIntPointer(i);
            assertEquals(i, pointer.getInt(0));
            pointer.free();
        }
    }
}
