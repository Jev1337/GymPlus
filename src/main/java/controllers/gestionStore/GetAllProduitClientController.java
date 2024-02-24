package controllers.gestionStore;

import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOut;
import animatefx.animation.FadeOutRight;
import controllers.gestionuser.GlobalVar;
import controllers.gestionuser.UserDashboardController;
import entities.gestionStore.produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import services.gestionStore.PanierService;
import services.gestionStore.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import javafx.scene.control.Label;

import javax.imageio.IIOParam;

import static controllers.gestionStore.GlobalStore.Ipc;


public class GetAllProduitClientController implements Initializable
{
    @FXML
    private Label AddProdFX;
    @FXML
    private Button ConsulterPnaierFX;
    @FXML
    private Button GetAllFactureBtn;
    @FXML
    private Label GetAllFactureLB;
    @FXML
    private HBox HBoxRecentlyAdded;
    @FXML
    private GridPane ProductContainer;
    @FXML
    private ScrollPane ScrollAllProduct;
    @FXML
    private Button ajouterProduitBtn;
    @FXML
    private ComboBox<String> boxCategorieFX;
    @FXML
    private ComboBox<String> boxMontantFX;
    @FXML
    private TextField searchFX;
    @FXML
    private Pane GetAllProduit;


    private final ProduitService prodService = new ProduitService();
    public static PanierService MonPanier;

    private InterfacePaneController interfacePaneController;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        if (MonPanier == null)
        {
            MonPanier = new PanierService();
            MonPanier.getMonPanier().setId(GlobalVar.getUser().getId());

            //var idClient = javax.swing.JOptionPane.showInputDialog("What is your ID Client?");
            //MonPanier.getMonPanier().setId(Integer.parseInt(idClient));

        }
        if (!GlobalVar.getUser().getRole().equals("admin"))
        {
            ajouterProduitBtn.setVisible(false);
            ajouterProduitBtn.setManaged(false);

            GetAllFactureBtn.setVisible(false);
            GetAllFactureBtn.setManaged(false);

            GetAllFactureLB.setVisible(false);
            GetAllFactureLB.setManaged(false);

            AddProdFX.setVisible(false);
            AddProdFX.setManaged(false);
        }


        //comboBox categorie
        ObservableList<String> list_categorie = FXCollections.observableArrayList("Food", "equipement", "vetement");
        boxCategorieFX.setItems(list_categorie);

        //comboBox Montant
        ObservableList<String> list_Montant = FXCollections.observableArrayList("Price low to high", "Price high to low");
        boxMontantFX.setItems(list_Montant);

        AfficherAllProduct();

    }


    public void AfficherAllProduct()
    {
        try {
            // Récupérer toutes les produits
            ObservableList<produit> produits = FXCollections.observableArrayList(prodService.getAll());

            // Nombre de produits par ligne
            int produitsParLigne = 4;

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
                vbox.getChildren().addAll(new VBox(new Label("Entité Produit " + (i + 1)), labelPhoto, labelNom, labelPrix));

                // Ajouter un gestionnaire d'événements pour ouvrir le fichier FXML spécifique lorsque vous cliquez sur la photo
                final int productId = produit.getIdProduit(); // Capturer l'ID du produit pour l'utiliser dans le gestionnaire d'événements
                labelPhoto.setOnMouseClicked(event -> {
                    loadFXML("/resourcesGestionStore/Get1Produit.fxml" , productId);

                    System.out.println("rania****" + productId);

                });

                // Ajouter du padding pour créer un espace vertical entre les colonnes
                if (col > 0)
                {
                    Insets insets = new Insets(0, 10, 10, 0); //haut / droite / bas / gauche
                    ProductContainer.add(vbox, col, row);
                    GridPane.setMargin(vbox, insets);
                } else
                {
                    ProductContainer.add(vbox, col, row);
                }

                // Incrémenter les indices de colonne et de ligne
                col++;
                if (col == produitsParLigne) {
                    col = 0;
                    row++;
                }
            }

            // Ajouter de l'espace vertical entre les lignes
            ProductContainer.setVgap(10);

        } catch (SQLException e)
        {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }


    private void loadFXML(String s , int productId)
    {
        //try
        //{
            Ipc.callPaneCharger("Get1Produit.fxml" , productId);
            /*

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/Get1Produit.fxml"));
            //Parent root = fxmlLoader.load();

            // Accéder au contrôleur du fichier FXML chargé
            Get1ProduitController controller = fxmlLoader.getController();

            // Passer l'ID du produit au contrôleur
            controller.setIdProduit(productId);

            //searchFX.getScene().setRoot(root);



        } catch (Exception e)
        {
            e.printStackTrace();
        }

             */
    }

    @FXML
    void GetAllFactureFX(ActionEvent event)
    {
        Ipc.callPane("GetAllFacture.fxml");
    }

    @FXML
    void ajouterProduitFX()
    {
        Ipc.callPane("AddProduit.fxml");
    }

    @FXML
    void ConsulterPanier(ActionEvent event)
    {
        Ipc.callPane("Panier.fxml");
    }

}
