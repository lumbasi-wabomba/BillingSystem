package com.system.billingsystem.dao;

import com.system.billingsystem.models.SalesItems;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesItemsDao implements  Dao<SalesItems> {


    @Override
    public SalesItems get(SalesItems salesItems) throws SQLException {
        String sqlGet = "";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);
            statementGet.setString(1, salesItems.getItemId());
            statementGet.setString(2, salesItems.getProductId());
            statementGet.setString(3, salesItems.getSaleId());
            statementGet.setString(4, salesItems.getProductCode());
            statementGet.setString(5, salesItems.getProductName());
            statementGet.setInt(6, salesItems.getQuantity());
            statementGet.setDouble(7, salesItems.getPrice());
            statementGet.setDouble(8, salesItems.getTotal());
            statementGet.setDate(9, (Date) salesItems.getDate());
            return (SalesItems) statementGet.executeQuery();
        } catch (SQLException e) {
            throw new SQLException("error fetching items" + 3+ 2+ 5, e);
        }
    }

    @Override
    public List<SalesItems> getAll() throws SQLException {
        String sqlGetAll = "";
        List<SalesItems> sales = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allsales = statementGetAll.executeQuery();
            while (allsales.next()) {
                sales.add(new SalesItems());
            } ;
            return sales;

        } catch (SQLException e) {
            throw new SQLException("Error fetching all users", e);
        }
    }

    @Override
    public SalesItems save(SalesItems salesItems) throws SQLException {
        String sqlSave = "";
        try(Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementSave = myConnection.prepareStatement(sqlSave);
            statementSave.setString(1, salesItems.getItemId());
            statementSave.setString(2, salesItems.getProductId());
            statementSave.setString(3, salesItems.getSaleId());
            statementSave.setString(4, salesItems.getProductName());
            statementSave.setString(5, salesItems.getProductCode());
            statementSave.setInt(6, salesItems.getQuantity());
            statementSave.setDouble(7, salesItems.getPrice());
            statementSave.setDouble(8, salesItems.getTotal());
            statementSave.setDate(9, (Date) salesItems.getDate());
            statementSave.executeUpdate();
            return salesItems;
        } catch (SQLException e) {
            throw new SQLException( "error while saving the product"+ 3+ 5, e);
        }
    }

    @Override
    public SalesItems update(SalesItems salesItems, String[] params) throws SQLException {
        String sqlUpdate = "";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
            statementUpdate.setString(3, params[2]);
            statementUpdate.setString(4, params[3]);
            statementUpdate.setString(5, params[4]);
            statementUpdate.setInt(6, Integer.parseInt(params[5]));
            statementUpdate.setDouble(7, Double.parseDouble(params[6]));
            statementUpdate.setDouble(8, Double.parseDouble(params[7]));
            statementUpdate.setDate(9, Date.valueOf(params[8]));
            statementUpdate.executeUpdate(sqlUpdate);
            return salesItems;
        } catch (SQLException e) {
            throw new SQLException("Error updating sales item: " + salesItems.getItemId(), e);
        }
    }

    @Override
    public SalesItems delete(String id) throws SQLException {
        String sqlDelete = "";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementDelete = myConnection.prepareStatement(sqlDelete);
            statementDelete.setString(1, id);
            statementDelete.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error deleting sales item with id: " + id, e);
        }
    }
}
