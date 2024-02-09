package test;
import entities.gestionSuivi.Objectif;
import services.gestionSuivi.ObjectifService;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class Suivi_Test {

    public static void main(String[] args) {
        ObjectifService objectifService = new ObjectifService() ;
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDureeNow =java.sql.Date.valueOf(currentDate);
        Objectif obj = new Objectif();
        Objectif obj1 = new Objectif(20,sqlDureeNow,100,1,"none","Default",3);
        Objectif obj2 = new Objectif(21,sqlDureeNow,90,89,"none","Default",90);

        System.out.println("adding this objectif");
        try {
            objectifService.add(obj1);
            objectifService.add(obj2);
            System.out.println("Done perfectly");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Would you like to delete the objectif?");
            String response = scanner.nextLine();
            if (response.equals("yes")) {
             objectifService.delete(1);
             System.out.println("Deleted1 perfectly");
            }
            System.out.println("Would you like to update the data? ");
             response = scanner.nextLine();
             if (response.equals("yes")){
                 obj1.setPoids_Obj(200);
                 String dateString = "2024-02-09";
                 java.sql.Date dateD = java.sql.Date.valueOf(dateString);
                 obj1.setDateF(dateD);
                 obj1.setPoids_Act(50);
                 obj1.setTaille(15);
                 obj1.setTypeObj("Version +");
                 obj1.setCoachId(1);
                 objectifService.update(obj1);

             }


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
