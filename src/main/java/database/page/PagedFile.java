package database.page;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.PriorityQueue;

public class PagedFile {
    public static final int PAGE_SIZE = 16 * 1024; // 16KB 페이지 크기

    private final RandomAccessFile file;
    private final FileHeader fileHeader;

    public PagedFile(RandomAccessFile file) throws IOException {
        this.file = file;
        this.fileHeader = new FileHeader(file, new byte[FileHeader.HEADER_SIZE], new PriorityQueue<>());
    }

    public int allocatePage() throws IOException {
        int pageNum = fileHeader.allocatePage();
        file.seek(FileHeader.HEADER_SIZE + (long) pageNum * PAGE_SIZE);
        file.write(new byte[PAGE_SIZE]);

        return pageNum;
    }

    public void deallocatePage(int pageNum) throws IOException {
        fileHeader.deallocatePage(pageNum);
        file.seek(FileHeader.HEADER_SIZE + (long) pageNum * PAGE_SIZE);
        file.write(new byte[PAGE_SIZE]);
    }

    public void writePage(int pageNum, byte[] data) throws IOException {
        long offset = FileHeader.HEADER_SIZE + (long) pageNum * PAGE_SIZE;

        file.seek(offset);
        file.write(data);
    }

    public Page readPage(int pageNum) throws IOException {
        long offset = FileHeader.HEADER_SIZE + (long) pageNum * PAGE_SIZE;
        byte[] data = new byte[PAGE_SIZE];

        file.seek(offset);
        file.readFully(data);

        return new Page(pageNum, data);
    }
}

