package database.engine;

import java.util.Arrays;

public class Record {

    private final byte[] data;

    public Record(byte[] data) {
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
        return "Record{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
