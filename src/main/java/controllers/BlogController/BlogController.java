package controllers.BlogController;

import controllers.gestionuser.GlobalVar;
import entities.gestionblog.Post;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import clientMessanger.Client;
import serverMessanger.Server;
import services.gestonblog.PostServices;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BlogController {

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
    @FXML
    private Label clientName;
    @FXML
    private Label clientName1;
    @FXML
    private Label clientName2;
    @FXML
    private Label clientName3;
    @FXML
    private Label clientName4;
    private String nameClient;
    private Server server;
    private final PostServices ps = new PostServices();
    private final UpdatePostController updatePostController = new UpdatePostController();
    private PostController pc = new PostController();
    private ClientMessangerController clientMessangerController = new ClientMessangerController();
    Date date = new Date();
    List<String> badWords = Arrays.asList("fuck", "suck", "kill", "suicide");

    public BlogController() {
    }

    @FXML
    void browse_btn_act() {
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
    String verifContent(String c) {
        String contentVerified = c;
        for (String word : badWords) {
            if (c.toLowerCase().contains(word)) {
                contentVerified = c.replaceAll(word, "****");
            }
        }
        return contentVerified;
    }
    @FXML
    void addPost(ActionEvent event) {
        String photo = "";
        String content = "";
        if (!contentTxt.getText().isEmpty()) {
            content = verifContent(contentTxt.getText());
        }
        File file = new File(photo_tf.getText());
        try {
            if (!photo_tf.getText().isEmpty()) {
                File dest = new File("src/assets/profileuploads/USERIMG" + file.getName().substring(file.getName().lastIndexOf(".")));
                Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                photo = dest.getName();
            }
            Timestamp timeStamp = new Timestamp(date.getTime());
            Post p = new Post(GlobalVar.getUser().getId(), "bbb", content, timeStamp, photo, 0, 0);
            ps.add(p);
            contentTxt.setText("");
            photo_tf.setText("");
            getAllFromDB();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("add:" + e.getMessage());
            alert.show();
        }
    }

    public void getAllFromDB() {
        if (!listPosts.getChildren().isEmpty()) {
            listPosts.getChildren().remove(0, listPosts.getChildren().size());
        }
        try {
            List<Post> postList = new ArrayList<>(ps.getAll());
            for (Post post : postList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/gestionBlog/post.fxml"));
                VBox vBox = fxmlLoader.load();
                pc= fxmlLoader.getController();
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

    void showMessages(String name) throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gestionBlog/ClientForm.fxml"));

        ClientMessangerController controller = new ClientMessangerController();
        controller.setClientName(name); // Set the parameter
        fxmlLoader.setController(controller);

        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.setTitle("Client");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        /*primaryStage.setOnCloseRequest(windowEvent -> {
            controller.shutdown();
        });*/
        primaryStage.show();
    }
    @FXML
    public void addUserBtn() throws IOException {
        showMessages(clientName.getText());
    }
    @FXML
    public void addUserBtn1() throws IOException {
        showMessages(clientName1.getText());
    }
    @FXML
    public void addUserBtn2() throws IOException {
        showMessages(clientName2.getText());
    }
    @FXML
    public void addUserBtn3() throws IOException {
        showMessages(clientName3.getText());
    }
    @FXML
    public void addUserBtn4() throws IOException {
        showMessages(clientName4.getText());
    }
    public void initialize() {

        username.setText(GlobalVar.getUser().getUsername());
        String profilePic = GlobalVar.getUser().getPhoto();
        Image img = new Image(new File("src/assets/profileuploads/" + profilePic).toURI().toString());
        userPic.setImage(img);
        Circle clip1 = new Circle(userPic.getFitWidth() / 2, userPic.getFitHeight() / 2, userPic.getFitWidth() / 2);
        userPic.setClip(clip1);
        userPic.setPreserveRatio(false);
        getAllFromDB();
        pc.setBlogController(this);
        updatePostController.setBlogControllerUpdate(this);
        //Messanger Handler: Client
        clientMessangerController.setClientName(GlobalVar.getUser().getUsername());
        new Thread(() -> {
            try {
                server = Server.getInstance();
                server.makeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
