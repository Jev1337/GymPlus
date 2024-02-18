package services.gestionuser;
import com.password4j.Password;
import entities.gestionuser.Client;

import services.IService;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ClientService implements IService<Client> {

    public Connection connection;
    public ClientService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Client client) throws SQLException {
        String query = "INSERT INTO user (id, username, firstname, lastname, date_naiss, password, email, num_tel, adresse, photo, role) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, client.getId());
        pst.setString(2, client.getUsername());
        pst.setString(3, client.getFirstname());
        pst.setString(4, client.getLastname());
        pst.setString(5, client.getDate_naiss());
        pst.setString(6, Password.hash(client.getPassword()).withBcrypt().getResult());
        pst.setString(7, client.getEmail());
        pst.setString(8, client.getNum_tel());
        pst.setString(9, client.getAdresse());
        pst.setString(10, client.getPhoto());
        pst.setString(11, client.getRole());
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
    public void update(Client client) throws SQLException {
        String query = "UPDATE user SET username = ?, firstname = ?, lastname = ?, date_naiss = ?, email = ?, num_tel = ?, adresse = ?, photo = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, client.getUsername());
        pst.setString(2, client.getFirstname());
        pst.setString(3, client.getLastname());
        pst.setString(4, client.getDate_naiss());
        pst.setString(5, client.getEmail());
        pst.setString(6, client.getNum_tel());
        pst.setString(7, client.getAdresse());
        pst.setString(8, client.getPhoto());
        pst.setInt(9, client.getId());
        pst.executeUpdate();
    }

    @Override
    public List<Client> getAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = 'client'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setUsername(rs.getString("username"));
            client.setFirstname(rs.getString("firstname"));
            client.setLastname(rs.getString("lastname"));
            client.setDate_naiss(rs.getString("date_naiss"));
            client.setPassword(rs.getString("password"));
            client.setEmail(rs.getString("email"));
            client.setNum_tel(rs.getString("num_tel"));
            client.setAdresse(rs.getString("adresse"));
            client.setPhoto(rs.getString("photo"));
            client.setRole(rs.getString("role"));
            clients.add(client);
        }
        return clients;
    }

    public Client getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ? AND role = 'client'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setUsername(rs.getString("username"));
            client.setFirstname(rs.getString("firstname"));
            client.setLastname(rs.getString("lastname"));
            client.setDate_naiss(rs.getString("date_naiss"));
            client.setPassword(rs.getString("password"));
            client.setEmail(rs.getString("email"));
            client.setNum_tel(rs.getString("num_tel"));
            client.setAdresse(rs.getString("adresse"));
            client.setPhoto(rs.getString("photo"));
            client.setRole(rs.getString("role"));
            return client;
        }
        return null;
    }

    public Client getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ? AND role = 'client'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setUsername(rs.getString("username"));
            client.setFirstname(rs.getString("firstname"));
            client.setLastname(rs.getString("lastname"));
            client.setDate_naiss(rs.getString("date_naiss"));
            client.setPassword(rs.getString("password"));
            client.setEmail(rs.getString("email"));
            client.setNum_tel(rs.getString("num_tel"));
            client.setAdresse(rs.getString("adresse"));
            client.setPhoto(rs.getString("photo"));
            client.setRole(rs.getString("role"));
            return client;
        }
        return null;
    }

    public Client getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ? AND role = 'client'";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setUsername(rs.getString("username"));
            client.setFirstname(rs.getString("firstname"));
            client.setLastname(rs.getString("lastname"));
            client.setDate_naiss(rs.getString("date_naiss"));
            client.setPassword(rs.getString("password"));
            client.setEmail(rs.getString("email"));
            client.setNum_tel(rs.getString("num_tel"));
            client.setAdresse(rs.getString("adresse"));
            client.setPhoto(rs.getString("photo"));
            client.setRole(rs.getString("role"));
            return client;
        }
        return null;
    }

    public List<Client> getNonSubscribedUserList() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = 'client' AND id NOT IN (SELECT user_id FROM abonnement WHERE dateFinAb > NOW())";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setUsername(rs.getString("username"));
            client.setFirstname(rs.getString("firstname"));
            client.setLastname(rs.getString("lastname"));
            client.setDate_naiss(rs.getString("date_naiss"));
            client.setPassword(rs.getString("password"));
            client.setEmail(rs.getString("email"));
            client.setNum_tel(rs.getString("num_tel"));
            client.setAdresse(rs.getString("adresse"));
            client.setPhoto(rs.getString("photo"));
            client.setRole(rs.getString("role"));
            clients.add(client);
        }
        return clients;
    }
}
