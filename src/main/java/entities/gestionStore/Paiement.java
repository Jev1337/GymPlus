package entities.gestionStore;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

public class Paiement {

        public static void main(String[] args) {
// Set your secret key here
            Stripe.apiKey = "sk_test_51OpFDfKiR2IT5FaSfdT8a4tfY4vjNvknSJTvMtbRFogNZHMNYSeAF4ooANjt6J5iapfoeyqxjntMSMtq9nvWwTm000PI1A2xEo";

            try {
// Retrieve your account information
                Account account = Account.retrieve();
                System.out.println("Account ID: " + account.getId());
// Print other account information as needed
            } catch (StripeException e) {
                e.printStackTrace();
            }
        }

}
