package database;

import database.page.Page;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BufferPool {

    private static final int BUFFER_SIZE = 40;

    private static final Map<Integer, Page> bufferPool;

    static {
        bufferPool = new LinkedHashMap<>(BUFFER_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Entry<Integer, Page> eldest) {
                return size() > BUFFER_SIZE && !eldest.getValue().isPinned();
            }
        };
    }

}
