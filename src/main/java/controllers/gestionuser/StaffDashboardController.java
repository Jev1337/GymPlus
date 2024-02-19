package controllers.gestionuser;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import atlantafx.base.controls.Message;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionuser.Abonnement;
import entities.gestionuser.Client;
import entities.gestionuser.Staff;
import entities.gestionuser.User;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.gestionuser.AbonnementService;
import services.gestionuser.ClientService;
import services.gestionuser.StaffService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class StaffDashboardController {

    private final StaffService staffService = new StaffService();

    private final ClientService clientService = new ClientService();
    private final AbonnementService abonnementService = new AbonnementService();

    private FadeIn[] fadeInAnimation = new FadeIn[7];
    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();
    private FadeInRight fadeInRightAnimation = new FadeInRight();

    private Notification msg = new Notification();

    @FXML
    private Button subscription_btn;

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
    private BarChart<String, Number> stat_barchart;

    @FXML
    private ComboBox<String> stat_combobox;

    @FXML
    private LineChart<String, Number> stat_linechart;

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
                Staff staff = new Staff(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), GlobalVar.getUser().getPhoto());
                staffService.update(staff);
                GlobalVar.setUser(staff);
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
                Staff staff = new Staff(GlobalVar.getUser().getId(), username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), dateofbirth_tf.getValue().toString(), GlobalVar.getUser().getPassword(), email_tf.getText(), phone_tf.getText(), address_ta.getText(), "USERIMG"+ GlobalVar.getUser().getId() + file.getName().substring(file.getName().lastIndexOf(".")));
                staffService.update(staff);
                GlobalVar.setUser(staff);
                initProfile();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Success");
            alert.setHeaderText("Profile picture updated");
            alert.setContentText("Your profile picture has been updated successfully");
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
                staffService.delete(GlobalVar.getUser().getId());
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
    }

    @FXML
    void home_btn_clicked(MouseEvent event) {
        switchToPane(StaffHomePane);
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

    }

    @FXML
    void subscription_btn_act(ActionEvent event) {
        switchToPane(StaffSubscriptionPane);
    }

    @FXML
    void subscription_btn_clicked(MouseEvent event) {
        switchToPane(StaffSubscriptionPane);
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


    private void initAnimations(){
        fadeInAnimation[0] = new FadeIn(home_btn);
        fadeInAnimation[1] = new FadeIn(settings_btn);
        fadeInAnimation[2] = new FadeIn(shop_btn);
        fadeInAnimation[3] = new FadeIn(logout_btn);
        fadeInAnimation[4] = new FadeIn(subscription_btn);
        fadeInAnimation[5] = new FadeIn(event_btn);
        fadeInAnimation[6] = new FadeIn(equipment_btn);
    }
    private void visibleAll(){
        home_btn.setVisible(true);
        settings_btn.setVisible(true);
        shop_btn.setVisible(true);
        logout_btn.setVisible(true);
        subscription_btn.setVisible(true);
        event_btn.setVisible(true);
        equipment_btn.setVisible(true);
    }

    private void invisibleAll(){
        home_btn.setVisible(false);
        settings_btn.setVisible(false);
        shop_btn.setVisible(false);
        logout_btn.setVisible(false);
        subscription_btn.setVisible(false);
        event_btn.setVisible(false);
        equipment_btn.setVisible(false);
    }
    private void nullOpacityAll(){
        home_btn.setOpacity(0);
        settings_btn.setOpacity(0);
        shop_btn.setOpacity(0);
        logout_btn.setOpacity(0);
        subscription_btn.setOpacity(0);
        event_btn.setOpacity(0);
        equipment_btn.setOpacity(0);
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
        if (subtypeedit_cb.getValue().equals("GP1"))
            date = Date.valueOf(LocalDate.now().plusMonths(3));
        else if (subtypeedit_cb.getValue().equals("GP2"))
            date = Date.valueOf(LocalDate.now().plusMonths(6));
        else if (subtypeedit_cb.getValue().equals("GP3"))
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
        if (subtypeadd_cb.getValue().equals("GP1"))
            date = Date.valueOf(LocalDate.now().plusMonths(3));
        else if (subtypeadd_cb.getValue().equals("GP2"))
            date = Date.valueOf(LocalDate.now().plusMonths(6));
        else if (subtypeadd_cb.getValue().equals("GP3"))
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
        fadeInRightAnimation.setNode(StaffHomePane);
        fadeInRightAnimation.play();
        stat_combobox.getItems().addAll(FXCollections.observableArrayList("Abonnements", "Clients", "Staff"));
        subtype_cb.getItems().addAll(FXCollections.observableArrayList( "All","GP 1", "GP 2", "GP 3"));
        subtypeadd_cb.getItems().addAll(FXCollections.observableArrayList("GP1", "GP2", "GP3"));
        subtypeedit_cb.getItems().addAll(FXCollections.observableArrayList("GP1", "GP2", "GP3"));

        initProfile();
        initCharts();
        setFitToWidthAll();
        initAnimations();
        initDecoratedStage();
        notify("Successfully Logged In as " + GlobalVar.getUser().getUsername() + "!");
        initSubList("All", "");
        initNonSubbedUserList("");
        notify("Successfully Logged In as " + GlobalVar.getUser().getUsername() + "!");

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

    private void initNonSubbedUserList(String condition) {
        try {
            List<User> users = (List<User>) (List<?>) clientService.getNonSubscribedUserList();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initSubList(String type, String search){
        List<Abonnement> list = null;
        try {
            if (type == null || type.equals("All"))
                list = abonnementService.getAll();
            else
                list = abonnementService.getAbonnementByType(type);
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
}
