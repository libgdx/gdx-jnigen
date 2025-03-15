package com.badlogic.gdx.jnigen.runtime.gc;


public final class ReferenceList {

    private ReferenceListNode head;
    private ReferenceListNode poolCache;
    private long size;

    public ReferenceList() {
        this.head = new ReferenceListNode();
    }

    public synchronized void insertReference(PointingPhantomReference reference) {
        if (head.size >= ReferenceListNode.NODE_SIZE) {
            if (poolCache != null) {
                poolCache.next = head;
                head = poolCache;
                poolCache = null;
            } else {
                ReferenceListNode newHead = new ReferenceListNode();
                newHead.next = head;
                head = newHead;
            }
        }

        head.addNode(reference);
        size++;
    }

    public synchronized void removeReference(PointingPhantomReference reference) {
        PointingPhantomReference headLastNode = head.getLastNode();
        if (headLastNode != reference) {
            reference.getNode().setNode(headLastNode, reference.getPosition());
        }

        head.removeLastNode();

        if (head.size == 0 && head.next != null) {
            if (poolCache == null) {
                poolCache = head;
            }
            head = head.next;
        }
        size--;
    }

    public long getSize() {
        return size;
    }

    public final static class ReferenceListNode {
        private static final int NODE_SIZE = 4096;

        private final PointingPhantomReference[] elements = new PointingPhantomReference[NODE_SIZE];
        private int size = 0;
        private ReferenceListNode next = null;

        public void addNode(PointingPhantomReference reference) {
            reference.setNode(this);
            reference.setPosition(size);
            elements[size] = reference;
            size++;
        }

        public void setNode(PointingPhantomReference reference, int position) {
            reference.setPosition(position);
            reference.setNode(this);
            elements[position] = reference;
        }

        public PointingPhantomReference getLastNode() {
            return elements[size - 1];
        }

        public void removeLastNode() {
            elements[size - 1] = null;
            size--;
        }
    }
}
