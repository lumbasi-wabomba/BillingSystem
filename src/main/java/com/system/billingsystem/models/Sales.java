package com.system.billingsystem.models;

import java.sql.Date;
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
    public Sales(String saleId, String customerId, String salesPersonId, double totalAmount, String status, String paymentMethod, Date saleDate) {
        this.saleId = saleId;
        this.customerId = customerId;
        this.saleDate = saleDate;
        this.salesPersonId = salesPersonId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;

    };

    @Override
    public String toString() {
        return "Sales{" +
                "saleId='" + saleId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", saleDate=" + saleDate +
                ", salesPersonId='" + salesPersonId + '\'' +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
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
}
