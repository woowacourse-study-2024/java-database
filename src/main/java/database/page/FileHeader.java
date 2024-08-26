package database.page;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.PriorityQueue;

public class FileHeader {
    static final int HEADER_SIZE = 16 * 1024;

    private final byte[] pageAllocationBitMap;
    private final PriorityQueue<Integer> freePages;
    private int numPages;

    public FileHeader(
            RandomAccessFile file,
            byte[] pageAllocationBitMap,
            PriorityQueue<Integer> freePages
    ) throws IOException {
        this.pageAllocationBitMap = pageAllocationBitMap;
        file.seek(0);
        if (file.length() == 0) {
            file.write(pageAllocationBitMap);
        } else {
            file.readFully(pageAllocationBitMap);
        }

        this.numPages = (int) (file.length() - HEADER_SIZE) / PagedFile.PAGE_SIZE;
        this.freePages = freePages;
        for (int i = 0; i < numPages; i++) {
            if (!isPageAllocated(i)) {
                freePages.add(i);
            }
        }
    }

    public boolean isPageAllocated(int pageNum) {
        int byteIndex = pageNum / 8;
        int bitIndex = pageNum % 8;
        return (pageAllocationBitMap[byteIndex] & (1 << bitIndex)) != 0;
    }

    public int allocatePage() {
        int pageNum;
        if (!freePages.isEmpty()) {
            pageNum = freePages.poll();
        } else {
            pageNum = numPages++;
        }

        int byteIndex = pageNum / 8;
        int bitIndex = pageNum % 8;
        pageAllocationBitMap[byteIndex] |= (byte) (1 << bitIndex);

        return pageNum;
    }

    public void deallocatePage(int pageNum) {
        int byteIndex = pageNum / 8;
        int bitIndex = pageNum % 8;
        pageAllocationBitMap[byteIndex] &= (byte) ~(1 << bitIndex);
        freePages.add(pageNum);
    }
}
