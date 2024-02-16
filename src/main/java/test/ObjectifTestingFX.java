package test;

import atlantafx.base.theme.PrimerLight;
import com.github.javafaker.App;
import controllers.gestionsuivi.ObjectifController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ObjectifTestingFX extends Application {

    private static ObjectifTestingFX instance;
    private static Scene scene;
    private ObjectifController oController = new ObjectifController();


    public static void main(String[] args){

        launch();

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            instance = this;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Objectif1.fxml"));
            Parent root = loader.load();
            ObjectifController objectifController = loader.getController();
            objectifController.setStage(primaryStage);



            Scene scene = new Scene(root);




            primaryStage.setScene(scene);
            primaryStage.setTitle("Objectif");
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ObjectifTestingFX getInstance() {
        return instance;
    }
    public HostServices getApplicationHostServices() {
        return this.getHostServices();
    }


}