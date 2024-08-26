package database.innodb.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageDirectory implements Serializable {

    private final List<Integer> recordPositions;

    public PageDirectory() {
        this.recordPositions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "PageDirectory{" +
                "recordPositions=" + recordPositions +
                '}';
    }
}
