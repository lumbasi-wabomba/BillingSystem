package com.system.billingsystem.dao;

import com.system.billingsystem.models.Customers;
import com.system.billingsystem.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao implements Dao<Customers> {

    public String getCutomerIdByEmail(String email) throws SQLException {
        String sqlGetCustomerId = "";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetCustomerId = myConnection.prepareStatement(sqlGetCustomerId);
            statementGetCustomerId.setString(1, email);
            ResultSet resultSet = statementGetCustomerId.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("customer_id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching customer ID by email: " + email, e);
        }
    }

    @Override
    public Customers get(Customers customers) throws SQLException {
        String sqlGet = "SELECT * FROM customer WHERE customer_email=?";
            try(Connection myConnection = DatabaseConnection.getConnection()){
                PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);
                statementGet.setString(1, customers.getCustomerId());
                statementGet.setString(2, customers.getName());
                statementGet.setString(3, customers.getEmail());
                statementGet.setString(4, customers.getPhoneNumber());
                statementGet.setDate(5, (Date) customers.getDate());
                return (Customers) statementGet.executeQuery();
            } catch (Exception e) {
                throw new SQLException("failed to fetch user", e);
            }
    }

    @Override
    public List<Customers> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM customer";
        List<Customers> customers = new ArrayList<>();
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allCustomers = statementGetAll.executeQuery();
            while (allCustomers.next()){
                customers.add(new Customers());
            }
            return customers;
        } catch (Exception e) {
            throw new SQLException("error while fetching customers", e);
        }
    }

    @Override
    public Customers save(Customers customers) throws SQLException {
        String sqlSave = "INSERT INTO customers (customerID,firstName,lastName,email,phoneNumber,date) VALUES (?,?,?,?,?,?) ";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementSave = myConnection.prepareStatement(sqlSave);
            statementSave.setString(1, customers.getCustomerId());
            statementSave.setString(2, customers.getName());
            statementSave.setString(3, customers.getEmail());
            statementSave.setString(4, customers.getPhoneNumber());
            statementSave.setDate(5, (Date) customers.getDate());
            return (Customers) statementSave.executeQuery();
        } catch (SQLException e) {
            throw new SQLException("Error saving user: " + customers.getCustomerId(), e);
        }
    }

    @Override
    public Customers update(Customers customers, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE customers SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, date = ? WHERE customer_id = ? ";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
            statementUpdate.setString(3, params[2]);
            statementUpdate.setString(4, params[3]);
            statementUpdate.setDate(5, Date.valueOf(params[4]));
            statementUpdate.executeUpdate(sqlUpdate);
        }catch (Exception e) {
            throw new SQLException("Error while updating", e);
        }
        return null;
    }

    @Override
    public Customers delete(String id) throws SQLException {
        String  sqlDelete = "DELETE FROM customers WHERE customer_id = ?";
        try(Connection myConnection  = DatabaseConnection.getConnection()){
            PreparedStatement statementDelete = myConnection.prepareStatement(sqlDelete);
            statementDelete.setString(1, id);
            statementDelete.executeQuery();
        } catch (Exception e) {
            throw  new SQLException("error while deleting the user", e);
        }
        return null;
    }
}
