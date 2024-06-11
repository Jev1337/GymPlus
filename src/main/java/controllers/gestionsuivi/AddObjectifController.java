package controllers.gestionsuivi;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.RingProgressIndicator;
import atlantafx.base.theme.Styles;
import com.github.javafaker.Faker;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import controllers.gestionuser.GlobalVar;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.gestionSuivi.ObjectifService;
import services.gestionuser.AdminService;
import utils.MyDatabase;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class AddObjectifController implements Initializable {



    @FXML
    private TextField ageField;

    @FXML
    private DatePicker dpDureeObjectif;

    @FXML
    private Text labelTaille;

    @FXML
    private ChoiceBox<String> lsCoachName;

    @FXML
    private ChoiceBox<String> lsTypeObjectif;

    @FXML
    private Slider slTaille;

    @FXML
    private TextArea taAlergie;

    @FXML
    private TextField tfPoidsActuelle;

    @FXML
    private TextField tfPoidsObjectif;



    int taille;

    private String[] type = {"Par Defaut","Version ++"};


    @FXML
    private ToggleButton barToggle;


    private  Objectif objectif2 ;



    @FXML
    private Label TtileLabel;




    private static Stage stage ;
    private Objectif obj;


    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        AddObjectifController.stage = stage;
    }




    @FXML
void updateObjectif(ActionEvent event) {




        String poidsObjectifText = tfPoidsObjectif.getText();

        try {
            float poidsObjectiff = Float.parseFloat(poidsObjectifText);
            if (poidsObjectiff < 30 || poidsObjectiff > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }

        String poidsObjectifText2 = tfPoidsActuelle.getText();

        try {
            float poidsObjectifAct = Float.parseFloat(poidsObjectifText2);
            if (poidsObjectifAct < 30 || poidsObjectifAct > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }
       /* double tailleValue = slTaille.getValue();
        if (tailleValue < 1 || tailleValue > 2.5) {
            showAlertHeight();
            return;
        }*/

        LocalDate currentDatee = LocalDate.now();
        LocalDate selectedDate = dpDureeObjectif.getValue();
        if (selectedDate == null) {
            showAlertDate();
            return;
        }
        if (selectedDate.isBefore(currentDatee.plusDays(30))) {
            showAlertDate();
            return;
        }

    //tawa bech nconverti el date
    float poidsObjectif = Float.parseFloat(tfPoidsObjectif.getText());
    LocalDate currentDate = LocalDate.now();
    LocalDate mydate = dpDureeObjectif.getValue();
    java.sql.Date sqlDureeObjectifValue = java.sql.Date.valueOf(mydate);
    java.sql.Date sqlDureeNow =java.sql.Date.valueOf(currentDate);
    float poidsActuel = Float.parseFloat(tfPoidsActuelle.getText());
    float taille1 =(float) slTaille.getValue() ;
    String alergie = taAlergie.getText();
    String typeObj = lsTypeObjectif.getSelectionModel().getSelectedItem();
    String name_coach_selected =  lsCoachName.getSelectionModel().getSelectedItem();

    //*********************************************Confirmation Pannel************************//
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Modifcation Confirmation");
    dialog.setHeaderText("Confirm your Objectif");
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(getStage()); // Modify this line to get the appropriate stage reference
    Label label1 = new Label("Date fin de l'objectif " + dpDureeObjectif.getValue() + " \n Votre Coach sera " + lsCoachName.getSelectionModel().getSelectedItem());
    dialog.getDialogPane().setContent(label1);
    ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

    Platform.runLater(() -> {
        Optional<ButtonType> result = dialog.showAndWait();

        // Handle the dialog result here
        result.ifPresent(buttonType -> {
            if (buttonType == okButton) {
                int userid= GlobalVar.getUser().getId();;

                ObjectifService querry = new ObjectifService();
                int coachId = querry.getCoachIdByName(name_coach_selected); // Retrieve the coach's ID
                Objectif obj2 = new Objectif(obj.getId_objectif(),userid,poidsObjectif,sqlDureeObjectifValue,poidsActuel,taille1,alergie,typeObj,coachId) ;
                System.out.println("fel update fn");
             //   System.out.println(this.objectif2.getId_objectif());
                obj2.setDateD(sqlDureeNow);
                try {
                    System.out.printf("passed the try");
                    querry.update(obj2);
                    updatedNotif();
                    if (objectifController != null) {
                        objectifController.refreshNodesListeItems();
                    } else {
                        System.out.println("objectifController is null");
                    }

                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (buttonType == cancelButton) {
                System.out.println("nope");
            }
        });
    });

}
    @FXML
    private HBox HboxAddNotif;

public void addingNotif() throws IOException {

        var success = new Message(
                "Info", Faker.instance().expression("Gym Plus vous informe que l'ajout est fait avec succées"),
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
    HboxAddNotif.getChildren().clear();
    success.getStyleClass().add(Styles.ACCENT);
     HboxAddNotif.getChildren().add(success);

    // Schedule a task to clear the warning after 4 seconds
    Duration duration = Duration.seconds(3);
    KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
    Timeline timeline = new Timeline(keyFrame);
    timeline.play();
}
    public void  showAlertHeight(){
        var danger = new Message(
                "Danger", Faker.instance().expression(" Please Check that the height must be between 100 and 250 metre  "),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)
        );
        danger.getStyleClass().add(Styles.DANGER);
        HboxAddNotif.getChildren().add(danger);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

   private  static  ObjectifController objectifController;
    public void setObjectifController(ObjectifController objectifController) {
        AddObjectifController.objectifController = objectifController;
    }


public void danger(){
    var danger = new Message(
            "Danger", Faker.instance().expression(" Please Check All fields must be Filled with data ,no empty fields "),
            new FontAwesomeIconView(FontAwesomeIcon.WARNING)
    );
    danger.getStyleClass().add(Styles.DANGER);
    HboxAddNotif.getChildren().add(danger);
    Duration duration = Duration.seconds(3);
    KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
    Timeline timeline = new Timeline(keyFrame);
    timeline.play();
}
    public void showAlert(){
        var danger = new Message(
                "Danger", Faker.instance().expression("Weight got to between 30kg and 180kg and check the type "),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)
        );
        danger.getStyleClass().add(Styles.DANGER);
        HboxAddNotif.getChildren().add(danger);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    public void showAlertDate(){
        var danger = new Message(
                "Danger", Faker.instance().expression("Date Warning , make sure the Date you picked is more than 1 month"),
                new FontAwesomeIconView(FontAwesomeIcon.WARNING)
        );
        danger.getStyleClass().add(Styles.DANGER);
        HboxAddNotif.getChildren().add(danger);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }



    @FXML
    void addObjectif(ActionEvent event) throws SQLException {

        if (tfPoidsObjectif.getText().isEmpty() || tfPoidsActuelle.getText().isEmpty()
                ||  taAlergie.getText().isEmpty() ||  lsTypeObjectif.getSelectionModel().isEmpty()
                || lsCoachName.getSelectionModel().isEmpty()) {
            danger();
            return;
        }


        String poidsObjectifText = tfPoidsObjectif.getText();

        try {
            float poidsObjectiff = Float.parseFloat(poidsObjectifText);
            if (poidsObjectiff < 30 || poidsObjectiff > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }

        String poidsObjectifText2 = tfPoidsActuelle.getText();

        try {
            float poidsObjectifAct = Float.parseFloat(poidsObjectifText2);
            if (poidsObjectifAct < 30 || poidsObjectifAct > 180) {
                showAlert();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }
      /*  double tailleValue = slTaille.getValue();
        if (tailleValue < 1 || tailleValue > 2.5) {
            showAlertHeight();
            return;
        }*/

        LocalDate currentDatee = LocalDate.now();
        LocalDate selectedDate = dpDureeObjectif.getValue();
        if (selectedDate == null) {
            showAlertDate();
            return;
        }
        if (selectedDate.isBefore(currentDatee.plusDays(30))) {
            showAlertDate();
            return;
        }
        //tawa bech nconverti el date
        float poidsObjectif = Float.parseFloat(tfPoidsObjectif.getText());
        LocalDate currentDate = LocalDate.now();
        LocalDate mydate = dpDureeObjectif.getValue();
        java.sql.Date sqlDureeObjectifValue = java.sql.Date.valueOf(mydate);
        java.sql.Date sqlDureeNow =java.sql.Date.valueOf(currentDate);
        float poidsActuel = Float.parseFloat(tfPoidsActuelle.getText());
        float taille1 =(float) slTaille.getValue() ;
        String alergie = taAlergie.getText();
        String typeObj = lsTypeObjectif.getSelectionModel().getSelectedItem();
        String name_coach_selected =  lsCoachName.getSelectionModel().getSelectedItem();


        //*********************************************Confirmation Pannel************************//
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Confirmation");
        dialog.setHeaderText("Confirm your Objectif");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage()); // Modify this line to get the appropriate stage reference
        Label label1 = new Label("Date fin de l'objectif " + dpDureeObjectif.getValue() + " \n Votre Coach sera " + lsCoachName.getSelectionModel().getSelectedItem());
        dialog.getDialogPane().setContent(label1);
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Platform.runLater(() -> {
            Optional<ButtonType> result = dialog.showAndWait();

            // Handle the dialog result here
            result.ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    int userid= GlobalVar.getUser().getId();
                    ObjectifService querry = new ObjectifService();
                    int coachId = querry.getCoachIdByName(name_coach_selected); // Retrieve the coach's ID
                    Objectif obj = new Objectif(userid,poidsObjectif,sqlDureeObjectifValue,poidsActuel,taille1,alergie,typeObj,coachId) ;
                    obj.setDateD(sqlDureeNow);
                    try {
                        querry.add(obj);
                        addingNotif();
                        int randomInt2 = (int) (Math.random() * 50) + 1;

                        if (Objects.equals(typeObj, "Version ++")) {

                            Thread thread = new Thread(() -> {
                                chatGptExercercice(alergie,randomInt2);
                                chatGPT(poidsObjectif, poidsActuel, taille1,coachId);

                            });
                            thread.start();
                        }


                        if (objectifController != null) {
                            objectifController.refreshNodesListeItems();
                        } else {
                            System.out.println("objectifController is null");
                        }
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }

                } else if (buttonType == cancelButton) {
                    System.out.println("nope");
                }
            });
        });

        //******************************************************************************************************************//

    }


    void chatGptExercercice (String descriptiion, int randomInt2) {
        String message ="I want you to act like a Exercices Workout Model , i don't need you to be specific just give me a list of exercices based on the description that i will give you So i will give first thing the description :" + descriptiion+ " after the exercices based on the description give me the rest basic exercices for basic muscles , I will put the response into a pdf so i need it to be structured and to have at first this sentence : Hello! We are GymPlus Workout  Model and we are happy to tell you that your Workout list is ready after this i will put the content that will be the Workout and in the footer i want to put :We hope the Workout helped you to achieve your goal";
        System.out.println("generating .....");
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-4o";
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
                String content = choiceObject.getJSONObject("message").getString("content");
                System.out.println("*******************************************");


                String[] rows = content.split("\n");
                String[][] foodDietData = new String[rows.length][1]; // Only one column

                for (int i = 0; i < rows.length; i++) {
                    foodDietData[i][0] = rows[i]; // Store the entire line in the first (and only) column
                }



                //léna lambda expression fel test function  for pdf generating ofc fi thread 2 threads tawa 3andi maa aiModel Call
                System.out.println("now going to generate a pdf ");
                Thread thread = new Thread(() -> {
                    ConvertingIntoHtmlPdf2(foodDietData,randomInt2);
                });
                thread.start();

                return;
            }

            throw new RuntimeException("Failed to extract response content.");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }


    }

    void chatGPT(float ObjectiveWeight , float ActualWeight , float Height,int IdCoach) {
        String message ="I want you to act like a Diet Food Model i don't need you to be specific just give me a diet of food based on Objective weight & Actual Weight and the height is does not need to be correct So i will give first thing thee Objective Weight :" + ObjectiveWeight+ " and Actual Weight :"+ ActualWeight+ " and Height is :" +Height+ "I will put the response into a pdf so i need it to be structured and to have at first this sentence : Hello! We are GymPlus Diet Making Model and we are happy to tell you that your plan is ready after this i will put the content that will be the diet and in the footer i want to put :We hope the Diet helped you to achieve your goal. note that the diet got to have only (Breakfast,Lunch,Dinner,Snacks)and to be returned on a table specified with quantity with grams";
        // Construct the message content



        System.out.println("generating .....");
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-4o";
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
                String content = choiceObject.getJSONObject("message").getString("content");
                System.out.println("*******************************************");


                String[] rows = content.split("\n");
                String[][] foodDietData = new String[rows.length][1]; // Only one column

                for (int i = 0; i < rows.length; i++) {
                    foodDietData[i][0] = rows[i]; // Store the entire line in the first (and only) column
                }



                //léna lambda expression fel test function  for pdf generating ofc fi thread 2 threads tawa 3andi maa aiModel Call
                System.out.println("now going to generate a pdf ");
                Thread thread = new Thread(() -> {
                    ConvertingIntoHtmlPdf(foodDietData,IdCoach);
                });
                thread.start();

                return;
            }

            throw new RuntimeException("Failed to extract response content.");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }

  /*  void ConvertingIntoHtmlPdf(String contentComing,int IdCoach) {
        try {

            File file = new File("src/assets/html/PlanDietPdf.html");
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            //content = content.replace("{E1}", EquipementService.getEquipementById(selectedMaint.getIde()).getName());
           // content = content.replace("{E2}", String.valueOf(java.time.temporal.ChronoUnit.DAYS.between(LocalDate.parse(selectedMaint.getDate_maintenance()), LocalDate.now())));
            content = content.replace("{E3}", contentComing);
            //content = content.replace("{E4}", LocalDate.now().toString());
            //content = content.replace("{E5}", String.valueOf(selectedMaint.getIdm()));

            File newFile = new File("src/assets/html/PlanDietPdf.html" + IdCoach + ".html");
            FileWriter writer = new FileWriter(newFile);
            writer.write(content);
            writer.close();
            ConverterProperties properties = new ConverterProperties();

            properties.setBaseUri("src/assets/html/");
            properties.setCharset("UTF-8");
            properties.setImmediateFlush(true);
            properties.setCreateAcroForm(true);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialFileName("attestation" + IdCoach);
            File file1 = fileChooser.showSaveDialog(gotoequip_btn.getScene().getWindow());
            if (file1 == null) {
                throw new IllegalArgumentException("No file selected");
            }
            HtmlConverter.convertToPdf(new FileInputStream("src/assets/html/attestation" + IdCoach + ".html"), new FileOutputStream(file1), properties);
            java.awt.Desktop.getDesktop().open(file1);
            newFile.delete();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
*/
  /*  void ConvertingIntoHtmlPdf(String contentComing, int IdCoach) {
        try {
            File file = new File("src/assets/html/PlanDietPdf.html");
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            content = content.replace("{E3}", contentComing);

            File newFile = new File("src/assets/html/PlanDietPdf.html" + IdCoach + ".html");
            FileWriter writer = new FileWriter(newFile);
            writer.write(content);
            writer.close();

            ConverterProperties properties = new ConverterProperties();
            properties.setBaseUri("src/assets/html/");
            properties.setCharset("UTF-8");
            properties.setImmediateFlush(true);
            properties.setCreateAcroForm(true);

            HtmlConverter.convertToPdf(new FileInputStream("src/assets/html/PlanDietPdf" + IdCoach + ".html"), new FileOutputStream("src/assets/pdf/PlanDietPdf" + IdCoach + ".pdf"), properties);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
*/





    private String generateFoodDietTable(String[][] data) {
        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append("<table style=\"width: 100%; border: 1px solid #ccc;\">\n");
        tableHtml.append("<tr>\n");
        tableHtml.append("<th>Meal</th>\n");
        tableHtml.append("</tr>\n");

        for (String[] row : data) {
            if (!row[0].isEmpty()) { // If the "Meal" cell is not empty
                tableHtml.append("<tr><td colspan=\"3\"></td></tr>\n"); // Add an empty row
            }
            tableHtml.append("<tr>\n");
            for (String cell : row) {
                tableHtml.append("<td>").append(cell).append("</td>\n");
            }
            tableHtml.append("</tr>\n");
        }

        tableHtml.append("</table>");
        return tableHtml.toString();
    }
    void ConvertingIntoHtmlPdf(String[][] foodDietData, int IdCoach) {
        try {
                File file = new File("src/assets/html/PlanDietPdf.html");
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            String foodDietTable = generateFoodDietTable(foodDietData);
            content = content.replace("{E3}", foodDietTable);

            File newFile = new File("src/assets/html/PlanDietPdf" + IdCoach + ".html");
            FileWriter writer = new FileWriter(newFile);
            writer.write(content);
            writer.close();

            ConverterProperties properties = new ConverterProperties();
            properties.setBaseUri("src/assets/html/");
            properties.setCharset("UTF-8");
            properties.setImmediateFlush(true);
            properties.setCreateAcroForm(true);

            HtmlConverter.convertToPdf(new FileInputStream("src/assets/html/PlanDietPdf" + IdCoach + ".html"), new FileOutputStream("src/assets/pdf/PlanDietPdf" + IdCoach + ".pdf"), properties);

            String mail = "gymplus-noreply@grandelation.com";
            String password = "yzDvS_UoSL7b";
            String to = GlobalVar.getUser().getEmail();
            String subject = "Your Diet Plan";
            //the body will be "index.html" inside src/assets/html
            String body = "Here is your plan";
            String host = "mail.grandelation.com";

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.debug", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(mail);
            msg.setRecipients(jakarta.mail.Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setSentDate(new java.util.Date());
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile("src/assets/pdf/PlanDietPdf" + IdCoach + ".pdf");
            multipart.addBodyPart(attachmentBodyPart);
            msg.setContent(multipart);

            Transport.send(msg, mail, password);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    void ConvertingIntoHtmlPdf2(String[][] foodDietData, int IdCoach) {
        try {
            File file = new File("src/assets/html/Workout.html");
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            String foodDietTable = generateFoodDietTable(foodDietData);
            content = content.replace("{E3}", foodDietTable);

            File newFile = new File("src/assets/html/Workout" + IdCoach + ".html");
            FileWriter writer = new FileWriter(newFile);
            writer.write(content);
            writer.close();

            ConverterProperties properties = new ConverterProperties();
            properties.setBaseUri("src/assets/html/");
            properties.setCharset("UTF-8");
            properties.setImmediateFlush(true);
            properties.setCreateAcroForm(true);

            HtmlConverter.convertToPdf(new FileInputStream("src/assets/html/Workout" + IdCoach + ".html"), new FileOutputStream("src/assets/pdf/Workout" + IdCoach + ".pdf"), properties);

            String mail = "gymplus-noreply@grandelation.com";
            String password = "yzDvS_UoSL7b";
            String to = GlobalVar.getUser().getEmail();
            String subject = "Your Workout Plan";
            //the body will be "index.html" inside src/assets/html
            String body = "Here is your Workout";
            String host = "mail.grandelation.com";

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.debug", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(mail);
            msg.setRecipients(jakarta.mail.Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setSentDate(new java.util.Date());
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile("src/assets/pdf/Workout" + IdCoach + ".pdf");
            multipart.addBodyPart(attachmentBodyPart);
            msg.setContent(multipart);

            Transport.send(msg, mail, password);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


  /*  public String insertLineBreaks(String text, int maxLineWidth) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() > maxLineWidth) {
                result.append(line).append("\n");
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        result.append(line);  // Add the final line

        return result.toString();
    }


    void testing(String content) {
        Path pdfPath = Paths.get("chatgpt_response.pdf");


        content = insertLineBreaks(content, 50);  // Assuming max line width of 50 characters

        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();

            // Create a new page for the PDF
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Create a content stream to write text on the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add a header with a logo
            PDImageXObject logo = PDImageXObject.createFromFile("src/assets/html/assets/GP.png", document);
            contentStream.drawImage(logo, 50, page.getMediaBox().getHeight() - 100, 100, 100);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(200, page.getMediaBox().getHeight() - 100);
            contentStream.showText("ChatGPT Response");
            contentStream.endText();

            // Begin the text stream
            contentStream.beginText();

            // Write the text on the page, one line at a time
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);  // Set line spacing
            contentStream.newLineAtOffset(50, page.getMediaBox().getHeight() - 200);  // Starting position
            for (String line : content.split("\\n")) {
                contentStream.showText(line);
                contentStream.newLine();  // Move to the next line
            }

            // Close the text stream
            contentStream.endText();

            // Close the content stream
            contentStream.close();

            // Save the PDF document
            try (OutputStream out = Files.newOutputStream(pdfPath)) {
                document.save(out);
            }

            // Close the PDF document
            document.close();

            System.out.println("PDF generated successfully at: " + pdfPath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/








    public void displayCoachNamefromCoachTable() {
        String  selectAllData ="SELECT * FROM user WHERE role='staff'";
        MyDatabase c = new MyDatabase();

        try {
            PreparedStatement prepare = c.getConnection().prepareStatement(selectAllData);
            ResultSet result = prepare.executeQuery();
            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()){
                String item = result.getString("username");
                listData.add(item);
            }
            lsCoachName.setItems(listData) ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    @FXML
    private ProgressBar ProgrssBarModif;

    @FXML
    private ToggleButton ToogleModif;





     void sendingDataToForm(Objectif obj) {
         try {
             ProgrssBarAdd.setVisible(false);
             ToogleAdd.setVisible(false);
             ProgrssBarModif.setVisible(true);
             ToogleModif.setVisible(true);
             TtileLabel.setText("Modification Objectif");
             int id_obj=obj.getId_objectif();
             int id_user = obj.getId_user();
             System.out.printf("fel sendingDataForm function ");
             System.out.println(id_obj);
             System.out.println(id_user);
             String coachName = obj.getCoachName();
             obj = new Objectif(obj.getId_objectif(),obj.getId_user(),obj.getPoids_Obj(),obj.getDateF(),obj.getPoids_Act(),obj.getTaille(),obj.getAlergie(),obj.getTypeObj(),obj.getCoachId());
             this.obj = obj;
             tfPoidsObjectif.setText(String.valueOf(obj.getPoids_Obj()));
             java.sql.Date dateF = obj.getDateF();
             LocalDate localDateF = dateF.toLocalDate();
             dpDureeObjectif.setValue(localDateF);
             tfPoidsActuelle.setText(String.valueOf(obj.getPoids_Act()));
             slTaille.setValue(obj.getTaille());
             taAlergie.setText(obj.getAlergie());
             String typeObj = obj.getTypeObj();
             lsTypeObjectif.getSelectionModel().select(typeObj);
             lsCoachName.getSelectionModel().select(coachName);

         }catch (Exception e){
             e.printStackTrace();
         }
     }


    public void updatedNotif (){

        var success = new Message(
                "Info",
                "well done you've modified the plan successfully for the client",
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
        success.getStyleClass().add(Styles.ACCENT);
        HboxAddNotif.getChildren().add(success);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    public void success (){

        var success = new Message(
                "Info",
                "well done you've added the plan successfully for the client",
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)
        );
        success.getStyleClass().add(Styles.ACCENT);
        HboxAddNotif.getChildren().add(success);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> HboxAddNotif.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    @FXML
    private ProgressBar ProgrssBarAdd;

    @FXML
    private ToggleButton ToogleAdd;


    void setToogleAddVisible(){
        ProgrssBarAdd.setVisible(true);
        ToogleAdd.setVisible(true);
        ProgrssBarModif.setVisible(false);
        ToogleModif.setVisible(false);
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dpDureeObjectif.getEditor().setDisable(true);
        //Adding Toogle
        ToogleAdd.textProperty().bind(Bindings.createStringBinding(
                () -> ToogleAdd.isSelected() ? "Adding..." : "Adding Objectif",
                ToogleAdd.selectedProperty()
        ));


        ProgrssBarAdd.progressProperty().bind(Bindings.createDoubleBinding(
                () -> ToogleAdd.isSelected() ? -1d : 0d,
                ToogleAdd.selectedProperty()
        ));
        ToogleAdd.setOnAction(event -> {
            if (ToogleAdd.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {

                    try {
                        addObjectif(new ActionEvent());
                        ToogleAdd.setSelected(false);

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }


                });
                pause.play();

            }

        });




        //Modiff ToogleButton
        ToogleModif.textProperty().bind(Bindings.createStringBinding(
                () -> ToogleModif.isSelected() ? "Updating..." : "Update Planning",
                ToogleModif.selectedProperty()
        ));


        ProgrssBarModif.progressProperty().bind(Bindings.createDoubleBinding(
                () -> ToogleModif.isSelected() ? -1d : 0d,
                ToogleModif.selectedProperty()
        ));
        ToogleModif.setOnAction(event -> {
            if (ToogleModif.isSelected()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {

                    updateObjectif(new ActionEvent());
                    ToogleModif.setSelected(false);

                });
                pause.play();

            }

        });


        displayCoachNamefromCoachTable();
        lsTypeObjectif.getItems().addAll(type);
        slTaille.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                taille =(int) slTaille.getValue() ;
                labelTaille.setText("Taille : "+Integer.toString(taille)+"cm");
            }
        });
    }


}
