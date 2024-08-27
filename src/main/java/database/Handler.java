package database;

public interface Handler {

    void insert(byte[] record);

    byte[] search(Object key);

    void update(Object key, byte[] newRecord);

    void delete(Object key);
}

