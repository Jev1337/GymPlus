package test;

import entities.gestionequipements.Equipements_details;
import services.gestionequipements.EquipementService;

import java.sql.SQLException;
import java.util.Scanner;

public class EquipementsTest {
        public static void main(String[] args) {
            EquipementService Es= new EquipementService();
            try{
                Es.add(new Equipements_details(2,"laptop","hp","2 years","good"));

                Es.getAll();
                System.out.println(Es.getAll());
                Scanner sc = new Scanner(System.in);
                System.out.println("Would you like to update? (yes/no)");
                String answer = sc.nextLine();
                if (answer.equals("yes")) {
                    Es.update(new Equipements_details(2, "laptop", "hp", "2 years", "bad"));
                    System.out.println(Es.getAll());
                }
                System.out.println("Would you like to delete? (yes/no)");
                answer = sc.nextLine();
                if (answer.equals("yes")) {
                    Es.delete(2);
                    System.out.println(Es.getAll());
                }

            }catch  (SQLException e) {
                throw new RuntimeException(e);
            }
        }


}
