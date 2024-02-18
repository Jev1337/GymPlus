package services.gestionuser;
import entities.gestionuser.Abonnement;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService implements IService<Abonnement> {
    public Connection connection;
    public AbonnementService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Abonnement abonnement) throws SQLException {
        String query = "INSERT INTO abonnement (user_id, dateFinAb, type) VALUES (?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, abonnement.getUser_id());
        pst.setString(2, abonnement.getDuree_abon());
        pst.setString(3, abonnement.getType());
        pst.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM abonnement WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    @Override
    public void update(Abonnement abonnement) throws SQLException {
        String query = "UPDATE abonnement SET user_id = ?, dateFinAb = ?, type = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, abonnement.getUser_id());
        pst.setString(2, abonnement.getDuree_abon());
        pst.setString(3, abonnement.getType());
        pst.setInt(4, abonnement.getId());
        pst.executeUpdate();
    }

    @Override
    public List<Abonnement> getAll() throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Abonnement abonnement = new Abonnement();
            abonnement.setId(rs.getInt("id"));
            abonnement.setUser_id(rs.getInt("user_id"));
            abonnement.setDuree_abon(rs.getString("dateFinAb"));
            abonnement.setType(rs.getString("type"));
            abonnements.add(abonnement);
        }
        return abonnements;
    }
    public boolean isUserSubscribed(int user_id) throws SQLException {
        String query = "SELECT * FROM abonnement WHERE user_id = ? AND dateFinAb > NOW()";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, user_id);
        ResultSet rs = pst.executeQuery();
        return rs.next();
    }

    public Abonnement getCurrentSubscription(int user_id) throws SQLException {
        String query = "SELECT * FROM abonnement WHERE user_id = ? AND dateFinAb > NOW()";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, user_id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            Abonnement abonnement = new Abonnement();
            abonnement.setId(rs.getInt("id"));
            abonnement.setUser_id(rs.getInt("user_id"));
            abonnement.setDuree_abon(rs.getString("dateFinAb"));
            abonnement.setType(rs.getString("type"));
            return abonnement;
        }
        return null;
    }
}
