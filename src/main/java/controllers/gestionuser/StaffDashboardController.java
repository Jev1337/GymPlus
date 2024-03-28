package controllers.gestionuser;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import atlantafx.base.controls.Message;
import atlantafx.base.controls.Notification;
import atlantafx.base.controls.RingProgressIndicator;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionuser.Abonnement;
import entities.gestionuser.Client;
import entities.gestionuser.Staff;
import entities.gestionuser.User;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import okhttp3.*;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.objdetect.QRCodeDetector;
import org.opencv.videoio.VideoCapture;
import services.gestionequipements.EquipementService;
import services.gestionequipements.MaintenancesService;
import services.gestionevents.Event_detailsService;
import services.gestionuser.AbonnementService;
import services.gestionuser.AdminService;
import services.gestionuser.ClientService;
import services.gestionuser.StaffService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class StaffDashboardController {

    private final StaffService staffService = new StaffService();

    private final ClientService clientService = new ClientService();
    private final AbonnementService abonnementService = new AbonnementService();

    private final FadeIn[] fadeInAnimation = new FadeIn[8];
    private final FadeOutRight fadeOutRightAnimation = new FadeOutRight();
    private final FadeInRight fadeInRightAnimation = new FadeInRight();

    private final Notification msg = new Notification();
    private Event_detailsService eventDetailsService = new Event_detailsService();

    @FXML
    private Button subscription_btn;

    @FXML
    private Button objective_btn;

    @FXML
    private ImageView user_imageview;

    @FXML
    private TextField username_tf;

    @FXML
    private ImageView userprofile_imageview;

    @FXML
    private ScrollPane StaffEquipmentManagementPane;

    @FXML
    private ScrollPane StaffEventPane;

    @FXML
    private ScrollPane StaffHomePane;

    @FXML
    private ScrollPane StaffInfoPane;

    @FXML
    private ScrollPane StaffSettingsPane;

    @FXML
    private ScrollPane StaffStorePane;

    @FXML
    private ScrollPane StaffSubscriptionPane;

    @FXML
    private TextArea address_ta;

    @FXML
    private FontAwesomeIconView bars_btn;

    @FXML
    private Pane bars_pane;

    @FXML
    private Button close_btn;

    @FXML
    private ImageView cover_imageview;

    @FXML
    private DatePicker dateofbirth_tf;

    @FXML
    private Pane dragpane;

    @FXML
    private TextField email_tf;

    @FXML
    private Label emailinfo;
    @FXML
    private Label globalusername;

    @FXML
    private Button equipment_btn;

    @FXML
    private Button event_btn;

    @FXML
    private TextField firstname_tf;

    @FXML
    private Button home_btn;

    @FXML
    private TextField lastname_tf;

    @FXML
    private Button logout_btn;

    @FXML
    private Button minimize_btn;

    @FXML
    private Label nameinfo;

    @FXML
    private TextField phone_tf;

    @FXML
    private TextField profilepic_pf;

    @FXML
    private Button settings_btn;

    @FXML
    private Button shop_btn;


    @FXML
    private ComboBox<String> stat_combobox;

    @FXML
    private LineChart<String, Integer> stat_linechart;

    @FXML
    private Pane EquipmentIdAdminStaff;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<User> userlistsub_tableview;

    @FXML
    private TableView<Abonnement> subscriptionslist_tableview;

    @FXML
    private TableColumn<?,?> subid_col;

    @FXML
    private TableColumn<?,?> cinsubcol;

    @FXML
    private TableColumn<?,?> enddatesubcol;

    @FXML
    private TableColumn<?,?> subtypecol;

    @FXML
    private TableColumn<?,?> cincoladdsub;

    @FXML
    private TableColumn<?,?> emailcoladdsub;

    @FXML
    private TableColumn<?,?> usernamecoladdsub;

    @FXML
    private TextField searchbarsub_tf;

    @FXML
    private TextField searchbarusersub_tf;

    @FXML
    private ComboBox<String> subtype_cb;

    @FXML
    private ComboBox<String> subtypeedit_cb;

    @FXML
    private ComboBox<String> subtypeadd_cb;

    @FXML
    private Pane subpane;

    @FXML
    private ScrollPane StaffObjectivePane;

    @FXML
    private ImageView faceid_change;

    @FXML
    private FontAwesomeIconView verified_icon;

    @FXML
    private FontAwesomeIconView nonverified_icon;

    @FXML
    private Label namesub_label;

    @FXML
    private Label subexpire;

    @FXML
    private Label subtype;

    @FXML
    private Label id_label;

    @FXML
    private Label dobsub_label;

    @FXML
    private ImageView scanneduser_imageview;

    @FXML
    private ImageView camerafeed_imageview;

    @FXML
    private ToggleButton opencam_btn;

    @FXML
    private Pane qr_pane;

    @FXML
    private Pane hidepane;

    @FXML
    private ScrollPane smalluserlist_sp;

    @FXML
    private ScrollPane subbedlist_sp;

    @FXML
    private VBox subbed_vbox;

    @FXML
    private VBox userlistsub_vbox;

    @FXML
    private CheckBox legacysubbedlist_cb;

    @FXML
    private CheckBox legacyuserlist_cb;

    @FXML
    private PieChart piechart_gp;

    @FXML
    private Label total_equip;

    @FXML
    private Label total_maint;

    @FXML
    private Label total_income;


    private Abonnement selectedAbonnement;
    private Client selectedClient;
    private VideoCapture capture;
    private Mat frame;

    @FXML
    private Pane PlanningPan;


    @FXML
    private Pane StorePane;

    @FXML
    private CheckBox dark_cb;

    @FXML
    private CheckBox tts_cb;
    @FXML
    private Label total_events;
    @FXML
    private Button checkver_btn;

    @FXML
    private TextArea feedback_ta;

    @FXML
    private Label smtp_lat;

    @FXML
    private Label db_lat;

    @FXML
    private Label mem_us;

    @FXML
    private Label openai_lat;

    @FXML
    private Label azure_lat;

    @FXML
    private Label facepp_lat;

    @FXML
    private void checkver_btn_act(ActionEvent event){
        try{
            checkver_btn.setDisable(true);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://grandelation.com/api/gymplus/checkver")
                    .method("GET", null)
                    .addHeader("User-Agent", "Moyasar Payment Gateway")
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getString("version").equals("1.0")){
                notify("You are using the latest version of GymPlus!");
            }else{
                notify("A new version of GymPlus is available! Please update to the latest version.");
            }
            checkver_btn.setDisable(false);

        }catch (Exception e){
            stackTraceAlert(e);
        }
    }

    @FXML
    private void sendfeedback_btn_act(ActionEvent event){
        try {
            if (!validateText(feedback_ta.getText())) {
                return;
            }
            String mail = "gymplus-noreply@grandelation.com";
            String password = "yzDvS_UoSL7b";
            List<String> emails = new AdminService().getAllAdminsEmails();
            String to = String.join(",", emails);
            String subject = "Maintenance Alert";
            //the body will be "index.html" inside src/assets/html
            File file = new File("src/assets/html/feedback.html");
            String body = "";
            body = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            body = body.replace("{E1}", String.valueOf(GlobalVar.getUser().getEmail()));
            body = body.replace("{E2}", feedback_ta.getText());
            String host = "mail.grandelation.com";

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.debug", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(mail);
            msg.setRecipients(jakarta.mail.Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setSentDate(new java.util.Date());
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            msg.setContent(multipart);

            Transport.send(msg, mail, password);
            notify("Feedback has been sent successfully!");
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void dark_cb_act(ActionEvent event){
        if (dark_cb.isSelected()){
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "theme", "dark");
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        }else{
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "theme", "light");
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
    }

    @FXML
    private void tts_cb_act(ActionEvent event){
        if (tts_cb.isSelected()){
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "tts", "true");
        }else{
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "tts", "false");
        }
    }

    @FXML
    private void opencam_btn_act(ActionEvent event){
        Task<Void> cameraTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                capture = new VideoCapture(0);
                frame = new Mat();
                if (capture.isOpened()) {
                    hidepane.setVisible(false);
                    Mat gray = new Mat();
                    Mat points = new Mat();
                    while (opencam_btn.isSelected() && !isCancelled() && getCurrentPane() == StaffSubscriptionPane){
                        if (capture.read(frame)) {
                            if (!frame.empty()) {
                                Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                                Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
                                QRCodeDetector qrCodeDetector = new QRCodeDetector();
                                String data = qrCodeDetector.detectAndDecode(gray, points);
                                if (!points.empty() && !data.isEmpty()) {
                                    //check if data is an int
                                    if (data.matches("^\\d+$")) {
                                        Client c = clientService.getUserById(Integer.parseInt(data));
                                        if (c != null)
                                            Platform.runLater(() -> {
                                                namesub_label.setText(c.getLastname() + " " + c.getFirstname());
                                                id_label.setText(String.valueOf(c.getId()));
                                                dobsub_label.setText(c.getDate_naiss());
                                                scanneduser_imageview.setImage(new Image(new File("webapp/src/gymplus/public/profileuploads/" + c.getPhoto()).toURI().toString()));
                                                Circle clip = new Circle(scanneduser_imageview.getFitWidth()/2, scanneduser_imageview.getFitHeight()/2, scanneduser_imageview.getFitWidth()/2);
                                                scanneduser_imageview.setClip(clip);
                                                scanneduser_imageview.setPreserveRatio(false);
                                                try {
                                                    if (abonnementService.isUserSubscribed(c.getId())) {
                                                        Abonnement abonnement = abonnementService.getCurrentSubscription(c.getId());
                                                        subexpire.setText(abonnement.getDuree_abon());
                                                        subtype.setText(abonnement.getType());
                                                        verified_icon.setVisible(true);
                                                        nonverified_icon.setVisible(false);
                                                    }else {
                                                        subexpire.setText("Not Subscribed");
                                                        subtype.setText("Not Subscribed");
                                                        nonverified_icon.setVisible(true);
                                                        verified_icon.setVisible(false);
                                                    }
                                                } catch (SQLException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            });

                                    }

                                    for (int i = 0; i < points.cols(); i++) {
                                        Point pt1 = new Point(points.get(0, i));
                                        Point pt2 = new Point(points.get(0, (i + 1) % 4));
                                        Imgproc.line(frame, pt1, pt2, new Scalar(255, 0, 0), 3);
                                    }
                                }
                                MatOfByte bytes = new MatOfByte();
                                Imgcodecs.imencode(".png", frame, bytes);
                                InputStream inputStream = new ByteArrayInputStream(bytes.toArray());
                                camerafeed_imageview.setImage(new Image(inputStream));
                            }
                        }
                    }
                }
                capture.release();
                Platform.runLater(() -> {
                    camerafeed_imageview.setImage(new Image(getClass().getResource("/assets/images/offlinemedia.png").toString()));
                    if (getCurrentPane() != StaffSubscriptionPane){
                        opencam_btn.setSelected(false);
                        opencam_btn.setText("Open Camera");
                        opencam_btn.setStyle("-color-button-bg: -color-success-4;");
                    }
                });
                return null;
            }
        };

        if (opencam_btn.isSelected()){
            opencam_btn.setText("Close Camera");
            opencam_btn.setStyle("-color-button-bg: -color-danger-4;");
            hidepane.setVisible(true);
            Thread cameraThread = new Thread(cameraTask);
            cameraThread.setDaemon(true);
            cameraThread.start();
        }else {
            opencam_btn.setText("Open Camera");
            opencam_btn.setStyle("-color-button-bg: -color-success-4;");

        }



    }
    @FXML
    void faceid_change_clicked(){
        setFaceID();
    }
    String faceId = "";
    public void setFaceID(){
        faceid_change.setDisable(true);
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
                                if (i == 5)
                                    break;
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
                                                updateFaceId(faceId);
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

                if(faceId == null || faceId.equals("")){
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.initOwner(StaffInfoPane.getScene().getWindow());
                        alert.setTitle("Warning");
                        alert.setHeaderText("Warning");
                        if (!isCancelled())
                            alert.setContentText("No face detected! Please try again.");
                        else
                            alert.setContentText("Faceid Cancelled!");
                        alert.show();
                    });
                }
                Platform.runLater(() -> faceid_change.setDisable(false));
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

    private void updateFaceId(String faceId) {
        try {
            Client client = new Client(GlobalVar.getUser().getId(), GlobalVar.getUser().getUsername(), GlobalVar.getUser().getFirstname(), GlobalVar.getUser().getLastname(), GlobalVar.getUser().getDate_naiss(), GlobalVar.getUser().getPassword(), GlobalVar.getUser().getEmail(), GlobalVar.getUser().getNum_tel(), GlobalVar.getUser().getAdresse(), GlobalVar.getUser().getPhoto(), faceId, new Date(System.currentTimeMillis()).toString());
            clientService.update(client);
            GlobalVar.setUser(client);
            initProfile();
            notify("FaceID has been updated successfully!");
        } catch (Exception e) {
            stackTraceAlert(e);
        }
    }


    @FXML
    void user_imageview_clicked(MouseEvent event) {
        switchToPane(StaffInfoPane);
    }
    @FXML
    private Pane affichage_events_adstaff;
    @FXML
    void browse_btn_act(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        fileChooser.setTitle("Choose a profile picture");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            profilepic_pf.setText(file.getAbsolutePath());
        }
    }
    @FXML
    void saveacc_btn_act(ActionEvent event) {

        if (firstname_tf.getText().isEmpty() || lastname_tf.getText().isEmpty() || username_tf.getText().isEmpty() || email_tf.getText().isEmpty() || phone_tf.getText().isEmpty() || address_ta.getText().isEmpty()) {
            errorAlert("All fields are required!", "All fields are required!", "Please fill in all the fields");
            return;
        }
        if (dateofbirth_tf.getValue() == null) {
            errorAlert("Date of birth is required!", "Date of birth is required!", "Please select a date of birth");
            return;
        }
        if (!validateEmail(email_tf.getText()))
            return;
        if (!validateText(username_tf.getText()))
            return;
        try {
            if (!phone_tf.getText().equals(GlobalVar.getUser().getNum_tel())){
                if (staffService.getUserByPhone(phone_tf.getText()) != null) {
                    errorAlert("Error", "Phone Number Already in Use", "The phone number you have entered is already in use");
                    return;
                }
                sendSms(phone_tf.getText());

                TextInputDialog dialog = new TextInputDialog();
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.setTitle("Phone Verification");
                dialog.initOwner(StaffHomePane.getScene().getWindow());
                dialog.setHeaderText("Phone Verification");
                dialog.setContentText("Please enter the verification code sent to your phone:");
                dialog.showAndWait();
                if (dialog.getResult() == null || dialog.getResult().isEmpty()){
                    errorAlert("Verification code cannot be empty!", "Verification code cannot be empty!", "Verification Failed due to empty code! Please try again.");
                    return;
                }
                if (!checkSms(phone_tf.getText(), dialog.getResult())){
                    errorAlert("Verification code is incorrect!", "Verification code is incorrect!", "Verification code is incorrect! Please try again.");
                    return;
                }
            }
            if (profilepic_pf.getText().isEmpty()) {
                Staff staff = new Staff(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), GlobalVar.getUser().getPhoto(), GlobalVar.getUser().getFaceid(), GlobalVar.getUser().getFaceid_ts());
                staffService.update(staff);
                GlobalVar.setUser(staff);
                initProfile();
            }else{
                File file = new File(profilepic_pf.getText());
                if (!file.exists()) {
                    errorAlert("File not found!", "File not found!", "The file you selected does not exist");
                    return;
                }
                userprofile_imageview.setImage(null);
                user_imageview.setImage(null);
                File oldFile = new File("webapp/src/gymplus/public/profileuploads/" +GlobalVar.getUser().getPhoto());
                oldFile.delete();
                Files.copy(file.toPath(), new File("webapp/src/gymplus/public/profileuploads/USERIMG"+ GlobalVar.getUser().getId() + file.getName().substring(file.getName().lastIndexOf("."))).toPath(), StandardCopyOption.REPLACE_EXISTING);
                Staff staff = new Staff(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), "USERIMG"+ GlobalVar.getUser().getId() + file.getName().substring(file.getName().lastIndexOf(".")), GlobalVar.getUser().getFaceid(), GlobalVar.getUser().getFaceid_ts());
                staffService.update(staff);
                GlobalVar.setUser(staff);
                initProfile();
            }
            notify("Account has been updated successfully!");
        }catch (Exception e){
            stackTraceAlert(e);
        }
    }

    @FXML
    void deleteacc_btn_act(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(StaffInfoPane.getScene().getWindow());
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action is irreversible");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                userprofile_imageview.setImage(null);
                user_imageview.setImage(null);
                File file = new File("webapp/src/gymplus/public/profileuploads/" +GlobalVar.getUser().getPhoto());
                file.delete();
                staffService.delete(GlobalVar.getUser().getId());
                logout_btn_act(null);
            }catch (Exception e){
                stackTraceAlert(e);
                return;
            }
        }
    }

    @FXML
    void bars_btn_clicked(MouseEvent event) {
        bars_btn.setDisable(true);
        System.out.println(bars_pane.getPrefWidth());
        if (bars_pane.getPrefWidth() == 200) {
            invisibleAll();
            nullOpacityAll();
        } else {
            visibleAll();
            for (FadeIn fadeIn : fadeInAnimation)
                fadeIn.play();

        }
        Duration cycleDuration = Duration.millis(250);
        Timeline timeline = new Timeline(
                new KeyFrame(cycleDuration,
                        new KeyValue(bars_pane.prefWidthProperty(), bars_pane.getPrefWidth() == 200 ? 51 : 200, Interpolator.EASE_BOTH)
                ));
        timeline.play();
        timeline.setOnFinished(e -> bars_btn.setDisable(false));
    }

    @FXML
    void close_btn_act(ActionEvent event) {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
        System.exit(0);
    }
    @FXML
    void minimize_btn_act(ActionEvent event) {
        Stage stage = (Stage) minimize_btn.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void equipment_btn_act(ActionEvent event) {
        switchToPane(StaffEquipmentManagementPane);
    }

    @FXML
    void equipment_btn_clicked(MouseEvent event) {
        switchToPane(StaffEquipmentManagementPane);
    }

    @FXML
    void event_btn_act(ActionEvent event) {
        switchToPane(StaffEventPane);
    }

    @FXML
    void event_btn_clicked(MouseEvent event) {
        switchToPane(StaffEventPane);
    }

    @FXML
    void home_btn_act(ActionEvent event) {
        switchToPane(StaffHomePane);
        initCharts();
    }

    @FXML
    void home_btn_clicked(MouseEvent event) {
        switchToPane(StaffHomePane);
        initCharts();
    }

    @FXML
    void logout_btn_act(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/authInterface.fxml"));
            Parent root = loader.load();
            logout_btn.getScene().getWindow().setWidth(600);
            logout_btn.getScene().getWindow().setHeight(400);
            logout_btn.getScene().setRoot(root);
        } catch (Exception e) {
            stackTraceAlert(e);
        }
    }

    @FXML
    void logout_btn_clicked(MouseEvent event) {
        logout_btn_act(null);
    }

    @FXML
    void settings_btn_act(ActionEvent event) {
        switchToPane(StaffSettingsPane);
    }

    @FXML
    void settings_btn_clicked(MouseEvent event) {
        switchToPane(StaffSettingsPane);
    }

    @FXML
    void shop_btn_act(ActionEvent event) {
        switchToPane(StaffStorePane);
    }

    @FXML
    void shop_btn_clicked(MouseEvent event) {
        switchToPane(StaffStorePane);
    }

    @FXML
    void stat_combobox_act(ActionEvent event) {
        String selectedItem = stat_combobox.getSelectionModel().getSelectedItem();

        if (selectedItem.equals("Events")) {
            total_events.setText(String.valueOf(eventDetailsService.total_events()));

            stat_linechart.getData().clear();

            try {
                stat_linechart.getData().setAll(eventDetailsService.getEventsByMonth());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {

            MaintenancesService maintenancesService = new MaintenancesService();
            try {
                stat_linechart.getData().setAll(maintenancesService.getMaintenancesByMonth());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void subscription_btn_act(ActionEvent event) {
        switchToPane(StaffSubscriptionPane);
        initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
    }

    @FXML
    void subscription_btn_clicked(MouseEvent event) {
        switchToPane(StaffSubscriptionPane);
        initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
    }
    @FXML
    public void objective_btn_act(ActionEvent actionEvent) {
        switchToPane(StaffObjectivePane);
    }
    @FXML
    public void objective_btn_clicked(MouseEvent mouseEvent) {
        switchToPane(StaffObjectivePane);
    }
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


    private void setFitToWidthAll(){
        StaffHomePane.setFitToWidth(true);
        StaffInfoPane.setFitToWidth(true);
        StaffEventPane.setFitToWidth(true);
        StaffSubscriptionPane.setFitToWidth(true);
        StaffEquipmentManagementPane.setFitToWidth(true);
        StaffStorePane.setFitToWidth(true);
        StaffSettingsPane.setFitToWidth(true);
        StaffObjectivePane.setFitToWidth(true);
    }
    private ScrollPane getCurrentPane(){
        if (StaffHomePane.isVisible())
            return StaffHomePane;
        if (StaffInfoPane.isVisible())
            return StaffInfoPane;
        if (StaffEventPane.isVisible())
            return StaffEventPane;
        if (StaffSubscriptionPane.isVisible())
            return StaffSubscriptionPane;
        if (StaffEquipmentManagementPane.isVisible())
            return StaffEquipmentManagementPane;
        if (StaffStorePane.isVisible())
            return StaffStorePane;
        if (StaffSettingsPane.isVisible())
            return StaffSettingsPane;
        if (StaffObjectivePane.isVisible())
            return StaffObjectivePane;
        return null;
    }

    private void switchToPane(Node node){
        if (getCurrentPane() == node)
            return;
        fadeOutRightAnimation.setNode(getCurrentPane());
        fadeOutRightAnimation.play();
        fadeOutRightAnimation.setOnFinished(e -> {
            getCurrentPane().setVisible(false);
            node.setOpacity(0);
            node.setVisible(true);
            fadeInRightAnimation.setNode(node);
            fadeInRightAnimation.play();
        });
    }

    private void initProfile(){

        String photoname = GlobalVar.getUser().getPhoto();
        user_imageview.setImage(new Image(new File("webapp/src/gymplus/public/profileuploads/" +photoname).toURI().toString()));
        Circle clip1 = new Circle(user_imageview.getFitWidth()/2, user_imageview.getFitHeight()/2, user_imageview.getFitWidth()/2);
        user_imageview.setClip(clip1);
        user_imageview.setPreserveRatio(false);
        Rectangle clip2 = new Rectangle(cover_imageview.getFitWidth(), cover_imageview.getFitHeight());
        clip2.setArcWidth(30);
        clip2.setArcHeight(30);
        cover_imageview.setClip(clip2);
        userprofile_imageview.setImage(new Image(new File("webapp/src/gymplus/public/profileuploads/" +photoname).toURI().toString()));
        Circle clip3 = new Circle(userprofile_imageview.getFitWidth()/2, userprofile_imageview.getFitHeight()/2, userprofile_imageview.getFitWidth()/2);
        userprofile_imageview.setPreserveRatio(false);
        userprofile_imageview.setClip(clip3);
        nameinfo.setText(GlobalVar.getUser().getFirstname()+" "+GlobalVar.getUser().getLastname());
        emailinfo.setText(GlobalVar.getUser().getEmail());
        globalusername.setText(GlobalVar.getUser().getUsername());
        dateofbirth_tf.setValue(LocalDate.parse(GlobalVar.getUser().getDate_naiss()));
        firstname_tf.setText(GlobalVar.getUser().getFirstname());
        lastname_tf.setText(GlobalVar.getUser().getLastname());
        username_tf.setText(GlobalVar.getUser().getUsername());
        email_tf.setText(GlobalVar.getUser().getEmail());
        phone_tf.setText(GlobalVar.getUser().getNum_tel());
        address_ta.setText(GlobalVar.getUser().getAdresse());
    }


    private void initAnimations(){
        fadeInAnimation[0] = new FadeIn(home_btn);
        fadeInAnimation[1] = new FadeIn(settings_btn);
        fadeInAnimation[2] = new FadeIn(shop_btn);
        fadeInAnimation[3] = new FadeIn(logout_btn);
        fadeInAnimation[4] = new FadeIn(subscription_btn);
        fadeInAnimation[5] = new FadeIn(event_btn);
        fadeInAnimation[6] = new FadeIn(equipment_btn);
        fadeInAnimation[7] = new FadeIn(objective_btn);
    }
    private void visibleAll(){
        home_btn.setVisible(true);
        settings_btn.setVisible(true);
        shop_btn.setVisible(true);
        logout_btn.setVisible(true);
        subscription_btn.setVisible(true);
        event_btn.setVisible(true);
        equipment_btn.setVisible(true);
        objective_btn.setVisible(true);
    }

    private void invisibleAll(){
        home_btn.setVisible(false);
        settings_btn.setVisible(false);
        shop_btn.setVisible(false);
        logout_btn.setVisible(false);
        subscription_btn.setVisible(false);
        event_btn.setVisible(false);
        equipment_btn.setVisible(false);
        objective_btn.setVisible(false);
    }
    private void nullOpacityAll(){
        home_btn.setOpacity(0);
        settings_btn.setOpacity(0);
        shop_btn.setOpacity(0);
        logout_btn.setOpacity(0);
        subscription_btn.setOpacity(0);
        event_btn.setOpacity(0);
        equipment_btn.setOpacity(0);
        objective_btn.setOpacity(0);
    }
    public void searchbarsub_tf_textchanged(KeyEvent keyEvent) {
        initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
    }

    public void subtype_cb_act(ActionEvent actionEvent) {
        initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
    }

    public void deletesub_btn_act(ActionEvent actionEvent) {
        if (selectedAbonnement == null)
            selectedAbonnement = subscriptionslist_tableview.getSelectionModel().getSelectedItem();
        if (selectedAbonnement == null){
            errorAlert("No subscription selected", "No subscription selected", "Please select a subscription to delete");
            return;
        }
        Abonnement abonnement = selectedAbonnement;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(StaffInfoPane.getScene().getWindow());
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this subscription?");
        alert.setContentText("This action is irreversible");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                abonnementService.delete(abonnement.getId());
                initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
                initNonSubbedUserList(searchbarusersub_tf.getText());
                notify("Successfully deleted the subscription!");
            }catch (Exception e){
                stackTraceAlert(e);
            }
        }
    }

    public void savesub_btn_act(ActionEvent actionEvent) {
        if (selectedAbonnement == null){
            selectedAbonnement = subscriptionslist_tableview.getSelectionModel().getSelectedItem();
        }
        if (selectedAbonnement == null){
            errorAlert("No subscription selected", "No subscription selected", "Please select a subscription to edit");
            return;
        }
        if (subtypeedit_cb.getValue() == null){
            errorAlert("No subscription type selected", "No subscription type selected", "Please select a subscription type");
            return;
        }
        Abonnement abonnement = selectedAbonnement;
        Date date = null;
        if (subtypeedit_cb.getValue().equals("GP 1"))
            date = Date.valueOf(LocalDate.now().plusMonths(3));
        else if (subtypeedit_cb.getValue().equals("GP 2"))
            date = Date.valueOf(LocalDate.now().plusMonths(6));
        else if (subtypeedit_cb.getValue().equals("GP 3"))
            date = Date.valueOf(LocalDate.now().plusMonths(12));
        assert date != null;
        abonnement.setDuree_abon(date.toString());
        abonnement.setType(subtypeedit_cb.getValue());
        try {
            abonnementService.update(abonnement);
            initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
            initNonSubbedUserList(searchbarusersub_tf.getText());
            notify("Successfully updated the subscription!");
        }catch (Exception e){
            stackTraceAlert(e);
        }
    }

    public void searchbarusersub_tf_textchanged(KeyEvent keyEvent) {
        initNonSubbedUserList(searchbarusersub_tf.getText());
    }

    public void addsub_btn_act(ActionEvent actionEvent) {
        if (selectedClient == null)
            selectedClient = (Client)userlistsub_tableview.getSelectionModel().getSelectedItem();

        if (selectedClient == null){
            errorAlert("No user selected", "No user selected", "Please select a user to subscribe");
            return;
        }
        if (subtypeadd_cb.getValue() == null){
            errorAlert("No subscription type selected", "No subscription type selected", "Please select a subscription type");
            return;
        }
        Abonnement abonnement = new Abonnement();
        abonnement.setUser_id(selectedClient.getId());
        Date date = null;
        if (subtypeadd_cb.getValue().equals("GP 1"))
            date = Date.valueOf(LocalDate.now().plusMonths(3));
        else if (subtypeadd_cb.getValue().equals("GP 2"))
            date = Date.valueOf(LocalDate.now().plusMonths(6));
        else if (subtypeadd_cb.getValue().equals("GP 3"))
            date = Date.valueOf(LocalDate.now().plusMonths(12));
        assert date != null;
        abonnement.setDuree_abon(date.toString());
        abonnement.setType(subtypeadd_cb.getValue());
        try {
            abonnementService.add(abonnement);
            initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
            initNonSubbedUserList(searchbarusersub_tf.getText());
            notify("Successfully subscribed user!");
        }catch (Exception e){
            stackTraceAlert(e);
        }
    }


    @FXML
    private void legacysubbedlist_cb_act(ActionEvent actionEvent) {
        initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
        if (legacysubbedlist_cb.isSelected()) {
            subbedlist_sp.setVisible(false);
            subscriptionslist_tableview.setVisible(true);
        }else {
            subbedlist_sp.setVisible(true);
            subscriptionslist_tableview.setVisible(false);
        }
    }

    @FXML
    private void legacyuserlist_cb_act(ActionEvent actionEvent) {
        initNonSubbedUserList(searchbarusersub_tf.getText());
        if (legacyuserlist_cb.isSelected()) {
            smalluserlist_sp.setVisible(false);
            userlistsub_tableview.setVisible(true);
        }else {
            smalluserlist_sp.setVisible(true);
            userlistsub_tableview.setVisible(false);
        }
    }

    private double xOffset = 0;
    private double yOffset = 0;
    public void initialize() {
        fadeInRightAnimation.setNode(StaffHomePane);
        fadeInRightAnimation.play();
        stat_combobox.getItems().addAll(FXCollections.observableArrayList("Abonnements", "Clients", "Staff"));
        subtype_cb.getItems().addAll(FXCollections.observableArrayList( "All","GP 1", "GP 2", "GP 3"));
        subtypeadd_cb.getItems().addAll(FXCollections.observableArrayList("GP 1", "GP 2", "GP 3"));
        subtypeedit_cb.getItems().addAll(FXCollections.observableArrayList("GP 1", "GP 2", "GP 3"));
        total_events.setText(String.valueOf(eventDetailsService.total_events()));
        initProfile();
        initCharts();
        setFitToWidthAll();
        initAnimations();
        initDecoratedStage();
        initSubList("All", "");
        initNonSubbedUserList("");
        notify("Successfully Logged In as " + GlobalVar.getUser().getUsername() + "!");
        String theme = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "theme");
        if (theme != null && theme.equals("dark")) {
            dark_cb.setSelected(true);
        }
        RingProgressIndicator progressIndicator = new RingProgressIndicator();
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressIndicator.setLayoutX(hidepane.getPrefWidth()/2 - progressIndicator.getPrefWidth()/2 - 12);
        progressIndicator.setLayoutY(hidepane.getPrefHeight()/2 - progressIndicator.getPrefHeight()/2 - 12);
        progressIndicator.setPrefWidth(24);
        progressIndicator.setPrefHeight(24);
        hidepane.getChildren().add(progressIndicator);
        var warning = new Message("Warning!", "Please note that as a Staff you have the ability to cause changes that could cost the company money. Please be careful with your actions!");
        warning.getStyleClass().addAll(
                Styles.WARNING
        );
        warning.setLayoutX(50);
        warning.setLayoutY(50);
        warning.setPrefWidth(1075);
        if (!subpane.getChildren().contains(warning)) {
            subpane.getChildren().add(warning);
        }
        try {
            Pane pane_planning= FXMLLoader.load(getClass().getResource("/gestionSuivi/Planning.fxml"));
            PlanningPan.getChildren().setAll(pane_planning);
            Pane pane= FXMLLoader.load(getClass().getResource("/gestionevents/eventstaffadmin.fxml"));
            affichage_events_adstaff.getChildren().setAll(pane);
            Pane pane_event= FXMLLoader.load(getClass().getResource("/gestionequipement/equipement.fxml"));
            EquipmentIdAdminStaff.getChildren().setAll(pane_event);
            Pane pane_Store= FXMLLoader.load(getClass().getResource("/resourcesGestionStore/InterfaceStore.fxml"));
            StorePane.getChildren().setAll(pane_Store);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        String tts = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\GymPlus", "tts");
        if (tts != null && tts.equals("true")) {
            tts_cb.setSelected(true);
            playWelcome();
        }
    }

    private void playWelcome(){
        Media media = new Media(new File(getClass().getResource("/assets/sounds/welcome.mp3").getFile()).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
    private void initNonSubbedUserList(String condition) {
        selectedClient = null;
        try {
            List<User> users = (List<User>) (List<?>) clientService.getNonSubscribedUserList();
            ObservableList<User> obs = FXCollections.observableArrayList(users);
            if (condition != null && !condition.isEmpty()) {
                for (int i = 0; i < obs.size(); i++) {
                    if (!obs.get(i).getUsername().contains(condition) && !obs.get(i).getEmail().contains(condition) && !String.valueOf(obs.get(i).getId()).contains(condition)){
                        obs.remove(i);
                        i--;
                    }
                }
            }
            userlistsub_tableview.setItems(obs);
            cincoladdsub.setCellValueFactory(new PropertyValueFactory<>("id"));
            emailcoladdsub.setCellValueFactory(new PropertyValueFactory<>("email"));
            usernamecoladdsub.setCellValueFactory(new PropertyValueFactory<>("username"));


            //clear the vbox
            userlistsub_vbox.getChildren().clear();

            for (User user : users) {
                // apply condition
                if (condition != null && !condition.isEmpty()) {
                    if (!user.getUsername().contains(condition) && !user.getEmail().contains(condition) && !String.valueOf(user.getId()).contains(condition)){
                        continue;
                    }
                }
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setPadding(new Insets(10, 10, 10, 10));
                hBox.setStyle("-fx-background-color: -color-bg-inset;");
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setCursor(Cursor.HAND);
                hBox.setOnMouseEntered(e -> {
                    if (selectedClient != null && selectedClient.getId() == user.getId())
                        hBox.setStyle("-fx-background-color: #2196f3");
                    else
                        hBox.setStyle("-fx-background-color: #e4e4e4;");
                });
                hBox.setOnMouseExited(e -> {
                    if (selectedClient != null && selectedClient.getId() == user.getId())
                        hBox.setStyle("-fx-background-color: #2196f3");
                    else
                        hBox.setStyle("-fx-background-color: -color-bg-inset;");
                });
                hBox.setOnMouseClicked(e -> {
                    selectedClient = (Client) user;
                    for (Node node : userlistsub_vbox.getChildren()) {
                        if (node instanceof HBox && node != hBox) {
                            node.setStyle("-fx-background-color: -color-bg-inset;");
                        }
                    }
                    hBox.setStyle("-fx-background-color: #2196f3");
                });
                ImageView imageView = new ImageView(new Image(new File("webapp/src/gymplus/public/profileuploads/" + user.getPhoto()).toURI().toString()));
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(false);
                hBox.getChildren().add(imageView);
                VBox vbox1 = new VBox();
                vbox1.setSpacing(10);
                vbox1.getChildren().add(new Label("CIN"));
                vbox1.getChildren().add(new Label(String.valueOf(user.getId())));
                VBox vbox2 = new VBox();
                vbox2.setSpacing(10);
                vbox2.getChildren().add(new Label("Username"));
                vbox2.getChildren().add(new Label(user.getUsername()));
                VBox vbox3 = new VBox();
                vbox3.setSpacing(10);
                vbox3.getChildren().add(new Label("Email"));
                vbox3.getChildren().add(new Label(user.getEmail()));
                hBox.getChildren().addAll(vbox1, vbox2, vbox3);
                userlistsub_vbox.getChildren().add(hBox);
            }
        } catch (Exception e) {
            stackTraceAlert(e);
        }
    }
    private void initSubList(String type, String search){
        selectedAbonnement = null;
        List<Abonnement> list = null;
        try {
            if (type == null || type.equals("All"))
                list = abonnementService.getAllCurrent();
            else
                list = abonnementService.getAbonnementByTypeCurrent(type);
            ObservableList<Abonnement> observableList = FXCollections.observableArrayList(list);
            if(search != null && !search.isEmpty()) {
                for (int i = 0; i < observableList.size(); i++) {
                    if (!String.valueOf(observableList.get(i).getUser_id()).contains(search) && !observableList.get(i).getType().contains(search) && !observableList.get(i).getDuree_abon().contains(search) && !String.valueOf(observableList.get(i).getId()).contains(search)) {
                        observableList.remove(i);
                        i--;
                    }
                }
            }
            subscriptionslist_tableview.setItems(observableList);
            subid_col.setCellValueFactory(new PropertyValueFactory<>("id"));
            cinsubcol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
            enddatesubcol.setCellValueFactory(new PropertyValueFactory<>("duree_abon"));
            subtypecol.setCellValueFactory(new PropertyValueFactory<>("type"));
            subbed_vbox.getChildren().clear();
            for (Abonnement abonnement : list) {
                Client client = clientService.getUserById(abonnement.getUser_id());
                if (!client.getUsername().contains(search) && !String.valueOf(abonnement.getUser_id()).contains(search) && !abonnement.getType().contains(search) && !abonnement.getDuree_abon().contains(search) && !String.valueOf(abonnement.getId()).contains(search))
                    continue;

                HBox hBox = new HBox();
                hBox.setCursor(Cursor.HAND);
                hBox.setSpacing(10);
                hBox.setPadding(new Insets(10));

                hBox.setStyle("-fx-background-color: -color-bg-inset;");
                ImageView imageView = new ImageView();
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setImage(new Image(new File("webapp/src/gymplus/public/profileuploads/" + client.getPhoto()).toURI().toString()));

                hBox.getChildren().add(imageView);

                VBox vbox1 = new VBox();
                vbox1.setSpacing(10);
                vbox1.getChildren().add(new Label("ID"));
                vbox1.getChildren().add(new Label(String.valueOf(abonnement.getId())));
                VBox vbox2 = new VBox();
                vbox2.setSpacing(10);
                vbox2.getChildren().add(new Label("Username"));
                vbox2.getChildren().add(new Label(client.getUsername()));
                VBox vbox3 = new VBox();
                vbox3.setSpacing(10);
                vbox3.getChildren().add(new Label("CIN"));
                vbox3.getChildren().add(new Label(String.valueOf(abonnement.getUser_id())));
                VBox vbox4 = new VBox();
                vbox4.setSpacing(10);
                vbox4.getChildren().add(new Label("End Date"));
                vbox4.getChildren().add(new Label(abonnement.getDuree_abon()));
                VBox vbox5 = new VBox();
                vbox5.setSpacing(10);
                vbox5.getChildren().add(new Label("Type"));
                vbox5.getChildren().add(new Label(abonnement.getType()));
                hBox.getChildren().addAll(vbox1, vbox2, vbox3, vbox4, vbox5);

                subbed_vbox.getChildren().add(hBox);

                hBox.setOnMouseEntered(e -> {
                    if (selectedAbonnement != null && selectedAbonnement.getId() == abonnement.getId())
                        hBox.setStyle("-fx-background-color: #2196f3");
                    else
                        hBox.setStyle("-fx-background-color: -color-bg-overlay;");
                });
                hBox.setOnMouseExited(e -> {
                    if (selectedAbonnement != null && selectedAbonnement.getId() == abonnement.getId())
                        hBox.setStyle("-fx-background-color: #2196f3");
                    else
                        hBox.setStyle("-fx-background-color: -color-bg-inset;");
                });
                hBox.setOnMouseClicked(e -> {
                    selectedAbonnement = abonnement;
                    for (Node node : subbed_vbox.getChildren()) {
                        if (node instanceof HBox && node != hBox) {
                            node.setStyle("-fx-background-color: -color-bg-inset;");
                        }
                    }
                    hBox.setStyle("-fx-background-color: #2196f3");
                });

            }
        }catch (Exception e){
            stackTraceAlert(e);
        }


    }

    private void notify(String message){
        msg.setMessage(message);
        msg.getStyleClass().addAll(
                Styles.SUCCESS, Styles.ELEVATED_1
        );
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);
        msg.setLayoutX(795);
        msg.setLayoutY(80);

        msg.setOnClose(e -> {
            var out = Animations.slideOutRight(msg, Duration.millis(250));
            out.setOnFinished(f -> mainPane.getChildren().remove(msg));
            out.playFromStart();
        });
        var in = Animations.slideInRight(msg, Duration.millis(250));
        if (!mainPane.getChildren().contains(msg)) {
            mainPane.getChildren().add(msg);
        }
        in.playFromStart();
    }
    private void initCharts(){
        try {
            MaintenancesService maintenancesService = new MaintenancesService();
            EquipementService equipementService = new EquipementService();
            total_income.setText(String.valueOf(abonnementService.getCurMonthIncome()));
            total_equip.setText(String.valueOf(equipementService.getEquipementCount()));
            total_maint.setText(String.valueOf(maintenancesService.getMaintenancesCount()));
            stat_linechart.getData().setAll(maintenancesService.getMaintenancesByMonth());
            stat_linechart.setLegendVisible(false);
            stat_combobox.setValue("Maintenances");
        }catch (Exception e) {
            stackTraceAlert(e);
        }


        int gp1 = 0, gp2 = 0, gp3 = 0;
        try {
            List<Abonnement> list = abonnementService.getAllCurrent();
            for (Abonnement abonnement : list) {
                if (abonnement.getType().equals("GP 1"))
                    gp1++;
                else if (abonnement.getType().equals("GP 2"))
                    gp2++;
                else if (abonnement.getType().equals("GP 3"))
                    gp3++;
            }
        }catch (Exception e){
            stackTraceAlert(e);
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("GP 1", gp1),
                new PieChart.Data("GP 2", gp2),
                new PieChart.Data("GP 3", gp3)
        );
        piechart_gp.setData(pieChartData);
        IcmpPingRequest pingRequest = IcmpPingUtil.createIcmpPingRequest();
        pingRequest.setHost("grandelation.com");
        pingRequest.setTimeout(1000);
        IcmpPingResponse pingResponse = IcmpPingUtil.executePingRequest(pingRequest);
        if (pingResponse.getSuccessFlag()) {
            smtp_lat.setText(pingResponse.getRtt() + "ms");
        }else {
            smtp_lat.setText("N/A");
        }
        pingRequest.setHost("localhost");
        pingResponse = IcmpPingUtil.executePingRequest(pingRequest);
        if (pingResponse.getSuccessFlag()) {
            db_lat.setText(pingResponse.getRtt() + "ms");
        }else {
            db_lat.setText("N/A");
        }
        Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        memory = memory / (1024 * 1024);
        mem_us.setText(memory + "MB");
        pingRequest.setHost("api.openai.com");
        pingResponse = IcmpPingUtil.executePingRequest(pingRequest);
        if (pingResponse.getSuccessFlag()) {
            openai_lat.setText(pingResponse.getRtt() + "ms");
        }else {
            openai_lat.setText("N/A");
        }
        pingRequest.setHost("api.cognitive.microsoft.com");
        pingResponse = IcmpPingUtil.executePingRequest(pingRequest);
        if (pingResponse.getSuccessFlag()) {
            azure_lat.setText(pingResponse.getRtt() + "ms");
        }else {
            azure_lat.setText("N/A");
        }
        pingRequest.setHost("dynamodb.ca-central-1.amazonaws.com");
        pingResponse = IcmpPingUtil.executePingRequest(pingRequest);
        if (pingResponse.getSuccessFlag()) {
            facepp_lat.setText(pingResponse.getRtt() + "ms");
        }else {
            facepp_lat.setText("N/A");
        }

    }

    private boolean validateEmail(String email){
        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            errorAlert("Invalid email format!", "Invalid email format!", "Please enter a valid email address");
            return false;
        }
        return true;
    }

    private boolean validateText(String username){
        if (username.length() < 4 && username.length() > 20){
            errorAlert("Invalid username length!", "Invalid username length!", "Username must be between 4 and 20 characters long");
            return false;
        }
        return true;
    }
    private void errorAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(StaffInfoPane.getScene().getWindow());
        alert.showAndWait();
    }

    private void successAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(StaffInfoPane.getScene().getWindow());
        alert.showAndWait();
    }

    private void infoAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(StaffInfoPane.getScene().getWindow());
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
        if (StaffInfoPane != null && StaffInfoPane.getScene() != null && StaffInfoPane.getScene().getWindow() != null) {
            alert.initOwner(StaffInfoPane.getScene().getWindow());
        }
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
                            "+216"+phone,
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
