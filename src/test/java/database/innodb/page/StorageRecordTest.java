package database.innodb.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("레코드 테스트")
class StorageRecordTest {

    @DisplayName("레코드 생성에 성공한다.")
    @Test
    void createRecord() {
        // given
        byte[] data = new byte[]{1, 2, 3, 4};
        StorageRecord storageRecord = new StorageRecord(data);

        // when
        byte[] retrievedData = storageRecord.getData();

        // then
        assertAll(
                () -> assertArrayEquals(data, retrievedData),
                () -> assertThat(storageRecord.getSize()).isEqualTo(data.length)
        );
    }
}
