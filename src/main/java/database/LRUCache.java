package database;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int bufferSize;
    private final PageManager pageManager;

    public LRUCache(int initialCapacity, float loadFactor, boolean accessOrder, PageManager pageManager) {
        super(initialCapacity, loadFactor, accessOrder);
        this.pageManager = pageManager;
        this.bufferSize = initialCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        boolean isBufferPoolOverCapacity = size() > bufferSize;
        boolean isUnpinned = !((Page) eldest.getValue()).isPinned();

        if (isBufferPoolOverCapacity && isUnpinned) {
            flushIfDirty((PageId) eldest.getKey(), (Page) eldest.getValue());
            return true;
        }
        return false;
    }

    private void flushIfDirty(PageId pageId, Page page) {
        if (page.isDirty()) {
            pageManager.savePage(pageId, page);
        }
    }
}
