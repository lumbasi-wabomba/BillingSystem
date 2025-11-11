package com.system.billingsystem.controller;

import com.system.billingsystem.models.Sales;
import com.system.billingsystem.models.SalesItems;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReceiptController {

    @FXML private VBox receiptRoot;
    @FXML private Label saleIdLabel;
    @FXML private Label dateLabel;
    @FXML private Label customerLabel;
    @FXML private Label cashierLabel;

    @FXML private TableView<SalesItems> itemsTable;
    @FXML private TableColumn<SalesItems, String> colProduct;
    @FXML private TableColumn<SalesItems, Integer> colQty;
    @FXML private TableColumn<SalesItems, Double> colPrice;
    @FXML private TableColumn<SalesItems, Double> colTotal;

    @FXML private Label subtotalLabel;
    @FXML private Label discountLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private Label notesLabel;
    @FXML private Label paidAmountLabel;
    @FXML private Label balanceLabel;

    @FXML private Button printBtn;
    @FXML private Button closeBtn;

    private Sales sale;
    private List<SalesItems> saleItems;
    private double discount;
    private String notes;
    private String customerName;
    private double paidAmount;
    private double balance;

    @FXML
    public void initialize() {
        setupTable();
        setupButtons();
    }

    private void setupTable() {
        // Value factories
        colProduct.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getProductName()));

        colQty.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getQuantity()));

        colPrice.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));

        colTotal.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotal()));

        // Format currency columns with KSH prefix
        colPrice.setCellFactory(col -> new TableCell<SalesItems, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("KSH %.2f", price));
                }
            }
        });

        colTotal.setCellFactory(col -> new TableCell<SalesItems, Double>() {
            @Override
            protected void updateItem(Double tot, boolean empty) {
                super.updateItem(tot, empty);
                if (empty || tot == null) {
                    setText(null);
                } else {
                    setText(String.format("KSH %.2f", tot));
                }
            }
        });

        // Optional: disable editing / resizing if desired
        if (itemsTable != null) {
            itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
    }

    private void setupButtons() {
        if (printBtn != null) printBtn.setOnAction(e -> printReceipt());
        if (closeBtn != null) closeBtn.setOnAction(e -> closeWindow());
    }

    /**
     * Set the sale data to display on the receipt.
     */
    public void setSaleData(Sales sale, List<SalesItems> items, double discount, String notes, String customerName) {
        this.sale = sale;
        this.saleItems = items;
        this.discount = discount;
        this.notes = notes;
        this.customerName = customerName;
        this.paidAmount = 0.0; // Default for full payment receipts
        this.balance = 0.0; // Default for full payment receipts
        displayReceiptData();
    }

    /**
     * Set the sale data with partial payment info.
     */
    public void setSaleDataWithPayment(Sales sale, List<SalesItems> items, double discount, String notes, String customerName, double paidAmount, double balance) {
        this.sale = sale;
        this.saleItems = items;
        this.discount = discount;
        this.notes = notes;
        this.customerName = customerName;
        this.paidAmount = paidAmount;
        this.balance = balance;
        displayReceiptData();
    }

    private void displayReceiptData() {
        if (sale == null) return;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Safe fallbacks
        saleIdLabel.setText(sale.getSaleId() != null ? sale.getSaleId() : "-");
        dateLabel.setText(sale.getSaleDate() != null ? dateFormat.format(sale.getSaleDate()) : "-");
        customerLabel.setText(customerName != null && !customerName.isBlank() ? customerName : "Walk-in Customer");
        cashierLabel.setText(sale.getSalesPersonId() != null ? sale.getSalesPersonId() : "-");
        paymentMethodLabel.setText(sale.getPaymentMethod() != null ? sale.getPaymentMethod() : "-");

        notesLabel.setText((notes != null && !notes.trim().isEmpty()) ? ("Notes: " + notes) : "");

        // Display partial payment info if applicable
        if (paidAmountLabel != null) {
            paidAmountLabel.setText(String.format("KSH %.2f", paidAmount));
            paidAmountLabel.setVisible(paidAmount > 0);
        }
        if (balanceLabel != null) {
            balanceLabel.setText(String.format("KSH %.2f", balance));
            balanceLabel.setVisible(balance > 0);
            balanceLabel.setStyle(balance > 0 ? "-fx-text-fill: #e74c3c; -fx-font-weight: bold;" : "");
        }

        // Prepare items list safely
        List<SalesItems> itemsSafe = (saleItems == null) ? new ArrayList<>() : saleItems;
        ObservableList<SalesItems> items = FXCollections.observableArrayList(itemsSafe);
        itemsTable.setItems(items);

        // Totals calculation using the safe list
        double subtotal = items.stream().mapToDouble(i -> i.getTotal()).sum();
        double taxableAmount = Math.max(0, subtotal - discount);
        double tax = taxableAmount * 0.16; // 16% VAT
        double total = taxableAmount + tax;

        subtotalLabel.setText(String.format("KSH %.2f", subtotal));
        discountLabel.setText(String.format("KSH %.2f", discount));
        taxLabel.setText(String.format("KSH %.2f", tax));
        totalLabel.setText(String.format("KSH %.2f", total));
    }

    private void printReceipt() {
        if (receiptRoot == null || receiptRoot.getScene() == null || receiptRoot.getScene().getWindow() == null) {
            showAlert(Alert.AlertType.ERROR, "Print Error", "Receipt window is not available for printing.");
            return;
        }

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob == null) {
            showAlert(Alert.AlertType.ERROR, "Printer Not Available", "No printer is available. Please check your printer settings.");
            return;
        }

        boolean showDialog = printerJob.showPrintDialog(receiptRoot.getScene().getWindow());
        if (!showDialog) return;

        boolean success = printerJob.printPage(receiptRoot);
        if (success) {
            printerJob.endJob();
            showAlert(Alert.AlertType.INFORMATION, "Print Successful", "Receipt printed successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Print Failed", "Failed to print receipt. Please try again.");
        }
    }

    private void closeWindow() {
        if (closeBtn != null && closeBtn.getScene() != null) {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
