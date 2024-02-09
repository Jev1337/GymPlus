package services;
import entities.Client;

import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ClientService implements IService<Client>{

    private Connection connection;
    public ClientService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Client client) throws SQLException {
        String query = "INSERT INTO user (id, username, firstname, lastname, date_naiss, password, email, num_tel, adresse, photo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, client.getId());
        pst.setString(2, client.getUsername());
        pst.setString(3, client.getFirstname());
        pst.setString(4, client.getLastname());
        pst.setString(5, client.getDate_naiss());
        pst.setString(6, client.getPassword());
        pst.setString(7, client.getEmail());
        pst.setString(8, client.getNum_tel());
        pst.setString(9, client.getAdresse());
        pst.setString(10, client.getPhoto());
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
        String query = "UPDATE user SET username = ?, firstname = ?, lastname = ?, date_naiss = ?, password = ?, email = ?, num_tel = ?, adresse = ?, photo = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, client.getUsername());
        pst.setString(2, client.getFirstname());
        pst.setString(3, client.getLastname());
        pst.setString(4, client.getDate_naiss());
        pst.setString(5, client.getPassword());
        pst.setString(6, client.getEmail());
        pst.setString(7, client.getNum_tel());
        pst.setString(8, client.getAdresse());
        pst.setString(9, client.getPhoto());
        pst.setInt(10, client.getId());
        pst.executeUpdate();
    }

    @Override
    public List<Client> getAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM user";
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
            clients.add(client);
        }
        return clients;
    }
}
