package org.market.trade.component;

import com.angelbroking.smartapi.smartstream.models.*;
import com.angelbroking.smartapi.smartstream.ticker.SmartStreamListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class BrokerStreamListener implements SmartStreamListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onLTPArrival(LTP ltp) {
//        System.out.println("--->"+ltp.getSequenceNumber());
//        System.out.println("LTP value: " + ltp.getExchangeType());
        System.out.println("LTP: " + ltp.getLastTradedPrice());

        messagingTemplate.convertAndSend("/stock/ltp", ltp.getLastTradedPrice());
    }

    @Override
    public void onConnected() {
        System.out.println("Connected successfully");
    }

    @Override
    public void onDisconnected() {
        System.out.println("Disconnected");
    }

    @Override
    public void onError(SmartStreamError smartStreamError) {
        //System.err.println("Error: " + smartStreamError.getErrorMessage());
    }

    @Override
    public void onQuoteArrival(Quote quote) {
        System.out.println("quote--->"+quote);
        messagingTemplate.convertAndSend("/stock/quote",quote);
    }

    @Override
    public void onSnapQuoteArrival(SnapQuote snapQuote) { }

    @Override
    public void onDepthArrival(Depth depth) { }

    @Override
    public void onPong() { }

    @Override
    public SmartStreamError onErrorCustom() {
        return null;
    }
}
