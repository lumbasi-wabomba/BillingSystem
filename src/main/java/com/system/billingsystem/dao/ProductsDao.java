package com.system.billingsystem.dao;

import com.system.billingsystem.models.Products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsDao  implements  Dao<Products> {

    // Get a product by its product code
    @Override
    public Products get(Products products) throws SQLException {
        String sqlGet = "SELECT * FROM products WHERE products_productcode = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);

            statementGet.setString(1, products.getProductCode());
            ResultSet productDetails = statementGet.executeQuery();
            while (productDetails.next()) {
                Products foundDetails = new Products(
                        productDetails.getString("products_productid"),
                        productDetails.getString("products_productname"),
                        productDetails.getString("products_productcode"),
                        productDetails.getDouble("products_price"),
                        productDetails.getInt("products_quantity"),
                        productDetails.getString("products_category"),
                        productDetails.getInt("products_reorderLevel")
                );
                return foundDetails;
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error fetching product" + 1+ 2, e);

        }
    }

    // Get all products from the database
    @Override
    public List<Products> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM products";
        List<Products> products = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allProducts = statementGetAll.executeQuery();
            while (allProducts.next()) {
                products.add(new Products(
                        allProducts.getString("products_productid"),
                        allProducts.getString("products_productname"),
                        allProducts.getString("products_productcode"),
                        allProducts.getDouble("products_price"),
                        allProducts.getInt("products_quantity"),
                        allProducts.getString("products_category"),
                        allProducts.getInt("products_reorderLevel")
                ));
            };
            return products;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all products", e);
        }
    }

    // Save a new product to the database
    @Override
    public Products save(Products products) throws SQLException {
        String sqlSave = "INSERT  INTO products (products_productid, products_productname, products_productcode, products_price, products_quantity, products_category, products_reorderlevel) VALUES (?,?,?,?,?,?,?)";
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

    // Update a product's details
    @Override
    public Products update(Products products, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE products SET products_productname = ?, products_productcode = ?, products_price = ?, products_quantity = ?, products_category = ?, products_reorderLevel = ? WHERE products_productcode = ?";
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

    // Delete a product by its product code
    @Override
    public Products delete(String id) throws SQLException {
        String sqlDelete = "DELETE FROM products WHERE products_productcode = ?";
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
