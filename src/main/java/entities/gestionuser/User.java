package entities.gestionuser;

public class User {
    /*
    Database Schema:
       `id` int(11) NOT NULL,
      `username` varchar(255) DEFAULT NULL,
      `firstname` varchar(255) DEFAULT NULL,
      `lastname` varchar(255) DEFAULT NULL,
      `date_naiss` date DEFAULT NULL,
      `password` varchar(255) DEFAULT NULL,
      `email` varchar(255) DEFAULT NULL,
      `role` varchar(255) DEFAULT NULL,
      `num_tel` varchar(255) DEFAULT NULL,
      `adresse` text DEFAULT NULL,
      `photo` text DEFAULT NULL
     */
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String date_naiss;
    private String password;
    private String email;
    private String num_tel;
    private String adresse;
    private String photo;
    private String role;
    private int event_points;
    private String faceid;
    private String faceid_ts;

    public User() {
    }

    public User(int id, String username, String firstname, String lastname, String date_naiss, String password, String email, String num_tel, String adresse, String photo, String role, String faceid, String faceid_ts) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date_naiss = date_naiss;
        this.password = password;
        this.email = email;
        this.num_tel = num_tel;
        this.adresse = adresse;
        this.photo = photo;
        this.role = role;
        this.event_points = 0;
        this.faceid = faceid;
        this.faceid_ts = faceid_ts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDate_naiss() {
        return date_naiss;
    }

    public void setDate_naiss(String date_naiss) {
        this.date_naiss = date_naiss;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(String num_tel) {
        this.num_tel = num_tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public void setEvent_points(int event_points) {
        this.event_points = event_points;
    }
    public int getEvent_points() {
        return event_points;
    }

    public String getFaceid() {
        return faceid;
    }

    public void setFaceid(String faceid) {
        this.faceid = faceid;
    }

    public String getFaceid_ts() {
        return faceid_ts;
    }

    public void setFaceid_ts(String faceid_ts) {
        this.faceid_ts = faceid_ts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", lastname='" + lastname + '\'' +
                ", date_naiss='" + date_naiss + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", num_tel='" + num_tel + '\'' +
                ", adresse='" + adresse + '\'' +
                ", photo='" + photo + '\'' +
                ", role='" + role + '\'' +
                ", event_points='" + event_points + '\'' +
                ", faceid='" + faceid + '\'' +
                ", faceid_ts='" + faceid_ts + '\'' +
                '}';
    }
}
