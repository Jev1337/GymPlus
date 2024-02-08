package test;

import entities.*;
import services.*;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        UserService userService = new UserService();
        try {
            userService.add(new User(4, "user1", "user1", "user1", "2001-01-01", "user1", "user1" , "user1", "user1", "user1", "user1", "user1"));
            System.out.println("User added successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
