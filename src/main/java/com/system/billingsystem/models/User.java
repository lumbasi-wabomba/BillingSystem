package com.system.billingsystem.models;

import java.sql.Date;
/*
* this is the User model class
* it contains the user attributes(userID, name, email, role, date created, password) and their getters
*/
public class User {
    private  String userID;
    private String username;
    private String firstName;
    private  String lastName;
    private  String email;
    private String role;
    private Date date;
    private String password;
    public int id =0;

    // Constructor
    public User(){};
    public User(String userID, String username, String firstName, String lastName, String email, String role, String password, Date date){
        this.userID = userID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.password = password;
        this.date = date;
    };

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", date=" + date +
                ", password='" + password + '\'' +
                '}';
    };

    // getters of the attributes of class User
    public String getUserID() {
        return userID;
    };
    public String getUsername(){return username;};
    public String getName() {
        return firstName + " " + lastName;
    };
    public  String getEmail() {
        return email;
    };
    public String getRole() {
        return role;
    };
    public String getPassword() {
        return  password;
    };
    public Date getDate() {
        return date;
    };

    //method for generating userID
    public String generateUserID(){
        id +=1;
        return "U0"+ Integer.toString(id);
    }

}
