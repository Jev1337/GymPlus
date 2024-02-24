package services.gestonblog;


import entities.gestionblog.Post;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostServices implements IService{
    private final Connection connection;

    public PostServices() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Object o) throws SQLException {
        Post p = (Post) o;
        if (!Objects.equals(p.getPhoto(), "") || !Objects.equals(p.getContent(), "")) {
            String sql = "insert into post (id_post, user_id, mode, content, date, photo, likes) values (" + p.getId_post() + ", " + p.getUser_id() + ", '" + p.getMode() + "', '" + p.getContent() + "', '" + p.getDate() + "', '" + p.getPhoto() + "', " + p.getLikes() + ");";
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM post WHERE id_post = " + id + ";";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void update(Object o) throws SQLException {
        Post p = (Post) o;
        String sql = "update post set content = ? where id_post = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, p.getContent());
        ps.setInt(2, p.getId_post());
        ps.executeUpdate();
    }

    public void addNbLikes(Object o) throws SQLException {
        Post p = (Post) o;
        String sql = "UPDATE post set likes = ? where id_post = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, p.getLikes() + 1);
        ps.setInt(2, p.getId_post());
        ps.executeUpdate();
    }

    public void minNbLikes(Object o) throws SQLException {
        Post p = (Post) o;
        String sql = "UPDATE post set likes = ? where id_post = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, p.getLikes() - 1);
        ps.setInt(2, p.getId_post());
        ps.executeUpdate();
    }

    public int getNbLikesById(int id) throws SQLException {
        int likes = 0;
        String sql = "select * from post where id_post = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            likes = rs.getInt("likes");
        }
        return likes;
    }

    @Override
    public List<Post> getAll() throws SQLException {
        String sql = "select * from post order by date DESC";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Post> posts = new ArrayList<>();
        while (rs.next()) {
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
