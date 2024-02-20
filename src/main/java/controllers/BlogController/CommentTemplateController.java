package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Commentaire;
import entities.gestionblog.Post;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.gestionuser.ClientService;
import services.gestonblog.CommentaireService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;


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
    private final ClientService us = new ClientService();
    private final CommentaireService cs = new CommentaireService();

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
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("comment deleted successfully!");
            a.show();
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
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("comment updated successfully!");
            a.show();
        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
        }
    }

    public void setComment(Commentaire c) {
        commentaire = c;
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
