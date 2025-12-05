package com.example.rocketpop.service;

import com.example.rocketpop.entity.SSOUser;
import com.example.rocketpop.repository.SSOUserRepository;
import com.example.rocketpop.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private SSOUserRepository userRepository;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Authenticate user and generate appropriate token
     */
    public String authenticateUser(String username, String password) {
        Optional<SSOUser> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }
        
        SSOUser user = userOpt.get();
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        // Generate appropriate token based on role
        if ("admin".equalsIgnoreCase(user.getRole())) {
            return jwtUtil.generateAdminToken(user.getUsername(), user.getEmail());
        } else {
            return jwtUtil.generateUserToken(user.getUsername(), user.getEmail(), user.getRole());
        }
    }
    
    /**
     * Get user by username
     */
    public SSOUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Get user info from token
     */
    public SSOUser getUserFromToken(String token) {
        boolean isAdmin = jwtUtil.isAdminToken(token);
        String username = jwtUtil.extractUsername(token, isAdmin);
        return getUserByUsername(username);
    }
    
    /**
     * Update user password
     */
    public void updatePassword(String username, String oldPassword, String newPassword) {
        SSOUser user = getUserByUsername(username);
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    /**
     * Create new user
     */
    public SSOUser createUser(String username, String password, String email, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        SSOUser user = new SSOUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);
        
        return userRepository.save(user);
    }
    
    /**
     * Update existing user
     */
    public SSOUser updateUser(String username, String password, String email, String role) {
        SSOUser user = getUserByUsername(username);
        
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        
        if (role != null && !role.isEmpty()) {
            user.setRole(role);
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Delete user by username
     */
    public void deleteUser(String username) {
        SSOUser user = getUserByUsername(username);
        userRepository.delete(user);
    }
    
    /**
     * Get all users
     */
    public List<SSOUser> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Search users by username
     */
    public List<SSOUser> searchUsers(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
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
