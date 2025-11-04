package com.system.billingsystem.models;

import java.util.Date;

/*
* this class represents a sales item in the billing system.
* it contains information about the item sold, including its id, product id, sale id,
* product name, product code, quantity, price, total amount, and date of sale.
*/
public class SalesItems {
    private String itemId;
    private String productId;
    private String saleId;
    private  String productName;
    private String productCode;
    private int quantity;
    private double price;
    private double total;
    //private Date date;

    // Constructor
    public SalesItems(String itemId, String productId, String saleId, String productName, String productCode, int quantity, double price, double total) {
        this.itemId = itemId;
        this.productId = productId;
        this.saleId = saleId;
        this.productName = productName;
        this.productCode = productCode;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
       // this.date = date;
    };
    public SalesItems() {}

    // Getters
    public String getItemId() {
        return itemId;
    };
    public String getProductId() {
        return productId;
    };
    public String getSaleId() {
        return saleId;
    };
    public String getProductName() {
        return productName;
    };
    public String getProductCode() {
        return productCode;
    };
    public int getQuantity() {
        return quantity;
    };
    public double getPrice() {
        return price;
    };
    public double getTotal() {
        return total;
    };
//    public Date getDate() {
//        return date;
//    };

}
