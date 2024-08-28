package database;

import database.page.Page;
import java.util.Map;

public class BufferPool {

    private static final int BUFFER_SIZE = 40;

    private final Map<PageId, Page> bufferPool;
    private final PageManager pageManager;

    public BufferPool(PageManager pageManager) {
        this.bufferPool = new LRUCache<>(BUFFER_SIZE, 0.75f, true, pageManager);
        this.pageManager = pageManager;
    }

    public Page getPage(PageId pageId) {
        Page page = bufferPool.computeIfAbsent(pageId, id -> pageManager.loadPage(pageId));
        page.pin();
        return page;
    }
}
