package com.system.billingsystem.service;

import com.system.billingsystem.dao.CustomerDao;
import com.system.billingsystem.models.Customers;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public String getLastCustomerId() throws SQLException {
    return customerDao.getLastCustomerId();
    }

    public Customers getCustomer(Customers customer) throws SQLException {
        return customerDao.get(customer);
    }

    public List<Customers> getAllCustomers() throws SQLException {
        return customerDao.getAll();
    }

    public Customers saveCustomer(Customers customer) throws SQLException {
        return customerDao.save(customer);
    }

    public Customers updateCustomer(Customers customer, String[] details) throws SQLException {
        return customerDao.update(customer, details);
    }

    public Customers deleteCustomer(String id) throws SQLException {
        return customerDao.delete(id);
    }
}
