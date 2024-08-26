package database.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LRUListTest {

    LinkedList<Integer> list;
    private LRUList lruList;

    @BeforeEach
    public void setUp() {
        list = new LinkedList<>();
        lruList = new LRUList(list);
    }

    @Test
    @DisplayName("LRU 리스트에 페이지를 추가한다")
    public void add() {
        lruList.add(1);
        lruList.add(2);
        lruList.add(3);

        assertThat(list).containsExactly(3, 2, 1);
    }

    @Test
    @DisplayName("LRU 리스트에서 페이지를 가장 앞으로 이동한다")
    public void moveToFront() {
        lruList.add(1);
        lruList.add(2);
        lruList.add(3);

        lruList.moveToFront(2);

        assertThat(list).containsExactly(2, 3, 1);
    }

    @Test
    @DisplayName("LRU 리스트에 제거를 수행한다")
    public void evict() {
        lruList.add(1);
        lruList.add(2);
        lruList.add(3);

        assertAll(
                () -> assertThat(lruList.evict()).isEqualTo(1),
                () -> assertThat(list).containsExactly(3, 2)
        );
    }
}
