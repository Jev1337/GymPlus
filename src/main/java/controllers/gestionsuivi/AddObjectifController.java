package controllers.gestionsuivi;

import entities.gestionSuivi.Objectif;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.gestionSuivi.ObjectifService;
import utils.MyDatabase;

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
    private Button btnSave;

    @FXML
    private DatePicker dpDureeObjectif;

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


    private static Stage stage ;


    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        AddObjectifController.stage = stage;
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
                    } catch (SQLException e) {
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

    int taille;
    @FXML
    private Text labelTaille;
    private String[] type = {"Par Defaut","Version ++"};

    @FXML
    private VBox vboxTest;
    @FXML
    private ToggleButton barToggle;

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
