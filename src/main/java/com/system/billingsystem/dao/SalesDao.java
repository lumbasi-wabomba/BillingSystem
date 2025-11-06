package com.system.billingsystem.dao;

import com.system.billingsystem.models.Sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesDao  implements  Dao<Sales> {

    //get sales by saleId
    @Override
    public Sales get(String saleID) throws SQLException {
        String sqlGet = "SELECT * FROM  sales WHERE sales_saleid = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);
            statementGet.setString(1, saleID);
            ResultSet saleDetails = statementGet.executeQuery();

            while (saleDetails.next()){
                Sales foundSales = new Sales(
                        saleDetails.getString("sales_salesid"),
                        saleDetails.getString("sales_customerid"),
                        saleDetails.getString("sales_salespersonid"),
                        saleDetails.getDouble("sales_totalamount"),
                        saleDetails.getString("sales_status"),
                        saleDetails.getString("sales_paymentmethod"),
                        saleDetails.getDate("sales_saledate")
                );
                return foundSales;
            }
        } catch (SQLException e) {
            //throw new SQLException("Error fetching sales" + 1 + 2 + 3, e);
            e.printStackTrace();
        }
        return null;
    }

    //get all sales
    @Override
    public List<Sales> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM sales";
        List<Sales> sales = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allSales = statementGetAll.executeQuery();
            while (allSales.next()) {
                sales.add(new Sales(
                        allSales.getString("sales_saleid"),
                        allSales.getString("customers_customerid"),
                        allSales.getString("sales_salespersonid"),
                        allSales.getDouble("sales_totalamount"),
                        allSales.getString("sales_status"),
                        allSales.getString("sales_paymentmethod"),
                        allSales.getDate("sales_saledate")
                ));
            };
            return sales;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all sales", e);
        }
    }

    //save sales
    @Override
    public Sales save(Sales sales) throws SQLException {
        String sqlSave = "INSERT INTO sales (sales_salesid,sales_customerid,sales_salespersonid,sales_totalamount,sales_status,sales_paymentMethod, sales_saledate) VALUES (?,?,?,?,?,?,?)";
        try(Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementSave = myConnection.prepareStatement(sqlSave);
            statementSave.setString(1, sales.getSaleId());
            statementSave.setString(2, sales.getCustomerId());
            statementSave.setDate(3, (java.sql.Date) sales.getSaleDate());
            statementSave.setString(4, sales.getSalesPersonId());
            statementSave.setDouble(5, sales.getTotalAmount());
            statementSave.setString(6, sales.getStatus());
            statementSave.setString(7, sales.getPaymentMethod());
            statementSave.executeUpdate();
            return sales;
        } catch (SQLException e) {
           // throw new SQLException("Error saving sales", e);
            e.printStackTrace();
        }
        return  null;
    }

    //update sales
    @Override
    public Sales update(Sales sales, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE sales SET sales_customerid = ?, sales_salespersonid = ?, sales_totalamount = ?, sales_status = ?, sales_paymentmethod = ? WHERE sales_saleid = ?";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
           // statementUpdate.setDate(3, java.sql.Date.valueOf(params[2]));
            statementUpdate.setString(3, params[2]);
            statementUpdate.setDouble(4, Double.parseDouble(params[3]));
            statementUpdate.setString(5, params[3]);
            statementUpdate.setString(6, params[4]);

            statementUpdate.executeUpdate();
            return sales;
        } catch (SQLException e) {
            throw new SQLException("Error updating sales", e);
        }
    }

    //delete sales by saleId
    @Override
    public Sales delete(String id) throws SQLException {
        String sqlDelete = "DELETE FROM sales WHERE sales_salesid = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementDelete = myConnection.prepareStatement(sqlDelete);
            statementDelete.setString(1, id);
            statementDelete.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error deleting sales with ID: " + id, e);
        }
    }
}
