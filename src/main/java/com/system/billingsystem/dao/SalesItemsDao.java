package com.system.billingsystem.dao;

import com.system.billingsystem.models.SalesItems;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesItemsDao implements  Dao<SalesItems> {

    //get a sales item from the DB
    @Override
    public SalesItems get(SalesItems salesItems) throws SQLException {
        String sqlGet = "SELECT * FROM salesitems WHERE salesitems_itemid = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet = myConnection.prepareStatement(sqlGet);
            statementGet.setString(1, salesItems.getItemId());
            ResultSet soldItems = statementGet.executeQuery();

            while(soldItems.next()){
                SalesItems foundItems = new SalesItems(
                        soldItems.getString("salesitems_itemid"),
                        soldItems.getString("salesitems_productid"),
                        soldItems.getString("salesitems_saleid"),
                        soldItems.getString("salesitems_productname"),
                        soldItems.getString("salesitems_productcode"),
                        soldItems.getInt("salesitems_quantity"),
                        soldItems.getDouble("salesitems_price"),
                        soldItems.getDouble("salesitems_total"),
                        soldItems.getDate("salesitems_date")
                );
                return foundItems;
            }
           return null;
        } catch (SQLException e) {
            throw new SQLException("error fetching items" + 3+ 2+ 5, e);
        }
    }

    //get all sales items from the DB
    @Override
    public List<SalesItems> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM salesitems";
        List<SalesItems> sales = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allsales = statementGetAll.executeQuery();
            while (allsales.next()) {
                sales.add(new SalesItems(
                        allsales.getString("salesitems_itemid"),
                        allsales.getString("salesitems_productid"),
                        allsales.getString("salesitems_saleid"),
                        allsales.getString("salesitems_productname"),
                        allsales.getString("salesitems_productcode"),
                        allsales.getInt("salesitems_quantity"),
                        allsales.getDouble("salesitems_price"),
                        allsales.getDouble("salesitems_total"),
                        allsales.getDate("salesitems_date")
                ));
            } ;
            return sales;

        } catch (SQLException e) {
            throw new SQLException("Error fetching all users", e);
        }
    }

    //save sales item to the DB
    @Override
    public SalesItems save(SalesItems salesItems) throws SQLException {
        String sqlSave = "INSERT INTO salesitems (salesitems_itemid,salesitems_productid,salesitems_saleid,salesitems_productname,salesitems_productcode,salesitems_quantity,salesitems_price,salesitems_total,salesitems_date) VALUES (?,?,?,?,?,?,?,?,?)";
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

    //update sales item in the DB
    @Override
    public SalesItems update(SalesItems salesItems, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE salesitems SET  salesitems_productid= ?, salesitems_saleid= ?, salesitems_productname = ?, salesitems_productcode= ?, salesitems_quantity = ?,salesitems_price = ?, salesitems_total = ?,  salesitems_date= ? WHERE salesitems_itemid = ?";
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

    //delete sales item from the DB
    @Override
    public SalesItems delete(String id) throws SQLException {
        String sqlDelete = "DELETE FROM salesitems WHERE salesitems_itemid = ?";
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
