package controllers.gestionsuivi;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.fazecast.jSerialComm.SerialPort;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.itextpdf.layout.Document;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import com.fazecast.jSerialComm.SerialPort;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.io.InputStream;


public class RealTimeTracker implements Initializable {

    @FXML
    private Button SttartB;

    @FXML
    private Button ratebutton;


    @FXML
    private LineChart<String, Number> RateChart;

    @FXML
    private LineChart<String, Number> ChartHearrate;

    @FXML
    private ProgressBar progressiveStart;

    @FXML
    private ProgressBar progressiveStart1;
    @FXML
    private ProgressBar progbuton;

    @FXML
    private ProgressBar progbuton1;
    private Timeline progressBarAnimation;

    private void startProgressBarAnimation() {
        progressiveStart.setTranslateX(-progressiveStart.getWidth());
        progressiveStart.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progressiveStart);
        transition2.setToX(0);
        transition2.play();
        progressBarAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressiveStart.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progressiveStart.progressProperty(), 1))

        );
        progressBarAnimation.setCycleCount(Timeline.INDEFINITE);
        progressBarAnimation.play();

    }

    private void startProgressBarAnimation2() {
        progressiveStart1.setTranslateX(-progressiveStart1.getWidth());
        progressiveStart1.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progressiveStart1);
        transition2.setToX(0);
        transition2.play();
        progressBarAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressiveStart1.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progressiveStart1.progressProperty(), 1))

        );
        progressBarAnimation.setCycleCount(Timeline.INDEFINITE);
        progressBarAnimation.play();

    }

    private void startProgressBarAnimation3() {
        progbuton1.setTranslateX(-progbuton1.getWidth());
        progbuton1.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progbuton1);
        transition2.setToX(0);
        transition2.play();
        progressBarAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progbuton1.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progbuton1.progressProperty(), 1))
        );
        progressBarAnimation.setCycleCount(Timeline.INDEFINITE);
        progressBarAnimation.play();

    }
    private void startProgressBarAnimation4() {
        progbuton.setTranslateX(-progbuton.getWidth());
        progbuton.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progbuton);
        transition2.setToX(0);
        transition2.play();
        progressBarAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progbuton.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progbuton.progressProperty(), 1))

        );
        progressBarAnimation.setCycleCount(Timeline.INDEFINITE);
        progressBarAnimation.play();

    }





    private void stopProgressBarAnimation() {
        if (progressBarAnimation != null) {
            progressiveStart.setTranslateX(-progressiveStart.getWidth());
            progressiveStart.setVisible(false);
            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progressiveStart);
            transition2.setToX(0);
            transition2.play();
            progressBarAnimation.stop();
            progressiveStart.setProgress(0);

        }
    }
    private void stopProgressBarAnimation2() {
        if (progressBarAnimation != null) {
            progressiveStart1.setTranslateX(-progressiveStart1.getWidth());
            progressiveStart1.setVisible(false);
            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progressiveStart1);
            transition2.setToX(0);
            transition2.play();
            progressBarAnimation.stop();
            progressiveStart1.setProgress(0);

        }
    }

    private void stopProgressBarAnimation3() {
        if (progressBarAnimation != null) {
            progbuton.setTranslateX(-progbuton.getWidth());
            progbuton.setVisible(false);
            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progbuton);
            transition2.setToX(0);
            transition2.play();
            progressBarAnimation.stop();
            progbuton.setProgress(0);

        }
    }
    private void stopProgressBarAnimation4() {
        if (progressBarAnimation != null) {
            progbuton1.setTranslateX(-progbuton1.getWidth());
            progbuton1.setVisible(false);
            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), progbuton1);
            transition2.setToX(0);
            transition2.play();
            progressBarAnimation.stop();
            progbuton1.setProgress(0);

        }
    }
  @FXML
    private Pane SquatPan;
    private String typeExercise = "";
    @FXML
    void SquatFunction(MouseEvent event) {
        typeExercise = "squat";
    }
    @FXML
    void PulUpFn(MouseEvent event) {
        typeExercise = "pull-up";
    }

    @FXML
    void PushUpFn(MouseEvent event) {
        typeExercise = "push-up";
    }

    @FXML
    void SitUpFn(MouseEvent event) {
        typeExercise = "sit-up";
    }

    @FXML
    void WalkingFn(MouseEvent event) {
        typeExercise = "walk";
    }
    @FXML
    private Button AiModelButton;

    @FXML
    private ProgressBar bar1Ai;

    @FXML
    private ProgressBar bar2Ai;


@FXML
private Text caloriesTextField;
    @FXML
    private Text BpmTextField;

    @FXML
    private Text AverageBpmField;
    @FXML
    private Text hearRateSound;

    @FXML
    private Text totalCalsFields;
    private static final int THRESHOLD = 1800; // Set this to a suitable value for your sensor
    private static long lastBeatTime = 0;

    private static int heartRateSum = 0;
    private  static  double sumCalories = 0.0;
    private static int averageHeartRate = 0;

    private  static  double caloriesBurned = 0.0;

    private  static  double timeInMinutes = 0.0;




    public static double calculateCaloriesBurned(int heartRate, double weight, int age, double timeInMinutes) {
        return (timeInMinutes * (0.6309 * heartRate + 0.1988 * weight + 0.2017 * age - 55.0969)) / 4.184;
    }





  private int count = 0;


    private void startBar1AiModel() {
        bar1Ai.setTranslateX(-bar1Ai.getWidth());
        bar1Ai.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), bar1Ai);
        transition2.setToX(0);
        transition2.play();
        progressBarAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(bar1Ai.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(bar1Ai.progressProperty(), 1))

        );
        progressBarAnimation.setCycleCount(Timeline.INDEFINITE);
        progressBarAnimation.play();

    }
    private void startBar2AiModel() {
        bar2Ai.setTranslateX(-bar2Ai.getWidth());
        bar2Ai.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), bar2Ai);
        transition2.setToX(0);
        transition2.play();
        progressBarAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(bar2Ai.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(bar2Ai.progressProperty(), 1))

        );
        progressBarAnimation.setCycleCount(Timeline.INDEFINITE);
        progressBarAnimation.play();

    }

    private void stopProgressBar2AiModel() {
        if (progressBarAnimation != null) {
            bar2Ai.setTranslateX(-bar2Ai.getWidth());
            bar2Ai.setVisible(false);
            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), bar2Ai);
            transition2.setToX(0);
            transition2.play();
            progressBarAnimation.stop();
            bar2Ai.setProgress(0);

        }
    }
    private void stopProgressBar1AiModel() {
        if (progressBarAnimation != null) {
            bar1Ai.setTranslateX(-bar1Ai.getWidth());
            bar1Ai.setVisible(false);
            TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), bar1Ai);
            transition2.setToX(0);
            transition2.play();
            progressBarAnimation.stop();
            bar1Ai.setProgress(0);

        }
    }


    /* @FXML
    public void callPy(ActionEvent event) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // Define the path to the Python executable
                    String pythonPath = "C:\\Users\\MSI\\AppData\\Local\\Programs\\Python\\Python312\\python.exe";

                    // Define the command to execute
                    String command = pythonPath + " C:\\Users\\MSI\\Desktop\\AI-Fitness-trainer-main\\main.py -t squat";

                    // Execute the command
                    Process process = Runtime.getRuntime().exec(command);

                    // Read the output of the command
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    // Check the exit code of the command
                    int exitCode = process.waitFor();
                    System.out.println("Python script executed with exit code: " + exitCode);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    } */

    public void callModelAi() {
        Task<Void> task = new Task<Void>() {
            private Process process;

            @Override
            protected Void call() throws Exception {
                try {
                    String pythonPath = "C:\\Users\\MSI\\AppData\\Local\\Programs\\Python\\Python312\\python.exe";
                    String command = pythonPath + " C:\\Users\\MSI\\Desktop\\AI-Fitness-trainer-main\\main.py -t squat";

                    process = Runtime.getRuntime().exec(command);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    int exitCode = process.waitFor();
                    System.out.println("Python script executed with exit code: " + exitCode);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                AiModelButton.setOnAction((E) -> {
                    Animations.wobble(AiModelButton).playFromStart();
                    callModelAi();
                    startBar1AiModel();
                    startBar2AiModel();

                });
                return null;
            }

            @Override
            protected void cancelled() {
                if (process != null) {
                    process.destroy();
                }
            }
        };

        // Start the task
        new Thread(task).start();

        AiModelButton.setOnAction((E) -> {
            Animations.wobble(AiModelButton).playFromStart();
            task.cancel(false);
            stopProgressBar2AiModel();
            stopProgressBar1AiModel();
        });
    }


    public void callChartWiFi() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String ipAddress = "192.168.1.162";
                int port = 80;

                try (Socket socket = new Socket(ipAddress, port)) {
                    System.out.println("Wi-Fi connection established.");

                    InputStream inputStream = socket.getInputStream();

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    try {
                        XYChart.Series<String, Number> series = new XYChart.Series<>();
                        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
                        List<Double> caloriesBurnedList = new ArrayList<>();

                        while (!isCancelled()) {
                            if (inputStream.available() > 0) {
                                bytesRead = inputStream.read(buffer);
                                String data = new String(buffer, 0, bytesRead);
                                Platform.runLater(() -> {
                                    if (!data.trim().isEmpty()) {
                                        double ecgData = Double.parseDouble(data.trim());
                                        series.getData().add(new XYChart.Data<>(String.valueOf(System.currentTimeMillis()), ecgData));
                                        double weight = 70; // in kg
                                        int age = 16; // in years
                                        hearRateSound.setText(String.valueOf(ecgData));

                                        if (ecgData > THRESHOLD) {
                                            long currentTime = System.currentTimeMillis();
                                            int beatInterval = (int) (currentTime - lastBeatTime);
                                            lastBeatTime = currentTime;
                                            if (beatInterval != 0) {
                                                int beatsPerMinute = 20000 / beatInterval;
                                                if (beatsPerMinute >= 55 && beatsPerMinute <= 100) {
                                                    System.out.println("Heart rate: " + beatsPerMinute + " bpm");
                                                    BpmTextField.setText(String.valueOf(beatsPerMinute));
                                                    series2.getData().add(new XYChart.Data<>(String.valueOf(System.currentTimeMillis()), beatsPerMinute));

                                                    heartRateSum += beatsPerMinute;
                                                    count++;
                                                }
                                            }
                                        }
                                        if (count == 22) { // If a minute has passed
                                            count = 23;
                                          //  heartRateSum += 1410;
                                           averageHeartRate = heartRateSum / count;
                                            System.out.println("Average heart rate over the last minute: " + averageHeartRate + " bpm");
                                            AverageBpmField.setText(String.valueOf(averageHeartRate));
                                            timeInMinutes = timeInMinutes + 0.2 ;
                                             caloriesBurned = calculateCaloriesBurned(averageHeartRate, weight, age, timeInMinutes);
                                            System.out.println("Estimated calories burned: " + caloriesBurned);
                                            DecimalFormat decimalFormat = new DecimalFormat("00.00");
                                            caloriesTextField.setText(decimalFormat.format(caloriesBurned));


                                            caloriesBurnedList.add(caloriesBurned);
                                            sumCalories += caloriesBurnedList.get(caloriesBurnedList.size() -1 );


                                            totalCalsFields.setText(decimalFormat.format(sumCalories));
                                            timeInMinutes= 0.2;
                                            caloriesBurned = 10;
                                            count = 0;
                                            averageHeartRate= 0;
                                            heartRateSum = 20;
                                        }

                                    }
                                    if (series2.getData().size() > 100)
                                        series2.getData().remove(0);
                                    RateChart.getData().clear();
                                    RateChart.getData().add(series2);

                                    if (series.getData().size() > 100)
                                        series.getData().remove(0);
                                    ChartHearrate.getData().clear();
                                    ChartHearrate.getData().add(series);
                                });
                            }
                            Thread.sleep(70);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.err.println("Failed to establish Wi-Fi connection: " + e.getMessage());
                }

                SttartB.setOnAction((E) -> {
                    Animations.wobble(SttartB).playFromStart();
                    callChartWiFi();
                    startProgressBarAnimation();
                    startProgressBarAnimation2();
                    startProgressBarAnimation3();
                    startProgressBarAnimation4();
                });

                return null;
            }
        };

        new Thread(task).start();

        SttartB.setOnAction((E) -> {
            Animations.wobble(SttartB).playFromStart();
            task.cancel(false);
            stopProgressBarAnimation();
            stopProgressBarAnimation2();
            stopProgressBarAnimation3();
            stopProgressBarAnimation4();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChartHearrate.setAnimated(false);

        RateChart.setAnimated(false);

        SttartB.setOnMouseEntered(event -> SttartB.setStyle("-fx-background-color: lightblue;"));
        SttartB.setOnMouseExited(event -> SttartB.setStyle("-fx-background-color: lightgray;"));
        // styling assitant button
        SttartB.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        SttartB.setMnemonicParsing(true);
        SttartB.setOnAction((E) -> {
            Animations.wobble(SttartB).playFromStart();
            //callChart();
            callChartWiFi();
            startProgressBarAnimation();
            startProgressBarAnimation2();
            startProgressBarAnimation3();
            startProgressBarAnimation4();
        });


        AiModelButton.setOnMouseEntered(event -> AiModelButton.setStyle("-fx-background-color: lightblue;"));
        AiModelButton.setOnMouseExited(event -> AiModelButton.setStyle("-fx-background-color: lightgray;"));
        // styling assitant button
        AiModelButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        AiModelButton.setMnemonicParsing(true);
        AiModelButton.setOnAction((E) -> {
            Animations.wobble(AiModelButton).playFromStart();
            callModelAi();
            startBar2AiModel();
            startBar1AiModel();
        });

    }
}
