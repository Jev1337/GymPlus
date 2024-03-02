package services.gestionStore;

import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import entities.gestionStore.produit;
import services.IService;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IService<produit>
{

    private final Connection connection;

    public ProduitService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(produit p) throws SQLException
    {
        String sql = "insert into produit (name,prix,stock,description,categorie,photo,seuil,promo) " +
                "values('" + p.getName() + "','" + p.getPrix() + "','" + p.getStock() + "','" + p.getDescription() + "','" + p.getCategorie() + "','" + p.getPhoto() + "','" + p.getSeuil() + "','" + p.getPromo() + "')";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void update(produit p) throws SQLException
    {
        String sql = "UPDATE produit SET name = ?, prix = ?, stock = ?, description = ?, categorie = ?, photo = ? , seuil = ?, promo = ? WHERE idProduit = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, p.getName());
        ps.setFloat(2, p.getPrix());
        ps.setInt(3, p.getStock());
        ps.setString(4, p.getDescription());
        ps.setString(5, p.getCategorie());
        ps.setString(6, p.getPhoto());
        ps.setInt(7, p.getSeuil());
        ps.setFloat(8, p.getPromo());
        ps.setInt(9, p.getIdProduit());
        ps.executeUpdate();

    }



    @Override
    public void delete(int id) throws SQLException
    {
        String sql = "DELETE FROM produit WHERE idProduit = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<produit> getAll() throws SQLException
    {
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

    public produit getOne(int id) throws SQLException
    {
        String sql = "SELECT * FROM produit WHERE idProduit = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        produit p = null; // Initialize the product as null

        if (rs.next()) { // Check if the result set has any rows
            p = new produit();
            p.setIdProduit(rs.getInt("idProduit"));
            p.setName(rs.getString("name"));
            p.setPrix(rs.getFloat("prix"));
            p.setStock(rs.getInt("stock"));
            p.setDescription(rs.getString("description"));
            p.setCategorie(rs.getString("categorie"));
            p.setPhoto(rs.getString("photo"));
            p.setSeuil(rs.getInt("seuil"));
            p.setPromo(rs.getFloat("promo"));
        }

        rs.close();
        ps.close();

        return p;
    }

    public void MAJ_Stock(int nb_article , int id) throws SQLException
    {
        //produit p = new produit();
        produit p = getOne(id);
        int n = p.getStock()-nb_article;
        p.setStock(n);

        //p.setIdProduit(id);
        update(p);
    }

    public produit getProduitById(int idProduit) throws SQLException
    {
        return getOne(idProduit);
    }

    public List<produit> recupererByName(String name) throws SQLException
    {
        //String sql = "select * from produit where name= '"+name+"' ";
        String sql = "SELECT * FROM produit WHERE name LIKE '%" + name + "%'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<produit> prod = new ArrayList<>();
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
            prod.add(p);
        }
        return prod;
    }

    public List<produit> recupererByCategorie(String categorie) throws SQLException {

        String sql = "SELECT * FROM produit WHERE categorie = '" + categorie + "'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<produit> produits = new ArrayList<>();

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
            produits.add(p);
        }
        return produits;
    }

    public List<produit> trierParPrixCroissant() throws SQLException {

        String sql = "SELECT * FROM produit ORDER BY prix ASC";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<produit> produits = new ArrayList<>();

        while (rs.next()) {
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
            produits.add(p);
        }
        return produits;
    }

    public List<produit> trierParPrixDecroissant() throws SQLException {

        String sql = "SELECT * FROM produit ORDER BY prix DESC";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<produit> produits = new ArrayList<>();

        // Parcourir le résultat de la requête et ajouter les produits à la liste
        while (rs.next()) {
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
            produits.add(p);
        }
        return produits;
    }

    public List<produit> getLatestProducts(int limit) throws SQLException {
        List<produit> produits = new ArrayList<>();

        String sql = "SELECT * FROM produit ORDER BY idProduit DESC LIMIT ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
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

            produits.add(p);
        }

        rs.close();
        ps.close();

        return produits;
    }


}

