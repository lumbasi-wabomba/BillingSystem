package com.system.billingsystem.controller;

import com.system.billingsystem.dao.InvoicesDao;
import com.system.billingsystem.models.Invoice;
import com.system.billingsystem.service.InvoicesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the Invoices view.
 */
public class InvoiceController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button clearSearchButton;
    @FXML private Button refreshButton;

    @FXML private TableView<Invoice> invoicesTable;
    @FXML private TableColumn<Invoice, String> colId;
    @FXML private TableColumn<Invoice, String> colSaleId;
    @FXML private TableColumn<Invoice, String> colCustomerId;
    @FXML private TableColumn<Invoice, Double> colTotalAmount;
    @FXML private TableColumn<Invoice, Double> colPaidAmount;
    @FXML private TableColumn<Invoice, Double> colBalance;
    @FXML private TableColumn<Invoice, String> colStatus;
    @FXML private TableColumn<Invoice, String> colDueDate;
    @FXML private TableColumn<Invoice, String> colCreatedAt;
    @FXML private TableColumn<Invoice, Void> colActions;

    // Services
    private InvoicesService invoicesService;

    // Data
    private ObservableList<Invoice> allInvoices = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initializeServices();
        setupTableColumns();
        setupActionColumn();
        setupSearchFunctionality();
        loadInvoicesData();
    }

    private void initializeServices() {
        try {
            invoicesService = new InvoicesService(new InvoicesDao());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error",
                     "Failed to initialize services: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        colId.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getInvoiceId()));

        colSaleId.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getSaleId()));

        colCustomerId.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCustomerId()));

        colTotalAmount.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalAmount()));
        colTotalAmount.setCellFactory(col -> new TableCell<Invoice, Double>() {
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

        colPaidAmount.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPaidAmount()));
        colPaidAmount.setCellFactory(col -> new TableCell<Invoice, Double>() {
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

        colBalance.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getBalance()));
        colBalance.setCellFactory(col -> new TableCell<Invoice, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("KSH %.2f", amount));
                    if (amount > 0) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                }
            }
        });

        colStatus.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        colStatus.setCellFactory(col -> new TableCell<Invoice, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("paid".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else if ("pending".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    } else if ("overdue".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        colDueDate.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                dateFormat.format(data.getValue().getDueDate())
            ));

        colCreatedAt.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                dateFormat.format(data.getValue().getCreatedAt())
            ));
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<Invoice, Void>() {
            private final Button viewDetailsBtn = new Button("View Details");
            private final Button recordPaymentBtn = new Button("Record Payment");

            {
                viewDetailsBtn.setOnAction(e -> {
                    Invoice invoice = getTableView().getItems().get(getIndex());
                    showInvoiceDetailsDialog(invoice);
                });

                recordPaymentBtn.setOnAction(e -> {
                    Invoice invoice = getTableView().getItems().get(getIndex());
                    showRecordPaymentDialog(invoice);
                });

                viewDetailsBtn.setStyle("-fx-font-size: 10px; -fx-padding: 4 8;");
                recordPaymentBtn.setStyle("-fx-font-size: 10px; -fx-padding: 4 8;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(4);
                    hbox.getChildren().addAll(viewDetailsBtn, recordPaymentBtn);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void setupSearchFunctionality() {
        // Search button
        searchButton.setOnAction(e -> performSearch());

        // Enter key in search field
        searchField.setOnAction(e -> performSearch());

        // Clear search button
        if (clearSearchButton != null) {
            clearSearchButton.setOnAction(e -> clearSearch());
        }

        // Refresh button
        if (refreshButton != null) {
            refreshButton.setOnAction(e -> {
                loadInvoicesData();
                showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Invoices data has been refreshed.");
            });
        }

        // Real-time search as user types
        searchField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                invoicesTable.setItems(allInvoices);
            }
        });
    }

    private void clearSearch() {
        searchField.clear();
        invoicesTable.setItems(allInvoices);
    }

    private void performSearch() {
        String searchText = searchField.getText();
        if (searchText == null || searchText.trim().isEmpty()) {
            invoicesTable.setItems(allInvoices);
            return;
        }

        String search = searchText.toLowerCase();
        List<Invoice> filtered = allInvoices.stream()
            .filter(invoice ->
                invoice.getInvoiceId().toLowerCase().contains(search) ||
                invoice.getSaleId().toLowerCase().contains(search) ||
                invoice.getCustomerId().toLowerCase().contains(search) ||
                invoice.getStatus().toLowerCase().contains(search)
            )
            .collect(Collectors.toList());

        invoicesTable.setItems(FXCollections.observableArrayList(filtered));
    }

    private void loadInvoicesData() {
        try {
            List<Invoice> invoices = invoicesService.getAllInvoices();
            allInvoices.setAll(invoices);
            invoicesTable.setItems(allInvoices);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                     "Failed to load invoices data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showInvoiceDetailsDialog(Invoice invoice) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Invoice Details - " + invoice.getInvoiceId());

            // Create content for invoice details
            VBox layout = new VBox(10);
            layout.setStyle("-fx-padding: 16;");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Label titleLabel = new Label("Invoice Details");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            GridPane detailsGrid = new GridPane();
            detailsGrid.setHgap(10);
            detailsGrid.setVgap(8);

            // Add invoice details
            detailsGrid.add(new Label("Invoice ID:"), 0, 0);
            detailsGrid.add(new Label(invoice.getInvoiceId()), 1, 0);

            detailsGrid.add(new Label("Sale ID:"), 0, 1);
            detailsGrid.add(new Label(invoice.getSaleId()), 1, 1);

            detailsGrid.add(new Label("Customer ID:"), 0, 2);
            detailsGrid.add(new Label(invoice.getCustomerId()), 1, 2);

            detailsGrid.add(new Label("Total Amount:"), 0, 3);
            detailsGrid.add(new Label(String.format("KSH %.2f", invoice.getTotalAmount())), 1, 3);

            detailsGrid.add(new Label("Paid Amount:"), 0, 4);
            detailsGrid.add(new Label(String.format("KSH %.2f", invoice.getPaidAmount())), 1, 4);

            detailsGrid.add(new Label("Balance:"), 0, 5);
            Label balanceLabel = new Label(String.format("KSH %.2f", invoice.getBalance()));
            balanceLabel.setStyle(invoice.getBalance() > 0 ? "-fx-text-fill: #e74c3c; -fx-font-weight: bold;" : "-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            detailsGrid.add(balanceLabel, 1, 5);

            detailsGrid.add(new Label("Status:"), 0, 6);
            Label statusLabel = new Label(invoice.getStatus());
            String statusStyle = "paid".equalsIgnoreCase(invoice.getStatus()) ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;" :
                               "pending".equalsIgnoreCase(invoice.getStatus()) ? "-fx-text-fill: #f39c12; -fx-font-weight: bold;" :
                               "-fx-text-fill: #e74c3c; -fx-font-weight: bold;";
            statusLabel.setStyle(statusStyle);
            detailsGrid.add(statusLabel, 1, 6);

            detailsGrid.add(new Label("Due Date:"), 0, 7);
            detailsGrid.add(new Label(dateFormat.format(invoice.getDueDate())), 1, 7);

            detailsGrid.add(new Label("Created At:"), 0, 8);
            detailsGrid.add(new Label(dateFormat.format(invoice.getCreatedAt())), 1, 8);

            Button closeBtn = new Button("Close");
            closeBtn.setOnAction(e -> dialog.close());

            layout.getChildren().addAll(titleLabel, detailsGrid, closeBtn);

            Scene scene = new Scene(layout, 400, 350);
            dialog.setScene(scene);
            dialog.showAndWait();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                     "Failed to display invoice details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showRecordPaymentDialog(Invoice invoice) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Record Payment - " + invoice.getInvoiceId());

            VBox layout = new VBox(10);
            layout.setStyle("-fx-padding: 16;");

            Label titleLabel = new Label("Record Payment");
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label balanceLabel = new Label("Current Balance: KSH " + String.format("%.2f", invoice.getBalance()));
            balanceLabel.setStyle("-fx-font-weight: bold;");

            TextField paymentAmountField = new TextField();
            paymentAmountField.setPromptText("Enter payment amount");

            Button recordBtn = new Button("Record Payment");
            recordBtn.setOnAction(e -> {
                try {
                    double paymentAmount = Double.parseDouble(paymentAmountField.getText());
                    if (paymentAmount <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Payment amount must be greater than 0.");
                        return;
                    }
                    if (paymentAmount > invoice.getBalance()) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Payment amount cannot exceed the balance.");
                        return;
                    }

                    double newPaidAmount = invoice.getPaidAmount() + paymentAmount;
                    double newBalance = invoice.getBalance() - paymentAmount;
                    String newStatus = newBalance <= 0 ? "paid" : "pending";

                    invoicesService.updatePayment(invoice.getInvoiceId(), newPaidAmount, newBalance, newStatus);

                    showAlert(Alert.AlertType.INFORMATION, "Payment Recorded", "Payment of KSH " + String.format("%.2f", paymentAmount) + " has been recorded.");
                    loadInvoicesData();
                    dialog.close();

                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid payment amount.");
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to record payment: " + ex.getMessage());
                }
            });

            Button cancelBtn = new Button("Cancel");
            cancelBtn.setOnAction(e -> dialog.close());

            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(recordBtn, cancelBtn);

            layout.getChildren().addAll(titleLabel, balanceLabel, paymentAmountField, buttonBox);

            Scene scene = new Scene(layout, 300, 200);
            dialog.setScene(scene);
            dialog.showAndWait();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                     "Failed to display payment dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
