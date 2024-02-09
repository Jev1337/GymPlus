package test;

import entities.Abonnement;
import entities.Admin;
import entities.Client;
import entities.Staff;
import services.AbonnementService;
import services.AdminService;
import services.ClientService;
import services.StaffService;

import java.sql.SQLException;
import java.util.Scanner;

public class UserModuleSample {

    public static void main(String[] args) {
        AdminService adminService = new AdminService();
        ClientService clientService = new ClientService();
        StaffService staffService = new StaffService();
        AbonnementService abonnementService = new AbonnementService();
        Admin admin = new Admin(10, "admin", "admin", "admin", "2002-02-02", "admin", "admin", "admin", "123", "admin", "admin");
        Client client = new Client(11, "client", "client", "client", "2002-02-02", "client", "client", "client", "123", "client");
        Staff staff = new Staff(12, "staff", "staff", "staff", "2002-02-02", "staff", "staff", "staff", "123", "staff", "staff");
        Abonnement abonnement = new Abonnement(12, 10, "1", "piscine");
        try {
            adminService.add(admin);
            clientService.add(client);
            staffService.add(staff);
            abonnementService.add(abonnement);
            System.out.println("Data inserted successfully");
            Scanner scanner = new Scanner(System.in);

            System.out.println("Would you like to update the data? (yes/no)");
            String response = scanner.nextLine();
            if (response.equals("yes")) {
                admin.setPoste("new poste");
                client.setAdresse("new adresse");
                staff.setPoste("new poste");
                abonnement.setDuree_abon("2");
                adminService.update(admin);
                clientService.update(client);
                staffService.update(staff);
                abonnementService.update(abonnement);
                System.out.println("Data updated successfully");
            }
            System.out.println("Would you like to delete the data? (yes/no)");
            response = scanner.nextLine();
            if (response.equals("yes")) {
                abonnementService.delete(12);
                adminService.delete(10);
                clientService.delete(11);
                staffService.delete(12);
                System.out.println("Data deleted successfully");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
