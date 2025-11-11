package com.system.billingsystem.controller;

import com.system.billingsystem.dao.SalesDao;
import com.system.billingsystem.dao.SalesItemsDao;
import com.system.billingsystem.models.Sales;
import com.system.billingsystem.models.SalesItems;
import com.system.billingsystem.service.SalesItemsService;
import com.system.billingsystem.service.SalesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the Sales view.
 * Displays all sales transactions and allows viewing sale details.
 */
public class SalesController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button clearSearchButton;
    @FXML private Button refreshButton;
    
    @FXML private TableView<Sales> salesTable;
    @FXML private TableColumn<Sales, String> colId;
    @FXML private TableColumn<Sales, String> colCustomerId;
    @FXML private TableColumn<Sales, String> colCreatedBy;
    @FXML private TableColumn<Sales, Double> colTotalAmount;
    @FXML private TableColumn<Sales, String> colPaymentMethod;
    @FXML private TableColumn<Sales, String> colStatus;
    @FXML private TableColumn<Sales, String> colCreatedAt;
    @FXML private TableColumn<Sales, Void> colActions;

    // Services
    private SalesService salesService;
    private SalesItemsService salesItemsService;

    // Data
    private ObservableList<Sales> allSales = FXCollections.observableArrayList();
    private ObservableList<SalesItems> allSaleItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initializeServices();
        setupTableColumns();
        setupActionColumn();
        setupSearchFunctionality();
        loadSalesData();
    }

    private void initializeServices() {
        try {
            salesService = new SalesService(new SalesDao());
            salesItemsService = new SalesItemsService(new SalesItemsDao());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error", 
                     "Failed to initialize services: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        colId.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getSaleId()));
        
        colCustomerId.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCustomerId()));
        
        colCreatedBy.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getSalesPersonId()));
        
        colTotalAmount.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalAmount()));
        
        // Format total amount as currency
        colTotalAmount.setCellFactory(col -> new TableCell<Sales, Double>() {
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
        
        colPaymentMethod.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getPaymentMethod()));
        
        colStatus.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        
        // Format status with color
        colStatus.setCellFactory(col -> new TableCell<Sales, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("completed".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else if ("pending".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        colCreatedAt.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                dateFormat.format(data.getValue().getSaleDate())
            ));
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<Sales, Void>() {
            private final Button viewItemsBtn = new Button("View Items");
            private final Button viewReceiptBtn = new Button("Receipt");
            private final Button viewInvoiceBtn = new Button("Invoice");

            {
                viewItemsBtn.setOnAction(e -> {
                    Sales sale = getTableView().getItems().get(getIndex());
                    showSaleItemsDialog(sale);
                });

                viewReceiptBtn.setOnAction(e -> {
                    Sales sale = getTableView().getItems().get(getIndex());
                    showReceiptDialog(sale);
                });

                viewInvoiceBtn.setOnAction(e -> {
                    Sales sale = getTableView().getItems().get(getIndex());
                    showInvoiceDialog(sale);
                });

                viewItemsBtn.setStyle("-fx-font-size: 10px; -fx-padding: 4 8;");
                viewReceiptBtn.setStyle("-fx-font-size: 10px; -fx-padding: 4 8;");
                viewInvoiceBtn.setStyle("-fx-font-size: 10px; -fx-padding: 4 8;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(4);
                    hbox.getChildren().addAll(viewItemsBtn, viewReceiptBtn, viewInvoiceBtn);
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
                loadSalesData();
                showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Sales data has been refreshed.");
            });
        }
        
        // Real-time search as user types
        searchField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                salesTable.setItems(allSales);
            }
        });
    }
    
    private void clearSearch() {
        searchField.clear();
        salesTable.setItems(allSales);
    }

    private void performSearch() {
        String searchText = searchField.getText();
        if (searchText == null || searchText.trim().isEmpty()) {
            salesTable.setItems(allSales);
            return;
        }
        
        String search = searchText.toLowerCase();
        List<Sales> filtered = allSales.stream()
            .filter(sale -> 
                sale.getSaleId().toLowerCase().contains(search) ||
                sale.getCustomerId().toLowerCase().contains(search) ||
                sale.getSalesPersonId().toLowerCase().contains(search) ||
                sale.getPaymentMethod().toLowerCase().contains(search) ||
                sale.getStatus().toLowerCase().contains(search)
            )
            .collect(Collectors.toList());
        
        salesTable.setItems(FXCollections.observableArrayList(filtered));
    }

    private void loadSalesData() {
        try {
            // Load all sales
            List<Sales> sales = salesService.getAllSales();
            allSales.setAll(sales);
            salesTable.setItems(allSales);

            // Note: Load sale items on demand to improve performance
            // allSaleItems will be loaded when needed for specific sales

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                     "Failed to load sales data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSaleItemsDialog(Sales sale) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Sale Items - " + sale.getSaleId());

            // Create table for sale items
            TableView<SalesItems> itemsTable = new TableView<>();

            // Get items for this sale on demand
            List<SalesItems> saleItems = salesItemsService.getItemsBySaleId(sale.getSaleId());

            itemsTable.setItems(FXCollections.observableArrayList(saleItems));

            // Setup columns
            TableColumn<SalesItems, String> colName = new TableColumn<>("Product Name");
            colName.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().getProductName()));
            colName.setPrefWidth(200);

            TableColumn<SalesItems, String> colCode = new TableColumn<>("Product Code");
            colCode.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().getProductCode()));
            colCode.setPrefWidth(120);

            TableColumn<SalesItems, Double> colUnitPrice = new TableColumn<>("Unit Price");
            colUnitPrice.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
            colUnitPrice.setPrefWidth(100);
            colUnitPrice.setCellFactory(col -> new TableCell<SalesItems, Double>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty || price == null ? null : String.format("KSH %.2f", price));
                }
            });

            TableColumn<SalesItems, Integer> colQty = new TableColumn<>("Quantity");
            colQty.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getQuantity()));
            colQty.setPrefWidth(80);

            TableColumn<SalesItems, Double> colTotal = new TableColumn<>("Line Total");
            colTotal.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotal()));
            colTotal.setPrefWidth(120);
            colTotal.setCellFactory(col -> new TableCell<SalesItems, Double>() {
                @Override
                protected void updateItem(Double total, boolean empty) {
                    super.updateItem(total, empty);
                    setText(empty || total == null ? null : String.format("KSH %.2f", total));
                }
            });

            itemsTable.getColumns().addAll(colName, colCode, colUnitPrice, colQty, colTotal);

            // Create layout
            VBox layout = new VBox(10);
            layout.setStyle("-fx-padding: 16;");
            
            Label titleLabel = new Label("Items for Sale: " + sale.getSaleId());
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            
            Label totalLabel = new Label(
                String.format("Total Amount: KSH %.2f", sale.getTotalAmount())
            );
            totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
            
            Button closeBtn = new Button("Close");
            closeBtn.setOnAction(e -> dialog.close());
            
            layout.getChildren().addAll(titleLabel, itemsTable, totalLabel, closeBtn);
            
            Scene scene = new Scene(layout, 700, 450);
            dialog.setScene(scene);
            dialog.showAndWait();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                     "Failed to display sale items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showReceiptDialog(Sales sale) {
        try {
            // Get sale items on demand
            List<SalesItems> saleItems = salesItemsService.getItemsBySaleId(sale.getSaleId());

            // Load receipt view
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/system/billingsystem/receipt.fxml")
            );
            VBox receiptRoot = loader.load();

            ReceiptController controller = loader.getController();
            controller.setSaleData(sale, saleItems, 0.0, "", sale.getCustomerId());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Receipt - " + sale.getSaleId());
            stage.setScene(new Scene(receiptRoot));
            stage.showAndWait();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                     "Failed to display receipt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showInvoiceDialog(Sales sale) {
        try {
            // Load invoice view
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/system/billingsystem/invoice.fxml")
            );
            VBox invoiceRoot = loader.load();

            InvoiceController controller = loader.getController();
            // Note: InvoiceController will load its own data, but we could pass sale info if needed

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Invoices - " + sale.getSaleId());
            stage.setScene(new Scene(invoiceRoot));
            stage.showAndWait();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                     "Failed to display invoice: " + e.getMessage());
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
