package controllers.gestionsuivi;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
    @FXML
    void goToYoutubeViaLink(MouseEvent event) {
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
