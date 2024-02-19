package controllers.gestionuser;

import animatefx.animation.*;
import com.password4j.Password;
import entities.gestionuser.Admin;
import entities.gestionuser.Client;
import entities.gestionuser.Staff;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.gestionuser.AdminService;
import services.gestionuser.ClientService;
import services.gestionuser.StaffService;

import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;


public class AuthController {
    private StaffService staffService = new StaffService();
    private ClientService clientService = new ClientService();
    private AdminService adminService = new AdminService();
    private FadeInRight fadeInRightAnimation = new FadeInRight();
    private FadeInLeft fadeInLeftAnimation = new FadeInLeft();
    private FadeOutLeft fadeOutLeftAnimation = new FadeOutLeft();
    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();

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
            File dest = new File("src/assets/profileuploads/USERIMG" + cin_tf.getText() + file.getName().substring(file.getName().lastIndexOf(".")));
            Files.copy(file.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            clientService.add(new Client(Integer.parseInt(cin_tf.getText()), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), date_dp.getValue().toString(), pwdsu_pf.getText(), emailsu_tf.getText(), phone_tf.getText(), address_tf.getText(), dest.getName()));
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
                client.setEvent_points(clientService.getEventPoints(client.getId()));
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
        if (username.length() < 4 && username.length() > 20){
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
