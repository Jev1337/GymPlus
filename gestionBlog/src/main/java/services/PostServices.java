package services;

import entities.Post;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostServices implements IPostService {
    private final Connection connection;

    public PostServices(){
        connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Post p) throws SQLException {
        String sql = "insert into post (id_post, user_id, mode, content, date, photo, likes) values ("+p.getId_post() + ", " + p.getUser_id() + ", '"+ p.getMode() + "', '" + p.getContent() +"', '" + p.getDate()+ "', '" + p.getPhoto()+ "', " + p.getLikes() + ");";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM post WHERE id_post = "+id+";";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void modifier(Post p) throws SQLException {
        String sql = "update post set id_post = ?, user_id = ?, mode = ? , content = ?, date = ?, photo = ?, likes = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, p.getId_post());
        ps.setInt(2, p.getUser_id());
        ps.setString(3, p.getMode());
        ps.setString(4, p.getContent());
        ps.setDate(5, p.getDate());
        ps.setString(6, p.getPhoto());
        ps.setInt(7, p.getLikes());
        ps.executeUpdate();
    }

    @Override
    public List<Post> recuperer() throws SQLException {
        String sql = "select * from post";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Post> posts = new ArrayList<>();
        while (rs.next()){
            Post p = new Post();
            p.setId_post(rs.getInt("id_post"));
            p.setUser_id(rs.getInt("user_id"));
            p.setMode(rs.getString("mode"));
            p.setContent(rs.getString("content"));
            p.setDate(rs.getDate("date"));
            p.setPhoto(rs.getString("photo"));
            p.setLikes(rs.getInt("likes"));
            posts.add(p);
        }
        return posts;
    }
}
