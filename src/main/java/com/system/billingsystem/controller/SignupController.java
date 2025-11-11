package com.system.billingsystem.controller;

import com.system.billingsystem.dao.UserDao;
import com.system.billingsystem.models.User;
import com.system.billingsystem.service.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label errorLabel;
    @FXML private Button signupButton;
    @FXML private Button goToLoginButton;

    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            userService = new UserService(new UserDao());
        } catch (Exception e) {
            showError("Failed to initialize user service: " + e.getMessage());
        }

        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        EventHandler<KeyEvent> enterKeyHandler = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                doSignup();
            }
        };

        firstNameField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        lastNameField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        emailField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        confirmField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);

        signupButton.setOnAction(e -> doSignup());
        goToLoginButton.setOnAction(e -> goToLogin());
    }

    private void doSignup() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Enter a valid email address.");
            return;
        }

        // Generate user ID
        User tempUser = new User();
        String userId = tempUser.generateUserID();

        // Create User object
        User newUser = new User(
            userId,
            email, // username as email
            firstName,
            lastName,
            email,
            "user", // default role
            new java.sql.Date(new Date().getTime()),
            password
        );

        try {
            userService.saveUser(newUser);
            System.out.println("Signup OK for: " + email + " (" + firstName + " " + lastName + ")");

            // Load login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            showError("Failed to signup user: " + ex.getMessage());
        }
    }

    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) goToLoginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Could not open login page.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #b00020;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
