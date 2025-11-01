package com.system.billingsystem.dao;

import com.system.billingsystem.models.Sales;
import com.system.billingsystem.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesDao  implements  Dao<Sales> {

    @Override
    public Sales get(Sales sales) throws SQLException {
        String sqlGet = "";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);
            statementGet.setString(1, sales.getSaleId());
            statementGet.setString(2, sales.getCustomerId());
            statementGet.setDate(3, (java.sql.Date) sales.getSaleDate());
            statementGet.setString(4, sales.getSalesPersonId());
            statementGet.setDouble(5, sales.getTotalAmount());
            statementGet.setString(6, sales.getStatus());
            statementGet.setString(7, sales.getPaymentMethod());
            return (Sales) statementGet.executeQuery();
        } catch (SQLException e) {
            throw new SQLException("Error fetching sales" + 1 + 2 + 3, e);
        }
    }

    @Override
    public List<Sales> getAll() throws SQLException {
        String sqlGetAll = "";
        List<Sales> sales = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allSales = statementGetAll.executeQuery();
            while (allSales.next()) {
                sales.add(new Sales());
            };
            return sales;
        } catch (SQLException e) {
            throw new SQLException("Error fetching all sales", e);
        }
    }

    @Override
    public Sales save(Sales sales) throws SQLException {
        String sqlSave = "";
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
            throw new SQLException("Error saving sales", e);
        }
    }

    @Override
    public Sales update(Sales sales, String[] params) throws SQLException {
        String sqlUpdate = "";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
            statementUpdate.setDate(3, java.sql.Date.valueOf(params[2]));
            statementUpdate.setString(4, params[4]);
            statementUpdate.setDouble(5, Double.parseDouble(params[3]));
            statementUpdate.setString(6, params[5]);
            statementUpdate.setString(7, params[6]);

            statementUpdate.executeUpdate(sqlUpdate);
            return sales;
        } catch (SQLException e) {
            throw new SQLException("Error updating sales", e);
        }
    }

    @Override
    public Sales delete(String id) throws SQLException {
        String sqlDelete = "";
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
