package controllers.gestionStore;

import entities.gestionStore.facture;
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
import services.gestionStore.FactureService;
import services.gestionStore.PanierService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddFactureController implements Initializable {

    @FXML
    private Button AjouterFX;

    @FXML
    private ComboBox<String> comboPaiement;

    @FXML
    private TextField idClient;

    private final PanierService panierService;

    public AddFactureController(PanierService panierService)
    {
        this.panierService = panierService;
    }


    public void Select(ActionEvent actionEvent) {

        String s = comboPaiement.getSelectionModel().getSelectedItem().toString();

    }

    public void initialize(URL url , ResourceBundle rb )
    {
        //comboBox
        ObservableList<String> list_methodeP = FXCollections.observableArrayList("carte" , "cheque" , "espece" , "en ligne" );
        comboPaiement.setItems(list_methodeP);

    }

    private final FactureService factureService = new FactureService();
    public void AjouterFacture(ActionEvent actionEvent)
    {

        try {
            factureService.add(new facture( comboPaiement.getValue() , Integer.parseInt(idClient.getText())));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Facture ajoutée avec succès");
            alert.showAndWait();

            refreshFields();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    void refreshFields()
    {
        idClient.setText("");
        comboPaiement.setValue("carte");
    }

    @FXML
    void naviguer(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllFacture.fxml"));
            Parent root = loader.load();
            idClient.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
