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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import services.gestionStore.FactureService;
import services.gestionStore.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateFactureController implements Initializable {

    @FXML
    private Button ModifierFX;

    @FXML
    private ComboBox<String> comboPaiement;

    @FXML
    private TextField idClient;

    private final FactureService factureService = new FactureService();
    @FXML
    void ModifierFacture(ActionEvent event) {

        try {

            facture f = new facture(comboPaiement.getValue() , Integer.parseInt(idClient.getText()));
            f.setIdFacture(12);
            factureService.update(f);

            //factureService.update(new facture( comboPaiement.getValue() , Integer.parseInt(idClient.getText())));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Facture ajoutée avec succès");
            alert.showAndWait();

            //Naviger vers le produit MAJ
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllFacture.fxml"));
                Parent root = loader.load();
                idClient.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void Select(ActionEvent event) {
        String s = comboPaiement.getSelectionModel().getSelectedItem().toString();
    }

    private final FactureService factureServ = new FactureService();
    public void initialize(URL url , ResourceBundle rb )
    {
        //comboBox
        ObservableList<String> list_methodeP = FXCollections.observableArrayList("carte" , "cheque" , "espece" , "en ligne" );
        comboPaiement.setItems(list_methodeP);

        //get 1 facture
        try {
            facture p = factureServ.getOne(11);
            String methodeDePaiement = p.getMethodeDePaiement();
            comboPaiement.setValue(methodeDePaiement);
            idClient.setText(String.valueOf(p.getId()));

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
