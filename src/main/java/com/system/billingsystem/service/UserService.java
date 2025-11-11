package com.system.billingsystem.service;

import com.system.billingsystem.dao.UserDao;
import com.system.billingsystem.models.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User saveUser(User user) throws SQLException {
        return userDao.save(user);
    }

    public User getUser(User user) throws SQLException {
        return userDao.get(user);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.getAll();
    }

    public boolean loginUser(String email, String password) throws SQLException {
        String storedPassword = userDao.getUserPassword(email);
        return storedPassword != null && storedPassword.equals(password);
    }
}
