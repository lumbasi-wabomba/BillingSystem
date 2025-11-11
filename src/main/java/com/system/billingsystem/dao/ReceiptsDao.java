package com.system.billingsystem.dao;

import com.system.billingsystem.models.Receipt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceiptsDao implements Dao<Receipt> {

    @Override
    public Receipt get(Receipt receipt) throws SQLException {
        String sql = "SELECT * FROM receipts WHERE receipt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, receipt.getReceiptId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReceipt(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error fetching receipt: " + receipt.getReceiptId(), e);
        }
    }

    @Override
    public List<Receipt> getAll() throws SQLException {
        String sql = "SELECT * FROM receipts ORDER BY issued_at DESC";
        List<Receipt> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToReceipt(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all receipts", e);
        }
    }

    @Override
    public Receipt save(Receipt receipt) throws SQLException {
        String sql = "INSERT INTO receipts (receipt_id, sale_id, customer_id, amount_paid, payment_method, issued_at, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, receipt.getReceiptId());
            ps.setString(2, receipt.getSaleId());
            ps.setString(3, receipt.getCustomerId());
            ps.setDouble(4, receipt.getAmountPaid());
            ps.setString(5, receipt.getPaymentMethod());

            LocalDateTime issuedAt = receipt.getIssuedAt() != null ? receipt.getIssuedAt() : LocalDateTime.now();
            ps.setTimestamp(6, Timestamp.valueOf(issuedAt));

            ps.setString(7, receipt.getNotes());

            ps.executeUpdate();
            return receipt;
        } catch (SQLException e) {
            throw new SQLException("Error saving receipt", e);
        }
    }

    @Override
    public Receipt update(Receipt receipt, String[] params) throws SQLException {
        String sql = "UPDATE receipts SET sale_id = ?, customer_id = ?, amount_paid = ?, payment_method = ?, issued_at = ?, notes = ? WHERE receipt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, params[0]); // sale_id
            ps.setString(2, params[1]); // customer_id
            ps.setDouble(3, Double.parseDouble(params[2])); // amount_paid
            ps.setString(4, params[3]); // payment_method
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.parse(params[4]))); // issued_at
            ps.setString(6, params[5]); // notes
            ps.setString(7, params[6]); // receipt_id (WHERE)

            ps.executeUpdate();
            return receipt;
        } catch (SQLException e) {
            throw new SQLException("Error updating receipt: " + receipt.getReceiptId(), e);
        }
    }

    @Override
    public Receipt delete(String id) throws SQLException {
        String sql = "DELETE FROM receipts WHERE receipt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error deleting receipt with ID: " + id, e);
        }
    }

    public List<Receipt> getReceiptsBySaleId(String saleId) throws SQLException {
        String sql = "SELECT * FROM receipts WHERE sale_id = ? ORDER BY issued_at DESC";
        return getReceiptsByField(saleId, sql);
    }

    public List<Receipt> getReceiptsByCustomerId(String customerId) throws SQLException {
        String sql = "SELECT * FROM receipts WHERE customer_id = ? ORDER BY issued_at DESC";
        return getReceiptsByField(customerId, sql);
    }

    // Helper method to avoid repeating code
    private List<Receipt> getReceiptsByField(String value, String sql) throws SQLException {
        List<Receipt> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReceipt(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching receipts for value: " + value, e);
        }
    }

    private Receipt mapResultSetToReceipt(ResultSet rs) throws SQLException {
        return new Receipt(
                rs.getString("receipt_id"),
                rs.getString("sale_id"),
                rs.getString("customer_id"),
                rs.getDouble("amount_paid"),
                rs.getString("payment_method"),
                rs.getTimestamp("issued_at").toLocalDateTime(),
                rs.getString("notes")
        );
    }
}
