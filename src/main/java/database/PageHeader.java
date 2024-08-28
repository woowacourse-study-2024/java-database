package database;

import java.io.Serializable;

public class PageHeader implements Serializable {

    public static final int HEADER_SIZE = 13;

    private final int pageNum;
    private final PageType pageType;
    private int recordCount;
    private boolean isDirty;

    public PageHeader(int pageNum, PageType pageType) {
        this.pageNum = pageNum;
        this.pageType = pageType;
        this.recordCount = 0;
        this.isDirty = false;
    }

    public void incrementRecordCount() {
        this.recordCount++;
    }

    public void decrementRecordCount() {
        this.recordCount--;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public int getPageNum() {
        return pageNum;
    }

    public PageType getPageType() {
        return pageType;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public int getHeaderSize() {
        return HEADER_SIZE;
    }
}
