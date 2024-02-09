package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private String URL = "jdbc:mysql://localhost:3306/projet_pi";
    private String USER = "root";
    private String PASS = "";

    private Connection connection;

    private static MyDataBase instance;
    public static MyDataBase getInstance(){
        if (instance == null)
            instance = new MyDataBase();
        return instance;
    }
    public MyDataBase() {
        try {
            connection = DriverManager.getConnection(URL,USER,PASS);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
