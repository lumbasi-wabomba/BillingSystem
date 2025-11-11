package com.system.billingsystem.dao;

import com.system.billingsystem.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User> {

    @Override
    public User save(User user) throws SQLException {
        String sql = "INSERT INTO user (user_id, username, first_name, last_name, email, role, created_at, password) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserID());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getRole());

            // Convert java.util.Date to java.sql.Timestamp for datetime
            ps.setTimestamp(7, new Timestamp(user.getCreatedAt().getTime()));
            ps.setString(8, user.getPassword());

            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new SQLException("Error saving user: " + user.getUserID(), e);
        }
    }

    @Override
    public User get(User user) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at"),
                        rs.getString("password")
                );
            }
            return null;
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at"),
                        rs.getString("password")
                ));
            }
        }
        return users;
    }

    @Override
    public User update(User user, String[] params) throws SQLException {
        String sql = "UPDATE user SET username=?, first_name=?, last_name=?, email=?, role=?, created_at=?, password=? WHERE email=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, params[0]);
            ps.setString(2, params[1]);
            ps.setString(3, params[2]);
            ps.setString(4, params[3]);
            ps.setString(5, params[4]);
            ps.setTimestamp(6, Timestamp.valueOf(params[5]));
            ps.setString(7, params[6]);
            ps.setString(8, user.getEmail());

            ps.executeUpdate();
        }
        return null;
    }

    @Override
    public User delete(String id) throws SQLException {
        String sql = "DELETE FROM user WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
        return null;
    }

    public String getUserByEmail(String email) throws SQLException {
        String sql = "SELECT email FROM user WHERE email=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("email") : null;
        }
    }

    public String getUserPassword(String email) throws SQLException {
        String sql = "SELECT password FROM user WHERE email=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("password") : null;
        }
    }
}
