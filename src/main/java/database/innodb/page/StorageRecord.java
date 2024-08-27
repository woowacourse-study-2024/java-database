package database.innodb.page;

import java.io.Serializable;
import java.util.Arrays;

public class StorageRecord implements Serializable {

    private final byte[] data;

    public StorageRecord(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }

    @Override
    public String toString() {
        return "StorageRecord{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
