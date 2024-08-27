package database.page;

import java.util.LinkedList;

public class LRUList {

    private final LinkedList<PageId> lruList;

    public LRUList(LinkedList<PageId> lruList) {
        this.lruList = lruList;
    }

    public void add(PageId pageId) {
        lruList.addFirst(pageId);
    }

    public void moveToFront(PageId pageId) {
        lruList.remove(pageId);
        lruList.addFirst(pageId);
    }

    public PageId evict() {
        return lruList.removeLast();
    }
}
