package com.system.billingsystem.models;

import java.sql.Date;
/*
* this class represents a customer in the billing system
* it contains customer details such as id, name, email, phone number and date of creation
* */
public class Customers {
    // Customer details
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date date;

    // Constructor
    public Customers(String customerId, String firstName, String lastName, String email, String phoneNumber, Date date) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.date = date;
    };

    @Override
    public String toString() {
        return "Customers{" +
                "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date=" + date +
                '}';
    };
    public Customers() {}

    // Getters
    public String getCustomerId() {
        return customerId;
    };
    public String getName() {
        return firstName + " " + lastName;
    };
    public String getEmail() {
        return email;
    };
    public String getPhoneNumber() {
        return phoneNumber;
    };
    public Date getDate() {
        return date;
    };
}
