package com.example.rocketpop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rocketpop.model.User;
import com.example.rocketpop.service.UserService;
import com.example.rocketpop.util.PasswordHasher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;


    @Autowired
    private final PasswordHasher passwordHasher = new PasswordHasher();

    @GetMapping("/info")
    public ResponseEntity<?> getSelf(@RequestHeader("Authorization") String token) {
        try {
            // Validate token
            if (!userService.validateUserToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Get user information from token
            User user = userService.getUserFromToken(token.replace("Bearer ", ""));
            
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("title", user.getTitle());
            response.put("id", user.getId());
            response.put("location", user.getLocation());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/updatepassword")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordChangeRequest passwordChangeRequest) {

            LOGGER.info("changePassword called with token: " + token);
        try {
            // Validate token
            if (!userService.validateUserToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Validate password fields are not empty
            if (passwordChangeRequest.getOldPassword() == null || passwordChangeRequest.getOldPassword().trim().isEmpty() ||
                passwordChangeRequest.getNewPassword() == null || passwordChangeRequest.getNewPassword().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Old password and new password are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Get user from token and update password
            User user = userService.getUserFromToken(token.replace("Bearer ", ""));

            String newPasswordHash = passwordHasher.hashPassword(passwordChangeRequest.getNewPassword(), user.getSalt());
            userService.updatePassword(
                user.getUsername(), 
                user.getPassword(),
                newPasswordHash
            );
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password updated successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            LOGGER.error("Error changing password", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /** Gets all of the usernames of all users */
    @GetMapping("/getusersnames")
    public ResponseEntity<?> getUsernames() {
        try {
            // Return a JSON array of all users first name, last name, title
            var users = userService.getAllUsers();
            var response = new ArrayList<Map<String, String>>();
            for (var user : users) {
                var userMap = new HashMap<String, String>();
                userMap.put("firstName", user.getFirstName());
                userMap.put("lastName", user.getLastName());
                userMap.put("title", user.getTitle());
                response.add(userMap);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // Inner class for password change request
    public static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;
        
        public String getOldPassword() {
            return oldPassword;
        }
        
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
    
}
