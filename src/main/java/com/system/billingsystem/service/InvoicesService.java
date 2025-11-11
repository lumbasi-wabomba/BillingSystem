package com.system.billingsystem.service;

import com.system.billingsystem.dao.InvoicesDao;
import com.system.billingsystem.models.Invoice;

import java.sql.SQLException;
import java.util.List;

public class InvoicesService {
    private final InvoicesDao invoicesDao;

    public InvoicesService(InvoicesDao invoicesDao) {
        this.invoicesDao = invoicesDao;
    }

    // Service methods for invoice operations
    public Invoice getInvoice(Invoice invoice) throws SQLException {
        try {
            return invoicesDao.get(invoice);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public Invoice saveInvoice(Invoice invoice) throws SQLException {
        try {
            return invoicesDao.save(invoice);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public List<Invoice> getAllInvoices() throws SQLException {
        try {
            return invoicesDao.getAll();
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public Invoice updateInvoice(Invoice invoice, String[] invoiceDetails) throws SQLException {
        try {
            return invoicesDao.update(invoice, invoiceDetails);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public Invoice deleteInvoice(String id) throws SQLException {
        try {
            return invoicesDao.delete(id);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public List<Invoice> getInvoicesBySaleId(String saleId) throws SQLException {
        try {
            return invoicesDao.getInvoicesBySaleId(saleId);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public List<Invoice> getInvoicesByCustomerId(String customerId) throws SQLException {
        try {
            return invoicesDao.getInvoicesByCustomerId(customerId);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }

    public void updatePayment(String invoiceId, double paidAmount, double balance, String status) throws SQLException {
        try {
            invoicesDao.updatePayment(invoiceId, paidAmount, balance, status);
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage(), e);
        }
    }
}
