package com.system.billingsystem.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton; 
    @FXML private Button goToSignupButton; 
    @FXML private Label errorLabel;
    @FXML private Label successLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        successLabel.setVisible(false);
        successLabel.setManaged(false);

        EventHandler<KeyEvent> enterKeyHandler = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                doLogin();
            }
        };
        emailField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyHandler);

        loginButton.addEventHandler(ActionEvent.ACTION, (EventHandler<ActionEvent>) e -> doLogin());

        goToSignupButton.addEventHandler(ActionEvent.ACTION, (EventHandler<ActionEvent>) e -> {
            javafx.scene.Node source = (javafx.scene.Node) e.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            handleGoToSignup();
        });
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

        // hide previous messages
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }

        //auth propaganda

        if (email.equals("u@e.c") && password.equals("one")) {
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);

            successLabel.setText("Login successful!");
            successLabel.setVisible(true);
            successLabel.setManaged(true);

            System.out.println("Login OK");

            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Could not open dashboard page after signin.");
        }
        } else {
            showError("Invalid email or password.");
            System.out.println("Login terribly FAILED, be serious with your life," + email + " !!!!" );

        }
    }


    private void handleGoToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) goToSignupButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load sign up page.");}
    }

}
