package org.market.trade.service;

import com.angelbroking.smartapi.smartstream.models.ExchangeType;
import com.angelbroking.smartapi.smartstream.models.SmartStreamSubsMode;
import com.angelbroking.smartapi.smartstream.models.TokenID;
import com.angelbroking.smartapi.smartstream.ticker.SmartStreamTicker;
import com.neovisionaries.ws.client.WebSocketException;
import org.market.trade.component.BrokerStreamListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BrokerStreamingService {

    private final BrokerStreamListener streamListener;

    public BrokerStreamingService(BrokerStreamListener streamListener) {
        this.streamListener = streamListener;
    }

    public void startStreaming(String clientId, String feedToken) {
        SmartStreamTicker smartStreamTicker = new SmartStreamTicker(clientId, feedToken, streamListener);

        try {
            smartStreamTicker.connect();
            System.out.println("Connection open: " + smartStreamTicker.isConnectionOpen());

            // Subscribe to tokens
            Set<TokenID> tokenSet = new HashSet<>();
            tokenSet.add(new TokenID(ExchangeType.NSE_CM, "26000")); // NIFTY
            //tokenSet.add(new TokenID(ExchangeType.NSE_CM, "26009")); // NIFTY BANK
            //tokenSet.add(new TokenID(ExchangeType.BSE_CM, "19000"));

            smartStreamTicker.subscribe(SmartStreamSubsMode.LTP, tokenSet);


            // Keep the connection open
            while (smartStreamTicker.isConnectionOpen()) {
                Thread.sleep(1000); // Keep the thread alive for continuous data reception
            }
        } catch (WebSocketException | InterruptedException e) {
            throw new RuntimeException("Streaming failed", e);
        }
    }

}