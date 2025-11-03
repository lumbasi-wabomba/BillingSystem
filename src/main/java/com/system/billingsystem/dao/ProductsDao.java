package com.system.billingsystem.dao;

import com.system.billingsystem.models.Products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsDao  implements  Dao<Products> {
    @Override
    public Products get(Products products) throws SQLException {
        String sqlGet = "SELECT * FROM products WHERE productId = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);
            statementGet.setString(1, products.getProductId());
            statementGet.setString(2, products.getProductName());
            statementGet.setString(3, products.getProductCode());
            statementGet.setDouble(4, products.getPrice());
            statementGet.setInt(5, products.getQuantity());
            statementGet.setString(6, products.getCategory());
            statementGet.setInt(7, products.getReorderLevel());
            return (Products) statementGet.executeQuery();
        } catch (SQLException e) {
            throw new SQLException("Error fetching product" + 1+ 2, e);

        }
    }

    @Override
    public List<Products> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM products";
        List<Products> products = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allProducts = statementGetAll.executeQuery();
            while (allProducts.next()) {
                products.add(new Products());
            };
            return products;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all products", e);
        }
    }

    @Override
    public Products save(Products products) throws SQLException {
        String sqlSave = "INSERT  INTO products (productId,productName,productCode,price,quantity,category,reorderLevel) VALUES (?,?,?,?,?,?,?)";
        try(Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementSave = myConnection.prepareStatement(sqlSave);
            statementSave.setString(1, products.getProductId());
            statementSave.setString(2, products.getProductName());
            statementSave.setString(3, products.getProductCode());
            statementSave.setDouble(4, products.getPrice());
            statementSave.setInt(5, products.getQuantity());
            statementSave.setString(6, products.getCategory());
            statementSave.setInt(7, products.getReorderLevel());
            statementSave.executeUpdate();
            return products;
        } catch (SQLException e) {
            throw new SQLException("Error saving product", e);
        }
    }

    @Override
    public Products update(Products products, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE products SET productName = ?, productCode = ?, price = ?, quantity = ?, category = ?, reorderLevel = ? WHERE productId = ?";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
            statementUpdate.setDouble(3, Double.parseDouble(params[2]));
            statementUpdate.setInt(4, Integer.parseInt(params[3]));
            statementUpdate.setString(5, params[4]);
            statementUpdate.setInt(6, Integer.parseInt(params[5]));
            statementUpdate.setString(7, products.getProductId());
            statementUpdate.executeUpdate();
            return products;
        } catch (SQLException e) {
            throw new SQLException("Error updating product", e);
        }
    }

    @Override
    public Products delete(String id) throws SQLException {
        String sqlDelete = "DELETE FROM products WHERE productId = ?";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementDelete = myConnection.prepareStatement(sqlDelete);
            statementDelete.setString(1, id);
            statementDelete.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error deleting product", e);
        }
    }
}
