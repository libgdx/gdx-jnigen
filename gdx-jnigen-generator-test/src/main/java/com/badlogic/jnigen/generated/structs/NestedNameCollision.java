package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.jnigen.generated.structs.NestedNameCollision;
import com.badlogic.gdx.jnigen.runtime.pointer.Union;
import com.badlogic.gdx.jnigen.runtime.closure.ClosureObject;
import com.badlogic.jnigen.generated.TestData_Internal.NestedNameCollision_Internal;
import com.badlogic.gdx.jnigen.runtime.closure.Closure;

/**
 * Same-named anonymous structs under different parents (SDL_GamepadBinding shape)
 */
public final class NestedNameCollision extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(36).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public NestedNameCollision(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public NestedNameCollision(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public NestedNameCollision() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public NestedNameCollision.NestedNameCollisionPointer asPointer() {
        return new NestedNameCollision.NestedNameCollisionPointer(getPointer(), false, 1, this);
    }

    public void asPointer(NestedNameCollision.NestedNameCollisionPointer ptr) {
        ptr.setPointer(this);
    }

    public NestedNameCollision.input input() {
        return new NestedNameCollision.input(getPointer(), false);
    }

    public void input(NestedNameCollision.input toSetPtr) {
        toSetPtr.setPointer(getPointer(), CHandler.IS_64_BIT ? 16 : 12, this);
    }

    public NestedNameCollision.input getInput() {
        return new NestedNameCollision.input(getBufPtr().duplicate(0, CHandler.IS_64_BIT ? 16 : 12), true);
    }

    public void getInput(NestedNameCollision.input toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, CHandler.IS_64_BIT ? 16 : 12);
    }

    public void setInput(NestedNameCollision.input toCopyFrom) {
        getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, CHandler.IS_64_BIT ? 16 : 12);
    }

    public NestedNameCollision.output output() {
        return new NestedNameCollision.output(getPointer() + (CHandler.IS_64_BIT ? 16 : 12), false);
    }

    public void output(NestedNameCollision.output toSetPtr) {
        toSetPtr.setPointer(getPointer() + (CHandler.IS_64_BIT ? 16 : 12), CHandler.IS_64_BIT ? 16 : 12, this);
    }

    public NestedNameCollision.output getOutput() {
        return new NestedNameCollision.output(getBufPtr().duplicate(CHandler.IS_64_BIT ? 16 : 12, CHandler.IS_64_BIT ? 16 : 12), true);
    }

    public void getOutput(NestedNameCollision.output toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), CHandler.IS_64_BIT ? 16 : 12, CHandler.IS_64_BIT ? 16 : 12);
    }

    public void setOutput(NestedNameCollision.output toCopyFrom) {
        getBufPtr().copyFrom(CHandler.IS_64_BIT ? 16 : 12, toCopyFrom.getBufPtr(), 0, CHandler.IS_64_BIT ? 16 : 12);
    }

    public static final class NestedNameCollisionPointer extends StackElementPointer<NestedNameCollision> {

        public NestedNameCollisionPointer(VoidPointer pointer) {
            super(pointer);
        }

        public NestedNameCollisionPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public NestedNameCollisionPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public NestedNameCollisionPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public NestedNameCollisionPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public NestedNameCollisionPointer() {
            this(1, true);
        }

        public NestedNameCollisionPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected NestedNameCollision createStackElement(long ptr, boolean freeOnGC) {
            return new NestedNameCollision(ptr, freeOnGC);
        }
    }

    public final static class input extends Union {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(37).getFfiType();
            __size = CHandler.getSizeFromFFIType(__ffi_type);
        }

        public input(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public input(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public input() {
            super(__size);
        }

        public long getSize() {
            return __size;
        }

        public long getFFIType() {
            return __ffi_type;
        }

        public input.inputPointer asPointer() {
            return new input.inputPointer(getPointer(), false, 1, this);
        }

        public void asPointer(input.inputPointer ptr) {
            ptr.setPointer(this);
        }

        public int button() {
            return getBufPtr().getInt(0);
        }

        public void button(int button) {
            getBufPtr().setInt(0, button);
        }

        public NestedNameCollision.input.axis axis() {
            return new NestedNameCollision.input.axis(getPointer(), false);
        }

        public void axis(NestedNameCollision.input.axis toSetPtr) {
            toSetPtr.setPointer(getPointer(), CHandler.IS_64_BIT ? 16 : 12, this);
        }

        public NestedNameCollision.input.axis getAxis() {
            return new NestedNameCollision.input.axis(getBufPtr().duplicate(0, CHandler.IS_64_BIT ? 16 : 12), true);
        }

        public void getAxis(NestedNameCollision.input.axis toCopyTo) {
            toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, CHandler.IS_64_BIT ? 16 : 12);
        }

        public void setAxis(NestedNameCollision.input.axis toCopyFrom) {
            getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, CHandler.IS_64_BIT ? 16 : 12);
        }

        public static final class inputPointer extends StackElementPointer<input> {

            public inputPointer(VoidPointer pointer) {
                super(pointer);
            }

            public inputPointer(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public inputPointer(long pointer, boolean freeOnGC, int capacity) {
                super(pointer, freeOnGC, capacity * __size);
            }

            public inputPointer(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public inputPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
                super(pointer, freeOnGC, capacity * __size);
                setParent(parent);
            }

            public inputPointer() {
                this(1, true);
            }

            public inputPointer(int count, boolean freeOnGC) {
                super(__size, count, freeOnGC);
            }

            public int getSize() {
                return __size;
            }

            protected input createStackElement(long ptr, boolean freeOnGC) {
                return new input(ptr, freeOnGC);
            }
        }

        public final static class axis extends Struct {

            private final static int __size;

            private final static long __ffi_type;

            static {
                __ffi_type = FFITypes.getCTypeInfo(38).getFfiType();
                __size = CHandler.getSizeFromFFIType(__ffi_type);
            }

            public axis(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public axis(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public axis() {
                super(__size);
            }

            public long getSize() {
                return __size;
            }

            public long getFFIType() {
                return __ffi_type;
            }

            public axis.axisPointer asPointer() {
                return new axis.axisPointer(getPointer(), false, 1, this);
            }

            public void asPointer(axis.axisPointer ptr) {
                ptr.setPointer(this);
            }

            public int axis() {
                return getBufPtr().getInt(0);
            }

            public void axis(int axis) {
                getBufPtr().setInt(0, axis);
            }

            public int axis_min() {
                return getBufPtr().getInt(4);
            }

            public void axis_min(int axis_min) {
                getBufPtr().setInt(4, axis_min);
            }

            public ClosureObject<NestedNameCollision.input.axis.cb> cb() {
                return CHandler.getClosureObject(getBufPtr().getNativePointer(8), NestedNameCollision_Internal.input_Internal.axis_Internal.cb_Internal::cb_downcall);
            }

            public void cb(ClosureObject<NestedNameCollision.input.axis.cb> cb) {
                getBufPtr().setNativePointer(8, cb.getPointer());
            }

            public static final class axisPointer extends StackElementPointer<axis> {

                public axisPointer(VoidPointer pointer) {
                    super(pointer);
                }

                public axisPointer(long pointer, boolean freeOnGC) {
                    super(pointer, freeOnGC);
                }

                public axisPointer(long pointer, boolean freeOnGC, int capacity) {
                    super(pointer, freeOnGC, capacity * __size);
                }

                public axisPointer(long pointer, boolean freeOnGC, Pointing parent) {
                    super(pointer, freeOnGC);
                    setParent(parent);
                }

                public axisPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
                    super(pointer, freeOnGC, capacity * __size);
                    setParent(parent);
                }

                public axisPointer() {
                    this(1, true);
                }

                public axisPointer(int count, boolean freeOnGC) {
                    super(__size, count, freeOnGC);
                }

                public int getSize() {
                    return __size;
                }

                protected axis createStackElement(long ptr, boolean freeOnGC) {
                    return new axis(ptr, freeOnGC);
                }
            }

            public interface cb extends Closure, NestedNameCollision_Internal.input_Internal.axis_Internal.cb_Internal {

                int cb_call(int arg0);
            }
        }
    }

    public final static class output extends Union {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(39).getFfiType();
            __size = CHandler.getSizeFromFFIType(__ffi_type);
        }

        public output(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public output(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public output() {
            super(__size);
        }

        public long getSize() {
            return __size;
        }

        public long getFFIType() {
            return __ffi_type;
        }

        public output.outputPointer asPointer() {
            return new output.outputPointer(getPointer(), false, 1, this);
        }

        public void asPointer(output.outputPointer ptr) {
            ptr.setPointer(this);
        }

        public int button() {
            return getBufPtr().getInt(0);
        }

        public void button(int button) {
            getBufPtr().setInt(0, button);
        }

        public NestedNameCollision.output.axis axis() {
            return new NestedNameCollision.output.axis(getPointer(), false);
        }

        public void axis(NestedNameCollision.output.axis toSetPtr) {
            toSetPtr.setPointer(getPointer(), CHandler.IS_64_BIT ? 16 : 12, this);
        }

        public NestedNameCollision.output.axis getAxis() {
            return new NestedNameCollision.output.axis(getBufPtr().duplicate(0, CHandler.IS_64_BIT ? 16 : 12), true);
        }

        public void getAxis(NestedNameCollision.output.axis toCopyTo) {
            toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, CHandler.IS_64_BIT ? 16 : 12);
        }

        public void setAxis(NestedNameCollision.output.axis toCopyFrom) {
            getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, CHandler.IS_64_BIT ? 16 : 12);
        }

        public static final class outputPointer extends StackElementPointer<output> {

            public outputPointer(VoidPointer pointer) {
                super(pointer);
            }

            public outputPointer(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public outputPointer(long pointer, boolean freeOnGC, int capacity) {
                super(pointer, freeOnGC, capacity * __size);
            }

            public outputPointer(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public outputPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
                super(pointer, freeOnGC, capacity * __size);
                setParent(parent);
            }

            public outputPointer() {
                this(1, true);
            }

            public outputPointer(int count, boolean freeOnGC) {
                super(__size, count, freeOnGC);
            }

            public int getSize() {
                return __size;
            }

            protected output createStackElement(long ptr, boolean freeOnGC) {
                return new output(ptr, freeOnGC);
            }
        }

        public final static class axis extends Struct {

            private final static int __size;

            private final static long __ffi_type;

            static {
                __ffi_type = FFITypes.getCTypeInfo(40).getFfiType();
                __size = CHandler.getSizeFromFFIType(__ffi_type);
            }

            public axis(long pointer, boolean freeOnGC) {
                super(pointer, freeOnGC);
            }

            public axis(long pointer, boolean freeOnGC, Pointing parent) {
                super(pointer, freeOnGC);
                setParent(parent);
            }

            public axis() {
                super(__size);
            }

            public long getSize() {
                return __size;
            }

            public long getFFIType() {
                return __ffi_type;
            }

            public axis.axisPointer asPointer() {
                return new axis.axisPointer(getPointer(), false, 1, this);
            }

            public void asPointer(axis.axisPointer ptr) {
                ptr.setPointer(this);
            }

            public int axis() {
                return getBufPtr().getInt(0);
            }

            public void axis(int axis) {
                getBufPtr().setInt(0, axis);
            }

            public int axis_min() {
                return getBufPtr().getInt(4);
            }

            public void axis_min(int axis_min) {
                getBufPtr().setInt(4, axis_min);
            }

            public ClosureObject<NestedNameCollision.output.axis.cb> cb() {
                return CHandler.getClosureObject(getBufPtr().getNativePointer(8), NestedNameCollision_Internal.output_Internal.axis_Internal.cb_Internal::cb_downcall);
            }

            public void cb(ClosureObject<NestedNameCollision.output.axis.cb> cb) {
                getBufPtr().setNativePointer(8, cb.getPointer());
            }

            public static final class axisPointer extends StackElementPointer<axis> {

                public axisPointer(VoidPointer pointer) {
                    super(pointer);
                }

                public axisPointer(long pointer, boolean freeOnGC) {
                    super(pointer, freeOnGC);
                }

                public axisPointer(long pointer, boolean freeOnGC, int capacity) {
                    super(pointer, freeOnGC, capacity * __size);
                }

                public axisPointer(long pointer, boolean freeOnGC, Pointing parent) {
                    super(pointer, freeOnGC);
                    setParent(parent);
                }

                public axisPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
                    super(pointer, freeOnGC, capacity * __size);
                    setParent(parent);
                }

                public axisPointer() {
                    this(1, true);
                }

                public axisPointer(int count, boolean freeOnGC) {
                    super(__size, count, freeOnGC);
                }

                public int getSize() {
                    return __size;
                }

                protected axis createStackElement(long ptr, boolean freeOnGC) {
                    return new axis(ptr, freeOnGC);
                }
            }

            public interface cb extends Closure, NestedNameCollision_Internal.output_Internal.axis_Internal.cb_Internal {

                int cb_call(int arg0);
            }
        }
    }
}
