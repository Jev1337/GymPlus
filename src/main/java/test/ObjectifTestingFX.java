package test;

import atlantafx.base.theme.PrimerLight;
import controllers.gestionsuivi.ObjectifController;
import controllers.gestionsuivi.ObjectifListController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ObjectifTestingFX extends Application {

    private static ObjectifTestingFX instance;
 //   private static Scene scene;




    private ObjectifController oController = new ObjectifController();


    public static void main(String[] args){

        launch();

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
          //  instance = this;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionSuivi/objectif1.fxml"));
            Parent root = loader.load();
            ObjectifController objectifController = loader.getController();
            Scene scene = new Scene(root);


          //  objectifController.setStage(scene);
//léna badalt el el blassa taa el scene kenit taa7t el ObjectifController

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