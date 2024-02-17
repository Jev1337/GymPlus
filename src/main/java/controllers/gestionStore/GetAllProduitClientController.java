package controllers.gestionStore;

import entities.gestionStore.facture;
import entities.gestionStore.produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import services.gestionStore.FactureService;
import services.gestionStore.ProduitService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;


public class GetAllProduitClientController implements Initializable {

    @FXML
    private Button ConsulterPnaierFX;
    @FXML
    private ComboBox<String> boxCategorieFX;
    @FXML
    private ComboBox<String> boxMontantFX;
    @FXML
    private TextField searchFX;
    @FXML
    private HBox HBoxRecentlyAdded;
    @FXML
    private GridPane ProductContainer;
    @FXML
    private ScrollPane ScrollAllProduct;

    @FXML
    void ConsulterPanier(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/Panier.fxml"));  //Panier
            Parent root = loader.load();
            searchFX.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void boxCategorie(ActionEvent event)
    {
        String s = boxCategorieFX.getSelectionModel().getSelectedItem().toString();
    }

    @FXML
    void boxMontant(ActionEvent event)
    {
        String s = boxMontantFX.getSelectionModel().getSelectedItem().toString();
    }

    private final ProduitService prodService = new ProduitService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //comboBox categorie
        ObservableList<String> list_categorie = FXCollections.observableArrayList("Food" , "equipement" , "vetement");
        boxCategorieFX.setItems(list_categorie);

        //comboBox Montant
        ObservableList<String> list_Montant = FXCollections.observableArrayList("Price low to high" , "Price high to low" );
        boxMontantFX.setItems(list_Montant);

        //Afficher All Product
        try {
            // Récupérer toutes les produits
            ObservableList<produit> produits = FXCollections.observableArrayList(prodService.getAll());

            // Nombre de produits par ligne
            int produitsParLigne = 3;

            // Ajout des données dans la GridPane
            int row = 0;
            int col = 0;
            for (int i = 0; i < produits.size(); i++)
            {
                produit produit = produits.get(i);
                String photo = produit.getPhoto();
                String nom = produit.getName();
                float prix = produit.getPrix();

                Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + photo);
                ImageView imageView = new ImageView(imageP);
                Label labelPhoto = new Label();
                labelPhoto.setGraphic(imageView);

                //Label labelPhoto = new Label(imageP);
                Label labelNom = new Label("Nom: " + nom);
                Label labelPrix = new Label("Prix: " + prix);

                // Créer un VBox pour empiler verticalement les labels
                VBox vbox = new VBox(10); // Espacement de 10 pixels entre chaque produit
                vbox.getChildren().addAll(new VBox(new Label("Entité Produit " + (i + 1)), labelPhoto , labelNom, labelPrix));

                // Ajouter du padding pour créer un espace vertical entre les colonnes
                if (col > 0)
                {
                    Insets insets = new Insets(0, 10, 10, 0); //haut / droite / bas / gauche
                    ProductContainer.add(vbox, col, row);
                    GridPane.setMargin(vbox, insets);
                } else {
                    ProductContainer.add(vbox, col, row);
                }

                // Incrémenter les indices de colonne et de ligne
                col++;
                if (col == produitsParLigne)
                {
                    col = 0;
                    row++;
                }
            }

            // Ajouter de l'espace vertical entre les lignes
            ProductContainer.setVgap(10);

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
