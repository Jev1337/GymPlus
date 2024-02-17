package controllers.BlogController;

import entities.gestionblog.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import services.gestonblog.PostServices;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddBlogController implements Initializable {

    @FXML
    private TextArea contentTxt = new TextArea();

    @FXML
    private VBox listPosts = new VBox();
    private final PostServices ps = new PostServices();
    String date = LocalDate.now().toString();

    @FXML
    void addPost(ActionEvent event) {
        Post p = new Post(10, "bbb", contentTxt.getText(), java.sql.Date.valueOf(date), "", 10);
        try {
            ps.add(p);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("post added");
            alert.show();
            getAllFromDB();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }

    }
    public void getAllFromDB (){
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
        getAllFromDB();
    }
}
