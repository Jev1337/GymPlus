package controllers.gestionStore;

import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.gestionStore.DetailFactureService;
import services.gestionStore.FactureService;
import services.gestionStore.PanierService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GetOneFactureController implements Initializable {

    @FXML
    private Label DateFX;
    @FXML
    private TableColumn<detailfacture, Float> PrixUniCol;
    @FXML
    private TableColumn<detailfacture, Integer> QteCol;
    @FXML
    private TableColumn<detailfacture, Float> RemiseCol;
    @FXML
    private TableColumn<detailfacture, Float> TotalArticleCol;
    @FXML
    private Label TotalFactureFX;
    @FXML
    private TableView<detailfacture> ViewDetailFacture;
    @FXML
    private Label idClientFX;
    @FXML
    private TableColumn<detailfacture, Integer> idDetailCol;
    @FXML
    private Label idFactureFX;
    @FXML
    private TableColumn<detailfacture, Integer> nameProdCol;
    private final DetailFactureService dfS = new DetailFactureService();
    private final PanierService panierService = new PanierService();


    @FXML
    void Quitter(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
            Parent root = loader.load();
            idFactureFX.getScene().setRoot(root);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void imprimer(ActionEvent event)
    {

    }

    public void setFacture(facture selectedFacture)
    {
        if (selectedFacture != null)
        {
            DateFX.setText(selectedFacture.getDateVente().toString());
            TotalFactureFX.setText(Float.toString(selectedFacture.getPrixtotalPaye()));
            idClientFX.setText(Integer.toString(selectedFacture.getId()));
            idFactureFX.setText(Integer.toString(selectedFacture.getIdFacture()));

            try {
                // Récupérer toutes les factures
                ObservableList<detailfacture> df = FXCollections.observableArrayList(dfS.getDetailFacture(selectedFacture.getIdFacture()));

                // Assigner les données à la TableView
                ViewDetailFacture.setItems(df);

                // Définir les cellules de la TableView
                idDetailCol.setCellValueFactory(new PropertyValueFactory<>("idDetailFacture"));
                nameProdCol.setCellValueFactory(new PropertyValueFactory<>("idProduit"));
                PrixUniCol.setCellValueFactory(new PropertyValueFactory<>("prixVenteUnitaire"));
                QteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
                RemiseCol.setCellValueFactory(new PropertyValueFactory<>("tauxRemise"));
                TotalArticleCol.setCellValueFactory(new PropertyValueFactory<>("prixTotalArticle"));


            } catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors du chargement des factures");
                alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
                alert.showAndWait();
            }
        } else
        {
            // Si la facture est null aff des champs vide
            DateFX.setText("");
            TotalFactureFX.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

}

