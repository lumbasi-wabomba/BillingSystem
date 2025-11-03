package com.system.billingsystem.dao;

import com.system.billingsystem.models.Customers;
import com.system.billingsystem.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao implements Dao<Customers> {

    //gets customer by email
    public String getCustomerIdByEmail(String email) throws SQLException {
        String sqlGetCustomerId = "SELECT customers_customerid, customers_email FROM customers WHERE customers_email=?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetCustomerId = myConnection.prepareStatement(sqlGetCustomerId);
            statementGetCustomerId.setString(1, email);
            ResultSet resultSet = statementGetCustomerId.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("customers_email");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching customer by email: " + email, e);
        }
    }

    //gets all customer details after passing to the DB the customer email
    @Override
    public Customers get(Customers customers) throws SQLException {
        String sqlGet = "SELECT * FROM customers WHERE customers_email=?";
            try(Connection myConnection = DatabaseConnection.getConnection()){
                PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);

                statementGet.setString(1, customers.getEmail());
                ResultSet customerDetails = statementGet.executeQuery();

                while (customerDetails.next()){
                    Customers foundDetails = new Customers(
                            customerDetails.getString("customers_customerid"),
                            customerDetails.getString("customers_firstname"),
                            customerDetails.getString("customers_lastname"),
                            customerDetails.getString("customers_email"),
                            customerDetails.getString("customers_phonenumber"),
                            customerDetails.getDate("customers_date")
                    );
                    return foundDetails;
                }
                return null;
            } catch (Exception e) {
                throw new SQLException("failed to fetch user", e);
            }
    }


    //get all customers from the DB
    @Override
    public List<Customers> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM customers";
        List<Customers> customers = new ArrayList<>();
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allCustomers = statementGetAll.executeQuery();
            while (allCustomers.next()){
                customers.add(new Customers(
                        allCustomers.getString("customers_customerid"),
                        allCustomers.getString("customers_firstname"),
                        allCustomers.getString("customers_lastname"),
                        allCustomers.getString("customers_email"),
                        allCustomers.getString("customers_phonenumber"),
                        allCustomers.getDate("customers_date")
                ));
            }
            return customers;
        } catch (Exception e) {
            throw new SQLException("error while fetching customers", e);
        }
    }

    //save customer to the DB
    @Override
    public Customers save(Customers customers) throws SQLException {
        String sqlSave = "INSERT INTO customers (customers_customerid,customers_firstname,customers_lastname,customers_email,customers_phonenumber,customers_date) VALUES (?,?,?,?,?,?) ";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementSave = myConnection.prepareStatement(sqlSave);
            statementSave.setString(1, customers.getCustomerId());
            statementSave.setString(2, customers.getName());
            statementSave.setString(3, customers.getEmail());
            statementSave.setString(4, customers.getPhoneNumber());
            statementSave.setDate(5, (Date) customers.getDate());
            statementSave.executeUpdate();
            return customers;
        } catch (SQLException e) {
            throw new SQLException("Error saving user: " + customers.getCustomerId(), e);
        }
    }

    //update customer details in the DB
    @Override
    public Customers update(Customers customers, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE customers SET customers_firstname = ?, customers_lastname = ?, customers_email = ?, customers_phonenumber = ?, customers_date = ? WHERE customer_email = ? ";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
            statementUpdate.setString(3, params[2]);
            statementUpdate.setString(4, params[3]);
            statementUpdate.setDate(5, Date.valueOf(params[4]));
            statementUpdate.executeUpdate();
        }catch (Exception e) {
            throw new SQLException("Error while updating", e);
        }
        return null;
    }

    //delete customer from the DB
    @Override
    public Customers delete(String id) throws SQLException {
        String  sqlDelete = "DELETE FROM customers WHERE customers_customerid = ?";
        try(Connection myConnection  = DatabaseConnection.getConnection()){
            PreparedStatement statementDelete = myConnection.prepareStatement(sqlDelete);
            statementDelete.setString(1, id);
            statementDelete.executeUpdate();
        } catch (Exception e) {
            throw  new SQLException("error while deleting the user", e);
        }
        return null;
    }
}
