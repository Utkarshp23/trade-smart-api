package org.market.trade;

import com.angelbroking.smartapi.SmartConnect;
import com.angelbroking.smartapi.http.SessionExpiryHook;
import com.angelbroking.smartapi.http.exceptions.SmartAPIException;
import com.angelbroking.smartapi.models.TokenSet;
import com.angelbroking.smartapi.models.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import java.io.IOException;

public class Trade {

    //private final  Dotenv dotenv;
    //private final  String apiKey;
    //private final  String authenticatorKey;
    //private final  String cliedId;
    //private final  String password;

    //public Trade(){
        //dotenv = Dotenv.load();
        //apiKey = dotenv.get("API_KEY");
        //cliedId = dotenv.get("CLIENT_ID");
        //password = dotenv.get("ANGLE_ONE_MPIN");
        //authenticatorKey = dotenv.get("AUTHENTICATOR_KEY");
    //}


    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String cliedId = dotenv.get("CLIENT_ID");
        String password = dotenv.get("ANGLE_ONE_MPIN");
        String authenticatorKey = dotenv.get("AUTHENTICATOR_KEY");

        // Getting TOTP from Authinticator Key
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        String totp = String.valueOf(gAuth.getTotpPassword(authenticatorKey));
        System.out.println(" TOTP : "+totp);

        SmartConnect smartConnect = new SmartConnect();
        smartConnect.setApiKey(apiKey);
        // Set session expiry callback.
        smartConnect.setSessionExpiryHook(new SessionExpiryHook() {
            @Override
            public void sessionExpired() {
                System.out.println("session expired");
            }
        });

        User user = smartConnect.generateSession(cliedId, password, totp);
        System.out.println("--->"+user.getUserId()+"--->"+user.getUserName());
        smartConnect.setAccessToken(user.getAccessToken());
        smartConnect.setUserId(user.getUserId());

        // token re-generate

//        TokenSet tokenSet = smartConnect.renewAccessToken(user.getAccessToken(),
//                user.getRefreshToken());
//        smartConnect.setAccessToken(tokenSet.getAccessToken());

        try {
            //getHolding(smartConnect);
            getAllHolding(smartConnect);
        } catch (SmartAPIException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Get Holdings */
    public static void getHolding(SmartConnect smartConnect) throws SmartAPIException, IOException {
        // Returns Holding.
        JSONObject response = smartConnect.getHolding();
        System.out.println(response);
    }

    /** Get All Holdings */
    public static void getAllHolding(SmartConnect smartConnect) throws SmartAPIException, IOException {
        // Returns Holdings.
        JSONObject response = smartConnect.getAllHolding();
        System.out.println("response--->"+response);
    }
}
