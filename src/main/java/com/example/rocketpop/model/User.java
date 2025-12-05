package com.example.rocketpop.model;

public class User {
    private final String userName;
    private final String password;  

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUsername() { return userName; }
    public String getPassword() { return password; }
}