package test;

import entities.Commentaire;
import entities.Post;
import services.CommentaireService;
import services.PostServices;
import utils.MyDataBase;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        String date = LocalDate.now().toString();
        MyDataBase db = new MyDataBase();
        Post p = new Post(1, 2, "bbb", "abcd", java.sql.Date.valueOf(date), "dbf", 10);
        Commentaire c = new Commentaire(1,2,1,"zsdzsf",java.sql.Date.valueOf(date), 1);
        Commentaire c1 = new Commentaire(2,2,1,"zsdzsf",java.sql.Date.valueOf(date), 1);
        PostServices ps = new PostServices();
        CommentaireService cs = new CommentaireService();

        try {
            ps.ajouter(p);
            //ps.modifier(p);
            //cs.supprimer(2);
            System.out.println(ps.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
