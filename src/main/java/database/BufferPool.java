package database;

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

    public void modifyPage(PageId pageId) {
        Page page = getPage(pageId);
        /*
          ..modify..
         */
        page.setDirty(true);
        page.unPin();
    }

    /**
     * 플러시 리스트 플러시
     */
    public void flush() {
        for (Map.Entry<PageId, Page> entry : bufferPool.entrySet()) {
            Page page = entry.getValue();

            if (!page.isPinned() && page.isDirty()) {
                pageManager.savePage(entry.getKey(), page);
                page.setDirty(false);
            }
        }
    }

    public Map<PageId, Page> getBufferPool() {
        return bufferPool;
    }

    public PageManager getPageManager() {
        return pageManager;
    }
}
