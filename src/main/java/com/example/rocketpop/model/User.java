package com.example.rocketpop.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private int id = -1;
    private String firstName;
    private String lastName;
    private String title;
    private int department;
    private String email;
    private String country;
    private String city;
    private int location;
    @JsonProperty("username")
    private String userName;
    private String password;
    private String salt;  

    // No-args constructor for Jackson
    public User() {}

    public User(String userName, String password, String salt) {
        this.userName = userName;
        this.password = password;
        this.salt = salt;
    }

    // getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getTitle() { return title; }
    public int getDepartment() { return department; }
    public String getEmail() { return email; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public int getLocation() { return location; }
    public String getUsername() { return userName; }
    public String getPassword() { return password; }
    public String getSalt() { return salt; }

    // setters
    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setTitle(String title) { this.title = title; }
    public void setDepartment(int department) { this.department = department; }
    public void setEmail(String email) { this.email = email; }
    public void setCountry(String country) { this.country = country; }
    public void setCity(String city) { this.city = city; }
    public void setLocation(int location) { this.location = location; }
    public void setUsername(String userName) { this.userName = userName; }
    public void setPassword(String password) { this.password = password; }
    public void setSalt(String salt) { this.salt = salt; }
}

