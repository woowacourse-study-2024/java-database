package database.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PageManager {

    private static final String DIRECTORY_PATH = "disk";
    private static final String INFIX = "page_";
    private static final String FILE_EXTENSION = ".ibd";

    public PageManager() {
        createDirectoryIfNotExists();
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

    public void savePage(Page page) {
        String fileName = DIRECTORY_PATH + File.separator + INFIX + page.getPageNumber() + FILE_EXTENSION;
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Page loadPage(long pageNum) {
        String fileName = DIRECTORY_PATH + File.separator + INFIX + pageNum + FILE_EXTENSION;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            Page page = (Page) in.readObject();
            return page;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
