package database.storageEngine;

import database.engine.Handler;
import database.engine.Record;
import database.storageEngine.bufferpool.BufferPool;
import database.storageEngine.bufferpool.PageReplacementStrategy;
import database.storageEngine.bufferpool.TablePageKey;
import database.storageEngine.bufferpool.pageReplacementStrategies.LRUStrategy;
import database.storageEngine.page.Page;
import database.storageEngine.page.PageFactory;
import database.storageEngine.page.PageManager;
import database.storageEngine.page.StorageRecord;
import java.util.List;
import java.util.Optional;

public class StorageEngineHandler implements Handler {

    private static final int BUFFER_SIZE = 40;

    private final BufferPool bufferPool;

    public StorageEngineHandler() {
        PageReplacementStrategy<TablePageKey> lruStrategy = new LRUStrategy<>(BUFFER_SIZE);
        this.bufferPool = new BufferPool(BUFFER_SIZE, lruStrategy);
    }

    @Override
    public void insert(String tableName, Record record) {
    }

    @Override
    public List<Record> search(String tableName, Object key) {
        // 디스크에서 레코드를 검색하는 로직
        return null;
    }

    @Override
    public void update(String tableName, Object key, byte[] newRecord) {
        // 디스크에서 레코드를 업데이트하는 로직
    }

    @Override
    public void delete(String tableName, Object key) {
        // 디스크에서 레코드를 삭제하는 로직
    }
}
