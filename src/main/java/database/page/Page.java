package database.page;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private static final int PAGE_SIZE = 16 * 1024;

    private final PageHeader header;
    private final List<Record> records;
    private int freeSpace;

    public Page(int pageNum, PageType pageType) {
        this.header = new PageHeader(pageNum, pageType);
        this.records = new ArrayList<>();
        this.freeSpace = PAGE_SIZE - header.getHeaderSize();
    }
}
