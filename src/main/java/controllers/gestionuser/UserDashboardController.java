package controllers.gestionuser;

import animatefx.animation.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class UserDashboardController {

    private FadeIn[] fadeInAnimation = new FadeIn[8];

    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();
    private FadeInRight fadeInRightAnimation = new FadeInRight();

    @FXML
    private ScrollPane UserHomePane;

    @FXML
    private ScrollPane UserInfoPane;

    @FXML
    private ScrollPane UserSubscriptionPane;
    @FXML
    private ScrollPane UserStorePane;

    @FXML
    private FontAwesomeIconView bars_btn;

    @FXML
    private Pane bars_pane;

    @FXML
    private Button blog_btn;

    @FXML
    private Button close_btn;

    @FXML
    private ImageView cover_imageview;

    @FXML
    private Pane dragpane;
    @FXML
    private Pane blogId = new Pane();

    @FXML
    private Button event_btn;

    @FXML
    private Button home_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private Button minimize_btn;

    @FXML
    private Button objective_btn;

    @FXML
    private Button settings_btn;

    @FXML
    private Button shop_btn;


    @FXML
    void settings_btn_clicked(MouseEvent event) {
        switchToPane(UserSettingsPane);
    }

    @FXML
    void home_btn_act(ActionEvent event) {
        switchToPane(UserHomePane);
    }

    @FXML
    void logout_btn_act(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/authInterface.fxml"));
            Parent root = loader.load();
            logout_btn.getScene().getWindow().setWidth(600);
            logout_btn.getScene().getWindow().setHeight(400);
            logout_btn.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void logout_btn_clicked(MouseEvent event) {
        logout_btn_act(null);
    }

    @FXML
    void settings_btn_act(ActionEvent event) {
        switchToPane(UserSettingsPane);
    }

    @FXML
    void shop_btn_act(ActionEvent event) {
        switchToPane(UserStorePane);
    }

    @FXML
    void subscription_btn_act(ActionEvent event) {
        switchToPane(UserSubscriptionPane);
    }
    @FXML
    void subscription_btn_clicked(MouseEvent event) {
        switchToPane(UserSubscriptionPane);
    }
    @FXML
    void shop_btn_clicked(MouseEvent event) {
        switchToPane(UserStorePane);
    }
    @FXML
    void home_btn_clicked(MouseEvent event) {
        switchToPane(UserHomePane);
    }
    @FXML
    void user_btn_clicked(MouseEvent event) {
        switchToPane(UserInfoPane);
    }
    @FXML
    void bars_btn_clicked(MouseEvent event) {
        bars_btn.setDisable(true);
        System.out.println(bars_pane.getPrefWidth());
        if (bars_pane.getPrefWidth() == 200) {
            invisibleAll();
            nullOpacityAll();
        } else {
            visibleAll();
            for (FadeIn fadeIn : fadeInAnimation)
                fadeIn.play();

        }
        Duration cycleDuration = Duration.millis(250);
        Timeline timeline = new Timeline(
                new KeyFrame(cycleDuration,
                        new KeyValue(bars_pane.prefWidthProperty(), bars_pane.getPrefWidth() == 200 ? 51 : 200, Interpolator.EASE_BOTH)
        ));
        timeline.play();
        timeline.setOnFinished(e -> bars_btn.setDisable(false));

    }

    @FXML
    private Button subscription_btn;

    @FXML
    private FontAwesomeIconView user_btn;

    @FXML
    private BarChart<String, Number> stat_barchart;

    @FXML
    private ComboBox<String> stat_combobox;

    @FXML
    private LineChart<String, Number> stat_linechart;

    @FXML
    private ScrollPane UserObjectivePane;

    @FXML
    private ScrollPane UserBlogPane;

    @FXML
    private ScrollPane UserEventPane;
    @FXML
    private ScrollPane UserSettingsPane;

    @FXML
    void blog_btn_act(ActionEvent event) {
        switchToPane(UserBlogPane);
    }

    @FXML
    void blog_btn_clicked(MouseEvent event) {
        switchToPane(UserBlogPane);
    }

    @FXML
    void event_btn_act(ActionEvent event) {
        switchToPane(UserEventPane);
    }

    @FXML
    void event_btn_clicked(MouseEvent event) {
        switchToPane(UserEventPane);
    }

    @FXML
    void objective_btn_act(ActionEvent event) {
        switchToPane(UserObjectivePane);
    }

    @FXML
    void objective_btn_clicked(MouseEvent event) {
        switchToPane(UserObjectivePane);
    }

    @FXML
    void close_btn_act(ActionEvent event) {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }
    @FXML
    void minimize_btn_act(ActionEvent event) {
        Stage stage = (Stage) minimize_btn.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void stat_combobox_act(ActionEvent event) {

    }
    private double xOffset = 0;
    private double yOffset = 0;
    public void initialize() {
        fadeInRightAnimation.setNode(UserHomePane);
        fadeInRightAnimation.play();
        stat_combobox.getItems().addAll(FXCollections.observableArrayList("Abonnements", "Clients", "Staff"));
        initProfile();
        initCharts();
        setFitToWidthAll();
        initAnimations();
        initDecoratedStage();
        try {
            Pane pane= FXMLLoader.load(getClass().getResource("/gestionBlog/Blog.fxml"));
            blogId.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDecoratedStage(){
        dragpane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        dragpane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
    }
    private void initProfile(){
        Rectangle clip = new Rectangle(cover_imageview.getFitWidth(), cover_imageview.getFitHeight());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        cover_imageview.setClip(clip);
    }

    private void setFitToWidthAll(){
        UserHomePane.setFitToWidth(true);
        UserInfoPane.setFitToWidth(true);
        UserObjectivePane.setFitToWidth(true);
        UserBlogPane.setFitToWidth(true);
        UserEventPane.setFitToWidth(true);
        UserSubscriptionPane.setFitToWidth(true);
        UserStorePane.setFitToWidth(true);
        UserSettingsPane.setFitToWidth(true);
    }
    private ScrollPane getCurrentPane(){
        if(UserHomePane.isVisible())
            return UserHomePane;
        if(UserInfoPane.isVisible())
            return UserInfoPane;
        if(UserObjectivePane.isVisible())
            return UserObjectivePane;
        if(UserBlogPane.isVisible())
            return UserBlogPane;
        if(UserEventPane.isVisible())
            return UserEventPane;
        if(UserSubscriptionPane.isVisible())
            return UserSubscriptionPane;
        if(UserStorePane.isVisible())
            return UserStorePane;
        if (UserSettingsPane.isVisible())
            return UserSettingsPane;
        return null;
    }

    private void switchToPane(Node node){
        if (getCurrentPane() == node)
            return;
        fadeOutRightAnimation.setNode(getCurrentPane());
        fadeOutRightAnimation.play();
        fadeOutRightAnimation.setOnFinished(e -> {
            getCurrentPane().setVisible(false);
            node.setOpacity(0);
            node.setVisible(true);
            fadeInRightAnimation.setNode(node);
            fadeInRightAnimation.play();
        });
    }

    private void initAnimations(){
        fadeInAnimation[0] = new FadeIn(home_btn);
        fadeInAnimation[1] = new FadeIn(settings_btn);
        fadeInAnimation[2] = new FadeIn(shop_btn);
        fadeInAnimation[3] = new FadeIn(logout_btn);
        fadeInAnimation[4] = new FadeIn(subscription_btn);
        fadeInAnimation[5] = new FadeIn(objective_btn);
        fadeInAnimation[6] = new FadeIn(event_btn);
        fadeInAnimation[7] = new FadeIn(blog_btn);
    }
    private void visibleAll(){
        home_btn.setVisible(true);
        settings_btn.setVisible(true);
        shop_btn.setVisible(true);
        logout_btn.setVisible(true);
        subscription_btn.setVisible(true);
        objective_btn.setVisible(true);
        event_btn.setVisible(true);
        blog_btn.setVisible(true);
    }

    private void invisibleAll(){
        home_btn.setVisible(false);
        settings_btn.setVisible(false);
        shop_btn.setVisible(false);
        logout_btn.setVisible(false);
        subscription_btn.setVisible(false);
        objective_btn.setVisible(false);
        event_btn.setVisible(false);
        blog_btn.setVisible(false);
    }
    private void nullOpacityAll(){
        home_btn.setOpacity(0);
        settings_btn.setOpacity(0);
        shop_btn.setOpacity(0);
        logout_btn.setOpacity(0);
        subscription_btn.setOpacity(0);
        objective_btn.setOpacity(0);
        event_btn.setOpacity(0);
        blog_btn.setOpacity(0);
    }


    private void initCharts(){
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Jan", 100));
        series.getData().add(new XYChart.Data<>("Feb", 200));
        series.getData().add(new XYChart.Data<>("Mar", 50));
        series.getData().add(new XYChart.Data<>("Apr", 75));
        series.getData().add(new XYChart.Data<>("May", 110));
        series.getData().add(new XYChart.Data<>("Jun", 300));
        series.getData().add(new XYChart.Data<>("Jul", 111));
        series.getData().add(new XYChart.Data<>("Aug", 30));
        series.getData().add(new XYChart.Data<>("Sep", 75));
        series.getData().add(new XYChart.Data<>("Oct", 55));
        series.getData().add(new XYChart.Data<>("Nov", 225));
        series.getData().add(new XYChart.Data<>("Dec", 99));
        series.setName("Lorem");
        stat_linechart.getData().add(series);

        XYChart.Series<String,Number> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>("Jan", 100));
        series2.getData().add(new XYChart.Data<>("Feb", 200));
        series2.getData().add(new XYChart.Data<>("Mar", 50));
        series2.getData().add(new XYChart.Data<>("Apr", 75));
        series2.getData().add(new XYChart.Data<>("May", 110));
        series2.getData().add(new XYChart.Data<>("Jun", 300));
        series2.getData().add(new XYChart.Data<>("Jul", 111));
        series2.getData().add(new XYChart.Data<>("Aug", 30));
        series2.getData().add(new XYChart.Data<>("Sep", 75));
        series2.getData().add(new XYChart.Data<>("Oct", 55));
        series2.getData().add(new XYChart.Data<>("Nov", 225));
        series2.getData().add(new XYChart.Data<>("Dec", 99));
        series2.setName("Ipsum");
        stat_barchart.getData().add(series2);
    }
}