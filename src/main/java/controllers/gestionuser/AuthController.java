package controllers.gestionuser;

import animatefx.animation.*;
import com.password4j.Password;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionuser.Admin;
import entities.gestionuser.Client;
import entities.gestionuser.Staff;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import okhttp3.*;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import services.gestionuser.AdminService;
import services.gestionuser.ClientService;
import services.gestionuser.StaffService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;


public class AuthController {

    String faceId = "";
    private StaffService staffService = new StaffService();
    private ClientService clientService = new ClientService();
    private AdminService adminService = new AdminService();
    private FadeInRight fadeInRightAnimation = new FadeInRight();
    private FadeInLeft fadeInLeftAnimation = new FadeInLeft();
    private FadeOutLeft fadeOutLeftAnimation = new FadeOutLeft();
    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();

    @FXML
    public ImageView faceid_btn;
    @FXML
    public FontAwesomeIconView google_btn;
    @FXML
    public Button browse_btn;

    @FXML
    private TextField address_tf;

    @FXML
    private Button back_btn;

    @FXML
    private TextField cin_tf;

    @FXML
    private Button close_btn;

    @FXML
    private PasswordField confpwd_pf;

    @FXML
    private DatePicker date_dp;

    @FXML
    private Pane dragpane;

    @FXML
    private TextField email_tf;

    @FXML
    private TextField emailsu_tf;

    @FXML
    private TextField firstname_tf;

    @FXML
    private TextField lastname_tf;

    @FXML
    private Button minimize_btn;

    @FXML
    private Button next_btn;

    @FXML
    private TextField phone_tf;

    @FXML
    private TextField photo_tf;

    @FXML
    private PasswordField pwd_pf;

    @FXML
    private PasswordField pwdsu_pf;

    @FXML
    private CheckBox setupfr_checkbox;

    @FXML
    private Button signin_btn;

    @FXML
    private Pane signin_pane;

    @FXML
    private Button signin_switch_btn;

    @FXML
    private Pane signin_switch_pane;

    @FXML
    private Button signup_btn;

    @FXML
    private Pane signup_pane;

    @FXML
    private Button signup_switch_btn;

    @FXML
    private Pane signup_switch_pane;

    @FXML
    private Pane signupad_pane;

    @FXML
    private TextField username_tf;

    @FXML
    private Label pwstrength_label;

    @FXML
    private ProgressBar pwstrength_progress;

    @FXML
    void pwdsu_pf_pressed(KeyEvent event) {
        if (pwdsu_pf.getText().isEmpty()) {
            pwstrength_progress.setProgress(0);
            pwstrength_label.setText("Password Strength: Poor");
        }
        else if (pwdsu_pf.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$")) {
            pwstrength_label.setText("Password Strength: Strong");
            pwstrength_progress.setProgress(1);
            pwstrength_progress.setStyle("-fx-fill: green;");
        }
        else if (pwdsu_pf.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.* ).{8,16}$")){
            pwstrength_label.setText("Password Strength: Medium");
            pwstrength_progress.setProgress(0.6);
            pwstrength_progress.setStyle("-fx-fill: orange;");
        }
        else if (pwdsu_pf.getText().matches("^[a-zA-Z0-9]*$")){
            pwstrength_label.setText("Password Strength: Weak");
            pwstrength_progress.setProgress(0.3);
            pwstrength_progress.setStyle("-fx-fill: red;");
        }


    }

    @FXML
    void back_btn_act(ActionEvent event) {
        fadeOutRightAnimation.setNode(signupad_pane);
        fadeOutRightAnimation.setOnFinished(e -> {
            signupad_pane.setVisible(false);
            signin_switch_pane.setOpacity(0);
            signin_switch_pane.setVisible(true);
            fadeInLeftAnimation.setNode(signin_switch_pane);
            fadeInLeftAnimation.play();
            signup_pane.setVisible(true);
            fadeInLeftAnimation.setNode(signup_pane);
            fadeInLeftAnimation.play();
        });
        fadeOutRightAnimation.play();
    }

    @FXML
    void signup_btn_act(ActionEvent event) {
        if (cin_tf.getText().isEmpty() || firstname_tf.getText().isEmpty() || lastname_tf.getText().isEmpty() || date_dp.getValue() == null || address_tf.getText().isEmpty() || phone_tf.getText().isEmpty() || photo_tf.getText().isEmpty() || username_tf.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Please fill in all the fields!");
            alert.showAndWait();
            return;
        }
        if (!validateText(firstname_tf.getText()))
            return;
        if (!validateText(lastname_tf.getText()))
            return;
        if (!validateText(phone_tf.getText()))
            return;
        if (!validateNumber(cin_tf.getText()))
            return;
        if (!validateText(address_tf.getText()))
            return;
        if (!validateNumber(cin_tf.getText()))
            return;


        File file = new File(photo_tf.getText());
        if (!file.exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Invalid photo path! Please try again.");
            alert.showAndWait();
            return;
        }
        try {
            if (clientService.getUserById(Integer.parseInt(cin_tf.getText())) != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning");
                alert.setContentText("Staff with this username already exists! Please try another one.");
                alert.showAndWait();
                return;
            }

            if (setupfr_checkbox.isSelected()){
                disableAllSU();
                setFaceID();
                return;
            }
            File dest = new File("src/assets/profileuploads/USERIMG" + cin_tf.getText() + file.getName().substring(file.getName().lastIndexOf(".")));
            Files.copy(file.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            clientService.add(new Client(Integer.parseInt(cin_tf.getText()), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), date_dp.getValue().toString(), pwdsu_pf.getText(), emailsu_tf.getText(), phone_tf.getText(), address_tf.getText(), dest.getName(), "", new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Success");
            alert.setHeaderText("Success");
            alert.setContentText("Your account has been created successfully! Please sign in to continue.");
            alert.showAndWait();

            fadeOutRightAnimation.setNode(signupad_pane);
            fadeOutRightAnimation.setOnFinished(e -> {
                signupad_pane.setVisible(false);
                signup_switch_pane.setOpacity(0);
                signup_switch_pane.setVisible(true);
                fadeInLeftAnimation.setNode(signup_switch_pane);
                fadeInLeftAnimation.play();
                signin_pane.setVisible(true);
                signin_pane.setOpacity(0);
                fadeInLeftAnimation.setNode(signin_pane);
                fadeInLeftAnimation.play();
            });
            fadeOutRightAnimation.play();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while trying to connect to the database! Please try again later.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private boolean validateNumber(String number){
        if (!number.matches("^[0-9]{8}$")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Invalid phone number format! Please try again.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    void browse_btn_act(ActionEvent event) {
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
    @FXML
    void close_btn_act(ActionEvent event) {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }
    @FXML
    void minimize_btn_act(ActionEvent event) {
        Stage stage = (Stage) minimize_btn.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    void signin_btn_act(ActionEvent event) {
        try {
            Client client = clientService.getUserByEmail(email_tf.getText());
            Admin admin = adminService.getUserByEmail(email_tf.getText());
            Staff staff = staffService.getUserByEmail(email_tf.getText());
            if (client == null && admin == null && staff == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning");
                alert.setContentText("Client with this email does not exist! Please try again.");
                alert.showAndWait();
                return;
            }
            if (client != null && Password.check(pwd_pf.getText(), client.getPassword()).withBcrypt()){
                GlobalVar.setUser(client);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/userDashboard.fxml"));
                Parent root = loader.load();
                signin_btn.getScene().getWindow().setWidth(1200);
                signin_btn.getScene().getWindow().setHeight(720);
                signin_btn.getScene().setRoot(root);
                return;
            }
            if (admin != null && Password.check(pwd_pf.getText(), admin.getPassword()).withBcrypt()){
                GlobalVar.setUser(admin);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/adminDashboard.fxml"));
                Parent root = loader.load();
                signin_btn.getScene().getWindow().setWidth(1200);
                signin_btn.getScene().getWindow().setHeight(720);
                signin_btn.getScene().setRoot(root);
                return;
            }
            if (staff != null && Password.check(pwd_pf.getText(), staff.getPassword()).withBcrypt()){
                GlobalVar.setUser(staff);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/staffDashboard.fxml"));
                Parent root = loader.load();
                signin_btn.getScene().getWindow().setWidth(1200);
                signin_btn.getScene().getWindow().setHeight(720);
                signin_btn.getScene().setRoot(root);
                return;
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Incorrect password! Please try again.");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while trying to connect to the database! Please try again later.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private boolean validateEmail(String email){
        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Invalid email format! Please try again.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean validateText(String username){
        if (username.length() < 4 || username.length() > 20){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Username must be between 4 and 20 characters long! Please try again.");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    @FXML
    void next_btn_act(ActionEvent event) {
        if (username_tf.getText().isEmpty() || pwdsu_pf.getText().isEmpty() || emailsu_tf.getText().isEmpty() || confpwd_pf.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Please fill in all the fields!");
            alert.showAndWait();
            return;
        }
        if (!pwdsu_pf.getText().equals(confpwd_pf.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Passwords do not match! Please try again.");
            alert.showAndWait();
            return;
        }
        if (!validateEmail(emailsu_tf.getText()))
            return;
        if (!validateText(username_tf.getText()))
            return;
        if (!validateText(pwdsu_pf.getText()))
            return;

        try {
            if (clientService.getUserByUsername(username_tf.getText()) != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning");
                alert.setContentText("Client with this username already exists! Please try another one.");
                alert.showAndWait();
                return;
            } else if (clientService.getUserByEmail(username_tf.getText()) != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning");
                alert.setContentText("Client with this username already exists! Please try another one.");
                alert.showAndWait();
                return;
            }
        }catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while trying to connect to the database! Please try again later.");
            alert.showAndWait();
        }
        fadeOutLeftAnimation.setNode(signup_pane);
        fadeOutLeftAnimation.play();
        fadeOutLeftAnimation.setNode(signin_switch_pane);
        fadeOutLeftAnimation.setOnFinished(e -> {
            signin_switch_pane.setVisible(false);
            signup_pane.setVisible(false);
            signupad_pane.setVisible(true);
            signupad_pane.setOpacity(0);
            fadeInRightAnimation.setNode(signupad_pane);
            fadeInRightAnimation.play();
        });
        fadeOutLeftAnimation.play();
    }
    @FXML
    void signup_switch_btn_act(ActionEvent event) {
        fadeOutLeftAnimation.setNode(signin_pane);
        fadeOutLeftAnimation.setOnFinished(e -> {
            signin_switch_pane.setOpacity(0);
            signin_switch_pane.setVisible(true);
            signin_pane.setVisible(false);
            fadeInLeftAnimation.setNode(signin_switch_pane);
            fadeInLeftAnimation.play();

        });
        fadeOutLeftAnimation.play();
        fadeOutRightAnimation.setNode(signup_switch_pane);
        fadeOutRightAnimation.setOnFinished(e -> {
            signup_pane.setOpacity(0);
            signup_pane.setVisible(true);
            signup_switch_pane.setVisible(false);
            fadeInRightAnimation.setNode(signup_pane);
            fadeInRightAnimation.play();
        });
        fadeOutRightAnimation.play();
    }
    @FXML
    void signin_switch_btn_act(ActionEvent event) {
        fadeOutRightAnimation.setNode(signup_pane);
        fadeOutRightAnimation.setOnFinished(e -> {
            signup_switch_pane.setOpacity(0);
            signup_switch_pane.setVisible(true);
            signup_pane.setVisible(false);
            fadeInRightAnimation.setNode(signup_switch_pane);
            fadeInRightAnimation.play();
        });
        fadeOutRightAnimation.play();
        fadeOutLeftAnimation.setNode(signin_switch_pane);
        fadeOutLeftAnimation.setOnFinished(e -> {
            signin_pane.setOpacity(0);
            signin_pane.setVisible(true);
            signin_switch_pane.setVisible(false);
            fadeInLeftAnimation.setNode(signin_pane);
            fadeInLeftAnimation.play();
        });
        fadeOutLeftAnimation.play();
    }
    public void google_btn_clicked(MouseEvent mouseEvent) {

    }
    public void faceid_btn_clicked(MouseEvent mouseEvent) {
        try {
            if (clientService.getUserByEmail(email_tf.getText()) == null && staffService.getUserByEmail(email_tf.getText()) == null && adminService.getUserByEmail(email_tf.getText()) == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning");
                alert.setContentText("Client with this email does not exist! Please try again.");
                alert.showAndWait();
                return;
            }


            if (clientService.getUserByEmail(email_tf.getText()) != null){
                if (clientService.getUserByEmail(email_tf.getText()).getFaceid() == null || clientService.getUserByEmail(email_tf.getText()).getFaceid().isEmpty() || clientService.getUserByEmail(email_tf.getText()).getFaceid_ts() == null || clientService.getUserByEmail(email_tf.getText()).getFaceid_ts().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("Client with this email does not have a faceid! Please try again.");
                    alert.showAndWait();
                    return;
                }
                Date parsed = Date.valueOf(clientService.getUserByEmail(email_tf.getText()).getFaceid_ts());
                if (LocalDate.now().minusDays(30).isAfter(parsed.toLocalDate())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("Faceid has expired! Please login with email and password!");
                    alert.showAndWait();
                    Client c = clientService.getUserByEmail(email_tf.getText());
                    c.setFaceid("");
                    clientService.update(c);
                    return;
                }
                GlobalVar.setUser(clientService.getUserByEmail(email_tf.getText()));

            }
            if (staffService.getUserByEmail(email_tf.getText()) != null){
                if (staffService.getUserByEmail(email_tf.getText()).getFaceid() == null || staffService.getUserByEmail(email_tf.getText()).getFaceid().isEmpty() || staffService.getUserByEmail(email_tf.getText()).getFaceid_ts() == null || staffService.getUserByEmail(email_tf.getText()).getFaceid_ts().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("Staff with this email does not have a faceid! Please try again.");
                    alert.showAndWait();
                    return;
                }
                Date parsed = Date.valueOf(staffService.getUserByEmail(email_tf.getText()).getFaceid_ts());
                if (LocalDate.now().minusDays(30).isAfter(parsed.toLocalDate())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("Faceid has expired! Please login with email and password!");
                    alert.showAndWait();
                    Staff c = staffService.getUserByEmail(email_tf.getText());
                    c.setFaceid("");
                    staffService.update(c);
                    return;
                }
                GlobalVar.setUser(staffService.getUserByEmail(email_tf.getText()));
            }
            if (adminService.getUserByEmail(email_tf.getText()) != null){
                if(adminService.getUserByEmail(email_tf.getText()).getFaceid() == null || adminService.getUserByEmail(email_tf.getText()).getFaceid().isEmpty() || adminService.getUserByEmail(email_tf.getText()).getFaceid_ts() == null || adminService.getUserByEmail(email_tf.getText()).getFaceid_ts().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("Admin with this email does not have a faceid! Please try again.");
                    alert.showAndWait();
                    return;
                }
                Date parsed = Date.valueOf(adminService.getUserByEmail(email_tf.getText()).getFaceid_ts());
                if (LocalDate.now().minusDays(30).isAfter(parsed.toLocalDate())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("Faceid has expired! Please login with email and password!");
                    alert.showAndWait();
                    Admin c = adminService.getUserByEmail(email_tf.getText());
                    c.setFaceid("");
                    adminService.update(c);
                    return;
                }
                GlobalVar.setUser(adminService.getUserByEmail(email_tf.getText()));
            }
            disableAllSI();
            setFaceID();

        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while trying to connect to the database! Please try again later.");
            alert.showAndWait();
            e.printStackTrace();
        }

    }


    private void compare(String token1, String token2){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("api_key","oVAqEDbCYmaILayXJdKAsuYbFcJ0LBP6")
                .addFormDataPart("api_secret","e76obC1xsr-zSMynWZoQCt62vWDgtZ6O")
                .addFormDataPart("face_token1",faceId)
                .addFormDataPart("face_token2",GlobalVar.getUser().getFaceid())
                .build();
        Request request = new Request.Builder()
                .url("https://api-us.faceplusplus.com/facepp/v3/compare")
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());

            if (jsonObject.getFloat("confidence") > 80){
                enableAllSI(true);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning");
                alert.setContentText("Faceid does not match! Please try again.");
                alert.showAndWait();
                GlobalVar.setUser(null);
                enableAllSI(false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFaceID(){
        Dialog<String> alert = new Dialog<>();
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("FaceID Verification");
        alert.setHeaderText("FaceID Verification");
        ButtonType okButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().add(okButton);
        Label label = new Label("Waiting for camera to initialize...");
        label.setLayoutX(0);
        label.setLayoutY(39);
        label.setPrefWidth(461);
        label.alignmentProperty().setValue(javafx.geometry.Pos.CENTER);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progressBar.setLayoutX(27);
        progressBar.setLayoutY(75);
        progressBar.setPrefWidth(407);
        progressBar.setPrefHeight(20);
        alert.getDialogPane().setContent(new Pane(label, progressBar));
        alert.show();


        Task<Void> cameraTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int i = 0;
                VideoCapture capture = new VideoCapture(0);
                Mat frame = new Mat();
                if (capture.isOpened()) {
                    while (!isCancelled()) {
                        if (capture.read(frame)) {
                            if (!frame.empty()) {
                                if (i == 5) {
                                    break;
                                }
                                i++;
                                System.out.println("Camera Initialized!");
                                Platform.runLater(()-> label.setText("Camera Initialized!"));
                                Thread.sleep(1000);
                                System.out.println("Capturing Frame...");
                                Platform.runLater(()-> label.setText("Capturing Frame..."));
                                Thread.sleep(1000);
                                MatOfRect facesDetected = new MatOfRect();
                                CascadeClassifier cascadeClassifier = new CascadeClassifier();
                                int minFaceSize = Math.round(frame.rows() * 0.1f);
                                cascadeClassifier.load(getClass().getResource("/assets/haarcascade_frontalface_alt.xml").toURI().getPath().toString().substring(1));
                                cascadeClassifier.detectMultiScale(frame,
                                        facesDetected,
                                        1.1,
                                        3,
                                        Objdetect.CASCADE_SCALE_IMAGE,
                                        new Size(minFaceSize, minFaceSize),
                                        new Size()
                                );
                                Rect[] facesArray = facesDetected.toArray();
                                if (facesArray.length > 0) {
                                    System.out.println("Face Found!");
                                    Platform.runLater(() -> label.setText("Face Found! Verifying..."));
                                    Platform.runLater(() -> progressBar.setProgress(1));

                                    MatOfByte matOfByte = new MatOfByte();
                                    Imgcodecs.imencode(".jpg", frame, matOfByte);
                                    byte[] byteArray = matOfByte.toArray();
                                    InputStream in = new ByteArrayInputStream(byteArray);
                                    BufferedImage img = null;
                                    try {
                                        img = ImageIO.read(in);
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    try {
                                        ImageIO.write(img, "png", bos);
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    byte[] imgBytes = bos.toByteArray();

                                    OkHttpClient client = new OkHttpClient().newBuilder()
                                            .build();
                                    MediaType mediaType = MediaType.parse("image/png");
                                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                            .addFormDataPart("image_file","image.png",
                                                    RequestBody.create(imgBytes, mediaType))
                                            .addFormDataPart("api_key","oVAqEDbCYmaILayXJdKAsuYbFcJ0LBP6")
                                            .addFormDataPart("api_secret","e76obC1xsr-zSMynWZoQCt62vWDgtZ6O")
                                            .addFormDataPart("return_attributes","emotion")
                                            .build();
                                    Request request = new Request.Builder()
                                            .url("https://api-us.faceplusplus.com/facepp/v3/detect")
                                            .method("POST", body)
                                            .build();
                                    try{
                                        Response response = client.newCall(request).execute();
                                        Platform.runLater(() -> {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body().string());
                                                faceId = jsonObject.getJSONArray("faces").getJSONObject(0).getString("face_token");
                                                System.out.println(GlobalVar.getUser());
                                                if (GlobalVar.getUser() == null){
                                                    enableAllSU(true);
                                                }
                                                else {
                                                    compare(faceId, GlobalVar.getUser().getFaceid());
                                                }
                                            }catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        });
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                                else {
                                    System.out.println("Face not found ");
                                    Platform.runLater(() -> label.setText("Face not found! Trying Again..."));
                                    Thread.sleep(2000);
                                }
                            }
                        }
                    }
                }
                capture.release();

                if(faceId == null || faceId.equals("")){
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Warning");
                        if (!isCancelled())
                            alert.setContentText("No face detected! Please try again.");
                        else
                            alert.setContentText("Faceid cancelled!");
                        alert.show();
                        GlobalVar.setUser(null);
                        enableAllSI(false);
                        enableAllSU(false);
                    });
                }
                Platform.runLater(alert::close);
                return null;
            }

        };

        Thread cameraThread = new Thread(cameraTask);
        alert.setOnCloseRequest(e -> {
            cameraTask.cancel(false);
        });
        cameraThread.start();

    }

    private void disableAllSI(){
        email_tf.setDisable(true);
        pwd_pf.setDisable(true);
        signin_btn.setDisable(true);
        signup_switch_btn.setDisable(true);
        faceid_btn.setDisable(true);
        google_btn.setDisable(true);
    }
    private void disableAllSU(){
        cin_tf.setDisable(true);
        firstname_tf.setDisable(true);
        lastname_tf.setDisable(true);
        date_dp.setDisable(true);
        address_tf.setDisable(true);
        phone_tf.setDisable(true);
        photo_tf.setDisable(true);
        username_tf.setDisable(true);
        pwdsu_pf.setDisable(true);
        emailsu_tf.setDisable(true);
        confpwd_pf.setDisable(true);
        next_btn.setDisable(true);
        back_btn.setDisable(true);
        signup_btn.setDisable(true);
        setupfr_checkbox.setDisable(true);
        browse_btn.setDisable(true);
    }
    private void enableAllSU(boolean b){
        if (b) {
            try {
                File file = new File(photo_tf.getText());
                File dest = new File("src/assets/profileuploads/USERIMG" + cin_tf.getText() + file.getName().substring(file.getName().lastIndexOf(".")));
                Files.copy(file.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                clientService.add(new Client(Integer.parseInt(cin_tf.getText()), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), date_dp.getValue().toString(), pwdsu_pf.getText(), emailsu_tf.getText(), phone_tf.getText(), address_tf.getText(), dest.getName(), faceId, new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Success");
                alert.setHeaderText("Success");
                alert.setContentText("Your account has been created successfully! Please sign in to continue.");
                alert.showAndWait();

                fadeOutRightAnimation.setNode(signupad_pane);
                fadeOutRightAnimation.setOnFinished(e -> {
                    signupad_pane.setVisible(false);
                    signup_switch_pane.setOpacity(0);
                    signup_switch_pane.setVisible(true);
                    fadeInLeftAnimation.setNode(signup_switch_pane);
                    fadeInLeftAnimation.play();
                    signin_pane.setVisible(true);
                    signin_pane.setOpacity(0);
                    fadeInLeftAnimation.setNode(signin_pane);
                    fadeInLeftAnimation.play();
                });
                fadeOutRightAnimation.play();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("An error occurred while trying to connect to the database! Please try again later.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
        cin_tf.setDisable(false);
        firstname_tf.setDisable(false);
        lastname_tf.setDisable(false);
        date_dp.setDisable(false);
        address_tf.setDisable(false);
        phone_tf.setDisable(false);
        photo_tf.setDisable(false);
        username_tf.setDisable(false);
        pwdsu_pf.setDisable(false);
        emailsu_tf.setDisable(false);
        confpwd_pf.setDisable(false);
        next_btn.setDisable(false);
        back_btn.setDisable(false);
        signup_btn.setDisable(false);
        setupfr_checkbox.setDisable(false);
        browse_btn.setDisable(false);
    }
    private void enableAllSI(boolean b){
        if (b) {
            GlobalVar.getUser().setFaceid_ts(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            GlobalVar.getUser().setFaceid(faceId);
            faceId = "";
            try {
                if (GlobalVar.getUser().getRole().equals("client")) {
                    clientService.update((Client) GlobalVar.getUser());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/userDashboard.fxml"));
                    Parent root = loader.load();
                    signin_btn.getScene().getWindow().setWidth(1200);
                    signin_btn.getScene().getWindow().setHeight(720);
                    signin_btn.getScene().setRoot(root);
                } else if (GlobalVar.getUser().getRole().equals("staff")) {
                    staffService.update((Staff) GlobalVar.getUser());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/staffDashboard.fxml"));
                    Parent root = loader.load();
                    signin_btn.getScene().getWindow().setWidth(1200);
                    signin_btn.getScene().getWindow().setHeight(720);
                    signin_btn.getScene().setRoot(root);
                } else if (GlobalVar.getUser().getRole().equals("admin")) {
                    adminService.update((Admin) GlobalVar.getUser());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/adminDashboard.fxml"));
                    Parent root = loader.load();
                    signin_btn.getScene().getWindow().setWidth(1200);
                    signin_btn.getScene().getWindow().setHeight(720);
                    signin_btn.getScene().setRoot(root);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("An error occurred while trying to connect to the database! Please try again later.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
        email_tf.setDisable(false);
        pwd_pf.setDisable(false);
        signin_btn.setDisable(false);
        signup_switch_btn.setDisable(false);
        faceid_btn.setDisable(false);
        google_btn.setDisable(false);
    }

    public void initialize() {
        fadeInLeftAnimation.setNode(signin_pane);
        fadeInLeftAnimation.play();
        fadeInRightAnimation.setNode(signup_switch_pane);
        fadeInRightAnimation.play();
        initDecoratedStage();
    }

    private double xOffset = 0;
    private double yOffset = 0;
    private void initDecoratedStage(){
        dragpane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        dragpane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
    }



}
