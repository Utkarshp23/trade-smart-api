package org.market.trade.service;

import com.angelbroking.smartapi.SmartConnect;
import com.angelbroking.smartapi.models.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class BrokerAuthService {

    private final SmartConnect smartConnect;
    private final Dotenv dotenv;

    public BrokerAuthService(SmartConnect smartConnect, Dotenv dotenv) {
        this.smartConnect = smartConnect;
        this.dotenv = dotenv;
    }

    public User authenticate(String clientId, String mpin)  {
        //String clientId = dotenv.get("CLIENT_ID");
        //String password = dotenv.get("ANGLE_ONE_MPIN");
        String authenticatorKey = dotenv.get("AUTHENTICATOR_KEY");
        System.out.println("clientId:"+clientId+"mpin:"+mpin);
        // Generate TOTP
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        int totp = gAuth.getTotpPassword(authenticatorKey);
        System.out.println("TOTP: " + totp);
        // Format the integer as a zero-padded 6-digit string
        String formattedTotp = String.format("%06d", totp);
        System.out.println("formattedTotp: " + formattedTotp);

        // Login and return user
        try {
            User user = smartConnect.generateSession(clientId, mpin, formattedTotp);
            System.out.println("User--->"+user);
            smartConnect.setAccessToken(user.getAccessToken());
            smartConnect.setUserId(user.getUserId());
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
}
