package database.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BufferManagerTest {

    private static final int BUFFER_SIZE = 3;
    private static final String testFileName = "testfile.db";
    private static final String oldFileName = "oldFile.db";

    private HashMap<PageId, Page> pageBuffer;
    private LinkedList<PageId> list;
    private LRUList lruList;
    private HashMap<String, PagedFile> openFiles;
    private PagedFileManager pagedFileManager;
    private PagedFile pagedFile;
    private Buffer buffer;
    private BufferManager bufferManager;

    @BeforeEach
    public void setUp() throws IOException {
        pageBuffer = new HashMap<>();
        list = new LinkedList<>();
        lruList = new LRUList(list);
        openFiles = new HashMap<>();
        pagedFileManager = new PagedFileManager(openFiles);
        pagedFileManager.createFile(testFileName);
        pagedFile = pagedFileManager.openFile(testFileName);
        buffer = new Buffer(BUFFER_SIZE, pageBuffer);
        bufferManager = new BufferManager(buffer, lruList, pagedFileManager);
    }

    @AfterEach
    public void clear() {
        File file = new File(testFileName);
        if (file.exists()) {
            file.delete();
        }
        File oldFile = new File(oldFileName);
        if (oldFile.exists()) {
            oldFile.delete();
        }
    }

    @Test
    @DisplayName("버퍼에 페이지를 로드한다")
    public void getPage() throws IOException {
        int pageNum = pagedFile.allocatePage();
        PageId pageId = new PageId(testFileName, pageNum);

        Page page = bufferManager.getPage(pageId);

        assertAll(
                () -> assertThat(list).containsExactly(pageId),
                () -> assertThat(pageBuffer).containsValue(page)
        );
    }

    @Test
    @DisplayName("페이지를 로드하면 LRU 리스트를 갱신한다.")
    public void getPageUpdateLRUList() throws IOException {
        int pageNum1 = pagedFile.allocatePage();
        int pageNum2 = pagedFile.allocatePage();
        PageId pageId1 = new PageId(testFileName, pageNum1);
        PageId pageId2 = new PageId(testFileName, pageNum2);

        bufferManager.getPage(pageId1);
        bufferManager.getPage(pageId2);
        bufferManager.getPage(pageId1);

        assertAll(
                () -> assertThat(list).containsExactly(pageId1, pageId2),
                () -> assertThat(pageBuffer).containsKeys(pageId2, pageId1)
        );
    }

    @Test
    @DisplayName("버퍼 크기를 넘으면 가장 오래된 페이지를 제거한다")
    public void getPageOverBufferSize() throws IOException {
        int pageNum1 = pagedFile.allocatePage();
        int pageNum2 = pagedFile.allocatePage();
        int pageNum3 = pagedFile.allocatePage();
        int pageNum4 = pagedFile.allocatePage();
        PageId pageId1 = new PageId(testFileName, pageNum1);
        PageId pageId2 = new PageId(testFileName, pageNum2);
        PageId pageId3 = new PageId(testFileName, pageNum3);
        PageId pageId4 = new PageId(testFileName, pageNum4);

        bufferManager.getPage(pageId1);
        bufferManager.getPage(pageId2);
        bufferManager.getPage(pageId3);
        bufferManager.getPage(pageId4);

        assertAll(
                () -> assertThat(list).containsExactly(pageId4, pageId3, pageId2),
                () -> assertThat(pageBuffer).containsKeys(pageId2, pageId3, pageId4)
        );
    }

    @Test
    @DisplayName("Dirty 페이지가 제거될 때 더이상 참조되지 않으면 디스크에 기록되고 파일을 닫는다")
    public void evictDirtyPageAndCloseFile() throws IOException {
        pagedFileManager.createFile(oldFileName);
        PagedFile oldPagedFile = pagedFileManager.openFile(oldFileName);

        int pageNum1 = oldPagedFile.allocatePage();
        int pageNum2 = pagedFile.allocatePage();
        int pageNum3 = pagedFile.allocatePage();
        int pageNum4 = pagedFile.allocatePage();
        PageId pageId1 = new PageId(oldFileName, pageNum1);
        PageId pageId2 = new PageId(testFileName, pageNum2);
        PageId pageId3 = new PageId(testFileName, pageNum3);
        PageId pageId4 = new PageId(testFileName, pageNum4);

        bufferManager.getPage(pageId1);
        bufferManager.getPage(pageId2);
        bufferManager.getPage(pageId3);
        bufferManager.getPage(pageId4);

        assertAll(
                () -> assertThat(openFiles).doesNotContainKey(oldFileName),
                () -> assertThat(openFiles).containsKey(testFileName)
        );
    }

    @Test
    @DisplayName("flushPage 메서드로 페이지를 디스크에 기록한다")
    public void flushPage() throws IOException {
        int pageNum = pagedFile.allocatePage();
        PageId pageId = new PageId(testFileName, pageNum);
        Page page = bufferManager.getPage(pageId);
        byte[] newData = "Test Data".getBytes();
        page.updateData(newData);

        bufferManager.flushPage(pageId);

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
        PageId pageId1 = new PageId(testFileName, pageNum1);
        PageId pageId2 = new PageId(testFileName, pageNum2);
        Page page1 = bufferManager.getPage(pageId1);
        Page page2 = bufferManager.getPage(pageId2);
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
