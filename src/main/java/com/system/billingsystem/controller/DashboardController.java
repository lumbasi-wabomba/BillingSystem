package com.system.billingsystem.controller;

import com.system.billingsystem.dao.CustomerDao;
import com.system.billingsystem.dao.ProductsDao;
import com.system.billingsystem.dao.SalesDao;
import com.system.billingsystem.dao.SalesItemsDao;
import com.system.billingsystem.models.Customers;
import com.system.billingsystem.models.Products;
import com.system.billingsystem.models.Sales;
import com.system.billingsystem.models.SalesItems;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
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

    private SalesDao salesDao;
    private SalesItemsDao salesItemsDao;
    private ProductsDao productsDao;
    private CustomerDao customerDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            salesDao = new SalesDao();
            salesItemsDao = new SalesItemsDao();
            productsDao = new ProductsDao();
            customerDao = new CustomerDao();

            loadDashboardData();
        } catch (Exception e) {
            e.printStackTrace();
            loadDemoData();
        }
    }

    private void loadDashboardData() throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayStart = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date todayEnd = cal.getTime();

        List<Sales> allSales = salesDao.getAll();
        double todaysSales = allSales.stream()
            .filter(s -> s.getSaleDate().after(todayStart) && s.getSaleDate().before(todayEnd))
            .mapToDouble(Sales::getTotalAmount)
            .sum();
        todaysSalesValue.setText("KSH " + fmt.format(todaysSales));

        List<SalesItems> allItems = salesItemsDao.getAll();
        int itemsSold = allItems.stream()
            .filter(si -> si.getDate().after(todayStart) && si.getDate().before(todayEnd))
            .mapToInt(SalesItems::getQuantity)
            .sum();
        itemsSoldValue.setText(String.valueOf(itemsSold));

        List<Products> allProducts = productsDao.getAll();
        List<Products> lowStockProducts = allProducts.stream()
            .filter(p -> p.getQuantity() <= p.getReorderLevel())
            .collect(Collectors.toList());
        double lowStockPercent = allProducts.isEmpty() ? 0 : (double) lowStockProducts.size() / allProducts.size() * 100;
        lowStockValue.setText(lowStockProducts.size() + " (" + String.format("%.1f", lowStockPercent) + "%)");

        List<Customers> allCustomers = customerDao.getAll();
        long activeCount = allCustomers.stream()
            .filter(c -> !c.getCustomerId().equalsIgnoreCase("WALKIN"))
            .count();
        activeCustomersValue.setText(String.valueOf(activeCount));

        Map<String, Double> paymentTotals = allSales.stream()
            .collect(Collectors.groupingBy(Sales::getPaymentMethod, Collectors.summingDouble(Sales::getTotalAmount)));
        salesPie.getData().clear();
        paymentTotals.forEach((method, total) -> salesPie.getData().add(new PieChart.Data(method, total)));
        salesPie.setLegendVisible(true);
        salesPie.setLabelsVisible(true);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales (KSH)");
        SimpleDateFormat dayFmt = new SimpleDateFormat("EEE");
        cal = Calendar.getInstance();
        for (int i = 6; i >= 0; i--) {
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, -i);
            Date dayStart = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date dayEnd = cal.getTime();

            double daySales = allSales.stream()
                .filter(s -> s.getSaleDate().after(dayStart) && s.getSaleDate().before(dayEnd))
                .mapToDouble(Sales::getTotalAmount)
                .sum();
            series.getData().add(new XYChart.Data<>(dayFmt.format(dayStart), daySales));
        }
        salesLine.getData().clear();
        salesLine.getData().add(series);

        List<String> recent = allSales.stream()
            .filter(s -> !s.getCustomerId().equalsIgnoreCase("WALKIN"))
            .sorted((a, b) -> b.getSaleDate().compareTo(a.getSaleDate()))
            .limit(5)
            .map(s -> {
                String customer = s.getCustomerId().equals("walk-in") ? "Walk-in" : s.getCustomerId();
                return customer + " — KSH " + fmt.format(s.getTotalAmount()) + " • " + s.getPaymentMethod() + " • " + s.getStatus();
            })
            .collect(Collectors.toList());
        recentTransactionsList.getItems().setAll(recent);

        List<String> low = lowStockProducts.stream()
            .map(p -> p.getProductName() + " (" + p.getCategory() + ") — " + p.getQuantity() + " / " + p.getReorderLevel())
            .collect(Collectors.toList());
        lowStockList.getItems().setAll(low);
    }

    private void loadDemoData() {
        todaysSalesValue.setText("KSH " + fmt.format(125000));
        itemsSoldValue.setText("52");
        lowStockValue.setText("3");
        activeCustomersValue.setText("118");

        salesPie.getData().setAll(
            new PieChart.Data("Retail", 65),
            new PieChart.Data("Wholesale", 20),
            new PieChart.Data("Online", 15)
        );
        salesPie.setLegendVisible(true);
        salesPie.setLabelsVisible(true);

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
