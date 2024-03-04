package controllers.gestionsuivi;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.RingProgressIndicator;
import atlantafx.base.theme.Styles;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import services.gestionSuivi.ObjectifService;
import services.gestionSuivi.PlanningService;

import javax.sql.DataSource;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ObjectifAddPlanningOnLeft implements Initializable {

    @FXML
    private TextArea DietLabel;


    @FXML
    private Pane AddingPan;


    @FXML
    private TextArea exerciceLabel;

    @FXML
    private BarChart<String, Number> CahrtBmi;



    private  static int id_objectifStatic;
    void setIdObjectif(int id_objectif){
        id_objectifStatic = id_objectif;
    }

   public void getTheBmiCahrt(int agee, double weight,double height){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://fitness-calculator.p.rapidapi.com/bmi?age=" + agee + "&weight=" + weight + "&height=" + height))
                .header("X-RapidAPI-Key", "18ae9b5d60msh7e6247128682805p139e5djsna435480ded7c")
                .header("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = jsonNode.get("data");
            double bmi = dataNode.get("bmi").asDouble();
            String health = dataNode.get("health").asText();
            String healthy_bmi_range = dataNode.get("healthy_bmi_range").asText();
            String[] bounds = healthy_bmi_range.split(" - ");
            double lowerBound = Double.parseDouble(bounds[0]);
            double upperBound = Double.parseDouble(bounds[1]);
            CahrtBmi.getData().clear();
            XYChart.Series<String, Number> bmiSeries = new XYChart.Series<>();
            bmiSeries.setName("YOUR BMI");
            bmiSeries.getData().add(new XYChart.Data<>("Client BMI", bmi));
            XYChart.Series<String, Number> healthyRangeSeries = new XYChart.Series<>();
            healthyRangeSeries.setName("Healthy BMI Range");
            healthyRangeSeries.getData().add(new XYChart.Data<>("Lower Bound", lowerBound));
            healthyRangeSeries.getData().add(new XYChart.Data<>("Upper Bound", upperBound));
            CahrtBmi.getData().addAll(bmiSeries, healthyRangeSeries);
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }

    }

    @FXML
    private HBox goBackHbix;

    private  static  PlanningController planningController ;
    public void setPallningControler(PlanningController planningController) {
        ObjectifAddPlanningOnLeft.planningController = planningController;
    }
    @FXML
    private Button loadPdfExercice;

    @FXML
    private Button loadPdfDiet;


    @FXML
    private ToggleButton  ToogleButton;

    @FXML
    private ProgressBar progrssBar;


    @FXML
    public void loadPdfExercice(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            exerciceLabel.setText(filePath);
            String fileName = selectedFile.getName();
            System.out.println("Selected PDF file: " + fileName);
        }
    }

    @FXML
    void loadPdfDiet(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            DietLabel.setText(filePath);
            String fileName = selectedFile.getName();
            System.out.println("Selected PDF file: " + fileName);
        }
    }




    public  void AddPlanning() {
        if (id_objectifStatic != 0){
            String urlPlan =exerciceLabel.getText();
            String urlDiet =DietLabel.getText();
            int id_obj = id_objectifStatic;
            System.out.println(id_obj);
            PlanningService querry = new PlanningService();
            Planning planning = new Planning(id_obj,urlPlan,urlDiet);
            try {
              querry.add(planning);


            }catch (SQLException e){
                throw new RuntimeException(e);

            }
        }
    }

    public void updatePlanning(){
        if (id_objectifStatic != 0){
            String urlPlan =exerciceLabel.getText();
            String urlDiet =DietLabel.getText();
            int id_obj = id_objectifStatic;
            System.out.println(id_obj);
            PlanningService querry = new PlanningService();
            Planning planning = new Planning(id_obj,urlPlan,urlDiet);
            try {
                querry.update(planning);


            }catch (SQLException e){
                throw new RuntimeException(e);

            }
        }

    }

    @FXML
    private HBox WarningHbox;


    public void warning () {
        var warning = new Message(
                "Warning",
                Faker.instance().expression("you can't add while there are empty fields fill all of them "),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)

        );
        warning.getStyleClass().add(Styles.WARNING);
        planningController.setMessageWarning(warning);


    }
    @FXML
    private ToggleButton ToogleUpdate;
    @FXML
    private ProgressBar progrssBar2;
    public void setDataFieldForTheUpdate(Planning planning){
        exerciceLabel.setText(planning.getTrainingProg());
        DietLabel.setText(planning.getFoodProg());
        ToogleButton.setVisible(false);
        ToogleUpdate.setVisible(true);
        progrssBar.setVisible(false);
        progrssBar2.setVisible(true);
    }







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var normalBtn = new Button(null, new FontAwesomeIconView(FontAwesomeIcon.BACKWARD));
        normalBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE);
        goBackHbix.getChildren().add(normalBtn);

        normalBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(planningController != null){
                    planningController.makePaneAddingInvisble();
                }
                else {
                    System.out.println("problem");
                }
            }
        });

        loadPdfExercice.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        loadPdfExercice.setMnemonicParsing(true);

        loadPdfDiet.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        loadPdfDiet.setMnemonicParsing(true);

        ToogleButton.textProperty().bind(Bindings.createStringBinding(
                () -> ToogleButton.isSelected() ? "Stop" : "Add Planning",
                ToogleButton.selectedProperty()
        ));


        progrssBar.progressProperty().bind(Bindings.createDoubleBinding(
                () -> ToogleButton.isSelected() ? -1d : 0d,
                ToogleButton.selectedProperty()
        ));
        ToogleButton.setOnAction(event -> {
            if (ToogleButton.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    if (exerciceLabel.getText().isEmpty() || DietLabel.getText().isEmpty()) {
                        warning();
                        ToogleButton.setSelected(false);

                    } else {
                        progrssBar.progressProperty().bind(Bindings.createDoubleBinding(
                                () -> ToogleButton.isSelected() ? -1d : 0d,
                                ToogleButton.selectedProperty()
                        ));
                        AddPlanning();
                        planningController.displayListeObjectif();
                        planningController.displayListePlanning();
                        planningController.makePaneAddInvisible();
                        planningController.makePaneAddingInvisble();
                      //  endEmailWithPDF("benammar.fares@esprit.tn", "String subject", "String body", "aaa") ;
                        success();
                        ToogleButton.setSelected(false);
                    }
                });
                pause.play();

            }

        });

        //*******************************************Update Planning **************
        ToogleUpdate.textProperty().bind(Bindings.createStringBinding(
                () -> ToogleUpdate.isSelected() ? "Stop" : "Update Planning",
                ToogleUpdate.selectedProperty()
        ));


        progrssBar2.progressProperty().bind(Bindings.createDoubleBinding(
                () -> ToogleUpdate.isSelected() ? -1d : 0d,
                ToogleUpdate.selectedProperty()
        ));
        ToogleUpdate.setOnAction(event -> {
            if (ToogleUpdate.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    if (exerciceLabel.getText().isEmpty() || DietLabel.getText().isEmpty()) {
                        warning();
                        ToogleUpdate.setSelected(false);

                    } else {
                        progrssBar2.progressProperty().bind(Bindings.createDoubleBinding(
                                () -> ToogleUpdate.isSelected() ? -1d : 0d,
                                ToogleUpdate.selectedProperty()
                        ));
                        updatePlanning();
                        planningController.displayListeObjectif();
                        planningController.displayListePlanning();
                        updatedNotif();
                        planningController.displayListeObjectif();
                        planningController.displayListePlanning();
                        planningController.makePaneAddInvisible();
                        planningController.makePaneAddingInvisble();
                        ToogleUpdate.setSelected(false);
                    }
                });
                pause.play();

            }

        });
    }

    public void success (){

        var success = new Message(
                "Info",
                "well done you've added the plan successfully for the client",
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
        success.getStyleClass().add(Styles.ACCENT);
        planningController.setMessageSuccess(success);
    }

    public void updatedNotif (){

        var success = new Message(
                "Info",
                "well done you've modified the plan successfully for the client",
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
        success.getStyleClass().add(Styles.ACCENT);
        planningController.setMessage(success);

    }
}
