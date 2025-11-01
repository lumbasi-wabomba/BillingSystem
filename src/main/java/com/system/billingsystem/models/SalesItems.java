package com.system.billingsystem.models;

public class SalesItems {

    private String id;
    private String saleId;
    private String productId;
    private String productName;
    private String productCode;

    public SalesItems(String id, String saleId, String productId, String productName, String productCode) {
        this.id = id;
        this.saleId = saleId;
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getSaleId() {
        return saleId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }
}
