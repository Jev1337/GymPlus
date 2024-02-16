package services.gestionuser;
import com.password4j.Password;
import entities.gestionuser.Staff;

import services.IService;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class StaffService implements IService<Staff> {

    private Connection connection;
    public StaffService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Staff staff) throws SQLException {
        String query = "INSERT INTO user (id, username, firstname, lastname, date_naiss, password, role, email, num_tel, adresse, photo, poste) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
        pst.setString(12, staff.getPoste());
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
        String query = "UPDATE user SET username = ?, firstname = ?, lastname = ?, date_naiss = ?, password = ?, role = ?, email = ?, num_tel = ?, adresse = ?, photo = ?, poste = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, staff.getUsername());
        pst.setString(2, staff.getFirstname());
        pst.setString(3, staff.getLastname());
        pst.setString(4, staff.getDate_naiss());
        pst.setString(5, Password.hash(staff.getPassword()).withBcrypt().getResult());
        pst.setString(6, staff.getRole());
        pst.setString(7, staff.getEmail());
        pst.setString(8, staff.getNum_tel());
        pst.setString(9, staff.getAdresse());
        pst.setString(10, staff.getPhoto());
        pst.setString(11, staff.getPoste());
        pst.setInt(12, staff.getId());
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
            staff.setPoste(rs.getString("poste"));
            staffs.add(staff);
        }
        return staffs;
    }

    public Staff getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
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
            staff.setPoste(rs.getString("poste"));
        }
        return staff;
    }
}
