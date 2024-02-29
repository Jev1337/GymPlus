package services.gestionequipements;

import entities.gestionequipements.Equipements_details;
import utils.MyDatabase;
import services.IService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipementService implements IService<Equipements_details>{

    private Connection connection;

    public EquipementService() {
        connection= MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Equipements_details equipementsDetails) throws SQLException {
        String sql="insert into equipements_details(name,description,duree_de_vie,etat) values (?,?,?,?)";
        PreparedStatement ps =connection.prepareStatement(sql);
        ps.setString(1, equipementsDetails.getName());
        ps.setString(2,equipementsDetails.getDescription());
        ps.setString(3,equipementsDetails.getDuree_de_vie());
        ps.setString(4,equipementsDetails.getEtat());
        ps.executeUpdate();
    }

    @Override
    public void update(Equipements_details equipementsDetails) throws SQLException
    {
        String sql="update equipements_details set name=?, description=? ,duree_de_vie=?, etat=? where id=?";
        PreparedStatement ps =connection.prepareStatement(sql);
        ps.setString(1, equipementsDetails.getName());
        ps.setString(2,equipementsDetails.getDescription());
        ps.setString(3,equipementsDetails.getDuree_de_vie());
        ps.setString(4,equipementsDetails.getEtat());
        ps.setInt(5,equipementsDetails.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql="delete from equipements_details where id =?";
        PreparedStatement ps =connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Equipements_details> getAll() throws SQLException {
        String sql = "select * from equipements_details";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Equipements_details> equipements = new ArrayList<>();
        while (rs.next()) {
            equipements.add(new Equipements_details(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("duree_de_vie"),rs.getString("etat")));
        }
        return equipements;

    }

    public Equipements_details getEquipementById(int id) throws SQLException {
        String sql = "select * from equipements_details where id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Equipements_details(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("duree_de_vie"),rs.getString("etat"));
        }
        return null;
    }
}
