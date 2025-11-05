package com.system.billingsystem.service;

import com.system.billingsystem.dao.ProductsDao;
import com.system.billingsystem.models.Products;

import java.sql.SQLException;
import java.util.List;

public class ProductsService {
    private  final ProductsDao productsDao;

    public ProductsService() {
        this.productsDao = new ProductsDao();
    }

    // Service methods for product operations
    public Products getProduct(String productCode) throws SQLException {
        try{
            return productsDao.get(productCode);
        } catch (SQLException e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public List<Products> getAllProducts() throws  SQLException{
        try{
            return productsDao.getAll();
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public Products save(Products product) throws SQLException{
        try{
            return productsDao.save(product);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public Products updateProduct(Products products, String[] productDetails) throws  SQLException{
        try{
            return productsDao.update(products, productDetails);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public  Products deleteProduct(String id) throws  SQLException {
        try{
            return productsDao.delete(id);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }

}
