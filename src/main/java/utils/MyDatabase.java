package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String URL = "jdbc:mysql://localhost:3306/gymplus";
    private final String USER = "root";
    private final String PASS = "";
    private Connection connection;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null)
            instance = new MyDatabase();
        else
            System.out.println("Database already connected");
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
