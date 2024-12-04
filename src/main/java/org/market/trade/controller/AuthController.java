package org.market.trade.controller;

import com.angelbroking.smartapi.models.User;
import org.market.trade.configuration.smarapi.SmartApiAuthenticationToken;
import org.market.trade.dto.LoginRequest;
import org.market.trade.dto.LoginResponse;
import org.market.trade.service.BrokerAuthService;
import org.market.trade.service.BrokerStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BrokerStreamingService brokerStreamingService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate using Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getClientId(), loginRequest.getMpin()));

            // Set the authentication object in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Extract user details from the authentication token
            SmartApiAuthenticationToken smartApiToken = (SmartApiAuthenticationToken) authentication;
            User smartApiUser = smartApiToken.getSmartApiUser();

            brokerStreamingService.startStreaming(smartApiUser.getUserId(),smartApiUser.getFeedToken());
            // Return user and success response
            return ResponseEntity.ok(new LoginResponse(true, "Login successful", smartApiUser));

        } catch (Exception e) {
            // Return error response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Login failed: " + e.getMessage(), null));
        }
    }
}
