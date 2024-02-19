package test;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import nu.pattern.OpenCV;
import org.opencv.core.Core;

public class UserAuthMainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        System.load(System.getProperty("user.dir")+"\\opencv\\x64\\" + Core.NATIVE_LIBRARY_NAME + ".dll");
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionuser/authInterface.fxml")); //change me
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
