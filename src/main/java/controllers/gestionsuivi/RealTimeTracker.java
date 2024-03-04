package controllers.gestionsuivi;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.fazecast.jSerialComm.SerialPort;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import com.fazecast.jSerialComm.SerialPort;
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
private Text caloriesTextField;
    @FXML
    private Text BpmTextField;

    @FXML
    private Text AverageBpmField;
    @FXML
    private Text hearRateSound;

    @FXML
    private Text totalCalsFields;
    private static final int THRESHOLD = 2000; // Set this to a suitable value for your sensor
    private static long lastBeatTime = 0;

    private static int count = 0;
    private static int heartRateSum = 0;
    public  void callChart(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String portName = "COM3"; // Replace with the actual serial port name
                int baudRate = 115200; // Match the baud rate with the Arduino code

                SerialPort serialPort = SerialPort.getCommPort(portName);
                serialPort.setBaudRate(baudRate);

                if (serialPort.openPort()) {
                    System.out.println("Serial port opened successfully.");

                    InputStream inputStream = serialPort.getInputStream();

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
                                        float ecgData = Float.parseFloat(data.trim());
                                        series.getData().add(new XYChart.Data<>(String.valueOf(System.currentTimeMillis()), ecgData));
                                        double weight = 91; // in kg
                                        int age = 21; // in years
                                        double timeInMinutes = 0; // start with 0 minutes
                                        hearRateSound.setText(String.valueOf(ecgData));


                                        if (ecgData > THRESHOLD) {
                                            long currentTime = System.currentTimeMillis();
                                            int beatInterval = (int) (currentTime - lastBeatTime);
                                            lastBeatTime = currentTime;
                                            if (beatInterval != 0) {
                                                int beatsPerMinute = 60000 / beatInterval;
                                                if (beatsPerMinute >= 58 && beatsPerMinute <= 150) {
                                                    System.out.println("Heart rate: " + beatsPerMinute + " bpm");
                                                    BpmTextField.setText(String.valueOf(beatsPerMinute));
                                                    series2.getData().add(new XYChart.Data<>(String.valueOf(System.currentTimeMillis()), beatsPerMinute));

                                                    heartRateSum += beatsPerMinute;
                                                    count++;
                                                }
                                            }
                                        }
                                        if (count == 60) { // If a minute has passed
                                            int averageHeartRate = heartRateSum / count;
                                            System.out.println("Average heart rate over the last minute: " + averageHeartRate + " bpm");
                                            AverageBpmField.setText(String.valueOf(averageHeartRate));
                                            timeInMinutes++;
                                            double caloriesBurned = calculateCaloriesBurned(averageHeartRate, weight, age, timeInMinutes);
                                            System.out.println("Estimated calories burned: " + caloriesBurned);
                                            DecimalFormat decimalFormat = new DecimalFormat("00.00");
                                            caloriesTextField.setText(decimalFormat.format(caloriesBurned));                                            caloriesBurnedList.add(caloriesBurned);
                                            double sumCalories = 0.0;

                                            for (double calories : caloriesBurnedList) {
                                                sumCalories += calories;
                                            }
                                            totalCalsFields.setText(decimalFormat.format(caloriesBurned));

                                            heartRateSum = 0;
                                            count = 0;
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
                } else {
                    System.err.println("Failed to open the serial port.");
                }

                serialPort.closePort();
                SttartB.setOnAction((E) -> {
                    Animations.wobble(SttartB).playFromStart();
                    callChart();
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



    public static double calculateCaloriesBurned(int heartRate, double weight, int age, double timeInMinutes) {
        return (timeInMinutes * (0.6309 * heartRate + 0.1988 * weight + 0.2017 * age - 55.0969)) / 4.184;
    }
void  WifiCOnnection(){

    String serverIP = "172.20.10.4"; // Replace with the ESP32 IP address
    int serverPort = 80; // Replace with the ESP32 port

    try (Socket socket = new Socket(serverIP, serverPort);
         PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

        writer.println("start_ecg_stream");

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String ecgDataString;
        double weight = 91; // in kg
        int age = 21; // in years
        double timeInMinutes = 0; // start with 0 minutes
        int heartRateSum = 0;
        int count = 0;

        while (socket.isConnected()) {
            ecgDataString = reader.readLine();
            if (ecgDataString == null) {
                continue;
            }
            float ecgData = Float.parseFloat(ecgDataString.trim());

            if (ecgData > THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                int beatInterval = (int) (currentTime - lastBeatTime);
                lastBeatTime = currentTime;
                if (beatInterval != 0) {
                    int beatsPerMinute = 60000 / beatInterval;
                    if (beatsPerMinute >= 58 && beatsPerMinute <= 150) {
                        System.out.println("Heart rate: " + beatsPerMinute + " bpm");
                        heartRateSum += beatsPerMinute;
                        count++;
                    }
                }
            }

            if (count == 60) { // If a minute has passed
                int averageHeartRate = heartRateSum / count;
                System.out.println("Average heart rate over the last minute: " + averageHeartRate + " bpm");
                timeInMinutes++;
                double caloriesBurned = calculateCaloriesBurned(averageHeartRate, weight, age, timeInMinutes);
                System.out.println("Estimated calories burned: " + caloriesBurned);
                heartRateSum = 0;
                count = 0;
            }
        }
        System.out.println("End of data stream.");
    } catch (IOException ex) {
        throw new RuntimeException(ex);
    }
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
            callChart();
            startProgressBarAnimation();
            startProgressBarAnimation2();
            startProgressBarAnimation3();
            startProgressBarAnimation4();
            //WifiCOnnection();
        });




    }
}
