package services.gestionevents;

import entities.gestionevents.Black_Listed;
import entities.gestionuser.Client;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BlackListedService implements IService <Black_Listed> {


    private Connection connection;

    public BlackListedService() {

        connection = MyDatabase.getInstance().getConnection();
    }
    public void add(Black_Listed blackListed) throws SQLException {
String query = "INSERT INTO black_listed (id_user, start_ban, end_ban) VALUES (?,?,?)";
        java.sql.PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, blackListed.getId_user());
        ps.setDate(2, blackListed.getStart_ban());
        ps.setDate(3, blackListed.getEnd_ban());
        ps.executeUpdate();

    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM black_listed WHERE id_user = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();

    }

    @Override
    public void update(Black_Listed blackListed) throws SQLException {

    }
    public boolean search(int id) throws SQLException {
        String query = "SELECT * FROM black_listed WHERE id_user = "+id;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        return rs.next();
    }

    @Override
    public List<Black_Listed> getAll() throws SQLException {
        List<Black_Listed> blacklistedl = new ArrayList<>();
        String query = "SELECT * FROM black_listed";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
           Black_Listed blackListed = new Black_Listed(rs.getInt("id_user"), rs.getDate("start_ban"), rs.getDate("end_ban"));
            blacklistedl.add(blackListed);


        }
        return blacklistedl;
    }
    //get all blacklisted people ids
    public  List<Integer> blpeople() throws SQLException {
        List<Integer> blacklistedIds = new ArrayList<>();
        String query = "SELECT id_user FROM black_listed";
        Statement st = null;
        try {
            st = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            blacklistedIds.add(rs.getInt("id_user"));
        }
        return blacklistedIds;
    }

    public List<Integer> getusersids() throws SQLException {
        String query = "SELECT id FROM user where role = 'client'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Integer> ids = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            ids.add(id);
        }
        return ids;
    }
    public Client getUserDetails(int userId) throws SQLException {
        String query = "SELECT id, username, firstname, lastname FROM user WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Client user = new Client();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            return user;
        }
        return null;
    }
    public String remainingDays(int userId) throws SQLException {
        String query = "SELECT end_ban FROM black_listed WHERE id_user = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Date endBanDate = rs.getDate("end_ban");
            LocalDate endBanLocalDate = endBanDate.toLocalDate();
            LocalDate now = LocalDate.now();
            int remainingDays = (int) ChronoUnit.DAYS.between(now, endBanLocalDate);
            return remainingDays + " days";
        }
        return null;
    }



}
