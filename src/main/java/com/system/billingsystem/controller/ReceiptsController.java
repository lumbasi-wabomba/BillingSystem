package com.system.billingsystem.controller;

import com.system.billingsystem.dao.ReceiptsDao;
import com.system.billingsystem.models.Receipt;
import com.system.billingsystem.service.ReceiptsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for the Receipts History view.
 * Displays all issued receipts and allows viewing receipt details.
 */
public class ReceiptsController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button clearSearchButton;
    @FXML private Button refreshButton;

    @FXML private TableView<Receipt> receiptsTable;
    @FXML private TableColumn<Receipt, String> colReceiptId;
    @FXML private TableColumn<Receipt, String> colSaleId;
    @FXML private TableColumn<Receipt, String> colCustomerId;
    @FXML private TableColumn<Receipt, Double> colAmountPaid;
    @FXML private TableColumn<Receipt, String> colPaymentMethod;
    @FXML private TableColumn<Receipt, String> colIssuedAt;
    @FXML private TableColumn<Receipt, String> colNotes;
    @FXML private TableColumn<Receipt, Void> colActions;

    private ReceiptsService receiptsService;
    private ObservableList<Receipt> allReceipts = FXCollections.observableArrayList();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        initializeServices();
        setupTableColumns();
        setupActionColumn();
        setupSearchFunctionality();
        loadReceiptsData();
    }

    private void initializeServices() {
        try {
            receiptsService = new ReceiptsService(new ReceiptsDao());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error",
                      "Failed to initialize services: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        colReceiptId.setCellValueFactory(new PropertyValueFactory<>("receiptId"));
        colSaleId.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colAmountPaid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

        // Format issued date from LocalDateTime
        colIssuedAt.setCellValueFactory(data -> {
            if (data.getValue().getIssuedAt() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getIssuedAt().format(DATE_FORMATTER)
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // Format amount paid as currency
        colAmountPaid.setCellFactory(col -> new TableCell<Receipt, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("KSH %.2f", amount));
                }
            }
        });
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            private final Button printBtn = new Button("Print");

            {
                viewBtn.setOnAction(e -> {
                    Receipt receipt = getTableView().getItems().get(getIndex());
                    showReceiptDetails(receipt);
                });

                printBtn.setOnAction(e -> {
                    Receipt receipt = getTableView().getItems().get(getIndex());
                    printReceipt(receipt);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, viewBtn, printBtn);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void setupSearchFunctionality() {
        searchButton.setOnAction(e -> performSearch());
        clearSearchButton.setOnAction(e -> {
            searchField.clear();
            receiptsTable.setItems(allReceipts);
        });
        searchField.setOnAction(e -> performSearch());
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            receiptsTable.setItems(allReceipts);
            return;
        }

        ObservableList<Receipt> filtered = allReceipts.filtered(receipt ->
                (receipt.getReceiptId() != null && receipt.getReceiptId().toLowerCase().contains(searchText)) ||
                (receipt.getSaleId() != null && receipt.getSaleId().toLowerCase().contains(searchText)) ||
                (receipt.getCustomerId() != null && receipt.getCustomerId().toLowerCase().contains(searchText)) ||
                (receipt.getPaymentMethod() != null && receipt.getPaymentMethod().toLowerCase().contains(searchText)) ||
                (receipt.getNotes() != null && receipt.getNotes().toLowerCase().contains(searchText))
        );

        receiptsTable.setItems(filtered);
    }

    @FXML
    private void handleRefresh() {
        loadReceiptsData();
    }

    private void loadReceiptsData() {
        try {
            List<Receipt> receipts = receiptsService.getAllReceipts();
            allReceipts.setAll(receipts);
            receiptsTable.setItems(allReceipts);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                      "Failed to load receipts: " + e.getMessage());
        }
    }

    private void showReceiptDetails(Receipt receipt) {
        String message = String.format(
                "Receipt ID: %s%nSale ID: %s%nAmount Paid: KSH %.2f%nPayment Method: %s%nIssued At: %s%nNotes: %s",
                receipt.getReceiptId(),
                receipt.getSaleId(),
                receipt.getAmountPaid(),
                receipt.getPaymentMethod(),
                receipt.getIssuedAt() != null ? receipt.getIssuedAt().format(DATE_FORMATTER) : "N/A",
                receipt.getNotes() != null ? receipt.getNotes() : "N/A"
        );

        showAlert(Alert.AlertType.INFORMATION, "Receipt Details", message);
    }

    private void printReceipt(Receipt receipt) {
        showAlert(Alert.AlertType.INFORMATION, "Print Receipt",
                "Printing functionality for receipt " + receipt.getReceiptId() + " would be implemented here.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
