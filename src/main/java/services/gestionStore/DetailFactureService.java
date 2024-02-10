package services.gestionStore;

import entities.gestionStore.detailfacture;

import java.sql.SQLException;
import java.util.List;

import entities.gestionStore.facture;
import entities.gestionStore.produit;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
public class DetailFactureService implements IService {

    private final Connection connection;

    public DetailFactureService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override


    public void add(Object o) throws SQLException {
        detailfacture p = (detailfacture) o;
        // Vérifier si la facture existe
        String checkFactureExistsQuery = "SELECT idFacture FROM facture WHERE idFacture = ?";
        PreparedStatement checkFactureExistsStatement = connection.prepareStatement(checkFactureExistsQuery);
        checkFactureExistsStatement.setInt(1, p.getIdFacture());
        ResultSet factureResultSet = checkFactureExistsStatement.executeQuery();

        if (!factureResultSet.next())
        {
            throw new SQLException("La facture avec l'ID spécifié n'existe pas.");
        }

        // Vérifier si le produit existe et récupérer son prix de vente unitaire
        String produitQuery = "SELECT prix FROM produit WHERE idProduit = ?";
        PreparedStatement produitStatement = connection.prepareStatement(produitQuery);
        produitStatement.setInt(1, p.getIdProduit());
        ResultSet produitResultSet = produitStatement.executeQuery();

        if (!produitResultSet.next()) {
            throw new SQLException("Le produit avec l'ID spécifié n'existe pas.");
        }

        float prixVenteUnitaire = produitResultSet.getFloat("prix");

        // Calculer le prix total de l'article
        float prixTotalArticle = prixVenteUnitaire * p.getQuantite() * (1-p.getTauxRemise());


        // Insérer les détails de la facture
        String sql = "INSERT INTO detailfacture (idFacture, idDetailFacture, idProduit, prixVenteunitaire, quantite, tauxRemise, prixTotalArticle) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, p.getIdFacture());
        preparedStatement.setInt(2, p.getIdDetailFacture());
        preparedStatement.setInt(3, p.getIdProduit());
        preparedStatement.setFloat(4, prixVenteUnitaire);
        preparedStatement.setInt(5, p.getQuantite());

        ProduitService prd = new ProduitService();
        prd.MAJ_Stock(p.getQuantite() , p.getIdProduit());

        preparedStatement.setFloat(6, p.getTauxRemise());
        preparedStatement.setFloat(7, prixTotalArticle);

        preparedStatement.executeUpdate();

        // Fermer les ressources
        preparedStatement.close();
        factureResultSet.close();
        produitResultSet.close();
        checkFactureExistsStatement.close();
        produitStatement.close();
    }


    @Override
    public void update(Object o) throws SQLException {
        detailfacture p = (detailfacture) o;
        // Vérifier si la facture associée à ce détail existe
        String checkFactureExistsQuery = "SELECT idFacture FROM facture WHERE idFacture = ?";
        PreparedStatement checkFactureExistsStatement = connection.prepareStatement(checkFactureExistsQuery);
        checkFactureExistsStatement.setInt(1, p.getIdFacture());
        ResultSet factureResultSet = checkFactureExistsStatement.executeQuery();

        if (!factureResultSet.next()) {
            throw new SQLException("La facture avec l'ID spécifié n'existe pas.");
        }

        // Vérifier si le produit existe et récupérer son prix de vente unitaire
        String produitQuery = "SELECT prix FROM produit WHERE idProduit = ?";
        PreparedStatement produitStatement = connection.prepareStatement(produitQuery);
        produitStatement.setInt(1, p.getIdProduit());
        ResultSet produitResultSet = produitStatement.executeQuery();

        if (!produitResultSet.next()) {
            throw new SQLException("Le produit avec l'ID spécifié n'existe pas.");
        }

        float prixVenteUnitaire = produitResultSet.getFloat("prix");

        // Calculer le prix total de l'article
        float prixTotalArticle = prixVenteUnitaire * p.getQuantite() * (1- p.getTauxRemise());

        // Mettre à jour les détails de la facture
        String sql = "UPDATE detailfacture SET prixVenteunitaire = ?, quantite = ?, tauxRemise = ?, prixTotalArticle = ? WHERE idFacture = ? AND idDetailFacture = ? AND idProduit = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setFloat(1, prixVenteUnitaire);
        preparedStatement.setInt(2, p.getQuantite());
        preparedStatement.setFloat(3, p.getTauxRemise());
        preparedStatement.setFloat(4, prixTotalArticle);
        preparedStatement.setInt(5, p.getIdFacture());
        preparedStatement.setInt(6, p.getIdDetailFacture());
        preparedStatement.setInt(7, p.getIdProduit());

        preparedStatement.executeUpdate();

        // Fermer les ressources
        preparedStatement.close();
        factureResultSet.close();
        produitResultSet.close();
        checkFactureExistsStatement.close();
        produitStatement.close();
    }


    @Override
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM detailfacture WHERE idDetailFacture = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<detailfacture> getAll() throws SQLException {
        String sql = "select * from detailfacture";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<detailfacture> detailfacture = new ArrayList<>();
        while (rs.next())
        {
            detailfacture p = new detailfacture();
            p.setIdFacture(rs.getInt("idFacture"));
            p.setIdDetailFacture(rs.getInt("idDetailFacture"));
            p.setIdProduit(rs.getInt("idProduit"));
            p.setPrixVenteUnitaire(rs.getFloat("prixVenteUnitaire"));
            p.setQuantite(rs.getInt("quantite"));
            p.setTauxRemise(rs.getFloat("tauxRemise"));
            p.setPrixtotalArticle(rs.getFloat("prixTotalArticle"));
            detailfacture.add(p);
        }
        return detailfacture;
    }

    public List<detailfacture> getDetailFacture(int id) throws SQLException {
        String sql = "select * from detailfacture WHERE idFacture = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        List<detailfacture> detailfacture = new ArrayList<>();
        while (rs.next())
        {
            detailfacture p = new detailfacture();
            p.setIdFacture(rs.getInt("idFacture"));
            p.setIdDetailFacture(rs.getInt("idDetailFacture"));
            p.setIdProduit(rs.getInt("idProduit"));
            p.setPrixVenteUnitaire(rs.getFloat("prixVenteUnitaire"));
            p.setQuantite(rs.getInt("quantite"));
            p.setTauxRemise(rs.getFloat("tauxRemise"));
            p.setPrixtotalArticle(rs.getFloat("prixTotalArticle"));
            detailfacture.add(p);
        }
        rs.close();
        return detailfacture;
    }
}


