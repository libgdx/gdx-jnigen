package com.badlogic.jnigen.generated.structs;

import com.badlogic.gdx.jnigen.runtime.CHandler;
import com.badlogic.gdx.jnigen.runtime.pointer.Struct;
import com.badlogic.gdx.jnigen.runtime.pointer.StackElementPointer;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;
import com.badlogic.gdx.jnigen.runtime.pointer.VoidPointer;
import com.badlogic.jnigen.generated.FFITypes;
import com.badlogic.jnigen.generated.structs.NestedNameCollisionArray;

public final class NestedNameCollisionArray extends Struct {

    private final static int __size;

    private final static long __ffi_type;

    static {
        __ffi_type = FFITypes.getCTypeInfo(41).getFfiType();
        __size = CHandler.getSizeFromFFIType(__ffi_type);
    }

    public NestedNameCollisionArray(long pointer, boolean freeOnGC) {
        super(pointer, freeOnGC);
    }

    public NestedNameCollisionArray(long pointer, boolean freeOnGC, Pointing parent) {
        super(pointer, freeOnGC);
        setParent(parent);
    }

    public NestedNameCollisionArray() {
        super(__size);
    }

    public long getSize() {
        return __size;
    }

    public long getFFIType() {
        return __ffi_type;
    }

    public NestedNameCollisionArray.NestedNameCollisionArrayPointer asPointer() {
        return new NestedNameCollisionArray.NestedNameCollisionArrayPointer(getPointer(), false, 1, this);
    }

    public void asPointer(NestedNameCollisionArray.NestedNameCollisionArrayPointer ptr) {
        ptr.setPointer(this);
    }

    public NestedNameCollisionArray.input input() {
        return new NestedNameCollisionArray.input(getPointer(), false);
    }

    public void input(NestedNameCollisionArray.input toSetPtr) {
        toSetPtr.setPointer(getPointer(), 8, this);
    }

    public NestedNameCollisionArray.input getInput() {
        return new NestedNameCollisionArray.input(getBufPtr().duplicate(0, 8), true);
    }

    public void getInput(NestedNameCollisionArray.input toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, 8);
    }

    public void setInput(NestedNameCollisionArray.input toCopyFrom) {
        getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, 8);
    }

    public NestedNameCollisionArray.output output() {
        return new NestedNameCollisionArray.output(getPointer() + (8), false);
    }

    public void output(NestedNameCollisionArray.output toSetPtr) {
        toSetPtr.setPointer(getPointer() + (8), 8, this);
    }

    public NestedNameCollisionArray.output getOutput() {
        return new NestedNameCollisionArray.output(getBufPtr().duplicate(8, 8), true);
    }

    public void getOutput(NestedNameCollisionArray.output toCopyTo) {
        toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 8, 8);
    }

    public void setOutput(NestedNameCollisionArray.output toCopyFrom) {
        getBufPtr().copyFrom(8, toCopyFrom.getBufPtr(), 0, 8);
    }

    public static final class NestedNameCollisionArrayPointer extends StackElementPointer<NestedNameCollisionArray> {

        public NestedNameCollisionArrayPointer(VoidPointer pointer) {
            super(pointer);
        }

        public NestedNameCollisionArrayPointer(long pointer, boolean freeOnGC) {
            super(pointer, freeOnGC);
        }

        public NestedNameCollisionArrayPointer(long pointer, boolean freeOnGC, int capacity) {
            super(pointer, freeOnGC, capacity * __size);
        }

        public NestedNameCollisionArrayPointer(long pointer, boolean freeOnGC, Pointing parent) {
            super(pointer, freeOnGC);
            setParent(parent);
        }

        public NestedNameCollisionArrayPointer(long pointer, boolean freeOnGC, int capacity, Pointing parent) {
            super(pointer, freeOnGC, capacity * __size);
            setParent(parent);
        }

        public NestedNameCollisionArrayPointer() {
            this(1, true);
        }

        public NestedNameCollisionArrayPointer(int count, boolean freeOnGC) {
            super(__size, count, freeOnGC);
        }

        public int getSize() {
            return __size;
        }

        protected NestedNameCollisionArray createStackElement(long ptr, boolean freeOnGC) {
            return new NestedNameCollisionArray(ptr, freeOnGC);
        }
    }

    public final static class input extends Struct {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(42).getFfiType();
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

        public NestedNameCollisionArray.input.axis.axisPointer axis() {
            return new NestedNameCollisionArray.input.axis.axisPointer(getPointer(), false, 2);
        }

        public void axis(NestedNameCollisionArray.input.axis.axisPointer toSetPtr) {
            toSetPtr.setPointer(getPointer(), 8, this);
        }

        public NestedNameCollisionArray.input.axis.axisPointer getAxis() {
            return new NestedNameCollisionArray.input.axis.axisPointer(getBufPtr().duplicate(0, 8), false, 2);
        }

        public void getAxis(NestedNameCollisionArray.input.axis.axisPointer toCopyTo) {
            toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, 8);
        }

        public void setAxis(NestedNameCollisionArray.input.axis.axisPointer toCopyFrom) {
            getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, 8);
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
                __ffi_type = FFITypes.getCTypeInfo(43).getFfiType();
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

            public int v() {
                return getBufPtr().getInt(0);
            }

            public void v(int v) {
                getBufPtr().setInt(0, v);
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
        }
    }

    public final static class output extends Struct {

        private final static int __size;

        private final static long __ffi_type;

        static {
            __ffi_type = FFITypes.getCTypeInfo(44).getFfiType();
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

        public NestedNameCollisionArray.output.axis.axisPointer axis() {
            return new NestedNameCollisionArray.output.axis.axisPointer(getPointer(), false, 2);
        }

        public void axis(NestedNameCollisionArray.output.axis.axisPointer toSetPtr) {
            toSetPtr.setPointer(getPointer(), 8, this);
        }

        public NestedNameCollisionArray.output.axis.axisPointer getAxis() {
            return new NestedNameCollisionArray.output.axis.axisPointer(getBufPtr().duplicate(0, 8), false, 2);
        }

        public void getAxis(NestedNameCollisionArray.output.axis.axisPointer toCopyTo) {
            toCopyTo.getBufPtr().copyFrom(0, getBufPtr(), 0, 8);
        }

        public void setAxis(NestedNameCollisionArray.output.axis.axisPointer toCopyFrom) {
            getBufPtr().copyFrom(0, toCopyFrom.getBufPtr(), 0, 8);
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
                __ffi_type = FFITypes.getCTypeInfo(45).getFfiType();
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

            public int v() {
                return getBufPtr().getInt(0);
            }

            public void v(int v) {
                getBufPtr().setInt(0, v);
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
        }
    }
}
