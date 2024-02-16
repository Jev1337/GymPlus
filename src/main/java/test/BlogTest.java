package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BlogTest extends Application {
    public static void main(String[] args) {
        /*String date = LocalDate.now().toString();
        MyDatabase db = new MyDatabase();
        Post p = new Post(10, "bbb", "abcd", java.sql.Date.valueOf(date), "dbf", 10);
        Commentaire c = new Commentaire(10,1,"zsdzsf",java.sql.Date.valueOf(date), 1);
        Commentaire c1 = new Commentaire(10,1,"zsdzsf",java.sql.Date.valueOf(date), 1);
        PostServices ps = new PostServices();
        CommentaireService cs = new CommentaireService();

        // CRUD Post
        try {
            //ps.add(p);
            //ps.modifier(p);
            //cs.supprimer(2);
            System.out.println(ps.getAll());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // CRUD Commentaire
        try {
            cs.add(c1);
            //ps.modifier(p);
            //cs.delete(3);
            System.out.println(cs.getAll());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }*/
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionBlog/Blog.fxml"));
        Parent root = loader.load(); // container yehriti ml parent(kima Object)
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
