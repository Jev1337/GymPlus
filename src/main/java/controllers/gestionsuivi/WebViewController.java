package controllers.gestionsuivi;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {
    @FXML
    private WebView webView;

    private static WebEngine engine;

    @FXML
    private Button RefreshButton;

    @FXML
    private Button WebButon;


    @FXML
    private TextField WebTextField;

    private String homePage;

    public void loadPage() {

        engine.load("https://"+WebTextField.getText());
    }

    public void loadPageExercices(String url){
        engine.load(url);
    }

public void refreshPage(){
        engine.reload();

}
    @FXML
    private ScrollPane scrollpanWeb;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        engine = webView.getEngine();
        homePage= "www.google.com";
        WebTextField.setText(homePage);
       loadPage();

    }



}
