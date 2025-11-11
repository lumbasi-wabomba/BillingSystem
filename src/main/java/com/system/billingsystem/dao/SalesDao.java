package com.system.billingsystem.dao;

import com.system.billingsystem.models.Sales;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesDao implements Dao<Sales> {

    // Get a sale by saleId
    @Override
    public Sales get(Sales sales) throws SQLException {
        String sql = "SELECT * FROM sales WHERE sale_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sales.getSaleId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Sales(
                            rs.getString("sale_id"),
                            rs.getString("customer_id"),
                            new Date(rs.getTimestamp("sale_date").getTime()),
                            rs.getString("sales_person_id"),
                            rs.getDouble("total_amount"),
                            rs.getString("status"),
                            rs.getString("payment_method")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error fetching sale: " + sales.getSaleId(), e);
        }
    }

    // Get all sales
    @Override
    public List<Sales> getAll() throws SQLException {
        String sql = "SELECT * FROM sales";
        List<Sales> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Sales(
                        rs.getString("sale_id"),
                        rs.getString("customer_id"),
                        new Date(rs.getTimestamp("sale_date").getTime()),
                        rs.getString("sales_person_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getString("payment_method")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all sales", e);
        }
    }

    @Override
    public Sales save(Sales sales) throws SQLException {
        String sql = "INSERT INTO sales (sale_id, customer_id, sale_date, sales_person_id, total_amount, status, payment_method) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sales.getSaleId());

            // Set default walk-in ID if customerId is null
            String customerId = sales.getCustomerId() == null ? "WALKIN" : sales.getCustomerId();
            ps.setString(2, customerId);

            ps.setTimestamp(3, sales.getSaleDate() != null
                    ? new Timestamp(sales.getSaleDate().getTime())
                    : new Timestamp(System.currentTimeMillis()));

            ps.setString(4, sales.getSalesPersonId());
            ps.setDouble(5, sales.getTotalAmount());
            ps.setString(6, sales.getStatus());
            ps.setString(7, sales.getPaymentMethod());

            ps.executeUpdate();
            return sales;
        } catch (SQLException e) {
            System.out.println("---- SQL ERROR ----");
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            throw e;
        }
    }



    // Update a sale
    // params expected: [customerId, saleDateIso, salesPersonId, totalAmount, status, paymentMethod, saleId]
    @Override
    public Sales update(Sales sales, String[] params) throws SQLException {
        String sql = "UPDATE sales SET customer_id = ?, sale_date = ?, sales_person_id = ?, total_amount = ?, status = ?, payment_method = ? WHERE sale_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, params[0]); // customerId
            // parse date string to Timestamp (expect ISO yyyy-MM-ddTHH:mm:ss or yyyy-MM-dd HH:mm:ss)
            ps.setTimestamp(2, Timestamp.valueOf(params[1].replace("T", " ")));
            ps.setString(3, params[2]); // salesPersonId
            ps.setDouble(4, Double.parseDouble(params[3])); // totalAmount
            ps.setString(5, params[4]); // status
            ps.setString(6, params[5]); // paymentMethod
            ps.setString(7, params[6]); // saleId (WHERE)

            ps.executeUpdate();
            return sales;
        } catch (SQLException e) {
            throw new SQLException("Error updating sale: " + sales.getSaleId(), e);
        }
    }

    // Delete a sale by id
    @Override
    public Sales delete(String id) throws SQLException {
        String sql = "DELETE FROM sales WHERE sale_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error deleting sale with ID: " + id, e);
        }
    }
}
