package com.system.billingsystem.models;

import java.util.Date;

/**
 * This class represents an invoice in the billing system.
 * It contains details such as invoice ID, sale ID, customer ID,
 * total amount, paid amount, balance, due date, and status.
 */
public class Invoice {
    private String invoiceId;
    private String saleId;
    private String customerId;
    private double totalAmount;
    private double paidAmount;
    private double balance;
    private Date dueDate;
    private String status;
    private Date createdAt;

    // Constructor
    public Invoice(String invoiceId, String saleId, String customerId, double totalAmount,
                   double paidAmount, double balance, Date dueDate, String status, Date createdAt) {
        this.invoiceId = invoiceId;
        this.saleId = saleId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balance = balance;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Invoice() {}

    // Getters and Setters
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }

    public String getSaleId() { return saleId; }
    public void setSaleId(String saleId) { this.saleId = saleId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
