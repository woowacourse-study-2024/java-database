package database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LRUCacheTest {

    private LRUCache<PageId, Page> lruCache;

    private PageId pageId1;
    private Page page1;
    private PageId pageId2;
    private Page page2;

    @BeforeEach
    void setUp() {
        FileManager fileManager = new FileManager();
        PageManager pageManager = new PageManager(fileManager);
        lruCache = new LRUCache<>(2, 0.75f, true, pageManager);

        pageId1 = new PageId("jazz", 1);
        page1 = new Page(1, PageType.CLUSTERED_INDEX);

        pageId2 = new PageId("jazz", 2);
        page2 = new Page(2, PageType.CLUSTERED_INDEX);
    }

    @DisplayName("캐시가 가득차고 새 페이지가 추가되었을 때, 가장 오래된 페이지가 캐시에서 제거된다.")
    @Test
    void removeEldestEntryTest() {
        lruCache.put(pageId1, page1);
        lruCache.put(pageId2, page2);

        PageId newPageId = new PageId("jazz", 3);
        Page newPage = new Page(3, PageType.CLUSTERED_INDEX);

        lruCache.get(pageId1); // cache hit
        lruCache.put(newPageId, newPage);

        assertAll(
                () -> assertThat(lruCache.containsKey(pageId2)).isFalse(),
                () -> assertThat(lruCache.containsKey(newPageId)).isTrue()
        );
    }
}
