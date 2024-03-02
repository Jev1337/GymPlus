package entities.gestionStore;

public class Livraison {

    private int idLivraison;
    private int idFacture;
    private int idClient;
    private String Lieu;
    private String etat;

    public int getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(int idLivraison) {
        this.idLivraison = idLivraison;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getLieu() {
        return Lieu;
    }

    public void setLieu(String lieu) {
        Lieu = lieu;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Livraison() {
    }

    public Livraison(int idFacture, int idClient, String lieu, String etat) {
        this.idFacture = idFacture;
        this.idClient = idClient;
        Lieu = lieu;
        this.etat = etat;
    }

    public Livraison(int idLivraison, int idFacture, int idClient, String lieu, String etat) {
        this.idLivraison = idLivraison;
        this.idFacture = idFacture;
        this.idClient = idClient;
        Lieu = lieu;
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Livraison{" +
                "idLivraison=" + idLivraison +
                ", idFacture=" + idFacture +
                ", idClient=" + idClient +
                ", Lieu='" + Lieu + '\'' +
                ", etat='" + etat + '\'' +
                '}';
    }
}
