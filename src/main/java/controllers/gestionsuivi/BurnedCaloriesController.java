package controllers.gestionsuivi;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.RingProgressIndicator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class BurnedCaloriesController implements Initializable {



    @FXML
    private PieChart PieChartBurnedCals;


    @FXML
    private TextField ageField;

    @FXML
    private TextField heightField;

    @FXML
    private VBox toogleVbox;

    @FXML
    private TextField weightField;




    @FXML
    private BarChart<String, Number> barChart;

public void BurnedCaloriesFromActivity(int age,double weight, double height) throws IOException, InterruptedException {

   HttpRequest request = HttpRequest.newBuilder()
           .uri(URI.create("https://fitness-calculator.p.rapidapi.com/bmi?age=" + age + "&weight=" + weight + "&height=" + height))
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
        barChart.getData().clear();
        XYChart.Series<String, Number> bmiSeries = new XYChart.Series<>();
        bmiSeries.setName("YOUR BMI");
        bmiSeries.getData().add(new XYChart.Data<>("Your BMI", bmi));
        XYChart.Series<String, Number> healthyRangeSeries = new XYChart.Series<>();
        healthyRangeSeries.setName("Healthy BMI Range");
        healthyRangeSeries.getData().add(new XYChart.Data<>("Lower Bound", lowerBound));
        healthyRangeSeries.getData().add(new XYChart.Data<>("Upper Bound", upperBound));
        barChart.getData().addAll(bmiSeries, healthyRangeSeries);
    }catch (Exception e) {
        System.out.println("Error: " + e.getMessage());

    }
}
    @FXML
    private Pane Pane2MacrosCalculator;
    @FXML
    private Pane Pane1Bmi;
void  changePanel2(){
    Pane1Bmi.setVisible(false);
    Pane2MacrosCalculator.setVisible(true);
}
    void  changePanel1(){
        Pane1Bmi.setVisible(true);
        Pane2MacrosCalculator.setVisible(false);
    }



    @FXML
    private ChoiceBox<String> ActivityLevl;

    @FXML
    private ChoiceBox<String> GoalCHoice;


public  void MacrosAmout(int age,String gender,double height,double weight,int lvl,String goal) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://fitness-calculator.p.rapidapi.com/macrocalculator?age=" + age + "&gender=" + gender + "&height=" + height + "&weight=" + weight + "&activitylevel=" + lvl + "&goal=" + goal))
            .header("X-RapidAPI-Key", "18ae9b5d60msh7e6247128682805p139e5djsna435480ded7c")
            .header("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();


         try {
             HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
             System.out.println(response.body());
             ObjectMapper objectMapper = new ObjectMapper();
             JsonNode root = objectMapper.readTree(response.body());
             JsonNode item2 = root;
             System.out.println("Item: " + item2);

             JsonNode item =item2.get("data");
             if (item.has("balanced")) {
                 JsonNode balancedNode = item.get("balanced");
                 System.out.println("balancedNode: " + balancedNode);
                 double balancedProtein = balancedNode.get("protein").asDouble();
                 double balancedFat = balancedNode.get("fat").asDouble();
                 double balancedCarbs = balancedNode.get("carbs").asDouble();
                 setBalancedPie(balancedProtein, balancedFat, balancedCarbs);
             }

             if (item.has("lowfat")) {
                 JsonNode lowfatNode = item.get("lowfat");
                 System.out.println("lowfatNode: " + lowfatNode);
                 double lowfatProtein = lowfatNode.get("protein").asDouble();
                 double lowfatFat = lowfatNode.get("fat").asDouble();
                 double lowfatCarbs = lowfatNode.get("carbs").asDouble();
                 setLowFatePie(lowfatProtein, lowfatFat, lowfatCarbs);
             }

             if (item.has("lowcarbs")) {
                 JsonNode lowcarbsNode = item.get("lowcarbs");
                 System.out.println("lowcarbsNode: " + lowcarbsNode);
                 double lowcarbsProtein = lowcarbsNode.get("protein").asDouble();
                 double lowcarbsFat = lowcarbsNode.get("fat").asDouble();
                 double lowcarbsCarbs = lowcarbsNode.get("carbs").asDouble();
                 setlowcarbs(lowcarbsProtein, lowcarbsFat, lowcarbsCarbs);
             }

             if (item.has("highprotein")) {
                 JsonNode highproteinNode = item.get("highprotein");
                 System.out.println("highproteinNode: " + highproteinNode);
                 double highproteinProtein = highproteinNode.get("protein").asDouble();
                 double highproteinFat = highproteinNode.get("fat").asDouble();
                 double highproteinCarbs = highproteinNode.get("carbs").asDouble();
                 sethighprotein(highproteinProtein, highproteinFat, highproteinCarbs);
             }
         }catch (Exception e) {
             System.out.println("Error: " + e.getMessage());

         }
}

   void setBalancedPie(double balancedProtein,double balancedFat,double balancedCarbs){
       ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
               new PieChart.Data("Protein", balancedProtein),
               new PieChart.Data("Fat", balancedFat),
               new PieChart.Data("Carbs", balancedCarbs)
       );
       BalancedPie.setData(pieChartData);

   }

    void setLowFatePie(double lowFatProtein,double lowFatFat,double lowFatCarbs){
        // Create the PieChart data

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Protein", lowFatProtein),
                new PieChart.Data("Fat", lowFatFat),
                new PieChart.Data("Carbs", lowFatCarbs)
        );
        LowFatPie.setData(pieChartData);
    }
    void setlowcarbs(double lowCarbsProtein,double lowCarbsFat,double lowCarbsCarbs){
        // Create the PieChart data

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Protein", lowCarbsProtein),
                new PieChart.Data("Fat", lowCarbsFat),
                new PieChart.Data("Carbs", lowCarbsCarbs)
        );
        LowCarPie.setData(pieChartData);

    }
    void sethighprotein(double highProteinProtein,double highProteinFat,double highProteinCarbs){
        // Create the PieChart data

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Protein", highProteinProtein),
                new PieChart.Data("Fat", highProteinFat),
                new PieChart.Data("Carbs", highProteinCarbs)
        );
        HightProteinPie.setData(pieChartData);
    }



@FXML
private VBox LeftVbox;
   /* @FXML
    void DIalogWinodw(MouseEvent event) {
        LeftVbox.setTranslateX(-LeftVbox.getWidth());
        LeftVbox.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), LeftVbox);
        transition.setToX(0);
        transition.play();
    }
*/

    @FXML
    private PieChart HightProteinPie;
    @FXML
    private PieChart LowFatPie;

    @FXML
    private PieChart LowCarPie;

    @FXML
    private PieChart BalancedPie;

private   String goal ;
private String gender;

    @FXML
    private RadioButton FemaleRadioButton;
    @FXML
    private RadioButton MaleRadioButton;
    @FXML
    private VBox MacrosToogleVbox;
    @FXML
    private TextField ageField2;
    @FXML
    private TextField weightField2;
    @FXML
    private TextField heightField2;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var indicatorToggle = new ToggleButton("Start");
        indicatorToggle.textProperty().bind(Bindings.createStringBinding(
                () -> indicatorToggle.isSelected() ? "Stop" : "Start",
                indicatorToggle.selectedProperty()
        ));
        var indicator = new ProgressIndicator(0);
        indicator.setMinSize(40, 40);
        indicator.progressProperty().bind(Bindings.createDoubleBinding(
                () -> indicatorToggle.isSelected() ? -1d : 0d,
                indicatorToggle.selectedProperty()
        ));
        indicatorToggle.setOnAction(eventt -> {
            if (indicatorToggle.isSelected()) {

                int age = Integer.parseInt(ageField.getText());
                double weight = Double.parseDouble(weightField.getText());
                double height = Double.parseDouble(heightField.getText());

                try {
                    BurnedCaloriesFromActivity(age,weight,height);

                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(e -> indicatorToggle.setSelected(false));
                delay.play();

            } else {
                System.out.println("no slected");
            }
        });

        toogleVbox.getChildren().addAll(indicatorToggle,indicator);



        GoalCHoice.getItems().add("maintain weight");
        GoalCHoice.getItems().add("Mild weight loss");
        GoalCHoice.getItems().add("Weight loss");
        GoalCHoice.getItems().add("Extreme weight loss");
        GoalCHoice.getItems().add("Mild weight gain");
        GoalCHoice.getItems().add("Weight gain");
        GoalCHoice.getItems().add("Extreme weight gain");
        GoalCHoice.getItems().add("Extreme weight gain");
        GoalCHoice.setValue("maintain weight");
        ActivityLevl.getItems().add("BMR");
        ActivityLevl.getItems().add("little or no exercise");
        ActivityLevl.getItems().add("Exercise 1-3 times/week");
        ActivityLevl.getItems().add("Exercise 4-5 times/week");
        ActivityLevl.getItems().add("Daily exercise or intense exercise 3-4 times/week");
        ActivityLevl.getItems().add("Intense exercise 6-7 times/week");
        ActivityLevl.getItems().add("Very intense exercise daily, or physical job");
        ActivityLevl.setValue("BMR");




        var indicatorToggle2 = new ToggleButton("Start");
        indicatorToggle2.textProperty().bind(Bindings.createStringBinding(
                () -> indicatorToggle2.isSelected() ? "Stop" : "Start",
                indicatorToggle2.selectedProperty()
        ));
        var indicator2 = new ProgressIndicator(0);
        indicator2.setMinSize(40, 40);
        indicator2.progressProperty().bind(Bindings.createDoubleBinding(
                () -> indicatorToggle2.isSelected() ? -1d : 0d,
                indicatorToggle2.selectedProperty()
        ));
        indicatorToggle2.setOnAction(eventt -> {
            if (indicatorToggle2.isSelected()) {


                if (FemaleRadioButton.isSelected()) {
                  gender ="female";
                }
                    if (MaleRadioButton.isSelected()) {
                         gender ="male";
                    }


                    int activiteLevel = 1;
                int age = Integer.parseInt(ageField2.getText());
                double weight = Double.parseDouble(weightField2.getText());
                double height = Double.parseDouble(heightField2.getText());
                if (ActivityLevl.getSelectionModel().getSelectedItem().equals("BMR")) {
                    activiteLevel = 1;
                }

                    if (ActivityLevl.getSelectionModel().getSelectedItem().equals("little or no exercise")) {
                    activiteLevel  = 2 ;
                }
                 if (ActivityLevl.getSelectionModel().getSelectedItem().equals("Exercise 1-3 times/week")) {
                    activiteLevel  = 3 ;
                }

                 if (ActivityLevl.getSelectionModel().getSelectedItem().equals("Exercise 4-5 times/week")) {

                    activiteLevel  = 4 ;
                }
                 if (ActivityLevl.getSelectionModel().getSelectedItem().equals("Daily exercise or intense exercise 3-4 times/week")) {

                    activiteLevel  = 5 ;
                }
                 if (ActivityLevl.getSelectionModel().getSelectedItem().equals("Intense exercise 6-7 times/week")) {
                    activiteLevel  = 6 ;
                }
                 if (ActivityLevl.getSelectionModel().getSelectedItem().equals("IVery intense exercise daily, or physical job")) {
                    activiteLevel  = 7 ;
                }


                if (GoalCHoice.getSelectionModel().getSelectedItem().equals("maintain weight")) {
                    goal = "maintain";
                }
                 if (GoalCHoice.getSelectionModel().getSelectedItem().equals("Mild weight loss")) {
                    goal = "mildlose";
                }

                if (GoalCHoice.getSelectionModel().getSelectedItem().equals("Weight loss")) {
                    goal = "weightlose";
                }
                if (GoalCHoice.getSelectionModel().getSelectedItem().equals("Extreme weight loss")) {
                    goal = "extremelose";
                }
                if (GoalCHoice.getSelectionModel().getSelectedItem().equals("Mild weight gain")) {
                    goal = "mildgain";
                }
                if (GoalCHoice.getSelectionModel().getSelectedItem().equals("Weight gain")) {
                    goal = "weightgain";
                }
                if (GoalCHoice.getSelectionModel().getSelectedItem().equals("Extreme weight gain")) {
                    goal = "extremegain";
                }




                try {
                    System.out.println(age);
                    System.out.println(gender);
                    System.out.println(height);
                    System.out.println(weight);
                    System.out.println(activiteLevel);
                    System.out.println(goal);

                    MacrosAmout(age,gender,height,weight,activiteLevel,goal);

                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(e -> indicatorToggle2.setSelected(false));
                delay.play();

            } else {
                System.out.println("no slected");
            }
        });

        MacrosToogleVbox.getChildren().addAll(indicatorToggle2,indicator2);


    }
}
