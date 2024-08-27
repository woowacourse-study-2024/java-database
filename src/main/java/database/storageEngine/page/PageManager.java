package database.storageEngine.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PageManager {

    private static final int PAGE_SIZE = 16 * 1024;
    private static final String DIRECTORY_PATH = "disk";
    private static final String FILE_EXTENSION = ".ibd";

    private final String tableName;
    private int pageSize;

    public PageManager(String tableName) {
        this.tableName = tableName;
        createDirectoryIfNotExists();
        createTableIfNotExists();
        this.pageSize = 0;
    }

    private void createDirectoryIfNotExists() {
        Path path = Paths.get(DIRECTORY_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTableIfNotExists() {
        String fileName = DIRECTORY_PATH + File.separator + tableName + FILE_EXTENSION;
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void savePage(Page page) {
        String fileName = DIRECTORY_PATH + File.separator + tableName + FILE_EXTENSION;

        try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
            file.seek(page.getPageNumber() * PAGE_SIZE);

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.getFD()))) {
                out.writeObject(page);
            }
            pageSize++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Page loadPage(long pageNum) {
        String fileName = DIRECTORY_PATH + File.separator + tableName + FILE_EXTENSION;

        try (RandomAccessFile file = new RandomAccessFile(fileName, "r")) {
            file.seek(pageNum * PAGE_SIZE);

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getFD()))) {
                Page page = (Page) in.readObject();
                return page;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNewPageNumber() {
        return pageSize;
    }
}