package database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BufferPoolTest {

    private PageManager pageManager;
    private BufferPool bufferPool;

    private PageId pageId;
    private Page page;

    @BeforeEach
    void setUp() {
        FileManager fileManager = new FileManager();
        pageManager = new PageManager(fileManager);
        bufferPool = new BufferPool(pageManager);

        pageId = new PageId("jazz", 1);
        page = new Page(1, PageType.SECONDARY_INDEX);
    }

    @DisplayName("버퍼 풀에 없는 페이지를 불러오면 디스크에서 버퍼 풀로 페이지를 적재한다")
    @Test
    void loadPageFromDiskWhenNotInCache() {
        pageManager.savePage(pageId, page);

        Page loadedPage = bufferPool.getPage(pageId);

        assertAll(
                () -> assertThat(loadedPage.getPageType()).isEqualTo(page.getPageType()),
                () -> assertThat(loadedPage.getPageNum()).isEqualTo(page.getPageNum()),
                () -> assertThat(loadedPage.getFreeSpace()).isEqualTo(page.getFreeSpace()),
                () -> assertThat(loadedPage.isPinned()).isTrue()
        );
    }

    @DisplayName("플러시 호출 시 핀 상태인 페이지는 디스크에 저장되지 않는다.")
    @Test
    void notFlushPinnedPageToDisk() {
        page.setDirty(true);
        page.pin();
        bufferPool.getBufferPool().put(pageId, page);

        bufferPool.flush();

        assertAll(
                () -> assertThat(page.isPinned()).isTrue(),
                () -> assertThat(page.isDirty()).isTrue()
        );
    }

    @DisplayName("플러시 호출 시 핀 상태가 아닌 페이지는 디스크에 저장되고 더티 페이지가 아니다.")
    @Test
    void flushUnpinnedPageToDisk() {
        page.setDirty(true);
        page.pin();
        page.unPin();
        bufferPool.getBufferPool().put(pageId, page);

        bufferPool.flush();

        assertAll(
                () -> assertThat(page.isPinned()).isFalse(),
                () -> assertThat(page.isDirty()).isFalse()
        );
    }
}
