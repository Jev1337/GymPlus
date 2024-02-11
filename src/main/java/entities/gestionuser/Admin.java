package entities.gestionuser;

public class Admin extends Staff {

    private final String role = "admin";

    public Admin() {
    }

    public Admin(int id, String username, String firstname, String lastname, String date_naiss, String password, String email, String poste, String num_tel, String adresse, String photo) {
        super(id, username, firstname, lastname, date_naiss, password, email, poste, num_tel, adresse, photo);
    }

    public String getRole() {
        return role;
    }

}
