package controllers.gestionStore;

import com.stripe.exception.StripeException;
import entities.gestionStore.facture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import services.gestionStore.FactureService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentController implements Initializable {

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
    private Spinner<Integer> cvc; //********************numéro de sécurité de votre carte bancaire.   qui se compose de 4 num

    private float total_pay;
    private facture Facture;
    @FXML
    private TextField client_name;
    @FXML
    private Button back_btn;
    private TextField spinnerTextField;
    private SpinnerValueFactory<Integer> spinMM;
    private SpinnerValueFactory<Integer> spinYY;
    private SpinnerValueFactory<Integer> spinCVC;



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
                boolean payment_result = PaymentProcessor.processPayment(name, email_txt, total_pay, num, mm, yy, cvc_num);
                if (payment_result) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Successful Payment.");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    //*************************************

                    //redirect_to_successPage();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Payment Failed.");
                    alert.setTitle("Problem");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    //***************************
                    //redirect_to_FailPage();
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

    /*
    private void redirect_to_successPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/reservation/Success_page.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Success_pageController controller = loader.getController();
            controller.setData(this.reservation);
            Scene scene = new Scene(root);
            Stage stage = (Stage) pay_btn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void redirect_to_FailPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/reservation/Fail_page.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Fail_pageController controller = loader.getController();
            controller.setData(this.reservation);
            Scene scene = new Scene(root);
            Stage stage = (Stage) pay_btn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void redirectToListReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/reservation/Reservation_view_client.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Reservation_view_client controller = loader.getController();
            controller.setData(this.reservation.getClient_id());
            Scene scene = new Scene(root);
            Stage stage = (Stage) back_btn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

     */

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
