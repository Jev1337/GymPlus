package test;

import entities.*;
import services.*;
import utils.MyDatabase;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        UserService userService = new UserService();
        try {
            userService.add(new User(2, "user1", "user1", "user1", "2021-01-01", "password","user@user.user", "user", "user", "12345678", "user1", "user1"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
