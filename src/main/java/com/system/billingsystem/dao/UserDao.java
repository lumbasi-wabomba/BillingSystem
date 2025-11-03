package com.system.billingsystem.dao;

import com.system.billingsystem.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


public class UserDao  implements Dao<User> {

    @Override
    public User get(User user) throws SQLException {
        String sqlGet = "";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet= myConnection.prepareStatement(sqlGet);
            statementGet.setString(1, user.getUserID());
            statementGet.setString(2, user.getName());
            statementGet.setString(3, user.getRole());
            statementGet.setString(4, user.getPassword());
            statementGet.setString(5, user.getUsername());
            statementGet.setString(6, user.getEmail());
            statementGet.setDate(7, (Date) user.getDate());
            return (User) statementGet.executeQuery();
        } catch (SQLException e) {
            throw new SQLException("Error fetching user" + 1+ 2, e);
        }
    }
    //error on fetching all users
    @Override
    public List<User> getAll() throws SQLException {
        String sqlGetAll = "";
        List<User> users = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allUsers = statementGetAll.executeQuery();
            while (allUsers.next()) {
                users.add(new User());
            } ;
            return users;

        } catch (SQLException e) {
            throw new SQLException("Error fetching all users", e);
        }
    }

    @Override
    public User save(User user) throws SQLException {
        String sqlSave = "";
        try(Connection myConnection = DatabaseConnection.getConnection()){
           PreparedStatement statementSave = myConnection.prepareStatement(sqlSave);
            statementSave.setString(1, user.getUserID());
            statementSave.setString(2, user.getName());
            statementSave.setString(3, user.getEmail());
            statementSave.setString(4, user.getRole());
            statementSave.setDate(5, (Date) user.getDate());
            statementSave.setString(6, user.getPassword());
            statementSave.setString(7, user.getUsername());
            return (User) statementSave.executeQuery();
        } catch (SQLException e) {
            throw new SQLException("Error saving user: " + user.getUserID(), e);
        }
    }

    @Override
    public User update(User user, String[] params) throws SQLException {
        String sqlUpdate = "";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
            statementUpdate.setString(3, params[2]);
            statementUpdate.setString(4, params[3]);
            statementUpdate.setString(5, params[4]);
            statementUpdate.setDate(5, Date.valueOf(params[5]));
            statementUpdate.setString(6, params[6]);

            statementUpdate.executeUpdate(sqlUpdate);
        }catch (Exception e) {
            throw new SQLException("Error while updating", e);
        }
        return null;
    }

    @Override
    public User delete(String id) throws SQLException {
        String  sqlDelete = "";
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