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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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


    public void fiellFields(String Title, JsonNode Ingredients, String Instructions, String Image) throws UnsupportedEncodingException {

        TitileFood.setText(Title);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> ingredientsMap = mapper.convertValue(Ingredients, new TypeReference<Map<String, String>>() {});
        for (Map.Entry<String, String> entry : ingredientsMap.entrySet()) {
            String ingredientStep = entry.getValue();
            Label ingredientLabel = new Label(ingredientStep);
            ingredientsVbox.getChildren().add(ingredientLabel);
        }

        instructionFoodLabel.setText(Instructions);
        loadImageFromURL(Image);

    }



}
