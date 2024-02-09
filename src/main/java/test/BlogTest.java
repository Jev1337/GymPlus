package test;

import entities.gestionblog.Commentaire;
import entities.gestionblog.Post;
import services.gestonblog.CommentaireService;
import services.gestonblog.PostServices;
import utils.MyDatabase;

import java.sql.SQLException;
import java.time.LocalDate;

public class BlogTest {
    public static void main(String[] args) {
        String date = LocalDate.now().toString();
        MyDatabase db = new MyDatabase();
        Post p = new Post(1, 2, "bbb", "abcd", java.sql.Date.valueOf(date), "dbf", 10);
        Commentaire c = new Commentaire(1,2,1,"zsdzsf",java.sql.Date.valueOf(date), 1);
        Commentaire c1 = new Commentaire(2,2,1,"zsdzsf",java.sql.Date.valueOf(date), 1);
        PostServices ps = new PostServices();
        CommentaireService cs = new CommentaireService();

        try {
            ps.add(p);
            //ps.modifier(p);
            //cs.supprimer(2);
            System.out.println(ps.getAll());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
