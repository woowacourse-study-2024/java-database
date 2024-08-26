package database.innodb.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {

    private static final int PAGE_SIZE = 16 * 1024;

    private final FileHeader fileHeader;
    private final PageHeader pageHeader;
    private final List<Record> userRecords;
    private final PageDirectory pageDirectory;
    private int freeSpace;

    public Page(long pageNumber, PageType pageType) {
        this.fileHeader = new FileHeader(pageNumber);
        this.pageHeader = new PageHeader(pageType);
        this.userRecords = new ArrayList<>();
        this.pageDirectory = new PageDirectory();
        this.freeSpace = PAGE_SIZE - (this.fileHeader.getHeaderSize() + this.pageHeader.getHeaderSize());
    }

    public boolean addRecord(Record record) {
        int recordSize = record.getSize();
        if (freeSpace >= recordSize) {
            userRecords.add(record);
            pageDirectory.addRecordPosition(userRecords.size() - 1);
            freeSpace -= recordSize;
            pageHeader.incrementRecordCount();
            return true;
        } else {
            return false;
        }
    }

    public void markDirty() {
        pageHeader.markDirty();
    }

    public long getPageNumber() {
        return fileHeader.getPageNumber();
    }

    public PageType getPageType() {
        return pageHeader.getPageType();
    }

    public int getRecordCount() {
        return pageHeader.getRecordCount();
    }

    public int getFreeSpace() {
        return freeSpace;
    }

    @Override
    public String toString() {
        return "Page{" +
                "fileHeader=" + fileHeader +
                ", pageHeader=" + pageHeader +
                ", userRecords=" + userRecords +
                ", freeSpace=" + freeSpace +
                '}';
    }
}
