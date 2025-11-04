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

    //fetches user email and user username from the DB which is to  be used in login
    public String getUserByEmail(String email) throws SQLException {
        String sqlGetUserId = "SELECT user_email, user_username FROM user WHERE user_email = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetUserId = myConnection.prepareStatement(sqlGetUserId);
            statementGetUserId.setString(1, email);
            ResultSet resultSet = statementGetUserId.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("user_email");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching customer ID by email: " + email, e);
        }
    }

    //to fetch user password from the DB to be used for login
    public String getUserPassword(String email) throws SQLException {
        String sqlGetUserPassword = "SELECT user_email, user_password FROM user WHERE user_email = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetUserPassword = myConnection.prepareStatement(sqlGetUserPassword);
            statementGetUserPassword.setString(1, email);
            ResultSet resultSet = statementGetUserPassword.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("user_password");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching user password by email: " + email, e);
        }
    }

    //to get all the customer detail after passing the email to the DB, DB returns all the details
    @Override
    public User get(User user) throws SQLException {
        String sqlGet = "SELECT * FROM  user WHERE user_email = ?";
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGet= myConnection.prepareStatement(sqlGet);

            statementGet.setString(1, user.getEmail());
            ResultSet userDetails = statementGet.executeQuery();

            while (userDetails.next()) {
                User foundDetails = new User(
                        userDetails.getString("user_id"),
                        userDetails.getString("user_username"),
                        userDetails.getString("user_firstname"),
                        userDetails.getString("user_lastname"),
                        userDetails.getString("user_email"),
                        userDetails.getString("user_role"),
                        userDetails.getString("user_password")
                );
                return foundDetails;
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Error fetching user" + 1+ 2, e);
        }
    }
    //fetches all registered users in the DB
    @Override
    public List<User> getAll() throws SQLException {
        String sqlGetAll = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Connection myConnection = DatabaseConnection.getConnection()) {
            PreparedStatement statementGetAll = myConnection.prepareStatement(sqlGetAll);
            ResultSet allUsers = statementGetAll.executeQuery();
            while (allUsers.next()) {
                users.add(new User(
                        allUsers.getString("user_userid"),
                        allUsers.getString("user_username"),
                        allUsers.getString("user_firstname"),
                        allUsers.getString("user_lastname"),
                        allUsers.getString("user_email"),
                        allUsers.getString("user_role"),
                        allUsers.getString("user_password")
                ));
            } ;
            return users;

        } catch (SQLException e) {
            throw new SQLException("Error fetching all users", e);
        }
    }

    //used to post user details to the DB
    @Override
    public User save(User user) throws SQLException {
        String sqlSave = "INSERT INTO user (user_userid,user_username,user_firstname,user_lastname,user_email,user_role,user_password) VALUES (?,?,?,?,?,?,?)";
        try(Connection myConnection = DatabaseConnection.getConnection()){
           PreparedStatement statementSave = myConnection.prepareStatement(sqlSave);

           String[] name = user.getName().split(" ", 2);
           String firstName = name[0];
           String lastName = name[1];

            statementSave.setString(1, user.getUserID());
            statementSave.setString(2, user.getUsername());
            statementSave.setString(3, firstName);
            statementSave.setString(4, lastName);
            statementSave.setString(5, user.getEmail());
            statementSave.setString(6, user.getRole());
        //    statementSave.setDate(7, (Date) user.getDate());
            statementSave.setString(7, user.getPassword());

            statementSave.executeUpdate();
            return user;
        } catch (SQLException e) {
            //throw new SQLException("Error saving user: " + user.getUserID(), e);
            e.printStackTrace();
        }
        return  null;
    }

    //updates the user details on the DB
    @Override
    public User update(User user, String[] params) throws SQLException {
        String sqlUpdate = "UPDATE user SET user_username = ?,User_firstname = ?, user_lastname = ?, user_email = ?, user_role = ?, user_password = ? WHERE user_email = ?";
        try(Connection myConnection = DatabaseConnection.getConnection()){
            PreparedStatement statementUpdate = myConnection.prepareStatement(sqlUpdate);
            statementUpdate.setString(1, params[0]);
            statementUpdate.setString(2, params[1]);
            statementUpdate.setString(3, params[2]);
            statementUpdate.setString(4, params[3]);
            statementUpdate.setString(5, params[4]);
            //statementUpdate.setDate(6, Date.valueOf(params[5]));
            statementUpdate.setString(6, params[5]);
            statementUpdate.setString(7, user.getEmail());

            statementUpdate.executeUpdate();
        }catch (Exception e) {
            throw new SQLException("Error while updating", e);
        }
        return null;
    }

    //deletes the user details from the DB
    @Override
    public User delete(String id) throws SQLException {
        String  sqlDelete = "DELETE FROM user WHERE user_userid = ?";
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
