package org.market.trade.service;

import com.angelbroking.smartapi.SmartConnect;
import com.angelbroking.smartapi.http.exceptions.SmartAPIException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HistoricDataService {

    @Autowired
    SmartConnect smartConnect;

    public void getCandleData() throws SmartAPIException, IOException {

        JSONObject requestObejct = new JSONObject();
        requestObejct.put("exchange", "NSE");
        requestObejct.put("symboltoken", "3045");
        requestObejct.put("interval", "ONE_MINUTE");
        requestObejct.put("fromdate", "2024-11-28 09:00");
        requestObejct.put("todate", "2024-11-29 09:20");

        JSONArray response = smartConnect.candleData(requestObejct);
        System.out.println("response--->"+response);
    }

}
