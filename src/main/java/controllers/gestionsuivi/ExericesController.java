package controllers.gestionsuivi;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.io.IOException;

public class ExericesController {
    @FXML
    private Label ExerciceName;

    @FXML
    private Label beginnerIntensity;

    @FXML
    private Label explanation;

    @FXML
    private Label hardintensity;

    @FXML
    private Label intermediereIntensity;

    @FXML
    private Label EquipmntName;

    @FXML
    private Hyperlink VideoLink;


    private String url ;

    private  static  ObjectifController objectifController;
    public void setExerciceControler(ObjectifController objectifController) {
        ExericesController.objectifController = objectifController;
    }

    @FXML
    void goToYoutubeViaLink(MouseEvent event) throws IOException {
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/WebView.fxml"));
       // Node node = loader.load();
       // ScrollPane webPanel = (ScrollPane) node.lookup("#scrollpanWeb");
        objectifController.getWebViewTrue();
        WebViewController webViewController = new WebViewController();
        webViewController.loadPageExercices(url);
    }

    public  void FillFields(String workout,String Equipment,String intensityLevel,String IntermediateLevel,String ExpertLevel,String Explaination,String Video){
        ExerciceName.setText(workout);
        EquipmntName.setText(Equipment);
        beginnerIntensity.setText(intensityLevel);
        intermediereIntensity.setText(IntermediateLevel);
        hardintensity.setText(ExpertLevel);
        explanation.setText(Explaination);
        url=Video;

    }


}
