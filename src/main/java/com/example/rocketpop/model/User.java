package com.example.rocketpop.model;

public class User {
    private final int id;
    private final String location;
    private final String userName;
    private final String password;  

    public User(int id, String location, String userName, String password) {
        this.id = id;
        this.location = location;
        this.userName = userName;
        this.password = password;
    }

    // getters
    public int getId() { return id; }
    public String getLocation() { return location; }
    public String getUsername() { return userName; }
    public String getPassword() { return password; }

}