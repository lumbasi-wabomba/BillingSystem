package com.system.billingsystem;



import com.system.billingsystem.dao.*;
import com.system.billingsystem.models.*;
import com.system.billingsystem.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//public class Main extends Application {
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/billingsystem/login.fxml"));
//        Scene scene = new Scene(loader.load(), 560,500);
//        stage.setTitle("Login");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}

public  class Main {
    public static void main(String[] args) {
//        String userID = "U01";
//        String username = "johndoe";
//        String firstName = "John";
//        String lastName = "Doe";
//        String email = "johndoe@gmail.com";
//        String role = "admin";
//        //java.util.Date date = new java.util.Date();
//        String password = "password123";
//        User user = new User(userID, username, firstName, lastName, email, role, password);
//        UserDao userDao = new UserDao();
//        UserService userService = new UserService(userDao);
//        try {
//            userService.saveUser(user);
//            System.out.println("User saved successfully!");
//        } catch (Exception e) {
//            System.out.println("Error saving user: " + e.getMessage());
//        }

//        String customerID = "C01";
//        String firstName = "Jane";
//        String lastName = "Smith";
//        String email = "janesmith@gmail.com";
//        String phoneNumber = "123-456-7890";
//        //java.util.Date date = new java.util.Date();
//
//        Customers mycustomer = new Customers(customerID, firstName, lastName, email, phoneNumber);
//        CustomerDao customerDao = new CustomerDao();
//        try {
//            customerDao.save(mycustomer);
//            System.out.println("Customer saved successfully!");
//        } catch (Exception e) {
//            System.out.println("Error saving customer: " + e.getMessage());
//        }
//        String productID = "P01";
//        String productName = "Laptop";
//        String productCode = "LP1001";
//        double price = 999.99;
//        int quantity = 10;
//        String category = "Electronics";
//        int reorderLevel = 5;
//
//        Products myproduct = new Products(productID, productName, productCode, price, quantity, category, reorderLevel);
//        ProductsDao productsDao = new ProductsDao();
//        try {
//            productsDao.save(myproduct);
//            System.out.println("Product saved successfully!");
//        } catch (Exception e) {
//            System.out.println("Error saving product: " + e.getMessage());
//        }

//        String saleID = "S01";
//        String customerID = "C01";
//        //java.util.Date saleDate = new java.util.Date();
//        String salesPersonID = "U01";
//        double totalAmount = 1500.00;
//        String status = "Completed";
//        String paymentMethod = "Credit Card";
//
//        Sales mySale = new Sales(saleID, customerID, salesPersonID, totalAmount, status, paymentMethod);
//        SalesDao salesDao = new SalesDao();
//        try {
//            salesDao.save(mySale);
//            System.out.println("Sale saved successfully!");
//        } catch (Exception e) {
//            System.out.println("Error saving sale: " + e.getMessage());
//        }

//        String itemId = "SIT01";
//        String productId = "P01";
//        String saleId = "S01";
//        String productName = "Laptop";
//        String productCode = "LP1001";
//        int quantity = 1;
//        double price = 999.99;
//        double total = 999.99;
//        //java.util.Date date = new java.util.Date();
//
//        SalesItems mySales = new SalesItems(itemId, productId, saleId, productName, productCode, quantity, price, total);
//        SalesItemsDao salesItemsDao = new SalesItemsDao();
//        try{
//            salesItemsDao.save(mySales);
//        } catch (Exception e) {
//            System.out.println("Error:" + e.getMessage());
//        }
    }
}
