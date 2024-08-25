package database.innodb.page;

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
    private static final String INFIX = "page_";
    private static final String FILE_EXTENSION = ".ibd";

    private PageManager pageManager;
    private final int pageNumber = 1234;

    @BeforeEach
    void setUp() {
        pageManager = new PageManager();
    }

    @DisplayName("페이지 저장에 성공한다.")
    @Test
    void savePage() {
        // given
        Page page = PageFactory.createDataPage(pageNumber);

        // when
        pageManager.savePage(page);

        // then
        Path filePath = Paths.get(DIRECTORY_PATH, INFIX + pageNumber + FILE_EXTENSION);
        assertThat(Files.exists(filePath)).isTrue();
    }

    @DisplayName("페이지 조회에 성공한다.")
    @Test
    void loadPage() {
        // given
        Page page = PageFactory.createDataPage(pageNumber);
        pageManager.savePage(page);

        // when
        Page foundPage = pageManager.loadPage(pageNumber);

        // then
        assertThat(foundPage.getPageNumber()).isEqualTo(pageNumber);
    }

    @AfterEach
    void tearDown() {
        Path filePath = Paths.get(DIRECTORY_PATH, INFIX + pageNumber + FILE_EXTENSION);
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
