package database;

public class PageManager {

    private final FileManager fileManager;

    public PageManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Page loadPage(PageId pageId) {
        return fileManager.readPage(pageId);
    }

    public void savePage(PageId pageId, Page page) {
        fileManager.writePage(pageId, page);
    }
}
