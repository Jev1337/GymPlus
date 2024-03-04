package controllers.gestionStore;

import com.stripe.exception.StripeException;
import controllers.gestionuser.GlobalVar;
import entities.gestionStore.detailfacture;
import entities.gestionStore.facture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import services.gestionStore.DetailFactureService;
import services.gestionStore.FactureService;


import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentController implements Initializable {

    //*****interface payment
    @FXML
    private Label total;
    @FXML
    private Button pay_btn;
    @FXML
    private TextField email;
    @FXML
    private TextField num_card;
    @FXML
    private Spinner<Integer> MM;
    @FXML
    private Spinner<Integer> YY;
    @FXML
    private Spinner<Integer> cvc; //********************numéro de sécurité de votre carte bancaire.   qui se compose de 3 num
    @FXML
    private TextField client_name;
    @FXML
    private Button back_btn;
    private TextField spinnerTextField;
    private SpinnerValueFactory<Integer> spinMM;
    private SpinnerValueFactory<Integer> spinYY;
    private SpinnerValueFactory<Integer> spinCVC;
    @FXML
    private Pane pane_payment;
    @FXML
    private Label successFX;

    //*****interface sucess
    @FXML
    private Pane pane_Success;

    //*****interfacefail
    @FXML
    private Pane pane_Fail;

    //****var
    private float total_pay;
    private facture Facture;



/*
    public void setMM()
    {
        spinMM = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        MM.setValueFactory(spinMM);
    }

    public void setYY()
    {
        spinYY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        YY.setValueFactory(spinYY);
    }

    public void setCVC()
    {
        spinCVC = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        cvc.setValueFactory(spinCVC);
    }

 */

    public void setData(facture f) {
        this.Facture = f;
        total_pay = (f.getPrixtotalPaye());
        System.out.println("prix total facture stripe " + f.getPrixtotalPaye());

        int mm = LocalDate.now().getMonthValue();
        int yy = LocalDate.now().getYear();
        SpinnerValueFactory<Integer> valueFactory_month = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, mm, 1);// (min,max,startvalue,incrementValue)
        SpinnerValueFactory<Integer> valueFactory_year = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999999, yy, 1);// (min,max,startvalue,incrementValue)
        SpinnerValueFactory<Integer> valueFactory_cvc = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1, 1);// (min,max,startvalue,incrementValue)
        MM.setValueFactory(valueFactory_month);
        YY.setValueFactory(valueFactory_year);
        cvc.setValueFactory(valueFactory_cvc);
        String total_txt = "Total : " + String.valueOf(total_pay) + " Dt.";
        total.setText(total_txt);
        spinnerTextField= cvc.getEditor();
        spinnerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                cvc.getValueFactory().setValue(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                // Handle invalid input
            }
        });

    }

    @FXML
    private void payment(ActionEvent event) throws StripeException {

        if (client_name.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to input your Name");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            client_name.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(client_name).play();
        } else if (email.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to input your Email");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            email.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(email).play();
        } else if (!isValidEmail(email.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid Email address.");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            email.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(email).play();
        } else if (num_card.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to input your Card Number");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            num_card.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(num_card).play();
        } else if (!check_cvc(cvc.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The CVC number should contain three digits");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            cvc.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(cvc).play();
        } else if (!check_expDate(YY.getValue(), MM.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid expiration date");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            MM.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            YY.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(MM).play();
            new animatefx.animation.Shake(YY).play();
        } else {
            client_name.setStyle(null);
            email.setStyle(null);
            num_card.setStyle(null);
            cvc.setStyle(null);
            MM.setStyle(null);
            YY.setStyle(null);
            boolean isValid = check_card_num(num_card.getText());
            if (!isValid) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a valid Card number");
                alert.setTitle("Problem");
                alert.setHeaderText(null);
                alert.showAndWait();
                num_card.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                new animatefx.animation.Shake(num_card).play();
            } else {
                num_card.setStyle(null);
                String name = client_name.getText();
                String email_txt = email.getText();
                String num = num_card.getText();
                int yy = YY.getValue();
                int mm = MM.getValue();
                String cvc_num = String.valueOf(cvc.getValue());
                //boolean payment_result1 = PaymentProcessor.processPayment(name, email_txt, total_pay, num, mm, yy, cvc_num);

                boolean payment_result = PaymentProcessor.processPayment(name, email_txt, total_pay, "4242424242424242", 12, 24, "100");

                if (payment_result) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Successful Payment.");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    //*************************************
                    redirect_to_successPage();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Payment Failed.");
                    alert.setTitle("Problem");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    //***************************
                    redirect_to_FailPage();
                }
            }
        }

    }

    private boolean check_cvc(int value) {
        String cvc_txt = String.valueOf(value);
        boolean valid = false;
        if (cvc_txt.length() == 3) {
            valid = true;
        }
        return valid;
    }

    private boolean check_expDate(int value_y, int value_mm) {
        boolean valid = false;
        LocalDate date = LocalDate.now();
        if ((value_y >= date.getYear()) && (value_mm >= date.getMonthValue())) {
            valid = true;
        }
        return valid;
    }

    private boolean check_card_num(String cardNumber) {
        // Trim the input string to remove any leading or trailing whitespace
        cardNumber = cardNumber.trim();
        // Step 1: Check length
        int length = cardNumber.length();
        if (length < 13 || length > 19) {
            return false;
        }
        String regex = "^(?:(?:4[0-9]{12}(?:[0-9]{3})?)|(?:5[1-5][0-9]{14})|(?:6(?:011|5[0-9][0-9])[0-9]{12})|(?:3[47][0-9]{13})|(?:3(?:0[0-5]|[68][0-9])[0-9]{11})|(?:((?:2131|1800|35[0-9]{3})[0-9]{11})))$";
        // Create a Pattern object with the regular expression
        Pattern pattern = Pattern.compile(regex);

        // Match the pattern against the credit card number
        Matcher matcher = pattern.matcher(cardNumber);

        // Return true if the pattern matches, false otherwise
        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        // Trim the input string to remove any leading or trailing whitespace
        email = email.trim();
        // Regular expression pattern to match an email address
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the pattern against the email address
        Matcher matcher = pattern.matcher(email);

        // Return true if the pattern matches, false otherwise
        return matcher.matches();
    }


    private void redirect_to_successPage() {
        pane_payment.setVisible(false);
        pane_Success.setVisible(true);
    }

    private void redirect_to_FailPage() {
        pane_payment.setVisible(false);
        pane_Fail.setVisible(true);
    }

    @FXML
    void getrecepeitpayment(ActionEvent event) {
        // Créer un nouv doc PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Écrire data de la facture dans fichier PDF
            getReceiptPdf(document);

            // Afficher dossier ou je vais select le dossier ou mettre mon pdf
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la facture");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            // Aff path du dossu=ier ou je vais mettre mon pdf
            Window window = pane_Success.getScene().getWindow();
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

    private final DetailFactureService dfS = new DetailFactureService();
    private void getReceiptPdf(PDDocument document) throws IOException {
        // Récupérer les données de la facture
        String date = String.valueOf(this.Facture.getDateVente());
        String total = String.valueOf(this.Facture.getPrixtotalPaye());
        String idClient = GlobalVar.getUser().getFirstname();
        String idFacture = String.valueOf(this.Facture.getIdFacture());

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





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        setMM();
        setCVC();
        setYY();
         */
        FactureService fs = new FactureService();
        try {
            facture f = fs.getDerniereFacture();
            setData(f);
            /*
            float x = f.getPrixtotalPaye();
            System.out.println("prix total facture stripe " + x );
            total.setText(String.valueOf(x));
             */
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ;
    }
}
