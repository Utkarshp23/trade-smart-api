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

    public User login() {
        String clientId = dotenv.get("CLIENT_ID");
        String password = dotenv.get("ANGLE_ONE_MPIN");
        String authenticatorKey = dotenv.get("AUTHENTICATOR_KEY");

        // Generate TOTP
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        String totp = String.valueOf(gAuth.getTotpPassword(authenticatorKey));
        System.out.println("TOTP: " + totp);

        // Login and return user
        try {
            User user = smartConnect.generateSession(clientId, password, totp);
            smartConnect.setAccessToken(user.getAccessToken());
            smartConnect.setUserId(user.getUserId());
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
}
