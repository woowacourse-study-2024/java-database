package database.storageEngine.bufferpool;

import database.storageEngine.page.Page;
import database.storageEngine.page.PageManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BufferPool {

    private final int capacity;
    private final PageReplacementStrategy<TablePageKey> strategy;
    private final Map<TablePageKey, Page> pages;
    private final PageManager pageManager;

    public BufferPool(int capacity, PageReplacementStrategy<TablePageKey> strategy) {
        this.capacity = capacity;
        this.strategy = strategy;
        this.pages = new HashMap<>();
        this.pageManager = new PageManager();
    }

    public Optional<Page> getPage(TablePageKey key) {
        if (pages.containsKey(key)) {
            strategy.get(key);
            return Optional.of(pages.get(key));
        }

        Optional<Page> optionalPage = pageManager.loadPage(key);
        if (optionalPage.isPresent()) {
            Page page = optionalPage.get();
            putPage(key, page);
            return Optional.of(page);
        }

        return Optional.empty();
    }

    public void putPage(TablePageKey key, Page page) {
        if (!pages.containsKey(key)) {
            if (pages.size() >= capacity) {
                TablePageKey evictedKey = strategy.evict();
                if (evictedKey != null) {
                    flushPage(evictedKey);
                    pages.remove(evictedKey);
                }
            }
            pages.put(key, page);
            strategy.put(key);
        }
    }

    public void flushPage(TablePageKey key) {
        if (pages.containsKey(key)) {
            Page page = pages.get(key);
            if (page.isDirty()) {
                pageManager.savePage(key.tableName(), page);
                page.clean();
            }
        }
    }

    public void flushAllPages() {
        for (Map.Entry<TablePageKey, Page> entry : pages.entrySet()) {
            flushPage(entry.getKey());
        }
    }

    public void removePage(TablePageKey key) {
        if (pages.containsKey(key)) {
            flushPage(key);
            pages.remove(key);
            strategy.evict();
        }
    }
}
