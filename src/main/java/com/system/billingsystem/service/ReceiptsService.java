package com.system.billingsystem.service;

import com.system.billingsystem.dao.ReceiptsDao;
import com.system.billingsystem.models.Receipt;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ReceiptsService {
    private final ReceiptsDao receiptsDao;

    public ReceiptsService(ReceiptsDao receiptsDao) {
        this.receiptsDao = receiptsDao;
    }

    public Receipt getReceipt(Receipt receipt) throws SQLException {
        return receiptsDao.get(receipt);
    }

    public List<Receipt> getAllReceipts() throws SQLException {
        return receiptsDao.getAll();
    }

    public Receipt saveReceipt(Receipt receipt) throws SQLException {
        // Business logic: validate receipt data
        if (receipt.getReceiptId() == null || receipt.getReceiptId().trim().isEmpty()) {
            throw new IllegalArgumentException("Receipt ID cannot be null or empty");
        }
        if (receipt.getSaleId() == null || receipt.getSaleId().trim().isEmpty()) {
            throw new IllegalArgumentException("Sale ID cannot be null or empty");
        }
        if (receipt.getAmountPaid() < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative");
        }

        return receiptsDao.save(receipt);
    }

    public Receipt updateReceipt(Receipt receipt, String[] receiptDetails) throws SQLException {
        return receiptsDao.update(receipt, receiptDetails);
    }

    public Receipt deleteReceipt(String id) throws SQLException {
        return receiptsDao.delete(id);
    }

    public List<Receipt> getReceiptsBySaleId(String saleId) throws SQLException {
        return receiptsDao.getReceiptsBySaleId(saleId);
    }

    public List<Receipt> getReceiptsByCustomerId(String customerId) throws SQLException {
        return receiptsDao.getReceiptsByCustomerId(customerId);
    }

    public Receipt createReceiptForPayment(String saleId, String customerId, double amountPaid,
                                           String paymentMethod, String notes) throws SQLException {
        String receiptId = "RCP" + System.currentTimeMillis();
        Receipt receipt = new Receipt(receiptId, saleId, customerId, amountPaid,
                paymentMethod, LocalDateTime.now(), notes);
        return saveReceipt(receipt);
    }
}
