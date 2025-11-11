package com.system.billingsystem.controller;

import com.system.billingsystem.dao.CustomerDao;
import com.system.billingsystem.dao.InvoicesDao;
import com.system.billingsystem.dao.ProductsDao;
import com.system.billingsystem.dao.ReceiptsDao;
import com.system.billingsystem.dao.SalesDao;
import com.system.billingsystem.dao.SalesItemsDao;
import com.system.billingsystem.models.*;
import com.system.billingsystem.service.CustomerService;
import com.system.billingsystem.service.InvoicesService;
import com.system.billingsystem.service.ProductsService;
import com.system.billingsystem.service.ReceiptsService;
import com.system.billingsystem.service.SalesItemsService;
import com.system.billingsystem.service.SalesService;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PosController {

    @FXML private TextField prodSearch;
    @FXML private ChoiceBox<String> categoryFilter;
    @FXML private ListView<Products> productList;

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> colProd;
    @FXML private TableColumn<CartItem, Integer> colQty;
    @FXML private TableColumn<CartItem, Double> colUnit;
    @FXML private TableColumn<CartItem, Double> colLine;
    @FXML private TableColumn<CartItem, Void> colRemove;
    @FXML private TextField notesField;

    @FXML private ComboBox<Customers> customerCombo;
    @FXML private Label subtotalLabel;
    @FXML private TextField discountField;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;
    @FXML private TextField paymentAmountField;
    @FXML private ChoiceBox<String> paymentMethod;
    @FXML private Button finalizeBtn;
    @FXML private Button clearBtn;
    @FXML private Button newSaleBtn;
    @FXML private ListView<String> recentSalesList;

    private ProductsService productsService;
    private CustomerService customerService;
    private SalesService salesService;
    private SalesItemsService salesItemsService;

    private ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
    private ObservableList<Products> allProducts = FXCollections.observableArrayList();
    private ObservableList<CartItem> cart = FXCollections.observableArrayList();
    private ObservableList<String> recentSales = FXCollections.observableArrayList();

    private final double TAX_RATE = 0.16;
    private String currentUserId = "u_demo";

    @FXML
    public void initialize() {
        initializeServices();
        loadDataFromDatabase();
        setupPaymentMethodDropdown();
        setupProductList();
        setupCartTable();
        setupCustomerCombo();
        setupActions();
        setupDiscountField();
        setupPaymentAmountField();
        refreshTotals();
    }

    private void initializeServices() {
        try {
            productsService = new ProductsService();
            customerService = new CustomerService(new CustomerDao());
            salesService = new SalesService(new SalesDao());
            salesItemsService = new SalesItemsService(new SalesItemsDao());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error",
                     "Failed to initialize services: " + e.getMessage());
        }
    }

    private void loadDataFromDatabase() {
        loadProducts();
        loadCustomers();
        loadRecentSales();
    }

    private void loadProducts() {
        try {
            List<Products> products = productsService.getAllProducts();
            allProducts.setAll(products);

            Set<String> categories = new LinkedHashSet<>();
            categories.add("All");
            products.forEach(p -> {
                if (p.getCategory() != null) categories.add(p.getCategory());
            });

            categoryFilter.setItems(FXCollections.observableArrayList(categories));
            categoryFilter.getSelectionModel().selectFirst();
            productList.setItems(allProducts);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "DB Error", e.getMessage());
        }
    }

    private void loadCustomers() {
        try {
            allCustomers.setAll(customerService.getAllCustomers());
            customerCombo.setItems(allCustomers);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "DB Error", e.getMessage());
        }
    }

    private void loadRecentSales() {
        try {
            List<Sales> sales = salesService.getAllSales();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            recentSales.clear();
            sales.stream()
                 .sorted((a, b) -> b.getSaleDate().compareTo(a.getSaleDate()))
                 .limit(20)
                 .forEach(s -> recentSales.add(s.getSaleId() + " • KSH " +
                             String.format("%.2f", s.getTotalAmount()) + " • " +
                             df.format(s.getSaleDate())));

            recentSalesList.setItems(recentSales);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "DB Error", e.getMessage());
        }
    }

    private void setupPaymentMethodDropdown() {
        paymentMethod.setItems(FXCollections.observableArrayList("Cash", "Mpesa", "Card", "Credit"));
        paymentMethod.getSelectionModel().select("Cash");
    }

    private void setupProductList() {
        productList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Products p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setGraphic(null);
                    return;
                }

                Label title = new Label(p.getProductName());
                title.setStyle("-fx-font-weight: bold;");

                Label details = new Label(
                    (p.getProductCode() == null ? "" : p.getProductCode()) +
                    " • " + p.getCategory() +
                    " • KSH " + String.format("%.2f", p.getPrice()) +
                    " • Stock: " + p.getQuantity()
                );
                details.setStyle("-fx-text-fill:#666; -fx-font-size:11px;");

                Button add = new Button("Add");
                add.setOnAction(e -> addToCart(p));

                if (p.getQuantity() <= 0) {
                    add.setDisable(true);
                    title.setStyle("-fx-font-weight:bold; -fx-text-fill:red;");
                }

                HBox h = new HBox(10, new VBox(title, details), new Pane(), add);
                HBox.setHgrow(h.getChildren().get(1), Priority.ALWAYS);

                setGraphic(h);
            }
        });

        prodSearch.textProperty().addListener((o, a, b) -> filterProducts());
        categoryFilter.valueProperty().addListener((o, a, b) -> filterProducts());
    }

    private void filterProducts() {
        String search = prodSearch.getText().toLowerCase();
        String cat = categoryFilter.getValue();

        List<Products> filtered = allProducts.stream()
            .filter(p -> "All".equals(cat) || p.getCategory().equals(cat))
            .filter(p -> p.getProductName().toLowerCase().contains(search)
                      || p.getProductCode().toLowerCase().contains(search))
            .collect(Collectors.toList());

        productList.setItems(FXCollections.observableArrayList(filtered));
    }

    private void setupCartTable() {
        colProd.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getProduct().getProductName()));
        colQty.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getQuantity()));
        colUnit.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getProduct().getPrice()));
        colLine.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getLineTotal()));

        cartTable.setEditable(true);
        colQty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        colQty.setOnEditCommit(event -> {
            CartItem item = event.getRowValue();
            int newQty = Math.max(1, event.getNewValue());

            if (newQty > item.getProduct().getQuantity()) {
                showAlert(Alert.AlertType.WARNING, "Stock Warning",
                          "Only " + item.getProduct().getQuantity() + " available.");
                cartTable.refresh();
                return;
            }

            item.setQuantity(newQty);
            cartTable.refresh();
            refreshTotals();
        });

        colRemove.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Remove");

            {
                btn.setOnAction(e -> {
                    cart.remove(getTableView().getItems().get(getIndex()));
                    refreshTotals();
                });
            }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });

        cartTable.setItems(cart);
    }

    private void setupCustomerCombo() {
        customerCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Customers c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getName() + " (" + c.getPhoneNumber() + ")");
            }
        });

        customerCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customers c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? "Select customer (optional)" : c.getName());
            }
        });
    }

    private void setupActions() {
        finalizeBtn.setOnAction(e -> finalizeSale());
        clearBtn.setOnAction(e -> clearCart());
        newSaleBtn.setOnAction(e -> clearCart());
    }

    private void setupDiscountField() {
        discountField.textProperty().addListener((o, a, b) -> refreshTotals());
    }

    private void setupPaymentAmountField() {
        paymentAmountField.textProperty().addListener((o, a, b) -> refreshTotals());
    }

    private void addToCart(Products p) {
        Optional<CartItem> existing = cart.stream()
                .filter(ci -> ci.getProduct().getProductId().equals(p.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem i = existing.get();
            if (i.getQuantity() + 1 > p.getQuantity()) {
                showAlert(Alert.AlertType.WARNING, "Stock Warning",
                          "Only " + p.getQuantity() + " available.");
                return;
            }
            i.setQuantity(i.getQuantity() + 1);
        } else {
            if (p.getQuantity() <= 0) {
                showAlert(Alert.AlertType.WARNING, "Out of Stock", "This product is out of stock.");
                return;
            }
            cart.add(new CartItem(p, 1));
        }
        cartTable.refresh();
        refreshTotals();
    }

    private void refreshTotals() {
        double subtotal = cart.stream().mapToDouble(CartItem::getLineTotal).sum();

        double discount = 0;
        try {
            if (!discountField.getText().trim().isEmpty())
                discount = Double.parseDouble(discountField.getText());
        } catch (Exception ignored) {}

        double taxable = Math.max(0, subtotal - discount);
        double tax = taxable * TAX_RATE;
        double total = taxable + tax;

        subtotalLabel.setText("KSH " + String.format("%.2f", subtotal));
        taxLabel.setText("KSH " + String.format("%.2f", tax));
        totalLabel.setText("KSH " + String.format("%.2f", total));
    }

    private void clearCart() {
        cart.clear();
        if (notesField != null) notesField.clear();
        discountField.setText("0");
        paymentAmountField.clear();
        customerCombo.getSelectionModel().clearSelection();
        refreshTotals();
    }
        private void finalizeSale() {

        if (cart.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Cart", "Add items before finalizing.");
            return;
        }

        String method = paymentMethod.getValue();
        double subtotal = cart.stream().mapToDouble(CartItem::getLineTotal).sum();

        double discount = 0;
        try {
            if (!discountField.getText().trim().isEmpty())
                discount = Double.parseDouble(discountField.getText().trim());
        } catch (Exception ignored) {}

        double taxable = Math.max(0, subtotal - discount);
        double tax = taxable * TAX_RATE;
        double total = taxable + tax;

        double paid = 0;
        try {
            if (!paymentAmountField.getText().trim().isEmpty())
                paid = Double.parseDouble(paymentAmountField.getText().trim());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Payment", "Enter a valid number.");
            return;
        }

        if (paid < 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Payment", "Payment cannot be negative.");
            return;
        }

        // CONFIRM END OF SALE
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Finalize this sale?\nPayment Method: " + method +
                        "\nAmount Paid: " + paid +
                        "\nTotal: " + total,
                ButtonType.YES, ButtonType.NO);

        confirm.setTitle("Confirm Sale");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.YES) return;


        //  CUSTOMER LOGIC BASED ON PAYMENT AMOUNT
        Customers customer = null;
        String customerId;

        if (paid >= total) {
            // Fully paid: Use default WALKIN, no prompt
            customerId = "WALKIN";
        } else if (paid > 0) {
            // Partially paid: Prompt for customer
            customer = promptSelectOrAddCustomer();
            if (customer == null) {
                showAlert(Alert.AlertType.WARNING,
                        "Customer Required",
                        "Partial payments require a customer.");
                return;
            }
            customerId = customer.getCustomerId();
        } else {
            // No payment: Prompt for customer
            customer = promptSelectOrAddCustomer();
            if (customer == null) {
                showAlert(Alert.AlertType.WARNING,
                        "Customer Required",
                        "No payment requires a customer for invoicing.");
                return;
            }
            customerId = customer.getCustomerId();
        }


        // CREATE SALE RECORD
        String saleId = "S" + System.currentTimeMillis();
        String status = paid >= total ? "completed" : "pending";

        Sales sale = new Sales(
                saleId,
                customerId,
                new Date(),
                currentUserId,
                total,
                status,
                method
        );

        try {
            salesService.saveSale(sale);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "DB Error", "Failed to save sale.");
            return;
        }


        //  SAVE ITEMS + UPDATE STOCK
        List<SalesItems> items = new ArrayList<>();

        for (CartItem ci : cart) {
            Products p = ci.getProduct();

            SalesItems si = new SalesItems(
                    "SI" + System.currentTimeMillis() + "_" + new Random().nextInt(999),
                    p.getProductId(),
                    saleId,
                    p.getProductName(),
                    p.getProductCode(),
                    ci.getQuantity(),
                    p.getPrice(),
                    ci.getLineTotal(),
                    new Date()
            );

            try {
                salesItemsService.saveSoldItem(si);
                productsService.updateProductStock(p.getProductId(), p.getQuantity() - ci.getQuantity());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            items.add(si);
        }


        //  NOTES ALWAYS INCLUDED
        String notes = (notesField == null || notesField.getText().trim().isEmpty())
                ? "POS Sale " + saleId
                : notesField.getText().trim();


        // RECEIPT AND INVOICE LOGIC
        if (paid >= total) {
            // Fully paid: Receipt only
            try {
                ReceiptsService rs = new ReceiptsService(new ReceiptsDao());
                rs.createReceiptForPayment(saleId, customerId, paid, method, notes);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (paid > 0) {
            // Partially paid: Receipt for paid + Invoice for balance
            try {
                ReceiptsService rs = new ReceiptsService(new ReceiptsDao());
                rs.createReceiptForPayment(saleId, customerId, paid, method, notes);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            double balance = total - paid;
            try {
                InvoicesService inv = new InvoicesService(new InvoicesDao());
                Invoice invoice = new Invoice(
                        "INV" + System.currentTimeMillis(),
                        saleId,
                        customerId,
                        total,
                        paid,
                        balance,
                        new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000),
                        "pending",
                        new Date()
                );
                inv.saveInvoice(invoice);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // No payment: Invoice only
            try {
                InvoicesService inv = new InvoicesService(new InvoicesDao());
                Invoice invoice = new Invoice(
                        "INV" + System.currentTimeMillis(),
                        saleId,
                        customerId,
                        total,
                        0,
                        total,
                        new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000),
                        "pending",
                        new Date()
                );
                inv.saveInvoice(invoice);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        //  SHOW RECEIPT WINDOW ONLY IF RECEIPT GENERATED
        if (paid >= total || paid > 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/receipt.fxml"));
                VBox root = loader.load();
                ReceiptController c = loader.getController();

                double balance = total - paid;
                c.setSaleDataWithPayment(
                        sale,
                        items,
                        discount,
                        notes,
                        customerId.equals("WALKIN") ? "Walk-in Customer" : (customer != null ? customer.getName() : customerId),
                        paid,
                        balance
                );

                Stage st = new Stage();
                st.initModality(Modality.APPLICATION_MODAL);
                st.setTitle("Receipt - " + saleId);
                st.setScene(new Scene(root));
                st.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // FINAL MESSAGES
        if ("Credit".equalsIgnoreCase(method)) {
            showAlert(Alert.AlertType.INFORMATION, "Credit Sale Recorded",
                    "Sale saved under credit.\nSale ID: " + saleId);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Sale Completed",
                    "Sale completed successfully.\nSale ID: " + saleId);
        }

        // Refresh UI
        loadRecentSales();
        loadProducts();
        clearCart();
    }

    private Customers promptSelectOrAddCustomer() {
        Dialog<Customers> dialog = new Dialog<>();
        dialog.setTitle("Select Customer for Credit Sale");
        dialog.setHeaderText("Select or Add Customer");

        ButtonType ok = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        ComboBox<Customers> cb = new ComboBox<>(allCustomers);
        cb.setPromptText("Choose customer");

        Button add = new Button("Add Customer");
        add.setOnAction(e -> {
            Customers c = openAddCustomerDialog();
            if (c != null) {
                allCustomers.add(c);
                cb.setValue(c);
            }
        });

        box.getChildren().addAll(new Label("Select Customer:"), cb, add);
        dialog.getDialogPane().setContent(box);

        Button b = (Button) dialog.getDialogPane().lookupButton(ok);
        b.setDisable(true);

        cb.valueProperty().addListener((o, oldV, newV) -> b.setDisable(newV == null));

        dialog.setResultConverter(btn -> btn == ok ? cb.getValue() : null);

        Optional<Customers> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private Customers openAddCustomerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/customers.fxml"));
            VBox root = loader.load();
            CustomersController c = loader.getController();

            Stage st = new Stage();
            st.setScene(new Scene(root));
            st.initModality(Modality.APPLICATION_MODAL);
            st.setTitle("Add Customer");
            st.showAndWait();

            loadCustomers();  // refresh

            return c.getLastAddedCustomer();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }
}



    