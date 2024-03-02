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

    public List<Abonnement> getAllCurrent() throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement WHERE dateFinAb > NOW()";
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

    public List<Abonnement> getAbonnementByType(String type) throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement WHERE type = ? ";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, type);
        ResultSet rs = pst.executeQuery();
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
    public List<Abonnement> getAbonnementByTypeCurrent(String type) throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement WHERE type = ? AND dateFinAb > NOW()";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, type);
        ResultSet rs = pst.executeQuery();
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

    public float getCurMonthIncome(){
        float income = 0;
        try {
            String query1 = "SELECT SUM(prix) as income FROM abonnement_details JOIN abonnement ON abonnement_details.name = abonnement.type WHERE MONTH(DATE_ADD(dateFinAb, INTERVAL -3 MONTH)) = MONTH(NOW()) AND YEAR(DATE_ADD(dateFinAb, INTERVAL -3 MONTH)) = YEAR(NOW()) AND abonnement.type = 'GP 1'";
            String query2 = "SELECT SUM(prix) as income FROM abonnement_details JOIN abonnement ON abonnement_details.name = abonnement.type WHERE MONTH(DATE_ADD(dateFinAb, INTERVAL -6 MONTH)) = MONTH(NOW()) AND YEAR(DATE_ADD(dateFinAb, INTERVAL -6 MONTH)) = YEAR(NOW()) AND abonnement.type = 'GP 2'";
            String query3 = "SELECT SUM(prix) as income FROM abonnement_details JOIN abonnement ON abonnement_details.name = abonnement.type WHERE MONTH(DATE_ADD(dateFinAb, INTERVAL -12 MONTH)) = MONTH(NOW()) AND YEAR(DATE_ADD(dateFinAb, INTERVAL -12 MONTH)) = YEAR(NOW()) AND abonnement.type = 'GP 3'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                income += rs.getFloat("income");
            }
            rs = st.executeQuery(query2);
            if (rs.next()) {
                income += rs.getFloat("income");
            }
            rs = st.executeQuery(query3);
            if (rs.next()) {
                income += rs.getFloat("income");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income;
    }
}
