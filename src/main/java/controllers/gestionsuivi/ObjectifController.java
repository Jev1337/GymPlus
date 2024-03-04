package controllers.gestionsuivi;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.github.javafaker.Faker;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.gestionSuivi.Objectif;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.events.Event;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectifController  implements Initializable {

    @FXML
    private VBox caloriesPrompted;
    @FXML
    private VBox WarningVbox;


    @FXML
    public VBox pnl_scroll2;



    @FXML
    private VBox ExerciceCalorieVbox;

    @FXML
    private VBox CaloriesRequirmentVbox;
    private static Stage stage ;


    private ObjectifController objectifController ;



    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ObjectifController.stage = stage;
    }



    private void refreshNodes() throws IOException {
        Pane1.getChildren().clear();
        Pane1.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/AddingObjectif.fxml"));
        Node node = loader.load();
        Pane1.getChildren().add(node);


    }




   /* public void animation () {
        var rectangle = new Rectangle(100,100) ;
        rectangle.setFill(Color.RED);
        var animation= Animations.fadeIn(rectangle, Duration.seconds(1));
        animation.playFromStart();
        pnl_scroll2.getChildren().add(animation);
    }
    */


    @FXML
    private ScrollPane ScrollPanelExerciceGen;

    @FXML
    private ScrollPane ScrollAffichage;

    public void refreshNodeCaloriesFx() throws IOException {
        caloriesPrompted.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/CaloriesGenerator.fxml"));
        Node node = loader.load();
        caloriesPrompted.getChildren().add(node);



    }

    public  void refreshNodeExercicesGenerator() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/ExercicesGenerator.fxml"));
        Node node = loader.load();
        ScrollPanelExerciceGen.setContent(node);

    }

    public  void refreshBurnedCaloriesController() throws IOException {
        CaloriesRequirmentVbox.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/CaloriesRequirment.fxml"));
        Node node = loader.load();
        BurnedCaloriesController ChangePannels = loader.getController();
        ChangePannels.changePanel1();
        CaloriesRequirmentVbox.getChildren().add(node);

    }
    public  void refreshBurnedCaloriesControllerPan2() throws IOException {
        CaloriesRequirmentVbox.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/CaloriesRequirment.fxml"));
        Node node = loader.load();
        BurnedCaloriesController ChangePannels = loader.getController();
        ChangePannels.changePanel2();
        CaloriesRequirmentVbox.getChildren().add(node);

    }

    @FXML
    private Pane Pane1;

    @FXML
    private Button ButtonAdd;


    @FXML
    private Button burnedCalsButton;

    @FXML
    void SendSignalToAddfucntion(ActionEvent event) throws IOException {
        PaneMiediaPlayer.setVisible(false);
        ScrollBmi_MacrosCal.setVisible(false);
        ScrollPanelExerciceGen.setVisible(false);
        scrollpanWeb.setVisible(false);
        PaneChatBot.setVisible(false);
        pnl_scroll2.setVisible(true);
        ScrollAffichage.setVisible(true);
        RealTimePan.setVisible(false);
        PanePage1.setTranslateX(PanePage1.getWidth());
        PanePage1.setVisible(true);
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(0.5), PanePage1);
        transition3.setToX(0);
        transition3.play();

        Animations.wobble(ButtonAdd).playFromStart();
        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/gestionSuivi/AddingObjectif.fxml"));
        Node node3 = loader3.load();
        AddObjectifController addoj = loader3.getController();
        addoj.setObjectifController(this);
        Pane1.getChildren().clear();
        PaneMiediaPlayer.setVisible(false);
        //addoj.setToogleAddVisible();
        Pane1.getChildren().add(node3);
        Pane1.setTranslateX(-Pane1.getWidth());
        Pane1.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), Pane1);
        transition2.setToX(0);
        transition2.play();
        addoj.setToogleAddVisible();
    }
    @FXML
    private Pane ResearchPan;
    @FXML
    private HBox rechercheHbox;
    @FXML
    private Button JoinButton;




    public void refreshNodesListeItems() throws IOException {
        PaneMiediaPlayer.setVisible(false);
        Pane1.setVisible(true);
        pnl_scroll2.getChildren().clear();
        ObjectifListController objectifListController = new ObjectifListController();
        ObservableList<Objectif> objectifList = objectifListController.getObjectifStatusList();
        for (Objectif objecitf : objectifList){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/Item.fxml"));
                Node node = loader.load();
                ObjectifListController itemController = loader.getController();
                itemController.setObjectif(objecitf);
                itemController.buttonPanel();
                itemController.scrollButton(objecitf);
                pnl_scroll2.getChildren().add(node);
                FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/gestionSuivi/AddingObjectif.fxml"));
                Node node3 = loader3.load();
                AddObjectifController addoj = loader3.getController();
                itemController.setObjectifController(this);
                addoj.setObjectifController(this);

                VBox vBox =(VBox) node.lookup("#VboxToBlue") ;


                if (vBox !=null){
                    vBox.setOnMouseClicked(mouseEvent -> {
                        PaneChatBot.setVisible(false);
                        PaneMiediaPlayer.setVisible(false);
                        Pane1.getChildren().clear();
                        Pane1.getChildren().add(node3);
                        Pane1.setTranslateX(-Pane1.getWidth());
                        Pane1.setVisible(true);
                        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), Pane1);
                        transition2.setToX(0);
                        transition2.play();
                        addoj.sendingDataToForm(objecitf);

                    });
                }
            }catch (IOException ex){
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }


    @FXML
    private Pane PaneChatBot;


    private void refreshNodesChatBot() throws IOException {
        ScrollAffichage.setVisible(false);
        PanePage1.setVisible(true);
        PaneMiediaPlayer.setVisible(true);
       FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/gestionSuivi/MediaPLayer.fxml"));
        Node node2 = loader2.load();
        PaneMiediaPlayer.getChildren().add(node2);
        PaneMiediaPlayer.setTranslateX(-PaneMiediaPlayer.getWidth());
        PaneMiediaPlayer.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), PaneMiediaPlayer);
        transition.setToX(0);
        transition.play();


           FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/chatBot.fxml"));
            Node node = loader.load();
        PaneChatBot.setVisible(true);
        PaneChatBot.getChildren().add(node);

        }
    @FXML
    private Pane PaneObjectif;
    @FXML
    private Pane PaneGetYourExercices;
    @FXML
    private Pane PaneMiediaPlayer;

    @FXML
    private HBox goBackToexercices;


    @FXML
    private VBox vboxWeb;

    private void refreshNodesWeb() throws IOException {
        vboxWeb.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/WebView.fxml"));
            Node node = loader.load();

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/gestionSuivi/ExerciesFx.fxml"));
            Node nod2 = loader2.load();
            ExericesController exericesController = loader2.getController();
            exericesController.setExerciceControler(this);
            vboxWeb.getChildren().add(node);

        }catch (IOException ex){
            Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }


    void getWebViewTrue () throws IOException {
        goBackToexercices.setVisible(true);
        refreshNodesWeb();
        ScrollPanelExerciceGen.setVisible(false);
        scrollpanWeb.setVisible(true);
    }



    @FXML
    private HBox rechercheHbox2;
 @FXML
 private Button ButtonAssistaant;
    @FXML
    private Button DietPlanButton;
    @FXML
    private Button GetBmiButton;

    @FXML
    private Button GetExercicesButton;


    @FXML
    private Button GetSocialMediaButon;

    @FXML
    private Button GetMacros;


    @FXML
    private Button RealTimeTrackerButton;
@FXML
    private Pane StatPane;

    @FXML
    private Pane PanePage1;

    @FXML
    private Pane FoodPanel;


    @FXML
    private Pane RealTimePan;
    @FXML
    private ScrollPane ScrollPannelFood;

    @FXML
    private ScrollPane scrollpanWeb;
    @FXML
    private ScrollPane ScrollBmi_MacrosCal;
    @FXML
    private Separator seperateur;

    public void plrefreshpaneRealTimePan() throws IOException {
        RealTimePan.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/RealimeFx.fxml"));
        Node node = loader.load();
        RealTimePan.getChildren().add(node);
        RealTimePan.setTranslateX(-RealTimePan.getWidth());
        RealTimePan.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), RealTimePan);
        transition2.setToX(0);
        transition2.play();
    }

    public void refreshNodeMediaPlayer() throws IOException {

    }


    @FXML
    void ShakeButtonExercices(MouseEvent event) {
        Animations.wobble(GetExercicesButton).playFromStart();

        Animations.wobble(RealTimeTrackerButton).playFromStart();

    }

    @FXML
    void SahkerObjectifButtonss(MouseEvent event) {
        Animations.wobble(ButtonAdd).playFromStart();
        Animations.wobble(ButtonAssistaant).playFromStart();
    }

    @FXML
    void ShakeFoodButton(MouseEvent event) {

        Animations.wobble(DietPlanButton).playFromStart();

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       try {
            refreshNodes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        var normalBtn3 = new Button(null, new FontAwesomeIconView(FontAwesomeIcon.BACKWARD));
        normalBtn3.getStyleClass().addAll(Styles.BUTTON_CIRCLE);
        goBackToexercices.getChildren().add(normalBtn3);
        normalBtn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScrollPanelExerciceGen.setVisible(true);
                scrollpanWeb.setVisible(false);

            }
        });



        //Styling add button

        PaneObjectif.setOnMouseEntered(event -> PaneObjectif.setStyle("-fx-background-color: lightblue;"));
        PaneObjectif.setOnMouseExited(event -> PaneObjectif.setStyle("-fx-background-color: lightgray;"));
        PaneGetYourExercices.setOnMouseEntered(event -> PaneGetYourExercices.setStyle("-fx-background-color: lightblue;"));
        PaneGetYourExercices.setOnMouseExited(event -> PaneGetYourExercices.setStyle("-fx-background-color: lightgray;"));
        StatPane.setOnMouseEntered(event -> StatPane.setStyle("-fx-background-color: lightblue;"));
        StatPane.setOnMouseExited(event -> StatPane.setStyle("-fx-background-color: lightgray;"));
        FoodPanel.setOnMouseEntered(event -> FoodPanel.setStyle("-fx-background-color: lightblue;"));
        FoodPanel.setOnMouseExited(event -> FoodPanel.setStyle("-fx-background-color: lightgray;"));


        burnedCalsButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        burnedCalsButton.setMnemonicParsing(true);


        JoinButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        JoinButton.setMnemonicParsing(true);

        ButtonAdd.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        ButtonAdd.setMnemonicParsing(true);

        DietPlanButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        DietPlanButton.setMnemonicParsing(true);

        GetBmiButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        GetBmiButton.setMnemonicParsing(true);

        GetExercicesButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        GetExercicesButton.setMnemonicParsing(true);

        GetMacros.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        GetMacros.setMnemonicParsing(true);


        GetSocialMediaButon.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        GetSocialMediaButon.setMnemonicParsing(true);

        RealTimeTrackerButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        RealTimeTrackerButton.setMnemonicParsing(true);




        var normalBtn = new Button(null, new FontAwesomeIconView(FontAwesomeIcon.BACKWARD));
        normalBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE);
        rechercheHbox.getChildren().add(normalBtn);

        normalBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ResearchPan.setTranslateX(ResearchPan.getWidth());
                ResearchPan.setVisible(true);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), ResearchPan);
                transition.setToX(0);
                transition.play();

            }
        });



        var normalBtn2 = new Button(null, new FontAwesomeIconView(FontAwesomeIcon.BACKWARD));
        normalBtn2.getStyleClass().addAll(Styles.BUTTON_CIRCLE);
        rechercheHbox2.getChildren().add(normalBtn2);

        normalBtn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ResearchPan.setTranslateX(ResearchPan.getWidth());
                ResearchPan.setVisible(false);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), ResearchPan);
                transition.setToX(0);
                transition.play();

            }
        });

        // styling assitant button
       ButtonAssistaant.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        ButtonAssistaant.setMnemonicParsing(true);
        ButtonAssistaant.setOnAction((E) -> {
            Animations.wobble(ButtonAssistaant).playFromStart();
            try {
                RealTimePan.setVisible(false);
                scrollpanWeb.setVisible(false);
                Pane1.setVisible(false);
               ScrollBmi_MacrosCal.setVisible(false);
                ScrollPannelFood.setVisible(false);
               ScrollPanelExerciceGen.setVisible(false);
                PaneMiediaPlayer.setTranslateX(PaneMiediaPlayer.getWidth());
                PaneMiediaPlayer.setVisible(true);

                TranslateTransition transition3 = new TranslateTransition(Duration.seconds(0.5), PaneMiediaPlayer);
                transition3.setToX(0);
                transition3.play();

                refreshNodesChatBot();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        RealTimeTrackerButton.setOnAction((E) -> {
            Animations.wobble(RealTimeTrackerButton).playFromStart();
            try {
                scrollpanWeb.setVisible(false);
                PaneMiediaPlayer.setVisible(false);
                 PaneChatBot.setVisible(false);
                ScrollBmi_MacrosCal.setVisible(false);
                ScrollPannelFood.setVisible(false);
                ScrollPanelExerciceGen.setVisible(false);
                PanePage1.setTranslateX(PanePage1.getWidth());
                ScrollAffichage.setVisible(false);
                seperateur.setVisible(false);
                Pane1.setVisible(false);
                PanePage1.setTranslateX(-PanePage1.getWidth());
                PanePage1.setVisible(true);

                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), PanePage1);
                transition.setToX(0);
                transition.play();
                plrefreshpaneRealTimePan();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        //Action on Get Exercices GENERATOR

        GetExercicesButton.setOnAction((E) -> {
            Animations.wobble(GetExercicesButton).playFromStart();
            try {                PaneMiediaPlayer.setVisible(false);
                RealTimePan.setVisible(false);

                scrollpanWeb.setVisible(false);
                ScrollBmi_MacrosCal.setVisible(false);
                PanePage1.setVisible(false);
                Pane1.setVisible(false);
                ScrollPannelFood.setVisible(false);
                PaneChatBot.setVisible(false);
                ScrollPanelExerciceGen.setTranslateX(-ScrollPanelExerciceGen.getWidth());
                ScrollPanelExerciceGen.setVisible(true);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), ScrollPanelExerciceGen);
                transition.setToX(0);
                transition.play();
                refreshNodeExercicesGenerator();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        //Action on page google
        GetSocialMediaButon.setOnAction((E) -> {
            Animations.wobble(GetSocialMediaButon).playFromStart();
            try {                PaneMiediaPlayer.setVisible(false);
                RealTimePan.setVisible(false);

                ScrollBmi_MacrosCal.setVisible(false);

                PanePage1.setVisible(false);
                Pane1.setVisible(false);
                ScrollPannelFood.setVisible(false);
                ScrollPanelExerciceGen.setVisible(false);
                PaneChatBot.setVisible(false);
                scrollpanWeb.setTranslateX(-scrollpanWeb.getWidth());
                scrollpanWeb.setVisible(true);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), scrollpanWeb);
                transition.setToX(0);
                transition.play();
                refreshNodesWeb();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //Action lel BMI W MACROS CALCULATOR
        GetBmiButton.setOnAction((E) -> {
            Animations.wobble(GetBmiButton).playFromStart();
            try {                PaneMiediaPlayer.setVisible(false);
                RealTimePan.setVisible(false);

                scrollpanWeb.setVisible(false);
                PanePage1.setVisible(false);
                Pane1.setVisible(false);
                ScrollPannelFood.setVisible(false);
                ScrollPanelExerciceGen.setVisible(false);
                PaneChatBot.setVisible(false);
                ScrollBmi_MacrosCal.setTranslateX(-ScrollBmi_MacrosCal.getWidth());
                ScrollBmi_MacrosCal.setVisible(true);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), ScrollBmi_MacrosCal);
                transition.setToX(0);
                transition.play();
                refreshBurnedCaloriesController();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

//Macros Action Button
        GetMacros.setOnAction((E) -> {
            Animations.wobble(GetMacros).playFromStart();
            try {                PaneMiediaPlayer.setVisible(false);
                RealTimePan.setVisible(false);

                scrollpanWeb.setVisible(false);
                PaneChatBot.setVisible(false);
                PanePage1.setVisible(false);
                Pane1.setVisible(false);
                ScrollPannelFood.setVisible(false);
                ScrollPanelExerciceGen.setVisible(false);
                ScrollBmi_MacrosCal.setTranslateX(-ScrollBmi_MacrosCal.getWidth());
                ScrollBmi_MacrosCal.setVisible(true);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), ScrollBmi_MacrosCal);
                transition.setToX(0);
                transition.play();
                refreshBurnedCaloriesControllerPan2();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

//ACTION ON MEALS GENERATOR
        DietPlanButton.setOnAction((E) -> {
            Animations.wobble(DietPlanButton).playFromStart();
            try {                PaneMiediaPlayer.setVisible(false);
                RealTimePan.setVisible(false);

                scrollpanWeb.setVisible(false);
                PaneChatBot.setVisible(false);
                ScrollBmi_MacrosCal.setVisible(false);
                PanePage1.setVisible(false);
                Pane1.setVisible(false);
                ScrollPanelExerciceGen.setVisible(false);

                ScrollPannelFood.setTranslateX(-ScrollPannelFood.getWidth());
                ScrollPannelFood.setVisible(true);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), ScrollPannelFood);
                transition.setToX(0);
                transition.play();
                refreshNodeCaloriesFx();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //**************//





        try {
            refreshNodesListeItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }






    }


    @FXML
    void ShakeStatbuttons(MouseEvent event) {
        Animations.wobble(GetBmiButton).playFromStart();
        Animations.wobble(GetMacros).playFromStart();

    }



}
