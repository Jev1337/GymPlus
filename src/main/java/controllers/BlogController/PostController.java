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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
    private ImageView imgPost;
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
    private HBox editPost;
    @FXML
    private ScrollPane comntListContainer;
    private Post post = new Post();
    private String date = LocalDate.now().toString();
    private boolean isClicked = false;
    private boolean isShown = false;
    public static int comnts = 0;
    private final PostServices ps = new PostServices();
    private final CommentaireService cs = new CommentaireService();
    private final ClientService us = new ClientService();
    private CommentTemplateController commentTemplateController = new CommentTemplateController();
    private static BlogController blogController;

    public void setBlogController(BlogController bc) {
        PostController.blogController = bc;
    }

    //*************************************POST HANDLER*************************************
    @FXML
    public void addLike() {
        isClicked = !isClicked;
        int likes = post.getLikes();
        try {
            if (isClicked) {
                likeBtn.setTextFill(Paint.valueOf("#005bee"));
                post.setLikes(likes+1);
                nbLikes.setText(post.getLikes() + " Likes");
            } else {
                post.setLikes(likes-1);
                likeBtn.setTextFill(Paint.valueOf("#616770"));
                nbLikes.setText(post.getLikes() + " Likes");
            }
            ps.updateNbLikes(post);
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
            upc.getData(post);
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
            blogController.getAllFromDB();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("post is deleted!");
            a.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PostController() {
    }

    public void setPost(Post p) {
        post = p;
        captionTxt.setText(post.getContent());
        nbLikes.setText(post.getLikes() + " Likes");
        comnts = post.getNbComnts();
        nbComnts.setText(comnts + " Comments");
        Image imgP = new Image(new File("webapp/src/gymplus/public/postPic/" + post.getPhoto()).toURI().toString());
        imgPost.setImage(imgP);
        dateTxt.setText(post.getDate().toString());
        if (post.getUser_id() != GlobalVar.getUser().getId()) {
            editPost.setVisible(false);
            editPost.setManaged(false);
        }
        try {
            userNameTxt.setText(us.getUserById(post.getUser_id()).getUsername());
            String profilePic = us.getUserById(p.getUser_id()).getPhoto();
            Image img = new Image(new File("webapp/src/gymplus/public/postPic/" + profilePic).toURI().toString());
            imgProfile.setImage(img);
            Circle clip1 = new Circle(imgProfile.getFitWidth() / 2, imgProfile.getFitHeight() / 2, imgProfile.getFitWidth() / 2);
            imgProfile.setClip(clip1);
            imgProfile.setPreserveRatio(false);
            if (Objects.equals(p.getPhoto(), "") && p.getPhoto().isEmpty()) {
                imgPost.setVisible(false);
                imgPost.setManaged(false);
            }
        } catch (Exception e) {
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
        if (!comntsList.getChildren().isEmpty()){
            comntsList.getChildren().remove(0,comntsList.getChildren().size());
        }
        try {
            List<Commentaire> commentaireList = new ArrayList<>(cs.getAllCommentsByPostId(idPost));
            for (Commentaire commentaire : commentaireList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/gestionBlog/commentTemplate.fxml"));
                VBox vBox = fxmlLoader.load();
                commentTemplateController = fxmlLoader.getController();
                commentTemplateController.setComment(commentaire, post);
                comntsList.getChildren().add(vBox);
                nbComnts.setText(comnts + " Comments");
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
            comnts++;
            nbComnts.setText(comnts + " Comments");
            post.setNbComnts(comnts);
            ps.updateNbComnts(post);
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
        commentTemplateController.setPostController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmntsContainer.setManaged(false);
    }
}
