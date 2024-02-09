package entities;

/*
Database Schema:
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `duree_abon` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
 */
public class Abonnement {

    private int id;
    private int user_id;
    private String duree_abon;
    private String type;

    public Abonnement() {
    }

    public Abonnement(int id, int user_id, String duree_abon, String type) {
        this.id = id;
        this.user_id = user_id;
        this.duree_abon = duree_abon;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDuree_abon() {
        return duree_abon;
    }

    public void setDuree_abon(String duree_abon) {
        this.duree_abon = duree_abon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
