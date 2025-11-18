package com.system.billingsystem.controller;

import com.system.billingsystem.dao.UserDao;
import com.system.billingsystem.service.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button goToSignupButton;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;

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
        successLabel.setVisible(false);
        successLabel.setManaged(false);

        // Handle Enter key properly
        emailField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);

        loginButton.setOnAction(e -> doLogin());
        goToSignupButton.setOnAction(e -> goToSignup());
    }

    // Proper Enter key handler
    private void handleEnterKey(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            doLogin();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #ff002fff;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void doLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        successLabel.setVisible(false);
        successLabel.setManaged(false);

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }

        try {
            if (userService.loginUser(email, password)) {
                successLabel.setText("Login successful!");
                successLabel.setVisible(true);
                successLabel.setManaged(true);
                System.out.println("Login OK");

                openDashboard();
            } else {
                showError("Invalid email or password.");
                System.out.println("Login FAILED: " + email);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Login failed: " + ex.getMessage());
        }
    }

    private void goToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/signup.fxml"));
            Parent root = loader.load();

            Stage stage = getStage();
            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.setTitle("Sign Up");
                stage.show();
            } else {
                showError("Stage not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load sign up page.");
        }
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/main.fxml"));
            Parent root = loader.load();

            Stage stage = getStage();
            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
                stage.show();
            } else {
                showError("Stage not found!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Could not open dashboard page.");
        }
    }

    private Stage getStage() {
        if (loginButton.getScene() != null) return (Stage) loginButton.getScene().getWindow();
        if (goToSignupButton.getScene() != null) return (Stage) goToSignupButton.getScene().getWindow();
        return null;
    }
}
