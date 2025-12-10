package com.example.rocketpop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rocketpop.model.User;
import com.example.rocketpop.service.UserService;
import com.example.rocketpop.util.PasswordHasher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:42067")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private final PasswordHasher passwordHasher = new PasswordHasher();

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(
            @RequestHeader("Authorization") String token,
            @RequestBody User userRequest) {

            logger.info("createUser called with username: {}", userRequest.getUsername());
            try {
                // Validate admin token
                if (!userService.validateAdminToken(token)) {
                    logger.error("Unauthorized access attempt to createUser - invalid token");
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Unauthorized - Admin access required");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
                }

                // Create new user with 'user' or 'manager' role
                String salt = passwordHasher.getRandomSalt();
                String passwordHash = passwordHasher.hashPassword(
                        passwordHasher.rsaDecrypt(userRequest.getPassword()),
                        salt
                        );

                userRequest.setPassword(passwordHash);
                userRequest.setSalt(salt);

                User newUser = userService.createUser(userRequest);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "User created successfully");
                response.put("username", newUser.getUsername());
                response.put("email", newUser.getEmail());
                response.put("title", newUser.getTitle());
                response.put("id", newUser.getId());
                response.put("location", newUser.getLocation());

                return ResponseEntity.ok(response);

            } catch (RuntimeException e) {
                logger.error("Error creating user", e);
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            } catch (Exception e) {
                logger.error("Internal error creating user", e);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Internal server error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
            }
    
    @PostMapping("/adminuser/create")
    public ResponseEntity<?> createAdmin(
            @RequestHeader("Authorization") String token,
            @RequestBody User userRequest) {
        try {
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized - Admin access required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Create new admin user (internal SSO use only)
            String salt = passwordHasher.getRandomSalt();
            String passwordHash = passwordHasher.hashPassword(
                    passwordHasher.rsaDecrypt(userRequest.getPassword()),
                    salt
                    );
            userRequest.setSalt(salt);
            userRequest.setPassword(passwordHash);

            User newAdmin = userService.createUser(userRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin user created successfully");
            response.put("username", newAdmin.getUsername());
            response.put("email", newAdmin.getEmail());
            response.put("title", newAdmin.getTitle());
            response.put("id", newAdmin.getId());
            response.put("location", newAdmin.getLocation());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/user/edit")
    public ResponseEntity<?> editUser(
            @RequestHeader("Authorization") String token,
            @RequestBody User userRequest) {
        try {
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized - Admin access required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            String salt = userService.getUserSalt(userRequest.getUsername());
            if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
                
            } else {
                String passwordHash = passwordHasher.hashPassword(
                        passwordHasher.rsaDecrypt(userRequest.getPassword()),
                        salt
                        );
                userRequest.setPassword(passwordHash);
            }

            User updatedUser = userService.updateUser(userRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User updated successfully");
            response.put("username", updatedUser.getUsername());
            response.put("email", updatedUser.getEmail());
            response.put("title", updatedUser.getTitle());
            response.put("id", updatedUser.getId());
            response.put("location", updatedUser.getLocation());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("Authorization") String token,
            @PathVariable String id) {
        try {
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized - Admin access required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Delete user by username (id is username in this context)
            userService.deleteUser(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("username", id);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/user/getall")
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(required = false) String username) {
        try {
            LOGGER.info("getAllUsers called with username filter: {}", username);
            
            if (token == null || token.isEmpty()) {
                LOGGER.error("No authorization token provided");
                Map<String, String> error = new HashMap<>();
                error.put("error", "No authorization token provided");
                error.put("message", "Authorization header is missing");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
                LOGGER.error("Unauthorized access attempt to getAllUsers - invalid token");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized - Admin access required");
                error.put("message", "Invalid or non-admin token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Get users (filtered by username if provided)
            List<User> users;
            if (username != null && !username.isEmpty()) {
                LOGGER.info("Searching users with username: {}", username);
                users = userService.searchUsers(username);
            } else {
                LOGGER.info("Getting all users");
                users = userService.getAllUsers();
            }
            
            LOGGER.info("Found {} users", users.size());
            
            // Convert to response format
            List<Map<String, Object>> response = users.stream().map(user -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("username", user.getUsername());
                userMap.put("email", user.getEmail());
                userMap.put("title", user.getTitle());
                userMap.put("location", user.getLocation());
                return userMap;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            LOGGER.error("Error getting all users", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/user/get/{username}")
    public ResponseEntity<?> getUser(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String username) {
        try {
            LOGGER.info("getUser called for username: {}", username);
            
            if (token == null || token.isEmpty()) {
                LOGGER.error("No authorization token provided");
                Map<String, String> error = new HashMap<>();
                error.put("error", "No authorization token provided");
                error.put("message", "Authorization header is missing");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
                LOGGER.error("Unauthorized access attempt to getUser - invalid token");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized - Admin access required");
                error.put("message", "Invalid or non-admin token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Get specific user
            User user = userService.getUserByUsername(username);
            
            if (user == null) {
                LOGGER.warn("User not found: {}", username);
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            LOGGER.info("User found: {}", username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("title", user.getTitle());
            response.put("location", user.getLocation());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            LOGGER.error("Error getting user: {}", username, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            LOGGER.error("Internal error getting user: {}", username, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
}
