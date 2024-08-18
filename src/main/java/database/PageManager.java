package database;

import database.page.Page;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PageManager {
    public void saveToFile(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
            System.out.println("페이지가 " + fileName + "에 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Page loadFromFile(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            Page page = (Page) in.readObject();
            System.out.println("페이지가 " + fileName + "에서 로드되었습니다.");
            return page;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
