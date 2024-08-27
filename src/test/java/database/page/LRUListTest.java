package database.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LRUListTest {

    private static final String testFileName = "testfile.db";

    LinkedList<PageId> list;
    private LRUList lruList;

    @BeforeEach
    public void setUp() {
        list = new LinkedList<>();
        lruList = new LRUList(list);
    }

    @Test
    @DisplayName("LRU 리스트에 페이지를 추가한다")
    public void add() {
        lruList.add(new PageId(testFileName, 1));
        lruList.add(new PageId(testFileName, 2));
        lruList.add(new PageId(testFileName, 3));

        List<Integer> pageNums = list.stream().map(PageId::pageNum).toList();
        assertThat(pageNums).containsExactly(3, 2, 1);
    }

    @Test
    @DisplayName("LRU 리스트에서 페이지를 가장 앞으로 이동한다")
    public void moveToFront() {
        lruList.add(new PageId(testFileName, 1));
        lruList.add(new PageId(testFileName, 2));
        lruList.add(new PageId(testFileName, 3));

        lruList.moveToFront(new PageId(testFileName, 2));

        List<Integer> pageNums = list.stream().map(PageId::pageNum).toList();
        assertThat(pageNums).containsExactly(2, 3, 1);
    }

    @Test
    @DisplayName("LRU 리스트에 제거를 수행한다")
    public void evict() {
        lruList.add(new PageId(testFileName, 1));
        lruList.add(new PageId(testFileName, 2));
        lruList.add(new PageId(testFileName, 3));

        PageId evictId = lruList.evict();
        List<Integer> pageNums = list.stream().map(PageId::pageNum).toList();
        assertAll(
                () -> assertThat(evictId.pageNum()).isEqualTo(1),
                () -> assertThat(pageNums).containsExactly(3, 2)
        );
    }
}
