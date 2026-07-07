package com.badlogic.gdx.jnigen.teavm;

import org.teavm.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TeaVM class transformer that automatically provides JavaScript implementations
 * for JNI native methods by routing them to Emscripten-exported functions.
 * <p>
 * For each native method found in user/library classes (not platform classes),
 * this transformer adds a {@code @GeneratedBy(JniMethodGenerator.class)} annotation.
 * At code generation time, {@link JniMethodGenerator} emits JavaScript that calls
 * {@code Module._Java_package_Class_method(0, 0, args...)}.
 * <p>
 * Special handling:
 * <ul>
 *   <li>{@code SharedLibraryLoader.load()} — made into a no-op</li>
 * </ul>
 */
public class CHandlerTransformer implements ClassHolderTransformer {

    private static final String JNIGEN_SHARED_LIB_LOADER = "com.badlogic.gdx.jnigen.loader.SharedLibraryLoader";
    private static final String LIBGDX_SHARED_LIB_LOADER = "com.badlogic.gdx.utils.SharedLibraryLoader";
    private static final String GENERATED_BY_ANNOTATION = "org.teavm.backend.javascript.spi.GeneratedBy";
    private static final String JSBODY_ANNOTATION = "org.teavm.jso.JSBody";

    /** Packages whose native methods are provided by the platform/runtime, not JNI. */
    private static final String[] SKIP_PREFIXES = {
            "java.", "javax.", "sun.", "jdk.", "com.sun.",
            "org.teavm.", "org.w3c.", "org.xml.",
            "com.badlogic.gdx.jnigen.teavm.",
            "com.badlogic.gdx.jnigen.runtime."
    };

    @Override
    public void transformClass (ClassHolder cls, ClassHolderTransformerContext context) {
        String className = cls.getName();

        // Make SharedLibraryLoader.load() a no-op — on web, WASM modules are
        // loaded via <script> tags before the app starts, not at runtime.
        // Handle both jnigen's and libGDX's SharedLibraryLoader.
        if (className.equals(JNIGEN_SHARED_LIB_LOADER) || className.equals(LIBGDX_SHARED_LIB_LOADER)) {
            makeLoadNoOp(cls);
            return;
        }

        // Skip platform/runtime classes — their native methods are handled by TeaVM itself
        if (shouldSkip(className)) {
            return;
        }

        transformNativeMethods(cls);
    }

    private boolean shouldSkip (String className) {
        for (String prefix : SKIP_PREFIXES) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private void makeLoadNoOp (ClassHolder cls) {
        for (MethodHolder method : cls.getMethods()) {
            if (method.getName().equals("load")) {
                makeVoidNoOp(method);
            }
        }
    }

    private void makeVoidNoOp (MethodHolder method) {
        method.getModifiers().remove(ElementModifier.NATIVE);
        Program program = new Program();
        BasicBlock block = program.createBasicBlock();

        int varCount = method.parameterCount() + (method.hasModifier(ElementModifier.STATIC) ? 0 : 1);
        for (int i = 0; i < varCount; i++) {
            program.createVariable();
        }

        block.add(new org.teavm.model.instructions.ExitInstruction());
        method.setProgram(program);
    }

    private void transformNativeMethods (ClassHolder cls) {
        for (MethodHolder method : cls.getMethods()) {
            if (method.hasModifier(ElementModifier.NATIVE)
                    && method.getAnnotations().get(JSBODY_ANNOTATION) == null
                    && method.getAnnotations().get(GENERATED_BY_ANNOTATION) == null) {
                addGeneratedByAnnotation(method);
            }
        }
    }

    /**
     * Add @GeneratedBy(JniMethodGenerator.class) to a native method.
     * The method stays native — JniMethodGenerator will emit the JavaScript
     * implementation during code generation.
     */
    private void addGeneratedByAnnotation (MethodHolder method) {
        AnnotationHolder generatedBy = new AnnotationHolder(GENERATED_BY_ANNOTATION);
        generatedBy.getValues().put("value",
                new AnnotationValue(ValueType.object(JniMethodGenerator.class.getName())));
        method.getAnnotations().add(generatedBy);
    }

    // ---- JNI Name Mangling (package-private for use by JniMethodGenerator) ----

    /**
     * Compute the JNI-mangled export function name for a native method.
     * <p>
     * For non-overloaded methods: {@code Java_package_Class_method}
     * For overloaded methods: {@code Java_package_Class_method__signature}
     */
    static String computeJniName (String className, String methodName,
                                   MethodDescriptor descriptor, boolean isOverloaded) {
        StringBuilder sb = new StringBuilder("Java_");
        // Mangle each package/class component individually, then join with '_'.
        // The '_' separators (from '.') must NOT be mangled — only original '_'
        // within identifiers get replaced with '_1'.
        String[] parts = className.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append('_');
            sb.append(mangleJniComponent(parts[i]));
        }
        sb.append('_');
        sb.append(mangleJniComponent(methodName));

        if (isOverloaded) {
            sb.append("__");
            for (int i = 0; i < descriptor.parameterCount(); i++) {
                appendJniTypeSignature(sb, descriptor.parameterType(i));
            }
        }

        return sb.toString();
    }

    /**
     * Mangle a JNI name component: replace _ with _1, ; with _2, [ with _3
     */
    private static String mangleJniComponent (String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '_':
                    sb.append("_1");
                    break;
                case ';':
                    sb.append("_2");
                    break;
                case '[':
                    sb.append("_3");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Append the JNI type signature character(s) for a ValueType.
     */
    private static void appendJniTypeSignature (StringBuilder sb, ValueType type) {
        if (type instanceof ValueType.Primitive) {
            switch (((ValueType.Primitive) type).getKind()) {
                case BOOLEAN: sb.append('Z'); break;
                case BYTE: sb.append('B'); break;
                case CHARACTER: sb.append('C'); break;
                case SHORT: sb.append('S'); break;
                case INTEGER: sb.append('I'); break;
                case LONG: sb.append('J'); break;
                case FLOAT: sb.append('F'); break;
                case DOUBLE: sb.append('D'); break;
            }
        } else if (type instanceof ValueType.Object) {
            String name = ((ValueType.Object) type).getClassName().replace('.', '/');
            sb.append('L');
            sb.append(mangleJniComponent(name.replace('/', '_')));
            sb.append("_2"); // mangled ';'
        } else if (type instanceof ValueType.Array) {
            sb.append("_3"); // mangled '['
            appendJniTypeSignature(sb, ((ValueType.Array) type).getItemType());
        }
    }
}
