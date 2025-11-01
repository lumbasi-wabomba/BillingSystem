package com.system.billingsystem.service;

import com.system.billingsystem.dao.UserDao;
import com.system.billingsystem.models.User;

import javax.swing.*;

/*
a service class for user related operations
connects to the UserDao for database interactions
 */

public class UserService {
    private  final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    //for logging in user
    public boolean authenticateUser(String username, String password) {
            String userEmail =  userDao.getUserByEmail(username);
            String user =  userDao.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        } else (userEmail!= null && userEmail.getPassword().equals(password)) {
            return true;
        }
        return false;
    }


    //for registering new user
    public boolean registerUser(User user){
        try{
            if(userDao.getUserByUsername(user.toString()) != null){
                System.out.println("Username already taken.");
                return false;
            }else {
                userDao.addUser(user);
                System.out.println("User added!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }





}
