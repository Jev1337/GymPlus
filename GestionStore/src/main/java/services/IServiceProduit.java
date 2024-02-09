package services;

import java.sql.SQLException;
import entities.produit;
import java.util.List;

public interface IServiceProduit {

    void ajouterProduit (produit p) throws SQLException;
    void modifierProduit (produit p) throws SQLException;
    void supprimerProduit(int id) throws SQLException;
    List<produit> recupererProduit() throws SQLException;

}
