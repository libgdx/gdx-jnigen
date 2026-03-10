package com.badlogic.gdx.jnigen.teavm;

import org.teavm.backend.javascript.codegen.SourceWriter;
import org.teavm.backend.javascript.spi.Generator;
import org.teavm.backend.javascript.spi.GeneratorContext;
import org.teavm.model.ClassReader;
import org.teavm.model.ElementModifier;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.MethodReader;
import org.teavm.model.MethodReference;
import org.teavm.model.PrimitiveType;
import org.teavm.model.ValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * TeaVM code generator that emits JavaScript calling Emscripten-exported JNI functions.
 * <p>
 * For a native method like {@code MiniAudio.jniInitContext(int, short, boolean)},
 * this generator emits:
 * <pre>
 * return Module._Java_games_rednblack_miniaudio_MiniAudio_jniInitContext(0, 0, p1, p2, p3);
 * </pre>
 * The first two zero arguments replace JNIEnv* and jclass/jobject.
 * <p>
 * Special handling:
 * <ul>
 *   <li>{@code long} params: wrapped with {@code Number()} (TeaVM BigInt → JS number)</li>
 *   <li>{@code long} return: wrapped with {@code BigInt()} (JS number → TeaVM BigInt)</li>
 *   <li>{@code String} params: allocated into WASM linear memory as UTF-8, freed after call</li>
 * </ul>
 */
public class JniMethodGenerator implements Generator {

    @Override
    public void generate (GeneratorContext context, SourceWriter writer, MethodReference methodRef) {
        // Determine if method is static or instance
        ClassReader cls = context.getClassSource().get(methodRef.getClassName());
        MethodReader method = cls != null ? cls.getMethod(methodRef.getDescriptor()) : null;
        boolean isStatic = method != null && method.hasModifier(ElementModifier.STATIC);

        // Compute JNI-mangled export name
        // Check for overloaded native methods. A method is considered a JNI native if it
        // is still marked NATIVE or has already been annotated with @GeneratedBy(JniMethodGenerator).
        boolean isOverloaded = false;
        if (cls != null) {
            int count = 0;
            for (MethodReader m : cls.getMethods()) {
                if (m.getName().equals(methodRef.getName())
                        && (m.hasModifier(ElementModifier.NATIVE) || m.getAnnotations().get("org.teavm.backend.javascript.spi.GeneratedBy") != null)) {
                    count++;
                }
            }
            isOverloaded = count > 1;
        }

        String jniName = CHandlerTransformer.computeJniName(
                methodRef.getClassName(), methodRef.getName(), methodRef.getDescriptor(), isOverloaded);

        MethodDescriptor descriptor = methodRef.getDescriptor();
        ValueType resultType = descriptor.getResultType();
        boolean hasReturn = resultType != ValueType.VOID;
        boolean returnsLong = isLongType(resultType);
        int paramCount = methodRef.parameterCount();

        // Identify String parameters that need WASM memory allocation
        List<Integer> stringParamIndices = new ArrayList<>();
        for (int i = 0; i < paramCount; i++) {
            if (isStringType(descriptor.parameterType(i))) {
                stringParamIndices.add(i);
            }
        }

        // Identify primitive array parameters that need WASM memory allocation
        List<Integer> arrayParamIndices = new ArrayList<>();
        for (int i = 0; i < paramCount; i++) {
            if (isPrimitiveArrayType(descriptor.parameterType(i))) {
                arrayParamIndices.add(i);
            }
        }

        // For String params: convert TeaVM string to JS string, allocate WASM memory and write UTF-8 bytes
        for (int idx : stringParamIndices) {
            String paramName = context.getParameterName(idx + 1);
            String ptrVar = "_s" + idx;
            String jsStrVar = "_js" + idx;
            // Convert TeaVM string object to native JS string via $rt_ustr()
            writer.append("var ").append(ptrVar).append(" = 0;").softNewLine();
            writer.append("if (").append(paramName).append(" !== null) {").indent().softNewLine();
            writer.append("var ").append(jsStrVar).append(" = ").appendFunction("$rt_ustr").append("(").append(paramName).append(");").softNewLine();
            writer.append("var _len" + idx + " = Module.lengthBytesUTF8(").append(jsStrVar).append(") + 1;").softNewLine();
            writer.append(ptrVar).append(" = Module._malloc(_len" + idx + ");").softNewLine();
            writer.append("Module.stringToUTF8(").append(jsStrVar).append(", ").append(ptrVar).append(", _len" + idx + ");").softNewLine();
            writer.outdent().append("}").softNewLine();
        }

        // For primitive array params: copy TeaVM array data into WASM linear memory
        for (int idx : arrayParamIndices) {
            String paramName = context.getParameterName(idx + 1);
            String ptrVar = "_a" + idx;
            String bufVar = "_buf" + idx;
            writer.append("var ").append(ptrVar).append(" = 0;").softNewLine();
            writer.append("if (").append(paramName).append(" !== null) {").indent().softNewLine();
            writer.append("var ").append(bufVar).append(" = ").append(paramName).append(".data;").softNewLine();
            writer.append(ptrVar).append(" = Module._malloc(").append(bufVar).append(".byteLength);").softNewLine();
            writer.append("Module.HEAPU8.set(new Uint8Array(").append(bufVar).append(".buffer, ")
                    .append(bufVar).append(".byteOffset, ").append(bufVar).append(".byteLength), ")
                    .append(ptrVar).append(");").softNewLine();
            writer.outdent().append("}").softNewLine();
        }

        boolean needsTryFinally = !stringParamIndices.isEmpty() || !arrayParamIndices.isEmpty();

        if (needsTryFinally) {
            writer.append("try {").indent().softNewLine();
        }

        // Emit: [var _r = ] [BigInt](Module._Java_...(0, 0, p1, p2, ...));
        if (hasReturn) {
            if (needsTryFinally) {
                writer.append("var _r = ");
            } else {
                writer.append("return ");
            }
        }
        // With WASM_BIGINT enabled, i64 (jlong) params are passed/returned as native BigInt.
        // TeaVM already represents Java long as BigInt, so no conversion needed for long params.
        // env (JNIEnv*) and obj (jclass/jobject) are i32 pointers, passed as 0.
        writer.append("Module._").append(jniName).append("(0,").ws().append("0");

        // Parameters: jstring → i32 (WASM pointer), jlong → i64 (BigInt),
        // primitive array → i32 (WASM pointer), others → i32
        for (int i = 0; i < paramCount; i++) {
            writer.append(",").ws();
            if (stringParamIndices.contains(i)) {
                // Pass the WASM pointer (i32) instead of the JS string
                writer.append("_s" + i);
            } else if (arrayParamIndices.contains(i)) {
                // Pass the WASM pointer (i32) instead of the array object
                writer.append("_a" + i);
            } else {
                writer.append(context.getParameterName(i + 1));
            }
        }
        writer.append(")");
        writer.append(";").softNewLine();

        if (needsTryFinally) {
            // Copy modified array data back from WASM memory into Java arrays
            // (matches JNI ReleasePrimitiveArrayCritical semantics with mode 0)
            for (int idx : arrayParamIndices) {
                String paramName = context.getParameterName(idx + 1);
                String ptrVar = "_a" + idx;
                String bufVar = "_buf" + idx;
                writer.append("if (").append(ptrVar).append(") {").indent().softNewLine();
                writer.append(bufVar).append(".set(new ").append(bufVar).append(".constructor(")
                        .append("Module.HEAPU8.buffer, ").append(ptrVar).append(", ")
                        .append(bufVar).append(".length));").softNewLine();
                writer.outdent().append("}").softNewLine();
            }
            if (hasReturn) {
                writer.append("return _r;").softNewLine();
            }
            writer.outdent().append("} finally {").indent().softNewLine();
            // Free allocated strings
            for (int idx : stringParamIndices) {
                String ptrVar = "_s" + idx;
                writer.append("if (").append(ptrVar).append(") Module._free(").append(ptrVar).append(");").softNewLine();
            }
            // Free allocated arrays
            for (int idx : arrayParamIndices) {
                String ptrVar = "_a" + idx;
                writer.append("if (").append(ptrVar).append(") Module._free(").append(ptrVar).append(");").softNewLine();
            }
            writer.outdent().append("}").softNewLine();
        }
    }

    private static boolean isLongType (ValueType type) {
        if (type instanceof ValueType.Primitive) {
            return ((ValueType.Primitive) type).getKind() == PrimitiveType.LONG;
        }
        return false;
    }

    private static boolean isStringType (ValueType type) {
        if (type instanceof ValueType.Object) {
            return "java.lang.String".equals(((ValueType.Object) type).getClassName());
        }
        return false;
    }

    private static boolean isPrimitiveArrayType (ValueType type) {
        if (type instanceof ValueType.Array) {
            ValueType itemType = ((ValueType.Array) type).getItemType();
            return itemType instanceof ValueType.Primitive;
        }
        return false;
    }
}
