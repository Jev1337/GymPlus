package controllers.gestionStore;

import entities.gestionStore.produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.gestionStore.ProduitService;

import static controllers.gestionStore.GlobalStore.Ipc;

public class AddProduitController implements Initializable {


    @FXML
    private ComboBox<String> Categorie_Combo;

    @FXML
    private TextArea DescriptionFX;

    @FXML
    private TextField NameFX;

    @FXML
    private TextField PrixFX;

    @FXML
    private TextField PromoFX;

    @FXML
    private TextField SeuilFX;

    @FXML
    private TextField StockFX;

    @FXML
    private Label PhotoPath;

    @FXML
    private Button ajouterFX;
    @FXML
    private Button ChoisirPhotoFX;


    FileChooser filePhoto = new FileChooser();
    @FXML
    void choisirPhoto(MouseEvent event)
    {
        File fileP = filePhoto.showOpenDialog(new Stage());
        //PhotoPath.setText(fileP.getPath().replace( "/" , "//")  + fileP.getName());
        PhotoPath.setText(fileP.getName());

    }

    @FXML
    void Select(ActionEvent event) {
        String s = Categorie_Combo.getSelectionModel().getSelectedItem().toString();
    }

    public void initialize(URL url , ResourceBundle rb )
    {
        //comboBox
        ObservableList<String> list_categorie = FXCollections.observableArrayList("Food" , "equipement" , "vetement");
        Categorie_Combo.setItems(list_categorie);

        //filePhoto
        filePhoto.setInitialDirectory(new File("D:\\projet_PI\\GymPlus\\imageProduit"));

    }

    private final ProduitService produitService = new ProduitService();

    @FXML
    void ajouter(ActionEvent event)
    {
        try {
            produitService.add(new produit( NameFX.getText(),Float.parseFloat(PrixFX.getText()), Integer.parseInt(StockFX.getText()), DescriptionFX.getText(), Categorie_Combo.getValue(), PhotoPath.getText(), Integer.parseInt(SeuilFX.getText()) , Float.parseFloat(PromoFX.getText())));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Produit ajouté avec succès");
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
        NameFX.setText("");
        PrixFX.setText("");
        StockFX.setText("");
        DescriptionFX.setText("");
        Categorie_Combo.setValue("Food");
        PhotoPath.setText("");
        SeuilFX.setText("");
        PromoFX.setText("");
    }

    @FXML
    void naviger(ActionEvent event)
    {
        Ipc.callPane("GetAllProduitClient.fxml");
    }


}
