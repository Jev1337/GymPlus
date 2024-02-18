package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import services.gestonblog.PostServices;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class BlogController implements Initializable {

    public BlogController() {
    }

    @FXML
    private TextArea contentTxt = new TextArea();

    @FXML
    private VBox listPosts = new VBox();
    @FXML
    private ImageView userPic;

    @FXML
    private Text username;
    private final PostServices ps = new PostServices();
    String date = LocalDate.now().toString();

    @FXML
    void addPost(ActionEvent event) {
        Post p = new Post(GlobalVar.getUser().getId(), "bbb", contentTxt.getText(), java.sql.Date.valueOf(date), "", 0);
        try {
            ps.add(p);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("post added");
            alert.show();
            contentTxt.setText("");
            listPosts.getChildren().remove(0, listPosts.getChildren().size());
            getAllFromDB();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void getAllFromDB (){
        if (!listPosts.getChildren().isEmpty()){
            listPosts.getChildren().remove(0, listPosts.getChildren().size());
        }
        try {
            List<Post> postList = new ArrayList<>(ps.getAll());
            for (Post post : postList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/gestionBlog/post.fxml"));
                VBox vBox = fxmlLoader.load();
                PostController pc = fxmlLoader.getController();
                pc.setPost(post);
                listPosts.getChildren().add(vBox);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(GlobalVar.getUser().getUsername());
        String profilePic = GlobalVar.getUser().getPhoto();
        Image img = new Image(new File("src/assets/profileuploads/" + profilePic).toURI().toString());
        userPic.setImage(img);
        Circle clip1 = new Circle(userPic.getFitWidth()/2, userPic.getFitHeight()/2, userPic.getFitWidth()/2);
        userPic.setClip(clip1);
        userPic.setPreserveRatio(false);
        getAllFromDB();
    }
}
