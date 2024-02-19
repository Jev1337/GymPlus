package services.gestionStore;

import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import java.sql.SQLException;
import java.util.List;

import entities.gestionStore.produit;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;

import java.util.Iterator;



public class FactureService implements IService
{

    private final Connection connection;
    public FactureService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void add(Object o) throws SQLException
    {
        facture p = (facture) o;
        // Vérifier si l'utilisateur avec l'ID  existe avant d'insérer la facture
        String checkUserExistsQuery = "SELECT id FROM user WHERE id = ?";
        PreparedStatement checkUserExistsStatement = connection.prepareStatement(checkUserExistsQuery);
        checkUserExistsStatement.setInt(1, p.getId());
        ResultSet resultSet = checkUserExistsStatement.executeQuery();

        if (!resultSet.next()) {
            throw new SQLException("L'utilisateur avec l'ID spécifié n'existe pas.");
        }

        // Calculer le prix total Facture
        float Total = p.calculerPrixTotalFacture();

        // Insérer la facture une fois que l'utilisateur existe
        String sql = "INSERT INTO facture (prixTatalPaye, methodeDePaiement, id) " +
                "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setFloat(1, Total); //p.getPrixtotalPaye());
        preparedStatement.setString(2, p.getMethodeDePaiement());
        preparedStatement.setInt(3, p.getId());

        preparedStatement.executeUpdate();

        String sqlMaxId = "Select Max(idFacture) as max FROM facture ";
        try (PreparedStatement getMax = connection.prepareStatement(sqlMaxId))
        {
            ResultSet resultSetmax = getMax.executeQuery();
            if (resultSetmax.next())
            { // Check if there are any results
                int x = resultSetmax.getInt("max") ;
                //p.setIdFacture(x);
                System.out.println("max de la colonne idFacture" + x );
                p.setIdFacture(x);
            }
        }

        DetailFactureService dfs = new DetailFactureService();
        int indice = 1;
        for (detailfacture df : p.ListeDetails)
        {
            df.setIdFacture(p.getIdFacture());
            df.setIdDetailFacture(indice);
            indice++;
            dfs.add(df);
        }

        //preparedStatement.executeUpdate();

        // Fermer les ressources
        preparedStatement.close();
        resultSet.close();
        checkUserExistsStatement.close();
    }

    public void update(Object o) throws SQLException
    {
        facture p = (facture) o;

        // Vérifier si la facture avec l'ID spécifié existe
        String checkFactureExistsQuery = "SELECT idFacture FROM facture WHERE idFacture = ?";
        try (PreparedStatement checkFactureExistsStatement = connection.prepareStatement(checkFactureExistsQuery))
        {
            checkFactureExistsStatement.setInt(1, p.getIdFacture());
            ResultSet resultSet = checkFactureExistsStatement.executeQuery();

            if (!resultSet.next())
            {
                throw new SQLException("La facture avec l'ID spécifié n'existe pas.");
            }
        }

        // Vérifier si l'utilisateur avec l'ID  existe avant d'insérer la facture
        String checkUserExistsQuery = "SELECT id FROM user WHERE id = ?";
        PreparedStatement checkUserExistsStatement = connection.prepareStatement(checkUserExistsQuery);
        checkUserExistsStatement.setInt(1, p.getId());
        ResultSet resultSet = checkUserExistsStatement.executeQuery();

        if (!resultSet.next()) {
            throw new SQLException("L'utilisateur avec l'ID spécifié n'existe pas.");
        }

        // Calculer le prix total de la facture
        float prixTotalPaye = p.calculerPrixTotalFacture();

        // Mettre à jour la facture
        String sql = "UPDATE facture SET prixTatalPaye = ?, methodeDePaiement = ? , id = ? WHERE idFacture = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFloat(1, prixTotalPaye);
            preparedStatement.setString(2, p.getMethodeDePaiement());
            preparedStatement.setInt(3, p.getId());
            preparedStatement.setInt(4, p.getIdFacture());
            preparedStatement.executeUpdate();
        }

        // Mettre à jour les détails de la facture
        DetailFactureService dfs = new DetailFactureService();
        for (detailfacture df : p.ListeDetails)
        {
            dfs.update(df);
        }
    }

    @Override
    public void delete(int id) throws SQLException
    {
        String sql = "DELETE FROM facture WHERE idFacture = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<facture> getAll() throws SQLException {
        String sql = "select * from facture";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<facture> facture = new ArrayList<>();
        DetailFactureService dfs = new DetailFactureService();
        while (rs.next())
        {
            facture p = new facture();
            p.setIdFacture(rs.getInt("idFacture"));
            p.setDateVente(rs.getDate("dateVente"));
            p.setPrixtotalPaye(rs.getFloat("prixTatalPaye"));
            p.setMethodeDePaiement(rs.getString("methodeDePaiement"));
            p.setId(rs.getInt("id"));

            p.ListeDetails = dfs.getDetailFacture(p.getIdFacture());

            facture.add(p);
        }
        return facture;
    }

    public facture getOne(int id) throws SQLException
    {
        String sql = "select * from facture WHERE idFacture = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        facture f = null;

       if (rs.next())
        {
            f = new facture();
            f.setIdFacture(rs.getInt("idFacture"));
            f.setDateVente(rs.getDate("dateVente"));
            f.setPrixtotalPaye(rs.getFloat("prixTatalPaye"));
            f.setMethodeDePaiement(rs.getString("methodeDePaiement"));
            f.setId(rs.getInt("id"));
        }
       rs.close();
       ps.close();
        return f;
    }

    public List<facture> ajouterDetails_Facture() throws SQLException {
        String sql = "SELECT * FROM facture";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<facture> factures = new ArrayList<>();
        DetailFactureService dfs = new DetailFactureService();
        while (rs.next())
        {
            facture p = new facture();
            p.setIdFacture(rs.getInt("idFacture"));
            p.ListeDetails=dfs.getDetailFacture(p.getIdFacture());
            factures.add(p);
        }
        return factures;
    }



}
