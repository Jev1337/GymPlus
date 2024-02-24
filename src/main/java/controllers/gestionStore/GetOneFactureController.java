package controllers.gestionStore;

import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import services.gestionStore.DetailFactureService;
import services.gestionStore.FactureService;
import services.gestionStore.PanierService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static controllers.gestionStore.GlobalStore.Ipc;

public class GetOneFactureController implements Initializable
{

    @FXML
    private Label DateFX;
    @FXML
    private TableColumn<detailfacture, Float> PrixUniCol;
    @FXML
    private TableColumn<detailfacture, Integer> QteCol;
    @FXML
    private TableColumn<detailfacture, Float> RemiseCol;
    @FXML
    private TableColumn<detailfacture, Float> TotalArticleCol;
    @FXML
    private Label TotalFactureFX;
    @FXML
    private TableView<detailfacture> ViewDetailFacture;
    @FXML
    private Label idClientFX;
    @FXML
    private TableColumn<detailfacture, Integer> idDetailCol;
    @FXML
    private Label idFactureFX;
    @FXML
    private TableColumn<detailfacture, Integer> nameProdCol;
    private final DetailFactureService dfS = new DetailFactureService();
    private final PanierService panierService = new PanierService();


    @FXML
    void Quitter(ActionEvent event)
    {
        Ipc.callPane("GetAllFacture.fxml");
    }

    @FXML
    void imprimer(ActionEvent event)
    {
        // Créer un nouveau document PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Écrire les données de la facture dans le fichier PDF
            writeInvoiceDataToPDF(document);

            // Afficher la boîte de dialogue de sélection du fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la facture");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            // Afficher la boîte de dialogue et obtenir l'emplacement sélectionné
            Window window = ViewDetailFacture.getScene().getWindow();
            File file = fileChooser.showSaveDialog(window);

            if (file != null) {
                // Sauvegarder le fichier PDF à l'emplacement sélectionné
                document.save(file);

                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impression PDF");
                alert.setHeaderText(null);
                alert.setContentText("La facture a été enregistrée dans un fichier PDF.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs d'écriture du fichier PDF
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la génération du fichier PDF");
            alert.setContentText("Une erreur s'est produite lors de la création du fichier PDF.");
            alert.showAndWait();
        }
    }

    private void writeInvoiceDataToPDF(PDDocument document) throws IOException
    {
        // Charger l'image depuis le fichier
        //String imagePath = "../../assets/profileuploads/USERIMG11111111.png";

        //PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

        // Récupérer les données de la facture
        String date = DateFX.getText();
        String total = TotalFactureFX.getText();
        String idClient = idClientFX.getText();
        String idFacture = idFactureFX.getText();

        String idDetail = idDetailCol.getText();
        String idProduit = nameProdCol.getText();
        String PrixUnitaire = PrixUniCol.getText();
        String Quantite = QteCol.getText();
        String Remise = RemiseCol.getText();
        String PrixTotalArticle = TotalArticleCol.getText();


        try (PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(0)))
        {
            //image
            //contentStream.drawImage(image, 100, 750);

            contentStream.setNonStrokingColor(Color.decode("#0000FF"));
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(250, 700);
            contentStream.showText("Facture");
            contentStream.endText();

            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(70, 680);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Date: " + date);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Total Facture: " + total);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("ID Client: " + idClient);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("ID Facture: " + idFacture);
            contentStream.endText();


            contentStream.setNonStrokingColor(Color.decode("#0000FF"));
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(70, 580);
            contentStream.showText("Detail Facture");
            contentStream.endText();

/*
            float yPosition = 580;
            ObservableList<detailfacture> detailFactureList = FXCollections.observableArrayList(dfS.getDetailFacture(Integer.parseInt(idFactureFX.getText())));

            for (detailfacture detail : detailFactureList)
            {
                contentStream.beginText();
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(100, yPosition);
                contentStream.showText("ID Detail: " + detail.getIdDetailFacture());
                contentStream.newLineAtOffset(100, -20);
                contentStream.showText("ID Produit: " + detail.getIdProduit());
                contentStream.newLineAtOffset(100, -20);
                contentStream.showText("Prix unitaire: " + detail.getPrixVenteUnitaire());
                contentStream.newLineAtOffset(100, -20);
                contentStream.showText("Quantite: " + detail.getQuantite());
                contentStream.newLineAtOffset(100, -20);
                contentStream.showText("Remise: " + detail.getTauxRemise());
                contentStream.newLineAtOffset(100, -20);
                contentStream.showText("Prix Total Article: " + (detail.getPrixVenteUnitaire() * detail.getQuantite() * (1 - detail.getTauxRemise())));
                contentStream.endText();
                yPosition -= 120; // Ajuster l'espacement vertical entre les détails de la facture
            }
            */

            float xPosition = 70;
            float yPosition = 560;
            ObservableList<detailfacture> detailFactureList = FXCollections.observableArrayList(dfS.getDetailFacture(Integer.parseInt(idFactureFX.getText())));

            for (detailfacture detail : detailFactureList)
            {
                contentStream.beginText();
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText("ID Detail: " + detail.getIdDetailFacture());
                contentStream.newLineAtOffset(80, 0); // Espace horizontal entre chaque détail
                contentStream.showText("ID Produit: " + detail.getIdProduit());
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText("Prix unitaire: " + detail.getPrixVenteUnitaire());
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Quantite: " + detail.getQuantite());
                contentStream.newLineAtOffset(100, 0);
                //contentStream.showText("Remise: " + detail.getTauxRemise());
                //contentStream.newLineAtOffset(200, 0);
                contentStream.showText("Total Article: " + (detail.getPrixVenteUnitaire() * detail.getQuantite() * (1 - detail.getTauxRemise())));
                contentStream.endText();
                yPosition -= 20; // Ajuster l'espacement vertical entre les détails de la facture
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void setFacture(facture selectedFacture)
    {
        if (selectedFacture != null)
        {
            DateFX.setText(selectedFacture.getDateVente().toString());
            TotalFactureFX.setText(Float.toString(selectedFacture.getPrixtotalPaye()));
            idClientFX.setText(Integer.toString(selectedFacture.getId()));
            idFactureFX.setText(Integer.toString(selectedFacture.getIdFacture()));

            try {
                // Récupérer toutes les factures
                ObservableList<detailfacture> df = FXCollections.observableArrayList(dfS.getDetailFacture(selectedFacture.getIdFacture()));

                // Assigner les données à la TableView
                ViewDetailFacture.setItems(df);

                // Définir les cellules de la TableView
                idDetailCol.setCellValueFactory(new PropertyValueFactory<>("idDetailFacture"));
                nameProdCol.setCellValueFactory(new PropertyValueFactory<>("idProduit"));
                PrixUniCol.setCellValueFactory(new PropertyValueFactory<>("prixVenteUnitaire"));
                QteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
                RemiseCol.setCellValueFactory(new PropertyValueFactory<>("tauxRemise"));
                //TotalArticleCol.setCellValueFactory(new PropertyValueFactory<>("prixTotalArticle"));
                TotalArticleCol.setCellValueFactory(cellData -> {
                    Float prixVenteUnitaire = cellData.getValue().getPrixVenteUnitaire();
                    Integer quantite = cellData.getValue().getQuantite();
                    Float Remise = cellData.getValue().getTauxRemise();
                    Float totalArticle = prixVenteUnitaire * quantite* (1-Remise);
                    return new SimpleFloatProperty(totalArticle).asObject();
                });


            } catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors du chargement des factures");
                alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
                alert.showAndWait();
            }
        } else
        {
            // Si la facture est null aff des champs vide
            DateFX.setText("");
            TotalFactureFX.setText("");
        }
    }

/*
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

 */

    public FactureService fs;
    public DetailFactureService dfs;
    public void initialize(URL location, ResourceBundle resources)
    {
         fs= new FactureService();
         dfs= new DetailFactureService();

        try {
            facture derniereFacture = fs.getDerniereFacture();
            afficherFacture(derniereFacture);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void afficherFacture(facture f) throws SQLException
    {
        if (f != null)
        {
            DateFX.setText(f.getDateVente().toString());
            TotalFactureFX.setText(Float.toString(f.getPrixtotalPaye()));
            idClientFX.setText(Integer.toString(f.getId()));
            idFactureFX.setText(Integer.toString(f.getIdFacture()));


            //idFactureFX.setText(fs.getDerniereFacture().getIdFacture());
            //DateFX.setText(fs.getDerniereFacture().getDateVente());
            //TotalFactureFX.setText(f.getPrixtotalPaye());

            chargerDetailsFacture(f.getIdFacture());
        } /*else
        {
            // Gérer le cas où la facture est null
            idFactureFX.setText("videeeee");
            DateFX.setText("");
            TotalFactureFX.setText("");
            ViewDetailFacture.getItems().clear();
        }
        */
    }

    private void chargerDetailsFacture(int idFacture)
    {
        try {

            ObservableList<detailfacture> detailFactureList = FXCollections.observableArrayList(dfS.getDetailFacture(idFacture));

            ViewDetailFacture.setItems(detailFactureList);

            idDetailCol.setCellValueFactory(new PropertyValueFactory<>("idDetailFacture"));
            nameProdCol.setCellValueFactory(new PropertyValueFactory<>("idProduit"));
            PrixUniCol.setCellValueFactory(new PropertyValueFactory<>("prixVenteUnitaire"));
            QteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            RemiseCol.setCellValueFactory(new PropertyValueFactory<>("tauxRemise"));
            //TotalArticleCol.setCellValueFactory(new PropertyValueFactory<>("prixTotalArticle"));
            TotalArticleCol.setCellValueFactory(cellData -> {
                Float prixVenteUnitaire = cellData.getValue().getPrixVenteUnitaire();
                Integer quantite = cellData.getValue().getQuantite();
                Float totalArticle = prixVenteUnitaire * quantite;
                Float Remise = cellData.getValue().getTauxRemise();
                Float totalArticleD = prixVenteUnitaire * quantite* (1-Remise);
                return new SimpleFloatProperty(totalArticleD).asObject();
            });
        } catch (SQLException e)
        {
            e.printStackTrace();

        }
    }

}

