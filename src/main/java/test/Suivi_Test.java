package test;

import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
import services.gestionSuivi.ObjectifService;
import services.gestionSuivi.PlanningService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class Suivi_Test {

    public static void main(String[] args) throws SQLException {

        //Testing Planning in interface Coach and User    **(update,delete,add) for Coach
        //   **(Display Data) for User
        Scanner scanner = new Scanner(System.in);
        System.out.println("Moving to the Coach Interface");
        PlanningService planningService = new PlanningService();
        System.out.println(planningService.getAll().toString());
        Planning plan1 = new Planning(94, "ARM DAY", "Keto diet");
        System.out.println("Do you want to add a plan \n");
        String choix = scanner.next();
        if (choix.contains("yes")) {
            planningService.add(plan1);
            System.out.println(planningService.getAll().toString());
        }
        System.out.println("Do you want to Delete a plan \n");
        String choix2 = scanner.nextLine();
        if (choix2.contains("yes")) {
            System.out.println(planningService.getAll().toString());
            System.out.println("enter an Id of the Plan you want to delete \n");
            int ID_plan = scanner.nextInt();
            planningService.delete(ID_plan);
            System.out.println(planningService.getAll().toString());

        }

        System.out.println("Do you want to Update a plan \n");
        choix2 = scanner.nextLine();
        if (choix2.contains("yes")) {
            System.out.println(planningService.getAll().toString());
            System.out.println("enter an Id of the Plan you want to update \n");
            int ID_plan = scanner.nextInt();
            plan1.setId_Planning(ID_plan);
            plan1.setTrainingProg("test");
            plan1.setFoodProg("test");
            planningService.update(plan1);

            System.out.println(planningService.getAll().toString());

        }


        //Testing Objectif  in interface  User
        int userId = 2;
        ObjectifService objectifService = new ObjectifService();
        System.out.println("gessing you are the user with the ( ID =2 ) These are the objectif you already has \n");
        System.out.println(objectifService.getAll().toString());


        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDureeNow = java.sql.Date.valueOf(currentDate);
        Objectif obj1 = new Objectif(userId, 20, sqlDureeNow, 100, 1, "none", "Default", 1);
        obj1.setDateD(sqlDureeNow);
        Objectif obj2 = new Objectif(userId, 21, sqlDureeNow, 90, 89, "none", "Default", 90);
        obj2.setDateD(sqlDureeNow);


        System.out.println(" Would you like to add an Objectif ? (yes/no)\n");

        String response = scanner.nextLine();
        try {
            if (response.equals("yes")) {
                objectifService.add(obj1);
                objectifService.add(obj2);
                System.out.println("Done adding obejctif perfectly ");
                System.out.println(objectifService.getAll().toString());
            }


            System.out.println("Would you like to delete the Objectif ? (yes/no)\n");

            response = scanner.nextLine();
            if (response.equals("yes")) {
                System.out.println("Enter id objectif from the list displayed for you fot the DELETE ? ");
                System.out.println(objectifService.getAll().toString());
                int ID_obj = scanner.nextInt();
                objectifService.delete(ID_obj);
                System.out.println("Deleted perfectly \n");
                System.out.println(objectifService.getAll().toString());
            }


            System.out.println("Would you like to update the Objectif ?  (yes/no) \n");

            String response2 = scanner.next();
            if (response2.equals("yes")) {
                System.out.println("Enter id objectif from the list displayed for you for the UPDATE ? ");
                System.out.println(objectifService.getAll().toString());
                int ID_obj2 = scanner.nextInt();
                // obj1.setId_user(ID_obj2);
                obj1.setId_objectif(ID_obj2);
                obj1.setPoids_Obj(200);
                String dateString = "2024-02-09";
                java.sql.Date dateD = java.sql.Date.valueOf(dateString);
                obj1.setDateD(dateD);
                obj1.setDateF(dateD);
                obj1.setPoids_Act(50);
                obj1.setTaille(15);
                obj1.setTypeObj("Version +");
                obj1.setCoachId(1);
                objectifService.update(obj1);
                System.out.println(objectifService.getAll().toString());


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
