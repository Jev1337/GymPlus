package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.gestonblog.PostServices;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private Label photo_tf;
    @FXML
    private Text username;
    private final PostServices ps = new PostServices();
    Date date = new Date();
    List<String> badWords = Arrays.asList("fuck", "suck", "kill", "suicide");

    @FXML
    void browse_btn_act(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        fileChooser.setTitle("Choose a profile picture");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            photo_tf.setText(file.getAbsolutePath());
        }
    }

    String verifContent(String c){
        String contentVerified = c;
        for (String word : badWords){
            if (c.toLowerCase().contains(word)){
                contentVerified = c.replaceAll(word, "****");
            }
        }
        return contentVerified;
    }

    @FXML
    void addPost(ActionEvent event) {
        String photo = "";
        String content = "";
        if (!contentTxt.getText().isEmpty()){
            content = verifContent(contentTxt.getText());
        }
        File file = new File(photo_tf.getText());
        try {
            if (!photo_tf.getText().isEmpty()){
                File dest = new File("src/assets/profileuploads/USERIMG" + file.getName().substring(file.getName().lastIndexOf(".")));
                Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                photo = dest.getName();
            }
            Timestamp timeStamp = new Timestamp(date.getTime());
            Post p = new Post(GlobalVar.getUser().getId(), "bbb", content, timeStamp, photo, 0);
            ps.add(p);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("post added");
            alert.show();
            contentTxt.setText("");
            photo_tf.setText("");
            getAllFromDB();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("add:" +e.getMessage());
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
            alert.setContentText("post: " + e.getMessage());
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
