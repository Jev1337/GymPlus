package services;

import entities.produit;
import utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IServiceProduit{

    private final Connection connection;

    public ProduitService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouterProduit(produit p) throws SQLException {
        String sql = "insert into produit (name,prix,stock,description,categorie,photo,seuil,promo) " +
                "values('" + p.getName() + "','" + p.getPrix() + "','" + p.getStock() + "','" + p.getDescription() + "','" + p.getCategorie() + "','" + p.getPhoto() + "','" + p.getSeuil() + "','" + p.getPromo() + "')";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void modifierProduit(produit p) throws SQLException {
        String sql = "update produit set name = ?, prix = ?, stock = ?, description = ?, categorie = ?, photo = ? , seuil = ?, promo = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, p.getName());
        ps.setFloat(2, p.getPrix());
        ps.setInt(3, p.getStock());
        ps.setString(4, p.getDescription());
        ps.setString(5, p.getCategorie());
        ps.setString(6, p.getPhoto());
        ps.setInt(7, p.getSeuil());
        ps.setFloat(8, p.getPromo());
        ps.executeUpdate();
    }

    @Override
    public void supprimerProduit(int id) throws SQLException {

        String sql = "DELETE FROM produit WHERE idProduit = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<produit> recupererProduit() throws SQLException {
        String sql = "select * from produit";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<produit> produit = new ArrayList<>();
        while (rs.next())
        {
            produit p = new produit();
            p.setIdProduit(rs.getInt("idProduit"));
            p.setName(rs.getString("name"));
            p.setPrix(rs.getFloat("prix"));
            p.setStock(rs.getInt("stock"));
            p.setDescription(rs.getString("description"));
            p.setCategorie(rs.getString("categorie"));
            p.setPhoto(rs.getString("photo"));
            p.setSeuil(rs.getInt("seuil"));
            p.setPromo(rs.getFloat("promo"));
            produit.add(p);
        }
        return produit;
    }
}

