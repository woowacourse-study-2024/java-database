package database.storageEngine.bufferpool;

public interface PageReplacementStrategy<K, V> {

    void put(K key, V value);

    V get(K key);

    boolean containsKey(K key);
}
