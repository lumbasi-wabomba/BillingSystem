package com.system.billingsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        setupNavigationButtons();
    }

    private void setupNavigationButtons() {
        if (dashboardButton != null) {
            dashboardButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/dashboard.fxml"));
        }

        if (newSaleButton != null) {
            newSaleButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/pos.fxml"));
        }

        if (manageInventoryButton != null) {
            manageInventoryButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/inventory.fxml"));
        }

        if (salesButton != null) {
            salesButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/sales.fxml"));
        }

        if (invoicesButton != null) {
            invoicesButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/invoice.fxml"));
        }

        if (receiptsButton != null) {
            receiptsButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/receipt.fxml"));
        }

        if (customersButton != null) {
            customersButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/customers.fxml"));
        }


        if (receiptsButton != null) {
            receiptsButton.setOnAction(e -> navigateToPage("/com/system/billingsystem/receipts.fxml"));
        }

        if (paymentsButton != null) {
            paymentsButton.setOnAction(e -> showComingSoonMessage("Payments"));
        }

        if (purchasesButton != null) {
            purchasesButton.setOnAction(e -> showComingSoonMessage("Purchases"));
        }
    }

    private void navigateToPage(String fxmlPath) {
        if (mainController != null) {
            mainController.loadPage(fxmlPath);
        } else {
            System.err.println("MainController not set. Cannot navigate to: " + fxmlPath);
        }
    }

    private void showComingSoonMessage(String featureName) {
        System.out.println(featureName + " feature coming soon!");
    }
}
