package database.engine;

import java.util.List;

public interface Handler {

    void insert(byte[] record);

    List<Record> search(Object key);

    void update(Object key, byte[] newRecord);

    void delete(Object key);
}

