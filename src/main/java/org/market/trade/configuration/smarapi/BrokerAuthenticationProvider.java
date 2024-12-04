package org.market.trade.configuration.smarapi;

import com.angelbroking.smartapi.models.User;
import org.market.trade.service.BrokerAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BrokerAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    BrokerAuthService brokerAuthService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String clientId = authentication.getName();
        String mpin = authentication.getCredentials().toString();

        // Authenticate using the BrokerAuthService
        User smartApiUser = brokerAuthService.authenticate(clientId, mpin);

        if (smartApiUser != null) {
            // Store the SmartAPI User object in the authentication token
            return new SmartApiAuthenticationToken(clientId, null, smartApiUser);
        }

        throw new UsernameNotFoundException("Invalid client ID or MPIN");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
