package services.gestionStore;

import entities.gestionStore.facture;
import java.sql.SQLException;
import java.util.List;

import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;


public class FactureService implements IService {

    private final Connection connection;
    public FactureService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void add(Object o) throws SQLException {
        facture p = (facture) o;
        // Vérifier si l'utilisateur avec l'ID spécifié existe avant d'insérer la facture
        String checkUserExistsQuery = "SELECT id FROM user WHERE id = ?";
        PreparedStatement checkUserExistsStatement = connection.prepareStatement(checkUserExistsQuery);
        checkUserExistsStatement.setInt(1, p.getId());
        ResultSet resultSet = checkUserExistsStatement.executeQuery();

        if (!resultSet.next()) {
            // L'utilisateur avec l'ID spécifié n'existe pas, vous pouvez gérer cette situation en levant une exception ou en affichant un message d'erreur
            throw new SQLException("L'utilisateur avec l'ID spécifié n'existe pas.");
        }

        // Insérer la facture une fois que l'utilisateur existe
        String sql = "INSERT INTO facture (prixTatalPaye, methodeDePaiement, id) " +
                "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setFloat(1, p.getPrixtotalPaye());
        preparedStatement.setString(2, p.getMethodeDePaiement());
        preparedStatement.setInt(3, p.getId());

        preparedStatement.executeUpdate();

        // Fermer les ressources
        preparedStatement.close();
        resultSet.close();
        checkUserExistsStatement.close();
    }


/*
    @Override
    public void modifierfacture(facture p) throws SQLException {
        //String sql = "update facture set dateVente = ?, prixTatalPaye = ?, methodeDePaiement = ?, id = ?";
        String sql = "update facture set prixTatalPaye = ?, methodeDePaiement = ?, id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);


        ps.setFloat(1, p.getPrixtotalPaye());
        ps.setString(2, p.getMethodeDePaiement());
        ps.setInt(3, p.getId());
        ps.executeUpdate();
    }

 */

    public void update(Object o) throws SQLException {
        facture p = (facture) o;
        // Vérifier si l'utilisateur avec l'ID spécifié existe avant de modifier la facture
        String checkUserExistsQuery = "SELECT id FROM user WHERE id = ?";
        PreparedStatement checkUserExistsStatement = connection.prepareStatement(checkUserExistsQuery);
        checkUserExistsStatement.setInt(1, p.getId());
        ResultSet resultSet = checkUserExistsStatement.executeQuery();

        if (!resultSet.next()) {
            // L'utilisateur avec l'ID spécifié n'existe pas, vous pouvez gérer cette situation en levant une exception ou en affichant un message d'erreur
            throw new SQLException("L'utilisateur avec l'ID spécifié n'existe pas.");
        }

        // Mettre à jour la facture une fois que l'utilisateur existe
        String sql = "UPDATE facture SET prixTatalPaye = ?, methodeDePaiement = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setFloat(1, p.getPrixtotalPaye());
        preparedStatement.setString(2, p.getMethodeDePaiement());
        preparedStatement.setInt(3, p.getId());

        preparedStatement.executeUpdate();

        // Fermer les ressources
        preparedStatement.close();
        resultSet.close();
        checkUserExistsStatement.close();
    }

    @Override
    public void delete(int id) throws SQLException {
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
        while (rs.next())
        {
            facture p = new facture();
            p.setIdFacture(rs.getInt("idFacture"));
            p.setDateVente(rs.getDate("dateVente"));
            p.setPrixtotalPaye(rs.getFloat("prixTatalPaye"));
            p.setMethodeDePaiement(rs.getString("methodeDePaiement"));
            p.setId(rs.getInt("id"));
            facture.add(p);
        }
        return facture;
    }
}
