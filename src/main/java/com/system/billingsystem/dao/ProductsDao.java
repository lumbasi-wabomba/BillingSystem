package com.system.billingsystem.dao;

import com.system.billingsystem.models.Products;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDao implements Dao<Products> {

    // Get a product by its product code
    @Override
    public Products get(Products products) throws SQLException {
        String sqlGet = "SELECT * FROM products WHERE product_code = ?";
        try (Connection myConnection = DatabaseConnection.getConnection();
             PreparedStatement statementGet = myConnection.prepareStatement(sqlGet)) {

            statementGet.setString(1, products.getProductCode());
            try (ResultSet productDetails = statementGet.executeQuery()) {
                if (productDetails.next()) {
                    return mapRowToProduct(productDetails);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error fetching product", e);
        }
    }

    // Get all products from the database
    @Override
    public List<Products> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM products";
        List<Products> products = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection();
             PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
             ResultSet allProducts = statementGetAll.executeQuery()) {

            while (allProducts.next()) {
                products.add(mapRowToProduct(allProducts));
            }
            return products;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all products", e);
        }
    }

    // Save a new product to the database
    @Override
    public Products save(Products products) throws SQLException {
        // generate product_id if missing
        if (products.getProductId() == null || products.getProductId().trim().isEmpty()) {
            products.setProductId(generateNextProductId());
        }

        String sqlSave = "INSERT INTO products (product_id, product_name, product_code, price, quantity, category, reorder_level) VALUES (?,?,?,?,?,?,?)";
        try (Connection myConnection = DatabaseConnection.getConnection();
             PreparedStatement statementSave = myConnection.prepareStatement(sqlSave)) {

            statementSave.setString(1, products.getProductId());
            statementSave.setString(2, products.getProductName());
            statementSave.setString(3, products.getProductCode());
            statementSave.setBigDecimal(4, java.math.BigDecimal.valueOf(products.getPrice()));
            statementSave.setInt(5, products.getQuantity());
            statementSave.setString(6, products.getCategory());
            statementSave.setInt(7, products.getReorderLevel());
            statementSave.executeUpdate();
            return products;
        } catch (SQLException e) {
            throw new SQLException("Error saving product", e);
        }
    }

    // Update a product's details
    @Override
    public Products update(Products products, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE products SET product_name = ?, product_code = ?, price = ?, quantity = ?, category = ?, reorder_level = ? WHERE product_id = ?";
        try (Connection myConnection = DatabaseConnection.getConnection();
             PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate)) {

            statementUpdate.setString(1, params[0]); // name
            statementUpdate.setString(2, params[1]); // code
            statementUpdate.setBigDecimal(3, java.math.BigDecimal.valueOf(Double.parseDouble(params[2]))); // price
            statementUpdate.setInt(4, Integer.parseInt(params[3])); // quantity
            statementUpdate.setString(5, params[4]); // category
            statementUpdate.setInt(6, Integer.parseInt(params[5])); // reorder level
            statementUpdate.setString(7, products.getProductId()); // WHERE product_id
            statementUpdate.executeUpdate();
            return products;
        } catch (SQLException e) {
            throw new SQLException("Error updating product", e);
        }
    }

    // Delete a product by its product code
    @Override
    public Products delete(String id) throws SQLException {
        String sql = "DELETE FROM products WHERE product_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            return null;
        } catch (SQLException e) {
            System.out.println("SQL Error while deleting product:");
            System.out.println("Query: " + sql);
            System.out.println("Parameter: " + id);
            System.out.println("Error message: " + e.getMessage());
            throw e;
        }
    }



    // Update stock quantity by product_id
    public void updateStock(String productId, int newQuantity) throws SQLException {
        String sqlUpdateStock = "UPDATE products SET quantity = ? WHERE product_id = ?";
        try (Connection myConnection = DatabaseConnection.getConnection();
             PreparedStatement stmt = myConnection.prepareStatement(sqlUpdateStock)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating product stock", e);
        }
    }

    // Helper: map ResultSet row to Products model
    private Products mapRowToProduct(ResultSet rs) throws SQLException {
        return new Products(
                rs.getString("product_id"),
                rs.getString("product_name"),
                rs.getString("product_code"),
                rs.getBigDecimal("price").doubleValue(),
                rs.getInt("quantity"),
                rs.getString("category"),
                rs.getInt("reorder_level")
        );
    }

    // Helper: generate next product id (P001, P002, ...)
    private String generateNextProductId() throws SQLException {
        String sql = "SELECT product_id FROM products WHERE product_id LIKE 'P%' ORDER BY product_id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String last = rs.getString("product_id"); // e.g., P012
                try {
                    int n = Integer.parseInt(last.replaceAll("\\D+", "")); // extract digits
                    return String.format("P%03d", n + 1);
                } catch (NumberFormatException ex) {
                    // fallback
                    return "P" + System.currentTimeMillis();
                }
            } else {
                return "P001";
            }
        } catch (SQLException e) {
            throw new SQLException("Error generating product id", e);
        }
    }
}
