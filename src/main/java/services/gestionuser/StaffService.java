package services.gestionuser;
import com.password4j.Password;
import entities.gestionuser.Staff;

import services.IService;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class StaffService implements IService<Staff> {

    public Connection connection;
    public StaffService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Staff staff) throws SQLException {
        String query = "INSERT INTO user (id, username, firstname, lastname, date_naiss, password, role, email, num_tel, adresse, photo, faceid, faceid_ts) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, staff.getId());
        pst.setString(2, staff.getUsername());
        pst.setString(3, staff.getFirstname());
        pst.setString(4, staff.getLastname());
        pst.setString(5, staff.getDate_naiss());
        pst.setString(6, Password.hash(staff.getPassword()).withBcrypt().getResult());
        pst.setString(7, staff.getRole());
        pst.setString(8, staff.getEmail());
        pst.setString(9, staff.getNum_tel());
        pst.setString(10, staff.getAdresse());
        pst.setString(11, staff.getPhoto());
        pst.setString(12, staff.getFaceid());
        pst.setString(13, staff.getFaceid_ts());
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
    public void update(Staff staff) throws SQLException {
        String query = "UPDATE user SET username = ?, firstname = ?, lastname = ?, date_naiss = ?, email = ?, num_tel = ?, adresse = ?, photo = ?, faceid=?, faceid_ts = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, staff.getUsername());
        pst.setString(2, staff.getFirstname());
        pst.setString(3, staff.getLastname());
        pst.setString(4, staff.getDate_naiss());
        pst.setString(5, staff.getEmail());
        pst.setString(6, staff.getNum_tel());
        pst.setString(7, staff.getAdresse());
        pst.setString(8, staff.getPhoto());
        pst.setString(9, staff.getFaceid());
        pst.setString(10, staff.getFaceid_ts());
        pst.setInt(11, staff.getId());

        pst.executeUpdate();
    }

    @Override
    public List<Staff> getAll() throws SQLException {
        List<Staff> staffs = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = 'staff'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Staff staff = new Staff();
            staff.setId(rs.getInt("id"));
            staff.setUsername(rs.getString("username"));
            staff.setFirstname(rs.getString("firstname"));
            staff.setLastname(rs.getString("lastname"));
            staff.setDate_naiss(rs.getString("date_naiss"));
            staff.setPassword(rs.getString("password"));
            staff.setEmail(rs.getString("email"));
            staff.setNum_tel(rs.getString("num_tel"));
            staff.setAdresse(rs.getString("adresse"));
            staff.setPhoto(rs.getString("photo"));
            staff.setFaceid(rs.getString("faceid"));
            staff.setFaceid_ts(rs.getString("faceid_ts"));
            staff.setRole(rs.getString("role"));
            staffs.add(staff);
        }
        return staffs;
    }

    public Staff getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ? AND role = 'staff'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        Staff staff = new Staff();
        while (rs.next()) {
            staff.setId(rs.getInt("id"));
            staff.setUsername(rs.getString("username"));
            staff.setFirstname(rs.getString("firstname"));
            staff.setLastname(rs.getString("lastname"));
            staff.setDate_naiss(rs.getString("date_naiss"));
            staff.setPassword(rs.getString("password"));
            staff.setEmail(rs.getString("email"));
            staff.setNum_tel(rs.getString("num_tel"));
            staff.setAdresse(rs.getString("adresse"));
            staff.setPhoto(rs.getString("photo"));
            staff.setFaceid(rs.getString("faceid"));
            staff.setFaceid_ts(rs.getString("faceid_ts"));
            staff.setRole(rs.getString("role"));
            return staff;
        }
        return null;
    }

    public Staff getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ? AND role = 'staff'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        Staff staff = new Staff();
        while (rs.next()) {
            staff.setId(rs.getInt("id"));
            staff.setUsername(rs.getString("username"));
            staff.setFirstname(rs.getString("firstname"));
            staff.setLastname(rs.getString("lastname"));
            staff.setDate_naiss(rs.getString("date_naiss"));
            staff.setPassword(rs.getString("password"));
            staff.setEmail(rs.getString("email"));
            staff.setNum_tel(rs.getString("num_tel"));
            staff.setAdresse(rs.getString("adresse"));
            staff.setPhoto(rs.getString("photo"));
            staff.setFaceid(rs.getString("faceid"));
            staff.setFaceid_ts(rs.getString("faceid_ts"));
            staff.setRole(rs.getString("role"));
            return staff;
        }
        return null;
    }

    public Staff getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ? AND role = 'staff'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        Staff staff = new Staff();
        while (rs.next()) {
            staff.setId(rs.getInt("id"));
            staff.setUsername(rs.getString("username"));
            staff.setFirstname(rs.getString("firstname"));
            staff.setLastname(rs.getString("lastname"));
            staff.setDate_naiss(rs.getString("date_naiss"));
            staff.setPassword(rs.getString("password"));
            staff.setEmail(rs.getString("email"));
            staff.setNum_tel(rs.getString("num_tel"));
            staff.setAdresse(rs.getString("adresse"));
            staff.setPhoto(rs.getString("photo"));
            staff.setFaceid(rs.getString("faceid"));
            staff.setFaceid_ts(rs.getString("faceid_ts"));
            staff.setRole(rs.getString("role"));
            return staff;
        }
        return null;
    }

    public Staff getUserByPhone(String num_tel) throws SQLException {
        String query = "SELECT * FROM user WHERE num_tel = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, num_tel);
        ResultSet rs = pst.executeQuery();
        Staff staff = new Staff();
        while (rs.next()) {
            staff.setId(rs.getInt("id"));
            staff.setUsername(rs.getString("username"));
            staff.setFirstname(rs.getString("firstname"));
            staff.setLastname(rs.getString("lastname"));
            staff.setDate_naiss(rs.getString("date_naiss"));
            staff.setPassword(rs.getString("password"));
            staff.setEmail(rs.getString("email"));
            staff.setNum_tel(rs.getString("num_tel"));
            staff.setAdresse(rs.getString("adresse"));
            staff.setPhoto(rs.getString("photo"));
            staff.setFaceid(rs.getString("faceid"));
            staff.setFaceid_ts(rs.getString("faceid_ts"));
            staff.setRole(rs.getString("role"));
            return staff;
        }
        return null;
    }

    public void updatePassword(int id, String password) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, Password.hash(password).withBcrypt().getResult());
        pst.setInt(2, id);
        pst.executeUpdate();
    }
}
