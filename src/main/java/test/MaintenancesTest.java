package test;

import entities.gestionequipements.Maintenances;
import services.gestionequipements.MaintenancesService;


import java.sql.SQLException;
import java.util.Scanner;

public class MaintenancesTest {

    public static void main(String[] args) {
        MaintenancesService Ms= new MaintenancesService();
        try {
            Ms.add(new Maintenances(1, 2, "2021-05-05", "good"));
            Ms.getAll();
            System.out.println(Ms.getAll());
            System.out.println("Would you like to update? (yes/no)");
            Scanner sc = new Scanner(System.in);
            String answer = sc.nextLine();
            if (answer.equals("yes")) {
                Ms.update(new Maintenances(1, 2, "2021-05-05", "bad"));
                System.out.println(Ms.getAll());
            }
            System.out.println("Would you like to delete? (yes/no)");
            answer = sc.nextLine();
            if (answer.equals("yes")) {
                Ms.delete(1);
                System.out.println(Ms.getAll());
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
