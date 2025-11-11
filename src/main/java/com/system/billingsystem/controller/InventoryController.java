package com.system.billingsystem.controller;

import com.system.billingsystem.models.Products;
import com.system.billingsystem.service.ProductsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InventoryController {

    @FXML private Label totalProductsValue;
    @FXML private Label lowStockValue;
    @FXML private Label totalValueLabel;
    @FXML private Label categoriesValue;

    @FXML private TextField searchField;
    @FXML private TableView<Products> productTable;
    @FXML private TableColumn<Products, String> colName;
    @FXML private TableColumn<Products, String> colCode;
    @FXML private TableColumn<Products, String> colCategory;
    @FXML private TableColumn<Products, Integer> colStock;
    @FXML private TableColumn<Products, String> colStatus;
    @FXML private TableColumn<Products, Double> colPrice;
    @FXML private TableColumn<Products, Void> colActions;

    @FXML private Button addProductButton;
    @FXML private Button searchButton; // ensure FXML has this id

    private final ObservableList<Products> inventory = FXCollections.observableArrayList();
    private final ProductsService productsService = new ProductsService();

    @FXML
    public void initialize() {
        loadProductsFromDB();

        productTable.setItems(inventory);

        // Setup columns
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getProductName()));
        colCode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getProductCode()));
        colCategory.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getQuantity() <= data.getValue().getReorderLevel() ? "LOW" : "OK"
        ));

        setupActionsColumn();
        updateSummary();

        addProductButton.setOnMouseClicked(this::handleAddProduct);

        if (searchButton != null) {
            searchButton.setOnAction(e -> performSearch());
        }

        // Enter key in search field
        if (searchField != null) {
            searchField.setOnAction(e -> performSearch());
        }
    }

    private void loadProductsFromDB() {
        try {
            List<Products> products = productsService.getAllProducts();
            inventory.setAll(products);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void setupActionsColumn() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Products, Void> call(TableColumn<Products, Void> param) {
                return new TableCell<>() {
                    private final Button viewBtn = new Button("View");
                    private final Button editBtn = new Button("Edit");
                    // private final Button delBtn = new Button("Delete");
                    private final HBox box = new HBox(8, viewBtn, editBtn /*, delBtn*/);

                    {
                        box.setPadding(new Insets(5));
                    }

                    {
                        viewBtn.setOnAction(evt -> showProductDialog(getCurrentProduct(), false));
                        editBtn.setOnAction(evt -> showProductDialog(getCurrentProduct(), true));
                        // delBtn.setOnAction(evt -> handleDeleteProduct(getCurrentProduct()));
                    }

                    private Products getCurrentProduct() {
                        return getTableView().getItems().get(getIndex());
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : box);
                    }
                };
            }
        });
    }

    private void handleDeleteProduct(Products product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete product \"" + product.getProductName() + "\"?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(null);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                productsService.deleteProduct(product.getProductCode());
                inventory.remove(product);
                updateSummary();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Delete Error", e.getMessage());
            }
        }
    }

    private void handleAddProduct(MouseEvent e) {
        Products newProduct = new Products();
        newProduct.setProductName("New Product");
        newProduct.setProductCode("NEW" + System.currentTimeMillis()); // ensure unique code
        newProduct.setCategory("Demo");
        newProduct.setPrice(0.0);
        newProduct.setQuantity(0);
        newProduct.setReorderLevel(5);

        try {
            Products saved = productsService.save(newProduct); // product_id will be generated in DAO if missing
            inventory.add(saved);
            updateSummary();
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Add Product Error", ex.getMessage());
        }
    }

    private void showProductDialog(Products p, boolean editable) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(editable ? "Edit Product" : "View Product");
        ButtonType saveType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        if (editable) dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);
        else dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        TextField nameField = new TextField(p.getProductName());
        TextField codeField = new TextField(p.getProductCode());
        TextField categoryField = new TextField(p.getCategory() == null ? "" : p.getCategory());
        TextField qtyField = new TextField(String.valueOf(p.getQuantity()));
        TextField reorderField = new TextField(String.valueOf(p.getReorderLevel()));
        TextField priceField = new TextField(String.valueOf(p.getPrice()));

        nameField.setEditable(editable);
        codeField.setEditable(editable);
        categoryField.setEditable(editable);
        qtyField.setEditable(editable);
        reorderField.setEditable(editable);
        priceField.setEditable(editable);

        grid.add(new Label("Name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Code:"), 0, 1); grid.add(codeField, 1, 1);
        grid.add(new Label("Category:"), 0, 2); grid.add(categoryField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3); grid.add(qtyField, 1, 3);
        grid.add(new Label("Reorder Level:"), 0, 4); grid.add(reorderField, 1, 4);
        grid.add(new Label("Price:"), 0, 5); grid.add(priceField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        Node saveButton = dialog.getDialogPane().lookupButton(saveType);
        if (saveButton != null) saveButton.setDisable(false);

        Optional<ButtonType> result = dialog.showAndWait();
        if (editable && result.isPresent() && result.get() == saveType) {
            try {
                p.setProductName(nameField.getText());
                p.setProductCode(codeField.getText());
                p.setCategory(categoryField.getText());
                p.setQuantity(Integer.parseInt(qtyField.getText().trim()));
                p.setReorderLevel(Integer.parseInt(reorderField.getText().trim()));
                p.setPrice(Double.parseDouble(priceField.getText().trim()));

                productsService.updateProduct(p, new String[]{
                        p.getProductName(),
                        p.getProductCode(),
                        String.valueOf(p.getPrice()),
                        String.valueOf(p.getQuantity()),
                        p.getCategory(),
                        String.valueOf(p.getReorderLevel())
                });

                // reload row from DB (optional) — here we just refresh table
                productTable.refresh();
                updateSummary();
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Update Error", ex.getMessage());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity, Reorder Level and Price must be numbers.");
            }
        }
    }

    private void updateSummary() {
        totalProductsValue.setText(String.valueOf(inventory.size()));
        long lowStockCount = inventory.stream().filter(p -> p.getQuantity() <= p.getReorderLevel()).count();
        lowStockValue.setText(String.valueOf(lowStockCount));
        double totalValue = inventory.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
        totalValueLabel.setText("KSH " + (int) totalValue);
        long categoriesCount = inventory.stream().map(Products::getCategory).distinct().count();
        categoriesValue.setText(String.valueOf(categoriesCount));
    }

    private void performSearch() {
        String searchText = searchField.getText();
        if (searchText == null || searchText.trim().isEmpty()) {
            productTable.setItems(inventory);
            return;
        }
        String s = searchText.toLowerCase().trim();
        ObservableList<Products> filtered = FXCollections.observableArrayList();
        for (Products p : inventory) {
            if ((p.getProductName() != null && p.getProductName().toLowerCase().contains(s)) ||
                (p.getProductCode() != null && p.getProductCode().toLowerCase().contains(s)) ||
                (p.getCategory() != null && p.getCategory().toLowerCase().contains(s))) {
                filtered.add(p);
            }
        }
        productTable.setItems(filtered);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type, message, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
