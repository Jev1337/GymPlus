package services.gestionuser;
import entities.gestionuser.Admin;

import entities.gestionuser.Staff;
import services.IService;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService extends StaffService implements IService<Staff> {

    public List<Staff> getAll() throws SQLException {
        List<Staff> admins = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = 'admin'";
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
            admin.setEmail(rs.getString("email"));
            admin.setNum_tel(rs.getString("num_tel"));
            admin.setAdresse(rs.getString("adresse"));
            admin.setPhoto(rs.getString("photo"));
            admins.add(admin);
        }
        return admins;
    }

    public Admin getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ? AND role = 'admin'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        Admin admin = new Admin();
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
            return admin;
        }
        return null;
    }

    public Admin getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ? AND role = 'admin'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        Admin admin = new Admin();
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
            return admin;
        }
        return null;
    }

    public Admin getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ? AND role = 'admin'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        Admin admin = new Admin();
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
            return admin;
        }
        return null;
    }
}
