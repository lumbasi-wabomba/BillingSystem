package com.system.billingsystem.service;

import com.system.billingsystem.dao.CustomerDao;
import com.system.billingsystem.models.Customers;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private  final CustomerDao customerDao;

    // Constructor
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    // Service methods for customer operations
    public   Customers getCustomer(String email) throws SQLException{
        try{
            return customerDao.get(email);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e );
        }
    }
    public List<Customers> getAllCustomers() throws  SQLException{
        try{
            return  customerDao.getAll();
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public Customers saveCustomer(Customers customer) throws SQLException{
        try{
            return customerDao.save(customer);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public Customers updateCustomer(Customers customer, String[] details) throws  SQLException{
        try{
            return customerDao.update(customer, details);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public Customers deleteCustomer(String id) throws  SQLException{
        try{
            return customerDao.delete(id);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }



}
