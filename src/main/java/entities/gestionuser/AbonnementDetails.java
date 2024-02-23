package entities.gestionuser;

public class AbonnementDetails {
    private String type;
    private double prix;

    public AbonnementDetails() {
    }

    public AbonnementDetails(String type, double prix) {
        this.type = type;
        this.prix = prix;
    }

    public String getType() {
        return type;
    }

    public double getPrix() {
        return prix;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
