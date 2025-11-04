package com.system.billingsystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/billing_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "biller";
    private static final String PASSWORD = "Billing@123";

    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver explicitly (helps in modular Java)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            throw new SQLException("Unable to connect to database", e);
        }
    }
}
