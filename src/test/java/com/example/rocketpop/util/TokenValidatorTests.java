package com.example.rocketpop.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenValidatorTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenValidator tokenValidator;

    private static final String VERIFY_URI = "http://172.16.0.51:8080/auth_service/api/auth/verify";
    private static final String PING_URI = "http://172.16.0.51:8080/auth_service/api/auth/ping";

    @BeforeEach
    public void setUp() {
        // Inject the mock RestTemplate
        ReflectionTestUtils.setField(tokenValidator, "restTemplate", restTemplate);
    }

    @Test
    public void testVerifyTokenWithAuthService_Success() {
        // Arrange
        String token = "valid-token";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("valid", true);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
            eq(VERIFY_URI),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(responseEntity);

        // Act
        boolean result = tokenValidator.verifyTokenWithAuthService(token);

        // Assert
        assertTrue(result);
        verify(restTemplate, times(1)).exchange(
            eq(VERIFY_URI),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)
        );
    }

    @Test
    public void testVerifyTokenWithAuthService_Accepted() {
        // Arrange
        String token = "valid-token";
        Map<String, Object> responseBody = new HashMap<>();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);

        when(restTemplate.exchange(
            eq(VERIFY_URI),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(responseEntity);

        // Act
        boolean result = tokenValidator.verifyTokenWithAuthService(token);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testVerifyTokenWithAuthService_Unauthorized() {
        // Arrange
        String token = "invalid-token";
        Map<String, Object> responseBody = new HashMap<>();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);

        when(restTemplate.exchange(
            eq(VERIFY_URI),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(responseEntity);

        // Act
        boolean result = tokenValidator.verifyTokenWithAuthService(token);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testVerifyTokenWithAuthService_Exception() {
        // Arrange
        String token = "error-token";

        when(restTemplate.exchange(
            eq(VERIFY_URI),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenThrow(new RestClientException("Connection error"));

        // Act
        boolean result = tokenValidator.verifyTokenWithAuthService(token);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testVerifyTokenWithAuthService_NullToken() {
        // Arrange
        when(restTemplate.exchange(
            eq(VERIFY_URI),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenThrow(new IllegalArgumentException("Token cannot be null"));

        // Act
        boolean result = tokenValidator.verifyTokenWithAuthService(null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testPingAuthService_Success() {
        // Arrange
        ResponseEntity<String> responseEntity = new ResponseEntity<>("pong", HttpStatus.OK);

        when(restTemplate.getForEntity(PING_URI, String.class)).thenReturn(responseEntity);

        // Act
        boolean result = tokenValidator.pingAuthService();

        // Assert
        assertTrue(result);
        verify(restTemplate, times(1)).getForEntity(PING_URI, String.class);
    }

    @Test
    public void testPingAuthService_NoContent() {
        // Arrange
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.getForEntity(PING_URI, String.class)).thenReturn(responseEntity);

        // Act
        boolean result = tokenValidator.pingAuthService();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testPingAuthService_ServerError() {
        // Arrange
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.getForEntity(PING_URI, String.class)).thenReturn(responseEntity);

        // Act
        boolean result = tokenValidator.pingAuthService();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testPingAuthService_Exception() {
        // Arrange
        when(restTemplate.getForEntity(PING_URI, String.class))
            .thenThrow(new RestClientException("Service unavailable"));

        // Act
        boolean result = tokenValidator.pingAuthService();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testPingAuthService_Timeout() {
        // Arrange
        when(restTemplate.getForEntity(PING_URI, String.class))
            .thenThrow(new RestClientException("Timeout"));

        // Act
        boolean result = tokenValidator.pingAuthService();

        // Assert
        assertFalse(result);
    }
}
