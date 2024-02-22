package controllers.gestionsuivi;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.RingProgressIndicator;
import atlantafx.base.theme.Styles;
import com.github.javafaker.Faker;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.gestionSuivi.ObjectifService;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddObjectifController implements Initializable {




    @FXML
    private DatePicker dpDureeObjectif;

    @FXML
    private Text labelTaille;

    @FXML
    private ChoiceBox<String> lsCoachName;

    @FXML
    private ChoiceBox<String> lsTypeObjectif;

    @FXML
    private Slider slTaille;

    @FXML
    private TextArea taAlergie;

    @FXML
    private TextField tfPoidsActuelle;

    @FXML
    private TextField tfPoidsObjectif;

    @FXML
    private VBox vboxTest;

    int taille;

    private String[] type = {"Par Defaut","Version ++"};


    @FXML
    private ToggleButton barToggle;


    private  Objectif objectif2 ;



    @FXML
    private Label TtileLabel;


    @FXML
    private VBox VboxModify;

    private static Stage stage ;
    private Objectif obj;


    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        AddObjectifController.stage = stage;
    }


    public void modifNotif() throws IOException {

        var success = new Message(
                "Success", Faker.instance().expression("Gym Plus vous informe que la modification est faite avec succées"),
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
        HboxAddNotif.getChildren().clear();
        success.getStyleClass().add(Styles.ACCENT);
        HboxAddNotif.getChildren().add(success);

        // Schedule a task to clear the warning after 4 seconds
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    @FXML
void updateObjectif(ActionEvent event) {
    //tawa bech nconverti el date
    float poidsObjectif = Float.parseFloat(tfPoidsObjectif.getText());
    LocalDate currentDate = LocalDate.now();
    LocalDate mydate = dpDureeObjectif.getValue();
    java.sql.Date sqlDureeObjectifValue = java.sql.Date.valueOf(mydate);
    java.sql.Date sqlDureeNow =java.sql.Date.valueOf(currentDate);
    float poidsActuel = Float.parseFloat(tfPoidsActuelle.getText());
    float taille1 =(float) slTaille.getValue() ;
    String alergie = taAlergie.getText();
    String typeObj = lsTypeObjectif.getSelectionModel().getSelectedItem();
    String name_coach_selected =  lsCoachName.getSelectionModel().getSelectedItem();

    //*********************************************Confirmation Pannel************************//
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Modifcation Confirmation");
    dialog.setHeaderText("Confirm your Objectif");
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(getStage()); // Modify this line to get the appropriate stage reference
    Label label1 = new Label("Date fin de l'objectif " + dpDureeObjectif.getValue() + " \n Votre Coach sera " + lsCoachName.getSelectionModel().getSelectedItem());
    dialog.getDialogPane().setContent(label1);
    ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

    Platform.runLater(() -> {
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the dialog result here
        result.ifPresent(buttonType -> {
            if (buttonType == okButton) {
                int userid=2;

                ObjectifService querry = new ObjectifService();
                int coachId = querry.getCoachIdByName(name_coach_selected); // Retrieve the coach's ID
                Objectif obj2 = new Objectif(obj.getId_objectif(),userid,poidsObjectif,sqlDureeObjectifValue,poidsActuel,taille1,alergie,typeObj,coachId) ;
                System.out.println("fel update fn");
             //   System.out.println(this.objectif2.getId_objectif());
                obj2.setDateD(sqlDureeNow);
                try {
                    System.out.printf("passed the try");
                    querry.update(obj2);
                    modifNotif();
                    if (objectifController != null) {
                        objectifController.refreshNodesListeItems();
                    } else {
                        System.out.println("objectifController is null");
                    }

                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (buttonType == cancelButton) {
                System.out.println("nope");
            }
        });
    });

}
    @FXML
    private HBox HboxAddNotif;

public void addingNotif() throws IOException {

        var success = new Message(
                "Success", Faker.instance().expression("Gym Plus vous informe que l'ajout est fait avec succées"),
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
    HboxAddNotif.getChildren().clear();
    success.getStyleClass().add(Styles.ACCENT);
     HboxAddNotif.getChildren().add(success);

    // Schedule a task to clear the warning after 4 seconds
    Duration duration = Duration.seconds(3);
    KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
    Timeline timeline = new Timeline(keyFrame);
    timeline.play();
}


   private  static  ObjectifController objectifController;
    public void setObjectifController(ObjectifController objectifController) {
        AddObjectifController.objectifController = objectifController;
    }
    @FXML
    void addObjectif(ActionEvent event) throws SQLException {
        //tawa bech nconverti el date
        float poidsObjectif = Float.parseFloat(tfPoidsObjectif.getText());
        LocalDate currentDate = LocalDate.now();
        LocalDate mydate = dpDureeObjectif.getValue();
        java.sql.Date sqlDureeObjectifValue = java.sql.Date.valueOf(mydate);
        java.sql.Date sqlDureeNow =java.sql.Date.valueOf(currentDate);
        float poidsActuel = Float.parseFloat(tfPoidsActuelle.getText());
        float taille1 =(float) slTaille.getValue() ;
        String alergie = taAlergie.getText();
        String typeObj = lsTypeObjectif.getSelectionModel().getSelectedItem();
        String name_coach_selected =  lsCoachName.getSelectionModel().getSelectedItem();

        //*********************************************Confirmation Pannel************************//
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Confirmation");
        dialog.setHeaderText("Confirm your Objectif");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage()); // Modify this line to get the appropriate stage reference
        Label label1 = new Label("Date fin de l'objectif " + dpDureeObjectif.getValue() + " \n Votre Coach sera " + lsCoachName.getSelectionModel().getSelectedItem());
        dialog.getDialogPane().setContent(label1);
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Platform.runLater(() -> {
            Optional<ButtonType> result = dialog.showAndWait();

            // Handle the dialog result here
            result.ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    int userid=2;

                    ObjectifService querry = new ObjectifService();
                    int coachId = querry.getCoachIdByName(name_coach_selected); // Retrieve the coach's ID
                    Objectif obj = new Objectif(userid,poidsObjectif,sqlDureeObjectifValue,poidsActuel,taille1,alergie,typeObj,coachId) ;
                    obj.setDateD(sqlDureeNow);
                    try {
                        querry.add(obj);
                        addingNotif();
                        if (objectifController != null) {
                            objectifController.refreshNodesListeItems();
                        } else {
                            System.out.println("objectifController is null");
                        }
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }

                } else if (buttonType == cancelButton) {
                    System.out.println("nope");
                }
            });
        });

        //******************************************************************************************************************//

    }




    public void displayCoachNamefromCoachTable() {
        String  selectAllData ="SELECT * FROM user WHERE role='Coach'";
        MyDatabase c = new MyDatabase();

        try {
            PreparedStatement prepare = c.getConnection().prepareStatement(selectAllData);
            ResultSet result = prepare.executeQuery();
            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()){
                String item = result.getString("username");
                listData.add(item);
            }
            lsCoachName.setItems(listData) ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public void loadingButton(){

        var indicatorToggle = new ToggleButton("Modify");
        indicatorToggle.textProperty().bind(Bindings.createStringBinding(
                () -> indicatorToggle.isSelected() ? "Stop" : "Modify",
                indicatorToggle.selectedProperty()
        ));
        var indicator = new ProgressIndicator(0);
        indicator.setMinSize(10, 10);
        indicator.progressProperty().bind(Bindings.createDoubleBinding(
                () -> indicatorToggle.isSelected() ? -1d : 0d,
                indicatorToggle.selectedProperty()
        ));
        indicatorToggle.setOnAction(event -> {
            if (indicatorToggle.isSelected()) {
                System.out.println("fel loading function  ");
                System.out.println(obj.getId_objectif());
                //id_objectif_Stored = obj.getId_objectif();

                PauseTransition pause = new PauseTransition(Duration.seconds(4));
                pause.setOnFinished(e -> {
                    updateObjectif(new ActionEvent());
                    indicatorToggle.setSelected(false);
                });
                pause.play();
            }
        });

        VboxModify.getChildren().addAll(indicatorToggle,indicator);

    }



    public  void loadButtons(){

        var barToggle = new ToggleButton("Modifier");
        barToggle.textProperty().bind(Bindings.createStringBinding(
                () -> barToggle.isSelected() ? "Stop" : "Modifier",
                barToggle.selectedProperty()
        ));
        var bar = new ProgressBar(0);
        bar.progressProperty().bind(Bindings.createDoubleBinding(
                () -> barToggle.isSelected() ? -1d : 0d,
                barToggle.selectedProperty()
        ));
        barToggle.setOnAction(event -> {
            if (barToggle.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(4));
                pause.setOnFinished(e -> {
                    try {
                        addObjectif(new ActionEvent());
                        barToggle.setSelected(false);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                });
                pause.play();
            }
        });

        VboxModify.getChildren().addAll(barToggle,bar);
    }

     void sendingDataToForm(Objectif obj) {

         try {
             // VboxModify.setVisible(true);

             TtileLabel.setText("Modification Objectif");

             int id_obj=obj.getId_objectif();
             int id_user = obj.getId_user();
             System.out.printf("fel sendingDataForm function ");
             System.out.println(id_obj);
             System.out.println(id_user);



             String coachName = obj.getCoachName();
             obj = new Objectif(obj.getId_objectif(),obj.getId_user(),obj.getPoids_Obj(),obj.getDateF(),obj.getPoids_Act(),obj.getTaille(),obj.getAlergie(),obj.getTypeObj(),obj.getCoachId());
             this.obj = obj;

             tfPoidsObjectif.setText(String.valueOf(obj.getPoids_Obj()));
             java.sql.Date dateF = obj.getDateF();
             LocalDate localDateF = dateF.toLocalDate();
             dpDureeObjectif.setValue(localDateF);
             tfPoidsActuelle.setText(String.valueOf(obj.getPoids_Act()));
             slTaille.setValue(obj.getTaille());
             taAlergie.setText(obj.getAlergie());
             String typeObj = obj.getTypeObj();
             lsTypeObjectif.getSelectionModel().select(typeObj);
             lsCoachName.getSelectionModel().select(coachName);

             loadingButton();




         }catch (Exception e){
             e.printStackTrace();
         }
     }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        var barToggle = new ToggleButton("Valider");
        barToggle.textProperty().bind(Bindings.createStringBinding(
                () -> barToggle.isSelected() ? "Stop" : "Valider",
                barToggle.selectedProperty()
        ));
        var bar = new ProgressBar(0);
        bar.progressProperty().bind(Bindings.createDoubleBinding(
                () -> barToggle.isSelected() ? -1d : 0d,
                barToggle.selectedProperty()
        ));
        barToggle.setOnAction(event -> {
            if (barToggle.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(4));
                pause.setOnFinished(e -> {
                    try {
                        addObjectif(new ActionEvent());
                        barToggle.setSelected(false);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                });
                pause.play();
            }
        });

        vboxTest.getChildren().addAll(barToggle,bar);
        displayCoachNamefromCoachTable();
        lsTypeObjectif.getItems().addAll(type);
        slTaille.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                taille =(int) slTaille.getValue() ;
                labelTaille.setText("Taille : "+Integer.toString(taille)+"cm");
            }
        });





    }


}
