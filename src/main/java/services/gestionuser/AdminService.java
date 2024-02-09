package services.gestionuser;
import entities.gestionuser.Admin;

import services.IService;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService implements IService<Admin> {

    private Connection connection;

    public AdminService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Admin admin) throws SQLException {
        String query = "INSERT INTO user (id, username, firstname, lastname, date_naiss, password, role, poste, email, num_tel, adresse, photo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, admin.getId());
        pst.setString(2, admin.getUsername());
        pst.setString(3, admin.getFirstname());
        pst.setString(4, admin.getLastname());
        pst.setString(5, admin.getDate_naiss());
        pst.setString(6, admin.getPassword());
        pst.setString(7, admin.getRole());
        pst.setString(8, admin.getPoste());
        pst.setString(9, admin.getEmail());
        pst.setString(10, admin.getNum_tel());
        pst.setString(11, admin.getAdresse());
        pst.setString(12, admin.getPhoto());
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
    public void update(Admin admin) throws SQLException {
        String query = "UPDATE user SET username = ?, firstname = ?, lastname = ?, date_naiss = ?, password = ?, role = ?, poste = ?, email = ?, num_tel = ?, adresse = ?, photo = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, admin.getUsername());
        pst.setString(2, admin.getFirstname());
        pst.setString(3, admin.getLastname());
        pst.setString(4, admin.getDate_naiss());
        pst.setString(5, admin.getPassword());
        pst.setString(6, admin.getRole());
        pst.setString(7, admin.getPoste());
        pst.setString(8, admin.getEmail());
        pst.setString(9, admin.getNum_tel());
        pst.setString(10, admin.getAdresse());
        pst.setString(11, admin.getPhoto());
        pst.setInt(12, admin.getId());
        pst.executeUpdate();
    }

    @Override
    public List<Admin> getAll() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM user";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Admin admin = new Admin();
            admin.setId(rs.getInt("id"));
            admin.setUsername(rs.getString("username"));
            admin.setFirstname(rs.getString("firstname"));
            admin.setLastname(rs.getString("lastname"));
            admin.setDate_naiss(rs.getString("date_naiss"));
            admin.setPassword(rs.getString("password"));
            admin.setPoste(rs.getString("poste"));
            admin.setEmail(rs.getString("email"));
            admin.setNum_tel(rs.getString("num_tel"));
            admin.setAdresse(rs.getString("adresse"));
            admin.setPhoto(rs.getString("photo"));
            admins.add(admin);
        }
        return admins;
    }
}
