package database.page;

import java.util.LinkedList;

public class LRUList {

    private final LinkedList<Integer> lruList;

    public LRUList(LinkedList<Integer> lruList) {
        this.lruList = lruList;
    }

    public void add(int pageNum) {
        lruList.addFirst(pageNum);
    }

    public void moveToFront(int pageNum) {
        lruList.remove(Integer.valueOf(pageNum));
        lruList.addFirst(pageNum);
    }

    public int evict() {
        return lruList.removeLast();
    }
}
