package com.example.rocketpop.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String location;

    // Constructor for database retrieval (all fields)
    public User(int id, String username, String password, String email, String role, String location) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.location = location;
    }

    // Constructor for simple user (backward compatibility)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Default constructor
    public User() {
    }

    // Getters and Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
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
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() { 
        return role; 
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getLocation() { 
        return location; 
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}