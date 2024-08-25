package database.innodb.page;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("페이지 헤더 테스트")
class PageHeaderTest {

    @DisplayName("페이지 헤더를 생성한다.")
    @Test
    void createPageHeader() {
        // given
        PageType pageType = PageType.PAGE_TYPE_CLUSTERED;
        int initialRecordCnt = 0;

        // when
        PageHeader pageHeader = new PageHeader(pageType);

        // then
        assertThat(pageHeader.getPageType()).isEqualTo(pageType);
        assertThat(pageHeader.getRecordCount()).isEqualTo(initialRecordCnt);
    }

    @DisplayName("페이지 레코드 수가 증가한다.")
    @Test
    void incrementRecord() {
        // given
        PageType pageType = PageType.PAGE_TYPE_CLUSTERED;
        int recordCnt = 2;

        // when
        PageHeader pageHeader = new PageHeader(pageType);
        IntStream.range(0, recordCnt)
                .forEach(i -> pageHeader.incrementRecordCount());

        // then
        assertThat(pageHeader.getRecordCount()).isEqualTo(recordCnt);
    }
}
