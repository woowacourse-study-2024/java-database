package database.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BufferManagerTest {

    private static final int BUFFER_SIZE = 3;
    private static final String testFileName = "testfile.db";

    private HashMap<Integer, Page> pageBuffer;
    private LinkedList<Integer> list;
    private LRUList lruList;
    private PagedFile pagedFile;
    private BufferManager bufferManager;

    @BeforeEach
    public void setUp() throws IOException {
        File file = new File(testFileName);
        RandomAccessFile randomAccessFile = new RandomAccessFile(testFileName, "rw");
        pageBuffer = new HashMap<>();
        list = new LinkedList<>();
        lruList = new LRUList(list);
        pagedFile = new PagedFile(randomAccessFile);
        bufferManager = new BufferManager(BUFFER_SIZE, pageBuffer, lruList, pagedFile);
    }

    @AfterEach
    public void clear() {
        File file = new File(testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("버퍼에서 페이지를 로드한다")
    public void getPage() throws IOException {
        int pageNum = pagedFile.allocatePage();
        Page page = bufferManager.getPage(pageNum);

        assertAll(
                () -> assertThat(list).containsExactly(pageNum),
                () -> assertThat(pageBuffer).containsValue(page)

        );
    }

    @Test
    @DisplayName("페이지를 로드하면 LRU 리스트를 갱신한다.")
    public void getPageUpdateLRUList() throws IOException {
        int pageNum1 = pagedFile.allocatePage();
        int pageNum2 = pagedFile.allocatePage();

        bufferManager.getPage(pageNum1);
        bufferManager.getPage(pageNum2);
        bufferManager.getPage(pageNum1);

        assertAll(
                () -> assertThat(list).containsExactly(pageNum1, pageNum2),
                () -> assertThat(pageBuffer).containsKeys(pageNum1, pageNum2)
        );
    }

    @Test
    @DisplayName("버퍼 크기를 넘으면 가장 오래된 페이지를 제거한다")
    public void getPageOverBufferSize() throws IOException {
        int pageNum1 = pagedFile.allocatePage();
        int pageNum2 = pagedFile.allocatePage();
        int pageNum3 = pagedFile.allocatePage();
        int pageNum4 = pagedFile.allocatePage();

        bufferManager.getPage(pageNum1);
        bufferManager.getPage(pageNum2);
        bufferManager.getPage(pageNum3);
        bufferManager.getPage(pageNum4);

        assertAll(
                () -> assertThat(list).containsExactly(pageNum4, pageNum3, pageNum2),
                () -> assertThat(pageBuffer).containsKeys(pageNum2, pageNum3, pageNum4)
        );
    }

    @Test
    @DisplayName("flushPage 메서드로 페이지를 디스크에 기록한다")
    public void flushPage() throws IOException {
        int pageNum = pagedFile.allocatePage();
        Page page = bufferManager.getPage(pageNum);
        byte[] newData = "Test Data".getBytes();
        page.updateData(newData);

        bufferManager.flushPage(pageNum);

        Page reloadedPage = pagedFile.readPage(pageNum);
        assertAll(
                () -> assertThat(reloadedPage.getData()).startsWith(newData),
                () -> assertThat(page.isDirty()).isFalse()
        );
    }

    @Test
    @DisplayName("flushAllPages 메서드로 모든 페이지를 디스크에 기록한다")
    public void flushAllPages() throws IOException {
        int pageNum1 = pagedFile.allocatePage();
        int pageNum2 = pagedFile.allocatePage();
        Page page1 = bufferManager.getPage(pageNum1);
        Page page2 = bufferManager.getPage(pageNum2);
        page1.updateData("Page1 Data".getBytes());
        page2.updateData("Page2 Data".getBytes());

        bufferManager.flushAllPages();

        Page reloadedPage1 = pagedFile.readPage(pageNum1);
        Page reloadedPage2 = pagedFile.readPage(pageNum2);
        assertAll(
                () -> assertThat(reloadedPage1.getData()).startsWith("Page1 Data".getBytes()),
                () -> assertThat(reloadedPage2.getData()).startsWith("Page2 Data".getBytes()),
                () -> assertThat(page1.isDirty()).isFalse(),
                () -> assertThat(page2.isDirty()).isFalse()
        );
    }
}
