package com.example.rocketpop.service;

import com.example.rocketpop.model.User;
import com.example.rocketpop.repository.UserDatabase;
import com.example.rocketpop.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    
    @Autowired
    private UserDatabase userDatabase;
    
    @Autowired
    private JWTUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    
    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Authenticate user and generate appropriate token
     */
    public String authenticateUser(String username, String password) {
        User user = userDatabase.getUser(username);
        
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        logger.info("user: {}", user);
        logger.info("password: {}", password);
        logger.info("user.getPassword(): {}", user.getPassword());
        
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
        
        // Generate appropriate token based on title
        String email = user.getEmail() != null ? user.getEmail() : username + "@rocketpop.com";
        String title = user.getTitle() != null ? user.getTitle() : "user";
        
        if ("admin".equalsIgnoreCase(title)) {
            return jwtUtil.generateAdminToken(user);
        } else {
            return jwtUtil.generateUserToken(user);
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
     * Get user by id
     */
    public User getUserById(String id) {
        User user = userDatabase.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    
    /**
     * Get user info from token
     */
    public User getUserFromToken(String token) {
        logger.info("getUserFromToken called with token: {}", token);
        boolean isAdmin = jwtUtil.isAdminToken(token);
        String id = jwtUtil.extractId(token, isAdmin);
        logger.info("userid: {}", id);
        return getUserById(id);
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
    public User createUser(User user) {
        // Check if user already exists
        User existingUser = userDatabase.getUser(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        
        // BCrypt includes the salt in the hash, so we'll store an empty string for the salt column
        //String encodedPassword = passwordEncoder.encode(password);
        //User user = new User(username, encodedPassword, "");
        boolean created = userDatabase.createUser(user);
        if (!created) {
            throw new RuntimeException("Failed to create user");
        }
        
        return userDatabase.getUser(user.getUsername());
    }
    
    /**
     * Update existing user
     */
    public User updateUser(User user) {
        if (!userDatabase.userExists(user.getUsername())) {
            throw new RuntimeException("User not found");
        }
        
        boolean updated = userDatabase.updateUser(user);

        if (!updated) {
            throw new RuntimeException("Failed to update user");
        }
        
        return userDatabase.getUser(user.getUsername());
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
    

    /** Gets all of the usernames of all users */
    public List<String> getUsernames() {
        return userDatabase.getUsernames();
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

    /**
     * Get user salt
     */
    public String getUserSalt(String username) {
        return userDatabase.getUserSalt(username);
    }

}
