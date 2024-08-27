package database.storageEngine.bufferpool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import database.storageEngine.bufferpool.pageReplacementStrategies.LRUStrategy;
import database.storageEngine.page.Page;
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
        PageReplacementStrategy<TablePageKey, Page> lruStrategy = new LRUStrategy<>(capacity);
        bufferPool = new BufferPool(capacity, lruStrategy);
    }

    @DisplayName("버퍼풀 생성에 성공한다.")
    @Test
    void createBufferPool() {
        // given
        int pageNumber1 = 1;
        int pageNumber2 = 2;

        // when
        bufferPool.putPage(tableName, PageFactory.createDataPage(pageNumber1));
        bufferPool.putPage(tableName, PageFactory.createDataPage(pageNumber2));

        // then
        assertThat(bufferPool.containsPage(tableName, 1)).isTrue();
        assertThat(bufferPool.containsPage(tableName, 2)).isTrue();
    }

    @DisplayName("LRU 정책에 따라 가장 오래된 페이지가 제거된다.")
    @Test
    void lru() {
        // given
        int pageNumber1 = 1;
        int pageNumber2 = 2;
        int pageNumber3 = 3;

        bufferPool.putPage(tableName, PageFactory.createDataPage(pageNumber1));
        bufferPool.putPage(tableName, PageFactory.createDataPage(pageNumber2));

        // when
        bufferPool.getPage(tableName, pageNumber1);
        bufferPool.putPage(tableName, PageFactory.createDataPage(pageNumber3));

        // then
        assertAll(
                () -> assertThat(bufferPool.containsPage(tableName, pageNumber1)).isTrue(),
                () -> assertThat(bufferPool.containsPage(tableName, pageNumber3)).isTrue(),
                () -> assertThat(bufferPool.containsPage(tableName, pageNumber2)).isFalse()
        );
    }
}
