package database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageTest {

    @DisplayName("페이지에 레코드를 추가하는데 성공하면 true를 반환한다.")
    @Test
    void addRecordSuccessTest() {
        Page page = new Page(1, PageType.CLUSTERED_INDEX);
        Record record = new Record(1, new byte[100]);

        int previousSize = page.getFreeSpace();
        boolean isAdded = page.addRecord(record);

        assertAll(
                () -> assertThat(isAdded).isTrue(),
                () -> assertThat(page.getFreeSpace()).isEqualTo(previousSize - record.getSize()),
                () -> assertThat(page.isDirty()).isTrue()
        );
    }

    @DisplayName("페이지에 레코드를 추가하는데 실패하면 false를 반환한다.")
    @Test
    void addRecordFailTest() {
        Page page = new Page(1, PageType.CLUSTERED_INDEX);
        Record record = new Record(1, new byte[Page.PAGE_SIZE]);

        boolean isAdded = page.addRecord(record);

        assertThat(isAdded).isFalse();
    }

    @DisplayName("페이지에 레코드를 삭제하는데 성공하면 true를 반환한다.")
    @Test
    void deleteRecordSuccessTest() {
        Page page = new Page(1, PageType.CLUSTERED_INDEX);
        Record record = new Record(1, new byte[100]);
        page.addRecord(record);

        int previousSize = page.getFreeSpace();
        boolean isDeleted = page.deleteRecord(record);

        assertAll(
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(page.getFreeSpace()).isEqualTo(previousSize + record.getSize()),
                () -> assertThat(page.isDirty()).isTrue()
        );
    }

    @DisplayName("페이지에 레코드를 삭제하는데 실패하면 false를 반환한다.")
    @Test
    void deleteRecordFailTest() {
        Page page = new Page(1, PageType.CLUSTERED_INDEX);
        Record record = new Record(1, new byte[Page.PAGE_SIZE]);

        boolean isDeleted = page.deleteRecord(record);

        assertThat(isDeleted).isFalse();
    }
}
