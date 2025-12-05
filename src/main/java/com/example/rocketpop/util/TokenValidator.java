package com.example.rocketpop.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TokenValidator {
    
    private static final String VERIFY_URI = "http://172.16.0.51:8080/auth_service/api/auth/verify";
    private static final String PING_URI = "http://172.16.0.51:8080/auth_service/api/auth/ping";
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Verify token with external auth service
     */
    public boolean verifyTokenWithAuthService(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                VERIFY_URI,
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Ping auth service to check if it's available
     */
    public boolean pingAuthService() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(PING_URI, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
