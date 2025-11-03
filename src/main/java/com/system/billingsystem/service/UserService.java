package com.system.billingsystem.service;

import com.system.billingsystem.dao.UserDao;
import com.system.billingsystem.models.User;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/*
a service class for user related operations
connects to the UserDao for database interactions
 */

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // Service methods for user operations
    public User getUser(User user) throws SQLException {
        try {
            return userDao.get(user);
        } catch (Exception e) {
            throw new SQLException("Error:" + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        try {
            return userDao.getAll();
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }

    }

    public User saveUser(User user) throws SQLException {
        try {
            return userDao.save(user);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public User updateUser(User user, String[] details) throws SQLException {
        try {
            return userDao.update(user, details);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public User deleteUser(String id) throws SQLException {
        try {
            return userDao.delete(id);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }


    //for logging in user
    public boolean loginUser(String email, String password) throws SQLException{
        try{
            if (userDao.getUserByEmail(email) != null){
                if(userDao.getUserPassword(email).equals(password)){
                    return  true;
                }
            }
            return false;

        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
}
