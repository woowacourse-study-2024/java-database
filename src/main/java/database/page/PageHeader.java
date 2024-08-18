package database.page;

public class PageHeader {

    private static final int HEADER_SIZE = 56;

    private int recordCount;
    private final PageType pageType;

    public PageHeader(PageType pageType) {
        this.recordCount = 0;
        this.pageType = pageType;
    }

    public void incrementRecordCount() {
        recordCount++;
    }

    public int getRecordCount() {
        return recordCount;
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
                ", pageType=" + pageType +
                '}';
    }
}
