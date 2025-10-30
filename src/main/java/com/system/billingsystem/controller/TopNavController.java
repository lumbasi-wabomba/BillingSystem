package com.system.billingsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TopNavController {

    @FXML private Button newSaleButton;
    @FXML private Button manageInventoryButton;
    @FXML private Button paymentsButton;
    @FXML private Button salesButton;
    @FXML private Button purchasesButton;
    @FXML private Button dashboardButton;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        Runnable mock = () -> System.out.println("Feature coming soon!");

        if (dashboardButton != null) {
            dashboardButton.setOnAction(e -> {
                if (mainController != null) {
                    mainController.loadPage("/dashboard.fxml");
                }
            });
        }

        if (newSaleButton != null) newSaleButton.setOnAction(e -> mock.run());

        if (manageInventoryButton != null) {
            manageInventoryButton.setOnAction(e -> {
                if (mainController != null) {
                    mainController.loadPage("/inventory.fxml"); 
                }
            });
        }

        if (paymentsButton != null) paymentsButton.setOnAction(e -> mock.run());
        if (salesButton != null) salesButton.setOnAction(e -> mock.run());
        if (purchasesButton != null) purchasesButton.setOnAction(e -> mock.run());
    }
}
