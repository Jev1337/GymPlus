package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Commentaire;
import entities.gestionblog.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import services.gestionuser.ClientService;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;


public class CommentTemplateController {
    @FXML
    private Label content;

    @FXML
    private Label date;

    @FXML
    private Label userName;
    @FXML
    private ImageView profilePic;
    private Commentaire commentaire = new Commentaire();
    private final ClientService us = new ClientService();

    public CommentTemplateController() {
    }

    public void setComment(Commentaire c) {
        commentaire = c;
        content.setText(c.getContent());
        userName.setText("ilyes arous");
        try {
            String profilePicture = us.getUserById(c.getUser_id()).getPhoto();
            Image img = new Image(new File("src/assets/profileuploads/" + profilePicture).toURI().toString());
            profilePic.setImage(img);
            Circle clip1 = new Circle(profilePic.getFitWidth()/2, profilePic.getFitHeight()/2, profilePic.getFitWidth()/2);
            profilePic.setClip(clip1);
            profilePic.setPreserveRatio(false);
            //nbLikes.setText(p.getLikes() + " Likes");
            //nbComnts.setText(p.getLikes() + " Comments");
            date.setText(c.getDate().toString());
       /* if (Objects.equals(p.getPhoto(), "") && p.getPhoto().isEmpty()) {
            imgPost.setVisible(false);
            imgPost.setManaged(false);
        }*/
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Commentaire getComment() {
        Commentaire c = new Commentaire();
        c.setId_comment(commentaire.getId_comment());
        //String[] arrOfStr = nbLikes.getText().split(" ");
        //c.setLikes(Integer.parseInt(arrOfStr[0]));
        //c.setContent(captionTxt.getText());
        c.setDate(Date.valueOf(date.getText()));
        return c;
    }

}
