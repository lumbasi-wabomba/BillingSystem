package com.system.billingsystem.models;

import java.util.Date;

public class User {
    private String userID;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Date createdAt;
    private String password;
    private static int idCounter = 0;

    // Constructors
    public User() {}

    public User(String userID, String username, String firstName, String lastName,
                String email, String role, Date createdAt, String password) {
        this.userID = userID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.password = password;
    }

    // Getters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Date getCreatedAt() { return createdAt; }
    public String getPassword() { return password; }

    public String getFullName() { return firstName + " " + lastName; }

    // Generate unique User ID
    public String generateUserID() {
        idCounter++;
        return "U0" + idCounter;
    }
}
