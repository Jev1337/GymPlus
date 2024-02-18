package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Post;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import services.gestonblog.PostServices;

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

    @FXML
    void updatePost() {
        try {
            p.setContent(contentTxt.getText());
            ps.update(p);
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
    }
}
