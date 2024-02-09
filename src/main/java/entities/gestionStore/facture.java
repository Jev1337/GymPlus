package entities.gestionStore;

import java.util.Date;

public class facture {

    private int idFacture;
    private Date dateVente;
    private float prixtatalPaye;
    private String methodeDePaiement;
    private int id;

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public Date getDateVente() {
        return dateVente;
    }

    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
    }

    public float getPrixtotalPaye() {
        return prixtatalPaye;
    }

    public void setPrixtotalPaye(float prixtotalPaye) {
        this.prixtatalPaye = prixtotalPaye;
    }

    public String getMethodeDePaiement() {
        return methodeDePaiement;
    }

    public void setMethodeDePaiement(String methodeDePaiement) {
        this.methodeDePaiement = methodeDePaiement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public facture() {
    }

    public facture(int idFacture, Date dateVente, float prixtotalPaye, String methodeDePaiement, int id) {
        this.idFacture = idFacture;
        this.dateVente = dateVente;
        this.prixtatalPaye = prixtotalPaye;
        this.methodeDePaiement = methodeDePaiement;
        this.id = id;
    }

    public facture(Date dateVente, float prixtotalPaye, String methodeDePaiement, int id) {
        this.dateVente = dateVente;
        this.prixtatalPaye = prixtotalPaye;
        this.methodeDePaiement = methodeDePaiement;
        this.id = id;
    }

    public facture(float prixtotalPaye, String methodeDePaiement, int id) {
        this.prixtatalPaye = prixtotalPaye;
        this.methodeDePaiement = methodeDePaiement;
        this.id = id;
    }

    @Override
    public String toString() {
        return "facture{" +
                "idFacture=" + idFacture +
                ", dateVente=" + dateVente +
                ", prixtotalPaye=" + prixtatalPaye +
                ", methodeDePaiement='" + methodeDePaiement + '\'' +
                ", id=" + id +
                '}';
    }
}
