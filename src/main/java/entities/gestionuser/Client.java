package entities.gestionuser;

public class Client extends User {

    public Client() {
    }

    public Client(int id, String username, String firstname, String lastname, String date_naiss, String password, String email, String num_tel, String adresse, String photo) {
        super(id, username, firstname, lastname, date_naiss, password, email, num_tel, adresse, photo);
    }
}
