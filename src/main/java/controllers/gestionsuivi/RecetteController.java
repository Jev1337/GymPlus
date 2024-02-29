package controllers.gestionsuivi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class RecetteController {
    @FXML
    private ImageView ImageFood;

    @FXML
    private Label TitileFood;

    @FXML
    private Label instructionFoodLabel;



    public void loadImageFromURL(String imageURL) throws UnsupportedEncodingException {
        String formattedURL = imageURL.replace("\\/", "");
        String formattedURL2 = "https:" + formattedURL;
        Image image = new Image(formattedURL2);

        ImageFood.setImage(image);
        Circle clip = new Circle(ImageFood.getFitWidth()/2, ImageFood.getFitHeight()/2, ImageFood.getFitWidth()/2);
        ImageFood.setClip(clip);
        ImageFood.setPreserveRatio(false);

    }

    @FXML
    private VBox ingredientsVbox;


    public void fiellFields(String Title, JsonNode Ingredients, String Instructions, String Image) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> ingredientsMap = mapper.convertValue(Ingredients, new TypeReference<Map<String, String>>() {});

            TitileFood.setText(Title);
            for (Map.Entry<String, String> entry : ingredientsMap.entrySet()) {
                String ingredientStep = entry.getValue();
                Label ingredientLabel = new Label(ingredientStep);
                ingredientsVbox.getChildren().add(ingredientLabel);
            }
            instructionFoodLabel.setText(Instructions);

        loadImageFromURL(Image);

    }

    public void fiellFieldsFR(String Title, JsonNode Ingredients, String Instructions, String Image) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> ingredientsMap = mapper.convertValue(Ingredients, new TypeReference<Map<String, String>>() {});
        TitileFood.setText(Title);
        if (ingredientsMap != null) { // Check if ingredientsMap is not null
            for (Map.Entry<String, String> entry : ingredientsMap.entrySet()) {
                String ingredientStep = entry.getValue();
                Label ingredientLabel = new Label(ingredientStep);
                ingredientsVbox.getChildren().add(ingredientLabel);
            }
        } else {
            System.out.println("Ingredients is null or not a JSON object");
        }
        instructionFoodLabel.setText(Instructions);
        loadImageFromURL(Image);
    }



}
