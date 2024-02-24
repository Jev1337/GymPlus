package controllers.gestionStore;

import controllers.gestionuser.GlobalVar;
import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import entities.gestionblog.Post;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import services.gestionStore.DetailFactureService;
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

    //private int idUser;
    private final FactureService fs = new FactureService();
    private facture f = new facture();

    @FXML
    void ModifierFacture(ActionEvent event)
    {
        try
        {
            f.setMethodeDePaiement(comboPaiement.getValue());

            String idFactureStr = idfacture.getText();

            int idFacture = 0; // Valeur par défaut en cas d'échec de la conversion

            try {
                idFacture = Integer.parseInt(idFactureStr);
            } catch (NumberFormatException e) {
                // Gérer le cas où la chaîne n'est pas un nombre valide
                e.printStackTrace(); // ou autre traitement approprié
            }

            //int idFacture = Integer.parseInt(idfacture.getText());
            String methodePaiement = comboPaiement.getValue();
            int idUser = GlobalVar.getUser().getId();

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
    private final DetailFactureService dfS = new DetailFactureService();

    public void setFacture(facture selectedFacture)
    {
        if (selectedFacture != null)
        {
            idfacture.setText(Integer.toString(selectedFacture.getIdFacture()));

            if (selectedFacture != null)
            {
                comboPaiement.setValue(selectedFacture.getMethodeDePaiement());
            } else
            {
                // Si la facture est null aff des champs vide
                comboPaiement.setValue("carte");
            }

            //comboPaiement.setItems(selectedFacture.getMethodeDePaiement().toString());
        }
    }

    public void setFacture(int idFacture )//, int idUser)
    {
        idfacture.setText(Integer.toString(idFacture));
        //this.idUser = idUser;
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
