package controllers.gestionuser;

import animatefx.animation.*;
import com.password4j.Password;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionuser.Admin;
import entities.gestionuser.Client;
import entities.gestionuser.Staff;
import entities.gestionuser.User;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.dreamlu.mica.captcha.core.Captcha;
import net.dreamlu.mica.captcha.enums.CaptchaType;
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
import java.io.*;
import java.nio.file.Files;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;


public class AuthController {

    String faceId = "";
    private final StaffService staffService = new StaffService();
    private final ClientService clientService = new ClientService();
    private final AdminService adminService = new AdminService();
    private final FadeInRight fadeInRightAnimation = new FadeInRight();
    private final FadeInLeft fadeInLeftAnimation = new FadeInLeft();
    private final FadeOutLeft fadeOutLeftAnimation = new FadeOutLeft();
    private final FadeOutRight fadeOutRightAnimation = new FadeOutRight();
    private final FadeInUp fadeInUpAnimation = new FadeInUp();
    private final FadeOutDown fadeOutDownAnimation = new FadeOutDown();

    @FXML
    private Button forgotpw_btn;

    @FXML
    private Label nameforgot_label;

    @FXML
    private Label usernameforgot_label;

    @FXML
    private TextField forgotnumber_tf;

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
    private Label phonelabelforgot_label;

    @FXML
    private Pane forgotpaneverif;

    @FXML
    private ImageView userforgot_imageview;

    @FXML
    private Label nameres_label;

    @FXML
    private Label usernameres_label;

    @FXML
    private Label phoneres_label;

    @FXML
    private Pane resetpane;

    @FXML
    private PasswordField passwordres_pf;

    @FXML
    private PasswordField passwordconfres_pf;

    @FXML
    private ImageView userreset_imageview;

    @FXML
    private void changepw_btn_act(ActionEvent event) {
        if (passwordres_pf.getText().isEmpty() || passwordconfres_pf.getText().isEmpty()){
            errorAlert("Fields cannot be empty!", "Fields cannot be empty!", "Please fill in all the fields!");
            return;
        }
        if (!validateText(passwordres_pf.getText()))
            return;
        if (!passwordres_pf.getText().equals(passwordconfres_pf.getText())){
            errorAlert("Passwords do not match!", "Passwords do not match!", "Passwords do not match! Please try again.");
            return;
        }
        try {
            if (clientService.getUserByEmail(email_tf.getText()) != null) {
                clientService.updatePassword(clientService.getUserByEmail(email_tf.getText()).getId(), passwordres_pf.getText());
            }
            if (staffService.getUserByEmail(email_tf.getText()) != null) {
                staffService.updatePassword(staffService.getUserByEmail(email_tf.getText()).getId(), passwordres_pf.getText());
            }
            if (adminService.getUserByEmail(email_tf.getText()) != null) {
                adminService.updatePassword(adminService.getUserByEmail(email_tf.getText()).getId(), passwordres_pf.getText());
            }
            successAlert("Password changed successfully!", "Password changed successfully!", "Your password has been changed successfully! Please sign in to continue.");
            fadeOutDownAnimation.setNode(resetpane);
            fadeOutDownAnimation.setOnFinished(e -> {
                resetpane.setVisible(false);
                signin_pane.setVisible(true);
                signup_switch_pane.setVisible(true);
                signin_pane.setOpacity(0);
                signup_switch_pane.setOpacity(0);
                fadeInLeftAnimation.setNode(signin_pane);
                fadeInRightAnimation.setNode(signup_switch_pane);
                fadeInRightAnimation.play();
                fadeInLeftAnimation.play();
            });
            fadeOutDownAnimation.play();
        }catch (Exception e) {
            stackTraceAlert(e);
        }
    }

    @FXML
    private void cancelres_btn_act(ActionEvent event) {
        fadeOutDownAnimation.setNode(resetpane);
        fadeOutDownAnimation.setOnFinished(e -> {
            resetpane.setVisible(false);
            signin_pane.setVisible(true);
            signup_switch_pane.setVisible(true);
            signin_pane.setOpacity(0);
            signup_switch_pane.setOpacity(0);
            fadeInLeftAnimation.setNode(signin_pane);
            fadeInRightAnimation.setNode(signup_switch_pane);
            fadeInRightAnimation.play();
            fadeInLeftAnimation.play();
        });
        fadeOutDownAnimation.play();
    }

    @FXML
    private void verify_btn_act(){
        if (forgotnumber_tf.getText().isEmpty()){
            errorAlert("Phone number cannot be empty!", "Phone number cannot be empty!", "Please fill in the phone number field!");
            return;
        }

        try {
            User user = null;
            if (clientService.getUserByEmail(email_tf.getText()) != null) {
                user = clientService.getUserByEmail(email_tf.getText());
            }
            if (staffService.getUserByEmail(email_tf.getText()) != null) {
                user = staffService.getUserByEmail(email_tf.getText());
            }
            if (adminService.getUserByEmail(email_tf.getText()) != null) {
                user = adminService.getUserByEmail(email_tf.getText());
            }
            assert user != null;
            if (!forgotnumber_tf.getText().equals(user.getNum_tel())){
                errorAlert("Phone number does not match!", "Phone number does not match!", "Phone number does not match! Please try again.");
                return;
            }

            sendSms(user.getNum_tel());

            TextInputDialog dialog = new TextInputDialog();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.setTitle("Verification");
            dialog.initOwner(email_tf.getScene().getWindow());
            dialog.setHeaderText("Verification");
            dialog.setContentText("Please enter the verification code sent to your phone:");
            dialog.showAndWait();
            if (dialog.getResult() == null || dialog.getResult().isEmpty()){
                errorAlert("Verification code cannot be empty!", "Verification code cannot be empty!", "Please fill in the verification code field!");
                return;
            }
            if (!checkSms(user.getNum_tel(), dialog.getResult())){
                errorAlert("Verification code is incorrect!", "Verification code is incorrect!", "Verification code is incorrect! Please try again.");
                return;
            }
            nameres_label.setText(user.getFirstname() + " " + user.getLastname());
            usernameres_label.setText(user.getUsername());
            phoneres_label.setText(user.getNum_tel());

            userreset_imageview.setImage(new Image(new File("src/assets/profileuploads/" + user.getPhoto()).toURI().toString()));
            Circle clip = new Circle(userreset_imageview.getFitWidth() / 2, userreset_imageview.getFitHeight() / 2, userreset_imageview.getFitWidth() / 2);
            userreset_imageview.setClip(clip);
            fadeOutDownAnimation.setNode(forgotpaneverif);
            fadeOutDownAnimation.setOnFinished(e -> {
                forgotpaneverif.setVisible(false);
                resetpane.setVisible(true);
                resetpane.setOpacity(0);
                fadeInUpAnimation.setNode(resetpane);
                fadeInUpAnimation.play();
            });
            fadeOutDownAnimation.play();


        }catch (Exception e) {
            stackTraceAlert(e);
        }

    }
    @FXML
    private void notyou_label_clicked(MouseEvent event) {
        fadeOutDownAnimation.setNode(forgotpaneverif);
        fadeOutDownAnimation.setOnFinished(e -> {
            forgotpaneverif.setVisible(false);
            signin_pane.setVisible(true);
            signup_switch_pane.setVisible(true);
            signin_pane.setOpacity(0);
            signup_switch_pane.setOpacity(0);
            fadeInLeftAnimation.setNode(signin_pane);
            fadeInRightAnimation.setNode(signup_switch_pane);
            fadeInRightAnimation.play();
            fadeInLeftAnimation.play();
        });
        fadeOutDownAnimation.play();
    }
    @FXML
    void pwdsu_pf_pressed(KeyEvent event) {
        if (pwdsu_pf.getText().isEmpty()) {
            pwstrength_progress.setProgress(0);
            pwstrength_label.setText("Password Strength: Poor");
            return;
        }
        if (pwdsu_pf.getText().length() > 20){
            pwstrength_label.setText("Password Strength: Invalid Length");
            pwstrength_progress.setProgress(0);
            return;
        }
        if (pwdsu_pf.getText().matches("^[a-zA-Z0-9]*$")){
            pwstrength_label.setText("Password Strength: Weak");
            pwstrength_progress.setProgress(0.3);
            pwstrength_progress.setStyle("-color-progress-bar-fill: red;");
        }
        if (pwdsu_pf.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.* ).{8,20}$")){
            pwstrength_label.setText("Password Strength: Medium");
            pwstrength_progress.setProgress(0.6);
            pwstrength_progress.setStyle("-color-progress-bar-fill: orange;");
        }

        if (pwdsu_pf.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,20}$")) {
            pwstrength_label.setText("Password Strength: Strong");
            pwstrength_progress.setProgress(1);
            pwstrength_progress.setStyle("-color-progress-bar-fill: green;");
        }

    }

    @FXML
    void forgotpw_clicked(MouseEvent event) {
        if (email_tf.getText().isEmpty()){
            errorAlert("Email cannot be empty!", "Email cannot be empty!", "Please fill in the email field!");
            return;
        }
        if (!validateEmail(email_tf.getText()))
            return;
        try {
            if (clientService.getUserByEmail(email_tf.getText()) == null && staffService.getUserByEmail(email_tf.getText()) == null && adminService.getUserByEmail(email_tf.getText()) == null) {
                errorAlert("User with this email does not exist!", "User with this email does not exist!", "User with this email does not exist! Please try again.");
                return;
            }
            User user = null;
            if (clientService.getUserByEmail(email_tf.getText()) != null){
                user = clientService.getUserByEmail(email_tf.getText());
            }
            if (staffService.getUserByEmail(email_tf.getText()) != null){
                user = staffService.getUserByEmail(email_tf.getText());
            }
            if (adminService.getUserByEmail(email_tf.getText()) != null){
                user = adminService.getUserByEmail(email_tf.getText());
            }
            assert user != null;
            nameforgot_label.setText(user.getFirstname() + " " + user.getLastname());
            usernameforgot_label.setText(user.getUsername());
            String phone = user.getNum_tel();
            String last4 = phone.substring(phone.length() - 4);
            String first = phone.substring(0, phone.length() - 4);
            String newphone = first.replaceAll("[0-9]", "*") + last4;
            phonelabelforgot_label.setText(newphone);

            userforgot_imageview.setImage(new Image(new File("src/assets/profileuploads/" + user.getPhoto()).toURI().toString()));
            Circle clip = new Circle(userforgot_imageview.getFitWidth() / 2, userforgot_imageview.getFitHeight() / 2, userforgot_imageview.getFitWidth() / 2);
            userforgot_imageview.setClip(clip);
            fadeOutLeftAnimation.setNode(signin_pane);
            fadeOutRightAnimation.setNode(signup_switch_pane);
            fadeOutRightAnimation.setOnFinished(e -> {


                signup_switch_pane.setVisible(false);
                signin_pane.setVisible(false);
                forgotpaneverif.setOpacity(0);
                forgotpaneverif.setVisible(true);
                fadeInUpAnimation.setNode(forgotpaneverif);
                fadeInUpAnimation.play();
            });
            fadeOutLeftAnimation.play();
            fadeOutRightAnimation.play();

        }catch (Exception e) {
            stackTraceAlert(e);
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
            errorAlert("Fields cannot be empty!", "Fields cannot be empty!", "Please fill in all the fields!");
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
        if (date_dp.getValue().isAfter(LocalDate.now())){
            errorAlert("Invalid date!", "Invalid date!", "Date of birth cannot be in the future! Please try again.");
            return;
        }
        File file = new File(photo_tf.getText());
        if (!file.exists()) {
            errorAlert("File does not exist!", "File does not exist!", "The file you are trying to upload does not exist! Please try again.");
            return;
        }



        try {
            if(!showCaptcha()) {
                errorAlert("Captcha is incorrect!", "Captcha is incorrect!", "Captcha is incorrect! Please try again.");
                return;
            }
            if (clientService.getUserById(Integer.parseInt(cin_tf.getText())) != null) {
                errorAlert("Client with this CIN already exists!", "Client with this CIN already exists!", "Client with this CIN already exists! Please try another one.");
                return;
            }
            if (clientService.getUserByPhone(phone_tf.getText()) != null) {
                errorAlert("Error", "Phone Number Already in Use", "The phone number you have entered is already in use");
                return;
            }

            sendSms(phone_tf.getText());

            TextInputDialog dialog = new TextInputDialog();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.setTitle("Phone Verification");
            dialog.initOwner(email_tf.getScene().getWindow());
            dialog.setHeaderText("Phone Verification");
            dialog.setContentText("Please enter the verification code sent to your phone:");
            dialog.showAndWait();
            if (dialog.getResult() == null || dialog.getResult().isEmpty()){
                errorAlert("Verification code cannot be empty!", "Verification code cannot be empty!", "Please fill in the verification code field!");
                return;
            }
            if (!checkSms(phone_tf.getText(), dialog.getResult())){
                errorAlert("Verification code is incorrect!", "Verification code is incorrect!", "Verification code is incorrect! Please try again.");
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
            successAlert("Client added successfully!", "Client added successfully!", "Client added successfully! Please sign in to continue.");

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
            stackTraceAlert(e);
        }
    }

    private boolean validateNumber(String number){
        if (!number.matches("^[0-9]{8}$")){
            errorAlert("Invalid number format!", "Invalid number format!", "Number must be 8 digits long! Please try again.");
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
                errorAlert("User does not exist!", "User does not exist!", "User with this email does not exist! Please try again.");
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
            errorAlert("Invalid credentials!", "Invalid credentials!", "Invalid credentials! Please try again.");

        } catch (Exception e) {
            stackTraceAlert(e);
        }
    }

    private boolean validateEmail(String email){
        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            errorAlert("Invalid email format!", "Invalid email format!", "Email must be in its correct format!");
            return false;
        }
        return true;
    }

    private boolean validateText(String username){
        if (username.length() < 4 || username.length() > 20){
            errorAlert("Invalid length!", "Invalid length!", "Texts must be between 4 and 20 characters long!");
            return false;
        }
        return true;
    }
    @FXML
    void next_btn_act(ActionEvent event) {
        if (username_tf.getText().isEmpty() || pwdsu_pf.getText().isEmpty() || emailsu_tf.getText().isEmpty() || confpwd_pf.getText().isEmpty()) {
            errorAlert("Fields cannot be empty!", "Fields cannot be empty!", "Please fill in all the fields!");
            return;
        }
        if (!pwdsu_pf.getText().equals(confpwd_pf.getText())) {
            errorAlert("Passwords do not match!", "Passwords do not match!", "Passwords do not match! Please try again.");
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
                errorAlert("Client with this username already exists!", "Client with this username already exists!", "Client with this username already exists! Please try another one.");
                return;
            } else if (clientService.getUserByEmail(email_tf.getText()) != null) {
                errorAlert("Client with this email already exists!", "Client with this email already exists!", "Client with this email already exists! Please try another one.");
                return;
            }
        }catch (Exception e) {
            stackTraceAlert(e);
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
                errorAlert("User does not exist!", "User does not exist!", "User with this email does not exist! Please try again.");
                return;
            }


            if (clientService.getUserByEmail(email_tf.getText()) != null){
                if (clientService.getUserByEmail(email_tf.getText()).getFaceid() == null || clientService.getUserByEmail(email_tf.getText()).getFaceid().isEmpty() || clientService.getUserByEmail(email_tf.getText()).getFaceid_ts() == null || clientService.getUserByEmail(email_tf.getText()).getFaceid_ts().isEmpty()) {
                    errorAlert("Client with this email does not have a faceid!", "Client with this email does not have a faceid!", "Client with this email does not have a faceid! Please try again.");
                    return;
                }
                Date parsed = Date.valueOf(clientService.getUserByEmail(email_tf.getText()).getFaceid_ts());
                if (LocalDate.now().minusDays(30).isAfter(parsed.toLocalDate())){
                    errorAlert("Faceid has expired!", "Faceid has expired!", "Faceid has expired! Please login with email and password!");
                    Client c = clientService.getUserByEmail(email_tf.getText());
                    c.setFaceid("");
                    clientService.update(c);
                    return;
                }
                GlobalVar.setUser(clientService.getUserByEmail(email_tf.getText()));

            }
            if (staffService.getUserByEmail(email_tf.getText()) != null){
                if (staffService.getUserByEmail(email_tf.getText()).getFaceid() == null || staffService.getUserByEmail(email_tf.getText()).getFaceid().isEmpty() || staffService.getUserByEmail(email_tf.getText()).getFaceid_ts() == null || staffService.getUserByEmail(email_tf.getText()).getFaceid_ts().isEmpty()) {
                    errorAlert("Staff with this email does not have a faceid!", "Staff with this email does not have a faceid!", "Staff with this email does not have a faceid! Please try again.");
                    return;
                }
                Date parsed = Date.valueOf(staffService.getUserByEmail(email_tf.getText()).getFaceid_ts());
                if (LocalDate.now().minusDays(30).isAfter(parsed.toLocalDate())){
                    errorAlert("Faceid has expired!", "Faceid has expired!", "Faceid has expired! Please login with email and password!");
                    Staff c = staffService.getUserByEmail(email_tf.getText());
                    c.setFaceid("");
                    staffService.update(c);
                    return;
                }
                GlobalVar.setUser(staffService.getUserByEmail(email_tf.getText()));
            }
            if (adminService.getUserByEmail(email_tf.getText()) != null){
                if(adminService.getUserByEmail(email_tf.getText()).getFaceid() == null || adminService.getUserByEmail(email_tf.getText()).getFaceid().isEmpty() || adminService.getUserByEmail(email_tf.getText()).getFaceid_ts() == null || adminService.getUserByEmail(email_tf.getText()).getFaceid_ts().isEmpty()) {
                    errorAlert("Admin with this email does not have a faceid!", "Admin with this email does not have a faceid!", "Admin with this email does not have a faceid! Please try again.");
                    return;
                }
                Date parsed = Date.valueOf(adminService.getUserByEmail(email_tf.getText()).getFaceid_ts());
                if (LocalDate.now().minusDays(30).isAfter(parsed.toLocalDate())){
                    errorAlert("Faceid has expired!", "Faceid has expired!", "Faceid has expired! Please login with email and password!");
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
            stackTraceAlert(e);
        }

    }


    private void compare(){
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
                errorAlert("Faceid does not match!", "Faceid does not match!", "Faceid does not match! Please try again.");
                GlobalVar.setUser(null);
                enableAllSI(false);
            }
        }
        catch (Exception e) {
            stackTraceAlert(e);
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
        alert.initOwner(email_tf.getScene().getWindow());
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
                                        stackTraceAlert(e);
                                    }
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    try {
                                        ImageIO.write(img, "png", bos);
                                    }catch (Exception e) {
                                        stackTraceAlert(e);
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
                                                    compare();
                                                }
                                            }catch (Exception e) {
                                                stackTraceAlert(e);
                                            }

                                        });
                                    }catch (Exception e) {
                                        stackTraceAlert(e);
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

                if(faceId == null || faceId.isEmpty()){
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Warning");
                        alert.initOwner(email_tf.getScene().getWindow());
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
                successAlert("Client added successfully!", "Client added successfully!", "Client added successfully! Please sign in to continue.");

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
                stackTraceAlert(e);
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
        GlobalVar.setUser(null);
        fadeInLeftAnimation.setNode(signin_pane);
        fadeInLeftAnimation.play();
        fadeInRightAnimation.setNode(signup_switch_pane);
        fadeInRightAnimation.play();
        initDecoratedStage();
    }

    private boolean showCaptcha(){
        var dialog = new TextInputDialog();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Captcha Verification");
        dialog.setHeaderText("Captcha Verification");
        dialog.setContentText("Please enter the captcha code:");
        Captcha captcha = new Captcha(CaptchaType.MATH);
        final File[] temp = {null};
        AtomicReference<String> code = new AtomicReference<>(captcha.generate(() -> {
            try {
                temp[0] = File.createTempFile("captcha", ".png");
                return new FileOutputStream(temp[0]);
            } catch (Exception e) {
                stackTraceAlert(e);
            }
            return null;
        }));
        ImageView imageView = new ImageView(new Image(temp[0].toURI().toString()));
        Button button = new Button("Refresh");
        button.setOnAction(e -> {
            code.set(captcha.generate(() -> {
                try {
                    temp[0].delete();
                    temp[0] = File.createTempFile("captcha", ".png");
                    return new FileOutputStream(temp[0]);
                } catch (Exception ex) {
                    stackTraceAlert(ex);
                }
                return null;
            }));
            imageView.setImage(new Image(temp[0].toURI().toString()));
        });
        VBox vBox = new VBox(imageView, button, dialog.getEditor());
        vBox.spacingProperty().setValue(10);
        dialog.getDialogPane().setContent(vBox);
        dialog.showAndWait();
        temp[0].delete();
        return captcha.validate(code.get(), dialog.getResult());
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

    private void errorAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(email_tf.getScene().getWindow());
        alert.showAndWait();
    }

    private void successAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(email_tf.getScene().getWindow());
        alert.showAndWait();
    }

    private void infoAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(email_tf.getScene().getWindow());
        alert.showAndWait();
    }

    private void stackTraceAlert(Exception exception){
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("An exception occurred");
        alert.setContentText("An exception occurred, please check the stacktrace below");

        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);

        var textArea = new TextArea(stringWriter.toString());
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        var content = new GridPane();
        content.setMaxWidth(Double.MAX_VALUE);
        content.add(new Label("Full stacktrace:"), 0, 0);
        content.add(textArea, 0, 1);

        alert.initOwner(email_tf.getScene().getWindow());
        alert.getDialogPane().setExpandableContent(content);
        alert.showAndWait();
    }

    private void sendSms(String phone) {
        try {
            //using twilio to send sms
            String account_sid = "ACa0a9c02e124f285821fe62b736260421";
            String auth_token = "e8cd361a90ce0dbcd5485d5719f935fb";
            String verify_sid = "VA10dd8bfd053741ce7361fd967c83a1e6";

            Twilio.init(account_sid, auth_token);
            Verification verification = Verification.creator(
                    verify_sid,
                    "whatsapp:+216"+phone,
                    "whatsapp")
                    .create();
            System.out.println(verification.getStatus());
        } catch (Exception e) {
            stackTraceAlert(e);
        }
    }

    private Boolean checkSms(String phone, String code){
        try {
            String account_sid = "ACa0a9c02e124f285821fe62b736260421";
            String auth_token = "e8cd361a90ce0dbcd5485d5719f935fb";
            String verify_sid = "VA10dd8bfd053741ce7361fd967c83a1e6";
            Twilio.init(account_sid, auth_token);
            VerificationCheck verificationCheck = VerificationCheck.creator(
                            verify_sid)
                    .setTo("+216" + phone)
                    .setCode(code)
                    .create();
            return verificationCheck.getStatus().equals("approved");
        }catch (Exception e) {
            stackTraceAlert(e);
        }
        return false;
    }
}
