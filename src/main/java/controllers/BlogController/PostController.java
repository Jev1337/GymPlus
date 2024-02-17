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

    private int idPost;
    private final String date = LocalDate.now().toString();
    private boolean isClicked = false;
    private final PostServices ps = new PostServices();

    @FXML
    public void addLike() {
        isClicked = !isClicked;
        Post p = getPost();
        try {
            if (isClicked) {
                likeBtn.setTextFill(Paint.valueOf("#005bee"));
                ps.addNbLikes(p);
                nbLikes.setText(ps.getNbLikesById(idPost) + " Likes");
            } else {
                ps.minNbLikes(p);
                likeBtn.setTextFill(Paint.valueOf("#616770"));
                nbLikes.setText(ps.getNbLikesById(idPost) + " Likes");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PostController() {
    }

    public void setPost(Post p) {
        idPost = p.getId_post();
        captionTxt.setText(p.getContent());
        userNameTxt.setText("ilyes arous");
        nbLikes.setText(p.getLikes() + " Likes");
        nbComnts.setText(p.getLikes() + " Comments");
        dateTxt.setText(p.getDate().toString());
        if (Objects.equals(p.getPhoto(), "") && p.getPhoto().isEmpty()) {
            imgPost.setVisible(false);
            imgPost.setManaged(false);
        }
    }

    public Post getPost() {
        Post p = new Post();
        p.setId_post(idPost);
        String[] arrOfStr = nbLikes.getText().split(" ");
        p.setLikes(Integer.parseInt(arrOfStr[0]));
        return p;
    }


}
