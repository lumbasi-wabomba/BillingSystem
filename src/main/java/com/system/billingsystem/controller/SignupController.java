package com.system.billingsystem.controller;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        EventHandler<KeyEvent> enterKeyHandler = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                doSignup();
            }
        };
        if (firstNameField != null) firstNameField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        if (lastNameField != null) lastNameField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        if (emailField != null) emailField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        if (passwordField != null) passwordField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        if (confirmField != null) confirmField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);

        if (signupButton != null) signupButton.addEventHandler(ActionEvent.ACTION, (EventHandler<ActionEvent>) e -> doSignup());
        if (goToLoginButton != null) goToLoginButton.addEventHandler(ActionEvent.ACTION, (EventHandler<ActionEvent>) e -> goToLogin());
    }


    private void doSignup() {
        // hide previous errors
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        String firstName = firstNameField.getText() == null ? "" : firstNameField.getText().trim();
        String lastName = lastNameField.getText() == null ? "" : lastNameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();
        String confirm = confirmField.getText() == null ? "" : confirmField.getText();

        // require both first and last name
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }

        // email val
        if (!email.contains("@") || !email.contains(".")) {
            showError("Enter a valid email address.");
            return;
        }

        String fullName = firstName + " " + lastName;
        System.out.println("Signup OK for: " + email + " (" + fullName + ")");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Could not open login page after signup.");
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
