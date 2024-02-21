package controllers.gestionuser;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import atlantafx.base.controls.Message;
import atlantafx.base.controls.Notification;
import atlantafx.base.controls.RingProgressIndicator;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionuser.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import okhttp3.*;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.objdetect.QRCodeDetector;
import org.opencv.videoio.VideoCapture;
import org.w3c.dom.Text;
import services.gestionuser.AbonnementService;
import services.gestionuser.AdminService;
import services.gestionuser.ClientService;
import services.gestionuser.StaffService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class AdminDashboardController {

    private final AdminService adminService = new AdminService();
    final Notification msg = new Notification();
    private final StaffService staffService = new StaffService();
    private final ClientService clientService = new ClientService();
    private final AbonnementService abonnementService = new AbonnementService();
    private FadeIn[] fadeInAnimation = new FadeIn[9];
    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();
    private FadeInRight fadeInRightAnimation = new FadeInRight();


    @FXML
    private ScrollPane AdminBlogPane;

    @FXML
    private ScrollPane AdminEquipmentManagementPane;

    @FXML
    private ScrollPane AdminEventPane;

    @FXML
    private ScrollPane AdminHomePane;

    @FXML
    private ScrollPane AdminInfoPane;

    @FXML
    private ScrollPane AdminSettingsPane;

    @FXML
    private ScrollPane AdminStorePane;

    @FXML
    private ScrollPane AdminSubscriptionPane;

    @FXML
    private ScrollPane AdminUserManagementPane;

    @FXML
    private TableColumn<?, ?> acctypecol;

    @FXML
    private ComboBox<String> acctypemanage_cb;

    @FXML
    private TextArea address_ta;

    @FXML
    private Label role_label;

    @FXML
    private TableColumn<?, ?> addresscol;

    @FXML
    private TextArea addressmanage_ta;

    @FXML
    private FontAwesomeIconView bars_btn;

    @FXML
    private Pane bars_pane;

    @FXML
    private Button blog_btn;

    @FXML
    private TableColumn<?, ?> cincol;

    @FXML
    private TextField cinmanage_tf;

    @FXML
    private Button close_btn;

    @FXML
    private ImageView cover_imageview;

    @FXML
    private Button createacc_btn;

    @FXML
    private DatePicker dateofbirth_tf;

    @FXML
    private Button deleteacc_btn;

    @FXML
    private Button deleteaccmanage_btn;

    @FXML
    private TableColumn<?, ?> dobcol;

    @FXML
    private TableColumn<?, ?> phonenumbercol;

    @FXML
    private DatePicker dobmanage_dp;

    @FXML
    private Pane dragpane;

    @FXML
    private TextField email_tf;

    @FXML
    private TableColumn<?, ?> emailcol;

    @FXML
    private Label emailinfo;

    @FXML
    private TextField emailmanage_tf;

    @FXML
    private Button equipment_btn;

    @FXML
    private Button event_btn;

    @FXML
    private TextField firstname_tf;

    @FXML
    private TableColumn<?, ?> firstnamecol;

    @FXML
    private TextField firstnamemanage_tf;

    @FXML
    private Label globalusername;

    @FXML
    private Button home_btn;

    @FXML
    private TextField lastname_tf;

    @FXML
    private TableColumn<?, ?> lastnamecol;

    @FXML
    private TextField lastnamemanage_tf;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button manageacc_btn;

    @FXML
    private Button minimize_btn;

    @FXML
    private Label nameinfo;

    @FXML
    private TextField phone_tf;

    @FXML
    private TextField phonemanage_tf;

    @FXML
    private Label profilelabel;

    @FXML
    private TextField profilepic_pf;

    @FXML
    private PasswordField pwdmanage_pf;

    @FXML
    private Button saveaccmanage_btn;

    @FXML
    private TextField searchbar_tf;

    @FXML
    private Button settings_btn;

    @FXML
    private CheckBox setupfrmanage_check;

    @FXML
    private Button shop_btn;

    @FXML
    private Button saveacc_btn;

    @FXML
    private Button subscription_btn;

    @FXML
    private ComboBox<String> type_cb;

    @FXML
    private ImageView user_imageview;

    @FXML
    private TableView<User> userlist_tableview;

    @FXML
    private TextField username_tf;

    @FXML
    private TableColumn<?, ?> usernamecol;

    @FXML
    private TextField usernamemanage_tf;

    @FXML
    private ImageView userprofile_imageview;

    @FXML
    private Button users_btn;

    @FXML
    private Button goback_btn;

    @FXML
    private TextField photomanage_tf;

    @FXML
    private BarChart<String, Number> stat_barchart;

    @FXML
    private ComboBox<String> stat_combobox;

    @FXML
    private LineChart<String, Number> stat_linechart;

    @FXML
    private Pane affichage_events_adstaff= new Pane();

    @FXML
    private Pane EquipmentIdAdminStaff;

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

    private VideoCapture capture;
    private Mat frame;


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
                    while (opencam_btn.isSelected() && !isCancelled() && getCurrentPane() == AdminSubscriptionPane){
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
                                                scanneduser_imageview.setImage(new Image(new File("src/assets/profileuploads/" + c.getPhoto()).toURI().toString()));
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
                    if (getCurrentPane() != AdminSubscriptionPane){
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
    void blog_btn_act(ActionEvent event) {
        switchToPane(AdminBlogPane);
    }

    @FXML
    void blog_btn_clicked(MouseEvent event) {
        switchToPane(AdminBlogPane);
    }


    @FXML
    void equipment_btn_act(ActionEvent event) {
        switchToPane(AdminEquipmentManagementPane);
    }

    @FXML
    void equipment_btn_clicked(MouseEvent event) {
        switchToPane(AdminEquipmentManagementPane);
    }

    @FXML
    void event_btn_act(ActionEvent event) {
        switchToPane(AdminEventPane);
    }

    @FXML
    void event_btn_clicked(MouseEvent event) {
        switchToPane(AdminEventPane);
    }

    @FXML
    void home_btn_act(ActionEvent event) {
        switchToPane(AdminHomePane);
    }

    @FXML
    void home_btn_clicked(MouseEvent event) {
        switchToPane(AdminHomePane);
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
                                                updateFaceId(faceId);
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
            User user = userlist_tableview.getSelectionModel().getSelectedItem();
            if(user == null) {
                GlobalVar.getUser().setFaceid(faceId);
                GlobalVar.getUser().setFaceid_ts(new Date(System.currentTimeMillis()).toString());
                initProfile();
            }
            else {
                user.setFaceid(faceId);
                user.setFaceid_ts(new Date(System.currentTimeMillis()).toString());
                switch (user.getRole()) {
                    case "client" -> {
                        Client client = (Client) user;
                        clientService.update(client);
                    }
                    case "staff" -> {
                        Staff staff = (Staff) user;
                        staffService.update(staff);
                    }
                    case "admin" -> {
                        Admin admin = (Admin) user;
                        adminService.update(admin);
                    }
                }
            }
            notify("FaceID has been updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }
    }

    @FXML
    void logout_btn_clicked(MouseEvent event) {
        logout_btn_act(null);
    }

    @FXML
    void settings_btn_act(ActionEvent event) {
        switchToPane(AdminSettingsPane);
    }

    @FXML
    void settings_btn_clicked(MouseEvent event) {
        switchToPane(AdminSettingsPane);
    }

    @FXML
    void shop_btn_act(ActionEvent event) {
        switchToPane(AdminStorePane);
    }

    @FXML
    void shop_btn_clicked(MouseEvent event) {
        switchToPane(AdminStorePane);
    }


    @FXML
    void subscription_btn_act(ActionEvent event) {
        switchToPane(AdminSubscriptionPane);
    }

    @FXML
    void subscription_btn_clicked(MouseEvent event) {
        switchToPane(AdminSubscriptionPane);
    }

    @FXML
    void user_imageview_clicked(MouseEvent event) {
        initProfile();
        switchToPane(AdminInfoPane);
    }

    @FXML
    void users_btn_click(ActionEvent event) {
        switchToPane(AdminUserManagementPane);
    }

    @FXML
    void users_btn_clicked(MouseEvent event) {
        switchToPane(AdminUserManagementPane);
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Empty fields");
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }
        if (dateofbirth_tf.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Date of birth is empty");
            alert.setContentText("Please choose a date of birth");
            alert.showAndWait();
            return;
        }
        if (!validateEmail(email_tf.getText()))
            return;
        if (!validateText(username_tf.getText()))
            return;
        try {
            if (profilepic_pf.getText().isEmpty()) {
                Admin admin= new Admin(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), GlobalVar.getUser().getPhoto(), GlobalVar.getUser().getFaceid(), GlobalVar.getUser().getFaceid_ts());
                adminService.update(admin);
                GlobalVar.setUser(admin);
                initProfile();
            }else{
                File file = new File(profilepic_pf.getText());
                if (!file.exists()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Error");
                    alert.setHeaderText("Profile picture not found");
                    alert.setContentText("Please choose a valid profile picture");
                    alert.showAndWait();
                    return;
                }
                userprofile_imageview.setImage(null);
                user_imageview.setImage(null);
                File oldFile = new File("src/assets/profileuploads/" +GlobalVar.getUser().getPhoto());
                oldFile.delete();
                Files.copy(file.toPath(), new File("src/assets/profileuploads/USERIMG"+ GlobalVar.getUser().getId() + file.getName().substring(file.getName().lastIndexOf("."))).toPath());
                Admin admin = new Admin(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), "USERIMG"+ GlobalVar.getUser().getId() + file.getName().substring(file.getName().lastIndexOf(".")), GlobalVar.getUser().getFaceid(), GlobalVar.getUser().getFaceid_ts());
                adminService.update(admin);
                GlobalVar.setUser(admin);
                initProfile();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Success");
            alert.setHeaderText("Profile picture updated");
            alert.setContentText("Your profile info has been updated successfully");
            alert.showAndWait();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText("An error has occured while trying to update your account");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    @FXML
    void deleteacc_btn_act(ActionEvent event) {
        deleteAcc(GlobalVar.getUser());
        logout_btn_act(null);
    }
    @FXML
    void stat_combobox_act(ActionEvent event) {

    }

    public void createacc_btn_act(ActionEvent actionEvent) {
        if (firstnamemanage_tf.getText().isEmpty() || lastnamemanage_tf.getText().isEmpty()
                || usernamemanage_tf.getText().isEmpty() || emailmanage_tf.getText().isEmpty()
                || phonemanage_tf.getText().isEmpty() || addressmanage_ta.getText().isEmpty()
                || photomanage_tf.getText().isEmpty() || pwdmanage_pf.getText().isEmpty()
                || dobmanage_dp.getValue() == null || acctypemanage_cb.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Empty fields");
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }
        if (!validateEmail(emailmanage_tf.getText()))
            return;
        if (!validateText(usernamemanage_tf.getText()))
            return;
        if (!validateText(firstnamemanage_tf.getText()))
            return;
        if (!validateText(lastnamemanage_tf.getText()))
            return;
        if (!validateNumber(phonemanage_tf.getText()))
            return;
        if (!validateNumber(cinmanage_tf.getText()))
            return;
        if (!validateText(addressmanage_ta.getText()))
            return;
        if(!validateText(pwdmanage_pf.getText()))
            return;
        try {
            if (adminService.getUserById(Integer.parseInt(cinmanage_tf.getText())) != null
                    || staffService.getUserById(Integer.parseInt(cinmanage_tf.getText())) != null
                    || clientService.getUserById(Integer.parseInt(cinmanage_tf.getText())) != null ||
                    adminService.getUserByEmail(emailmanage_tf.getText()) != null
                    || staffService.getUserByEmail(emailmanage_tf.getText()) != null
                    || clientService.getUserByEmail(emailmanage_tf.getText()) != null
                || clientService.getUserByUsername(usernamemanage_tf.getText()) != null
                    || staffService.getUserByUsername(usernamemanage_tf.getText()) != null
                    || adminService.getUserByUsername(usernamemanage_tf.getText()) != null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Error");
                alert.setHeaderText("Some fields are already used");
                alert.setContentText("Please choose another username, email or CIN");
                alert.showAndWait();
                return;
            }
            File file = new File(photomanage_tf.getText());
            if (!file.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Error");
                alert.setHeaderText("Profile picture not found");
                alert.setContentText("Please choose a valid profile picture");
                alert.showAndWait();
                return;
            }
            if (acctypemanage_cb.getValue().equals("Admin")) {
                adminService.add(new Admin(Integer.parseInt(cinmanage_tf.getText()), usernamemanage_tf.getText(), firstnamemanage_tf.getText(), lastnamemanage_tf.getText(), dobmanage_dp.getValue().toString(), pwdmanage_pf.getText(), emailmanage_tf.getText(), phonemanage_tf.getText(), addressmanage_ta.getText(), "USERIMG"+ cinmanage_tf.getText() + file.getName().substring(file.getName().lastIndexOf(".")),"",new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))));
            }else if (acctypemanage_cb.getValue().equals("Staff")) {
                staffService.add(new Staff(Integer.parseInt(cinmanage_tf.getText()), usernamemanage_tf.getText(), firstnamemanage_tf.getText(), lastnamemanage_tf.getText(), dobmanage_dp.getValue().toString(), pwdmanage_pf.getText(), emailmanage_tf.getText(), phonemanage_tf.getText(), addressmanage_ta.getText(), "USERIMG"+ cinmanage_tf.getText() +  file.getName().substring(file.getName().lastIndexOf(".")),"",new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))));
            }else if (acctypemanage_cb.getValue().equals("Client")) {
                clientService.add(new Client(Integer.parseInt(cinmanage_tf.getText()), usernamemanage_tf.getText(), firstnamemanage_tf.getText(), lastnamemanage_tf.getText(), dobmanage_dp.getValue().toString(), pwdmanage_pf.getText(), emailmanage_tf.getText(), phonemanage_tf.getText(), addressmanage_ta.getText(), "USERIMG"+ cinmanage_tf.getText() +  file.getName().substring(file.getName().lastIndexOf(".")),"",new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))));
            }
            Files.copy(file.toPath(), new File("src/assets/profileuploads/USERIMG"+ cinmanage_tf.getText() + file.getName().substring(file.getName().lastIndexOf("."))).toPath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Success");
            alert.setHeaderText("Account created");
            alert.setContentText("The account has been created successfully");
            alert.showAndWait();
            initUserList(-1, "");
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText("An error has occured while trying to create the account");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }

    }

    private boolean deleteAcc(User user){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this account?");
        alert.setContentText("This action is irreversible");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                if(user.getId() != GlobalVar.getUser().getId()) {
                    userprofile_imageview.setImage(null);
                    user_imageview.setImage(null);
                }
                File file = new File("src/assets/profileuploads/" +user.getPhoto());
                file.delete();
                adminService.delete(user.getId());
            }catch (Exception e){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Error");
                alert.setHeaderText("An error has occured");
                alert.setContentText("An error has occured while trying to delete your account");
                alert.showAndWait();
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void deleteaccmanage_btn(ActionEvent actionEvent) {
        deleteAcc(userlist_tableview.getSelectionModel().getSelectedItem());
        switchToPane(AdminUserManagementPane);
    }

    public void saveaccmanage_btn_act(ActionEvent actionEvent) {
        if (firstname_tf.getText().isEmpty() || lastname_tf.getText().isEmpty() || username_tf.getText().isEmpty() || email_tf.getText().isEmpty() || phone_tf.getText().isEmpty() || address_ta.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Empty fields");
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }
        if (dateofbirth_tf.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Date of birth is empty");
            alert.setContentText("Please choose a date of birth");
            alert.showAndWait();
            return;
        }
        if (!validateEmail(email_tf.getText()))
            return;
        if (!validateText(username_tf.getText()))
            return;
        try {
            User user = userlist_tableview.getSelectionModel().getSelectedItem();
            if (profilepic_pf.getText().isEmpty()) {
                if (user.getRole().equals("client")){
                    clientService.update(new Client(user.getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), user.getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), user.getPhoto(),user.getFaceid(), user.getFaceid_ts()));
                }else if (user.getRole().equals("staff")) {
                    staffService.update(new Staff(user.getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), user.getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), user.getPhoto(), user.getFaceid(), user.getFaceid_ts()));
                }else if (user.getRole().equals("admin")) {
                    adminService.update(new Admin(user.getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), user.getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), user.getPhoto(), user.getFaceid(), user.getFaceid_ts()));
                }
                user.setUsername(username_tf.getText());
                user.setFirstname(firstname_tf.getText());
                user.setLastname(lastname_tf.getText());
                user.setDate_naiss(dateofbirth_tf.getValue().toString());
                user.setEmail(email_tf.getText());
                user.setNum_tel(phone_tf.getText());
                user.setAdresse(address_ta.getText());
                initProfileTemp(user);

            }else{
                File file = new File(profilepic_pf.getText());
                if (!file.exists()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Error");
                    alert.setHeaderText("Profile picture not found");
                    alert.setContentText("Please choose a valid profile picture");
                    alert.showAndWait();
                    return;
                }
                userprofile_imageview.setImage(null);
                File oldFile = new File("src/assets/profileuploads/" +user.getPhoto());
                oldFile.delete();
                Files.copy(file.toPath(), new File("src/assets/profileuploads/USERIMG"+ user.getId() + file.getName().substring(file.getName().lastIndexOf("."))).toPath());
                if (acctypecol.getCellData(userlist_tableview.getSelectionModel().getSelectedIndex()) == null){
                    clientService.update(new Client(user.getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), user.getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), user.getPhoto(),user.getFaceid(), user.getFaceid_ts()));
                }else if (acctypecol.getCellData(userlist_tableview.getSelectionModel().getSelectedIndex()).equals("staff")) {
                    staffService.update(new Staff(user.getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), user.getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), user.getPhoto(), user.getFaceid(), user.getFaceid_ts()));
                }else if (acctypecol.getCellData(userlist_tableview.getSelectionModel().getSelectedIndex()).equals("admin")) {
                    adminService.update(new Admin(user.getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), user.getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), user.getPhoto(), user.getFaceid(), user.getFaceid_ts()));
                }
                user.setUsername(username_tf.getText());
                user.setFirstname(firstname_tf.getText());
                user.setLastname(lastname_tf.getText());
                user.setDate_naiss(dateofbirth_tf.getValue().toString());
                user.setEmail(email_tf.getText());
                user.setNum_tel(phone_tf.getText());
                user.setAdresse(address_ta.getText());
                user.setPhoto("USERIMG"+ user.getId() + file.getName().substring(file.getName().lastIndexOf(".")));
                initProfileTemp(user);

            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Success");
            alert.setHeaderText("Profile picture updated");
            alert.setContentText("Profile info has been updated successfully");
            alert.showAndWait();
            switchToPane(AdminUserManagementPane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText("An error has occured while trying to update your account");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    public void manageacc_btn_act(ActionEvent actionEvent) {
        if(userlist_tableview.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user to manage");
            alert.showAndWait();
            return;
        }
        User user = userlist_tableview.getSelectionModel().getSelectedItem();
        if (user.getId() == GlobalVar.getUser().getId()){
            //This is your own account, would you like to go to your own profile instead?
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Confirmation");
            alert.setHeaderText("This is your own account");
            alert.setContentText("Would you like to go to your own profile instead?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                initProfile();
                switchToPane(AdminInfoPane);
            }
            return;
        }
        initProfileTemp(user);
        switchToPane(AdminInfoPane);
    }

    @FXML
    void goback_btn_act(ActionEvent event) {
        switchToPane(AdminUserManagementPane);
    }


    public void type_cb_act(ActionEvent actionEvent) {
        if (type_cb.getValue().equals("All"))
            initUserList(-1 , searchbar_tf.getText());
        else if (type_cb.getValue().equals("Admin"))
            initUserList(0, searchbar_tf.getText());
        else if (type_cb.getValue().equals("Staff"))
            initUserList(1, searchbar_tf.getText());
        else if (type_cb.getValue().equals("Client"))
            initUserList(2, searchbar_tf.getText());
    }

    public void searchbar_tf_textchanged(KeyEvent keyEvent) {
        if (type_cb.getValue() == null || type_cb.getValue().equals("All"))
            initUserList(-1 , searchbar_tf.getText());
        else if (type_cb.getValue().equals("Admin"))
            initUserList(0, searchbar_tf.getText());
        else if (type_cb.getValue().equals("Staff"))
            initUserList(1, searchbar_tf.getText());
        else if (type_cb.getValue().equals("Client"))
            initUserList(2, searchbar_tf.getText());
    }

    public void browsemanage_btn_act(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        fileChooser.setTitle("Choose a profile picture");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            photomanage_tf.setText(file.getAbsolutePath());
        }
    }


    public void searchbarsub_tf_textchanged(KeyEvent keyEvent) {
        initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
    }

    public void subtype_cb_act(ActionEvent actionEvent) {
        initSubList(subtype_cb.getValue(), searchbarsub_tf.getText());
    }

    public void deletesub_btn_act(ActionEvent actionEvent) {
        if (subscriptionslist_tableview.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("No subscription selected");
            alert.setContentText("Please select a subscription to delete");
            alert.showAndWait();
            return;
        }
        Abonnement abonnement = subscriptionslist_tableview.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
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
                alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Error");
                alert.setHeaderText("An error has occured");
                alert.setContentText("An error has occured while trying to delete the subscription");
                alert.showAndWait();
                e.printStackTrace();
                return;
            }
        }
    }

    public void savesub_btn_act(ActionEvent actionEvent) {
        if (subscriptionslist_tableview.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user to subscribe");
            alert.showAndWait();
            return;
        }
        if (subtypeedit_cb.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("No subscription type selected");
            alert.setContentText("Please select a subscription type");
            alert.showAndWait();
            return;
        }
        Abonnement abonnement = subscriptionslist_tableview.getSelectionModel().getSelectedItem();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText("An error has occured while trying to subscribe the user");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    public void searchbarusersub_tf_textchanged(KeyEvent keyEvent) {
        initNonSubbedUserList(searchbarusersub_tf.getText());
    }

    public void addsub_btn_act(ActionEvent actionEvent) {
        if (userlistsub_tableview.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user to subscribe");
            alert.showAndWait();
            return;
        }
        if (subtypeadd_cb.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("No subscription type selected");
            alert.setContentText("Please select a subscription type");
            alert.showAndWait();
            return;
        }
        Abonnement abonnement = new Abonnement();
        abonnement.setUser_id(userlistsub_tableview.getSelectionModel().getSelectedItem().getId());
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText("An error has occured while trying to subscribe the user");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }
    private double xOffset = 0;
    private double yOffset = 0;
    public void initialize() {
        Media media = new Media(new File(getClass().getResource("/assets/sounds/welcome.mp3").getFile()).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        fadeInRightAnimation.setNode(AdminHomePane);
        fadeInRightAnimation.play();
        stat_combobox.getItems().addAll(FXCollections.observableArrayList("Abonnements", "Clients", "Staff"));
        type_cb.getItems().addAll(FXCollections.observableArrayList("All", "Admin", "Staff", "Client"));
        acctypemanage_cb.getItems().addAll(FXCollections.observableArrayList("Admin", "Staff", "Client"));
        subtype_cb.getItems().addAll(FXCollections.observableArrayList( "All","GP 1", "GP 2", "GP 3"));
        subtypeadd_cb.getItems().addAll(FXCollections.observableArrayList("GP 1", "GP 2", "GP 3"));
        subtypeedit_cb.getItems().addAll(FXCollections.observableArrayList("GP 1", "GP 2", "GP 3"));
        initProfile();
        initCharts();
        setFitToWidthAll();
        initAnimations();
        initDecoratedStage();
        notify("Successfully Logged In as " + GlobalVar.getUser().getUsername() + "!");
        initUserList(-1, "");
        initSubList("All", "");
        initNonSubbedUserList("");

        RingProgressIndicator progressIndicator = new RingProgressIndicator();
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressIndicator.setLayoutX(hidepane.getPrefWidth()/2 - progressIndicator.getPrefWidth()/2 - 12);
        progressIndicator.setLayoutY(hidepane.getPrefHeight()/2 - progressIndicator.getPrefHeight()/2 - 12);
        progressIndicator.setPrefWidth(24);
        progressIndicator.setPrefHeight(24);
        hidepane.getChildren().add(progressIndicator);
        var warning = new Message("Warning!", "Be careful with the actions you take, they are irreversible! Proceed with caution.");
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
            Pane pane= FXMLLoader.load(getClass().getResource("/gestionevents/eventstaffadmin.fxml"));
            affichage_events_adstaff.getChildren().setAll(pane);
            Pane pane_event= FXMLLoader.load(getClass().getResource("/gestionequipement/equipement.fxml"));
            EquipmentIdAdminStaff.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initSubList(String type, String search){
        List<Abonnement> list = null;
        try {
            if (type == null || type.equals("All"))
                list = abonnementService.getAllCurrent();
            else
                list = abonnementService.getAbonnementByTypeCurrent(type);
            ObservableList<Abonnement> observableList = FXCollections.observableArrayList(list);
            if(search != null && !search.isEmpty()) {
                for (int i = 0; i < observableList.size(); i++) {
                    if (!(String.valueOf(observableList.get(i).getUser_id()).equals(search))) {
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

        }catch (Exception e){
            e.printStackTrace();
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
        AdminHomePane.setFitToWidth(true);
        AdminInfoPane.setFitToWidth(true);
        AdminBlogPane.setFitToWidth(true);
        AdminEventPane.setFitToWidth(true);
        AdminSubscriptionPane.setFitToWidth(true);
        AdminEquipmentManagementPane.setFitToWidth(true);
        AdminStorePane.setFitToWidth(true);
        AdminSettingsPane.setFitToWidth(true);
        AdminUserManagementPane.setFitToWidth(true);
    }
    private ScrollPane getCurrentPane(){
        if(AdminHomePane.isVisible())
            return AdminHomePane;
        if(AdminInfoPane.isVisible())
            return AdminInfoPane;
        if(AdminBlogPane.isVisible())
            return AdminBlogPane;
        if(AdminEventPane.isVisible())
            return AdminEventPane;
        if(AdminSubscriptionPane.isVisible())
            return AdminSubscriptionPane;
        if(AdminEquipmentManagementPane.isVisible())
            return AdminEquipmentManagementPane;
        if(AdminStorePane.isVisible())
            return AdminStorePane;
        if(AdminSettingsPane.isVisible())
            return AdminSettingsPane;
        if(AdminUserManagementPane.isVisible())
            return AdminUserManagementPane;
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

    private void initAnimations(){
        fadeInAnimation[0] = new FadeIn(home_btn);
        fadeInAnimation[1] = new FadeIn(settings_btn);
        fadeInAnimation[2] = new FadeIn(shop_btn);
        fadeInAnimation[3] = new FadeIn(logout_btn);
        fadeInAnimation[4] = new FadeIn(subscription_btn);
        fadeInAnimation[5] = new FadeIn(users_btn);
        fadeInAnimation[6] = new FadeIn(event_btn);
        fadeInAnimation[7] = new FadeIn(blog_btn);
        fadeInAnimation[8] = new FadeIn(equipment_btn);
    }
    private void visibleAll(){
        home_btn.setVisible(true);
        settings_btn.setVisible(true);
        shop_btn.setVisible(true);
        logout_btn.setVisible(true);
        subscription_btn.setVisible(true);
        users_btn.setVisible(true);
        event_btn.setVisible(true);
        blog_btn.setVisible(true);
        equipment_btn.setVisible(true);
    }

    private void invisibleAll(){
        home_btn.setVisible(false);
        settings_btn.setVisible(false);
        shop_btn.setVisible(false);
        logout_btn.setVisible(false);
        subscription_btn.setVisible(false);
        users_btn.setVisible(false);
        event_btn.setVisible(false);
        blog_btn.setVisible(false);
        equipment_btn.setVisible(false);
    }
    private void nullOpacityAll(){
        home_btn.setOpacity(0);
        settings_btn.setOpacity(0);
        shop_btn.setOpacity(0);
        logout_btn.setOpacity(0);
        subscription_btn.setOpacity(0);
        users_btn.setOpacity(0);
        event_btn.setOpacity(0);
        blog_btn.setOpacity(0);
        equipment_btn.setOpacity(0);
    }


    private void initCharts(){
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Jan", 100));
        series.getData().add(new XYChart.Data<>("Feb", 200));
        series.getData().add(new XYChart.Data<>("Mar", 50));
        series.getData().add(new XYChart.Data<>("Apr", 75));
        series.getData().add(new XYChart.Data<>("May", 110));
        series.getData().add(new XYChart.Data<>("Jun", 300));
        series.getData().add(new XYChart.Data<>("Jul", 111));
        series.getData().add(new XYChart.Data<>("Aug", 30));
        series.getData().add(new XYChart.Data<>("Sep", 75));
        series.getData().add(new XYChart.Data<>("Oct", 55));
        series.getData().add(new XYChart.Data<>("Nov", 225));
        series.getData().add(new XYChart.Data<>("Dec", 99));
        series.setName("Lorem");
        stat_linechart.getData().add(series);

        XYChart.Series<String,Number> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>("Jan", 100));
        series2.getData().add(new XYChart.Data<>("Feb", 200));
        series2.getData().add(new XYChart.Data<>("Mar", 50));
        series2.getData().add(new XYChart.Data<>("Apr", 75));
        series2.getData().add(new XYChart.Data<>("May", 110));
        series2.getData().add(new XYChart.Data<>("Jun", 300));
        series2.getData().add(new XYChart.Data<>("Jul", 111));
        series2.getData().add(new XYChart.Data<>("Aug", 30));
        series2.getData().add(new XYChart.Data<>("Sep", 75));
        series2.getData().add(new XYChart.Data<>("Oct", 55));
        series2.getData().add(new XYChart.Data<>("Nov", 225));
        series2.getData().add(new XYChart.Data<>("Dec", 99));
        series2.setName("Ipsum");
        stat_barchart.getData().add(series2);
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
    private void initProfile(){
        goback_btn.setVisible(false);
        deleteacc_btn.setVisible(true);
        saveacc_btn.setVisible(true);
        saveaccmanage_btn.setVisible(false);
        deleteaccmanage_btn.setVisible(false);
        role_label.setText("Admin");
        profilelabel.setText("Admin / Profile");
        String photoname = GlobalVar.getUser().getPhoto();
        user_imageview.setImage(new Image(new File("src/assets/profileuploads/" +photoname).toURI().toString()));
        Circle clip1 = new Circle(user_imageview.getFitWidth()/2, user_imageview.getFitHeight()/2, user_imageview.getFitWidth()/2);
        user_imageview.setClip(clip1);
        user_imageview.setPreserveRatio(false);
        Rectangle clip2 = new Rectangle(cover_imageview.getFitWidth(), cover_imageview.getFitHeight());
        clip2.setArcWidth(30);
        clip2.setArcHeight(30);
        cover_imageview.setClip(clip2);
        userprofile_imageview.setImage(new Image(new File("src/assets/profileuploads/" +photoname).toURI().toString()));
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

    private void initProfileTemp(User user){
        goback_btn.setVisible(true);
        deleteacc_btn.setVisible(false);
        saveacc_btn.setVisible(false);
        saveaccmanage_btn.setVisible(true);
        deleteaccmanage_btn.setVisible(true);
        profilelabel.setText("Admin / Previewing user " + user.getUsername());
        role_label.setText(user.getRole().substring(0, 1).toUpperCase() + user.getRole().substring(1));
        String photoname = user.getPhoto();
        Rectangle clip2 = new Rectangle(cover_imageview.getFitWidth(), cover_imageview.getFitHeight());
        clip2.setArcWidth(30);
        clip2.setArcHeight(30);
        cover_imageview.setClip(clip2);
        userprofile_imageview.setImage(new Image(new File("src/assets/profileuploads/" +photoname).toURI().toString()));
        Circle clip3 = new Circle(userprofile_imageview.getFitWidth()/2, userprofile_imageview.getFitHeight()/2, userprofile_imageview.getFitWidth()/2);
        userprofile_imageview.setPreserveRatio(false);
        userprofile_imageview.setClip(clip3);
        nameinfo.setText(user.getFirstname()+" "+user.getLastname());
        emailinfo.setText(user.getEmail());
        dateofbirth_tf.setValue(LocalDate.parse(user.getDate_naiss()));
        firstname_tf.setText(user.getFirstname());
        lastname_tf.setText(user.getLastname());
        username_tf.setText(user.getUsername());
        email_tf.setText(user.getEmail());
        phone_tf.setText(user.getNum_tel());
        address_ta.setText(user.getAdresse());
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

    private void initUserList(int type, String condition){
        List<User> users = null;
        if (type == 0){
            try {
                users = (List<User>)(List<?>)adminService.getAll();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }else if (type == 1){
            try {
                users = (List<User>)(List<?>)staffService.getAll();


            }catch (Exception e) {
                e.printStackTrace();
            }
        }else if (type == 2){
            try {
                users = (List<User>)(List<?>)clientService.getAll();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }else if (type == -1){
            try {
                users = (List<User>)(List<?>)adminService.getAll();
                users.addAll((List<User>)(List<?>)staffService.getAll());
                users.addAll((List<User>)(List<?>)clientService.getAll());
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        ObservableList<User> obs = FXCollections.observableArrayList(users);
        if (condition != null && !condition.isEmpty()) {
            for (int i = 0; i < obs.size(); i++) {
                if (!obs.get(i).getUsername().contains(condition)) {
                    obs.remove(i);
                    i--;
                }
            }
        }
        userlist_tableview.setItems(obs);
        cincol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernamecol.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstnamecol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnamecol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        dobcol.setCellValueFactory(new PropertyValueFactory<>("date_naiss"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        addresscol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        acctypecol.setCellValueFactory(new PropertyValueFactory<>("role"));
        phonenumbercol.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
    }

    private void initNonSubbedUserList(String condition) {
        try {
            List<User> users = (List<User>)(List<?>)clientService.getNonSubscribedUserList();
            ObservableList<User> obs = FXCollections.observableArrayList(users);
            if (condition != null && !condition.isEmpty()) {
                for (int i = 0; i < obs.size(); i++) {
                    if (!obs.get(i).getUsername().contains(condition)) {
                        obs.remove(i);
                        i--;
                    }
                }
            }
            userlistsub_tableview.setItems(obs);
            cincoladdsub.setCellValueFactory(new PropertyValueFactory<>("id"));
            emailcoladdsub.setCellValueFactory(new PropertyValueFactory<>("email"));
            usernamecoladdsub.setCellValueFactory(new PropertyValueFactory<>("username"));
    }catch(Exception e){
        e.printStackTrace();
    }
}

}
