package com.system.billingsystem.models;

public class Products {
    private String productId;
    private String productName;
    private String productCode;
    private double price;
    private int quantity;
    private String category;
    private int reorderLevel;

    public Products(String productId, String productName, String productCode, double price, int quantity, String category, int reorderLevel) {
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.reorderLevel = reorderLevel;
    }
    public Products() {}

    // Getters and setters...
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductCode() { return productCode; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public int getReorderLevel() { return reorderLevel; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    @Override
    public String toString() {
        return "Products{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", reorderLevel=" + reorderLevel +
                '}';
    }
}
