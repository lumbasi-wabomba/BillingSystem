package com.system.billingsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the top navigation bar.
 * Handles navigation between different sections of the application.
 */
public class TopNavController {

    @FXML private Button dashboardButton;
    @FXML private Button newSaleButton;
    @FXML private Button manageInventoryButton;
    @FXML private Button salesButton;
    @FXML private Button invoicesButton;
    @FXML private Button receiptsButton;
    @FXML private Button customersButton;
    @FXML private Button paymentsButton;
    @FXML private Button purchasesButton;

    private MainController mainController;

    /**
     * Set the main controller reference for navigation
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        setupNavigationButtons();
    }

    private void setupNavigationButtons() {
        // Dashboard button
        if (dashboardButton != null) {
            dashboardButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/dashboard.fxml"));
        }

        // New Sale (POS) button
        if (newSaleButton != null) {
            newSaleButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/pos.fxml"));
        }

        // Manage Inventory button
        if (manageInventoryButton != null) {
            manageInventoryButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/inventory.fxml"));
        }

        // Sales button
        if (salesButton != null) {
            salesButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/sales.fxml"));
        }

        // Invoices button
        if (invoicesButton != null) {
            invoicesButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/invoice.fxml"));
        }

        // Receipts button
        if (receiptsButton != null) {
            receiptsButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/receipt.fxml"));
        }

        // Customers button
        if (customersButton != null) {
            customersButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/customers.fxml"));
        }

        // Receipts button
        if (receiptsButton != null) {
            receiptsButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/receipts.fxml"));
        }

        // Payments button (future feature)
        if (paymentsButton != null) {
            paymentsButton.setOnAction(e -> showComingSoonMessage("Payments"));
        }

        // Purchases button (future feature)
        if (purchasesButton != null) {
            purchasesButton.setOnAction(e -> showComingSoonMessage("Purchases"));
        }
    }

    /**
     * Navigate to a specific page
     */
    private void navigateToPage(String fxmlPath) {
        if (mainController != null) {
            mainController.loadPage(fxmlPath);
        } else {
            System.err.println("MainController not set. Cannot navigate to: " + fxmlPath);
        }
    }

    /**
     * Show a message for features that are coming soon
     */
    private void showComingSoonMessage(String featureName) {
        System.out.println(featureName + " feature coming soon!");
        // TODO: Show a proper alert dialog to the user
    }
}
