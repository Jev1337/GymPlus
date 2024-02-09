package services;

import entities.Commentaire;
import utils.MyDataBase;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements ICommentaireService{

    private final Connection connection;

    public CommentaireService(){
        connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Commentaire p) throws SQLException {
        String sql = "insert into commentaire (id_comment, user_id, id_post, content, date, likes) values ("+p.getId_comment() + ", " + p.getUser_id()  + ", "+p.getId_post()+ ", '" + p.getContent() +"', '" + p.getDate()+  "', " + p.getLikes() + ");";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM commentaire WHERE id_comment = "+id+";";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void modifier(Commentaire p) throws SQLException {
        String sql = "update commentaire set id_comment = ?, user_id = ?, id_post = ?, content = ?, date = ?, likes = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, p.getId_comment());
        ps.setInt(2, p.getUser_id());
        ps.setInt(3, p.getId_post());
        ps.setString(4, p.getContent());
        ps.setDate(4, p.getDate());
        ps.setInt(5, p.getLikes());
        ps.executeUpdate();
    }

    @Override
    public List<Commentaire> recuperer() throws SQLException {
        String sql = "select * from commentaire";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Commentaire> posts = new ArrayList<>();
        while (rs.next()){
            Commentaire p = new Commentaire();
            p.setId_comment(rs.getInt("id_comment"));
            p.setUser_id(rs.getInt("user_id"));
            p.setId_post(rs.getInt("id_post"));
            p.setContent(rs.getString("content"));
            p.setDate(rs.getDate("date"));
            p.setLikes(rs.getInt("likes"));
            posts.add(p);
        }
        return posts;
    }

}
