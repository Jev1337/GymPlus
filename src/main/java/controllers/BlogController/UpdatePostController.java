package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import services.gestionuser.ClientService;
import services.gestonblog.PostServices;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class UpdatePostController {

    @FXML
    private TextArea contentTxt = new TextArea();

    @FXML
    private Text userName = new Text();

    @FXML
    private ImageView userPic;
    private final PostServices ps = new PostServices();
    private Post p = new Post();
    private final ClientService us = new ClientService();
    private static BlogController blogController;
    public void setBlogControllerUpdate(BlogController bc){
        UpdatePostController.blogController = bc;
    }
    @FXML
    void updatePost() {
        try {
            p.setContent(contentTxt.getText());
            ps.update(p);
            blogController.getAllFromDB();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("post updated!");
            alert.show();
            contentTxt.setText("");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public UpdatePostController() {
    }

    public void getData(Post post){
        p = post;
        contentTxt.setText(p.getContent());
        userName.setText(GlobalVar.getUser().getUsername());
        try {
        String profilePic = us.getUserById(p.getUser_id()).getPhoto();
        Image img = new Image(new File("webapp/src/gymplus/public/profileuploads/" + profilePic).toURI().toString());
        userPic.setImage(img);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
