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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
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



    private void refreshNodes()
    {
        Pane1.getChildren().clear();

        Node[] nodes = new Node[15];

        for(int i = 0; i<1; i++)
        {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/gestionSuivi/AddingObjectif.fxml"));
                Pane1.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }




   /* public void animation () {
        var rectangle = new Rectangle(100,100) ;
        rectangle.setFill(Color.RED);
        var animation= Animations.fadeIn(rectangle, Duration.seconds(1));
        animation.playFromStart();
        pnl_scroll2.getChildren().add(animation);
    }
    */




    public void refreshNodeCaloriesFx() throws IOException {
        caloriesPrompted.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/CaloriesGenerator.fxml"));
        Node node = loader.load();
        caloriesPrompted.getChildren().add(node);



        CaloriesGeneratorController caloriesGeneratorController = loader.getController();

    }

    public  void refreshNodeExercicesGenerator() throws IOException {
        ExerciceCalorieVbox.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/ExercicesGenerator.fxml"));
        Node node = loader.load();
        ExerciceCalorieVbox.getChildren().add(node);

    }

    public  void refreshBurnedCaloriesController() throws IOException {
        CaloriesRequirmentVbox.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/CaloriesRequirment.fxml"));
        Node node = loader.load();
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
        PaneChatBot.setVisible(false);
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
        PaneChatBot.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/chatBot.fxml"));
            Node node = loader.load();
        PaneChatBot.getChildren().add(node);
        PaneChatBot.setTranslateX(-PaneChatBot.getWidth());
        PaneChatBot.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), PaneChatBot);
        transition2.setToX(0);
        transition2.play();
            ChatControler chatControler = loader.getController() ;
        }
    @FXML
    private Pane PaneObjectif;
    @FXML
    private Pane PaneRealTimerProgress;
    @FXML
    private Pane PaneMiediaPlayer;




    @FXML
    private VBox vboxWeb;

    private void refreshNodesWeb()
    {
        vboxWeb.getChildren().clear();

        Node[] nodes = new Node[1];

        for(int i = 0; i<1; i++)
        {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/gestionSuivi/WebView.fxml"));
                vboxWeb.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(ObjectifController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }




    @FXML
    private HBox ScrollPageHbox;
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
    private Button GetMacrosButton;

    @FXML
    private Button GetStaticsCalories;
    @FXML
    private Button RealTimeTrackerButton;
@FXML
    private Pane DietPane;

    public void refreshNodeMediaPlayer() throws IOException {
        PaneMiediaPlayer.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/MediaPLayer.fxml"));
        Node node = loader.load();
        PaneMiediaPlayer.getChildren().add(node);
        PaneMiediaPlayer.setTranslateX(-PaneMiediaPlayer.getWidth());
        PaneMiediaPlayer.setVisible(true);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), PaneChatBot);
        transition2.setToX(0);
        transition2.play();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            refreshNodeMediaPlayer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Styling add button
        PaneObjectif.setOnMouseEntered(event -> PaneObjectif.setStyle("-fx-background-color: lightblue;"));
        PaneObjectif.setOnMouseExited(event -> PaneObjectif.setStyle("-fx-background-color: lightgray;"));
        PaneRealTimerProgress.setOnMouseEntered(event -> PaneRealTimerProgress.setStyle("-fx-background-color: lightblue;"));
        PaneRealTimerProgress.setOnMouseExited(event -> PaneRealTimerProgress.setStyle("-fx-background-color: lightgray;"));
        DietPane.setOnMouseEntered(event -> DietPane.setStyle("-fx-background-color: lightblue;"));
        DietPane.setOnMouseExited(event -> DietPane.setStyle("-fx-background-color: lightgray;"));


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


        GetMacrosButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        GetMacrosButton.setMnemonicParsing(true);

        GetStaticsCalories.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        GetStaticsCalories.setMnemonicParsing(true);

        RealTimeTrackerButton.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        RealTimeTrackerButton.setMnemonicParsing(true);


        var pg = new Pagination(8, 0);
        pg.setMaxPageIndicatorCount(8);
        pg.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pg.setPageFactory(index -> {
            var label = new Label("Page #" + (index + 1));
            label.setStyle("-fx-font-size: 2em;");
            return new BorderPane(label);
        });
        ScrollPageHbox.getChildren().add(pg);

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
                PaneMiediaPlayer.setVisible(false);
                Pane1.setTranslateX(-Pane1.getWidth());
                Pane1.setVisible(false);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), Pane1);
                transition.setToX(0);
                transition.play();

               refreshNodesChatBot();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        //**************//


    /*    try {
            refreshNodeExercicesGenerator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            refreshNodeCaloriesFx();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        refreshNodesWeb();
            refreshNodes();

        try {
            refreshNodesListeItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



       /* try {
            refreshBurnedCaloriesController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


    }


}
