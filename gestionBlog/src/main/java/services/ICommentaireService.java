package services;

import entities.Commentaire;

import java.sql.SQLException;
import java.util.List;

public interface ICommentaireService {
    void ajouter(Commentaire t) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(Commentaire t) throws SQLException;
    List<Commentaire> recuperer() throws SQLException;
}
