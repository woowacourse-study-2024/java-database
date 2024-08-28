package database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageIdTest {

    @DisplayName("내부 필드가 모두 같으면 동일한 객체이다.")
    @Test
    void equalsTest() {
        PageId pageId1 = new PageId("jazz", 1);
        PageId pageId2 = new PageId("jazz", 1);

        assertThat(Objects.equals(pageId1, pageId2)).isTrue();
    }

    @DisplayName("내부 필드가 모두 같으면 동일한 해시를 반환한다.")
    @Test
    void hashCodeTest() {
        PageId pageId1 = new PageId("jazz", 1);
        PageId pageId2 = new PageId("jazz", 1);

        assertThat(pageId1.hashCode()).isEqualTo(pageId2.hashCode());
    }
}
