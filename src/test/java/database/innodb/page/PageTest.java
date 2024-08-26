package database.innodb.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("페이지 테스트")
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

    @DisplayName("레코드 추가에 성공한다.")
    @Test
    void addRecord() {
        // given
        int pageNumber = 1;
        PageType pageType = PageType.PAGE_TYPE_CLUSTERED;
        Page page = new Page(pageNumber, pageType);
        Record record1 = new Record(new byte[]{1, 2, 3, 4});
        Record record2 = new Record(new byte[]{5, 6, 7, 8});

        // when
        page.addRecord(record1);
        page.addRecord(record2);

        // then
        assertAll(
                () -> assertThat(page.getRecordCount()).isEqualTo(2),
                () -> assertThat(page.getPageNumber()).isEqualTo(pageNumber)
        );
    }
}
