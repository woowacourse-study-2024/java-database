package database.page;

import java.io.IOException;

public class BufferManager {
    private final Buffer buffer;
    private final LRUList lruList;
    private final PagedFileManager pagedFileManager;

    public BufferManager(Buffer buffer, LRUList lruList, PagedFileManager pagedFileManager) {
        this.buffer = buffer;
        this.lruList = lruList;
        this.pagedFileManager = pagedFileManager;
    }

    public Page getPage(PageId pageId) throws IOException {
        if (buffer.containsPage(pageId)) {
            lruList.moveToFront(pageId);
            return buffer.getPage(pageId);
        }

        PagedFile pagedFile = pagedFileManager.openFile(pageId.filename());

        Page page = pagedFile.readPage(pageId.pageNum());
        if (buffer.isFull()) {
            evictPage();
        }

        buffer.putPage(pageId, page);
        lruList.add(pageId);
        return page;
    }

    private void evictPage() throws IOException {
        PageId pageIdToEvict = lruList.evict();
        Page pageToEvict = buffer.removePage(pageIdToEvict);
        String filename = pageIdToEvict.filename();
        if (pageToEvict.isDirty()) {
            PagedFile pagedFile = pagedFileManager.openFile(filename);
            pagedFile.writePage(pageToEvict.getPageNum(), pageToEvict.getData());
        }

        if (!buffer.isReferenced(filename)) {
            pagedFileManager.closeFile(pageIdToEvict.filename());
        }
    }

    public void flushPage(PageId pageId) throws IOException {
        if (buffer.containsPage(pageId)) {
            Page page = buffer.getPage(pageId);
            if (page.isDirty()) {
                PagedFile pagedFile = pagedFileManager.openFile(pageId.filename());
                pagedFile.writePage(pageId.pageNum(), page.getData());
                page.flush();
            }
        }
    }

    public void flushAllPages() throws IOException {
        for (PageId pageId : buffer.getPageIds()) {
            Page page = buffer.getPage(pageId);
            if (page.isDirty()) {
                PagedFile pagedFile = pagedFileManager.openFile(pageId.filename());
                pagedFile.writePage(page.getPageNum(), page.getData());
                page.flush();
            }
        }
    }
}
