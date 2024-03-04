package controllers.gestionsuivi;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.Message;
import atlantafx.base.controls.Popover;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import com.github.javafaker.Faker;
import com.twilio.rest.api.v2010.account.usage.record.TodayReader;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import entities.gestionSuivi.Planning;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HorizontalDirection;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.apache.poi.ss.formula.functions.Hyperlink;
import org.apache.poi.ss.formula.functions.Today;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PlanningController implements Initializable {


    @FXML
    private VBox CalendarVbox;


    @FXML
    private VBox ListeObjectifVbox;


    @FXML
    private Pane PaneRight;

    @FXML
    private Pane PaneAjouterPlanning;


    @FXML
    void SetInterfaceEtatInitialie(MouseEvent event) {
        PaneRight.setVisible(false);
        PaneAjouterPlanning.setVisible(false);

    }


    @FXML
    private Text labelDone;

    @FXML
    private Text labelInProgress;
    private   int NumlberOfPlanDone;

    public void displayListePlanning(){
        ListeObjectifVbox.getChildren().clear();
        PlanningListController planningListController = new PlanningListController();
        ObservableList<Objectif> objectifList = planningListController.getPlansList();

        for (Objectif objPlan :objectifList) {
            try {
                NumlberOfPlanDone = objPlan.getNumbersPlansDone();
                System.out.println("objPlan.getId_planning");
                System.out.println(objPlan.getId_planning());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/PlanObjectifList.fxml"));
                Node node = loader.load();
                PlanningListController itemController = loader.getController();
                itemController.setData(objPlan);
                ListeObjectifVbox.getChildren().add(node);
                labelDone.setText(String.valueOf(NumlberOfPlanDone));

                Node nodee = node.lookup("#ObjectifLabelHyperlink");
                if (nodee != null) {
                    nodee.setOnMouseClicked(event -> {
                        System.out.println("aaaaaa");
                        try {
                            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/gestionSuivi/ObjectifDisplayOnRight.fxml"));
                            PaneRight.setTranslateX(-PaneRight.getWidth());
                            PaneRight.setVisible(true);
                            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), PaneRight);
                            transition.setToX(0);
                            transition.play();
                            Node node2 = loader2.load();
                            ObjectifDisplayOnRightController objectifDisplayOnRightController = loader2.getController();
                            objectifDisplayOnRightController.setPallningControler(this);
                            objectifDisplayOnRightController.makeModifButtonVisible();
                            objectifDisplayOnRightController.setPallningControler(this);
                            objectifDisplayOnRightController.dataInfos(objPlan);
                            PaneRight.getChildren().add(node2);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                } else {
                    System.out.println("nope");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private   int NumlberOfPlanInProgress;

    public void displayListeObjectif() {
        ListeObjectifVbox.getChildren().clear();
        PlanningListController planningListController = new PlanningListController();
        ObservableList<Objectif> objectifList = planningListController.getObjectifList();

        for (Objectif objPlan : objectifList) {
            try {
                NumlberOfPlanInProgress = objPlan.getNumberPlansInProgressive();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/PlanObjectifList.fxml"));
                Node node = loader.load();
                PlanningListController itemController = loader.getController();
                itemController.setData(objPlan);
                ListeObjectifVbox.getChildren().addAll(node);
                labelInProgress.setText(String.valueOf(NumlberOfPlanInProgress));

                Node nodee = node.lookup("#ObjectifLabelHyperlink");
                if (nodee != null) {
                    nodee.setOnMouseClicked(event -> {
                        System.out.println("aaaaaa");
                        try {
                            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/gestionSuivi/ObjectifDisplayOnRight.fxml"));
                            PaneRight.setTranslateX(-PaneRight.getWidth());
                            PaneRight.setVisible(true);

                            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), PaneRight);
                            transition.setToX(0);
                            transition.play();
                            Node node2 = loader2.load();
                            ObjectifDisplayOnRightController objectifDisplayOnRightController = loader2.getController();
                            objectifDisplayOnRightController.setPallningControler(this);
                            objectifDisplayOnRightController.makeModifButtonInvisble();
                            objectifDisplayOnRightController.setPallningControler(this);
                            objectifDisplayOnRightController.dataInfos(objPlan);
                            PaneRight.getChildren().add(node2);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                } else {
                    System.out.println("nope");
                }

            } catch (IOException ex) {
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);

            }
        }



    }
    private  static  PlanningListController  planningListController = new PlanningListController() ;

    void MakeItVisibleAddingPlanPan (Objectif objPlan) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/AjouterPlanning.fxml"));
        Node node = loader.load();
        ListeObjectifVbox.setVisible(false);
        Date date = objPlan.getDateNaissance();
        java.util.Date utilDate = new java.util.Date(date.getTime());
        LocalDate birthDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        int agee = age.getYears();

        double  weight = objPlan.getPoids_Act();
        double  height =objPlan.getTaille()   ;

        ObjectifAddPlanningOnLeft itemController = loader.getController();
        if(objPlan.getId_planning() !=0){
            ObservableList<Planning> objectifList = planningListController.getPlanningItems(objPlan.getId_planning());
            for (Planning planning : objectifList) {
                itemController.setDataFieldForTheUpdate(planning);
System.out.println("eyy");
            }
        }
        itemController.getTheBmiCahrt(agee,weight,height);
        itemController.setIdObjectif(objPlan.getId_objectif());

        PaneAjouterPlanning.setTranslateX(PaneAjouterPlanning.getWidth());
        PaneAjouterPlanning.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), PaneAjouterPlanning);
        transition.setToX(0);
        transition.play();

        itemController.setPallningControler(this);
        PaneAjouterPlanning.getChildren().add(node);


    }
    void makePaneAddingInvisble(){
        PaneAjouterPlanning.setTranslateX(PaneAjouterPlanning.getWidth());
        PaneAjouterPlanning.setVisible(false);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), PaneAjouterPlanning);
        transition2.setToX(0);
        transition2.play();

        ListeObjectifVbox.setTranslateX(ListeObjectifVbox.getWidth());
        ListeObjectifVbox.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), ListeObjectifVbox);
        transition.setToX(0);
        transition.play();

    }

    void makePaneAddInvisible(){
        PaneRight.setTranslateX(PaneRight.getWidth());
        PaneRight.setVisible(false);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1), PaneRight);
        transition2.setToX(0);
        transition2.play();
    }
    @FXML
    private HBox MessagetHbox;


    void setMessage(Message Success){
        MessagetHbox.getChildren().add(Success);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> MessagetHbox.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }


    void warning2(){

        var success = new Message(
                "Info",
                Faker.instance().expression("Planning Deleted Successfully now it is back as an objectif "),
                new FontAwesomeIconView(FontAwesomeIcon.CHECK)

        );
        success.getStyleClass().add(Styles.ACCENT);
        MessagetHbox.getChildren().add(success);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> MessagetHbox.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    void setMessageSuccess(Message Success){
        MessagetHbox.getChildren().add(Success);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> MessagetHbox.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }


    void setMessageWarning(Message warning){
        MessagetHbox.getChildren().add(warning);
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> MessagetHbox.getChildren().clear());
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }
    @FXML
    private HBox ToogleSwitchHbix;
    @FXML
    private HBox RefreshHbox;

    @FXML
    private Pane PanePlansDone;

    @FXML
    private Pane PaneInProgress;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayListePlanning();
        displayListeObjectif();

        var toggle2 = new ToggleSwitch("Plans not completed");
        toggle2.selectedProperty().addListener((obs, old, val) -> toggle2.setText(val ? "Plans compeleted" : "Plans not completed"));
        toggle2.setOnMouseClicked(event -> {
            System.out.println("ToggleSwitch clicked or toggled");
            if (toggle2.isSelected()) {
                displayListePlanning();
                System.out.println("ToggleSwitch is on the plans");
                toggle2.setSelected(true);


            } else {
                  displayListeObjectif();
                   System.out.println("ToggleSwitch is back to all the objectif without Plans");
            }
        });
        toggle2.setLabelPosition(HorizontalDirection.RIGHT);
        toggle2.setSelected(false);
        ToogleSwitchHbix.getChildren().add(toggle2);



                                //******************rREFRESH //
        var toggle3 = new ToggleSwitch("Refresh");
        toggle3.selectedProperty().addListener((obs, old, val) -> toggle3.setText(val ? "Refreshed" : "Refresh"));
        toggle3.setOnMouseClicked(event -> {
            if (toggle3.isSelected()) {
                makePaneAddingInvisble();
                PaneRight.setVisible(false);
                TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1), PaneRight);
                transition2.setToX(0);
                transition2.play();
                displayListeObjectif();
                System.out.println("ToggleSwitch is on the plans");
            } else {
                displayListePlanning();
                System.out.println("ToggleSwitch is back to all the objectif without Plans");
            }
        });
        toggle3.setLabelPosition(HorizontalDirection.RIGHT);
        toggle3.setSelected(false);
        RefreshHbox.getChildren().add(toggle3);




        class Clock extends VBox {

            static final DateTimeFormatter DATE_FORMATTER =
                    DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy");
            static final DateTimeFormatter TIME_FORMATTER =
                    DateTimeFormatter.ofPattern("HH:mm:ss");

            public Clock() {
                var clockLbl = new Label(TIME_FORMATTER.format(
                        LocalTime.now(ZoneId.systemDefault()))
                );
                clockLbl.getStyleClass().add(Styles.TITLE_2);

                var dateLbl = new Label(DATE_FORMATTER.format(
                        LocalDate.now(ZoneId.systemDefault()))
                );

                // -fx-border-width: 0 0 0.5 0;
                // -fx-border-color: -color-border-default;
                //setStyle(style);
                setSpacing(10);
                getChildren().setAll(clockLbl, dateLbl);

                var t = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                            var time = LocalTime.now(ZoneId.systemDefault());
                            clockLbl.setText(TIME_FORMATTER.format(time));
                        }
                ));
                t.setCycleCount(Animation.INDEFINITE);
                t.playFromStart();
            }
        }
        var cal = new Calendar(LocalDate.now());
        cal.setTopNode(new Clock());
        cal.setShowWeekNumbers(true);
        CalendarVbox.getChildren().add(cal);
        PanePlansDone.setOnMouseEntered(event -> PanePlansDone.setStyle("-fx-background-color: lightblue;"));
        PanePlansDone.setOnMouseExited(event -> PanePlansDone.setStyle("-fx-background-color: lightgray;"));
        PaneInProgress.setOnMouseEntered(event -> PaneInProgress.setStyle("-fx-background-color: lightblue;"));
        PaneInProgress.setOnMouseExited(event -> PaneInProgress.setStyle("-fx-background-color: lightgray;"));

    }
    @FXML
    void VboxPlansDone(MouseEvent event) {
        displayListePlanning();
    }
    
    @FXML
    void VboxPlansInProgress(MouseEvent event) {
        displayListeObjectif();

    }




}
