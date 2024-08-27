package database.innodb.page;

import java.io.Serializable;

public class PageHeader implements Serializable {

    private static final int HEADER_SIZE = 56;

    private int recordCount;
    private boolean isDirty;
    private final PageType pageType;

    public PageHeader(PageType pageType) {
        this.recordCount = 0;
        this.isDirty = false;
        this.pageType = pageType;
    }

    public void incrementRecordCount() {
        recordCount++;
    }

    public void markDirty() {
        isDirty = true;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public PageType getPageType() {
        return pageType;
    }

    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    @Override
    public String toString() {
        return "PageHeader{" +
                "recordCount=" + recordCount +
                ", isDirty=" + isDirty +
                ", pageType=" + pageType +
                '}';
    }
}