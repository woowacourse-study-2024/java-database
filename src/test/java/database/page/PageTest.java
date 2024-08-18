package database.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("페이지 관련 테스트")
class PageTest {

    @DisplayName("페이지 생성에 성공하다.")
    @Test
    void createPage() {
        // given
        int pageNumber = 1;
        PageType pageType = PageType.PAGE_TYPE_CLUSTERED;

        // when
        Page page = new Page(pageNumber, pageType);

        // then
        assertAll(
                () -> assertThat(page.getPageNumber()).isEqualTo(pageNumber),
                () -> assertThat(page.getPageType()).isEqualTo(pageType),
                () -> assertThat(page.getRecordCount()).isEqualTo(0)
        );
    }
}
