package database.innodb.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("레코드 테스트")
class RecordTest {

    @DisplayName("레코드 생성에 성공한다.")
    @Test
    void createRecord() {
        // given
        byte[] data = new byte[]{1, 2, 3, 4};
        Record record = new Record(data);

        // when
        byte[] retrievedData = record.getData();

        // then
        assertAll(
                () -> assertArrayEquals(data, retrievedData),
                () -> assertThat(record.getSize()).isEqualTo(data.length)
        );
    }
}
