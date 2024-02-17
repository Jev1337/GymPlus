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
    void ajouter(ActionEvent event) {
        try {
            produitService.add(new produit( NameFX.getText(),Float.parseFloat(PrixFX.getText()), Integer.parseInt(StockFX.getText()), DescriptionFX.getText(), Categorie_Combo.getValue(), PhotoPath.getText(), Integer.parseInt(SeuilFX.getText()) , Float.parseFloat(PromoFX.getText())));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Personne ajoutée avec succès");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void naviger(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
            Parent root = loader.load();
            NameFX.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
