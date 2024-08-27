package database.innodb.bufferpool.pageReplacementStrategies;

import database.innodb.bufferpool.PageReplacementStrategy;
import java.util.HashMap;
import java.util.Map;

public class LFUStrategy<K, V> implements PageReplacementStrategy<K, V> {

    private final int capacity;
    private final Map<K, V> cache;
    private final Map<K, Integer> usageCount;

    public LFUStrategy(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.usageCount = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() >= capacity) {
            K leastUsedKey = findLeastUsedKey();
            cache.remove(leastUsedKey);
            usageCount.remove(leastUsedKey);
        }
        cache.put(key, value);
        usageCount.put(key, 1);
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            usageCount.put(key, usageCount.get(key) + 1);
            return cache.get(key);
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    private K findLeastUsedKey() {
        return usageCount.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }
}
