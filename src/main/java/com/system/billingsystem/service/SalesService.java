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
    
    public Sales saveSale(Sales sale) throws SQLException {
        try{
            return salesDao.save(sale);
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
    public Sales updateSale(Sales sale, String[] saleDetails) throws  SQLException{
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

    // Finalize sale with payment logic
    public Sales finalizeSale(Sales sale, double paymentAmount) throws SQLException {
        try {
            double totalAmount = sale.getTotalAmount();
            String status;

            if (paymentAmount >= totalAmount) {
                status = "completed";
            } else if (paymentAmount > 0) {
                status = "partial";
            } else {
                status = "pending";
            }

            // Update sale status
            sale.setStatus(status);
            String[] params = {
                sale.getCustomerId(),
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sale.getSaleDate()),
                sale.getSalesPersonId(),
                String.valueOf(totalAmount),
                status,
                sale.getPaymentMethod(),
                sale.getSaleId()
            };

            return salesDao.update(sale, params);
        } catch (Exception e) {
            throw new SQLException("Error finalizing sale: " + e.getMessage(), e);
        }
    }
}
