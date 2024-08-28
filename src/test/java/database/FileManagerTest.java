package database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileManagerTest {

    private FileManager fileManager;
    private PageId pageId;
    private Page page;

    @BeforeEach
    void setUp() {
        fileManager = new FileManager();
        pageId = new PageId("jazz", 1);
        page = new Page(1, PageType.SECONDARY_INDEX);
    }

    @DisplayName("파일에 페이지를 저장하고, 읽어올 수 있다.")
    @Test
    void writePageTest() {
        fileManager.writePage(pageId, page);

        Page readPage = fileManager.readPage(pageId);

        assertAll(
                () -> assertThat(readPage.getPageType()).isEqualTo(page.getPageType()),
                () -> assertThat(readPage.getPageNum()).isEqualTo(page.getPageNum()),
                () -> assertThat(readPage.getFreeSpace()).isEqualTo(page.getFreeSpace())
        );
    }
}
