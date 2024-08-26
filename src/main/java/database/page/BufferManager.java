package database.page;

import java.io.IOException;
import java.util.Map;

public class BufferManager {
    private final int bufferSize;
    private final Map<Integer, Page> pageBuffer;
    private final LRUList lruList;
    private final PagedFile pagedFile;

    public BufferManager(int bufferSize, Map<Integer, Page> pageBuffer, LRUList lruList, PagedFile pagedFile) {
        this.bufferSize = bufferSize;
        this.pageBuffer = pageBuffer;
        this.lruList = lruList;
        this.pagedFile = pagedFile;
    }

    public Page getPage(int pageNum) throws IOException {
        if (pageBuffer.containsKey(pageNum)) {
            lruList.moveToFront(pageNum);
            return pageBuffer.get(pageNum);
        }

        Page page = pagedFile.readPage(pageNum);
        if (pageBuffer.size() >= bufferSize) {
            evictPage();
        }

        pageBuffer.put(pageNum, page);
        lruList.add(pageNum);
        return page;
    }

    private void evictPage() throws IOException {
        int pageNumToEvict = lruList.evict();
        Page pageToEvict = pageBuffer.remove(pageNumToEvict);
        if (pageToEvict.isDirty()) {
            pagedFile.writePage(pageToEvict.getPageNum(), pageToEvict.getData());
        }
    }

    public void flushPage(int pageNum) throws IOException {
        if (pageBuffer.containsKey(pageNum)) {
            Page page = pageBuffer.get(pageNum);
            if (page.isDirty()) {
                pagedFile.writePage(pageNum, page.getData());
                page.flush();
            }
        }
    }

    public void flushAllPages() throws IOException {
        for (Page page : pageBuffer.values()) {
            if (page.isDirty()) {
                pagedFile.writePage(page.getPageNum(), page.getData());
                page.flush();
            }
        }
    }
}
