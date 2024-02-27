package controllers.gestionsuivi;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.RingProgressIndicator;
import atlantafx.base.theme.Styles;
import com.github.javafaker.Faker;
import controllers.gestionuser.GlobalVar;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
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
import javafx.scene.layout.Pane;
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
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddObjectifController implements Initializable {



    @FXML
    private TextField ageField;

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



    int taille;

    private String[] type = {"Par Defaut","Version ++"};


    @FXML
    private ToggleButton barToggle;


    private  Objectif objectif2 ;



    @FXML
    private Label TtileLabel;




    private static Stage stage ;
    private Objectif obj;


    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        AddObjectifController.stage = stage;
    }




    @FXML
void updateObjectif(ActionEvent event) {




        String poidsObjectifText = tfPoidsObjectif.getText();

        try {
            float poidsObjectiff = Float.parseFloat(poidsObjectifText);
            if (poidsObjectiff < 30 || poidsObjectiff > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }

        String poidsObjectifText2 = tfPoidsActuelle.getText();

        try {
            float poidsObjectifAct = Float.parseFloat(poidsObjectifText2);
            if (poidsObjectifAct < 30 || poidsObjectifAct > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }
       /* double tailleValue = slTaille.getValue();
        if (tailleValue < 1 || tailleValue > 2.5) {
            showAlertHeight();
            return;
        }*/

        LocalDate currentDatee = LocalDate.now();
        LocalDate selectedDate = dpDureeObjectif.getValue();
        if (selectedDate == null) {
            showAlertDate();
            return;
        }
        if (selectedDate.isBefore(currentDatee.plusDays(30))) {
            showAlertDate();
            return;
        }

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
                int userid= GlobalVar.getUser().getId();;

                ObjectifService querry = new ObjectifService();
                int coachId = querry.getCoachIdByName(name_coach_selected); // Retrieve the coach's ID
                Objectif obj2 = new Objectif(obj.getId_objectif(),userid,poidsObjectif,sqlDureeObjectifValue,poidsActuel,taille1,alergie,typeObj,coachId) ;
                System.out.println("fel update fn");
             //   System.out.println(this.objectif2.getId_objectif());
                obj2.setDateD(sqlDureeNow);
                try {
                    System.out.printf("passed the try");
                    querry.update(obj2);
                    updatedNotif();
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
                "Info", Faker.instance().expression("Gym Plus vous informe que l'ajout est fait avec succées"),
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
    public void  showAlertHeight(){
        var danger = new Message(
                "Danger", Faker.instance().expression(" Please Check that the height must be between 100 and 250 metre  "),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)
        );
        danger.getStyleClass().add(Styles.DANGER);
        HboxAddNotif.getChildren().add(danger);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

   private  static  ObjectifController objectifController;
    public void setObjectifController(ObjectifController objectifController) {
        AddObjectifController.objectifController = objectifController;
    }


public void danger(){
    var danger = new Message(
            "Danger", Faker.instance().expression(" Please Check All fields must be Filled with data ,no empty fields "),
            new FontAwesomeIconView(FontAwesomeIcon.WARNING)
    );
    danger.getStyleClass().add(Styles.DANGER);
    HboxAddNotif.getChildren().add(danger);
    Duration duration = Duration.seconds(3);
    KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
    Timeline timeline = new Timeline(keyFrame);
    timeline.play();
}
    public void showAlert(){
        var danger = new Message(
                "Danger", Faker.instance().expression("Weight got to between 30kg and 180kg and check the type "),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)
        );
        danger.getStyleClass().add(Styles.DANGER);
        HboxAddNotif.getChildren().add(danger);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    public void showAlertDate(){
        var danger = new Message(
                "Danger", Faker.instance().expression("Date Warning , make sure the Date you picked is more than 1 month"),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)
        );
        danger.getStyleClass().add(Styles.DANGER);
        HboxAddNotif.getChildren().add(danger);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }



    @FXML
    void addObjectif(ActionEvent event) throws SQLException {

        if (tfPoidsObjectif.getText().isEmpty() || tfPoidsActuelle.getText().isEmpty()
                ||  taAlergie.getText().isEmpty() ||  lsTypeObjectif.getSelectionModel().isEmpty()
                || lsCoachName.getSelectionModel().isEmpty()) {
            danger();
            return;
        }


        String poidsObjectifText = tfPoidsObjectif.getText();

        try {
            float poidsObjectiff = Float.parseFloat(poidsObjectifText);
            if (poidsObjectiff < 30 || poidsObjectiff > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }

        String poidsObjectifText2 = tfPoidsActuelle.getText();

        try {
            float poidsObjectifAct = Float.parseFloat(poidsObjectifText2);
            if (poidsObjectifAct < 30 || poidsObjectifAct > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }
      /*  double tailleValue = slTaille.getValue();
        if (tailleValue < 1 || tailleValue > 2.5) {
            showAlertHeight();
            return;
        }*/

        LocalDate currentDatee = LocalDate.now();
        LocalDate selectedDate = dpDureeObjectif.getValue();
        if (selectedDate == null) {
            showAlertDate();
            return;
        }
        if (selectedDate.isBefore(currentDatee.plusDays(30))) {
            showAlertDate();
            return;
        }
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
                    int userid= GlobalVar.getUser().getId();
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
        String  selectAllData ="SELECT * FROM user WHERE role='staff'";
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






    @FXML
    private ProgressBar ProgrssBarModif;

    @FXML
    private ToggleButton ToogleModif;





     void sendingDataToForm(Objectif obj) {
         try {
             ProgrssBarAdd.setVisible(false);
             ToogleAdd.setVisible(false);
             ProgrssBarModif.setVisible(true);
             ToogleModif.setVisible(true);
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

         }catch (Exception e){
             e.printStackTrace();
         }
     }


    public void updatedNotif (){

        var success = new Message(
                "Info",
                "well done you've modified the plan successfully for the client",
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
        success.getStyleClass().add(Styles.ACCENT);
        HboxAddNotif.getChildren().add(success);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    public void success (){

        var success = new Message(
                "Info",
                "well done you've added the plan successfully for the client",
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
        success.getStyleClass().add(Styles.ACCENT);
        HboxAddNotif.getChildren().add(success);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    @FXML
    private ProgressBar ProgrssBarAdd;

    @FXML
    private ToggleButton ToogleAdd;


    void setToogleAddVisible(){
        ProgrssBarAdd.setVisible(true);
        ToogleAdd.setVisible(true);
        ProgrssBarModif.setVisible(false);
        ToogleModif.setVisible(false);
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dpDureeObjectif.getEditor().setDisable(true);
        //Adding Toogle
        ToogleAdd.textProperty().bind(Bindings.createStringBinding(
                () -> ToogleAdd.isSelected() ? "Adding..." : "Adding Objectif",
                ToogleAdd.selectedProperty()
        ));


        ProgrssBarAdd.progressProperty().bind(Bindings.createDoubleBinding(
                () -> ToogleAdd.isSelected() ? -1d : 0d,
                ToogleAdd.selectedProperty()
        ));
        ToogleAdd.setOnAction(event -> {
            if (ToogleAdd.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {

                    try {
                        addObjectif(new ActionEvent());
                        ToogleAdd.setSelected(false);

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }


                });
                pause.play();

            }

        });




        //Modiff ToogleButton
        ToogleModif.textProperty().bind(Bindings.createStringBinding(
                () -> ToogleModif.isSelected() ? "Updating..." : "Update Planning",
                ToogleModif.selectedProperty()
        ));


        ProgrssBarModif.progressProperty().bind(Bindings.createDoubleBinding(
                () -> ToogleModif.isSelected() ? -1d : 0d,
                ToogleModif.selectedProperty()
        ));
        ToogleModif.setOnAction(event -> {
            if (ToogleModif.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {

                    updateObjectif(new ActionEvent());
                    ToogleModif.setSelected(false);

                });
                pause.play();

            }

        });


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
