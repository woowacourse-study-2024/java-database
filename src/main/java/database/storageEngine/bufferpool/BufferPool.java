package database.storageEngine.bufferpool;

import database.storageEngine.page.Page;
import database.storageEngine.page.PageManager;
import database.storageEngine.page.StorageRecord;
import java.util.stream.IntStream;

public class BufferPool {

    private final int capacity;
    private final PageReplacementStrategy<TablePageKey, Page> cache;
    private final PageManager pageManager;

    public BufferPool(int capacity, PageReplacementStrategy<TablePageKey, Page> cacheStrategy) {
        this.capacity = capacity;
        this.cache = cacheStrategy;
        this.pageManager = new PageManager();
    }

    public void putPage(String tableName, Page page) {
        cache.put(new TablePageKey(tableName, page.getPageNumber()), page);
    }

    public Page findPageWithSpace(String tableName, StorageRecord storageRecord) {
        return IntStream.range(0, capacity)
                .filter(i -> containsPage(tableName, i))
                .mapToObj(i -> getPage(tableName, i))
                .filter(page -> page.getFreeSpace() >= storageRecord.getSize())
                .findFirst()
                .orElseGet(pageManager::createNewDataPage);
    }

    public boolean containsPage(String tableName, long pageNum) {
        return cache.containsKey(new TablePageKey(tableName, pageNum));
    }

    public Page getPage(String tableName, long pageNum) {
        return cache.get(new TablePageKey(tableName, pageNum));
    }
}
