package database.storageEngine.bufferpool.pageReplacementStrategies;

import database.storageEngine.bufferpool.PageReplacementStrategy;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUStrategy<K, V> implements PageReplacementStrategy<K, V> {

    private final LinkedHashMap<K, V> cache;

    public LRUStrategy(int capacity) {
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}

