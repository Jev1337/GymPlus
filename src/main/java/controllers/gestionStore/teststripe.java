package controllers.gestionStore;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Token;
import com.stripe.param.TokenCreateParams;

import java.util.HashMap;
import java.util.Map;

public class teststripe {

    private static final String STRIPE_API_KEY = "sk_test_51OpFDfKiR2IT5FaSfdT8a4tfY4vjNvknSJTvMtbRFogNZHMNYSeAF4ooANjt6J5iapfoeyqxjntMSMtq9nvWwTm000PI1A2xEo";

    public static void main(String[] args) throws StripeException {
        // Initialisez l'API Stripe avec votre clé secrète
        //Stripe.apiKey = STRIPE_API_KEY;


        Stripe.apiKey = "sk_test_VePHdqKTYQjKNInc7u56JBrQ";

        TokenCreateParams params =
                TokenCreateParams.builder()
                        .setCard(
                                TokenCreateParams.Card.builder()
                                        .setNumber("4242424242424242")
                                        .setExpMonth("5")
                                        .setExpYear("2024")
                                        .setCvc("314")
                                        .build()
                        )
                        .build();

        Token token = Token.create(params);




            // Récupérez l'identifiant du token
            String tokenId = token.getId();

            // Utilisez l'identifiant du token pour effectuer des paiements ou d'autres opérations avec Stripe
            System.out.println("Token ID: " + tokenId);
    }
}
