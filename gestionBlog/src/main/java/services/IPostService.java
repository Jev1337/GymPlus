package services;

import entities.Post;

import java.sql.SQLException;
import java.util.List;

public interface IPostService {
    void ajouter(Post t) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(Post t) throws SQLException;
    List<Post> recuperer() throws SQLException;
}
