package com.system.billingsystem.service;

import com.system.billingsystem.dao.SalesDao;
import com.system.billingsystem.models.Sales;

import java.sql.SQLException;
import java.util.List;

public class SalesService {
    private  final SalesDao salesDao;

    public SalesService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    // Service methods for sales operations
    public Sales getSale(Sales sale) throws SQLException {
        try{
            return  salesDao.get(sale);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public List<Sales> getAllSales() throws  SQLException{
        try{
            return salesDao.getAll();
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public Sales updateProduct(Sales sale, String[] saleDetails) throws  SQLException{
        try{
            return salesDao.update(sale, saleDetails);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public Sales deleteSale(String id) throws  SQLException{
        try{
            return salesDao.delete(id);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(),e);
        }
    }
}