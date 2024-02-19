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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.gestionStore.FactureService;
import services.gestionStore.ProduitService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateProduitController implements Initializable {


    @FXML
    private ComboBox<String> Categorie_Combo;

    @FXML
    private Button ChoisirPhotoFX;

    @FXML
    private TextArea DescriptionFX;

    @FXML
    private Button ModifierFX;

    @FXML
    private TextField NameFX;

    @FXML
    private Label PhotoPath;

    @FXML
    private TextField PrixFX;

    @FXML
    private TextField PromoFX;

    @FXML
    private TextField SeuilFX;

    @FXML
    private TextField StockFX;

    FileChooser filePhoto = new FileChooser();
    public void choisirPhoto(MouseEvent mouseEvent)
    {
        File fileP = filePhoto.showOpenDialog(new Stage());
        //PhotoPath.setText(fileP.getPath().replace( "/" , "//")  + fileP.getName());
        PhotoPath.setText(fileP.getName());
    }

    public void Select(ActionEvent actionEvent)
    {
        String s = Categorie_Combo.getSelectionModel().getSelectedItem().toString();
    }

    private final ProduitService produitService = new ProduitService();
    public void Modifier(ActionEvent actionEvent)
    {
        try {
            produit p3 = new produit(NameFX.getText(),Float.parseFloat(PrixFX.getText()), Integer.parseInt(StockFX.getText()), DescriptionFX.getText(), Categorie_Combo.getValue(), PhotoPath.getText(), Integer.parseInt(SeuilFX.getText()) , Float.parseFloat(PromoFX.getText()));
            p3.setIdProduit(15);
            produitService.update(p3);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Produit ajoutée avec succès");
            alert.showAndWait();

            //Naviger vers le produit MAJ
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourcesGestionStore/GetAllProduitClient.fxml"));
                Parent root = loader.load();
                NameFX.getScene().setRoot(root);
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

    private final ProduitService ProdService = new ProduitService();
    public void initialize(URL url , ResourceBundle rb )
    {
        //comboBox
        ObservableList<String> list_categorie = FXCollections.observableArrayList("Food" , "equipement" , "vetement");
        Categorie_Combo.setItems(list_categorie);

        //filePhoto
        filePhoto.setInitialDirectory(new File("D:\\projet_PI\\GymPlus\\imageProduit"));

        //get 1 produit
        try {
            produit p = ProdService.getOne(15);
            String categorie = p.getCategorie();
            Categorie_Combo.setValue(categorie);
            NameFX.setText(p.getName());
            PrixFX.setText(String.valueOf(p.getPrix()));
            StockFX.setText(String.valueOf(p.getStock()));
            SeuilFX.setText(String.valueOf(p.getSeuil()));
            DescriptionFX.setText(p.getDescription());
            PhotoPath.setText(p.getPhoto());
            PromoFX.setText(String.valueOf(p.getPromo()));


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
