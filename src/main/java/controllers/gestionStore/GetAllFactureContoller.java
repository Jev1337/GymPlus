package controllers.gestionStore;

import entities.gestionStore.facture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import services.gestionStore.FactureService;
import services.gestionStore.ProduitService;

import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

public class GetAllFactureContoller implements Initializable {

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
    void refrechFx(MouseEvent event)
    {

    }

    private final FactureService factureService = new FactureService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        try {
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
