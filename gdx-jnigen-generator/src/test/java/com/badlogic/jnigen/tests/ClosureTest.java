package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestStruct.TestStructPointer;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
        StructPointer<TestStruct> structPointer = new TestStructPointer();
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
        AtomicReference<StructPointer<TestStruct>> ref = new AtomicReference<>();
        ClosureObject<methodWithCallbackTestStructPointerArg> closureObject = ClosureObject.fromClosure(ref::set);
        call_methodWithCallbackTestStructPointerArg(closureObject);
        StructPointer<TestStruct> structPointer = ref.get();
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
    public void testCallbackStructReturn() {
        TestStruct struct = new TestStruct();
        struct.field3((char)77);
        ClosureObject<methodWithCallbackTestStructReturn> closureObject = ClosureObject.fromClosure(() -> struct);
        TestStruct ret = call_methodWithCallbackTestStructReturn(closureObject);
        assertNotEquals(struct.getPointer(), ret.getPointer());
        assertEquals(77, struct.field3());
        assertEquals(77, ret.field3());
        closureObject.free();
    }

    @Test
    public void testCallbackStructPointerReturn() {
        TestStructPointer structPointer = new TestStructPointer();
        structPointer.asStruct().field3((char)77);
        ClosureObject<methodWithCallbackTestStructPointerReturn> closureObject = ClosureObject.fromClosure(() -> structPointer);
        StructPointer<TestStruct> ret = call_methodWithCallbackTestStructPointerReturn(closureObject);
        assertEquals(structPointer.getPointer(), ret.getPointer());
        assertEquals(77, structPointer.asStruct().field3());
        assertEquals(77, ret.asStruct().field3());
        closureObject.free();
    }
}
