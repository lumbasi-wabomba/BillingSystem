package com.system.billingsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.scene.Parent;
import java.net.URL;
public class MainController {

    @FXML private BorderPane root;
    @FXML private AnchorPane topPlaceholder;
    @FXML private Node contentArea; 

    private TopNavController topNavController;

    @FXML
    public void initialize() {
        loadTopNav();        
        loadPage("/com/system/billingsystem/dashboard.fxml");
    }

    private void loadTopNav() {
        try {
            URL url = getClass().getResource("/com/system/billingsystem/topnav.fxml");
            if (url == null) {
                System.err.println("topnav.fxml not found!");
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Node topNav = loader.load();
            topPlaceholder.getChildren().setAll(topNav);
            topNavController = loader.getController();
            if (topNavController != null) topNavController.setMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadPage(String fxmlPath) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("FXML not found: " + fxmlPath + " — check resource path and that the file is in resources.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent page = loader.load();
            root.setCenter(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
