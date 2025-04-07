package com.badlogic.gdx.jnigen.runtime.mem;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache extends LinkedHashMap<Long, BufferPtr> {

    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Entry<Long, BufferPtr> eldest) {
        return size() > capacity;
    }
}
