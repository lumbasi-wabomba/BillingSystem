package com.system.billingsystem.dao;

import com.system.billingsystem.models.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InvoicesDao implements Dao<Invoice> {

    // Get an invoice by invoiceId
    @Override
    public Invoice get(Invoice invoice) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoice.getInvoiceId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Invoice(
                            rs.getString("invoice_id"),
                            rs.getString("sale_id"),
                            rs.getString("customer_id"),
                            rs.getDouble("total_amount"),
                            rs.getDouble("paid_amount"),
                            rs.getDouble("balance"),
                            new Date(rs.getTimestamp("due_date").getTime()),
                            rs.getString("status"),
                            new Date(rs.getTimestamp("created_at").getTime())
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error fetching invoice: " + invoice.getInvoiceId(), e);
        }
    }

    // Get all invoices
    @Override
    public List<Invoice> getAll() throws SQLException {
        String sql = "SELECT * FROM invoices ORDER BY created_at DESC";
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Invoice(
                        rs.getString("invoice_id"),
                        rs.getString("sale_id"),
                        rs.getString("customer_id"),
                        rs.getDouble("total_amount"),
                        rs.getDouble("paid_amount"),
                        rs.getDouble("balance"),
                        new Date(rs.getTimestamp("due_date").getTime()),
                        rs.getString("status"),
                        new Date(rs.getTimestamp("created_at").getTime())
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all invoices", e);
        }
    }

    // Save an invoice
    @Override
    public Invoice save(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (invoice_id, sale_id, customer_id, total_amount, paid_amount, balance, due_date, status, created_at) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoice.getInvoiceId());
            ps.setString(2, invoice.getSaleId());
            ps.setString(3, invoice.getCustomerId());
            ps.setDouble(4, invoice.getTotalAmount());
            ps.setDouble(5, invoice.getPaidAmount());
            ps.setDouble(6, invoice.getBalance());

            // due_date as timestamp
            if (invoice.getDueDate() != null) {
                ps.setTimestamp(7, new Timestamp(invoice.getDueDate().getTime()));
            } else {
                // Default due date: 30 days from now
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 30);
                ps.setTimestamp(7, new Timestamp(cal.getTimeInMillis()));
            }

            ps.setString(8, invoice.getStatus());

            // created_at as timestamp
            if (invoice.getCreatedAt() != null) {
                ps.setTimestamp(9, new Timestamp(invoice.getCreatedAt().getTime()));
            } else {
                ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            }

            ps.executeUpdate();
            return invoice;
        } catch (SQLException e) {
            throw new SQLException("Error saving invoice", e);
        }
    }

    // Update an invoice
    @Override
    public Invoice update(Invoice invoice, String[] params) throws SQLException {
        String sql = "UPDATE invoices SET sale_id = ?, customer_id = ?, total_amount = ?, paid_amount = ?, balance = ?, due_date = ?, status = ? WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, params[0]); // sale_id
            ps.setString(2, params[1]); // customer_id
            ps.setDouble(3, Double.parseDouble(params[2])); // total_amount
            ps.setDouble(4, Double.parseDouble(params[3])); // paid_amount
            ps.setDouble(5, Double.parseDouble(params[4])); // balance
            ps.setTimestamp(6, Timestamp.valueOf(params[5].replace("T", " "))); // due_date
            ps.setString(7, params[6]); // status
            ps.setString(8, params[7]); // invoice_id (WHERE)

            ps.executeUpdate();
            return invoice;
        } catch (SQLException e) {
            throw new SQLException("Error updating invoice: " + invoice.getInvoiceId(), e);
        }
    }

    // Delete an invoice
    @Override
    public Invoice delete(String id) throws SQLException {
        String sql = "DELETE FROM invoices WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error deleting invoice with ID: " + id, e);
        }
    }

    // Get invoices by sale ID
    public List<Invoice> getInvoicesBySaleId(String saleId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE sale_id = ? ORDER BY created_at DESC";
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Invoice(
                            rs.getString("invoice_id"),
                            rs.getString("sale_id"),
                            rs.getString("customer_id"),
                            rs.getDouble("total_amount"),
                            rs.getDouble("paid_amount"),
                            rs.getDouble("balance"),
                            new Date(rs.getTimestamp("due_date").getTime()),
                            rs.getString("status"),
                            new Date(rs.getTimestamp("created_at").getTime())
                    ));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching invoices for sale: " + saleId, e);
        }
    }

    // Get invoices by customer ID
    public List<Invoice> getInvoicesByCustomerId(String customerId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE customer_id = ? ORDER BY created_at DESC";
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Invoice(
                            rs.getString("invoice_id"),
                            rs.getString("sale_id"),
                            rs.getString("customer_id"),
                            rs.getDouble("total_amount"),
                            rs.getDouble("paid_amount"),
                            rs.getDouble("balance"),
                            new Date(rs.getTimestamp("due_date").getTime()),
                            rs.getString("status"),
                            new Date(rs.getTimestamp("created_at").getTime())
                    ));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching invoices for customer: " + customerId, e);
        }
    }

    // Update payment for an invoice
    public void updatePayment(String invoiceId, double paidAmount, double balance, String status) throws SQLException {
        String sql = "UPDATE invoices SET paid_amount = ?, balance = ?, status = ? WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, paidAmount);
            ps.setDouble(2, balance);
            ps.setString(3, status);
            ps.setString(4, invoiceId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating payment for invoice: " + invoiceId, e);
        }
    }
}
