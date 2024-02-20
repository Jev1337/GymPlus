package controllers.gestionStore;

import controllers.gestionuser.GlobalVar;
import entities.gestionStore.facture;
import entities.gestionStore.produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.gestionStore.FactureService;
import services.gestionStore.PanierService;
import services.gestionStore.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static controllers.gestionStore.GetAllProduitClientController.MonPanier;

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
    private Label TotalArticleFX;
    @FXML
    private Spinner<Integer> spinerQte;
    @FXML
    private Button DeleteProductFX;
    @FXML
    private Button UpdateProductFX;
    private int productId;
    private final ProduitService prodService;
    private SpinnerValueFactory<Integer> spin;


    produit p ;

    public Get1ProduitController(PanierService panierService)
    {
       // this.panierService = panierService;
        this.prodService = new ProduitService();

    }

    public Get1ProduitController()
    {
       // this.panierService = new PanierService();
        this.prodService = new ProduitService();
    }


    @FXML
    public  void AddPanier(ActionEvent event)
    {
        try
        {
            MonPanier.AjouterProduit(p.getIdProduit() , spinerQte.getValue() ,0.0F );
            System.out.println("button Add Panier get1ProduitController");
            MonPanier.getContenuPanier();

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit ajouté au panier avec succès.");

            //loadFXML("/resourcesGestionStore/OneproduitPanier.fxml" , 13);
            //loadFXML("/resourcesGestionStore/Panier.fxml" , 13);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/Panier.fxml"));
            Parent root = loader.load();
            NameFX.getScene().setRoot(root);

        } catch (Exception e)
        {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());


            // Gérer les exceptions liées à l'ajout du produit au panier
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du produit au panier.");
        }

        //try
        //{

            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/OneproduitPanier.fxml"));  //Panier.fxml"));
            Parent root = loader.load();
            NameFX.getScene().setRoot(root);

             */
       // } catch (IOException e)
      //  {
      //      throw new RuntimeException(e);
       // }

    }

    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*
    @FXML
    void AddPanier(ActionEvent event)
    {
        //PanierService PS = new PanierService();
        //PS.AjouterProduit(13,1,0.0F , 1);
        panierService.AjouterProduit(13,1,0.0F , 1);
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/Panier.fxml"));
            Parent root = loader.load();
            NameFX.getScene().setRoot(root);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

     */

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        if (!GlobalVar.getUser().getRole().equals("admin"))
        {
            DeleteProductFX.setVisible(false);
            DeleteProductFX.setManaged(false);

            UpdateProductFX.setVisible(false);
            UpdateProductFX.setManaged(false);

        }

        /*
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

         */

    }

    public void setIdProduit(int productId)
    {
        this.productId = productId;
        loadProductDetails();
    }

    private void loadProductDetails()
    {
        try
        {
            System.out.println("load get 1 Produit" + productId);
            p = prodService.getOne(productId);
            if (p != null)
            {
                Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + p.getPhoto());
                ImgProduit.setImage(imageP);
                NameFX.setText(p.getName());
                priceFX.setText(String.valueOf(p.getPrix()));
                descriptionFx.setText(p.getDescription());
                TotalArticleFX.setText(String.valueOf(p.getPrix()));
                setQte();
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

    public float prixTotal()
    {
        float prixUnitaire = Float.parseFloat(priceFX.getText());
        float totalArticle = prixUnitaire * spin.getValue();
        TotalArticleFX.setText(String.valueOf(totalArticle));
        return totalArticle;
    }
    public void setQte()
    {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        spinerQte.setValueFactory(spin);
        // détecter les changements de valeur
        spin.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Mettre à jour le total de l'article lorsque la quantité change
            float t = prixTotal();
        });
    }


    @FXML
    void DeleteProduct(ActionEvent event)
    {
            try {

                prodService.delete(productId);
                System.out.println();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Le produit a été supprimée avec succès.");
                alert.showAndWait();

                try
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
                    Parent root = loader.load();
                    NameFX.getScene().setRoot(root);
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }

            } catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de la suppression du produit");
                alert.setContentText("Une erreur s'est produite lors de la suppression du produit.");
                alert.showAndWait();
            }
    }
    @FXML
    void UpdateProductFX(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/UpdateProduit.fxml"));
            Parent root = loader.load();

            UpdateProduitController updateProduitController = loader.getController();
            updateProduitController.setIdProduit(productId);


            Scene scene = NameFX.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFXML(String s , int productId)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(s));
            Parent root = fxmlLoader.load();

            // Accéder au contrôleur du fichier FXML chargé
            OneproduitPanierController controller = fxmlLoader.getController();
            controller.setProductId(productId);
            System.out.println("loadFXML get 1 produit controller " + productId);

            // Passer l'ID du produit au contrôleur
            //controller.setIdProduit(productId);
            controller.loadProductDetails();

            NameFX.getScene().setRoot(root);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



}
