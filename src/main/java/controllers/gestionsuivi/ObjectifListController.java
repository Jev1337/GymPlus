package controllers.gestionsuivi;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValues;
import com.github.javafaker.service.FakeValuesInterface;
import controllers.gestionuser.GlobalVar;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HorizontalDirection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.gestionSuivi.ObjectifService;
import test.ObjectifTestingFX;
import utils.MyDatabase;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ObjectifListController implements Initializable  {


    @FXML
    private Pane PaneItem;
    @FXML
    private VBox pannelbuttonVbox;


    @FXML
    private Label DateDebutField;

    @FXML
    private Label DateFinField;

    @FXML
    private Label NameField;

    @FXML
    private Label TypeObjField;
    @FXML
    private ImageView CoachPic;
    @FXML
    private VBox VboxToBlue;
    MyDatabase c = new MyDatabase();

    public int getCoachIdByName(String name_coach_selected) {
        String query = "SELECT id  FROM user WHERE FirstName = ? AND Role='staff'";
        int coachId = -1;

        try {
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setString(1, name_coach_selected);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                coachId = rs.getInt("id");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coachId;
    }

    public void buttonPanel(){
        var accentBtn = new Button(null, new FontAwesomeIconView(FontAwesomeIcon.LIST));
        accentBtn.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.ACCENT
        );
        pannelbuttonVbox.getChildren().add(accentBtn);

    }

    @FXML
    private VBox deleteVbox;
    private static  Scene scene;
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {

        ObjectifListController.scene = scene;
    }
    private static Stage stage ;
    public static Stage getStage() {
        return stage;
    }

    @FXML
    private VBox WarningVbox2;
    public void warning () {
        var warning = new Message(
                "Warning",
                Faker.instance().expression("Suppression Annulée ,veuillez entrer oui si vous voullez supprimer"),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)

        );
        WarningVbox2.getChildren().clear();
        warning.getStyleClass().add(Styles.WARNING);
        WarningVbox2.getChildren().addAll(warning);

        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> WarningVbox2.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();

    }
    private  static  ObjectifController objectifController ;
    public void setObjectifController(ObjectifController objectifController) {
        ObjectifListController.objectifController = objectifController;
    }

    public void scrollButton (Objectif obj)
    {
        var toggle2 = new ToggleSwitch("Supprimer");
        toggle2.selectedProperty().addListener((obs, old, val) -> {
                toggle2.setText(val ? "en cours" : "Supprimer");
                // Perform the desired action when the toggle switch is clicked
                if (val) {


                var dialog = new TextInputDialog();
                dialog.setTitle("Text Input Dialog");
                Button buttonType = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                buttonType.setDisable(false);

                dialog.setHeaderText(Faker.instance().expression("Supression Confirmation"));
                dialog.setContentText("Tapez Oui:");
                dialog.initOwner(getStage());
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent() && result.get().equalsIgnoreCase("oui")) {
                    System.out.println("fi wost el try");
                    ObjectifService querry = new ObjectifService();

                    try {
                        querry.delete(obj.getId_objectif());
                        if (objectifController != null) {
                            objectifController.refreshNodesListeItems();
                        } else {
                            System.out.println("objectifController is null");
                        }

                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                  warning();
                    toggle2.setLabelPosition(HorizontalDirection.RIGHT);
                    toggle2.setSelected(false);

                }

            } else {
                // Action to perform when toggle switch is deselected
                System.out.println("Toggle switch is NOT selected");
                // Add your code here to handle the action
            }
        });
      // toggle2.setLabelPosition(HorizontalDirection.RIGHT);
        // toggle2.setSelected(false);
        deleteVbox.getChildren().add(toggle2);

    }

   /* public void enterYesFunction (){
        var button = new Button("Click", new FontIcon(Feather.EDIT_2));
        button.setOnAction(e -> {
            var dialog = new TextInputDialog();
            dialog.setTitle("Text Input Dialog");
            dialog.setHeaderText(FAKER.chuckNorris().fact());
            dialog.setContentText("Enter your name:");
            dialog.initOwner(getScene().getWindow());
            show(dialog);
        });
    }*/

    public ObservableList<Objectif> getObjectifStatusList() {

        ObservableList<Objectif> objectifList = FXCollections.observableArrayList();
        try {
            int userId = GlobalVar.getUser().getId();

            String query = "SELECT o.idObjectif , o.userId, o.poidsObj, o.DateD, o.DateF, o.PoidsAct, o.Taille, o.Alergie, o.TypeObj, u.photo, u.username AS username " +
                    "FROM objectif o " +
                    "JOIN user u ON o.CoachId = u.id " +
                    "WHERE o.userId = ?  AND u.role='staff' ";


            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Objectif objectifs = new Objectif();
                objectifs.setId_objectif(rs.getInt("idObjectif"));
                objectifs.setId_user(rs.getInt("userId"));
                objectifs.setPoids_Obj(rs.getFloat("poidsObj"));
                objectifs.setDateD(rs.getDate("DateD"));
                objectifs.setDateF(rs.getDate("DateF"));
                objectifs.setPoids_Act(rs.getFloat("PoidsAct"));
                objectifs.setTaille(rs.getFloat("Taille"));
                objectifs.setAlergie(rs.getString("Alergie"));
                objectifs.setTypeObj(rs.getString("TypeObj"));
                objectifs.setCoachPhoto(rs.getString("photo"));
                objectifs.setCoachName(rs.getString("username"));

                objectifList.add(objectifs);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objectifList;
    }




   /* public void handleButtonClick(Objectif obj) {
        System.out.println("Button clicked for objectif: " + obj);
        AddObjectifController addController = new AddObjectifController();
        addController.sendingDataToForm(obj);

     }*/

    public void setObjectif(Objectif objectif) throws MalformedURLException {
        NameField.setText(objectif.getCoachName());
        TypeObjField.setText(objectif.getTypeObj());
        java.sql.Date dateD = objectif.getDateD();
        java.sql.Date dateF = objectif.getDateF();

        String dateString = null;
        String dateString2 = null;

        if (dateD != null && dateF!=null) {
            dateString = dateD.toString();
            dateString2 = dateF.toString();

        }
        DateDebutField.setText(dateString);
        DateFinField.setText(dateString2);

        String coachPhotoPath =  "webapp/src/gymplus/public/profileuploads/"+ objectif.getCoachPhoto();
        System.out.println(coachPhotoPath);
        if (coachPhotoPath != null && !coachPhotoPath.isEmpty()) {
            File file = new File(coachPhotoPath);
            if (file.exists()) {
                try {
                    String imageUrl = file.toURI().toURL().toString();
                    System.out.println(imageUrl);
                    CoachPic.setImage(null);
                    Image coachImage = new Image(imageUrl, true);
                    CoachPic.setImage(coachImage);

                    //*** rendering into cercle
                    Circle clip = new Circle(CoachPic.getFitWidth()/2, CoachPic.getFitHeight()/2, CoachPic.getFitWidth()/2
                    );
                    CoachPic.setClip(clip);
                    CoachPic.setPreserveRatio(false);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else {
                String imageUrl = file.toURI().toURL().toString();
                file = new File(imageUrl);
                CoachPic.setImage(null);
                Image coachImage = new Image(imageUrl, true);
                CoachPic.setImage(coachImage);
                //*** rendering into cercle
                Circle clip = new Circle(CoachPic.getFitWidth()/2, CoachPic.getFitHeight()/2, CoachPic.getFitWidth()/2
                );
                CoachPic.setClip(clip);
                CoachPic.setPreserveRatio(false);


                System.out.println("File does not exist: " + file.getAbsolutePath());

            }
        } else {
            CoachPic.setImage(null);
        }

        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VboxToBlue.setOnMouseEntered(event -> VboxToBlue.setStyle("-fx-background-color: lightblue;"));
        VboxToBlue.setOnMouseExited(event -> VboxToBlue.setStyle("-fx-background-color: lightgray;"));
    }
}








