package com.example.rocketpop.service;

import com.example.rocketpop.model.User;
import com.example.rocketpop.repository.UserDatabase;
import com.example.rocketpop.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserDatabase userDatabase;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Authenticate user and generate appropriate token
     */
    public String authenticateUser(String username, String password) {
        User user = userDatabase.getUser(username);
        
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }
        
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        /*
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        */
        /*
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        */
        
        // Generate appropriate token based on role
        String email = user.getEmail() != null ? user.getEmail() : username + "@rocketpop.com";
        String role = user.getTitle() != null ? user.getTitle() : "user";
        
        if ("admin".equalsIgnoreCase(role)) {
            return jwtUtil.generateAdminToken(user.getUsername(), email);
        } else {
            return jwtUtil.generateUserToken(user.getUsername(), email, role);
        }
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        User user = userDatabase.getUser(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    
    /**
     * Get user info from token
     */
    public User getUserFromToken(String token) {
        boolean isAdmin = jwtUtil.isAdminToken(token);
        String username = jwtUtil.extractUsername(token, isAdmin);
        return getUserByUsername(username);
    }
    
    /**
     * Update user password
     */
    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username);
        
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Current password is incorrect");
        }
        /*
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        */
        
        //user.setPassword(passwordEncoder.encode(newPassword));
        user.setPassword(newPassword);
        // BCrypt includes the salt in the hash, so we keep the salt column empty
        user.setSalt("");
        userDatabase.updateUser(user);
    }
    
    /**
     * Create new user
     */
    public User createUser(String username, String password, String email, String role) {
        // Check if user already exists
        User existingUser = userDatabase.getUser(username);
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        
        // BCrypt includes the salt in the hash, so we'll store an empty string for the salt column
        //String encodedPassword = passwordEncoder.encode(password);
        //User user = new User(username, encodedPassword, "");
        User user = new User(username, password, "");
        user.setEmail(email);
        user.setTitle(role);
        
        boolean created = userDatabase.createUser(user);
        if (!created) {
            throw new RuntimeException("Failed to create user");
        }
        
        return userDatabase.getUser(username);
    }
    
    /**
     * Update existing user
     */
    public User updateUser(String username, String password, String email, String role, String location) {
        User user = getUserByUsername(username);
        
        if (password != null && !password.isEmpty()) {
            //user.setPassword(passwordEncoder.encode(password));
            user.setPassword(password);
            // BCrypt includes the salt in the hash, so we keep the salt column empty
            user.setSalt("");
        }
        
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        
        if (role != null && !role.isEmpty()) {
            user.setTitle(role);
        }
        
        if (location != null && !location.isEmpty()) {
            user.setLocation(Integer.parseInt(location));
        }
        
        boolean updated = userDatabase.updateUser(user);
        if (!updated) {
            throw new RuntimeException("Failed to update user");
        }
        
        return userDatabase.getUser(username);
    }
    
    /**
     * Delete user by username
     */
    public void deleteUser(String username) {
        User user = getUserByUsername(username);
        userDatabase.deleteUser(user.getId());
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDatabase.getAllUsers();
    }
    
    /**
     * Search users by username
     */
    public List<User> searchUsers(String username) {
        if (username == null || username.isEmpty()) {
            return getAllUsers();
        }
        return userDatabase.searchUsers(username);
    }
    
    /**
     * Validate admin token
     */
    public boolean validateAdminToken(String token) {
        token = jwtUtil.cleanToken(token);
        return jwtUtil.isAdminToken(token) && jwtUtil.validateToken(token, true);
    }
    
    /**
     * Validate user token (admin or user)
     */
    public boolean validateUserToken(String token) {
        token = jwtUtil.cleanToken(token);
        boolean isAdmin = jwtUtil.isAdminToken(token);
        return jwtUtil.validateToken(token, isAdmin);
    }
}
