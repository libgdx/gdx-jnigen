package com.badlogic.jnigen.tests;

import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.gdx.jnigen.runtime.pointer.CSizedIntPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.PointerPointer;
import com.badlogic.jnigen.generated.TestData.*;
import com.badlogic.jnigen.generated.enums.TestEnum;
import com.badlogic.jnigen.generated.enums.TestEnum.TestEnumPointer;
import com.badlogic.jnigen.generated.structs.GlobalArg;
import com.badlogic.jnigen.generated.structs.TestStruct;
import com.badlogic.jnigen.generated.structs.TestStruct.TestStructPointer;
import com.badlogic.jnigen.generated.structs.TestUnion;
import com.badlogic.jnigen.generated.structs.TestUnion.TestUnionPointer;
import org.junit.jupiter.api.Test;

import static com.badlogic.jnigen.generated.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class DowncallTests extends BaseTest {

    @Test
    public void testDowncallUpcallException() {
        ClosureObject<methodWithCallbackCallThrowingCallback> callback = getCallThrowingCallbackCallback();
        ClosureObject<methodWithThrowingCallback> thrower = ClosureObject.fromClosure(() -> {
            throw new IllegalArgumentException();
        });

        assertThrows(IllegalArgumentException.class, () -> callback.getClosure().methodWithCallbackCallThrowingCallback_call(thrower));
    }

    @Test
    public void testCantFreeDowncall() {
        ClosureObject<methodWithCallback> voidCallback = getVoidCallback();
        assertThrows(IllegalArgumentException.class, voidCallback::free);
    }

    @Test
    public void testReuseClosureForDowncall() {
        ClosureObject<methodWithCallback> voidCallback = getVoidCallback();
        ClosureObject<methodWithCallback> voidCallback2 = getVoidCallback();
        assertEquals(voidCallback, voidCallback2);
    }

    @Test
    public void testCVoidCallback() {
        ClosureObject<methodWithCallback> voidCallback = getVoidCallback();
        voidCallback.getClosure().methodWithCallback_call();
        assertEquals(1, getGlobalArgState().longVal());
    }

    @Test
    public void testCLongArgCallback() {
        ClosureObject<methodWithCallbackLongArg> callback = getLongArgCallback();
        long testValue = 0x1234567890ABCDEFL;
        callback.getClosure().methodWithCallbackLongArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().longVal());
    }

    @Test
    public void testCIntArgCallback() {
        ClosureObject<methodWithCallbackIntArg> callback = getIntArgCallback();
        int testValue = 42;
        callback.getClosure().methodWithCallbackIntArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().intVal());
    }

    @Test
    public void testCShortArgCallback() {
        ClosureObject<methodWithCallbackShortArg> callback = getShortArgCallback();
        short testValue = 12345;
        callback.getClosure().methodWithCallbackShortArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().shortVal());
    }

    @Test
    public void testCByteArgCallback() {
        ClosureObject<methodWithCallbackByteArg> callback = getByteArgCallback();
        byte testValue = 123;
        callback.getClosure().methodWithCallbackByteArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().byteVal());
    }

    @Test
    public void testCCharArgCallback() {
        ClosureObject<methodWithCallbackCharArg> callback = getCharArgCallback();
        char testValue = 'A';
        callback.getClosure().methodWithCallbackCharArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().charVal());
    }

    @Test
    public void testCBooleanArgCallback() {
        ClosureObject<methodWithCallbackBooleanArg> callback = getBooleanArgCallback();
        boolean testValue = true;
        callback.getClosure().methodWithCallbackBooleanArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().boolVal());
    }

    @Test
    public void testCFloatArgCallback() {
        ClosureObject<methodWithCallbackFloatArg> callback = getFloatArgCallback();
        float testValue = 3.14159f;
        callback.getClosure().methodWithCallbackFloatArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().floatVal(), 0.0001f);
    }

    @Test
    public void testCDoubleArgCallback() {
        ClosureObject<methodWithCallbackDoubleArg> callback = getDoubleArgCallback();
        double testValue = 2.71828;
        callback.getClosure().methodWithCallbackDoubleArg_call(testValue);
        assertEquals(testValue, getGlobalArgState().doubleVal(), 0.0001);
    }

    @Test
    public void testCAllArgsCallback() {
        ClosureObject<methodWithCallbackAllArgs> callback = getAllArgsCallback();
        long arg1 = 0x1234567890ABCDEFL;
        int arg2 = 42;
        short arg3 = 12345;
        byte arg4 = 123;
        char arg5 = 'A';
        boolean arg6 = true;
        float arg7 = 3.14159f;
        double arg8 = 2.71828;

        callback.getClosure().methodWithCallbackAllArgs_call(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);

        GlobalArg state = getGlobalArgState();
        assertEquals(arg1, state.allArgs().arg1());
        assertEquals(arg2, state.allArgs().arg2());
        assertEquals(arg3, state.allArgs().arg3());
        assertEquals(arg4, state.allArgs().arg4());
        assertEquals(arg5, state.allArgs().arg5());
        assertEquals(arg6, state.allArgs().arg6());
        assertEquals(arg7, state.allArgs().arg7(), 0.0001f);
        assertEquals(arg8, state.allArgs().arg8(), 0.0001);
    }

    @Test
    public void testCLongReturnCallback() {
        ClosureObject<methodWithCallbackLongReturn> callback = getLongReturnCallback();
        assertEquals(123456789L, callback.getClosure().methodWithCallbackLongReturn_call());
    }

    @Test
    public void testCIntReturnCallback() {
        ClosureObject<methodWithCallbackIntReturn> callback = getIntReturnCallback();
        assertEquals(42, callback.getClosure().methodWithCallbackIntReturn_call());
    }

    @Test
    public void testCShortReturnCallback() {
        ClosureObject<methodWithCallbackShortReturn> callback = getShortReturnCallback();
        assertEquals(100, callback.getClosure().methodWithCallbackShortReturn_call());
    }

    @Test
    public void testCCharReturnCallback() {
        ClosureObject<methodWithCallbackCharReturn> callback = getCharReturnCallback();
        assertEquals(65, callback.getClosure().methodWithCallbackCharReturn_call());
    }

    @Test
    public void testCByteReturnCallback() {
        ClosureObject<methodWithCallbackByteReturn> callback = getByteReturnCallback();
        assertEquals('X', callback.getClosure().methodWithCallbackByteReturn_call());
    }

    @Test
    public void testCBooleanReturnCallback() {
        ClosureObject<methodWithCallbackBooleanReturn> callback = getBooleanReturnCallback();
        assertTrue(callback.getClosure().methodWithCallbackBooleanReturn_call());
    }

    @Test
    public void testCFloatReturnCallback() {
        ClosureObject<methodWithCallbackFloatReturn> callback = getFloatReturnCallback();
        assertEquals(3.14159f, callback.getClosure().methodWithCallbackFloatReturn_call(), 0.0001f);
    }

    @Test
    public void testCDoubleReturnCallback() {
        ClosureObject<methodWithCallbackDoubleReturn> callback = getDoubleReturnCallback();
        assertEquals(2.71828, callback.getClosure().methodWithCallbackDoubleReturn_call(), 0.0001);
    }

    @Test
    public void testCIntPtrPtrArg() {
        ClosureObject<methodWithIntPtrPtrArg> callback = getIntPtrPtrArgCallback();
        CSizedIntPointer ptr = new CSizedIntPointer("int");
        ptr.setInt(42);
        PointerPointer<CSizedIntPointer> ptrPtr = new PointerPointer<>(CSizedIntPointer.pointerPointer("int"));
        ptrPtr.setBackingCType("int");
        ptrPtr.setValue(ptr);
        callback.getClosure().methodWithIntPtrPtrArg_call(ptrPtr);
        assertEquals(ptrPtr.getValue().getInt(), getGlobalArgState().intPtrPtr().getValue().getInt());
    }

    @Test
    public void testCIntPtrPtrRet() {
        ClosureObject<methodWithIntPtrPtrRet> callback = getIntPtrPtrRetCallback();
        PointerPointer<CSizedIntPointer> result = callback.getClosure().methodWithIntPtrPtrRet_call();
        assertNotNull(result);
        assertEquals(42, result.getValue().getInt());
    }

    @Test
    public void testCTestStructReturn() {
        ClosureObject<methodWithCallbackTestStructReturn> callback = getTestStructReturnCallback();
        TestStruct result = callback.getClosure().methodWithCallbackTestStructReturn_call();
        assertEquals(0x1234567890ABCDEFL, result.field1());
        assertEquals(0x12345678, result.field2());
        assertEquals(0x1234, result.field3());
        assertEquals(0x12, result.field4());
    }

    @Test
    public void testCTestStructPointerReturn() {
        ClosureObject<methodWithCallbackTestStructPointerReturn> callback = getTestStructPointerReturnCallback();
        TestStructPointer result = callback.getClosure().methodWithCallbackTestStructPointerReturn_call();
        assertNotNull(result);
        assertEquals(0x1234567890ABCDEFL, result.get().field1());
    }

    @Test
    public void testCTestStructArg() {
        ClosureObject<methodWithCallbackTestStructArg> callback = getTestStructArgCallback();
        TestStruct testStruct = new TestStruct();
        testStruct.field1(0x1234567890ABCDEFL);
        testStruct.field2(0x12345678);
        testStruct.field3((char)0x1234);
        testStruct.field4((char)0x12);
        callback.getClosure().methodWithCallbackTestStructArg_call(testStruct);
        assertEquals(testStruct.field1(), getGlobalArgState().structVal().field1());
        assertEquals(testStruct.field2(), getGlobalArgState().structVal().field2());
        assertEquals(testStruct.field3(), getGlobalArgState().structVal().field3());
        assertEquals(testStruct.field4(), getGlobalArgState().structVal().field4());
    }

    @Test
    public void testCTestEnumReturn() {
        ClosureObject<methodWithCallbackTestEnumReturn> callback = getTestEnumReturnCallback();
        assertEquals(TestEnum.THIRD, callback.getClosure().methodWithCallbackTestEnumReturn_call());
    }

    @Test
    public void testCTestEnumArg() {
        ClosureObject<methodWithCallbackTestEnumArg> callback = getTestEnumArgCallback();
        callback.getClosure().methodWithCallbackTestEnumArg_call(TestEnum.SECOND);
        assertEquals(TestEnum.SECOND, getGlobalArgState().enumVal());
    }

    @Test
    public void testCTestEnumPointerReturn() {
        ClosureObject<methodWithCallbackTestEnumPointerReturn> callback = getTestEnumPointerReturnCallback();
        TestEnumPointer result = callback.getClosure().methodWithCallbackTestEnumPointerReturn_call();
        assertNotNull(result);
        assertEquals(TestEnum.SECOND, result.getEnumValue());
    }

    @Test
    public void testCIntPointerReturn() {
        ClosureObject<methodWithCallbackIntPointerReturn> callback = getIntPointerReturnCallback();
        CSizedIntPointer result = callback.getClosure().methodWithCallbackIntPointerReturn_call();
        assertNotNull(result);
        assertEquals(42, result.getInt());
    }

    @Test
    public void testCTestUnionPointerReturn() {
        ClosureObject<methodWithCallbackTestUnionPointerReturn> callback = getTestUnionPointerReturnCallback();
        TestUnionPointer result = callback.getClosure().methodWithCallbackTestUnionPointerReturn_call();
        assertNotNull(result);
        assertEquals(0x1234567890ABCDEFL, result.get().uintType());
    }

    @Test
    public void testCTestUnionPointerArg() {
        ClosureObject<methodWithCallbackTestUnionPointerArg> callback = getTestUnionPointerArgCallback();
        TestUnion testUnion = new TestUnion();
        testUnion.uintType(0x1234567890ABCDEFL);
        callback.getClosure().methodWithCallbackTestUnionPointerArg_call(testUnion.asPointer());
        assertEquals(testUnion.getPointer(), getGlobalArgState().unionPtr().getPointer());
    }
}
