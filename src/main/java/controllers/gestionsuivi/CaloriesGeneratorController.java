package controllers.gestionsuivi;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CaloriesGeneratorController implements Initializable {

    @FXML
    private TextField CaloriesField;

    @FXML
    private VBox VboxCaloriesPrompted;



    public CaloriesGeneratorController() throws UnsupportedEncodingException {
    }




    @FXML
    private TextField ReciepeField;

    @FXML
    private VBox ReciepeVbox;
    @FXML
    void ReciepePromptingButton(ActionEvent event) throws IOException {

        String apiKey = "tuTgdx5oCYa7c3JlJ7+MeA==5DQ2xVNKpJ7uVtAY"; // Replace with your actual API key
        String query = ReciepeField.getText();
        String encodedQuery = URLEncoder.encode(query, "UTF-8");


        String urlString = "https://api.api-ninjas.com/v1/recipe?query" + encodedQuery;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-Api-Key", apiKey);
        connection.setRequestProperty("accept", "application/json");
        try {
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseStream);
            System.out.println(root.path("fact").asText());
            System.out.println(root);
            ReciepeVbox.getChildren().clear();


            for(JsonNode item : root) {

                String title = item.get("title").asText();
                String ingredients = item.get("ingredients").asText();
                String servings = item.get("servings").asText();
                String instructions = item.get("instructions").asText();

                String labelText = "Title: " + title + "\n" +
                        "Ingredients: " + ingredients + "\n" +
                        "Servings: " + servings + "\n" +
                        "Instructions: " + instructions;

                Label label = new Label(labelText);
                label.getStyleClass().add("label-style");

                ReciepeVbox.getChildren().add(label);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("Root cause: " + cause.toString());
            }


        }

    }


    @FXML
    void generateCalories(ActionEvent event) throws IOException {
        String apiKey = "tuTgdx5oCYa7c3JlJ7+MeA==5DQ2xVNKpJ7uVtAY"; // Replace with your actual API key
        String query = CaloriesField.getText();
        String encodedQuery = URLEncoder.encode(query, "UTF-8");

        String urlString = "https://api.api-ninjas.com/v1/nutrition?query=" + encodedQuery;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-Api-Key", apiKey);
        connection.setRequestProperty("accept", "application/json");

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
                double sugar_g = item.get("sugar_g").asDouble();


                String labelText = "Name: " + name + "\n" +
                        "Calories: " + calories + "\n" +
                        "Serving Size: " + serving_size_g + "g\n" +
                        "Fat Total: " + fat_total_g + "g\n" +
                        "Fat Saturated: " + fat_saturated_g + "g\n" +
                        "Protein: " + protein_g + "g\n" +
                        "Sodium: " + sodium_mg + "mg\n" +
                        "Potassium: " + potassium_mg + "mg\n" +
                        "Cholesterol: " + cholesterol_mg + "mg\n" +
                        "Carbs: " + carbohydrates_total_g + "g\n" +
                        "Sugar: " + sugar_g + "g";

                Label label = new Label(labelText);
                label.getStyleClass().add("label-style");

                VboxCaloriesPrompted.getChildren().add(label);
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
