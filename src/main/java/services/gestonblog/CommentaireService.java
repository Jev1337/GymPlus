package services.gestonblog;


import entities.gestionblog.Commentaire;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements IService {

    private final Connection connection;

    public CommentaireService(){
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Object o) throws SQLException {
        Commentaire c = (Commentaire) o;
        String sql = "insert into commentaire (id_comment, user_id, id_post, content, date, likes) values ("+c.getId_comment() + ", " + c.getUser_id()  + ", "+c.getId_post()+ ", '" + c.getContent() +"', '" + c.getDate()+  "', " + c.getLikes() + ");";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM commentaire WHERE id_comment = "+id+";";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }
    public void deleteComntsByPostId(int id) throws SQLException {
        String sql = "DELETE FROM commentaire WHERE id_post = "+id+";";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void update(Object o) throws SQLException {
        Commentaire c = (Commentaire) o;
        String sql = "update commentaire set id_comment = ?, user_id = ?, id_post = ?, content = ?, date = ?, likes = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, c.getId_comment());
        ps.setInt(2, c.getUser_id());
        ps.setInt(3, c.getId_post());
        ps.setString(4, c.getContent());
        ps.setDate(4, c.getDate());
        ps.setInt(5, c.getLikes());
        ps.executeUpdate();
    }

    public  List<Commentaire> getAllCommentsByPostId(int id) throws SQLException {
        String sql = "select * from commentaire where id_post = " +id+";";
        Statement ps = connection.createStatement();
        //ps.setInt(1, id);
        ResultSet rs = ps.executeQuery(sql);
        List<Commentaire> comments = new ArrayList<>();
        while (rs.next()){
            Commentaire p = new Commentaire();
            p.setId_comment(rs.getInt("id_comment"));
            p.setUser_id(rs.getInt("user_id"));
            p.setId_post(rs.getInt("id_post"));
            p.setContent(rs.getString("content"));
            p.setDate(rs.getDate("date"));
            p.setLikes(rs.getInt("likes"));
            comments.add(p);
        }
        return comments;
    }
    @Override
    public List<Commentaire> getAll() throws SQLException {
        String sql = "select * from commentaire ";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Commentaire> comments = new ArrayList<>();
        while (rs.next()){
            Commentaire p = new Commentaire();
            p.setId_comment(rs.getInt("id_comment"));
            p.setUser_id(rs.getInt("user_id"));
            p.setId_post(rs.getInt("id_post"));
            p.setContent(rs.getString("content"));
            p.setDate(rs.getDate("date"));
            p.setLikes(rs.getInt("likes"));
            comments.add(p);
        }
        return comments;
    }

}
