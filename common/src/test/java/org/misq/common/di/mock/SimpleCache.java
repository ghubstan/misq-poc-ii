package org.misq.common.di.mock;

import org.misq.common.di.annotation.Singleton;

import java.util.Map;
import java.util.TreeMap;

import static java.lang.System.identityHashCode;

@Singleton
public class SimpleCache {

    private final Map<String, Object> cache = new TreeMap<>();

    // @Singleton
    public SimpleCache() {
    }

    public void addToCache(String key, Object value) {
        cache.put(key, value);  // Can't use cache.put( new AbstractMap.SimpleEntry<>(key, value) )
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public Integer numCacheEntries() {
        return cache.size();
    }

    @Override
    public String toString() {
        return "SimpleCache{" +
                "id=" + identityHashCode(this) +
                ", cache=" + cache +
                ", cache-id=" + identityHashCode(cache) +
                '}';
    }
}