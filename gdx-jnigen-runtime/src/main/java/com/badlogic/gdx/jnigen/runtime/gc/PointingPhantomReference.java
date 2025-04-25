package com.badlogic.gdx.jnigen.runtime.gc;

import com.badlogic.gdx.jnigen.runtime.gc.ReferenceList.ReferenceListNode;
import com.badlogic.gdx.jnigen.runtime.mem.BufferPtr;
import com.badlogic.gdx.jnigen.runtime.pointer.Pointing;

import java.lang.ref.PhantomReference;

public final class PointingPhantomReference extends PhantomReference<Pointing> {

    private final BufferPtr bufferPtr;
    private ReferenceListNode node;
    private int position;


    public PointingPhantomReference(Pointing referent, BufferPtr bufferPtr) {
        super(referent, GCHandler.REFERENCE_QUEUE);
        this.bufferPtr = bufferPtr;
        if (bufferPtr == null)
            throw new IllegalArgumentException("Trying to register null pointer for GC");
    }

    public void setNode(ReferenceListNode node) {
        this.node = node;
    }

    public ReferenceListNode getNode() {
        return node;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public BufferPtr getBufferPtr() {
        return bufferPtr;
    }
}
