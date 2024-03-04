package services.gestionStore;

import entities.gestionStore.Livraison;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivraisonService implements IService<Livraison> {

    private final Connection connection;
    public LivraisonService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Livraison l) throws SQLException {
        String sql = "insert into livraison (idFacture , idClient , Lieu , etat) " +
                "values('" + l.getIdFacture()+ "','" + l.getIdClient() + "','" + l.getLieu() + "','" + l.getEtat() + "')";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void update(Livraison l) throws SQLException {

        String sql = "UPDATE livraison SET etat = ? WHERE idLivraison = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, l.getEtat());
        ps.setFloat(2, l.getIdLivraison());
        ps.executeUpdate();

    }

    @Override
    public List<Livraison> getAll() throws SQLException {
        String sql = "select * from livraison";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Livraison> livraison = new ArrayList<>();
        while (rs.next())
        {
            Livraison l = new Livraison();
            l.setIdLivraison(rs.getInt("idLivraison"));
            l.setIdFacture(rs.getInt("idFacture"));
            l.setIdClient(rs.getInt("idClient"));
            l.setLieu(rs.getString("Lieu"));
            l.setEtat(rs.getString("etat"));
            livraison.add(l);
        }
        return livraison;
    }

    public Livraison getOne(int id) throws SQLException
    {
        String sql = "SELECT * FROM livraison WHERE idProduit = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        Livraison l = null; // Initialize the product as null

        if (rs.next()) { // Check if the result set has any rows
            l = new Livraison();
            l.setIdLivraison(rs.getInt("idLivraison"));
            l.setIdFacture(rs.getInt("idFacture"));
            l.setIdClient(rs.getInt("idClient"));
            l.setLieu(rs.getString("Lieu"));
            l.setEtat(rs.getString("etat"));
        }

        rs.close();
        ps.close();

        return l;
    }

    public Livraison getLivraisonById(int idLivraison) throws SQLException
    {
        return getOne(idLivraison);
    }

    @Override
    public void delete(int id) throws SQLException {

    }

}
