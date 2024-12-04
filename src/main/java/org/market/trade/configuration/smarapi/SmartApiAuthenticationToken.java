package org.market.trade.configuration.smarapi;

import com.angelbroking.smartapi.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SmartApiAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final User smartApiUser;

    public SmartApiAuthenticationToken(Object principal, Object credentials, User smartApiUser) {
        super(principal, credentials);
        this.smartApiUser = smartApiUser;
    }

    public User getSmartApiUser() {
        return smartApiUser;
    }
}
