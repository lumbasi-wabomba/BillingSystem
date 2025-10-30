package com.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class InventoryController {

    @FXML private Label totalProductsValue;
    @FXML private Label lowStockValue;
    @FXML private Label totalValueLabel;
    @FXML private Label categoriesValue;

    @FXML private TextField searchField;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCode;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, String> colStatus;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Void> colActions; // new actions column

    @FXML private Button addProductButton;

    private ObservableList<Product> inventory = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Demo data
        inventory.addAll(
            new Product("Apple", "A001", "Fruit", 50, 10, 5),
            new Product("Banana", "B002", "Fruit", 30, 3, 5),
            new Product("Carrot", "C003", "Vegetable", 20, 15, 5)
        );

        productTable.setItems(inventory);

        // Setup columns
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCode.setCellValueFactory(data -> data.getValue().codeProperty());
        colCategory.setCellValueFactory(data -> data.getValue().categoryProperty());
        colStock.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        colPrice.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());

        // Actions column: view, edit, delete
        colActions.setCellFactory(new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button viewBtn = new Button("View");
                    private final Button editBtn = new Button("Edit");
                    private final Button delBtn = new Button("Delete");
                    private final HBox box = new HBox(8, viewBtn, editBtn, delBtn);

                    {
                        viewBtn.setOnAction(evt -> {
                            Product p = getTableView().getItems().get(getIndex());
                            showProductDialog(p, false);
                        });

                        editBtn.setOnAction(evt -> {
                            Product p = getTableView().getItems().get(getIndex());
                            showProductDialog(p, true);
                        });

                        delBtn.setOnAction(evt -> {
                            Product p = getTableView().getItems().get(getIndex());
                            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                            confirm.setTitle("Confirm Delete");
                            confirm.setHeaderText(null);
                            confirm.setContentText("Delete product \"" + p.nameProperty().get() + "\"?");
                            Optional<ButtonType> res = confirm.showAndWait();
                            if (res.isPresent() && res.get() == ButtonType.OK) {
                                inventory.remove(p);
                                updateSummary();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(box);
                        }
                    }
                };
            }
        });

        updateSummary();

        addProductButton.setOnMouseClicked(this::handleAddProduct);
    }

    private void updateSummary() {
        totalProductsValue.setText(String.valueOf(inventory.size()));
        long lowStockCount = inventory.stream().filter(p -> p.getQuantity() <= p.getReorderLevel()).count();
        lowStockValue.setText(String.valueOf(lowStockCount));
        double totalValue = inventory.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
        totalValueLabel.setText("KSH " + (int)totalValue);
        long categoriesCount = inventory.stream().map(Product::getCategory).distinct().count();
        categoriesValue.setText(String.valueOf(categoriesCount));
    }

    private void handleAddProduct(MouseEvent e) {
        inventory.add(new Product("Demo Product", "D004", "Demo", 100, 10, 5));
        updateSummary();
    }

    private void showProductDialog(Product p, boolean editable) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(editable ? "Edit Product" : "View Product");
        ButtonType saveType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);

        if (editable) dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);
        else dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Fields
        TextField nameField = new TextField(p.nameProperty().get());
        TextField codeField = new TextField(p.codeProperty().get());
        TextField categoryField = new TextField(p.categoryProperty().get());
        TextField qtyField = new TextField(String.valueOf(p.getQuantity()));
        TextField reorderField = new TextField(String.valueOf(p.getReorderLevel()));
        TextField priceField = new TextField(String.valueOf(p.getPrice()));

        // Timestamps - read-only labels
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Label createdLabel = new Label(p.getCreatedAt().format(fmt));
        Label updatedLabel = new Label(p.getUpdatedAt().format(fmt));

        // Disable editing if not editable
        nameField.setEditable(editable);
        codeField.setEditable(editable);
        categoryField.setEditable(editable);
        qtyField.setEditable(editable);
        reorderField.setEditable(editable);
        priceField.setEditable(editable);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Code:"), 0, 1);
        grid.add(codeField, 1, 1);

        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);

        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(qtyField, 1, 3);

        grid.add(new Label("Reorder Level:"), 0, 4);
        grid.add(reorderField, 1, 4);

        grid.add(new Label("Price:"), 0, 5);
        grid.add(priceField, 1, 5);

        grid.add(new Label("Created At:"), 0, 6);
        grid.add(createdLabel, 1, 6);

        grid.add(new Label("Updated At:"), 0, 7);
        grid.add(updatedLabel, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Validation / Save handler
        Node saveButton = dialog.getDialogPane().lookupButton(saveType);
        if (saveButton != null) {
            saveButton.setDisable(false); // could add validation
        }

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == saveType) {
            // apply updates
            try {
                p.nameProperty().set(nameField.getText());
                p.codeProperty().set(codeField.getText());
                p.categoryProperty().set(categoryField.getText());

                int newQty = Integer.parseInt(qtyField.getText().trim());
                int newReorder = Integer.parseInt(reorderField.getText().trim());
                double newPrice = Double.parseDouble(priceField.getText().trim());

                p.quantityProperty().set(newQty);
                p.reorderLevelProperty().set(newReorder);
                p.priceProperty().set(newPrice);

                p.updatedAtProperty().set(LocalDateTime.now());

                // Refresh UI
                productTable.refresh();
                updateSummary();
            } catch (NumberFormatException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Please enter valid numeric values for quantity, reorder level, and price.", ButtonType.OK);
                a.setHeaderText(null);
                a.showAndWait();
            }
        }
    }

    // Product class (inner)
    public static class Product {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty code;
        private final javafx.beans.property.SimpleStringProperty category;
        private final javafx.beans.property.SimpleIntegerProperty quantity;
        private final javafx.beans.property.SimpleIntegerProperty reorderLevel;
        private final javafx.beans.property.SimpleDoubleProperty price;
        private final javafx.beans.property.SimpleStringProperty status;

        private final javafx.beans.property.ObjectProperty<LocalDateTime> createdAt;
        private final javafx.beans.property.ObjectProperty<LocalDateTime> updatedAt;

        public Product(String name, String code, String category, int quantity, int reorderLevel, double price) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.code = new javafx.beans.property.SimpleStringProperty(code);
            this.category = new javafx.beans.property.SimpleStringProperty(category);
            this.quantity = new javafx.beans.property.SimpleIntegerProperty(quantity);
            this.reorderLevel = new javafx.beans.property.SimpleIntegerProperty(reorderLevel);
            this.price = new javafx.beans.property.SimpleDoubleProperty(price);
            this.status = new javafx.beans.property.SimpleStringProperty(computeStatus(quantity, reorderLevel));

            LocalDateTime now = LocalDateTime.now();
            this.createdAt = new javafx.beans.property.SimpleObjectProperty<>(now);
            this.updatedAt = new javafx.beans.property.SimpleObjectProperty<>(now);

            // listen to quantity changes to update status and updatedAt
            this.quantity.addListener((obs, oldV, newV) -> {
                status.set(computeStatus(newV.intValue(), this.reorderLevel.get()));
                this.updatedAt.set(LocalDateTime.now());
            });

            // listen to reorder changes to update status
            this.reorderLevel.addListener((obs, oldV, newV) -> {
                status.set(computeStatus(this.quantity.get(), newV.intValue()));
                this.updatedAt.set(LocalDateTime.now());
            });

            // listen to price/name/category changes to bump updatedAt
            this.price.addListener((obs, o, n) -> this.updatedAt.set(LocalDateTime.now()));
            this.name.addListener((obs, o, n) -> this.updatedAt.set(LocalDateTime.now()));
            this.code.addListener((obs, o, n) -> this.updatedAt.set(LocalDateTime.now()));
            this.category.addListener((obs, o, n) -> this.updatedAt.set(LocalDateTime.now()));
        }

        private String computeStatus(int qty, int reorder) { return qty <= reorder ? "LOW" : "OK"; }

        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty codeProperty() { return code; }
        public javafx.beans.property.StringProperty categoryProperty() { return category; }
        public javafx.beans.property.IntegerProperty quantityProperty() { return quantity; }
        public javafx.beans.property.IntegerProperty reorderLevelProperty() { return reorderLevel; }
        public javafx.beans.property.DoubleProperty priceProperty() { return price; }
        public javafx.beans.property.StringProperty statusProperty() { return status; }

        public javafx.beans.property.ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
        public javafx.beans.property.ObjectProperty<LocalDateTime> updatedAtProperty() { return updatedAt; }

        public int getQuantity() { return quantity.get(); }
        public int getReorderLevel() { return reorderLevel.get(); }
        public double getPrice() { return price.get(); }
        public String getCategory() { return category.get(); }

        public LocalDateTime getCreatedAt() { return createdAt.get(); }
        public LocalDateTime getUpdatedAt() { return updatedAt.get(); }
    }
}
