package com.system.billingsystem.models;
/*
* this class represents the products in the billing system
*it contains the following attributes: productId, productName, productCode, price, quantity, category, reorderLevel
*its the stock records for the billing system
*  */
public class Products {
    private String productId;
    private String productName;
    private String productCode;
    private double price;
    private int quantity;
    private String category;
    private  int reorderLevel;

    public Products(String productId, String productName, String productCode, double price, int quantity, String category, int reorderLevel) {
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.reorderLevel = reorderLevel;
    };

    public Products() {

    }

    public String getProductId() {
        return productId;
    };
    public String getProductName() {
        return productName;
    };
    public String getProductCode() {
        return productCode;
    };
    public double getPrice() {
        return price;
    };
    public int getQuantity() {
        return quantity;
    };
    public String getCategory() {
        return category;
    };
    public int getReorderLevel() {
        return reorderLevel;
    };

}
