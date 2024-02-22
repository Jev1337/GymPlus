package services.gestionuser;

import entities.gestionuser.Abonnement;
import entities.gestionuser.AbonnementDetails;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDetailsService implements IService<AbonnementDetails> {
    public Connection connection;
    public AbonnementDetailsService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(AbonnementDetails abonnementDetails) throws SQLException {
        String query = "INSERT INTO abonnement_details (name, prix) VALUES (?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, abonnementDetails.getType());
        pst.setDouble(2, abonnementDetails.getPrix());
        pst.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        System.out.println("Not implemented");
    }

    @Override
    public void update(AbonnementDetails abonnementDetails) throws SQLException {
        String query = "UPDATE abonnement_details SET prix = ? WHERE name = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setDouble(1, abonnementDetails.getPrix());
        pst.setString(2, abonnementDetails.getType());
        pst.executeUpdate();
    }

    @Override
    public List<AbonnementDetails> getAll() throws SQLException {
        List<AbonnementDetails> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement_details";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            AbonnementDetails abonnement = new AbonnementDetails();
            abonnement.setType(rs.getString("name"));
            abonnement.setPrix(rs.getDouble("prix"));
            abonnements.add(abonnement);
        }
        return abonnements;
    }

    public Double getPriceByType(String type) throws SQLException {
        String query = "SELECT prix FROM abonnement_details WHERE name = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, type);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getDouble("prix");
        }
        return null;
    }
}
