package controllers.gestionsuivi;


import atlantafx.base.controls.ToggleSwitch;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CaloriesGeneratorController implements Initializable {

    @FXML
    private TextField CaloriesField;

    @FXML
    private VBox VboxCaloriesPrompted;



    public CaloriesGeneratorController() throws UnsupportedEncodingException {
    }

    private    int testVariable =0 ;


    @FXML
    private TextField ReciepeField;

    @FXML
    private VBox ReciepeVbox;


    @FXML
    private Label LabelRecipe;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;







    public String translationApi(String query) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://translate281.p.rapidapi.com/"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", "5d8f909459mshfb1291f1ed93992p10402ajsn75655f4af883")
                .header("X-RapidAPI-Host", "translate281.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("text=" + query + "&from=auto&to=fr"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        // Extract the translated response from the JSON data
        String jsonResponse = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
      /*   System.out.println("Fel translate function");
        return jsonNode.asText();/*/

        String translatedText = jsonNode.get("Ingredients").asText(); // Replace "translatedText" with the actual field name
        byte[] decodedBytes = translatedText.getBytes(StandardCharsets.ISO_8859_1);
        String correctEncodingString = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println("In translate function");
        return correctEncodingString;
    }


    @FXML
    void ReciepePromptingButton(ActionEvent event) throws IOException, InterruptedException {
        String query = ReciepeField.getText();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://food-recipes-with-images.p.rapidapi.com/?q=" + query))
                .header("X-RapidAPI-Key", "18ae9b5d60msh7e6247128682805p139e5djsna435480ded7c")
                .header("X-RapidAPI-Host", "food-recipes-with-images.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        image1.setVisible(false);

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode dataArray = jsonNode.get("d");
            ReciepeVbox.getChildren().clear();
            int conter = 0;
            for (JsonNode item : dataArray) {

                if (conter >=12){
                    break;
                }
                String Title = item.get("Title").asText();
                JsonNode Ingredients = item.get("Ingredients");
                String Instructions = item.get("Instructions").asText();
                String Image = item.get("Image").asText();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/recette.fxml"));
                Node node = loader.load();
                RecetteController recetteController = loader.getController();
                    recetteController.fiellFields(Title, Ingredients, Instructions, Image);
                ReciepeVbox.getChildren().add(node);

                conter++;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }

    }



    @FXML
    void generateCalories(ActionEvent event) throws IOException {
        String apiKey = "tuTgdx5oCYa7c3JlJ7+MeA==5DQ2xVNKpJ7uVtAY";
        String query = CaloriesField.getText();
        String encodedQuery = URLEncoder.encode(query, "UTF-8");

        String urlString = "https://api.api-ninjas.com/v1/nutrition?query=" + encodedQuery;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-Api-Key", apiKey);
        connection.setRequestProperty("accept", "application/json");
        image2.setVisible(false);
        try {
            InputStream responseStream = connection.getInputStream();

            com.fasterxml.jackson.databind.ObjectMapper mapper1 = new com.fasterxml.jackson.databind.ObjectMapper();
            JsonNode root = mapper1.readTree(responseStream);
            System.out.println(root.path("fact").asText());
            System.out.println(root);

            Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
            String responseBody = scanner.hasNext() ? scanner.next() : "";
            System.out.println(responseBody);

            VboxCaloriesPrompted.getChildren().clear();
            for(JsonNode item : root) {

                String name = item.get("name").asText();
                double calories = item.get("calories").asDouble();
                double serving_size_g = item.get("serving_size_g").asDouble();
                double fat_total_g = item.get("fat_total_g").asDouble();
                double fat_saturated_g = item.get("fat_saturated_g").asDouble();
                double protein_g = item.get("protein_g").asDouble();
                double sodium_mg = item.get("sodium_mg").asDouble();
                double potassium_mg = item.get("potassium_mg").asDouble();
                double cholesterol_mg = item.get("cholesterol_mg").asDouble();
                double carbohydrates_total_g = item.get("carbohydrates_total_g").asDouble();
                double fiber = item.get("fiber_g").asDouble();
                double sugar_g = item.get("sugar_g").asDouble();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/MacrosChat.fxml"));
                Node node = loader.load();
                MacrosController macrosController = loader.getController();
                macrosController.fillMacros(name,calories,serving_size_g,fat_total_g,fat_saturated_g,protein_g,sodium_mg,potassium_mg,cholesterol_mg,carbohydrates_total_g,fiber,sugar_g);
                VboxCaloriesPrompted.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("Root cause: " + cause.toString());
            }
        }

    }










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }
}
