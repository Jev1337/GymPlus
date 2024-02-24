package controllers.gestionStore;

import entities.gestionStore.facture;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static controllers.gestionStore.GlobalStore.*;

public class InterfacePaneController implements Initializable
{
    @FXML
    private AnchorPane GestionStore;
    private String nomPane="GetAllProduitClient.fxml";

    public InterfacePaneController() {
        GestionStore = new AnchorPane();
    }

    public AnchorPane getGestionStore() {
        return GestionStore;
    }

    public void setGestionStore(AnchorPane gestionStore) {
        GestionStore = gestionStore;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        callPane(nomPane);
    }

    public void callPane(String s){
        try
        {
            System.out.println("callPane");
            Pane pane= FXMLLoader.load(getClass().getResource("/resourcesGestionStore/" + s));
            GestionStore.getChildren().setAll(pane);

            GestionStore.setVisible(true);

            stage.setScene(scene);
            stage.show();


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //get 1 Produit photo
    public void callPaneCharger(String s , int id){
        try
        {

            System.out.println("callPane id");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/" + s));
            Pane pane = loader.load();
            GestionStore.getChildren().setAll(pane);

            Get1ProduitController controller = loader.getController();
            controller.setIdProduit(id);
            GestionStore.setVisible(true);

            stage.setScene(scene);
            stage.show();


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //UpdateProduit
    public void callPaneUpdate(String s , int id){
        try
        {

            System.out.println("callPane id");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/" + s));
            Pane pane = loader.load();
            GestionStore.getChildren().setAll(pane);

            UpdateProduitController controller = loader.getController();
            controller.setIdProduit(id);
            GestionStore.setVisible(true);

            stage.setScene(scene);
            stage.show();


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //affichage 1 facture
    public void callPaneGet1Facture(String s , facture f){
        try
        {
            System.out.println("callPane 1 facture");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/" + s));
            Pane pane = loader.load();
            GestionStore.getChildren().setAll(pane);

            GetOneFactureController controller = loader.getController();
            controller.setFacture(f);

            GestionStore.setVisible(true);

            stage.setScene(scene);
            stage.show();


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}