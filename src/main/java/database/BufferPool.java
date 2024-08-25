package database;

import database.page.Page;
import java.util.LinkedHashMap;
import java.util.Map;

public class BufferPool {

    private final int capacity;
    private final LinkedHashMap<Long, Page> cache;

    public BufferPool(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<Long, Page> eldest) {
                return size() > BufferPool.this.capacity;
            }
        };
    }

    public Page getPage(long pageNum) {
        return cache.get(pageNum);
    }

    public void putPage(Page page) {
        cache.put(page.getPageNumber(), page);
    }

    public boolean containsPage(long pageNum) {
        return cache.containsKey(pageNum);
    }
}
