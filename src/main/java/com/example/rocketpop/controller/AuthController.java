package com.example.rocketpop.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.rocketpop.util.PasswordHasher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rocketpop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:42067")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    private final PasswordHasher passwordHasher = new PasswordHasher();

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "online");
        response.put("message", "Rocket Pop SSO API is running");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        LOGGER.info("login called with username: {}, password: {}"
                , loginRequest.getUsername(), loginRequest.getPassword());
        try {
            String passwordHash = passwordHasher.hashPassword(
                    passwordHasher.rsaDecrypt(loginRequest.getPassword()),
                    userService.getUserSalt(loginRequest.getUsername())
                    );
            // Authenticate user and generate JWT token
            String token = userService.authenticateUser(
                    loginRequest.getUsername(),
                    passwordHash
            );

            LOGGER.info("login successful");
            
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            LOGGER.error("login failed: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    // Inner class for login request
    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
