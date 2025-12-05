package com.example.rocketpop.model;

public class User {
    private final int id;
    private final String location;
    private final String userName;
    private final String passwordHash;  

    public User(int id, String location, String userName, String passwordHash) {
        this.id = id;
        this.location = location;
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    // getters
    public int getId() { return id; }
    public String getLocation() { return location; }
    public String getUsername() { return userName; }
    public String getpasswordHash() { return passwordHash; }

}