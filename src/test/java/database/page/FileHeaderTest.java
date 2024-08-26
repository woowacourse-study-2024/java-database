package database.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.PriorityQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileHeaderTest {

    private final String testFileName = "test.dat";

    private File file;
    private RandomAccessFile randomAccessFile;

    @BeforeEach
    public void setUp() throws IOException {
        file = new File(testFileName);
        file.createNewFile();
        randomAccessFile = new RandomAccessFile(file, "rw");
    }

    @AfterEach
    public void clearFile() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("파일 헤더를 최초로 생성한다.")
    void createFileHeader() throws IOException {
        FileHeader fileHeader = new FileHeader(
                randomAccessFile,
                new byte[FileHeader.HEADER_SIZE],
                new PriorityQueue<>()
        );

        assertThat(randomAccessFile.length()).isEqualTo(FileHeader.HEADER_SIZE);
    }

    @Test
    @DisplayName("생성된 파일 헤더를 불러온다.")
    void loadFileHeader() throws IOException {
        long length = randomAccessFile.length();
        byte[] pageAllocationBitMap = new byte[FileHeader.HEADER_SIZE];
        pageAllocationBitMap[0] = 0b10;
        FileHeader fileHeader = new FileHeader(
                randomAccessFile,
                pageAllocationBitMap,
                new PriorityQueue<Integer>()
        );
        randomAccessFile.seek(FileHeader.HEADER_SIZE);
        randomAccessFile.write(new byte[PagedFile.PAGE_SIZE * 2]);

        byte[] loadedPageAllocationBitMap = new byte[FileHeader.HEADER_SIZE];
        PriorityQueue<Integer> freePages = new PriorityQueue<>();
        FileHeader loadedFileHeader = new FileHeader(
                new RandomAccessFile(file, "rw"),
                loadedPageAllocationBitMap,
                freePages
        );

        assertAll(
                () -> assertThat(loadedPageAllocationBitMap[0]).isEqualTo((byte) 0b10),
                () -> assertThat(freePages).containsExactly(0)
        );
    }

    @Test
    @DisplayName("페이지 할당 여부를 확인한다.")
    void isPageAllocated() throws IOException {
        byte[] pageAllocationBitMap = new byte[FileHeader.HEADER_SIZE];
        pageAllocationBitMap[0] = 0b10;
        FileHeader fileHeader = new FileHeader(
                randomAccessFile,
                pageAllocationBitMap,
                new PriorityQueue<>()
        );

        assertAll(
                () -> assertThat(fileHeader.isPageAllocated(0)).isFalse(),
                () -> assertThat(fileHeader.isPageAllocated(1)).isTrue()
        );
    }

    @Test
    @DisplayName("페이지를 할당한다.")
    void allocatePage() throws IOException {
        byte[] pageAllocationBitMap = new byte[FileHeader.HEADER_SIZE];
        FileHeader fileHeader = new FileHeader(
                randomAccessFile,
                pageAllocationBitMap,
                new PriorityQueue<>()
        );

        int allocatePage = fileHeader.allocatePage();

        assertAll(
                () -> assertThat(allocatePage).isEqualTo(0),
                () -> assertThat(pageAllocationBitMap[0]).isEqualTo((byte) 0b1)
        );
    }

    @Test
    @DisplayName("할당 해제된 페이지를 할당한다.")
    void allocatePageFree() throws IOException {
        byte[] pageAllocationBitMap = new byte[FileHeader.HEADER_SIZE];
        pageAllocationBitMap[0] = 0b111;
        PriorityQueue<Integer> freePages = new PriorityQueue<>();
        FileHeader fileHeader = new FileHeader(
                randomAccessFile,
                pageAllocationBitMap,
                freePages
        );

        fileHeader.deallocatePage(0);
        int allocatePage = fileHeader.allocatePage();

        assertAll(
                () -> assertThat(allocatePage).isEqualTo(0),
                () -> assertThat(pageAllocationBitMap[0]).isEqualTo((byte) 0b111),
                () -> assertThat(freePages).isEmpty()
        );
    }

    @Test
    @DisplayName("페이지를 해제한다.")
    void deallocatePage() throws IOException {
        byte[] pageAllocationBitMap = new byte[FileHeader.HEADER_SIZE];
        pageAllocationBitMap[0] = 0b11;
        PriorityQueue<Integer> freePages = new PriorityQueue<>();
        FileHeader fileHeader = new FileHeader(
                randomAccessFile,
                pageAllocationBitMap,
                freePages
        );

        fileHeader.deallocatePage(1);

        assertAll(
                () -> assertThat(pageAllocationBitMap[0]).isEqualTo((byte) 0b01),
                () -> assertThat(freePages).containsExactly(1)
        );
    }
}
