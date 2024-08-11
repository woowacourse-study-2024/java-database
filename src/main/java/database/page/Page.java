package database.page;

public class Page {
    private final long pageNum;
    private final byte[] data;

    public Page(long pageNum, byte[] data) {
        this.pageNum = pageNum;
        this.data = data;
    }

    public long getPageNum() {
        return pageNum;
    }

    public byte[] getData() {
        return data;
    }
}
