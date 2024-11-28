package org.market.trade.configuration;

import com.angelbroking.smartapi.SmartConnect;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrokerConfiguration {
    @Bean
    public SmartConnect smartConnect(Dotenv dotenv) {
        String apiKey = dotenv.get("API_KEY");
        SmartConnect smartConnect = new SmartConnect();
        smartConnect.setApiKey(apiKey);
        smartConnect.setSessionExpiryHook(() -> System.out.println("Session expired"));
        return smartConnect;
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}
