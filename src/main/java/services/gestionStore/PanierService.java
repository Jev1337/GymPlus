package services.gestionStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import entities.gestionStore.produit;
import entities.gestionuser.User;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.MyDatabase;


public class PanierService
{
    private facture MonPanier = new facture();

    public facture getMonPanier() {
        return MonPanier;
    }

    public void setMonPanier(facture monPanier) {
        MonPanier = monPanier;
    }

    public PanierService(facture monPanier)
    {
        MonPanier = monPanier;
    }
    public ProduitService prodService = new ProduitService();
    public PanierService( ){}


    public void AjouterProduit(int idp , int Quantite , float Remise ) throws SQLException    //produit p , int Quantite , float Remise , int idDetail)
    {
        detailfacture df = new detailfacture();
        produit p = prodService.getOne(idp);

        df.setIdProduit(p.getIdProduit());
        df.setPrixVenteUnitaire(p.getPrix());
        df.setQuantite(Quantite);
        df.setTauxRemise(Remise);
        df.setPrixtotalArticle(p.getPrix() * Quantite * (1- Remise));
        System.out.println(df.getPrixtotalArticle());

        MonPanier.ListeDetails.add(df);
        MonPanier.calculerPrixTotalFacture();
        Afficher();

         /*

        produit p = null;
        try
        {
            // Récupérer le produit correspondant à l'id depuis le service des produits
            p = prodService.getOne(idp);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        if (p != null)
        {
            detailfacture df = new detailfacture();
            df.setIdProduit(idp);
            df.setPrixVenteUnitaire(p.getPrix());
            df.setQuantite(Quantite);
            df.setTauxRemise(Remise);
            // Calculer le prix total de l'article
            float prixTotalArticle = p.getPrix() * Quantite * (1 - Remise);
            df.setPrixtotalArticle(prixTotalArticle);
            df.setIdDetailFacture(idDetail);
            df.setIdFacture(idFacture);

            MonPanier.ListeDetails.add(df);
            MonPanier.calculerPrixTotalFacture();
            Afficher();
        } else
        {
            System.out.println("Produit non trouvé avec l'ID : " + idp);
        }

        //chargerContenuPanier();

          */

    }
/*
    private ObservableList<detailfacture> contenuPanier = FXCollections.observableArrayList();
    private void chargerContenuPanier()
    {
        contenuPanier.clear(); // Nettoyer le contenu actuel du panier
        contenuPanier.addAll(MonPanier.ListeDetails); // Ajouter les nouveaux éléments au panier
    }

 */

    public void Afficher()
    {
        for (detailfacture df : MonPanier.ListeDetails)
        {
            System.out.println(df.toString());
        }
    }

    /*
    public void RetirerProduit(int indice)
    {
        MonPanier.ListeDetails.remove(indice);
        MonPanier.calculerPrixTotalFacture();
        //chargerContenuPanier();
    }

     */

    public void RetirerProduit(int index)
    {
        System.out.println("indexe a supprimer envouye  : " + index);
        System.out.println("panierrr  : " + MonPanier.ListeDetails.size());

        if (!MonPanier.ListeDetails.isEmpty() && index >= 0 && index < MonPanier.ListeDetails.size())
        {

            MonPanier.ListeDetails.remove(index);
            System.out.println("Taille de la liste après supp : " + MonPanier.ListeDetails.size());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Produit retiré avec succès");
            alert.showAndWait();
        } else
        {
            System.err.println("Erreur");
        }
    }

    private static final Logger logger = Logger.getLogger(PanierService.class.getName());
    public List<detailfacture> getContenuPanier()
    {
        //return MonPanier.ListeDetails;

        logger.info("La méthode getContenuPanier() a été invoquée.");
        List<detailfacture> contenuPanier = MonPanier.ListeDetails;
        logger.info("Le contenu du panier est : " + contenuPanier);
        return contenuPanier;
    }

    public void Valider()
    {
        try
        {
            FactureService fs = new FactureService();
            fs.add(MonPanier);
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void ModifierProduit(produit p , int Quantite , float Remise , int idDetail)
    {
        detailfacture df = MonPanier.ListeDetails.get(idDetail);
        df.setPrixVenteUnitaire(p.getPrix());
        df.setQuantite(Quantite);
        df.setTauxRemise(Remise);
        df.setPrixtotalArticle(p.getPrix() * Quantite * (1- Remise));
        df.setIdDetailFacture(idDetail);

        MonPanier.calculerPrixTotalFacture();
    }

    public void RattacherClient(User client)
    {
        MonPanier.setLeClient(client);
        MonPanier.setId(client.getId());
    }


/*
    public facture getDerniereFacture() throws SQLException
    {
        facture p = new facture();
        String sqlMaxId = "Select Max(idFacture) as max FROM facture ";
        try (PreparedStatement getMax = connection.prepareStatement(sqlMaxId))
        {
            ResultSet resultSetmax = getMax.executeQuery();
            if (resultSetmax.next())
            {
                int x = resultSetmax.getInt("max") ;
                p.setIdFacture(x);
                System.out.println("max de la colonne idFacture" + x );
            }
        }
        return p;
    }

 */
}
