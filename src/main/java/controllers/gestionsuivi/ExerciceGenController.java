package controllers.gestionsuivi;

import com.diffplug.spotless.maven.json.Gson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExerciceGenController {


    private  String selectedMuscle = "";
    private  String selectedEquipment = "";
    @FXML
    private VBox ExercicesVbox;

    @FXML
    void AbdoPan(MouseEvent event) throws IOException {
        selectedMuscle = "Abs";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void BackPan(MouseEvent event) throws IOException {
        selectedMuscle = "Back";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }
    @FXML
    void BicepsPan(MouseEvent event) throws IOException {
        selectedMuscle = "Biceps";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void CalvesPan(MouseEvent event) throws IOException {
        selectedMuscle = "Calves";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void ChestPan(MouseEvent event) throws IOException {
        selectedMuscle = "Chest";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void ShouldersPan(MouseEvent event) throws IOException {
        selectedMuscle = "Shoulders";
        exercicessGenerator(selectedMuscle,selectedEquipment);

    }

    @FXML
    void GlutesPan(MouseEvent event) throws IOException {
        selectedMuscle = "Glutes";
        exercicessGenerator(selectedMuscle,selectedEquipment);

    }

    @FXML
    void HamstringPan(MouseEvent event) throws IOException {
        selectedMuscle = "Hamstring";
        exercicessGenerator(selectedMuscle,selectedEquipment);

    }

    @FXML
    void LatsPan(MouseEvent event) throws IOException {
        selectedMuscle = "Lats";
        exercicessGenerator(selectedMuscle,selectedEquipment);

    }

    @FXML
    void QuadsPan(MouseEvent event) throws IOException {
        selectedMuscle = "Quadriceps";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void TricepsPan(MouseEvent event) throws IOException {
        selectedMuscle = "Triceps";
        exercicessGenerator(selectedMuscle,selectedEquipment);

    }

    @FXML
    void TrapeziusPan(MouseEvent event) throws IOException {
        selectedMuscle = "Trapezius";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }
    @FXML
    void BarbelPane(MouseEvent event) throws IOException {
        String selectedEquipment ="Barbell";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }


    @FXML
    void EzBarPane(MouseEvent event) throws IOException {
        String selectedEquipment ="EZ-bar";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void BenchPanel(MouseEvent event) throws IOException {
        String selectedEquipment ="Bench";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void PressPan(MouseEvent event) throws IOException {
        String selectedEquipment ="Chest press machine";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void kettlebellPan(MouseEvent event) throws IOException {
        String selectedEquipment ="kettlebelle";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

    @FXML
    void DumbellsPan(MouseEvent event) throws IOException {
        String selectedEquipment ="Dumbbells";
        exercicessGenerator(selectedMuscle,selectedEquipment);
    }

@FXML
    private ImageView imageWorkout;


    public void exercicessGenerator(String muscles,String equipment) throws IOException {
        if (muscles.isEmpty()) {
            System.out.println("Please select a muscle.");
            return;
        }

        ExercicesVbox.getChildren().clear();
        String uri;
        if (equipment.isEmpty()) {
            uri = "https://work-out-api1.p.rapidapi.com/search?Muscles=" + muscles;
        } else {
            uri = "https://work-out-api1.p.rapidapi.com/search?Muscles=" + muscles + "&Equipment=" + equipment;
        }


        ExercicesVbox.getChildren().clear();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("X-RapidAPI-Key", "18ae9b5d60msh7e6247128682805p139e5djsna435480ded7c")
                .header("X-RapidAPI-Host", "work-out-api1.p.rapidapi.com")
                .build();


        imageWorkout.setVisible(false);
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
           System.out.println(response.body());
            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            for (JsonNode exerciseNode : jsonNode) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/ExerciesFx.fxml"));
                Node node = loader.load();
                String muscless = exerciseNode.get("Muscles").asText();
                String workout = exerciseNode.get("WorkOut").asText();
                String intensityLevel = exerciseNode.get("Beginner Sets").asText();
                String IntermediateLevel = exerciseNode.get("Intermediate Sets").asText();
                String ExpertLevel = exerciseNode.get("Expert Sets").asText();
                 String Equipment =exerciseNode.get("Equipment").asText();
                String Explaination =exerciseNode.get("Explaination").asText();
                String Video =exerciseNode.get("Video").asText();
                ExericesController exericesController = loader.getController();
                exericesController.FillFields(workout,Equipment,intensityLevel,IntermediateLevel,ExpertLevel,Explaination,Video);

                ExercicesVbox.getChildren().add(node);
                System.out.println("Muscles: " + muscless);

            }


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }



        //**** API NINJA*******************
       /* String apiKey = "tuTgdx5oCYa7c3JlJ7+MeA==5DQ2xVNKpJ7uVtAY";
        String urlStr = "https://api.api-ninjas.com/v1/exercises?muscle=" + queryy;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("X-Api-Key", apiKey); // Set the API key header
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseStream);*/



        /*for (JsonNode item : root){

            String name = item.get("name").asText();
            String equipment = item.get("equipment").asText();

            String labelText = "Name Of the exercice : " + name + "\n" +
                    "Equipement: " + equipment ;

            Text text = new Text(labelText + "\n\n");
            text.getStyleClass().add("text");
            TextFlow textFlow = new TextFlow(text);
            textFlow.setMaxWidth(ExercicesVbox.getWidth());
            ExercicesVbox.getChildren().add(textFlow);


        }*/




    }



}