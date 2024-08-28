package database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private static final String DIRECTORY_PATH = "data/files/";
    private static final String FILE_EXTENSION = ".ibd";

    public FileManager() {
        createDirectory();
    }

    private void createDirectory() {
        Path directory = Paths.get(DIRECTORY_PATH);
        if (Files.notExists(directory)) {
            try {
                Files.createDirectory(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Page readPage(PageId pageId) {
        Path filePath = Paths.get(DIRECTORY_PATH, pageId.getFileName() + FILE_EXTENSION);

        try (RandomAccessFile file = new RandomAccessFile(filePath.toString(), "r")) {
            long offset = (long) pageId.getPageNum() * Page.PAGE_SIZE;
            file.seek(offset);

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getFD()))) {
                return (Page) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void writePage(PageId pageId, Page page) {
        Path filePath = Paths.get(DIRECTORY_PATH, pageId.getFileName() + FILE_EXTENSION);

        try (RandomAccessFile file = new RandomAccessFile(filePath.toString(), "rw")) {
            long offset = (long) pageId.getPageNum() * Page.PAGE_SIZE;
            file.seek(offset);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.getFD()))) {
                oos.writeObject(page);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
