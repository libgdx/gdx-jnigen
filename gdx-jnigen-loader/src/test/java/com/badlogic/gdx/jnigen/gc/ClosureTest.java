package com.badlogic.gdx.jnigen.gc;

import com.badlogic.gdx.jnigen.Global;
import com.badlogic.gdx.jnigen.closure.ClosureObject;
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
import com.badlogic.gdx.jnigen.gc.TestStruct.CallbackNoReturnStructArg;
import com.badlogic.gdx.jnigen.gc.TestStruct.CallbackNoReturnStructPointerArg;
import com.badlogic.gdx.jnigen.pointer.StructPointer;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClosureTest {


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
        Global.free(0);
        new TestStruct();
        StructPointer<TestStruct> structPointer = new StructPointer<>();
        ClosureObject<CallbackNoReturnStructArg> closureObject = ClosureObject.fromClosure(structPointer::set);
        Closures.methodWithCallbackStructArg(closureObject.getFnPtr());
        TestStruct testStruct = structPointer.get(TestStruct.class);
        assertEquals(1, testStruct.getField1());
        assertEquals(2, testStruct.getField2());
        assertEquals(3, testStruct.getField3());
        assertEquals(4, testStruct.getField4());
        closureObject.free();
    }

    @Test
    public void testCallbackStructPointerArg() {
        Global.free(0);
        new TestStruct();
        new StructPointer<>();
        AtomicReference<StructPointer<TestStruct>> ref = new AtomicReference<>();
        ClosureObject<CallbackNoReturnStructPointerArg> closureObject = ClosureObject.fromClosure(ref::set);
        Closures.methodWithCallbackStructPointerArg(closureObject.getFnPtr());
        StructPointer<TestStruct> structPointer = ref.get();
        TestStruct testStruct = structPointer.get(TestStruct.class);
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
}
