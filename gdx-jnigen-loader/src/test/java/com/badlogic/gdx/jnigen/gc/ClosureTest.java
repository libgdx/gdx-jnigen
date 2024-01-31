package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackBooleanReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackByteReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackCharReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackDoubleReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackFloatReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackIntReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackLongReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnAllArgs;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnBooleanArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnByteArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnCharArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnDoubleArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnFloatArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnIntArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnLongArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnNoArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnShortArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackNoReturnShortUpcastArg;
import com.badlogic.gdx.jnigen.gc.Closures.CallbackShortReturnNoArg;
import com.badlogic.gdx.jnigen.gc.TestStruct.CallbackNoReturnStructArg;
import com.badlogic.gdx.jnigen.gc.TestStruct.CallbackNoReturnStructPointerArg;
import com.badlogic.gdx.jnigen.gc.TestStruct.CallbackStructPointerReturnNoArg;
import com.badlogic.gdx.jnigen.gc.TestStruct.CallbackStructReturnNoArg;
import com.badlogic.gdx.jnigen.gc.TestStruct.TestStructPointer;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
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

import static org.junit.jupiter.api.Assertions.*;

public class ClosureTest extends BaseTest {

    @Test
    public void testCallbackNoValue() {
        AtomicBoolean changed = new AtomicBoolean(false);
        ClosureObject<CallbackNoReturnNoArg> closureObject = ClosureObject.fromClosure(() -> changed.set(true));
        Closures.methodWithCallback(closureObject);
        assertTrue(changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackLongArg() {
        AtomicLong changed = new AtomicLong(0);
        ClosureObject<CallbackNoReturnLongArg> closureObject = ClosureObject.fromClosure(changed::set);
        Closures.methodWithCallbackLongArg(closureObject.getFnPtr());
        assertEquals(5, changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackIntArg() {
        AtomicInteger changed = new AtomicInteger(0);
        ClosureObject<CallbackNoReturnIntArg> closureObject = ClosureObject.fromClosure(changed::set);
        Closures.methodWithCallbackIntArg(closureObject.getFnPtr());
        assertEquals(5, changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackShortArg() {
        ShortBuffer changed = ShortBuffer.allocate(1);
        ClosureObject<CallbackNoReturnShortArg> closureObject = ClosureObject.fromClosure(changed::put);
        Closures.methodWithCallbackShortArg(closureObject.getFnPtr());
        assertEquals(5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackShortUpcastArg() {
        IntBuffer changed = IntBuffer.allocate(1);
        ClosureObject<CallbackNoReturnShortUpcastArg> closureObject = ClosureObject.fromClosure(changed::put);
        Closures.methodWithCallbackShortUpcastArg(closureObject.getFnPtr());
        assertEquals(65535, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackByteArg() {
        ByteBuffer changed = ByteBuffer.allocate(1);
        ClosureObject<CallbackNoReturnByteArg> closureObject = ClosureObject.fromClosure(changed::put);
        Closures.methodWithCallbackByteArg(closureObject.getFnPtr());
        assertEquals(5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackCharArg() {
        ByteBuffer changed = ByteBuffer.allocate(2);
        ClosureObject<CallbackNoReturnCharArg> closureObject = ClosureObject.fromClosure(changed::putChar);
        Closures.methodWithCallbackCharArg(closureObject.getFnPtr());
        assertEquals(5, changed.getChar(0));
        closureObject.free();
    }

    @Test
    public void testCallbackBooleanArg() {
        AtomicBoolean changed = new AtomicBoolean(false);
        ClosureObject<CallbackNoReturnBooleanArg> closureObject = ClosureObject.fromClosure(changed::set);
        Closures.methodWithCallbackBooleanArg(closureObject.getFnPtr());
        assertTrue(changed.get());
        closureObject.free();
    }

    @Test
    public void testCallbackFloatArg() {
        FloatBuffer changed = FloatBuffer.allocate(1);
        ClosureObject<CallbackNoReturnFloatArg> closureObject = ClosureObject.fromClosure(changed::put);
        Closures.methodWithCallbackFloatArg(closureObject.getFnPtr());
        assertEquals(5.5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackDoubleArg() {
        DoubleBuffer changed = DoubleBuffer.allocate(1);
        ClosureObject<CallbackNoReturnDoubleArg> closureObject = ClosureObject.fromClosure(changed::put);
        Closures.methodWithCallbackDoubleArg(closureObject.getFnPtr());
        assertEquals(5.5, changed.get(0));
        closureObject.free();
    }

    @Test
    public void testCallbackStructArg() {
        new TestStruct();
        StructPointer<TestStruct> structPointer = new TestStructPointer();
        ClosureObject<CallbackNoReturnStructArg> closureObject = ClosureObject.fromClosure(structPointer::set);
        Closures.methodWithCallbackStructArg(closureObject.getFnPtr());
        TestStruct testStruct = structPointer.get();
        assertEquals(1, testStruct.getField1());
        assertEquals(2, testStruct.getField2());
        assertEquals(3, testStruct.getField3());
        assertEquals(4, testStruct.getField4());
        closureObject.free();
    }

    @Test
    public void testCallbackStructPointerArg() {
        new TestStruct();
        new TestStructPointer();
        AtomicReference<StructPointer<TestStruct>> ref = new AtomicReference<>();
        ClosureObject<CallbackNoReturnStructPointerArg> closureObject = ClosureObject.fromClosure(ref::set);
        Closures.methodWithCallbackStructPointerArg(closureObject.getFnPtr());
        StructPointer<TestStruct> structPointer = ref.get();
        TestStruct testStruct = structPointer.get();
        assertEquals(1, testStruct.getField1());
        assertEquals(2, testStruct.getField2());
        assertEquals(3, testStruct.getField3());
        assertEquals(4, testStruct.getField4());
        testStruct = null;
        closureObject.free();
        structPointer.free();
    }

    @Test
    public void testCallbackAllArgs() {
        ByteBuffer changed = ByteBuffer.allocate(30);
        ClosureObject<CallbackNoReturnAllArgs> closureObject = ClosureObject.fromClosure((arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
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
        Closures.methodWithCallbackAllArgs(closureObject.getFnPtr());
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
        ClosureObject<CallbackLongReturnNoArg> closureObject = ClosureObject.fromClosure(() -> 55);
        long ret = Closures.methodWithCallbackLongReturn(closureObject.getFnPtr());
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackIntReturn() {
        ClosureObject<CallbackIntReturnNoArg> closureObject = ClosureObject.fromClosure(() -> 55);
        int ret = Closures.methodWithCallbackIntReturn(closureObject.getFnPtr());
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackShortReturn() {
        ClosureObject<CallbackShortReturnNoArg> closureObject = ClosureObject.fromClosure(() -> (short)55);
        short ret = Closures.methodWithCallbackShortReturn(closureObject.getFnPtr());
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackCharReturn() {
        ClosureObject<CallbackCharReturnNoArg> closureObject = ClosureObject.fromClosure(() -> 'a');
        char ret = Closures.methodWithCallbackCharReturn(closureObject.getFnPtr());
        assertEquals('a', ret);
        closureObject.free();
    }

    @Test
    public void testCallbackByteReturn() {
        ClosureObject<CallbackByteReturnNoArg> closureObject = ClosureObject.fromClosure(() -> (byte)55);
        byte ret = Closures.methodWithCallbackByteReturn(closureObject.getFnPtr());
        assertEquals(55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackBooleanReturn() {
        ClosureObject<CallbackBooleanReturnNoArg> closureObject = ClosureObject.fromClosure(() -> true);
        boolean ret = Closures.methodWithCallbackBooleanReturn(closureObject.getFnPtr());
        assertTrue(ret);
        closureObject.free();
    }

    @Test
    public void testCallbackFloatReturn() {
        ClosureObject<CallbackFloatReturnNoArg> closureObject = ClosureObject.fromClosure(() -> 55.55f);
        float ret = Closures.methodWithCallbackFloatReturn(closureObject.getFnPtr());
        assertEquals(55.55f, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackDoubleReturn() {
        ClosureObject<CallbackDoubleReturnNoArg> closureObject = ClosureObject.fromClosure(() -> 55.55);
        double ret = Closures.methodWithCallbackDoubleReturn(closureObject.getFnPtr());
        assertEquals(55.55, ret);
        closureObject.free();
    }

    @Test
    public void testCallbackStructReturn() {
        TestStruct struct = new TestStruct();
        struct.setField3((short)77);
        ClosureObject<CallbackStructReturnNoArg> closureObject = ClosureObject.fromClosure(() -> struct);
        TestStruct ret = TestStruct.methodWithStructReturn(closureObject);
        assertNotEquals(struct.getPointer(), ret.getPointer());
        assertEquals(77, struct.getField3());
        assertEquals(77, ret.getField3());
        closureObject.free();
    }

    @Test
    public void testCallbackStructPointerReturn() {
        StructPointer<TestStruct> structPointer = new TestStructPointer();
        structPointer.asStruct().setField3((short)77);
        ClosureObject<CallbackStructPointerReturnNoArg> closureObject = ClosureObject.fromClosure(() -> structPointer);
        StructPointer<TestStruct> ret = TestStruct.methodWithStructPointerReturn(closureObject);
        assertEquals(structPointer.getPointer(), ret.getPointer());
        assertEquals(77, structPointer.asStruct().getField3());
        assertEquals(77, ret.asStruct().getField3());
        closureObject.free();
    }
}
