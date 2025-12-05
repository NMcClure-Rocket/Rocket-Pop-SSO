package com.example.rocketpop.model;

public class User {
    private int id = -1;
    private String firstName;
    private String lastName;
    private String title;
    private String location;
    private final String userName;
    private final String password;
    private String salt;  

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getUsername() { return userName; }
    public String getPassword() { return password; }
    public String getSalt() { return salt; }

    // setters
    public void setId(int id) {this.id = id;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setTitle(String title) {this.title = title;}
    public void setLocation(String location) {this.location = location;}
    public void setSalt(String salt) {this.salt = salt;}
}