package database.innodb.bufferpool;

import database.innodb.page.Page;
import database.innodb.page.Record;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class BufferPool {

    private final int capacity;
    private final LinkedHashMap<Long, Page> cache;

    public BufferPool(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            // TODO: 전략 패턴 도입
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

    public Optional<Page> findPageWithSpace(Record record) {
        return IntStream.range(0, capacity)
                .filter(this::containsPage)
                .mapToObj(this::getPage)
                .filter(page -> page.getFreeSpace() >= record.getSize())
                .findFirst()
                .or(Optional::empty);
    }
}
