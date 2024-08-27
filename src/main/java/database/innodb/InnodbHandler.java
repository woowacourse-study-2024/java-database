package database.innodb;

import database.Handler;
import database.innodb.bufferpool.BufferPool;
import database.innodb.bufferpool.PageReplacementStrategy;
import database.innodb.bufferpool.pageReplacementStrategies.LRUStrategy;
import database.innodb.page.Page;
import database.innodb.page.PageFactory;
import database.innodb.page.PageManager;
import database.innodb.page.Record;

public class InnodbHandler implements Handler {

    private static final int BUFFER_SIZE = 40;

    private final BufferPool bufferPool;
    private final PageManager pageManager;

    public InnodbHandler(String tableName) {
        PageReplacementStrategy<Long, Page> lruStrategy = new LRUStrategy<>(BUFFER_SIZE);
        this.bufferPool = new BufferPool(BUFFER_SIZE, lruStrategy);
        this.pageManager = new PageManager(tableName);
    }

    @Override
    public void insert(byte[] recordData) {
        Record record = new Record(recordData);
        Page page = bufferPool.findPageWithSpace(record)
                .orElseGet(this::createNewPage);
        page.addRecord(record);
        bufferPool.putPage(page);
    }

    private Page createNewPage() {
        long newPageNumber = pageManager.getNewPageNumber();
        return PageFactory.createDataPage(newPageNumber);
    }

    @Override
    public byte[] search(Object key) {
        // 디스크에서 레코드를 검색하는 로직
        return null;
    }

    @Override
    public void update(Object key, byte[] newRecord) {
        // 디스크에서 레코드를 업데이트하는 로직
    }

    @Override
    public void delete(Object key) {
        // 디스크에서 레코드를 삭제하는 로직
    }
}
