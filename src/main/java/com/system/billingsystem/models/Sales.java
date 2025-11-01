package com.system.billingsystem.models;

public class Sales {
    private String id;
    private String userId;
    private String date;
    private double totalAmount;

    public Sales(String id, String userId, String date, double totalAmount) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
