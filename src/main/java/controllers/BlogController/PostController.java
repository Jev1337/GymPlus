package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Commentaire;
import entities.gestionblog.Post;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.gestionuser.AdminService;
import services.gestionuser.ClientService;
import services.gestonblog.CommentaireService;
import services.gestonblog.PostServices;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PostController implements Initializable {


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
    private TextField comntContent;

    @FXML
    private Label userNameTxt;
    @FXML
    private Label likeBtn;
    @FXML
    private VBox loadUpdate;
    @FXML
    private VBox cmntsContainer;
    @FXML
    private VBox comntsList;
    @FXML
    private ScrollPane comntListContainer;
    private Post post = new Post();
    private String date = LocalDate.now().toString();
    private boolean isClicked = false;
    private boolean isShown = false;
    private final PostServices ps = new PostServices();
    private final CommentaireService cs = new CommentaireService();
    private final ClientService us = new ClientService();
    private final BlogController blogController = new BlogController();

    //*************************************POST HANDLER*************************************
    @FXML
    public void addLike() {
        isClicked = !isClicked;
        Post p = getPost();
        try {
            if (isClicked) {
                likeBtn.setTextFill(Paint.valueOf("#005bee"));
                ps.addNbLikes(p);
                nbLikes.setText(ps.getNbLikesById(post.getId_post()) + " Likes");
            } else {
                ps.minNbLikes(p);
                likeBtn.setTextFill(Paint.valueOf("#616770"));
                nbLikes.setText(ps.getNbLikesById(post.getId_post()) + " Likes");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void showUpdatePost() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gestionBlog/UpdatePost.fxml"));
            loadUpdate = loader.load();
            UpdatePostController upc = loader.getController();
            upc.getData(getPost());
            ScaleTransition st = new ScaleTransition(Duration.millis(100), loadUpdate);
            st.setInterpolator(Interpolator.EASE_IN);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);
            Stage stage = new Stage();
            stage.setTitle("Update Post");
            Scene scene = new Scene(loadUpdate, 413, 190);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void deletePost() {
        try {
            cs.deleteComntsByPostId(post.getId_post());
            ps.delete(post.getId_post());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PostController() {
    }

    public void setPost(Post p) {
        post = p;
        captionTxt.setText(post.getContent());
        userNameTxt.setText(GlobalVar.getUser().getUsername());
        nbLikes.setText(post.getLikes() + " Likes");
        nbComnts.setText(post.getLikes() + " Comments");
        dateTxt.setText(post.getDate().toString());
        try {
            String profilePic = us.getUserById(p.getUser_id()).getPhoto();
            Image img = new Image(new File("src/assets/profileuploads/" + profilePic).toURI().toString());
            imgProfile.setImage(img);
            Circle clip1 = new Circle(imgProfile.getFitWidth()/2, imgProfile.getFitHeight()/2, imgProfile.getFitWidth()/2);
            imgProfile.setClip(clip1);
            imgProfile.setPreserveRatio(false);
            if (Objects.equals(p.getPhoto(), "") && p.getPhoto().isEmpty()) {
                imgPost.setVisible(false);
                imgPost.setManaged(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Post getPost() {
        String[] arrOfStr = nbLikes.getText().split(" ");
        post.setLikes(Integer.parseInt(arrOfStr[0]));
        post.setContent(captionTxt.getText());
        post.setDate(Date.valueOf(dateTxt.getText()));
        return post;
    }

    //*************************************CMNTS HANDLER*************************************

    public void getAllComments(int idPost) {
        try {
            List<Commentaire> commentaireList = new ArrayList<>(cs.getAllCommentsByPostId(idPost));
            for (Commentaire commentaire : commentaireList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/gestionBlog/commentTemplate.fxml"));
                VBox vBox = fxmlLoader.load();
                CommentTemplateController commentTemplateController = fxmlLoader.getController();
                commentTemplateController.setComment(commentaire);
                comntsList.getChildren().add(vBox);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    void addComnt(ActionEvent event) {
        Commentaire commentaire = new Commentaire(GlobalVar.getUser().getId(), post.getId_post(), comntContent.getText(), java.sql.Date.valueOf(date), 0);
        try {
            cs.add(commentaire);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("post added");
            alert.show();
            if (!comntListContainer.isVisible()) {
                comntListContainer.setVisible(true);
                comntListContainer.setManaged(true);
                comntsList.setVisible(true);
                comntsList.setManaged(true);
            }
            comntContent.setText("");
            comntsList.getChildren().remove(0, comntsList.getChildren().size());
            getAllComments(post.getId_post());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    void showComnts() throws SQLException {
        isShown = !isShown;
        cmntsContainer.setVisible(isShown);
        cmntsContainer.setManaged(isShown);
        if (cs.getAllCommentsByPostId(post.getId_post()).isEmpty()) {
            comntListContainer.setManaged(false);
            comntListContainer.setVisible(false);
            comntsList.setVisible(false);
            comntsList.setManaged(false);
        } else {
            comntsList.getChildren().remove(0, comntsList.getChildren().size());
            getAllComments(post.getId_post());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmntsContainer.setManaged(false);
    }
}
