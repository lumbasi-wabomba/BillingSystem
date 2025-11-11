package com.system.billingsystem.controller;

import com.system.billingsystem.models.Customers;
import com.system.billingsystem.service.CustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CustomersController {

    @FXML private TableView<Customers> customersTable;
    @FXML private TableColumn<Customers, String> colId;
    @FXML private TableColumn<Customers, String> colFirstName;
    @FXML private TableColumn<Customers, String> colLastName;
    @FXML private TableColumn<Customers, String> colPhone;
    @FXML private TableColumn<Customers, String> colEmail;
    @FXML private TableColumn<Customers, String> colCreatedAt;
    @FXML private TableColumn<Customers, String> colNotes;
    @FXML private TableColumn<Customers, Void> colActions;

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button addCustomerButton;

    @FXML private Label totalCustomersValue;
    @FXML private Label newThisMonthValue;
    @FXML private Label latestCustomerLabel;
    @FXML private Label latestCustomerDate;

    private CustomerService customerService;
    private ObservableList<Customers> customerList;

    @FXML
    public void initialize() {
        customerService = new CustomerService(new com.system.billingsystem.dao.CustomerDao());
        setupTableColumns();
        setupActionsColumn();
        setupSearch();
        loadData();

        addCustomerButton.setOnAction(e -> showAddCustomerForm());
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colCreatedAt.setCellValueFactory(data -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getDate() != null ? sdf.format(data.getValue().getDate()) : ""
            );
        });
    }

    private void setupActionsColumn() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Customers, Void> call(TableColumn<Customers, Void> param) {
                return new TableCell<>() {
                    private final Button editBtn = new Button("Edit");

                    {
                        editBtn.setOnAction(e -> {
                            Customers customer = getTableView().getItems().get(getIndex());
                            showEditCustomerForm(customer);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editBtn);
                        }
                    }
                };
            }
        });
    }

    private void setupSearch() {
        searchButton.setOnAction(e -> performSearch());
        searchField.setOnAction(e -> performSearch());
    }

    private void performSearch() {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            customersTable.setItems(customerList);
            return;
        }

        ObservableList<Customers> filtered = FXCollections.observableArrayList();
        for (Customers c : customerList) {
            boolean matchesQuery = c.getName().toLowerCase().contains(query)
                    || c.getEmail().toLowerCase().contains(query)
                    || c.getPhoneNumber().toLowerCase().contains(query)
                    || (c.getNotes() != null && c.getNotes().toLowerCase().contains(query));
            if (matchesQuery) filtered.add(c);
        }
        customersTable.setItems(filtered);
    }

    private void loadData() {
        try {
            List<Customers> list = customerService.getAllCustomers();
            customerList = FXCollections.observableArrayList(list);
            customersTable.setItems(customerList);
            updateStats();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load customers: " + e.getMessage());
        }
    }

    private void updateStats() {
        totalCustomersValue.setText(String.valueOf(customerList.size()));

        LocalDate now = LocalDate.now();
        long newThisMonth = customerList.stream()
                .filter(c -> c.getDate() != null)
                .filter(c -> {
                    LocalDate date = new java.sql.Date(c.getDate().getTime()).toLocalDate();
                    return date.getMonth() == now.getMonth() && date.getYear() == now.getYear();
                })
                .count();
        newThisMonthValue.setText(String.valueOf(newThisMonth));

        Customers last = getLastAddedCustomer();
        if (last != null) {
            latestCustomerLabel.setText(last.getName());
            latestCustomerDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(last.getDate()));
        } else {
            latestCustomerLabel.setText("—");
            latestCustomerDate.setText("—");
        }
    }

    public Customers getLastAddedCustomer() {
        if (customerList == null || customerList.isEmpty()) return null;
        return customerList.stream()
                .max(Comparator.comparing(Customers::getDate))
                .orElse(null);
    }

    private void showAddCustomerForm() {
        Customers newCustomer = new Customers();
        if (showCustomerForm(newCustomer, "Add New Customer")) {
            try {
                String lastId = customerService.getLastCustomerId(); // e.g., "C012"
                int nextIdNum = (lastId != null) ? Integer.parseInt(lastId.substring(1)) + 1 : 1;
                newCustomer.setCustomerId(String.format("C%03d", nextIdNum));
                newCustomer.setCreatedAt(new java.util.Date());
                customerService.saveCustomer(newCustomer);
                loadData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add customer: " + e.getMessage());
            }
        }
    }

    private void showEditCustomerForm(Customers customer) {
        if (showCustomerForm(customer, "Edit Customer")) {
            try {
                String[] details = new String[]{
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail(),
                        customer.getPhoneNumber(),
                        customer.getNotes()
                };
                customerService.updateCustomer(customer, details);
                loadData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update customer: " + e.getMessage());
            }
        }
    }

    private boolean showCustomerForm(Customers customer, String title) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);

        Label fnLabel = new Label("First Name:");
        TextField fnField = new TextField(customer.getFirstName());

        Label lnLabel = new Label("Last Name:");
        TextField lnField = new TextField(customer.getLastName());

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField(customer.getEmail());

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField(customer.getPhoneNumber());

        Label notesLabel = new Label("Notes:");
        TextField notesField = new TextField(customer.getNotes());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(fnLabel, 0, 0); grid.add(fnField, 1, 0);
        grid.add(lnLabel, 0, 1); grid.add(lnField, 1, 1);
        grid.add(emailLabel, 0, 2); grid.add(emailField, 1, 2);
        grid.add(phoneLabel, 0, 3); grid.add(phoneField, 1, 3);
        grid.add(notesLabel, 0, 4); grid.add(notesField, 1, 4);

        GridPane.setHgrow(fnField, Priority.ALWAYS);
        GridPane.setHgrow(lnField, Priority.ALWAYS);
        GridPane.setHgrow(emailField, Priority.ALWAYS);
        GridPane.setHgrow(phoneField, Priority.ALWAYS);
        GridPane.setHgrow(notesField, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> dialogButton);

        var result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            customer.setFirstName(fnField.getText().trim());
            customer.setLastName(lnField.getText().trim());
            customer.setEmail(emailField.getText().trim());
            customer.setPhoneNumber(phoneField.getText().trim());
            customer.setNotes(notesField.getText().trim());
            return true;
        }
        return false;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
