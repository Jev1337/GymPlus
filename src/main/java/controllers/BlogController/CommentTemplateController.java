package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Commentaire;
import entities.gestionblog.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import services.gestionuser.ClientService;
import services.gestonblog.CommentaireService;
import services.gestonblog.PostServices;

import java.io.File;
import java.sql.SQLException;


public class CommentTemplateController {
    @FXML
    private TextField content;
    @FXML
    private Label date;
    @FXML
    private Label userName;
    @FXML
    private ImageView profilePic;
    @FXML
    private HBox editComnt;
    @FXML
    private Button updateBtn;
    private Commentaire commentaire = new Commentaire();
    private Post post = new Post();
    private final ClientService us = new ClientService();
    private final CommentaireService cs = new CommentaireService();
    private final PostServices ps = new PostServices();
    private static PostController postController;
    public void setPostController(PostController pc){
        CommentTemplateController.postController = pc;
    }

    public CommentTemplateController() {
    }
    @FXML
    void showUpdateComnt(){
        content.setDisable(false);
        updateBtn.setDisable(false);
    }
    @FXML
    void deleteComnt(){
        try {
            cs.delete(commentaire.getId_comment());
            PostController.comnts--;
            post.setNbComnts(PostController.comnts);
            ps.updateNbComnts(post);
            postController.getAllComments(post.getId_post());
        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
        }
    }
    @FXML
    void updateComnt(){
        commentaire.setContent(content.getText());
        try {
            cs.update(commentaire);
            content.setDisable(true);
            updateBtn.setDisable(true);

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
        }
    }

    public void setComment(Commentaire c, Post p) {
        commentaire = c;
        post = p;
        content.setText(c.getContent());
        if (commentaire.getUser_id() != GlobalVar.getUser().getId()){
            editComnt.setVisible(false);
            editComnt.setManaged(false);
        }
        try {
            userName.setText(us.getUserById(commentaire.getUser_id()).getUsername());
            String profilePicture = us.getUserById(c.getUser_id()).getPhoto();
            Image img = new Image(new File("src/assets/profileuploads/" + profilePicture).toURI().toString());
            profilePic.setImage(img);
            Circle clip1 = new Circle(profilePic.getFitWidth()/2, profilePic.getFitHeight()/2, profilePic.getFitWidth()/2);
            profilePic.setClip(clip1);
            profilePic.setPreserveRatio(false);
            date.setText(c.getDate().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
