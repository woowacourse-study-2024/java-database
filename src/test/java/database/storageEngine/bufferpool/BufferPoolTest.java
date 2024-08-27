package database.storageEngine.bufferpool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import database.storageEngine.bufferpool.pageReplacementStrategies.LRUStrategy;
import database.storageEngine.page.PageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("버퍼풀 테스트")
class BufferPoolTest {

    private final int capacity = 2;
    private final String tableName = "table";
    private BufferPool bufferPool;

    @BeforeEach
    void setUp() {
        PageReplacementStrategy<TablePageKey> lruStrategy = new LRUStrategy<>(capacity);
        bufferPool = new BufferPool(capacity, lruStrategy);
    }

    @DisplayName("버퍼풀 생성에 성공한다.")
    @Test
    void createBufferPool() {
        // given
        int pageNumber1 = 1;
        TablePageKey tablePageKey1 = new TablePageKey(tableName, pageNumber1);
        int pageNumber2 = 2;
        TablePageKey tablePageKey2 = new TablePageKey(tableName, pageNumber2);

        // when
        bufferPool.putPage(tablePageKey1, PageFactory.createDataPage(pageNumber1));
        bufferPool.putPage(tablePageKey2, PageFactory.createDataPage(pageNumber2));

        // then
        assertAll(
                () -> assertThat(bufferPool.getPage(tablePageKey1)).isPresent(),
                () -> assertThat(bufferPool.getPage(tablePageKey2)).isPresent()
        );
    }

    @DisplayName("LRU 정책에 따라 가장 오래된 페이지가 제거된다.")
    @Test
    void lru() {
        // given
        int pageNumber1 = 1;
        TablePageKey tablePageKey1 = new TablePageKey(tableName, pageNumber1);
        int pageNumber2 = 2;
        TablePageKey tablePageKey2 = new TablePageKey(tableName, pageNumber2);
        int pageNumber3 = 3;
        TablePageKey tablePageKey3 = new TablePageKey(tableName, pageNumber3);

        bufferPool.putPage(tablePageKey1, PageFactory.createDataPage(pageNumber1));
        bufferPool.putPage(tablePageKey2, PageFactory.createDataPage(pageNumber2));

        // when
        bufferPool.getPage(tablePageKey1);
        bufferPool.putPage(tablePageKey3, PageFactory.createDataPage(pageNumber3));

        // then
        assertAll(
                () -> assertThat(bufferPool.getPage(tablePageKey1)).isPresent(),
                () -> assertThat(bufferPool.getPage(tablePageKey2)).isEmpty(),
                () -> assertThat(bufferPool.getPage(tablePageKey3)).isPresent()
        );
    }
}
