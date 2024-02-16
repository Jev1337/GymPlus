package controllers.gestionuser;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutRight;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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

public class StaffDashboardController {
    private FadeIn[] fadeInAnimation = new FadeIn[7];
    private FadeOutRight fadeOutRightAnimation = new FadeOutRight();
    private FadeInRight fadeInRightAnimation = new FadeInRight();

    @FXML
    private ScrollPane StaffEquipmentManagementPane;

    @FXML
    private ScrollPane StaffEventPane;

    @FXML
    private ScrollPane StaffHomePane;

    @FXML
    private ScrollPane StaffInfoPane;

    @FXML
    private ScrollPane StaffSettingsPane;

    @FXML
    private ScrollPane StaffStorePane;

    @FXML
    private ScrollPane StaffSubscriptionPane;

    @FXML
    private FontAwesomeIconView bars_btn;

    @FXML
    private Pane bars_pane;

    @FXML
    private Button close_btn;

    @FXML
    private ImageView cover_imageview;

    @FXML
    private Pane dragpane;

    @FXML
    private Button equipment_btn;

    @FXML
    private Button event_btn;

    @FXML
    private Button home_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private Button minimize_btn;

    @FXML
    private Button settings_btn;

    @FXML
    private Button shop_btn;

    @FXML
    private BarChart<String, Number> stat_barchart;

    @FXML
    private ComboBox<String> stat_combobox;

    @FXML
    private LineChart<String, Number> stat_linechart;

    @FXML
    private Button subscription_btn;

    @FXML
    private FontAwesomeIconView user_btn;

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
    void equipment_btn_act(ActionEvent event) {
        switchToPane(StaffEquipmentManagementPane);
    }

    @FXML
    void equipment_btn_clicked(MouseEvent event) {
        switchToPane(StaffEquipmentManagementPane);
    }

    @FXML
    void event_btn_act(ActionEvent event) {
        switchToPane(StaffEventPane);
    }

    @FXML
    void event_btn_clicked(MouseEvent event) {
        switchToPane(StaffEventPane);
    }

    @FXML
    void home_btn_act(ActionEvent event) {
        switchToPane(StaffHomePane);
    }

    @FXML
    void home_btn_clicked(MouseEvent event) {
        switchToPane(StaffHomePane);
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
        switchToPane(StaffSettingsPane);
    }

    @FXML
    void settings_btn_clicked(MouseEvent event) {
        switchToPane(StaffSettingsPane);
    }

    @FXML
    void shop_btn_act(ActionEvent event) {
        switchToPane(StaffStorePane);
    }

    @FXML
    void shop_btn_clicked(MouseEvent event) {
        switchToPane(StaffStorePane);
    }

    @FXML
    void stat_combobox_act(ActionEvent event) {

    }

    @FXML
    void subscription_btn_act(ActionEvent event) {
        switchToPane(StaffSubscriptionPane);
    }

    @FXML
    void subscription_btn_clicked(MouseEvent event) {
        switchToPane(StaffSubscriptionPane);
    }

    @FXML
    void user_btn_clicked(MouseEvent event) {
        switchToPane(StaffInfoPane);
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
        StaffHomePane.setFitToWidth(true);
        StaffInfoPane.setFitToWidth(true);
        StaffEventPane.setFitToWidth(true);
        StaffSubscriptionPane.setFitToWidth(true);
        StaffEquipmentManagementPane.setFitToWidth(true);
        StaffStorePane.setFitToWidth(true);
        StaffSettingsPane.setFitToWidth(true);
    }
    private ScrollPane getCurrentPane(){
        if (StaffHomePane.isVisible())
            return StaffHomePane;
        if (StaffInfoPane.isVisible())
            return StaffInfoPane;
        if (StaffEventPane.isVisible())
            return StaffEventPane;
        if (StaffSubscriptionPane.isVisible())
            return StaffSubscriptionPane;
        if (StaffEquipmentManagementPane.isVisible())
            return StaffEquipmentManagementPane;
        if (StaffStorePane.isVisible())
            return StaffStorePane;
        if (StaffSettingsPane.isVisible())
            return StaffSettingsPane;
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
        fadeInAnimation[5] = new FadeIn(event_btn);
        fadeInAnimation[6] = new FadeIn(equipment_btn);
    }
    private void visibleAll(){
        home_btn.setVisible(true);
        settings_btn.setVisible(true);
        shop_btn.setVisible(true);
        logout_btn.setVisible(true);
        subscription_btn.setVisible(true);
        event_btn.setVisible(true);
        equipment_btn.setVisible(true);
    }

    private void invisibleAll(){
        home_btn.setVisible(false);
        settings_btn.setVisible(false);
        shop_btn.setVisible(false);
        logout_btn.setVisible(false);
        subscription_btn.setVisible(false);
        event_btn.setVisible(false);
        equipment_btn.setVisible(false);
    }
    private void nullOpacityAll(){
        home_btn.setOpacity(0);
        settings_btn.setOpacity(0);
        shop_btn.setOpacity(0);
        logout_btn.setOpacity(0);
        subscription_btn.setOpacity(0);
        event_btn.setOpacity(0);
        equipment_btn.setOpacity(0);
    }

    private double xOffset = 0;
    private double yOffset = 0;
    public void initialize() {
        fadeInRightAnimation.setNode(StaffHomePane);
        fadeInRightAnimation.play();
        stat_combobox.getItems().addAll(FXCollections.observableArrayList("Abonnements", "Clients", "Staff"));
        initProfile();
        initCharts();
        setFitToWidthAll();
        initAnimations();
        initDecoratedStage();
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
