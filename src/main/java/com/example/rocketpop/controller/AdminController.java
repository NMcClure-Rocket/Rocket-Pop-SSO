package com.example.rocketpop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:42067")
public class AdminController {

    @Autowired
    private UserService userService;


    private final PasswordHasher passwordHasher = new PasswordHasher();

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(
            @RequestHeader("Authorization") String token,
            @RequestBody User userRequest) {
        try {
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
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
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
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
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String username) {
        try {
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized - Admin access required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Get users (filtered by username if provided)
            List<User> users;
            if (username != null && !username.isEmpty()) {
                users = userService.searchUsers(username);
            } else {
                users = userService.getAllUsers();
            }
            
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
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/user/get/{username}")
    public ResponseEntity<?> getUser(
            @RequestHeader("Authorization") String token,
            @PathVariable String username) {
        try {
            // Validate admin token
            if (!userService.validateAdminToken(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized - Admin access required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Get specific user
            User user = userService.getUserByUsername(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("title", user.getTitle());
            response.put("location", user.getLocation());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
}
