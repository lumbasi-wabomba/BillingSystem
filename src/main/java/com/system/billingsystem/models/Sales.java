package com.system.billingsystem.models;

import java.util.Date;
/*
* this class represents a sales transaction in the billing system.
* It contains details such as sale ID, customer ID, sale date, sales person ID,
* total amount, status, and payment method.
* */
public class Sales {
    private String saleId;
    private String customerId;
    private Date saleDate;
    private String salesPersonId;
    private double totalAmount;
    private  String status;
    private  String paymentMethod;

    // Constructor
    public Sales(String saleId, String customerId, Date saleDate, String salesPersonId, double totalAmount, String status, String paymentMethod) {
        this.saleId = saleId;
        this.customerId = customerId;
        this.saleDate = saleDate;
        this.salesPersonId = salesPersonId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
    };
    public Sales() {

    }

    // Getters
    public String getSaleId() {
        return saleId;
    };
    public String getCustomerId() {
        return customerId;
    };
    public Date getSaleDate() {
        return saleDate;
    };
    public String getSalesPersonId() {
        return salesPersonId;
    };
    public double getTotalAmount() {
        return totalAmount;
    };
    public String getStatus() {
        return status;
    };
    public String getPaymentMethod() {
        return paymentMethod;
    };

    // Setters
    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void setSalesPersonId(String salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
