package test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;
import javafx.application.Application;
import javafx.scene.layout.AnchorPane;

public class test_store_MainFX extends Application
{

    public void start(Stage primaryStage)throws  Exception
    {
        //FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/AddProduit.fxml"));
        //FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/AddFacture.fxml"));
        //FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/UpdateProduit.fxml"));
        //FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/UpdateFacture.fxml"));
        //FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllFacture.fxml"));
        FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
        //FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/Get1Produit.fxml"));
        //FXMLLoader loarder = new FXMLLoader(getClass().getResource("/resourcesGestionStore/Panier.fxml"));


        Parent root = loarder.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args){

        launch((args));
    }

}


