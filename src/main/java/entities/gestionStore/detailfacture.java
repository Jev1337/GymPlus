package entities.gestionStore;

import services.gestionStore.ProduitService;

import java.sql.SQLException;

public class detailfacture {

    private int idFacture;
    private int idDetailFacture;
    private int idProduit;
    private float prixVenteUnitaire;
    private int quantite;
    private float tauxRemise;
    private float prixTotalArticle;


    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public int getIdDetailFacture() {
        return idDetailFacture;
    }

    public void setIdDetailFacture(int idDetailFacture) {
        this.idDetailFacture = idDetailFacture;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public float getPrixVenteUnitaire() {
        return prixVenteUnitaire;
    }

    public void setPrixVenteUnitaire(float prixVenteUnitaire) {
        this.prixVenteUnitaire = prixVenteUnitaire;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public float getTauxRemise() {
        return tauxRemise;
    }

    public void setTauxRemise(float tauxRemise) {
        this.tauxRemise = tauxRemise;
    }

    public float getPrixtotalArticle() {
        return prixTotalArticle;
    }

    public void setPrixtotalArticle(float prixtotalArticle) {
        this.prixTotalArticle = prixtotalArticle;
    }

    public detailfacture() {
    }

    public detailfacture(int idFacture, int idDetailFacture, int idProduit, float prixVenteUnitaire, int quantite, float tauxRemise, float prixtotalArticle) {
        this.idFacture = idFacture;
        this.idDetailFacture = idDetailFacture;
        this.idProduit = idProduit;
        this.prixVenteUnitaire = prixVenteUnitaire;
        this.quantite = quantite;
        this.tauxRemise = tauxRemise;
        this.prixTotalArticle = prixtotalArticle;
    }

    public detailfacture(int idFacture, int idDetailFacture, int idProduit, int quantite, float tauxRemise) {
        this.idFacture = idFacture;
        this.idDetailFacture = idDetailFacture;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.tauxRemise = tauxRemise;
    }

    @Override
    public String toString() {
        return "detailfacture{" +
                "idFacture=" + idFacture +
                ", idDetailFacture=" + idDetailFacture +
                ", idProduit=" + idProduit +
                ", prixVenteUnitaire=" + prixVenteUnitaire +
                ", quantite=" + quantite +
                ", tauxRemise=" + tauxRemise +
                ", prixtotalArticle=" + prixTotalArticle +
                '}';
    }


    public produit getProduit() throws SQLException
    {
        ProduitService produitService = new ProduitService();
        return produitService.getProduitById(idProduit);
    }
}
