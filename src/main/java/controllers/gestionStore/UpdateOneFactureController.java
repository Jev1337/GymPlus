package controllers.gestionStore;

import controllers.gestionuser.GlobalVar;
import entities.gestionStore.facture;
import entities.gestionblog.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import services.gestionStore.FactureService;
import services.gestonblog.PostServices;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateOneFactureController implements Initializable
{

    @FXML
    private ComboBox<String> comboPaiement;

    @FXML
    private Label idfacture;

    private int idUser;
    private final FactureService fs = new FactureService();
    private facture f = new facture();

    @FXML
    void ModifierFacture(ActionEvent event)
    {
        try
        {
            f.setMethodeDePaiement(comboPaiement.getValue());


            int idFacture = Integer.parseInt(idfacture.getText());
            String methodePaiement = comboPaiement.getValue();

            facture f = new facture(idFacture, methodePaiement , idUser);
            System.out.println(f.getIdFacture());

            fs.update(f);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("facture updated!");
            alert.show();
            //comboPaiement.setValue("carte");
        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void setFacture(int idFacture , int idUser)
    {
        idfacture.setText(Integer.toString(idFacture));
        this.idUser = idUser;
        /*
        if (selectedFacture != null)
        {
            comboPaiement.setValue(selectedFacture.getMethodeDePaiement());
        } else {
            // Si la facture est null aff des champs vide
            comboPaiement.setValue("carte");
        }

         */
    }

    public UpdateOneFactureController() { }

    /*
    public void getFactureData(facture selectedFacture)
    {
        if (selectedFacture != null)
        {
            comboPaiement.setValue(selectedFacture.getMethodeDePaiement());
        } else
        {
            // Si la facture est null aff des champs vide
            comboPaiement.setValue("carte");
        }
    }

     */

    @FXML
    void Select(ActionEvent event)
    {
        String s = comboPaiement.getSelectionModel().getSelectedItem().toString();
    }

    public void initialize(URL url , ResourceBundle rb )
    {
        //comboBox
        ObservableList<String> list_methodeP = FXCollections.observableArrayList("carte", "cheque", "espece", "en ligne");
        comboPaiement.setItems(list_methodeP);
    }
}
