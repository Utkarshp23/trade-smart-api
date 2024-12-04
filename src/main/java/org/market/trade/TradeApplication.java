package org.market.trade;

import com.angelbroking.smartapi.http.exceptions.SmartAPIException;
import com.angelbroking.smartapi.models.User;
import org.market.trade.service.BrokerAuthService;
import org.market.trade.service.BrokerStreamingService;
import org.market.trade.service.HistoricDataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
public class TradeApplication {

	public static void main(String[] args) throws SmartAPIException, IOException {

		//SpringApplication.run(TradeApplication.class, args);
		ApplicationContext context = SpringApplication.run(TradeApplication.class, args);
		//BrokerAuthService authService = context.getBean(BrokerAuthService.class);
		//BrokerStreamingService streamingService = context.getBean(BrokerStreamingService.class);

		// Login and start streaming
		//User user = authService.login();
		//streamingService.startStreaming(user.getUserId(), user.getFeedToken());

		//HistoricDataService historicDataService = context.getBean(HistoricDataService.class);
		//historicDataService.getCandleData();
	}

}
