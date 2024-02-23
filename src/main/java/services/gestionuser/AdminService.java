package services.gestionuser;
import com.password4j.Password;
import entities.gestionuser.Admin;

import entities.gestionuser.Staff;
import services.IService;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService implements IService<Admin> {
    private Connection connection = MyDatabase.getInstance().getConnection();

    public AdminService() {
    }

    @Override
    public void add(Admin admin) throws SQLException {
        String query = "INSERT INTO user (id, username, firstname, lastname, date_naiss, password, email, num_tel, adresse, photo, role, faceid, faceid_ts) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, admin.getId());
        pst.setString(2, admin.getUsername());
        pst.setString(3, admin.getFirstname());
        pst.setString(4, admin.getLastname());
        pst.setString(5, admin.getDate_naiss());
        pst.setString(6, Password.hash(admin.getPassword()).withBcrypt().getResult());
        pst.setString(7, admin.getEmail());
        pst.setString(8, admin.getNum_tel());
        pst.setString(9, admin.getAdresse());
        pst.setString(10, admin.getPhoto());
        pst.setString(11, admin.getRole());
        pst.setString(12, admin.getFaceid());
        pst.setString(13, admin.getFaceid_ts());
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
        String query = "UPDATE user SET username = ?, firstname = ?, lastname = ?, date_naiss = ?, email = ?, num_tel = ?, adresse = ?, photo = ?, faceid = ?, faceid_ts = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, admin.getUsername());
        pst.setString(2, admin.getFirstname());
        pst.setString(3, admin.getLastname());
        pst.setString(4, admin.getDate_naiss());
        pst.setString(5, admin.getEmail());
        pst.setString(6, admin.getNum_tel());
        pst.setString(7, admin.getAdresse());
        pst.setString(8, admin.getPhoto());
        pst.setString(9, admin.getFaceid());
        pst.setString(10, admin.getFaceid_ts());
        pst.setInt(11, admin.getId());

        pst.executeUpdate();
    }

    @Override
    public List<Admin> getAll() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = 'admin'";
        PreparedStatement pst = connection.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Admin admin = new Admin();
            admin.setId(rs.getInt("id"));
            admin.setUsername(rs.getString("username"));
            admin.setFirstname(rs.getString("firstname"));
            admin.setLastname(rs.getString("lastname"));
            admin.setDate_naiss(rs.getString("date_naiss"));
            admin.setEmail(rs.getString("email"));
            admin.setPassword(rs.getString("password"));
            admin.setNum_tel(rs.getString("num_tel"));
            admin.setAdresse(rs.getString("adresse"));
            admin.setPhoto(rs.getString("photo"));
            admin.setFaceid(rs.getString("faceid"));
            admin.setFaceid_ts(rs.getString("faceid_ts"));
            admin.setRole(rs.getString("role"));
            admins.add(admin);
        }
        return admins;
    }

    public Admin getUserById(int id) throws SQLException {
        Admin admin = new Admin();
        String query = "SELECT * FROM user WHERE id = ? AND role = 'admin'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            admin.setId(rs.getInt("id"));
            admin.setUsername(rs.getString("username"));
            admin.setFirstname(rs.getString("firstname"));
            admin.setLastname(rs.getString("lastname"));
            admin.setDate_naiss(rs.getString("date_naiss"));
            admin.setPassword(rs.getString("password"));
            admin.setEmail(rs.getString("email"));
            admin.setNum_tel(rs.getString("num_tel"));
            admin.setAdresse(rs.getString("adresse"));
            admin.setPhoto(rs.getString("photo"));
            admin.setFaceid(rs.getString("faceid"));
            admin.setFaceid_ts(rs.getString("faceid_ts"));
            admin.setRole(rs.getString("role"));
            return admin;
        }
        return null;
    }

    public Admin getUserByUsername(String username) throws SQLException {
        Admin admin = new Admin();
        String query = "SELECT * FROM user WHERE username = ? AND role = 'admin'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            admin.setId(rs.getInt("id"));
            admin.setUsername(rs.getString("username"));
            admin.setFirstname(rs.getString("firstname"));
            admin.setLastname(rs.getString("lastname"));
            admin.setDate_naiss(rs.getString("date_naiss"));
            admin.setEmail(rs.getString("email"));
            admin.setPassword(rs.getString("password"));
            admin.setNum_tel(rs.getString("num_tel"));
            admin.setAdresse(rs.getString("adresse"));
            admin.setPhoto(rs.getString("photo"));
            admin.setFaceid(rs.getString("faceid"));
            admin.setFaceid_ts(rs.getString("faceid_ts"));
            admin.setRole(rs.getString("role"));
            return admin;
        }
        return null;
    }

    public Admin getUserByEmail(String email) throws SQLException {
        Admin admin = new Admin();
        String query = "SELECT * FROM user WHERE email = ? AND role = 'admin'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            admin.setId(rs.getInt("id"));
            admin.setUsername(rs.getString("username"));
            admin.setFirstname(rs.getString("firstname"));
            admin.setLastname(rs.getString("lastname"));
            admin.setDate_naiss(rs.getString("date_naiss"));
            admin.setPassword(rs.getString("password"));
            admin.setEmail(rs.getString("email"));
            admin.setNum_tel(rs.getString("num_tel"));
            admin.setAdresse(rs.getString("adresse"));
            admin.setPhoto(rs.getString("photo"));
            admin.setFaceid(rs.getString("faceid"));
            admin.setFaceid_ts(rs.getString("faceid_ts"));
            admin.setRole(rs.getString("role"));
            return admin;
        }
        return null;
    }

}
