package controllers.gestionStore;

import controllers.gestionuser.GlobalVar;
import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import entities.gestionStore.produit;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONException;
import org.json.JSONObject;
import services.gestionStore.DetailFactureService;
import services.gestionStore.FactureService;
import services.gestionStore.PanierService;
import services.gestionStore.ProduitService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class InterfaceStoreController implements Initializable {

    //*****GetAllProduit
    @FXML
    private Label AddProdFX;
    @FXML
    private Pane AddProduit;
    @FXML
    private Button ConsulterPnaierFX;
    @FXML
    private Pane GetAllFacture;
    @FXML
    private Button GetAllFactureBtn;
    @FXML
    private Label GetAllFactureLB;
    @FXML
    private Pane GetAllProduit;
    @FXML
    private Pane GetOneFacture;
    @FXML
    private Pane GetOneProduit;
    @FXML
    private HBox HBoxRecentlyAdded;
    @FXML
    private Pane Panier;
    @FXML
    private GridPane ProductContainer;
    @FXML
    private ScrollPane ScrollAllProduct;
    @FXML
    private Pane UpdateProduit;
    @FXML
    private Button ajouterProduitBtn;
    @FXML
    private ComboBox<String> boxCategorieFX;
    @FXML
    private ComboBox<String> boxMontantFX;
    @FXML
    private TextField searchFX;

    //*****AddProduit
    @FXML
    private TextArea DescriptionFX;
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
    @FXML
    private Button ajouterFX;
    @FXML
    private ComboBox<String> Categorie_Combo;

    //*****GetOneProduit
    @FXML
    private Button AddPanierFX;
    @FXML
    private Button QuittezFX;
    @FXML
    private ImageView ImgProduit;
    @FXML
    private Label NameFX1;
    @FXML
    private Label descriptionFx;
    @FXML
    private Label priceFX;
    @FXML
    private Label TotalArticleFX;
    @FXML
    private Spinner<Integer> spinerQte;
    @FXML
    private Button DeleteProductFX;
    @FXML
    private Button UpdateProductFX;
    private int productId;
    private SpinnerValueFactory<Integer> spin;

    //*****Update Produit
    @FXML
    private ComboBox<String> Categorie_Combo1;
    @FXML
    private Button ChoisirPhotoFX1;
    @FXML
    private TextArea DescriptionFX1;
    @FXML
    private TextField NameFX2;
    @FXML
    private Label PhotoPath1;
    @FXML
    private TextField PrixFX1;
    @FXML
    private TextField PromoFX1;
    @FXML
    private TextField SeuilFX1;
    @FXML
    private TextField StockFX1;
    @FXML
    private Label descriptionFx1;

    //*****Panier
    @FXML
    private Label TotalPanierFX;
    @FXML
    private GridPane GridPanier;
    @FXML
    private Label nbArticleFX;
    @FXML
    private Label nomClientFX;
    @FXML
    private ComboBox<String> comboPaiement;


    //*****GetAllFacture
    @FXML
    private TableView<facture> GetAllFactureTable;
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

    //*****GetOneFacture
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


    //*****Var
    FileChooser filePhoto = new FileChooser();
    produit p ;
    public static PanierService MonPanier;
    private final ProduitService prodS;
    public InterfaceStoreController() {
        this.prodS = new ProduitService();
    }
    private facture fc = new facture();
    private final FactureService factureService = new FactureService();
    String pathPhoto = "file:///D:/projet_PI/GymPlus/src/assets/imageProduit/";


    //*****GetAllProduit

    @FXML
    void ConsulterPanier(ActionEvent event) {
        chargerContenuPanier();
        GetAllProduit.setVisible(false);
        Panier.setVisible(true);
    }
    @FXML
    void GetAllFactureFX(ActionEvent event) {
        GetAllProduit.setVisible(false);
        GetAllFacture.setVisible(true);
        chargerTableFacture();
    }

    @FXML
    void ajouterProduitFX(ActionEvent event) {
        GetAllProduit.setVisible(false);
        AddProduit.setVisible(true);
    }

    @FXML
    void boxCategorie(ActionEvent event)
    {
        String s = boxCategorieFX.getSelectionModel().getSelectedItem().toString();
    }

    @FXML
    void boxMontant(ActionEvent event)
    {
        String s = boxMontantFX.getSelectionModel().getSelectedItem().toString();
    }

    public void AfficherAllProduct()
    {
        try {
            ProduitService prodService = new ProduitService();

            ObservableList<produit> produits = FXCollections.observableArrayList(prodService.getAll());

            gridAff(produits);

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }

    private void filterProducts(String searchText) {
        try {
            if (searchText.isEmpty()) {
                ProductContainer.getChildren().clear();
                AfficherAllProduct();
                return;
            }
            ProduitService prodService = new ProduitService();

            ObservableList<produit> produits = FXCollections.observableArrayList(prodService.recupererByName(searchText));

            displayProducts(produits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void trierProduitsMontant(String triSelectionne) {
        if (triSelectionne != null) {
            try {
                ObservableList<produit> produits;
                ProduitService prodService = new ProduitService();
                if (triSelectionne.equals("Price low to high")) {
                    produits = FXCollections.observableArrayList(prodService.trierParPrixCroissant());
                } else {
                    produits = FXCollections.observableArrayList(prodService.trierParPrixDecroissant());
                }
                displayProducts(produits);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void trierProduitsCategorie(String triSelectionne) {
        if (triSelectionne != null) {
            try {
                ProduitService prodService = new ProduitService();
                ObservableList<produit> produits = FXCollections.observableArrayList(prodService.recupererByCategorie(triSelectionne));
                displayProducts(produits);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(triSelectionne.equals("All"))
            {
                AfficherAllProduct();
            }
        }
    }


    private void displayProducts(ObservableList<produit> produits) {

        ProductContainer.getChildren().clear();
        gridAff(produits);
    }

    public void gridAff(ObservableList<produit> produits)
    {
        // Nombre de produits par colonne
        int produitsParLigne = 4;

        // Ajout data dans GridPane
        int row = 0;
        int col = 0;
        for (int i = 0; i < produits.size(); i++)
        {
            produit produit = produits.get(i);
            String photo = produit.getPhoto();
            String nom = produit.getName();
            float prix = produit.getPrix();

            //Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + photo);
            //Image imageP = new Image("file:///D:/projet_PI/GymPlus/src/assets/imageProduit/" + photo);
            Image imageP = new Image(pathPhoto + photo);


            ImageView imageView = new ImageView(imageP);
            Label labelPhoto = new Label();
            labelPhoto.setGraphic(imageView);

            //Label labelPhoto = new Label(imageP);
            Label labelNom = new Label("Nom: " + nom);
            Label labelPrix = new Label("Prix: " + prix);

            // Créer un VBox pour empiler verticalement les labels
            VBox vbox = new VBox(10); // espace de 10 pixels entre chaque produit
            vbox.getChildren().addAll(new VBox(new Label("Entité Produit " + (i + 1)), labelPhoto, labelNom, labelPrix));

            // gestionnaire d'événements quand je clik sur photo
            final int productId = produit.getIdProduit();
            labelPhoto.setOnMouseClicked(event -> {
                GetAllProduit.setVisible(false);
                GetOneProduit.setVisible(true);

                loadProductDetails(productId);
                System.out.println("rania****" + productId);

            });

            // espace vertical entre les colonnes
            if (col > 0)
            {
                Insets insets = new Insets(0, 10, 10, 0); //haut / droite / bas / gauche
                ProductContainer.add(vbox, col, row);
                GridPane.setMargin(vbox, insets);
            } else
            {
                ProductContainer.add(vbox, col, row);
            }

            // Incrémenter nb colonne et ligne
            col++;
            if (col == produitsParLigne) {
                col = 0;
                row++;
            }
        }

        // espace vertical entre les lignes
        ProductContainer.setVgap(10);
    }





    //*****AddProduit
    @FXML
    void Select(ActionEvent event) {
        String s = Categorie_Combo.getSelectionModel().getSelectedItem().toString();
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            ProduitService prodService = new ProduitService();

            prodService.add(new produit( NameFX.getText(),Float.parseFloat(PrixFX.getText()), Integer.parseInt(StockFX.getText()), DescriptionFX.getText(), Categorie_Combo.getValue(), PhotoPath.getText(), Integer.parseInt(SeuilFX.getText()) , Float.parseFloat(PromoFX.getText())));
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
    void choisirPhoto(MouseEvent event) {
        File fileP = filePhoto.showOpenDialog(new Stage());
        //PhotoPath.setText(fileP.getPath().replace( "/" , "//")  + fileP.getName());
        PhotoPath.setText(fileP.getName());
    }

    @FXML
    void naviger(ActionEvent event) {
        AddProduit.setVisible(false);
        GetAllProduit.setVisible(true);
        refreshProductList();
    }

    public void refreshProductList() {
        ProductContainer.getChildren().clear();
        filterProducts(searchFX.getText());
        trierProduitsMontant(boxMontantFX.getValue());
        trierProduitsCategorie(boxCategorieFX.getValue());
        AfficherAllProduct();
    }



    //*****GetOneProduit

    @FXML
    public  void AddPanier(ActionEvent event)
    {
        try
        {
            MonPanier.AjouterProduit(p.getIdProduit() , spinerQte.getValue() ,0.0F );
            System.out.println("button Add Panier get1ProduitController");
            MonPanier.getContenuPanier();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Produit ajouté au panier avec succès.");

            chargerContenuPanier();

            GetOneProduit.setVisible(false);
            Panier.setVisible(true);

        } catch (Exception e)
        {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());

            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du produit au panier.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Quittez(ActionEvent event)
    {
        GetOneProduit.setVisible(false);
        GetAllProduit.setVisible(true);
        refreshProductList();
    }

    public void setIdProduit(int productId)
    {
        this.productId = productId;
        loadProductDetails(productId);
    }

    private int selectedProductId;

    private void loadProductDetails( int id )
    {
        try
        {
            System.out.println("load get 1 Produit : " + id);
            p = prodS.getOne(id);
            if (p != null)
            {
                //Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + p.getPhoto());//../assets/imageProduit/
                ImgProduit.setImage(new Image(new File("src/assets/imageProduit/" +p.getPhoto()).toURI().toString()));

                //ImgProduit.setImage(imageP);
                NameFX1.setText(p.getName());
                priceFX.setText(String.valueOf(p.getPrix()));
                descriptionFx.setText(p.getDescription());
                TotalArticleFX.setText(String.valueOf(p.getPrix()));
                setQte();

                selectedProductId = id;

            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }

    public float prixTotal()
    {
        float prixUnitaire = Float.parseFloat(priceFX.getText());
        float totalArticle = prixUnitaire * spin.getValue();
        TotalArticleFX.setText(String.valueOf(totalArticle));
        return totalArticle;
    }
    public void setQte()
    {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        spinerQte.setValueFactory(spin);
        // détecter les changements de valeur
        spin.valueProperty().addListener((observable, oldValue, newValue) -> {
            // MAJ le total article
            float t = prixTotal();
        });
    }

    @FXML
    void DeleteProduct(ActionEvent event)
    {
        try {
            ProduitService prodService = new ProduitService();
            System.out.println("prod delete " + selectedProductId);
            prodService.delete(selectedProductId);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Le produit a été supprimée avec succès.");
            alert.showAndWait();

            GetOneProduit.setVisible(false);
            GetAllProduit.setVisible(true);

            refreshProductList();

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la suppression du produit");
            alert.setContentText("Une erreur s'est produite lors de la suppression du produit.");
            alert.showAndWait();
        }
    }
    @FXML
    void UpdateProductFX(ActionEvent event)
    {
        GetOneProduit.setVisible(false);
        UpdateProduit.setVisible(true);
        ChargerProductDetails();
    }


    //*****Update Produit
    @FXML
    void choisirPhotoUpdate(MouseEvent event) {
        File fileP = filePhoto.showOpenDialog(new Stage());
        //PhotoPath.setText(fileP.getPath().replace( "/" , "//")  + fileP.getName());
        PhotoPath.setText(fileP.getName());
    }
    @FXML
    void combo_update(ActionEvent event) {
        String s = Categorie_Combo1.getSelectionModel().getSelectedItem().toString();
    }

    public void setIdProduitUpdate(int productId)
    {
        this.productId = productId;
        ChargerProductDetails();
    }

    private void ChargerProductDetails()
    {
        try {
            ProduitService produitService = new ProduitService();

            System.out.println("a mod " + selectedProductId);
            produit p = produitService.getOne(selectedProductId);

            String categorie = p.getCategorie();
            Categorie_Combo1.setValue(categorie);
            NameFX2.setText(p.getName());
            PrixFX1.setText(String.valueOf(p.getPrix()));
            StockFX1.setText(String.valueOf(p.getStock()));
            SeuilFX1.setText(String.valueOf(p.getSeuil()));
            descriptionFx1.setText(p.getDescription());
            PhotoPath1.setText(p.getPhoto());
            PromoFX1.setText(String.valueOf(p.getPromo()));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des détails du produit");
            alert.setContentText("Une erreur s'est produite lors du chargement des détails du produit.");
            alert.showAndWait();
        }
    }

    public void Modifier(ActionEvent actionEvent)
    {
        try {
//            setIdProduitUpdate(selectedProductId);

            ProduitService produitService = new ProduitService();
            String name = NameFX2.getText();
            float prix = Float.parseFloat(PrixFX1.getText());
            int stock = Integer.parseInt(StockFX1.getText());
            String description = descriptionFx1.getText();
            String categorie = Categorie_Combo1.getValue();
            String photo = PhotoPath1.getText();
            int seuil = Integer.parseInt(SeuilFX1.getText());
            float promo = Float.parseFloat(PromoFX1.getText());

            produit updatedProduct = new produit(selectedProductId , name, prix, stock, description, categorie, photo, seuil, promo);


            produitService.update(updatedProduct);
            UpdateProduit.setVisible(false);
            GetAllProduit.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Produit modifie avec succes");
            alert.showAndWait();
            refreshProductList();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    //*****Panier
    @FXML
    void selectPaiement(ActionEvent event) {
        String s = comboPaiement.getSelectionModel().getSelectedItem().toString();
    }
    @FXML
    void ContinuerAchat(ActionEvent event)
    {
        Panier.setVisible(false);
        GetAllProduit.setVisible(true);
    }
    @FXML
    void ValiderPanier(ActionEvent event)
    {
        MonPanier.getMonPanier().setMethodeDePaiement(comboPaiement.getValue());
        MonPanier.Valider();

        MonPanier = new PanierService();

        MonPanier.getMonPanier().setId(GlobalVar.getUser().getId());

        Panier.setVisible(false);
        GetOneFacture.setVisible(true);

        try
        {
            facture derniereFacture = factureService.getDerniereFacture();
            afficherFacture(derniereFacture);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private WebView WebViewMap;
    @FXML
    private Label villeLabel;

    private static String getCityName(double latitude, double longitude) {
        String cityName = "Unknown";

        try {
            String urlStr = "https://nominatim.openstreetmap.org/reverse?lat=" + latitude + "&lon=" + longitude + "&format=json";
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("address")) {
                JSONObject addressObject = jsonResponse.getJSONObject("address");
                if (addressObject.has("city")) {
                    cityName = addressObject.getString("city");
                } else if (addressObject.has("town")) {
                    cityName = addressObject.getString("town");
                } else if (addressObject.has("village")) {
                    cityName = addressObject.getString("village");
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return cityName;
    }


    public void chargerMap()
    {
        WebEngine webEngine = WebViewMap.getEngine();
        webEngine.loadContent("<html><head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Map</title>"
                + "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet/dist/leaflet.css\"/>"
                + "<style>#map { height: 100%; width: 100%; } body, html { height: 100%; margin: 0; padding: 0; }</style>"
                + "</head>"
                + "<body>"
                + "<div id=\"map\"></div>"
                + "<script src=\"https://unpkg.com/leaflet/dist/leaflet.js\"></script>"
                + "<script> var map = L.map('map').setView([51.505, -0.09], 13);"
                + "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors' }).addTo(map);"
                + "var marker; map.on('click', function (e) { if (marker) { map.removeLayer(marker); } marker = L.marker(e.latlng).addTo(map); "
                + "document.getElementById('latitude').value = e.latlng.lat;"
                + "document.getElementById('longitude').value = e.latlng.lng;});"
                + "</script>"
                + "</body></html>");
    }

    @FXML
    void selectville(MouseEvent event) {
        // Récupérez les coordonnées du clic de la souris
        double x = event.getX();
        double y = event.getY();

        // Convertissez les coordonnées de la souris en latitude et longitude (vous devrez adapter cela en fonction de votre carte)
        double latitude = convertirXenLatitude(x , 456);
        double longitude = convertirYenLongitude(y , 416);

        // Faites ce que vous voulez avec les coordonnées (par exemple, affichez-les dans une étiquette)
        //System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);

        //villeLabel.setText("Latitude: " + latitude + ", Longitude: " + longitude);
        String a = getCityName(latitude , longitude);
        //System.out.println("a : " + a );
        villeLabel.setText("Latitude: " + latitude + ", Longitude: " + longitude + "lieu :" + a);

    }

    // Méthode pour convertir les coordonnées X en latitude
    private double convertirXenLatitude(double x, int mapHeight) {
        // Adapter la latitude en fonction de la taille de la carte en pixels
        // Utilisez une conversion inverse pour une meilleure précision
        double lat = ((x / mapHeight) * 180.0) - 90.0;
        return lat;
    }

    // Méthode pour convertir les coordonnées Y en longitude
    private double convertirYenLongitude(double y, int mapWidth) {
        // Adapter la longitude en fonction de la taille de la carte en pixels
        double lon = ((y / mapWidth) * 360.0) - 180.0;
        return lon;
    }




    public void chargerContenuPanier()
    {
        nbArticleFX.setText(String.valueOf( MonPanier.getMonPanier().ListeDetails.size()));
        nomClientFX.setText(String.valueOf(MonPanier.getMonPanier().getId()));
        TotalPanierFX.setText(String.valueOf(MonPanier.getMonPanier().calculerPrixTotalFacture()));

        chargerMap();

        try {

            //rafrechirrr Gridpane
            GridPanier.getChildren().clear();

            int produitsParLigne = 1;

            // Ajout data dans GridPane
            int row = 0;
            int col = 0;
            List<detailfacture> lp = MonPanier.getMonPanier().ListeDetails;

            for (int i = 0; i < MonPanier.getMonPanier().ListeDetails.size(); i++)
            {
                lp.get(i);

                ProduitService produitService = new ProduitService();
                produit p = produitService.getProduitById(lp.get(i).getIdProduit());

                String photo = p.getPhoto();
                String nom = p.getName();
                float prix = lp.get(i).getPrixVenteUnitaire();
                float prixtotalArticle = lp.get(i).getPrixtotalArticle();
                int quantite = lp.get(i).getQuantite();

                //Image imageP = new Image("file:///D:/projet_PI/GymPlus/imageProduit/" + photo);
                Image imageP = new Image(pathPhoto + photo);

                ImageView imageView = new ImageView(imageP);

                Label labelPhoto = new Label();
                labelPhoto.setGraphic(imageView);

                Label labelNom = new Label("Nom: " + nom);
                Label labelPrix = new Label("Prix: " + prix);
                Label labelPrixTotalArtile = new Label("Prix Total: " + prixtotalArticle);
                Button Supprimer = new Button("Supprimer"); //RetirerProduit()

                int index = i;
                Supprimer.setOnAction(event -> {
                    //System.out.println("Indice à supprimer : " + index);
                    //System.out.println("Taille de la liste avant supp : " + MonPanier.getMonPanier().ListeDetails.size());
                    MonPanier.RetirerProduit(index);
                    nbArticleFX.setText(String.valueOf( MonPanier.getMonPanier().ListeDetails.size()));
                    TotalPanierFX.setText(String.valueOf(MonPanier.getMonPanier().calculerPrixTotalFacture()));

                    chargerContenuPanier();
                    //System.out.println("Taille de la liste après supp : " + MonPanier.getMonPanier().ListeDetails.size());
                });

                VBox vbox = new VBox(10); // Espacement de 10 pixels entre chaque produit
                vbox.getChildren().addAll(new VBox(new Label("Entité Produit " + (i + 1)), labelPhoto, labelNom, labelPrix , labelPrixTotalArtile , Supprimer));

                //espace vertical entre les colonnes
                if (col > 0)
                {
                    Insets insets = new Insets(10, 10, 10, 0); //haut / droite / bas / gauche
                    GridPanier.add(vbox, col, row);
                    GridPane.setMargin(vbox, insets);
                } else
                {
                    GridPanier.add(vbox, col, row);
                }

                // Incrémenter nb colonne et ligne
                col++;
                if (col == produitsParLigne)
                {
                    col = 0;
                    row++;
                }
            }

            // espace vertical entre les lignes
            GridPanier.setVgap(10);

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }

    //*****GetAllFacture
    public TableView.TableViewSelectionModel<facture> getSelectionModel() {
        return GetAllFactureTable.getSelectionModel();
    }

    @FXML
    void AffOneFacture(ActionEvent event)
    {
        facture selectedFacture = GetAllFactureTable.getSelectionModel().getSelectedItem();

        if (selectedFacture != null)
        {
            try {

                GetAllFacture.setVisible(false);
                GetOneFacture.setVisible(true);

                afficherFacture(selectedFacture);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de l'affichage de la facture");
                alert.setContentText("Une erreur s'est produite lors de l'affichage de la facture sélectionnée.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une facture dans la TableView.");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteOneFacture(ActionEvent event)
    {

        facture selectedFacture = GetAllFactureTable.getSelectionModel().getSelectedItem();
        System.out.println("hidiii select loulaaa bech nchouff chnouwa id kdehh : " + selectedFacture.getIdFacture());

        if (selectedFacture != null)
        {
            try {

                factureService.delete(selectedFacture.getIdFacture());
                System.out.println(selectedFacture.getIdFacture());

                GetAllFactureTable.getItems().remove(selectedFacture);

                System.out.println("id facture selectionner " + selectedFacture);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("La facture a été supprimée avec succès.");
                alert.showAndWait();
            } catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de la suppression de la facture");
                alert.setContentText("Une erreur s'est produite lors de la suppression de la facture.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une facture dans la TableView.");
            alert.showAndWait();
        }
    }

    @FXML
    void UpdateOneFacture(ActionEvent event) throws IOException {

        facture selectedFacture = GetAllFactureTable.getSelectionModel().getSelectedItem();
        System.out.println("id facture selectionner " + selectedFacture);

        if (selectedFacture != null)
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resourcesGestionStore/UpdateOneFacture.fxml"));
            GetAllFacture = loader.load();
            UpdateOneFactureController upF = loader.getController();

            upF.setFacture(selectedFacture.getIdFacture() );//, GlobalVar.getUser().getId());

            ScaleTransition st = new ScaleTransition(Duration.millis(100), GetAllFacture);
            st.setInterpolator(Interpolator.EASE_IN);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);
            Stage stage = new Stage();
            stage.setTitle("Update facture");
            Scene scene = new Scene(GetAllFacture, 413, 190);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            //refreshTable();

        } else
        {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une facture dans la TableView.");
        }

    }

    @FXML
    void RefreshTable(ActionEvent event)
    {
        try
        {
            ObservableList<facture> factures = FXCollections.observableArrayList(factureService.getAll());

            // Refrechhhhhh TableView
            GetAllFactureTable.setItems(factures);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du rafraîchissement des factures");
            alert.setContentText("Une erreur s'est produite lors du rafraîchissement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }



/*
    public void refreshTable() // sans button
    {
        try {
            // Récupérer à nouveau toutes les factures
            ObservableList<facture> factures = FXCollections.observableArrayList(factureService.getAll());

            // Réassigner les données à la TableView
            GetAllFactureTable.setItems(factures);

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du rafraîchissement des factures");
            alert.setContentText("Une erreur s'est produite lors du rafraîchissement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }

 */


    public facture getFacture()
    {
        fc.setMethodeDePaiement(MethodeP.getText());
        return fc;
    }

    @FXML
    void Quitter(ActionEvent event)
    {
        GetAllFacture.setVisible(false);
        GetAllProduit.setVisible(true);
    }

    public void chargerTableFacture()
    {
        try
        {
            ObservableList<facture> factures = FXCollections.observableArrayList(factureService.getAll());

            // refrechhhh TableView
            GetAllFactureTable.setItems(factures);

            // colonne TableView
            idFactureCol.setCellValueFactory(new PropertyValueFactory<>("idFacture"));
            idClientCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            DateCol.setCellValueFactory(new PropertyValueFactory<>("dateVente"));
            TotalCol.setCellValueFactory(new PropertyValueFactory<>("prixtotalPaye"));
            MethodeP.setCellValueFactory(new PropertyValueFactory<>("methodeDePaiement"));
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }


    //*****GetOneFacture
    @FXML
    void QuitterOneFacture(ActionEvent event)
    {
        GetOneFacture.setVisible(false);
        GetAllProduit.setVisible(true);
    }

    @FXML
    void imprimer(ActionEvent event)
    {
        // Créer un nouv doc PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Écrire data de la facture dans fichier PDF
            writeInvoiceDataToPDF(document);

            // Afficher dossier ou je vais select le dossier ou mettre mon pdf
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la facture");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            // Aff path du dossu=ier ou je vais mettre mon pdf
            Window window = ViewDetailFacture.getScene().getWindow();
            File file = fileChooser.showSaveDialog(window);

            if (file != null) {
                // Sauvegarder le fichier PDF au path select
                document.save(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impression PDF");
                alert.setHeaderText(null);
                alert.setContentText("La facture a été enregistrée dans un fichier PDF.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la génération du fichier PDF");
            alert.setContentText("Une erreur s'est produite lors de la création du fichier PDF.");
            alert.showAndWait();
        }
    }

    @FXML
    void PaiementStipe(ActionEvent event) throws IOException {

            int totalStripe = Integer.parseInt(idFactureFX.getText());
            System.out.println("button paiement stripe :" + totalStripe);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/resourcesGestionStore/Payment.fxml"));
                GetOneFacture = loader.load();

                //UpdateOneFactureController upF = loader.getController();
                //upF.setFacture(selectedFacture.getIdFacture() );//, GlobalVar.getUser().getId());

                ScaleTransition st = new ScaleTransition(Duration.millis(100), GetOneFacture);
                st.setInterpolator(Interpolator.EASE_IN);
                st.setFromX(0);
                st.setFromY(0);
                st.setToX(1);
                st.setToY(1);
                Stage stage = new Stage();
                stage.setTitle("Payment");
                Scene scene = new Scene(GetOneFacture, 576, 430);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                scene.setFill(Color.TRANSPARENT);
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();

    }

    private void writeInvoiceDataToPDF(PDDocument document) throws IOException
    {
        /*
        // Charger l'image depuis le fichier
        //String imagePath = "../../assets/profileuploads/USERIMG11111111.png";

        //PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

        // Récupérer data facture
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

            contentStream.setNonStrokingColor(java.awt.Color.decode("#0000FF"));
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(250, 700);
            contentStream.showText("Facture");
            contentStream.endText();

            contentStream.setNonStrokingColor(java.awt.Color.BLACK);
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


            contentStream.setNonStrokingColor(java.awt.Color.decode("#0000FF"));
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(70, 580);
            contentStream.showText("Detail Facture");
            contentStream.endText();


            float xPosition = 70;
            float yPosition = 560;
            ObservableList<detailfacture> detailFactureList = FXCollections.observableArrayList(dfS.getDetailFacture(Integer.parseInt(idFactureFX.getText())));

            for (detailfacture detail : detailFactureList)
            {
                contentStream.beginText();
                contentStream.setNonStrokingColor(java.awt.Color.BLACK);
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

         */
        // Récupérer les données de la facture
//        String date = String.valueOf(this.Facture.getDateVente());
//        String total = String.valueOf(this.Facture.getPrixtotalPaye());
//        String idClient = GlobalVar.getUser().getFirstname();
//        String idFacture = String.valueOf(this.Facture.getIdFacture());
        String date = DateFX.getText();
        String total = TotalFactureFX.getText();
        String idClient = idClientFX.getText();
        String idFacture = idFactureFX.getText();


        try (PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(0))) {
            contentStream.setNonStrokingColor(java.awt.Color.decode("#0000FF"));
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(250, 700);
            contentStream.showText("GymPlus");
            contentStream.endText();

            contentStream.setNonStrokingColor(java.awt.Color.BLACK);
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

            contentStream.setNonStrokingColor(java.awt.Color.decode("#0000FF"));
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(70, 580);
            contentStream.showText("Detail Facture");
            contentStream.endText();

            float xPosition = 70;
            float yPosition = 560;
            ObservableList<detailfacture> detailFactureList = FXCollections.observableArrayList(dfS.getDetailFacture(Integer.parseInt(idFacture)));


            // Dessiner les bordures du tableau
            float tableWidth = 480; // Largeur totale du tableau
            float tableHeight = 20 * (detailFactureList.size() + 1); // Hauteur totale du tableau
            float yStart = yPosition; // Position verticale de départ du tableau
            float yEnd = yStart - tableHeight; // Position verticale de fin du tableau

            // Dessiner les lignes horizontales
            contentStream.setStrokingColor(java.awt.Color.white);
            contentStream.setLineWidth(1f); // Épaisseur de la ligne

            // Ligne supérieure du tableau
            contentStream.moveTo(xPosition, yStart);
            contentStream.lineTo(xPosition + tableWidth, yStart);
            contentStream.stroke();

            // Dessiner les lignes verticales
            float columnWidth = tableWidth / 5; // Largeur de chaque colonne

            // Boucle pour dessiner les lignes verticales séparant les colonnes
            for (int i = 1; i < 6; i++) {
                float x = xPosition + i * columnWidth;
                contentStream.moveTo(x, yStart);
                contentStream.lineTo(x, yEnd);
                contentStream.stroke();
            }

            // Affichage des en-têtes du tableau
            contentStream.beginText();
            contentStream.setNonStrokingColor(java.awt.Color.BLACK);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(xPosition + 5, yPosition);
            contentStream.showText("ID Detail");
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("ID Produit");
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Prix unitaire");
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Quantite");
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Total Article");
            contentStream.endText();
            yPosition -= 20; // Ajuster l'espacement vertical pour le contenu

            // Affichage des détails de la facture dans le PDF
            for (detailfacture detail : detailFactureList) {
                contentStream.beginText();
                contentStream.setNonStrokingColor(java.awt.Color.BLACK);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText(String.valueOf(detail.getIdDetailFacture()));
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(String.valueOf(detail.getIdProduit()));
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(String.valueOf(detail.getPrixVenteUnitaire()));
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(String.valueOf(detail.getQuantite()));
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(String.valueOf(detail.getPrixVenteUnitaire() * detail.getQuantite() * (1 - detail.getTauxRemise())));
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
                ObservableList<detailfacture> df = FXCollections.observableArrayList(dfS.getDetailFacture(selectedFacture.getIdFacture()));

                // Refrechhhh TableView
                ViewDetailFacture.setItems(df);

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


    public void afficherFacture(facture f) throws SQLException
    {
        if (f != null)
        {
            DateFX.setText(f.getDateVente().toString());
            TotalFactureFX.setText(Float.toString(f.getPrixtotalPaye()));
            idClientFX.setText(Integer.toString(f.getId()));
            idFactureFX.setText(Integer.toString(f.getIdFacture()));

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //*****Get All Product
        if (!GlobalVar.getUser().getRole().equals("admin"))
        {
            ajouterProduitBtn.setVisible(false);
            ajouterProduitBtn.setManaged(false);

            GetAllFactureBtn.setVisible(false);
            GetAllFactureBtn.setManaged(false);

            GetAllFactureLB.setVisible(false);
            GetAllFactureLB.setManaged(false);

            AddProdFX.setVisible(false);
            AddProdFX.setManaged(false);
        }

        //comboBox categorie
        ObservableList<String> list_categorie = FXCollections.observableArrayList("Food", "equipement", "vetement" , "All");
        boxCategorieFX.setItems(list_categorie);

        //comboBox Montant
        ObservableList<String> list_Montant = FXCollections.observableArrayList("Price low to high", "Price high to low");
        boxMontantFX.setItems(list_Montant);

        //text Field Search
        searchFX.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });

        //combo box montant trie
        boxMontantFX.setOnAction(event -> {
            String triSelectionne = boxMontantFX.getValue();
            trierProduitsMontant(triSelectionne);
        });

        //combo box categorie trie
        boxCategorieFX.setOnAction(event -> {
            String triSelectionne = boxCategorieFX.getValue();
            trierProduitsCategorie(triSelectionne);
        });


        filterProducts(searchFX.getText());
        trierProduitsMontant(boxMontantFX.getValue());
        trierProduitsCategorie(boxCategorieFX.getValue());
        AfficherAllProduct();


        //*****Add Produit
        //comboBox
        Categorie_Combo.setItems(list_categorie);
        //*****Update Produit
        Categorie_Combo1.setItems(list_categorie);

        //*****Panier
        //comboBox Paiement
        ObservableList<String> list_methodeP = FXCollections.observableArrayList("carte" , "cheque" , "espece" , "en ligne" );
        comboPaiement.setItems(list_methodeP);

        if (MonPanier == null)
        {
            MonPanier = new PanierService();
            MonPanier.getMonPanier().setId(GlobalVar.getUser().getId());
        }

        //filePhoto.setInitialDirectory(new File("D:\\projet_PI\\GymPlus\\imageProduit"));
        filePhoto.setInitialDirectory(new File("src/assets/imageProduit/"));


        //*****GetOneProduit
        if (!GlobalVar.getUser().getRole().equals("admin"))
        {
            DeleteProductFX.setVisible(false);
            DeleteProductFX.setManaged(false);

            UpdateProductFX.setVisible(false);
            UpdateProductFX.setManaged(false);

        }


    }


}
