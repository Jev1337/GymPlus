package controllers.gestionuser;

import animatefx.animation.*;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.micromata.paypal.PayPalConfig;
import de.micromata.paypal.PayPalConnector;
import de.micromata.paypal.data.Currency;
import de.micromata.paypal.data.Payment;
import de.micromata.paypal.data.ShippingPreference;
import de.micromata.paypal.data.Transaction;
import entities.gestionuser.Abonnement;
import entities.gestionuser.Client;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import okhttp3.*;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import services.gestionuser.AbonnementDetailsService;
import services.gestionuser.AbonnementService;
import services.gestionuser.ClientService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDashboardController {

    private ClientService clientService = new ClientService();
    private AbonnementService abonnementService = new AbonnementService();
    private AbonnementDetailsService abonnementDetailsService = new AbonnementDetailsService();
    private FadeIn[] fadeInAnimation = new FadeIn[8];

    private Notification msg = new Notification();
    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();
    private FadeInRight fadeInRightAnimation = new FadeInRight();

    @FXML
    private Pane blogId = new Pane();

    @FXML
    private Pane usereventpane_id ;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane subscribed_pane;

    @FXML
    private Pane unsubscribed_pane;

    @FXML
    private Label daysremain_label;

    @FXML
    private Label packname_label;

    @FXML
    private ImageView QR_imageview;

    @FXML
    private ImageView barcode_imageview;

    @FXML
    void settings_btn_clicked(MouseEvent event) {
        switchToPane(UserSettingsPane);
    }

    @FXML
    void home_btn_act(ActionEvent event) {
        switchToPane(UserHomePane);
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText("An error has occured while trying to logout");
            alert.showAndWait();
        }
    }
    @FXML
    void logout_btn_clicked(MouseEvent event) {
        logout_btn_act(null);
    }

    @FXML
    void settings_btn_act(ActionEvent event) {
        switchToPane(UserSettingsPane);
    }

    @FXML
    void shop_btn_act(ActionEvent event) {
        switchToPane(UserStorePane);
    }

    @FXML
    void subscription_btn_act(ActionEvent event) {
        switchToPane(UserSubscriptionPane);
    }
    @FXML
    void subscription_btn_clicked(MouseEvent event) {
        switchToPane(UserSubscriptionPane);
    }
    @FXML
    void shop_btn_clicked(MouseEvent event) {
        switchToPane(UserStorePane);
    }
    @FXML
    void home_btn_clicked(MouseEvent event) {
        switchToPane(UserHomePane);
    }
    @FXML
    void user_imageview_clicked(MouseEvent event) {
        switchToPane(UserInfoPane);
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
    private ScrollPane UserBlogPane;

    @FXML
    private ScrollPane UserEventPane;

    @FXML
    private ScrollPane UserHomePane;

    @FXML
    private ScrollPane UserInfoPane;

    @FXML
    private ScrollPane UserObjectivePane;

    @FXML
    private ScrollPane UserSettingsPane;

    @FXML
    private ScrollPane UserStorePane;

    @FXML
    private ScrollPane UserSubscriptionPane;

    @FXML
    private TextArea address_ta;

    @FXML
    private FontAwesomeIconView bars_btn;

    @FXML
    private Pane bars_pane;

    @FXML
    private Button blog_btn;

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
    private Button event_btn;

    @FXML
    private TextField firstname_tf;

    @FXML
    private Label globalusername;

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
    private Button objective_btn;

    @FXML
    private TextField phone_tf;

    @FXML
    private TextField profilepic_pf;

    @FXML
    private Button settings_btn;

    @FXML
    private Button shop_btn;
    @FXML
    private BarChart<String, Number> stat_barchart;

    @FXML
    private ComboBox<String> stat_combobox;

    @FXML
    private LineChart<String, Number> stat_linechart;

    @FXML
    private Button subscription_btn;

    @FXML
    private Pane ObjectifPan;



    @FXML
    private ImageView user_imageview;

    @FXML
    private TextField username_tf;

    @FXML
    private ImageView userprofile_imageview;

    @FXML
    private ImageView faceid_change;
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
            Client client = new Client(GlobalVar.getUser().getId(), GlobalVar.getUser().getUsername(), GlobalVar.getUser().getFirstname(), GlobalVar.getUser().getLastname(), GlobalVar.getUser().getDate_naiss(), GlobalVar.getUser().getPassword(), GlobalVar.getUser().getEmail(), GlobalVar.getUser().getNum_tel(), GlobalVar.getUser().getAdresse(), GlobalVar.getUser().getPhoto(), faceId, new Date(System.currentTimeMillis()).toString());
            clientService.update(client);
            GlobalVar.setUser(client);
            initProfile();
            notify("FaceID has been updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    void blog_btn_act(ActionEvent event) {
        switchToPane(UserBlogPane);
    }

    @FXML
    void blog_btn_clicked(MouseEvent event) {
        switchToPane(UserBlogPane);
    }

    @FXML
    void event_btn_act(ActionEvent event) {
        //if user not subscribed it redirects him to the siubscription pane
        try {
            if (!abonnementService.isUserSubscribed(GlobalVar.getUser().getId())) {
                switchToPane(UserSubscriptionPane);
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        switchToPane(UserEventPane);
    }


    @FXML
    void event_btn_clicked(MouseEvent event) {
        {
            //if user not subscribed it redirects him to the siubscription pane
            try {
                if (!abonnementService.isUserSubscribed(GlobalVar.getUser().getId())) {
                    switchToPane(UserSubscriptionPane);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            switchToPane(UserEventPane);
        }
    }

    @FXML
    void objective_btn_act(ActionEvent event) {
        switchToPane(UserObjectivePane);
    }

    @FXML
    void objective_btn_clicked(MouseEvent event) {
        switchToPane(UserObjectivePane);
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
                Client client = new Client(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), GlobalVar.getUser().getPhoto(), GlobalVar.getUser().getFaceid(), GlobalVar.getUser().getFaceid_ts());
                clientService.update(client);
                GlobalVar.setUser(client);
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
                Client client = new Client(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), "USERIMG"+ GlobalVar.getUser().getId() + file.getName().substring(file.getName().lastIndexOf(".")), GlobalVar.getUser().getFaceid(), GlobalVar.getUser().getFaceid_ts());
                clientService.update(client);
                GlobalVar.setUser(client);
                initProfile();
            }
            notify("Account has been updated successfully!");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action is irreversible");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                userprofile_imageview.setImage(null);
                user_imageview.setImage(null);
                File file = new File("src/assets/profileuploads/" +GlobalVar.getUser().getPhoto());
                file.delete();
                clientService.delete(GlobalVar.getUser().getId());
            }catch (Exception e){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Error");
                alert.setHeaderText("An error has occured");
                alert.setContentText("An error has occured while trying to delete your account");
                alert.showAndWait();
                e.printStackTrace();
                return;

            }
            logout_btn_act(null);
        }
    }
    @FXML
    void stat_combobox_act(ActionEvent event) {

    }

    @FXML
    void cancel_sub_act(ActionEvent event) {
        try {
            abonnementService.delete(abonnementService.getCurrentSubscription(GlobalVar.getUser().getId()).getId());
            fadeOutRightAnimation.setNode(subscribed_pane);
            fadeOutRightAnimation.setOnFinished(e -> {
                subscribed_pane.setVisible(false);
                unsubscribed_pane.setOpacity(0);
                unsubscribed_pane.setVisible(true);
                fadeInRightAnimation.setNode(unsubscribed_pane);
                fadeInRightAnimation.play();
            });
            fadeOutRightAnimation.play();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void buy(int pkg){
        try {
            if (pkg == 1) {
                abonnementService.add(new Abonnement(GlobalVar.getUser().getId(), Date.valueOf(LocalDate.now().plusMonths(3)).toString(), "GP 1"));
            }else if (pkg == 2) {
                abonnementService.add(new Abonnement(GlobalVar.getUser().getId(), Date.valueOf(LocalDate.now().plusMonths(6)).toString(), "GP 2"));
            }else if (pkg == 3) {
                abonnementService.add(new Abonnement(GlobalVar.getUser().getId(), Date.valueOf(LocalDate.now().plusMonths(12)).toString(), "GP 3"));
            }
            Date date = Date.valueOf(abonnementService.getCurrentSubscription(GlobalVar.getUser().getId()).getDuree_abon());
            long diff = date.getTime() - System.currentTimeMillis();
            long days = diff / (24 * 60 * 60 * 1000);
            daysremain_label.setText(days + " days");
            packname_label.setText(abonnementService.getCurrentSubscription(GlobalVar.getUser().getId()).getType());
            ByteArrayOutputStream out = QRCode.from(String.valueOf(GlobalVar.getUser().getId())).to(ImageType.PNG).withSize(168, 149).stream();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            QR_imageview.setImage(new Image(in));
            MultiFormatWriter ean8Writer = new MultiFormatWriter();
            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(ean8Writer.encode(String.valueOf(GlobalVar.getUser().getId()), BarcodeFormat.CODE_128, 280,73), "png", out2);
            ByteArrayInputStream in2 = new ByteArrayInputStream(out2.toByteArray());
            barcode_imageview.setImage(new Image(in2));
            fadeOutRightAnimation.setNode(unsubscribed_pane);
            fadeOutRightAnimation.setOnFinished(e -> {
                unsubscribed_pane.setVisible(false);
                subscribed_pane.setOpacity(0);
                subscribed_pane.setVisible(true);
                fadeInRightAnimation.setNode(subscribed_pane);
                fadeInRightAnimation.play();
            });
            fadeOutRightAnimation.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setupBuy(double price){
        PayPalConfig paypalConfig = new PayPalConfig()
                .setClientId("Af9N6m41ryO-aKuZO3cwgGr1PZeBsbxLTeSYVJ7iUr71UO3tA8Uc0p50NeEMbewLzmq00go8MoXAzXPC").setClientSecret("EBfq6ayRpNgVeWdALsblGLrMKUspTHt5GzfuH5MOM1FN7gqsoR6y5TCOTTYlm4R3adGWPgKoWYOI46Xz")
                .setReturnUrl("http://localhost/gymplus/checkout/success/").setCancelUrl("http://localhost/gymplus/checkout/cancel/")
                .setMode(PayPalConfig.Mode.SANDBOX);
        Transaction transaction = new Transaction(Currency.EUR); // Every price in EUR
        transaction.addItem("Gym Plus Membership", price);  // Item to sell for 29.99 plus optional tax.
        transaction.setInoviceNumber(String.valueOf(GlobalVar.getUser().getId() + Date.valueOf(LocalDate.now()).toString()));
        Payment payment = new Payment(transaction);              // A payment has transaction(s).
        payment.setNoteToPayer("Please contact ...");            // Note to payer for important messages.
        payment.setShipping(ShippingPreference.NO_SHIPPING);     // Don't prompt the user for a shipping address.
        try {
            Payment paymentCreated = PayPalConnector.createPayment(paypalConfig, payment);
            //database.save(paymentCreated.getOriginalPayPalResponse()); // optional but recommended.
            if (paymentCreated != null) {
                String redirectUrl = paymentCreated.getPayPalApprovalUrl();
                // create new window that has webview to show the redirectUrl
                Dialog<String> alert = new Dialog<>();
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("PayPal Payment");
                alert.setHeaderText("PayPal Payment");
                ButtonType okButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getDialogPane().getButtonTypes().add(okButton);
                WebView webView = new WebView();
                webView.getEngine().load(redirectUrl);
                alert.getDialogPane().setContent(webView);
                Worker<Void> worker = webView.getEngine().getLoadWorker();
                worker.stateProperty().addListener(e->{
                    if (worker.getState() == Worker.State.SUCCEEDED) {
                        if (webView.getEngine().getLocation().contains("success")) {
                            alert.close();
                        }else if (webView.getEngine().getLocation().contains("cancel")) {
                            alert.close();
                        }
                    }
                });
                alert.showAndWait();
                if (webView.getEngine().getLocation().contains("success")) {
                    alert.close();
                    return true;
                }else {
                    alert.close();
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void buy1_btn_act(ActionEvent actionEvent) {
        try {
            if (setupBuy(abonnementDetailsService.getPriceByType("GP 1"))) {
                buy(1);
                notify("Payment was successful!");
            } else
                notify("Payment was cancelled!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buy2_btn_act(ActionEvent actionEvent) {
        try {
            if (setupBuy(abonnementDetailsService.getPriceByType("GP 2"))) {
                buy(2);
                notify("Payment was successful!");
            } else
                notify("Payment was cancelled!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buy3_btn_act(ActionEvent actionEvent) {
        try {
            if (setupBuy(abonnementDetailsService.getPriceByType("GP 3"))) {
                buy(3);
                notify("Payment was successful!");
            } else
                notify("Payment was cancelled!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private double xOffset = 0;
    private double yOffset = 0;
    public void initialize() {
        Media media = new Media(new File(getClass().getResource("/assets/sounds/welcome.mp3").getFile()).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        fadeInRightAnimation.setNode(UserHomePane);
        fadeInRightAnimation.play();
        stat_combobox.getItems().addAll(FXCollections.observableArrayList("Abonnements", "Clients", "Staff"));
        initProfile();
        initCharts();
        setFitToWidthAll();
        initAnimations();
        initDecoratedStage();
        notify("Successfully Logged In as " + GlobalVar.getUser().getUsername() + "!");
        initSubsciption();
        initGPPrices();
        try {
            Pane pane= FXMLLoader.load(getClass().getResource("/gestionBlog/Blog.fxml"));
            blogId.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Pane pane_event= FXMLLoader.load(getClass().getResource("/gestionevents/event.fxml"));
            usereventpane_id.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Pane pane_Objectif= FXMLLoader.load(getClass().getResource("/gestionSuivi/objectif1.fxml"));
            ObjectifPan.getChildren().setAll(pane_Objectif);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private Label gp1_label;
    @FXML
    private Label gp2_label;
    @FXML
    private Label gp3_label;

    private void initGPPrices(){
        try {
            gp1_label.setText(abonnementDetailsService.getPriceByType("GP 1") + "€ / 3 months");
            gp2_label.setText(abonnementDetailsService.getPriceByType("GP 2") + "€ / 6 months");
            gp3_label.setText(abonnementDetailsService.getPriceByType("GP 3") + "€ / 12 months");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initSubsciption(){
        try {
            if (abonnementService.isUserSubscribed(GlobalVar.getUser().getId())) {
                Date date = Date.valueOf(abonnementService.getCurrentSubscription(GlobalVar.getUser().getId()).getDuree_abon());
                long diff = date.getTime() - System.currentTimeMillis();
                long days = diff / (24 * 60 * 60 * 1000);
                daysremain_label.setText(days + " days");
                packname_label.setText(abonnementService.getCurrentSubscription(GlobalVar.getUser().getId()).getType());
                ByteArrayOutputStream out = QRCode.from(String.valueOf(GlobalVar.getUser().getId())).to(ImageType.PNG).withSize(168, 149).stream();
                ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                QR_imageview.setImage(new Image(in));

                MultiFormatWriter ean8Writer = new MultiFormatWriter();
                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(ean8Writer.encode(String.valueOf(GlobalVar.getUser().getId()), BarcodeFormat.CODE_128, 280,73), "png", out2);
                ByteArrayInputStream in2 = new ByteArrayInputStream(out2.toByteArray());
                barcode_imageview.setImage(new Image(in2));
                subscribed_pane.setVisible(true);
                unsubscribed_pane.setVisible(false);
            } else {
                subscribed_pane.setVisible(false);
                unsubscribed_pane.setVisible(true);
            }
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
    private void initProfile(){

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

    private void setFitToWidthAll(){
        UserHomePane.setFitToWidth(true);
        UserInfoPane.setFitToWidth(true);
        UserObjectivePane.setFitToWidth(true);
        UserBlogPane.setFitToWidth(true);
        UserEventPane.setFitToWidth(true);
        UserSubscriptionPane.setFitToWidth(true);
        UserStorePane.setFitToWidth(true);
        UserSettingsPane.setFitToWidth(true);
    }
    private ScrollPane getCurrentPane(){
        if(UserHomePane.isVisible())
            return UserHomePane;
        if(UserInfoPane.isVisible())
            return UserInfoPane;
        if(UserObjectivePane.isVisible())
            return UserObjectivePane;
        if(UserBlogPane.isVisible())
            return UserBlogPane;
        if(UserEventPane.isVisible())
            return UserEventPane;
        if(UserSubscriptionPane.isVisible())
            return UserSubscriptionPane;
        if(UserStorePane.isVisible())
            return UserStorePane;
        if (UserSettingsPane.isVisible())
            return UserSettingsPane;
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
        fadeInAnimation[5] = new FadeIn(objective_btn);
        fadeInAnimation[6] = new FadeIn(event_btn);
        fadeInAnimation[7] = new FadeIn(blog_btn);
    }
    private void visibleAll(){
        home_btn.setVisible(true);
        settings_btn.setVisible(true);
        shop_btn.setVisible(true);
        logout_btn.setVisible(true);
        subscription_btn.setVisible(true);
        objective_btn.setVisible(true);
        event_btn.setVisible(true);
        blog_btn.setVisible(true);
    }

    private void invisibleAll(){
        home_btn.setVisible(false);
        settings_btn.setVisible(false);
        shop_btn.setVisible(false);
        logout_btn.setVisible(false);
        subscription_btn.setVisible(false);
        objective_btn.setVisible(false);
        event_btn.setVisible(false);
        blog_btn.setVisible(false);
    }
    private void nullOpacityAll(){
        home_btn.setOpacity(0);
        settings_btn.setOpacity(0);
        shop_btn.setOpacity(0);
        logout_btn.setOpacity(0);
        subscription_btn.setOpacity(0);
        objective_btn.setOpacity(0);
        event_btn.setOpacity(0);
        blog_btn.setOpacity(0);
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

}