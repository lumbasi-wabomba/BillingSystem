package com.system.billingsystem.dao;

import com.system.billingsystem.models.Customers;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao implements Dao<Customers> {

    @Override
    public Customers get(Customers customer) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getEmail());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customers(
                        rs.getString("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getTimestamp("created_at"),
                        rs.getString("notes")
                );
            }
            return null;
        }
    }

    @Override
    public List<Customers> getAll() throws SQLException {
        String sql = "SELECT * FROM customers";
        List<Customers> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Customers(
                        rs.getString("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getTimestamp("created_at"),
                        rs.getString("notes")
                ));
            }
            return list;
        }
    }

    @Override
    public Customers save(Customers customer) throws SQLException {
        String sql = "INSERT INTO customers (customer_id, first_name, last_name, email, phone_number, created_at, notes) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getPhoneNumber());
            stmt.setTimestamp(6, new Timestamp(customer.getDate().getTime()));
            stmt.setString(7, customer.getNotes());
            stmt.executeUpdate();
            return customer;
        }
    }

    @Override
    public Customers update(Customers customer, String[] params) throws SQLException {
        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone_number=?, notes=? WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, params[0]);
            stmt.setString(2, params[1]);
            stmt.setString(3, params[2]);
            stmt.setString(4, params[3]);
            stmt.setString(5, params[4]);
            stmt.setString(6, customer.getCustomerId());
            stmt.executeUpdate();
            return get(customer);
        }
    }

    public String getLastCustomerId() throws SQLException {
    String sql = "SELECT customer_id FROM customers ORDER BY customer_id DESC LIMIT 1";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            return rs.getString("customer_id");
        }
        return null; // No customers yet
    }
}


    @Override
    public Customers delete(String id) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            return null;
        }
    }


}
