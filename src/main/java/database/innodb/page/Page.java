package database.innodb.page;

import java.io.Serializable;

public class Page implements Serializable {

    private static final int PAGE_SIZE = 16 * 1024;

    private final FileHeader fileHeader;
    private final PageHeader pageHeader;
    private int freeSpace;

    public Page(long pageNumber, PageType pageType) {
        this.fileHeader = new FileHeader(pageNumber);
        this.pageHeader = new PageHeader(pageType);
        this.freeSpace = PAGE_SIZE - (this.fileHeader.getHeaderSize() + this.pageHeader.getHeaderSize());
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
}
