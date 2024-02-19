package controllers.gestionStore;

import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import entities.gestionStore.produit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import services.gestionStore.PanierService;
import services.gestionStore.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class OneproduitPanierController implements Initializable
{
    @FXML
    private ImageView ImageFX;
    @FXML
    private Label NameFX;
    @FXML
    private Label PrixUnitaireFX;
    @FXML
    private Label TotalArticleFX;
    @FXML
    private Spinner<Integer> UpdateQteFX;
    @FXML
    private AnchorPane prodSelectedFX;
    private int idproduit;
    private SpinnerValueFactory<Integer> spin;
    private final ProduitService prodService = new ProduitService();

    private int productId;
    public void setProductId(int productId)
    {
        this.productId = productId;
        System.out.println("setProductId called with productId: " + productId);
    }

    public float prixTotal()
    {
        float prixUnitaire = Float.parseFloat(PrixUnitaireFX.getText());
        float totalArticle = prixUnitaire * spin.getValue();
        TotalArticleFX.setText(String.valueOf(totalArticle));
        return totalArticle;
    }
    public void setQte()
    {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        UpdateQteFX.setValueFactory(spin);
        // détecter les changements de valeur
        spin.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Mettre à jour le total de l'article lorsque la quantité change
            float t = prixTotal();
        });
    }

    public void setProduit(produit p)
    {
        detailfacture df = new detailfacture();
        idproduit = p.getIdProduit();
        System.out.println(p.getIdProduit());
        NameFX.setText(p.getName());
        PrixUnitaireFX.setText(String.valueOf(p.getPrix()));
        TotalArticleFX.setText(String.valueOf(df.getPrixtotalArticle()));
        Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + p.getPhoto());
        ImageFX.setImage(imageP);
    }

    @FXML
    void SupprimerArticle(ActionEvent event)
    {
        //Delete from list Panier tmp
    }

    public void loadProductDetails()
    {
        try {
            System.out.println("loadProductDetails called");

            produit p = prodService.getOne(productId);
            System.out.println("load Panierrrrrr One produit Panier " + productId);

            if (p != null)
            {
                NameFX.setText(p.getName());
                PrixUnitaireFX.setText(String.valueOf(p.getPrix()));
                Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + p.getPhoto());
                ImageFX.setImage(imageP);
                setQte();
                TotalArticleFX.setText(String.valueOf(prixTotal()));
            }

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        loadProductDetails();

        //get 1 produit
        /*
        try {
            produit p = prodService.getOne(15);
            NameFX.setText(p.getName());
            PrixUnitaireFX.setText(String.valueOf(p.getPrix()));
            Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + p.getPhoto());
            ImageFX.setImage(imageP);
            setQte();
            TotalArticleFX.setText(String.valueOf(prixTotal()));

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }

         */


    }

}
