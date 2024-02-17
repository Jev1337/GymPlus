package controllers.gestionStore;

import entities.gestionStore.facture;
import entities.gestionStore.produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.gestionStore.FactureService;
import services.gestionStore.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Get1ProduitController implements Initializable {

    @FXML
    private Button AddPanierFX;
    @FXML
    private Button QuittezFX;
    @FXML
    private ImageView ImgProduit;
    @FXML
    private Label NameFX;
    @FXML
    private Label descriptionFx;
    @FXML
    private Label priceFX;

    @FXML
    void AddPanier(ActionEvent event)
    {

    }

    @FXML
    void Quittez(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
            Parent root = loader.load();
            NameFX.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final ProduitService prodService = new ProduitService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            produit p = prodService.getOne(20);
            Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + p.getPhoto());
            ImgProduit.setImage(imageP);
            NameFX.setText(p.getName());
            priceFX.setText(String.valueOf(p.getPrix()));
            descriptionFx.setText(p.getDescription());

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }
}
