package database.page;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private static final int PAGE_SIZE = 16 * 1024;

    private final PageHeader header;
    private final List<Record> records;
    private int freeSpace;
    private int pinCount;

    public Page(int pageNum, PageType pageType) {
        this.header = new PageHeader(pageNum, pageType);
        this.records = new ArrayList<>();
        this.freeSpace = PAGE_SIZE - header.getHeaderSize();
    }

    public boolean addRecord(Record record) {
        if (freeSpace >= record.getSize()) {
            records.add(record);
            freeSpace -= record.getSize();

            header.incrementRecordCount();
            header.setDirty(true);

            return true;
        } else {
            return false;
        }
    }

    public boolean deleteRecord(Record record) {
        if (records.remove(record)) {
            freeSpace += record.getSize();

            header.setDirty(true);
            header.decrementRecordCount();

            return true;
        } else {
            return false;
        }
    }

    public void pin() {
        this.pinCount++;
    }

    public void unPin() {
        this.pinCount--;
    }

    public boolean isPinned() {
        return this.pinCount > 0;
    }

    public PageHeader getHeader() {
        return header;
    }

    public List<Record> getRecords() {
        return records;
    }

    public int getFreeSpace() {
        return freeSpace;
    }

    public int getPinCount() {
        return pinCount;
    }
}
