package entities.gestionuser;

public class Staff extends User {
    private final String role = "staff";

    public Staff() {
    }

    public Staff(int id, String username, String firstname, String lastname, String date_naiss, String password, String email, String num_tel, String adresse, String photo) {
        super(id, username, firstname, lastname, date_naiss, password, email, num_tel, adresse, photo);
    }

    public String getRole() {
        return role;
    }

}
