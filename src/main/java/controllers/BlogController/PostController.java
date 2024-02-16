package controllers.BlogController;

import entities.gestionblog.Post;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import services.gestonblog.PostServices;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class PostController {


    @FXML
    private Label captionTxt;

    @FXML
    private ImageView imgPost = new ImageView();

    @FXML
    private ImageView imgProfile;

    @FXML
    private Label nbComnts;

    @FXML
    private Label nbLikes;

    @FXML
    private Label dateTxt;

    @FXML
    private Label userNameTxt;
    @FXML
    private Label likeBtn;

    @FXML
    private HBox likeBtnContainer;
    @FXML
    private VBox idPost;
    private String date = LocalDate.now().toString();
    private boolean isClicked = false;
    private PostServices ps = new PostServices();

    @FXML
    public void addLike() {
        isClicked = !isClicked;
        if (isClicked) {
            likeBtn.setTextFill(Paint.valueOf("#005bee"));
            /*try {
                ps.updateNbLikes(getPost());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }*/
        } else {
            likeBtn.setTextFill(Paint.valueOf("#616770"));
        }
    }

    public PostController() {
    }

    public void setPost(Post p) {
        //idPost.setId(String.valueOf(p.getId_post()));
        captionTxt.setText(p.getContent());
        userNameTxt.setText("ilyes arous");
        nbLikes.setText(p.getLikes() + " Likes");
        nbComnts.setText(p.getLikes() + " Comments");
        dateTxt.setText(p.getDate().toString());
        if (Objects.equals(p.getPhoto(), "null") && p.getPhoto().isEmpty()) {
            imgPost.setVisible(false);
            imgPost.setManaged(false);
        }
    }
    /*public Post getPost(){
        Post p = new Post();
        //p.setId_post(Integer.parseInt(idPost.getId()));
        p.setDate(Date.valueOf(dateTxt.getText()));
        p.setContent(captionTxt.getText());
        p.setLikes(Integer.parseInt(nbLikes.getText()));
        p.setPhoto(imgPost.getImage().toString());
        p.setUser_id(10);
        return p;
    }*/


}
