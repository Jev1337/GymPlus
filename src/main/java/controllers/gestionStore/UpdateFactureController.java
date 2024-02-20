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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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
    @FXML
    private Label idfacture;


    private final FactureService factureService = new FactureService();
    @FXML
    void ModifierFacture(ActionEvent event) {

        try {

            int idFacture = Integer.parseInt(idfacture.getText());
            String methodePaiement = comboPaiement.getValue();
            int idC = Integer.parseInt(idClient.getText());

            facture f = new facture(idFacture, methodePaiement, idC);

            factureService.update(f);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Facture ajoutée avec succès");
            alert.showAndWait();

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

    public void setFacture(facture selectedFacture)
    {
        if (selectedFacture != null)
        {
            idfacture.setText(Integer.toString(selectedFacture.getIdFacture()));
            comboPaiement.setValue(selectedFacture.getMethodeDePaiement());
            //idClient.setText(Integer.toString(selectedFacture.getId()));
            idClient.setText(String.valueOf(GlobalVar.getUser().getId()));
        } else {
            // Si la facture est null aff des champs vide
            idClient.setText("");
            comboPaiement.setValue("carte");
        }
    }
}
