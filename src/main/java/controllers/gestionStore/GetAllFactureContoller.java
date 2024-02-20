package controllers.gestionStore;

import controllers.BlogController.UpdatePostController;
import controllers.gestionuser.GlobalVar;
import entities.gestionStore.facture;
import entities.gestionblog.Post;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.gestionStore.FactureService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class GetAllFactureContoller implements Initializable
{

    @FXML
    private TableView<facture> GetAllFacture;
    @FXML
    private TableColumn<facture, Date> DateCol;
    @FXML
    private TableColumn<facture, Float> TotalCol;
    @FXML
    private TableColumn<facture, Integer> idClientCol;
    @FXML
    private TableColumn<facture, Integer> idFactureCol;
    @FXML
    private TableColumn<facture, String> MethodeP;
    @FXML
    private AnchorPane AnchorPaneGetAllF;
    private facture fc = new facture();
    private final FactureService factureService = new FactureService();


    public TableView.TableViewSelectionModel<facture> getSelectionModel() {
        return GetAllFacture.getSelectionModel();
    }

    @FXML
    void AffOneFacture(ActionEvent event)
    {
        facture selectedFacture = GetAllFacture.getSelectionModel().getSelectedItem();

        if (selectedFacture != null)
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetOneFacture.fxml"));
                Parent root = loader.load();

                GetOneFactureController controller = loader.getController();
                controller.setFacture(selectedFacture);

                AnchorPaneGetAllF.getChildren().setAll(root);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de l'affichage de la facture");
                alert.setContentText("Une erreur s'est produite lors de l'affichage de la facture sélectionnée.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une facture dans la TableView.");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteOneFacture(ActionEvent event)
    {

        facture selectedFacture = GetAllFacture.getSelectionModel().getSelectedItem();
        System.out.println("hidiii select loulaaa bech nchouff chnouwa id kdehh : " + selectedFacture.getIdFacture());


        if (selectedFacture != null)
        {
            try {

                factureService.delete(selectedFacture.getIdFacture());
                System.out.println(selectedFacture.getIdFacture());

                GetAllFacture.getItems().remove(selectedFacture);

                System.out.println("id facture selectionner " + selectedFacture);


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("La facture a été supprimée avec succès.");
                alert.showAndWait();
            } catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de la suppression de la facture");
                alert.setContentText("Une erreur s'est produite lors de la suppression de la facture.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une facture dans la TableView.");
            alert.showAndWait();
        }
    }

    @FXML
    void UpdateOneFacture(ActionEvent event)
    {
        facture selectedFacture = GetAllFacture.getSelectionModel().getSelectedItem();
        if (selectedFacture != null)
        {
            try {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/resourcesGestionStore/UpdateOneFacture.fxml"));
                AnchorPaneGetAllF = loader.load();
                UpdateOneFactureController upF = loader.getController();

                System.out.println("id facture selectionner " + selectedFacture);

                upF.setFacture(selectedFacture.getIdFacture() , GlobalVar.getUser().getId());

                ScaleTransition st = new ScaleTransition(Duration.millis(100), AnchorPaneGetAllF);
                st.setInterpolator(Interpolator.EASE_IN);
                st.setFromX(0);
                st.setFromY(0);
                st.setToX(1);
                st.setToY(1);
                Stage stage = new Stage();
                stage.setTitle("Update Post");
                Scene scene = new Scene(AnchorPaneGetAllF, 413, 190);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                scene.setFill(Color.TRANSPARENT);
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();

                //refreshTable();


            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        } else
        {
        showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une facture dans la TableView.");
        }
 /*
        facture selectedFacture = GetAllFacture.getSelectionModel().getSelectedItem();

        if (selectedFacture != null)
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/UpdateFacture.fxml"));
                Parent root = loader.load();

                UpdateFactureController controller = loader.getController();
                controller.setFacture(selectedFacture);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors du chargement de la vue de mise à jour de la facture", "Une erreur s'est produite lors du chargement de la vue de mise à jour de la facture.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une facture dans la TableView.");
        }

         */

    }

    @FXML
    void RefreshTable(ActionEvent event)
    {
        try
        {
            // Récupérer à nouveau toutes les factures
            ObservableList<facture> factures = FXCollections.observableArrayList(factureService.getAll());

            // Réassigner les données à la TableView
            GetAllFacture.setItems(factures);

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du rafraîchissement des factures");
            alert.setContentText("Une erreur s'est produite lors du rafraîchissement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }

    /*
    public void refreshTable() // sans button
    {
        try {
            // Récupérer à nouveau toutes les factures
            ObservableList<facture> factures = FXCollections.observableArrayList(factureService.getAll());

            // Réassigner les données à la TableView
            GetAllFacture.setItems(factures);

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du rafraîchissement des factures");
            alert.setContentText("Une erreur s'est produite lors du rafraîchissement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }

     */

    public facture getFacture()
    {
        fc.setMethodeDePaiement(MethodeP.getText());
        return fc;
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void Quitter(ActionEvent event) 
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
            Parent root = loader.load();
            Scene scene = GetAllFacture.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            // Récupérer toutes les factures
            ObservableList<facture> factures = FXCollections.observableArrayList(factureService.getAll());

            // Assigner les données à la TableView
            GetAllFacture.setItems(factures);

            // Définir les cellules de la TableView
            idFactureCol.setCellValueFactory(new PropertyValueFactory<>("idFacture"));
            idClientCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            DateCol.setCellValueFactory(new PropertyValueFactory<>("dateVente"));
            TotalCol.setCellValueFactory(new PropertyValueFactory<>("prixtotalPaye"));
            MethodeP.setCellValueFactory(new PropertyValueFactory<>("methodeDePaiement"));
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


}
