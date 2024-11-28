package org.market.trade;

import com.angelbroking.smartapi.SmartConnect;
import com.angelbroking.smartapi.http.SessionExpiryHook;
import com.angelbroking.smartapi.http.exceptions.SmartAPIException;
import com.angelbroking.smartapi.models.TokenSet;
import com.angelbroking.smartapi.models.User;
import com.angelbroking.smartapi.smartstream.models.*;
import com.angelbroking.smartapi.smartstream.ticker.SmartStreamListener;
import com.angelbroking.smartapi.smartstream.ticker.SmartStreamTicker;
import com.neovisionaries.ws.client.WebSocketException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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


    public static void main(String[] args) throws InterruptedException {
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


        //System.out.println(smartConnect.getProfile());
//        try {
//            getRMS(smartConnect);
//        }catch (Exception e){
//
//        } catch (SmartAPIException e) {
//            throw new RuntimeException(e);
//        }
        // token re-generate

//        TokenSet tokenSet = smartConnect.renewAccessToken(user.getAccessToken(),
//                user.getRefreshToken());
//        smartConnect.setAccessToken(tokenSet.getAccessToken());

//        try {
//            //getHolding(smartConnect);
//            getAllHolding(smartConnect);
//        } catch (SmartAPIException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        String feedToken = user.getFeedToken();
        SmartStreamListener smartStreamListener = new SmartStreamListener() {
            @Override
            public void onLTPArrival(LTP ltp) {
                System.out.println("ltp value==========>" + ltp.getExchangeType());
                System.out.println("ltp--->"+ltp.getLastTradedPrice());
            }

            @Override
            public void onQuoteArrival(Quote quote) {

            }

            @Override
            public void onSnapQuoteArrival(SnapQuote snapQuote) {

            }

            @Override
            public void onDepthArrival(Depth depth) {

            }

            @Override
            public void onConnected() {
                System.out.println("connected successfully");
            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onError(SmartStreamError smartStreamError) {

            }

            @Override
            public void onPong() {

            }

            @Override
            public SmartStreamError onErrorCustom() {
                return null;
            }
        };

        SmartStreamTicker smartStreamTicker = new SmartStreamTicker(cliedId,feedToken,smartStreamListener);
        try {
            smartStreamTicker.connect();
        } catch (WebSocketException e) {
            throw new RuntimeException(e);
        }
        Boolean connection =  smartStreamTicker.isConnectionOpen();

        Set<TokenID> tokenSet = new HashSet<>();
        tokenSet.add(new TokenID(ExchangeType.NSE_CM, "26000")); // NIFTY
        tokenSet.add(new TokenID(ExchangeType.NSE_CM, "26009")); // NIFTY BANK
        tokenSet.add(new TokenID(ExchangeType.BSE_CM, "19000"));

        smartStreamTicker.subscribe(SmartStreamSubsMode.LTP,tokenSet);
        // Keep the connection open
        while (smartStreamTicker.isConnectionOpen()) {
            Thread.sleep(1000); // Keep the thread alive for continuous data reception
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

    /** Get Margin in trading account*/
    public static void getRMS(SmartConnect smartConnect) throws SmartAPIException, IOException {
        // Returns RMS.
        JSONObject response = smartConnect.getRMS();
        System.out.println(response);
    }
}
