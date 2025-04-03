package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.PointerPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.BytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SBytePointer;
import com.badlogic.gdx.jnigen.runtime.pointer.integer.SIntPointer;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerReturn;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.jnigen.generated.enums.TestEnum.TestEnumPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestStruct.TestStructPointer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.jnigen.generated.TestData.call_methodWithCallbackIntPointerArg;
import static com.badlogic.jnigen.generated.TestData.call_methodWithCallbackIntPointerReturn;
import static org.junit.jupiter.api.Assertions.*;

public class PointerTest extends BaseTest {

    @Test
    public void testCallbackIntPointerReturn() {
        SIntPointer ptr = new SIntPointer();
        ptr.setInt(14);
        assertEquals(14, ptr.getInt());
        ClosureObject<methodWithCallbackIntPointerReturn> closureObject = ClosureObject.fromClosure(() -> ptr);
        SIntPointer ret = call_methodWithCallbackIntPointerReturn(closureObject, 20);
        assertEquals(ret.getPointer(), ptr.getPointer());
        assertEquals(20, ptr.getInt());
        closureObject.free();
    }

    @Test
    public void testCallbackIntPointerArg() {
        AtomicReference<SIntPointer> ptrRef = new AtomicReference<>();
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
        SIntPointer pointer = new SIntPointer();
        for (int i = -10; i < 10; i++) {
            pointer.setInt(i, 0);
            assertEquals(i, TestData.passIntPointer(pointer));
        }
    }

    @Test
    public void testReturnPointer() {
        for (int i = -10; i < 10; i++) {
            SIntPointer pointer = TestData.returnIntPointer(i);
            assertEquals(i, pointer.getInt(0));
            pointer.free();
        }
    }

    @Test
    public void voidPointerPointerTest() {
        PointerPointer<VoidPointer> voidPtrPtr = new PointerPointer<>(VoidPointer::new);
        PointerPointer<VoidPointer> ret = TestData.voidPointerPointer(voidPtrPtr);

        assertEquals(voidPtrPtr.getPointer(), ret.getPointer());
    }

    @Test
    public void closureIntPtrPtrTest() {
        ClosureObject<TestData.methodWithIntPtrPtrArg> ptrPtrArg = ClosureObject.fromClosure(arg0 -> {
            assertEquals(5, arg0.getValue().getInt());
        });
        TestData.call_methodWithIntPtrPtrArg(ptrPtrArg);
        ptrPtrArg.free();

        PointerPointer<SIntPointer> pointer = new PointerPointer<>(SIntPointer::new);
        ClosureObject<TestData.methodWithIntPtrPtrRet> ptrPtrRet = ClosureObject.fromClosure(() -> pointer);
        TestData.call_methodWithIntPtrPtrRet(ptrPtrRet);
        ptrPtrRet.free();
    }

    @Test
    public void enumPointerPointerTest() {
        PointerPointer<TestEnumPointer> pointer = new PointerPointer<>(TestEnumPointer::new);
        assertEquals(pointer.getPointer(), TestData.enumPointerPointer(pointer).getPointer());
        assertEquals(TestEnum.SECOND, pointer.getValue().getEnumValue());
        pointer.getValue().free();
    }

    @Test
    public void structPointerPointerTest() {
        PointerPointer<TestStructPointer> pointer = new PointerPointer<>(TestStructPointer::new);
        assertEquals(pointer.getPointer(), TestData.structPointerPointer(pointer).getPointer());
        TestStruct struct = pointer.getValue().get();
        assertEquals(1, struct.field1());
        assertEquals(2, struct.field2());
        assertEquals(3, struct.field3());
        assertEquals(4, struct.field4());

        pointer.getValue().free();
    }

    @Test
    public void intPointerPointerTest() {
        PointerPointer<SIntPointer> pointer = new PointerPointer<>(SIntPointer::new);
        assertEquals(pointer.getPointer(), TestData.intPointerPointer(pointer).getPointer());
        assertEquals(5, pointer.getValue().getInt());

        pointer.getValue().free();
    }

    @Test
    public void floatPointerPointerTest() {
        PointerPointer<FloatPointer> pointer = new PointerPointer<>(FloatPointer::new);
        assertEquals(pointer.getPointer(), TestData.floatPointerPointer(pointer).getPointer());
        assertEquals(5.5f, pointer.getValue().getFloat());

        pointer.getValue().free();
    }

    @Test
    public void voidPointerPointerManyTest() {
        PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> pointer = new PointerPointer<>((long peer5, boolean owned5) -> new PointerPointer<>(
                peer5, false,
                (long peer4, boolean owned4) -> new PointerPointer<>(peer4, false,
                        (long peer3, boolean owned3) -> new PointerPointer<>(peer3, false,
                                (long peer2, boolean owned2) -> new VoidPointer(peer2, false)))));
        assertEquals(pointer.getPointer(), TestData.pointerPointerManyyy(pointer).getPointer());
        assertEquals(5, pointer.getValue().getValue().getValue().getValue().getPointer());

        pointer.getValue().getValue().getValue().free();
        pointer.getValue().getValue().free();
        pointer.getValue().free();
    }

    @Test
    public void stringTests() {
        BytePointer rightPointer = BytePointer.fromString("TEST STRING", true);
        assertEquals("TEST STRING", rightPointer.getString());
        assertTrue(TestData.validateString(rightPointer));

        BytePointer wrongPointer = BytePointer.fromString("TEST STRING ", true);
        assertFalse(TestData.validateString(wrongPointer));

        BytePointer ret = TestData.returnString();
        assertEquals("HALLO 123", ret.getString());
        assertFalse(TestData.validateString(ret));

        ret.free();
    }
}
