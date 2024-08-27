package database.page;

import java.io.IOException;
import java.util.Map;

public class BufferManager {
    private final int bufferSize;
    private final Map<PageId, Page> pageBuffer;
    private final LRUList lruList;
    private final PagedFileManager pagedFileManager;

    public BufferManager(
            int bufferSize,
            Map<PageId, Page> pageBuffer,
            LRUList lruList,
            PagedFileManager pagedFileManager
    ) {
        this.bufferSize = bufferSize;
        this.pageBuffer = pageBuffer;
        this.lruList = lruList;
        this.pagedFileManager = pagedFileManager;
    }

    public Page getPage(PageId pageId) throws IOException {
        if (pageBuffer.containsKey(pageId)) {
            lruList.moveToFront(pageId);
            return pageBuffer.get(pageId);
        }

        PagedFile pagedFile = pagedFileManager.openFile(pageId.filename());

        Page page = pagedFile.readPage(pageId.pageNum());
        if (pageBuffer.size() >= bufferSize) {
            evictPage();
        }

        pageBuffer.put(pageId, page);
        lruList.add(pageId);
        return page;
    }

    private void evictPage() throws IOException {
        PageId pageIdToEvict = lruList.evict();
        Page pageToEvict = pageBuffer.remove(pageIdToEvict);
        String filename = pageIdToEvict.filename();
        if (pageToEvict.isDirty()) {
            PagedFile pagedFile = pagedFileManager.openFile(filename);
            pagedFile.writePage(pageToEvict.getPageNum(), pageToEvict.getData());
        }

        boolean fileNotReferenced = pageBuffer.keySet().stream()
                .map(PageId::filename)
                .noneMatch(filename::equals);
        if (fileNotReferenced) {
            pagedFileManager.closeFile(pageIdToEvict.filename());
        }
    }

    public void flushPage(PageId pageId) throws IOException {
        if (pageBuffer.containsKey(pageId)) {
            Page page = pageBuffer.get(pageId);
            if (page.isDirty()) {
                PagedFile pagedFile = pagedFileManager.openFile(pageId.filename());
                pagedFile.writePage(pageId.pageNum(), page.getData());
                page.flush();
            }
        }
    }

    public void flushAllPages() throws IOException {
        for (PageId pageId : pageBuffer.keySet()) {
            Page page = pageBuffer.get(pageId);
            if (page.isDirty()) {
                PagedFile pagedFile = pagedFileManager.openFile(pageId.filename());
                pagedFile.writePage(page.getPageNum(), page.getData());
                page.flush();
            }
        }
    }
}
