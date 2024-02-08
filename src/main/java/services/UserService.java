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


    public void setStatements(PreparedStatement pst, User user) throws SQLException {
        pst.setString(1, user.getUsername());
        pst.setString(2, user.getLastname());
        pst.setString(3, user.getDate_naiss());
        pst.setString(4, user.getPassword());
        pst.setString(5, user.getEmail());
        pst.setString(6, user.getPoste());
        pst.setString(7, user.getRole());
        pst.setString(8, user.getNum_tel());
        pst.setString(9, user.getAdresse());
        pst.setString(10, user.getPhoto());
    }
    @Override
    public void add(User user) throws SQLException {
        String query = "INSERT INTO user (username, lastname, date_naiss, password, email, poste, role, num_tel, adresse, photo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        setStatements(pst, user);
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
        String query = "UPDATE user SET username = ?, lastname = ?, date_naiss = ?, password = ?, email = ?, poste = ?, role = ?, num_tel = ?, adresse = ?, photo = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        setStatements(pst, user);
        pst.executeUpdate();
    }

    @Override
    public List<User> getAll() throws SQLException {
        String query = "SELECT * FROM user";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<User> users = new ArrayList<>();
        while (rs.next())
            users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("lastname"), rs.getString("date_naiss"), rs.getString("password"), rs.getString("email"), rs.getString("poste"), rs.getString("role"), rs.getString("num_tel"), rs.getString("adresse"), rs.getString("photo")));
        return users;
    }
}
