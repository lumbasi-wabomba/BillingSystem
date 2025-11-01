package com.system.billingsystem.models;
public class Products {
    private String id;
    private String name;
    private String code;
    private double price;

    public Products(String id, String name, String code, double price) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }
}