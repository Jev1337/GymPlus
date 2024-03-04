package controllers.gestionsuivi;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.beust.ah.A;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ChatControler implements Initializable{


    @FXML
    private TextArea textFieldPrompt;

    @FXML
    private Button SearchButton;

    @FXML
    private Button vocalSendButton;
    @FXML
    private VBox PromptingVbox;


    List<String> chatHistory = new ArrayList<>();

    @FXML
    void chatGPT(ActionEvent event) {
        String message = textFieldPrompt.getText();
        textFieldPrompt.setText("");
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
                // Add the current question and response to the chat history
                chatHistory.add("User: " + message);
                chatHistory.add("Assistant: " + content);
                updateChatHistoryUI();


                return;
            }

            // If the expected field is not found, handle the error
            throw new RuntimeException("Failed to extract response content.");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }
    private static int latestResponseIndex = 0; // Track the latest response checked

    private void updateChatHistoryUI() {
        PromptingVbox.getChildren().clear();

        int chatHistorySize = chatHistory.size(); // Get the current size of the chat history

        for (int i = 0; i < chatHistorySize; i++) {
            String entry = chatHistory.get(i);

            if (entry.startsWith("User:")) {
                // Display user's question immediately
                Label labelNode = new Label(entry);
                labelNode.getStyleClass().add("text");
                PromptingVbox.getChildren().add(labelNode);
            } else if (entry.startsWith("Assistant:")) {
                String response = entry.substring("Assistant: ".length());
                Label labelNode = new Label();
                labelNode.getStyleClass().add("text");
                labelNode.setWrapText(true); // Enable text wrapping
                PromptingVbox.getChildren().add(labelNode);

                if (i > latestResponseIndex) {
                    PauseTransition initialDelay = new PauseTransition(Duration.seconds(0.5));

                    Timeline typingAnimation = new Timeline();

                    for (int j = 0; j < response.length(); j++) {
                        final int index = j;
                        KeyFrame keyFrame = new KeyFrame(Duration.millis(50 * (index + 1)), event -> {
                            labelNode.setText(response.substring(0, index + 1));
                        });
                        typingAnimation.getKeyFrames().add(keyFrame);
                    }

                    initialDelay.setOnFinished(event -> typingAnimation.play());
                    initialDelay.play();
                } else {
                    labelNode.setText(response);
                }
            }
        }

        latestResponseIndex = chatHistorySize - 1; // Update the latest response index
    }


    @FXML
    void SpeechTotext(MouseEvent event) throws ExecutionException, InterruptedException {
        if (isSpeechRecognitionRunning) {
            stopSpeechRecognition();
            stopProgressBarAnimation();

        } else {
            startSpeechRecognition();
            startProgressBarAnimation();


        }
    }

    private boolean isSpeechRecognitionRunning = false;
    private SpeechRecognizer speechRecognizer;
    @FXML
    private ProgressBar progressivBarVocal;

    private void startSpeechRecognition() throws ExecutionException, InterruptedException {

        SpeechConfig speechConfig = SpeechConfig.fromSubscription("75e26984895744239a820bf2ffc206b9", "westeurope");
        AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig);

        System.out.println("Speak into your microphone.");
        isSpeechRecognitionRunning = true;

        new Thread(() -> {
            try {
                while (isSpeechRecognitionRunning) {
                    Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
                    SpeechRecognitionResult result = task.get();
                    System.out.println("RECOGNIZED: Text=" + result.getText());

                    Timeline timeline = new Timeline();
                    for (int i = 0; i <= result.getText().length(); i++) {
                        int currentIndex = i;
                        KeyFrame keyFrame = new KeyFrame(Duration.millis(100 * currentIndex), event -> {
                            textFieldPrompt.setText(result.getText().substring(0, currentIndex));
                        });
                        timeline.getKeyFrames().add(keyFrame);
                    }

                    timeline.play();


                    if (result.getText().equalsIgnoreCase("clap clap clap")) {
                        stopSpeechRecognition();
                        stopProgressBarAnimation();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                speechRecognizer.close();
            }
        }).start();
    }

    private Timeline progressBarAnimation;
    private void startProgressBarAnimation() {
        progressivBarVocal.setTranslateX(-progressivBarVocal.getWidth());
        progressivBarVocal.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progressivBarVocal);
        transition2.setToX(0);
        transition2.play();
        progressBarAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressivBarVocal.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progressivBarVocal.progressProperty(), 1))
        );
        progressBarAnimation.setCycleCount(Timeline.INDEFINITE);
        progressBarAnimation.play();
    }


    private void stopProgressBarAnimation() {
        if (progressBarAnimation != null) {
            progressivBarVocal.setTranslateX(-progressivBarVocal.getWidth());
            progressivBarVocal.setVisible(false);
            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progressivBarVocal);
            transition2.setToX(0);
            transition2.play();
            progressBarAnimation.stop();
            progressivBarVocal.setProgress(0);

        }
    }
    private void stopSpeechRecognition() {
        if (isSpeechRecognitionRunning) {
            isSpeechRecognitionRunning = false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        vocalSendButton.setOnMouseEntered(event -> vocalSendButton.setStyle("-fx-background-color: lightblue;"));
        vocalSendButton.setOnMouseExited(event -> vocalSendButton.setStyle("-fx-background-color: lightgray;"));

        SearchButton.setOnMouseEntered(event -> SearchButton.setStyle("-fx-background-color: lightblue;"));
        SearchButton.setOnMouseExited(event -> SearchButton.setStyle("-fx-background-color: lightgray;"));


    }
}




