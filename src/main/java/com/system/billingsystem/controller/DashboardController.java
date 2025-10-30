package com.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;



public class DashboardController implements Initializable {

    @FXML private AnchorPane rootPane; 
    @FXML private Label todaysSalesValue;
    @FXML private Label itemsSoldValue;
    @FXML private Label lowStockValue;
    @FXML private Label activeCustomersValue;

    @FXML private PieChart salesPie;
    @FXML private LineChart<String, Number> salesLine;
    @FXML private ListView<String> recentTransactionsList;
    @FXML private ListView<String> lowStockList;

    private final DecimalFormat fmt = new DecimalFormat("#,###");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // demo numbers

        todaysSalesValue.setText("KSH " + fmt.format(125000));
        itemsSoldValue.setText("52");
        lowStockValue.setText("3");
        activeCustomersValue.setText("118");

        // pie chart 
        salesPie.getData().setAll(
            new PieChart.Data("Retail", 65),
            new PieChart.Data("Wholesale", 20),
            new PieChart.Data("Online", 15)
        );
        salesPie.setLegendVisible(true);
        salesPie.setLabelsVisible(true);

        // line chart 
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales (KSH)");
        series.getData().add(new XYChart.Data<>("Mon", 15000));
        series.getData().add(new XYChart.Data<>("Tue", 18000));
        series.getData().add(new XYChart.Data<>("Wed", 12000));
        series.getData().add(new XYChart.Data<>("Thu", 17000));
        series.getData().add(new XYChart.Data<>("Fri", 22000));
        series.getData().add(new XYChart.Data<>("Sat", 25000));
        series.getData().add(new XYChart.Data<>("Sun", 18000));

        salesLine.getData().clear();
        salesLine.getData().add(series);

        // simple lists
        List<String> recent = List.of(
            "Grace Njoki — KSH " + fmt.format(4_500) + " • M-Pesa • completed",
            "Peter Otieno — KSH " + fmt.format(320) + " • Cash • pending",
            "Alice Mwende — KSH " + fmt.format(12_500) + " • M-Pesa • completed"
        );
        recentTransactionsList.getItems().setAll(recent);

        List<String> low = List.of(
            "Blue Paint (20L) (Paints) — 2 / 10",
            "Oil Filter - Model X (Auto) — 3 / 5",
            "USB Cable 1m (Electronics) — 4 / 20"
        );
        lowStockList.getItems().setAll(low);

    }
}
