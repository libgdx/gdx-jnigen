package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.pointer.EnumPointer;
import com.badlogic.gdx.jnigen.pointer.FloatPointer;
import com.badlogic.gdx.jnigen.pointer.PointerPointer;
import com.badlogic.gdx.jnigen.pointer.VoidPointer;
import com.badlogic.jnigen.generated.TestData;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerArg;
import com.badlogic.jnigen.generated.TestData.methodWithCallbackIntPointerReturn;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestStruct.TestStructPointer;
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
    public void testCallbackIntPointerReturnWrongType() {
        CSizedIntPointer ptr = new CSizedIntPointer("uint32_t");
        ClosureObject<methodWithCallbackIntPointerReturn> closureObject = ClosureObject.fromClosure(() -> ptr);
        assertThrows(IllegalArgumentException.class, () -> call_methodWithCallbackIntPointerReturn(closureObject, 20));

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

    @Test
    public void voidPointerPointerTest() {
        PointerPointer<VoidPointer> voidPtrPtr = new PointerPointer<>(VoidPointer::new, 2);
        PointerPointer<VoidPointer> ret = TestData.voidPointerPointer(voidPtrPtr);

        assertEquals(voidPtrPtr.getPointer(), ret.getPointer());

        PointerPointer<VoidPointer> wrongDepth = new PointerPointer<>(VoidPointer::new, 3);
        assertThrows(IllegalArgumentException.class, () -> TestData.voidPointerPointer(wrongDepth));
    }

    // TODO: 21.03.24 Closure PtrPtr tests

    @Test
    public void enumPointerPointerTest() {
        PointerPointer<EnumPointer<TestEnum>> pointer = new PointerPointer<>(EnumPointer.getPointerPointerSupplier(TestEnum::getByIndex), 2);
        assertEquals(pointer.getPointer(), TestData.enumPointerPointer(pointer).getPointer());
        assertEquals(TestEnum.SECOND, pointer.getValue().getEnumValue());
        pointer.getValue().free();
    }

    @Test
    public void structPointerPointerTest() {
        PointerPointer<TestStructPointer> pointer = new PointerPointer<>(TestStructPointer::new, 2);
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
        PointerPointer<CSizedIntPointer> pointer = new PointerPointer<>(CSizedIntPointer.getPointerPointerSupplier("int"), 2);
        assertEquals(pointer.getPointer(), TestData.intPointerPointer(pointer).getPointer());
        assertEquals(5, pointer.getValue().getInt());

        pointer.getValue().free();
    }

    @Test
    public void floatPointerPointerTest() {
        PointerPointer<FloatPointer> pointer = new PointerPointer<>(FloatPointer::new, 2);
        assertEquals(pointer.getPointer(), TestData.floatPointerPointer(pointer).getPointer());
        assertEquals(5.5f, pointer.getValue().getFloat());

        pointer.getValue().free();
    }

    @Test
    public void voidPointerPointerManyTest() {
        PointerPointer<PointerPointer<PointerPointer<PointerPointer<VoidPointer>>>> pointer = new PointerPointer<>(VoidPointer::new, 5);
        assertEquals(pointer.getPointer(), TestData.pointerPointerManyyy(pointer).getPointer());
        assertEquals(5, pointer.getValue().getValue().getValue().getValue().getPointer());

        pointer.getValue().getValue().getValue().free();
        pointer.getValue().getValue().free();
        pointer.getValue().free();
    }
}
