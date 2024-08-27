package database.storageEngine.bufferpool;

import database.storageEngine.page.Page;
import database.storageEngine.page.PageManager;
import database.storageEngine.page.StorageRecord;
import java.util.stream.IntStream;

public class BufferPool {

    private final int capacity;
    private final PageReplacementStrategy<Long, Page> cache;
    private final PageManager pageManager;

    public BufferPool(int capacity, PageReplacementStrategy<Long, Page> cacheStrategy) {
        this.capacity = capacity;
        this.cache = cacheStrategy;
        this.pageManager = new PageManager();
    }

    public void putPage(Page page) {
        cache.put(page.getPageNumber(), page);
    }

    public Page findPageWithSpace(StorageRecord storageRecord) {
        return IntStream.range(0, capacity)
                .filter(this::containsPage)
                .mapToObj(this::getPage)
                .filter(page -> page.getFreeSpace() >= storageRecord.getSize())
                .findFirst()
                .orElseGet(pageManager::createNewDataPage);
    }

    public boolean containsPage(long pageNum) {
        return cache.containsKey(pageNum);
    }

    public Page getPage(long pageNum) {
        return cache.get(pageNum);
    }
}
