package database.storageEngine.page;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("페이지 매니저 테스트")
class PageManagerTest {

    private static final String DIRECTORY_PATH = "disk";
    private static final String FILE_EXTENSION = ".ibd";

    private PageManager pageManager;
    private final String tableName = "table";

    @BeforeEach
    void setUp() {
        pageManager = new PageManager();
    }

    @DisplayName("페이지 저장에 성공한다.")
    @Test
    void savePage() {
        // given
        Page page = PageFactory.createDataPage(0);

        // when
        pageManager.savePage(tableName, page);

        // then
        Path filePath = Paths.get(DIRECTORY_PATH, tableName + FILE_EXTENSION);
        assertThat(Files.exists(filePath)).isTrue();
    }

    @DisplayName("페이지 조회에 성공한다.")
    @Test
    void loadPage() {
        // given
        int pageNumber1 = 1;
        int pageNumber2 = 2;

        Page page1 = PageFactory.createDataPage(pageNumber1);
        Page page2 = PageFactory.createUndoPage(pageNumber2);

        pageManager.savePage(tableName, page1);
        pageManager.savePage(tableName, page2);

        // when
        Page foundPage1 = pageManager.loadPage(tableName, pageNumber1);
        Page foundPage2 = pageManager.loadPage(tableName, pageNumber2);

        // then
        assertThat(foundPage1.getPageNumber()).isEqualTo(pageNumber1);
        assertThat(foundPage2.getPageNumber()).isEqualTo(pageNumber2);
    }

    @AfterEach
    void tearDown() {
        Path filePath = Paths.get(DIRECTORY_PATH, tableName + FILE_EXTENSION);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.deleteIfExists(Paths.get(DIRECTORY_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
