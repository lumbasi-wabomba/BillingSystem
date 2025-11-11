package com.system.billingsystem.models;

/**
 * This class represents an item in the shopping cart for the POS system.
 * It contains a product and the quantity being purchased.
 */
public class CartItem {
    private Products product;
    private int quantity;

    // Constructor
    public CartItem(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters
    public Products getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate line total
    public double getLineTotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return product.getProductName() + " x " + quantity;
    }
}
