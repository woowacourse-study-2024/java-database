package database;

import java.util.Objects;

public class PageId {

    private final String fileName;
    private final int PageNum;

    public PageId(String fileName, int pageNum) {
        this.fileName = fileName;
        this.PageNum = pageNum;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPageNum() {
        return PageNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageId pageId = (PageId) o;
        return PageNum == pageId.PageNum && Objects.equals(fileName, pageId.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, PageNum);
    }
}
