package services;
import entities.User;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User>{

    private Connection connection;
    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void add(User user) throws SQLException {
        String query = "INSERT INTO user (id, username,firstname, lastname, date_naiss, password, email, poste, role, num_tel, adresse, photo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, user.getId());
        pst.setString(2, user.getUsername());
        pst.setString(3, user.getFirstname());
        pst.setString(4, user.getLastname());
        pst.setString(5, user.getDate_naiss());
        pst.setString(6, user.getPassword());
        pst.setString(7, user.getEmail());
        pst.setString(8, user.getPoste());
        pst.setString(9, user.getRole());
        pst.setString(10, user.getNum_tel());
        pst.setString(11, user.getAdresse());
        pst.setString(12, user.getPhoto());
        pst.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM user WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    @Override
    public void update(User user) throws SQLException {
        String query = "UPDATE user SET username = ?, firstname = ?, lastname = ?, date_naiss = ?, password = ?, email = ?, poste = ?, role = ?, num_tel = ?, adresse = ?, photo = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, user.getUsername());
        pst.setString(2, user.getFirstname());
        pst.setString(3, user.getLastname());
        pst.setString(4, user.getDate_naiss());
        pst.setString(5, user.getPassword());
        pst.setString(6, user.getEmail());
        pst.setString(7, user.getPoste());
        pst.setString(8, user.getRole());
        pst.setString(9, user.getNum_tel());
        pst.setString(10, user.getAdresse());
        pst.setString(11, user.getPhoto());
        pst.setInt(12, user.getId());
        pst.executeUpdate();
    }

    @Override
    public List<User> getAll() throws SQLException {
        String query = "SELECT * FROM user";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<User> users = new ArrayList<>();
        while (rs.next())
            users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("date_naiss"), rs.getString("password"), rs.getString("email"), rs.getString("poste"), rs.getString("role"), rs.getString("num_tel"), rs.getString("adresse"), rs.getString("photo")));
        return users;
    }
}
