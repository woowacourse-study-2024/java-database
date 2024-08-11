package database;

public class Application {

    public static void main(String[] args) {
        DatabaseServer server = new DatabaseServer(3306);
        server.start();
    }
}
