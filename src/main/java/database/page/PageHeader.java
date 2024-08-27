package database.page;

public class PageHeader {

    public static final int HEADER_SIZE = 13;

    private final int pageNum;
    private final PageType pageType;
    private final int recordCount;
    private final boolean isDirty;

    public PageHeader(int pageNum, PageType pageType) {
        this.pageNum = pageNum;
        this.pageType = pageType;
        this.recordCount = 0;
        this.isDirty = false;
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
