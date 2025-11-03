package com.system.billingsystem.dao;

import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.DriverManager;

class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/billing_system";
    private static final String USER = "biller";
    private static final String PASSWORD = "Billing@123";

    public static Connection getConnection() throws SQLDataException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new SQLDataException("Unable to connect to database", e);
        }
    }
}
