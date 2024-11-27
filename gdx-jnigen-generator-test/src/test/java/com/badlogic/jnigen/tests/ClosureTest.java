package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestStruct.TestStructPointer;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.jnigen.generated.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClosureTest extends BaseTest {

    @Test
    public void testCallbackNoValue() {
        AtomicBoolean changed = new AtomicBoolean(false);
        ClosureObject<methodWithCallback> closureObject = ClosureObject.fromClosure(() -> changed.set(true));
        call_methodWithCallback(closureObject);
        assertTrue(changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackThrows() {
        ClosureObject<methodWithThrowingCallback> closureObject = ClosureObject.fromClosure(() -> {
            throw new IllegalStateException("This is expected");
        });
        assertThrows(IllegalStateException.class, () -> call_methodWithThrowingCallback(closureObject));
        closureObject.free();
    }

    @Test
    public void testCallbackLongArg() {
        AtomicLong changed = new AtomicLong(0);
        ClosureObject<methodWithCallbackLongArg> closureObject = ClosureObject.fromClosure(changed::set);
        call_methodWithCallbackLongArg(closureObject);
        assertEquals(5, changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackIntArg() {
        AtomicInteger changed = new AtomicInteger(0);
        ClosureObject<methodWithCallbackIntArg> closureObject = ClosureObject.fromClosure(changed::set);
        call_methodWithCallbackIntArg(closureObject);
        assertEquals(5, changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackShortArg() {
        ShortBuffer changed = ShortBuffer.allocate(1);
        ClosureObject<methodWithCallbackShortArg> closureObject = ClosureObject.fromClosure(changed::put);
        call_methodWithCallbackShortArg(closureObject);
        assertEquals(5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackByteArg() {
        ByteBuffer changed = ByteBuffer.allocate(1);
        ClosureObject<methodWithCallbackByteArg> closureObject = ClosureObject.fromClosure(changed::put);
        call_methodWithCallbackByteArg(closureObject);
        assertEquals(5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackCharArg() {
        ByteBuffer changed = ByteBuffer.allocate(2);
        ClosureObject<methodWithCallbackCharArg> closureObject = ClosureObject.fromClosure(changed::putChar);
        call_methodWithCallbackCharArg(closureObject);
        assertEquals(5, changed.getChar(0));
        closureObject.free();
    }

    @Test
    public void testCallbackBooleanArg() {
        AtomicBoolean changed = new AtomicBoolean(false);
        ClosureObject<methodWithCallbackBooleanArg> closureObject = ClosureObject.fromClosure(changed::set);
        call_methodWithCallbackBooleanArg(closureObject);
        assertTrue(changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackFloatArg() {
        FloatBuffer changed = FloatBuffer.allocate(1);
        ClosureObject<methodWithCallbackFloatArg> closureObject = ClosureObject.fromClosure(changed::put);
        call_methodWithCallbackFloatArg(closureObject);
        assertEquals(5.5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackDoubleArg() {
        DoubleBuffer changed = DoubleBuffer.allocate(1);
        ClosureObject<methodWithCallbackDoubleArg> closureObject = ClosureObject.fromClosure(changed::put);
        call_methodWithCallbackDoubleArg(closureObject);
        assertEquals(5.5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackStructArg() {
        TestStructPointer structPointer = new TestStructPointer();
        ClosureObject<methodWithCallbackTestStructArg> closureObject = ClosureObject.fromClosure(structPointer::set);
        call_methodWithCallbackTestStructArg(closureObject);
        TestStruct testStruct = structPointer.get();
        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
        closureObject.free();
    }

    @Test
    public void testCallbackStructPointerArg() {
        new TestStruct();
        AtomicReference<TestStructPointer> ref = new AtomicReference<>();
        ClosureObject<methodWithCallbackTestStructPointerArg> closureObject = ClosureObject.fromClosure(ref::set);
        call_methodWithCallbackTestStructPointerArg(closureObject);
        StackElementPointer<TestStruct> structPointer = ref.get();
        TestStruct testStruct = structPointer.get();
        assertEquals(1, testStruct.field1());
        assertEquals(2, testStruct.field2());
        assertEquals(3, testStruct.field3());
        assertEquals(4, testStruct.field4());
        testStruct = null;
        closureObject.free();
        structPointer.free();
    }

    @Test
    public void testCallbackAllArgs() {
        ByteBuffer changed = ByteBuffer.allocate(30);
        ClosureObject<methodWithCallbackAllArgs> closureObject = ClosureObject.fromClosure((arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
            changed.putLong(arg1);
            changed.putInt(arg2);
            changed.putShort(arg3);
            changed.put(arg4);
            changed.putChar(arg5);
            changed.put((byte)(arg6 ? 1 : 0));
            changed.putFloat(arg7);
            changed.putDouble(arg8);
            changed.flip();
        });
        call_methodWithCallbackAllArgs(closureObject);
        assertEquals(1, changed.getLong());
        assertEquals(2, changed.getInt());
        assertEquals(3, changed.getShort());
        assertEquals(4, changed.get());
        assertEquals(5, changed.getChar());
        assertEquals(1, changed.get());
        assertEquals(6.6f, changed.getFloat());
        assertEquals(7.7, changed.getDouble());
        closureObject.free();
    }

    @Test
    public void testCallbackLongReturn() {
        ClosureObject<methodWithCallbackLongReturn> closureObject = ClosureObject.fromClosure(() -> 55);
        long ret = call_methodWithCallbackLongReturn(closureObject);
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackIntReturn() {
        ClosureObject<methodWithCallbackIntReturn> closureObject = ClosureObject.fromClosure(() -> 55);
        int ret = call_methodWithCallbackIntReturn(closureObject);
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackShortReturn() {
        ClosureObject<methodWithCallbackShortReturn> closureObject = ClosureObject.fromClosure(() -> (short)55);
        short ret = call_methodWithCallbackShortReturn(closureObject);
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackCharReturn() {
        ClosureObject<methodWithCallbackCharReturn> closureObject = ClosureObject.fromClosure(() -> 'a');
        char ret = call_methodWithCallbackCharReturn(closureObject);
        assertEquals('a', ret);
        closureObject.free();
    }

    @Test
    public void testCallbackByteReturn() {
        ClosureObject<methodWithCallbackByteReturn> closureObject = ClosureObject.fromClosure(() -> (byte)55);
        byte ret = call_methodWithCallbackByteReturn(closureObject);
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackBooleanReturn() {
        ClosureObject<methodWithCallbackBooleanReturn> closureObject = ClosureObject.fromClosure(() -> true);
        boolean ret = call_methodWithCallbackBooleanReturn(closureObject);
        assertTrue(ret);
        closureObject.free();
    }

    @Test
    public void testCallbackFloatReturn() {
        ClosureObject<methodWithCallbackFloatReturn> closureObject = ClosureObject.fromClosure(() -> 55.55f);
        float ret = call_methodWithCallbackFloatReturn(closureObject);
        assertEquals(55.55f, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackDoubleReturn() {
        ClosureObject<methodWithCallbackDoubleReturn> closureObject = ClosureObject.fromClosure(() -> 55.55);
        double ret = call_methodWithCallbackDoubleReturn(closureObject);
        assertEquals(55.55, ret);
        closureObject.free();
    }

    @Test
    public void closureFromThread() {
        AtomicReference<String> spawnedThreadName = new AtomicReference<>();
        ClosureObject<thread_callback> closureObject = ClosureObject.fromClosure((arg) -> {
            spawnedThreadName.set(Thread.currentThread().getName());
            return arg;
        });
        call_callback_in_thread(closureObject);
        assertNotEquals(spawnedThreadName.get(), Thread.currentThread().getName());
        assertTrue(spawnedThreadName.get().startsWith("Thread-"));
        closureObject.free();
    }
}
