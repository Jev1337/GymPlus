package controllers.gestionsuivi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatControler  {


    @FXML
    private TextArea textFieldPrompt;

    @FXML
    private Button SearchButton;

    @FXML
    private VBox PromptingVbox;



    @FXML
    void chatGPT(ActionEvent event) {
         String message = textFieldPrompt.getText();
                               System.out.println("generating .....");
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            String body = "{\n" +
                    "  \"model\": \"" + model + "\",\n" +
                    "  \"messages\": [\n" +
                    "    {\n" +
                    "      \"role\": \"system\",\n" +
                    "      \"content\": \"You are a helpful assistant.\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"role\": \"user\",\n" +
                    "      \"content\": \"" + message + "\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the response JSON to extract the assistant's reply
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray choicesArray = jsonObject.getJSONArray("choices");
            if (choicesArray != null && choicesArray.length() > 0) {
                JSONObject choiceObject = choicesArray.getJSONObject(0);
                System.out.println(choiceObject.getJSONObject("message").getString("content"));
                System.out.println("*******************************************");

                String content = choiceObject.getJSONObject("message").getString("content");
                Label labelNode = new Label(content);
                labelNode.getStyleClass().add("label-style");
                PromptingVbox.getChildren().clear();
                PromptingVbox.getChildren().add(labelNode);

                return ;
            }

            // If the expected field is not found, handle the error
            throw new RuntimeException("Failed to extract response content.");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }



}
