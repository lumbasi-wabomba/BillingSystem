package com.system.billingsystem.dao;

import com.system.billingsystem.models.SalesItems;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesItemsDao implements Dao<SalesItems> {

    // Get a sales item by item id
    @Override
    public SalesItems get(SalesItems salesItems) throws SQLException {
        String sql = "SELECT * FROM sales_items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, salesItems.getItemId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SalesItems(
                            rs.getString("item_id"),
                            rs.getString("product_id"),
                            rs.getString("sale_id"),
                            rs.getString("product_name"),
                            rs.getString("product_code"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            rs.getDouble("total"),
                            new Date(rs.getTimestamp("sale_date").getTime())
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error fetching sales item: " + salesItems.getItemId(), e);
        }
    }

    // Get all sales items
    @Override
    public List<SalesItems> getAll() throws SQLException {
        String sql = "SELECT * FROM sales_items";
        List<SalesItems> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new SalesItems(
                        rs.getString("item_id"),
                        rs.getString("product_id"),
                        rs.getString("sale_id"),
                        rs.getString("product_name"),
                        rs.getString("product_code"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("total"),
                        new Date(rs.getTimestamp("sale_date").getTime())
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all sales items", e);
        }
    }

    // Save a sales item
    @Override
    public SalesItems save(SalesItems salesItems) throws SQLException {
        String sql = "INSERT INTO sales_items (item_id, product_id, sale_id, product_name, product_code, quantity, price, total, sale_date) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, salesItems.getItemId());
            ps.setString(2, salesItems.getProductId());
            ps.setString(3, salesItems.getSaleId());
            ps.setString(4, salesItems.getProductName());
            ps.setString(5, salesItems.getProductCode());
            ps.setInt(6, salesItems.getQuantity());
            ps.setDouble(7, salesItems.getPrice());
            ps.setDouble(8, salesItems.getTotal());

            // sale_date as timestamp
            if (salesItems.getDate() != null) {
                ps.setTimestamp(9, new Timestamp(salesItems.getDate().getTime()));
            } else {
                ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            }

            ps.executeUpdate();
            return salesItems;
        } catch (SQLException e) {
            throw new SQLException("Error saving sales item", e);
        }
    }

    // Update sales item
    @Override
    public SalesItems update(SalesItems salesItems, String[] params) throws SQLException {
        String sql = "UPDATE sales_items SET product_id = ?, sale_id = ?, product_name = ?, product_code = ?, quantity = ?, price = ?, total = ?, sale_date = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, params[0]); // product_id
            ps.setString(2, params[1]); // sale_id
            ps.setString(3, params[2]); // product_name
            ps.setString(4, params[3]); // product_code
            ps.setInt(5, Integer.parseInt(params[4])); // quantity
            ps.setDouble(6, Double.parseDouble(params[5])); // price
            ps.setDouble(7, Double.parseDouble(params[6])); // total
            ps.setTimestamp(8, Timestamp.valueOf(params[7].replace("T", " "))); // sale_date
            ps.setString(9, params[8]); // item_id (WHERE)

            ps.executeUpdate();
            return salesItems;
        } catch (SQLException e) {
            throw new SQLException("Error updating sales item: " + salesItems.getItemId(), e);
        }
    }

    // Delete a sales item
    @Override
    public SalesItems delete(String id) throws SQLException {
        String sql = "DELETE FROM sales_items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error deleting sales item with id: " + id, e);
        }
    }

    // Extra: get items by sale id (useful for receipts)
    public List<SalesItems> getItemsBySaleId(String saleId) throws SQLException {
        String sql = "SELECT * FROM sales_items WHERE sale_id = ?";
        List<SalesItems> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new SalesItems(
                            rs.getString("item_id"),
                            rs.getString("product_id"),
                            rs.getString("sale_id"),
                            rs.getString("product_name"),
                            rs.getString("product_code"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            rs.getDouble("total"),
                            new Date(rs.getTimestamp("sale_date").getTime())
                    ));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error fetching sales items for sale: " + saleId, e);
        }
    }
}
