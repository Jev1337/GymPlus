package controllers.gestionStore;

import entities.gestionStore.detailfacture;
import entities.gestionStore.produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import services.gestionStore.PanierService;
import services.gestionStore.ProduitService;

import java.util.ArrayList;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.gestionStore.GetAllProduitClientController.MonPanier;

public class PanierController implements Initializable
{

    @FXML
    private Label TotalPanierFX;
    @FXML
    private GridPane GridPanier;
    @FXML
    private Label nbArticleFX;
    @FXML
    private Label nomClientFX;
    @FXML
    private ComboBox<String> comboPaiement;



    //private final PanierService panierService;

    /*public PanierController(PanierService panierService)
    {
        MonPanier = panierService;
    }


    public PanierController() {
        this.panierService = new PanierService();
    }

     */



    public void Select(ActionEvent actionEvent)
    {
        String s = comboPaiement.getSelectionModel().getSelectedItem().toString();
    }
    @FXML
    void ContinuerAchat(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
            Parent root = loader.load();
            TotalPanierFX.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ValiderPanier(ActionEvent event)
    {
        MonPanier.getMonPanier().setMethodeDePaiement(comboPaiement.getValue());
        MonPanier.Valider();

        MonPanier = new PanierService();
        var idClient = javax.swing.JOptionPane.showInputDialog("What is your ID Client?");
        MonPanier.getMonPanier().setId(Integer.parseInt(idClient));

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetOneFacture.fxml"));
            Parent root = loader.load();
            TotalPanierFX.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        chargerContenuPanier();
        nbArticleFX.setText(String.valueOf( MonPanier.getMonPanier().ListeDetails.size()));
        nomClientFX.setText(String.valueOf(MonPanier.getMonPanier().getId()));
        TotalPanierFX.setText(String.valueOf(MonPanier.getMonPanier().calculerPrixTotalFacture()));

        //comboBox Paiement
        ObservableList<String> list_methodeP = FXCollections.observableArrayList("carte" , "cheque" , "espece" , "en ligne" );
        comboPaiement.setItems(list_methodeP);
    }

    public void chargerContenuPanier()
    {

        //Afficher All Product List Panier
        try {
            // Récupérer toutes les produits
            //ObservableList<produit> produits = FXCollections.observableArrayList(prodService.getAll());
            //MonPanier.getMonPanier();


            // Nombre de produits par ligne
            int produitsParLigne = 1;

            // Ajout des données dans la GridPane
            int row = 0;
            int col = 0;
            List<detailfacture> lp = MonPanier.getMonPanier().ListeDetails;
            for (int i = 0; i < MonPanier.getMonPanier().ListeDetails.size(); i++)
            {

                lp.get(i);

                ProduitService produitService = new ProduitService();
                produit p = produitService.getProduitById(lp.get(i).getIdProduit());

                String photo = p.getPhoto();
                String nom = p.getName();
                float prix = lp.get(i).getPrixVenteUnitaire();
                float prixtotalArticle = lp.get(i).getPrixtotalArticle();
                int quantite = lp.get(i).getQuantite();

                Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + photo);
                ImageView imageView = new ImageView(imageP);
                Label labelPhoto = new Label();
                labelPhoto.setGraphic(imageView);

                Label labelNom = new Label("Nom: " + nom);
                Label labelPrix = new Label("Prix: " + prix);
                Label labelPrixTotalArtile = new Label("Prix Total: " + prixtotalArticle);
                Button Supprimer = new Button("Supprimer");


                // Créer un VBox pour empiler verticalement les labels
                VBox vbox = new VBox(10); // Espacement de 10 pixels entre chaque produit
                vbox.getChildren().addAll(new VBox(new Label("Entité Produit " + (i + 1)), labelPhoto, labelNom, labelPrix , labelPrixTotalArtile , Supprimer));

                // Ajouter du padding pour créer un espace vertical entre les colonnes
                if (col > 0)
                {
                    Insets insets = new Insets(10, 10, 10, 0); //haut / droite / bas / gauche
                    GridPanier.add(vbox, col, row);
                    GridPane.setMargin(vbox, insets);
                } else
                {
                    GridPanier.add(vbox, col, row);
                }

                // Incrémenter les indices de colonne et de ligne
                col++;
                if (col == produitsParLigne) {
                    col = 0;
                    row++;
                }
            }

            // Ajouter de l'espace vertical entre les lignes
            GridPanier.setVgap(10);

        } catch (SQLException e)
        {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }

        /*
        System.out.println("charger contenu Panier => Panier controller");
        //List<detailfacture> panierList = MonPanier.getContenuPanier();

        for (detailfacture df : MonPanier.getMonPanier().ListeDetails)
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/resourcesGestionStore/OneproduitPanier.fxml"));
                VBox vBox = fxmlLoader.load();
                OneproduitPanierController pc = fxmlLoader.getController();
                pc.setProduit(df.getProduit());
                PanierVboxFx.getChildren().add(vBox);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

         */
    }

}
