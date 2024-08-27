package database.innodb.bufferpool;

import database.innodb.page.Page;
import database.innodb.page.StorageRecord;
import java.util.Optional;
import java.util.stream.IntStream;

public class BufferPool {

    private final int capacity;
    private final PageReplacementStrategy<Long, Page> cache;

    public BufferPool(int capacity, PageReplacementStrategy<Long, Page> cacheStrategy) {
        this.capacity = capacity;
        this.cache = cacheStrategy;
    }

    public void putPage(Page page) {
        cache.put(page.getPageNumber(), page);
    }

    public Optional<Page> findPageWithSpace(StorageRecord storageRecord) {
        return IntStream.range(0, capacity)
                .filter(this::containsPage)
                .mapToObj(this::getPage)
                .filter(page -> page.getFreeSpace() >= storageRecord.getSize())
                .findFirst()
                .or(Optional::empty);
    }

    public boolean containsPage(long pageNum) {
        return cache.containsKey(pageNum);
    }

    public Page getPage(long pageNum) {
        return cache.get(pageNum);
    }
}
