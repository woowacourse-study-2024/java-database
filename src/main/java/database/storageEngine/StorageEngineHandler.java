package database.storageEngine;

import database.engine.Handler;
import database.engine.Record;
import database.storageEngine.bufferpool.BufferPool;
import database.storageEngine.bufferpool.PageReplacementStrategy;
import database.storageEngine.bufferpool.pageReplacementStrategies.LRUStrategy;
import database.storageEngine.page.Page;
import database.storageEngine.page.PageFactory;
import database.storageEngine.page.PageManager;
import database.storageEngine.page.StorageRecord;
import java.util.List;

public class StorageEngineHandler implements Handler {

    private static final int BUFFER_SIZE = 40;

    private final BufferPool bufferPool;
    private final PageManager pageManager;

    public StorageEngineHandler(String tableName) {
        PageReplacementStrategy<Long, Page> lruStrategy = new LRUStrategy<>(BUFFER_SIZE);
        this.bufferPool = new BufferPool(BUFFER_SIZE, lruStrategy);
        this.pageManager = new PageManager(tableName);
    }

    @Override
    public void insert(Record record) {
        StorageRecord storageRecord = new StorageRecord(record.getValues());
        Page page = bufferPool.findPageWithSpace(storageRecord)
                .orElseGet(this::createNewPage);
        page.addRecord(storageRecord);
        bufferPool.putPage(page);
    }

    private Page createNewPage() {
        long newPageNumber = pageManager.getNewPageNumber();
        return PageFactory.createDataPage(newPageNumber);
    }

    @Override
    public List<Record> search(Object key) {
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
