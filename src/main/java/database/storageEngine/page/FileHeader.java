package database.storageEngine.page;

import java.io.Serializable;

public class FileHeader implements Serializable {

    private static final int HEADER_SIZE = 38;

    private final long pageNumber;
    private final long checksum;

    public FileHeader(long pageNumber) {
        this.pageNumber = pageNumber;
        this.checksum = 0;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public long getChecksum() {
        return checksum;
    }

    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    @Override
    public String toString() {
        return "FileHeader{" +
                "pageNumber=" + pageNumber +
                ", checksum=" + checksum +
                '}';
    }
}
