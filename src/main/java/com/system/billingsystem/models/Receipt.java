package com.system.billingsystem.models;

import java.time.LocalDateTime;

/**
 * Represents a receipt issued for a sale or payment.
 * Tracks printed receipts separately from sales transactions.
 */
public class Receipt {
    private String receiptId;
    private String saleId;
    private String customerId;
    private double amountPaid;
    private String paymentMethod;
    private LocalDateTime issuedAt;
    private String notes;

    // Constructors
    public Receipt() {}

    public Receipt(String receiptId, String saleId, String customerId, double amountPaid,
                   String paymentMethod, LocalDateTime issuedAt, String notes) {
        this.receiptId = receiptId;
        this.saleId = saleId;
        this.customerId = customerId;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.issuedAt = issuedAt;
        this.notes = notes;
    }

    // Getters and Setters
    public String getReceiptId() { return receiptId; }
    public void setReceiptId(String receiptId) { this.receiptId = receiptId; }

    public String getSaleId() { return saleId; }
    public void setSaleId(String saleId) { this.saleId = saleId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId='" + receiptId + '\'' +
                ", saleId='" + saleId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amountPaid=" + amountPaid +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", issuedAt=" + issuedAt +
                ", notes='" + notes + '\'' +
                '}';
    }
}
