package com.system.billingsystem.models;

import java.util.Date;

public class Customers {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date createdAt;
    private String notes;

    public Customers() {}

    public Customers(String customerId, String firstName, String lastName, String email,
                     String phoneNumber, Date createdAt, String notes) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.notes = notes;
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Date getCreatedAt() { return createdAt; }
    public Date getDate() { return createdAt; }
    public String getNotes() { return notes; }

    // Setters
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setNotes(String notes) { this.notes = notes; }

        @Override
        public String toString() {
        return getName() + " (" + getPhoneNumber() + ")";
}
}
